package com.tistory.ospace.simpleproject.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.ospace.base.data.SearchDto;
import com.tistory.ospace.common.util.DataUtils;
import com.tistory.ospace.common.util.StringUtils;
import com.tistory.ospace.simpleproject.exception.SimpleProjectDataIntegrityException;
import com.tistory.ospace.simpleproject.exception.SimpleProjectDuplicateException;
import com.tistory.ospace.simpleproject.repository.UserPropRepository;
import com.tistory.ospace.simpleproject.repository.dto.UserPropDto;
import com.tistory.ospace.simpleproject.util.SessionUtils;


@Service
public class UserPropService{
	
	@Autowired
	private UserPropRepository userRepo;
		
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
	/**
	 * 회원 조회
	 * @param usrSrl
	 * @return
	 */
	public UserPropDto getById(Integer id){
		return  userRepo.findById(id);
	}
	
	/**
	 * 회원 삭제
	 * @param user
	 */
	@Transactional
	public void delete(UserPropDto user) {
		userRepo.deleteById(user.getId());
	}

	public int count(SearchDto search) {
		return userRepo.countBy(search, null);
	}

	public List<UserPropDto> search(SearchDto search) {
		List<UserPropDto> ret = userRepo.findAllBy(search, null);
		return ret;
	}
	
	public List<UserPropDto> searchIn(Collection<Integer> ids) {
		if(DataUtils.isEmpty(ids)) return null;
		return userRepo.findAllIn(ids);
	}

	public void save(UserPropDto user) {
//		if(StringUtils.isNotEmpty(user.getPassword())) {
//			user.setPassword(passwordEncoder.encode(user.getPassword()));
//		}
		
		if(user.getId() != null) {
			user.setModifier(SessionUtils.getCurrentUserId());
			user.setLoginId(null);
			userRepo.update(user);
		} else {
			if(null != userRepo.findByLoginId(user.getLoginId())) {
				throw new SimpleProjectDuplicateException("아이디중복:"+user.getLoginId());
			}
			
			user.setCreator(SessionUtils.getCurrentUserId());
			userRepo.insert(user);
		}
	}

	public void deleteById(Integer id) {
		if(null == id) return;
		try {
			userRepo.deleteById(id);
		} catch(DataIntegrityViolationException ex) {
			throw new SimpleProjectDataIntegrityException("사용자삭제: id["+id+"]", ex);
		}
		
	}

	public UserPropDto getByLoginId(String loginId) {
		if(StringUtils.isEmpty(loginId)) return null;
		return userRepo.findByLoginId(loginId);
	}
}
