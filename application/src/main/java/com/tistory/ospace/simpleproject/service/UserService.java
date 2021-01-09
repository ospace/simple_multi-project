package com.tistory.ospace.simpleproject.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.ospace.base.data.SearchDto;
import com.tistory.ospace.common.util.DataUtils;
import com.tistory.ospace.common.util.StringUtils;
import com.tistory.ospace.simpleproject.exception.SimpleProjectDataIntegrityException;
import com.tistory.ospace.simpleproject.exception.SimpleProjectDuplicateException;
import com.tistory.ospace.simpleproject.repository.AuthorityRepository;
import com.tistory.ospace.simpleproject.repository.UserRepository;
import com.tistory.ospace.simpleproject.repository.dto.AuthorityDto;
import com.tistory.ospace.simpleproject.repository.dto.UserDto;
import com.tistory.ospace.simpleproject.util.SessionUtils;


@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
	@Autowired
	private UserRepository accountRepo;
	
	@Autowired
    private AuthorityRepository authorityRepo;
		
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException, DataAccessException {
        logger.info("loadUserByUsername begin: userId[{}]", userId);
        
        UserDto account = this.accountRepo.findByLoginId(userId);
        if(null == account) {
            throw new UsernameNotFoundException("사용자없음: "+userId);
        }
        
        List<AuthorityDto> authorities = authorityRepo.findAllIn(Arrays.asList(account.getId()));
        account.setAuthorities(authorities);
        
        /* Group Authority에서 추출하는 방법
         select
            g.id,
            g.group_name,
            gr.authority
        from tb_group g, tm_group_user u, tb_group_authority gr
        where u.user_id = 10 and g.id = gr.group_id and g.id = u.group_id
         */
        
        logger.info("loadUserByUsername end: account[{}]", account);
    
        return account;
    }
	
	/**
	 * 사용자 조회
	 * @param usrSrl
	 * @return
	 */
	public UserDto getById(Integer id){
		return  accountRepo.findById(id);
	}
	
	/**
	 * 사용자 삭제
	 * @param user
	 */
	@Transactional
	public void delete(UserDto account) {
		accountRepo.deleteById(account.getId());
	}

	public int count(SearchDto search) {
		return accountRepo.countBy(search, null);
	}

	public List<UserDto> search(SearchDto search) {
		List<UserDto> ret = accountRepo.findAllBy(search, null);
		return ret;
	}
	
	public List<UserDto> searchIn(Collection<Integer> ids) {
		if(DataUtils.isEmpty(ids)) return null;
		return accountRepo.findAllIn(ids);
	}

	public void save(UserDto account) {
		if(StringUtils.isNotEmpty(account.getPassword())) {
			account.setPassword(passwordEncoder.encode(account.getPassword()));
		}
		
		if(account.getId() != null) {
			account.setModifier(SessionUtils.getCurrentUserId());
			account.setLoginId(null);
			accountRepo.update(account);
		} else {
			if(null != accountRepo.findByLoginId(account.getLoginId())) {
				throw new SimpleProjectDuplicateException("아이디중복["+account.getLoginId()+"]");
			}
			
			account.setCreator(SessionUtils.getCurrentUserId());
			accountRepo.insert(account);
		}
	}

	public void deleteById(Integer id) {
		if(null == id) return;
		try {
			accountRepo.deleteById(id);	
		} catch(DataIntegrityViolationException ex) {
			throw new SimpleProjectDataIntegrityException("계정삭제: id["+id+"]", ex);
		}
	}

	public UserDto getByLoginId(String loginId) {
		if(StringUtils.isEmpty(loginId)) return null;
		return accountRepo.findByLoginId(loginId);
	}
}

