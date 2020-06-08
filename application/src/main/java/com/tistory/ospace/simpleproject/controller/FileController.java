package com.tistory.ospace.simpleproject.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tistory.ospace.core.util.CmmUtils;
import com.tistory.ospace.core.util.DataUtils;
import com.tistory.ospace.simpleproject.model.FileInfo;
import com.tistory.ospace.simpleproject.model.ListRS;
import com.tistory.ospace.simpleproject.model.SearchKeyword;
import com.tistory.ospace.simpleproject.repository.dto.FileDto;
import com.tistory.ospace.simpleproject.repository.dto.SearchDto;
import com.tistory.ospace.simpleproject.service.FileService;
import com.tistory.ospace.simpleproject.util.ModelUtils;

import freemarker.template.TemplateModelException;

@Validated
@Controller
@RequestMapping("/file")
public class FileController{
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private FileService fileService;
	
//	@Autowired
//    private UserService userService;
	
//	@Autowired
//	private CodeService codeService;
	

	/**
	 * 파일 목록
	 * @param file
	 * @param model
	 * @return
	 * @throws TemplateModelException 
	 */
	@RequestMapping(value="/list")
	public String list(@ModelAttribute("search") SearchKeyword search, Model model) {
		logger.debug("list begin: search[{}]", search);
		
		logger.debug("list end:");
		
		return "normal:file/fileList";
	} 

	
	/**
	 * 파일 등록/수정 폼
	 * @param file
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/form")
	public String form(@ModelAttribute("search") SearchKeyword search, Integer id, Model model) {
		logger.info("form begin: search[{}] id[{}]", CmmUtils.toString(search), id);
		
		//model.addAttribute("roleList", codeService.searchRole());
		
		FileInfo file = null;
		if(null != id){
		    file = ModelUtils.convert(fileService.getById(id), new FileInfo());
		}
		model.addAttribute("file", file);
		
		logger.info("form end: file[{}]", CmmUtils.toString(file));
		
		return "normal:file/fileForm";
	} 
	
	//------------------------------- API -------------------------------------
    /**
     * 파일 검색
     * @param code
     * @return
     * @throws  
     */
    @ResponseBody
    @RequestMapping(value="/search")
    public ListRS<?> search(SearchKeyword searchKeyword) {
        logger.info("search begin: searchKeyword[{}]", searchKeyword);
        long runtime = System.currentTimeMillis();
        
        SearchDto search = ModelUtils.convert(searchKeyword, new SearchDto());
        List<FileDto> res = fileService.search(search);
        int total = fileService.count(search);
        
        List<FileInfo> data = DataUtils.map(res, it->ModelUtils.convert(it, new FileInfo()));
        ListRS<?> ret = ListRS.of(data, total);
        
        logger.info("search end: runtime[{} msec] response[{}]", System.currentTimeMillis()-runtime, ret);
        return ret;
    }
    
    /**
     * 파일 조회
     * @throws IOException 
     * @throws FileNotFoundException     * 
     * @param code
     * @return
     * @throws  
     */
    //@ResponseBody
    @GetMapping(value="/{id}")
    public void select(@Min(0) @PathVariable("id") String id, HttpServletResponse res) throws IOException {
        logger.debug("select begin: id[{}]", id);
        long runtime = System.currentTimeMillis();
        
        Integer numId = CmmUtils.parseInt(id);
        FileDto ret = null==numId ? fileService.readFile(id, res) : fileService.readFile(numId, res);
        
        logger.debug("select end: response[{}] runtime[{} msec]", ret, System.currentTimeMillis()-runtime);
    }
    
    @GetMapping(value="/{path}/{file}")
    public void select2(@NotEmpty @PathVariable("path") String path, @NotEmpty @PathVariable("file") String file, HttpServletResponse res) throws IOException {
        logger.debug("select2 begin: path[{}] file[{}]", path, file);
        long runtime = System.currentTimeMillis();
        
        FileDto ret = fileService.readFile(path+"/"+file, res);
        
        logger.debug("select2 end: response[{}] runtime[{} msec]", ret, System.currentTimeMillis()-runtime);
    }
    
    @GetMapping(value="/{path1}/{path2}/{file}")
    public void select3(@NotEmpty  @PathVariable("path1") String path1, @NotEmpty  @PathVariable("path2") String path2, @NotEmpty @PathVariable("file") String file, HttpServletResponse res) throws IOException {
        logger.debug("select3 begin: path1[{}] path2[{}] file[{}]", path1, path2, file);
        long runtime = System.currentTimeMillis();
        
        FileDto ret = fileService.readFile(path1+"/"+path2+"/"+file, res);
        
        logger.debug("select3 end: file[{}] runtime[{} msec]", ret, System.currentTimeMillis()-runtime);
    }
    
    /**
     * 파일 등록
     * @param file
     * @return
     * @throws  
     */
    @ResponseBody
    @PostMapping
    @Transactional
    public void save(@RequestParam("file") List<MultipartFile> files) throws ParseException {
        logger.debug("save begin: files[{}]", files);
        long runtime = System.currentTimeMillis();
        
        List<FileDto> res = DataUtils.map(files, file->{
            return fileService.upload(file);
        });
        
        logger.debug("save end: file[{}] runtime[{} msec]", res, System.currentTimeMillis()-runtime);
    }

    /**
     * 파일 삭제
     * @param code
     * @return
     * @throws ParseException 
     */
    @ResponseBody
    @DeleteMapping(value="/{id}")
    public void delete(@Min(0) @PathVariable("id") Integer id) {
        logger.debug("delete begin: id[{}]", id);
        long runtime = System.currentTimeMillis();
        
        FileDto file = fileService.deleteById(id);
        
        logger.debug("delete end: file[{}] runtime[{} msec]", file, System.currentTimeMillis()-runtime);
    } 
}
