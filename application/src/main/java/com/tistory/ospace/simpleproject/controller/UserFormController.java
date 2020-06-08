package com.tistory.ospace.simpleproject.controller;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.tistory.ospace.core.util.CmmUtils;
import com.tistory.ospace.core.util.DataUtils;
import com.tistory.ospace.simpleproject.exception.SimpleProjectException;
import com.tistory.ospace.simpleproject.model.Account;
import com.tistory.ospace.simpleproject.model.SearchKeyword;
import com.tistory.ospace.simpleproject.model.User;
import com.tistory.ospace.simpleproject.repository.dto.SearchDto;
import com.tistory.ospace.simpleproject.repository.dto.UserDto;
import com.tistory.ospace.simpleproject.service.CodeService;
import com.tistory.ospace.simpleproject.service.UserService;
import com.tistory.ospace.simpleproject.util.ModelUtils;

import freemarker.template.TemplateModelException;


@Controller
@RequestMapping("/userForm")
public class UserFormController{
	private static final Logger logger = LoggerFactory.getLogger(UserFormController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CodeService codeService;
	

	/**
	 * 사용자 목록
	 * @param user
	 * @param model
	 * @return
	 * @throws TemplateModelException 
	 */
	@RequestMapping(value="/list")
	public String list(@ModelAttribute("search") SearchKeyword search, Model model) {
		logger.debug("userList begin: search[{}]", search);
		long runtime = System.currentTimeMillis();
		
		SearchDto searchDto = ModelUtils.convert(search, new SearchDto());
		List<UserDto> data = userService.search(searchDto);
		int total = userService.count(searchDto);
		
		ModelUtils.mappingAccountName(userService, data);
		DataUtils.iterate(data, it->it.setPassword("[HIDDEN]"));
		
		List<User> ret = DataUtils.map(data, it->ModelUtils.convert(it, new User()));
		model.addAttribute("data", ret);
		model.addAttribute("total", total);
		model.addAttribute("roleMap", codeService.searchRoleMap());
		//model.addAttribute("yn", YN.toMap());
		
		logger.debug("userList end: runtime[{} msec] data[{}]", System.currentTimeMillis()-runtime, CmmUtils.toString(ret));
		
		return "normal:userForm/userList";
	} 

	
	/**
	 * 사용자 등록/수정 폼
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/form")
	public String form(@ModelAttribute("search") SearchKeyword search, Integer id, Model model) {
		logger.info("form begin: search[{}] id[{}]", CmmUtils.toString(search), id);
		
		model.addAttribute("roleList", codeService.searchRole());
		
		User user = null;
		if(null != id){
		    user = ModelUtils.convert(userService.getById(id), new User());
		}
		
		model.addAttribute("user", user);
		
		logger.info("form end: user[{}]", CmmUtils.toString(user));
		
		return "normal:userForm/userForm";
	} 
	
	/**
	 * 사용자 등록/수정
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="/save")
	public RedirectView  save(User user, RedirectAttributes attrs) throws ParseException {
		logger.debug("saveAccount begin: user[{}]", user);
		long runtime = System.currentTimeMillis();
		
		boolean error = false;
		try {
		    userService.save(ModelUtils.convert(user, new UserDto()));
		} catch (SimpleProjectException ex) {
		    attrs.addFlashAttribute("state", ex.status);
		    attrs.addFlashAttribute("message", ex.getMessage());
		    error = true;
		}
		
		attrs.addFlashAttribute("user", user);
		
		logger.debug("saveAccount end: runtime[{} msec]", System.currentTimeMillis()-runtime);
		
		return new RedirectView(error ? "/userForm/form" : "/userForm/list");
	} 

	/**
	 * 사용자 삭제
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="/delete")
	public String delete(Account user) {
		logger.debug("delete begin: user[{}]", user);
		long runtime = System.currentTimeMillis();
		
		try {
			userService.deleteById(user.getId());
		} catch (Exception e) {
			logger.warn("delete failed: loginId[{}]", user.getLoginId(), e);
		}
		
		logger.debug("delete end: runtime[{} msec]", System.currentTimeMillis()-runtime);
		
		return "redirect:/userForm/list";
	} 
}
