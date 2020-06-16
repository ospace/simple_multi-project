package com.tistory.ospace.simpleproject.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tistory.ospace.base.data.SearchDto;
import com.tistory.ospace.core.util.DataUtils;
import com.tistory.ospace.core.util.FileUtils;
import com.tistory.ospace.core.util.StringUtils;
import com.tistory.ospace.simpleproject.exception.SimpleProjectDataIntegrityException;
import com.tistory.ospace.simpleproject.repository.FileRepository;
import com.tistory.ospace.simpleproject.repository.dto.FileDto;
import com.tistory.ospace.simpleproject.util.DateHelper;
import com.tistory.ospace.simpleproject.util.SessionUtils;


@Service
public class FileService {
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);
	
	@Value("${file.prefix:''}") 
	private String prefix;
	
	@Value("${file.store-path}") 
	private String storePath;
	
	@Autowired
	private FileRepository fileRepo;
	
	@Transactional
	public FileDto upload(MultipartFile mf) {
		if (null == mf || mf.isEmpty()) return null;
		
		String origFilename = mf.getOriginalFilename(); //금칙어 체크 필요(.., /, \)
		
		if(origFilename.indexOf("..") != -1) {
			throw new RuntimeException("금칙어사용: 사용금지('..')");
		}
		
		String fileExt = FileUtils.getExtension(origFilename);
		switch(fileExt) {
		case "jsp": case "ftl":
		    fileExt = "txt";
		    break;
		}
		
		String filename = createFilepath(fileExt);
		
		try {
			saveFile(mf.getInputStream(), filename);
		} catch(Exception e) {
			throw new RuntimeException(origFilename + " upload failed: ", e);
		}

		FileDto file = new  FileDto();
		
		file.setFilename(filename);
		file.setOriginalFilename(origFilename);
		file.setSize(mf.getSize());
		file.setExtension(fileExt);
		file.setType(mf.getContentType());
		
		save(file);
		
		return file;
	}
	
	public List<FileDto> search(SearchDto search) {
		List<FileDto> ret = fileRepo.findAllBy(search, null);
		
		return ret;
	}
	
	public int count(SearchDto search) {
		Integer res = fileRepo.countBy(search, null);
		return null == res ? 0 : res; 
	}
	
	public FileDto readFile(Integer id, HttpServletResponse res) throws IOException {
		if(null == id) return null;
		
		FileDto file = getById(id);
		if(null == file) return null;
		
		File f =  new File(getStorePath(file.getFilename()));
		
		res.setContentType(file.getType());
		res.setContentLength((int)f.length());
		
		FileUtils.copy(f, res.getOutputStream());
		
		if(f.length() != file.getSize()) {
			logger.warn("file size is different: filename[{}] original[{}] saved[{}]", file.getFilename(), file.getSize(), f.length());
		}
		
		return file;
	}
	
	public FileDto readFile(String filename, HttpServletResponse res) throws IOException {
		if(StringUtils.isEmpty(filename)) return null;
		
		Path path =  Paths.get(getStorePath(filename));
	    String type = Files.probeContentType(path);
	    if(StringUtils.isEmpty(type)) {
	    	throw new FileNotFoundException(path.toString());
	    }
	    
		res.setContentType(type);
		
		File f = path.toFile();
		res.setContentLength((int)f.length());
		
		FileUtils.copy(f, res.getOutputStream());
		
		FileDto ret = new FileDto();
		ret.setOriginalFilename(filename);
		ret.setType(type);
		ret.setSize(f.length());
		
		logger.debug("file[{}]", ret);
		
		return ret;
	}
	
	public FileDto getById(Integer id) {
		if(null == id) return null;
		return fileRepo.findById(id);
	}
	
	@Transactional
	public FileDto save(FileDto file) {
		
		file.setCreator(SessionUtils.getCurrentUserId());
		fileRepo.insert(file);
		
		return file;
	}
	
	@Transactional
	public FileDto deleteById(Integer id) {
		if(null == id) return null;
		
		FileDto file = getById(id);
		if(null == file) return null;
		
		String filepath = getStorePath(file.getFilename());
		FileUtils.deleteFile(filepath);
		
		try {
			fileRepo.deleteById(id);
		} catch(DataIntegrityViolationException ex) {
			throw new SimpleProjectDataIntegrityException("파일삭제: id["+id+"]", ex);
		}
		
		return file;
	}
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 * @param file
	 * @throws IOException 
	 * @throws UnsupportedEncodingException
	 */
	public FileDto download(Integer id, HttpServletRequest req, HttpServletResponse res) throws IOException {
		if(null == id) return null;
		
		FileDto file = getById(id);
		if(null == file) return null;
		
		String filename = file.getFilename();
		if(filename.indexOf("..") != -1) return null;
		
		String origFilename =  file.getOriginalFilename();
		if(origFilename.indexOf("..") != -1) return null;
		
		File f =  new File(getStorePath(filename));
		
		String downloadFilename = encodeFilename(getBrowserType(req), origFilename);
        res.setContentType("application/octet-stream");
        //res.setContentType("application/x-msdownload");
        res.setHeader("Content-Disposition", "attachment; filename=" +  downloadFilename);
        res.setHeader("Content-Transfer-Encoding", "binary");
        res.setHeader("Content-Length", "" + f.length() );
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Expires", "0");
        
		FileUtils.copy(f, res.getOutputStream());
		
		return file;
	}
	
	protected String createFilepath(String ext) {
		StringBuilder sb = new StringBuilder();
		String dateStr = DateHelper.toStringDateShort2(LocalDateTime.now());
		
		sb.append('/').append(dateStr)
		  .append('/').append(prefix).append(StringUtils.newId());
		
		if(null != ext) {
			sb.append('.').append(ext);
		}
		
		return sb.toString();	
	}
	
	protected String getStorePath(String filename) {
		StringBuilder sb = new StringBuilder();
		sb.append(storePath).append('/').append(filename);
		
		return sb.toString();
	}
	
	protected void saveFile(InputStream in, String filename) throws Exception {
		String filepath = getStorePath(filename); //getFilepath(filename, true);
		
		Path path = Paths.get(filepath);
		FileUtils.mkdirs(path.getParent().toString());
		
		try {
			FileUtils.copy(in, path);
		} catch (Exception e) {
			FileUtils.deleteFile(filepath);
			throw e;
		}
	}
	
//	protected long readFile(String filename, OutputStream out) throws IOException {
//		Path path =  Paths.get(getStorePath(filename));
//		File file = path.toFile();
//		FileUtils.copy(file, out);
//		
//		return file.length();
//	}
	
	protected static String encodeFilename(String browser, String filename) {
		try {
			switch(browser) {
			case "MSIE":
				return URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
			case "Firefox": case "Opera":
				return "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
			case "Chrome": {
				StringBuffer sb = new StringBuffer();
	            char c = 0;
	            int len = filename.length();
	            for (int i = 0; i < len; i++) {
	                c = filename.charAt(i);
	                if (c > '~') {
	                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
	                } else {
	                    sb.append(c);
	                }
	            }
	            return sb.toString();
				}
			default:
				break;
			}
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException("encoding failed: " + filename);
		}
		
		throw new RuntimeException("Not supported browser: " + browser);
	}
	
	//ref: http://mkil.tistory.com/223
	protected static String getBrowserType(HttpServletRequest req) {
        String header = req.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        } else if (header.indexOf("Trident/") > -1){ //IE8~
            //IE 버전 별 체크 >> Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8)
            return "MSIE";
        }
        return "Firefox";
    }

	public List<FileDto> searchIn(Collection<Integer> ids) {
		if(DataUtils.isEmpty(ids)) return null;
		return fileRepo.findAllIn(ids);
	}
}
