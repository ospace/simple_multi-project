package com.tistory.ospace.simpleproject.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tistory.ospace.core.util.CmmUtils;
import com.tistory.ospace.core.util.DataUtils;
import com.tistory.ospace.simpleproject.model.ListRS;
import com.tistory.ospace.simpleproject.model.SearchKeyword;
import com.tistory.ospace.simpleproject.model.User;
import com.tistory.ospace.simpleproject.repository.dto.SearchDto;
import com.tistory.ospace.simpleproject.repository.dto.UserDto;
import com.tistory.ospace.simpleproject.service.UserService;
import com.tistory.ospace.simpleproject.util.ModelUtils;

import freemarker.template.TemplateModelException;


@Validated
@Controller
@RequestMapping("/userVue")
public class UserVueController{
	private static final Logger logger = LoggerFactory.getLogger(UserVueController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;
	
//	@Autowired
//	private CodeService codeService;
	

	/**
	 * 사용자 목록
	 * @param user
	 * @param model
	 * @return
	 * @throws TemplateModelException 
	 */
	@RequestMapping(value="/list")
	public String list(@ModelAttribute("search") SearchKeyword search, Model model) {
		logger.debug("list begin: search[{}]", search);
		
		logger.debug("list end:");
		
		return "normal:userVue/userList";
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
		
		//model.addAttribute("roleList", codeService.searchRole());
		
		User user = null;
		if(null != id){
		    user = ModelUtils.convert(userService.getById(id), new User());
		}
		
		model.addAttribute("user", user);
		
		logger.info("form end: user[{}]", CmmUtils.toString(user));
		
		return "normal:userVue/userForm";
	} 
	
	//------------------------------- API -------------------------------------
    /**
     * 사용자 검색
     * @param code
     * @return
     * @throws  
     */
    
    //@GetMapping(value="/search")
    @RequestMapping(value="/search")
//	@RequestMapping(value="/search",
//	        consumes= {"application/json", "application/xml"},
//	        produces = { "application/json", "application/xml" },
//	        method= {RequestMethod.GET}
//	)
    public @ResponseBody ListRS<?> search(SearchKeyword searchKeyword) {
        logger.info("search begin: searchKeyword[{}]", CmmUtils.toString(searchKeyword));
        long runtime = System.currentTimeMillis();
        
        SearchDto search = ModelUtils.convert(searchKeyword, new SearchDto());
        
        List<UserDto> res = userService.search(search);
        Integer total = userService.count(search);
        
        ModelUtils.mappingAccountName(userService, res);
        List<User> data = DataUtils.map(res, it->ModelUtils.convert(it, new User()));
                
        ListRS<?> ret = ListRS.of(data, total);
        
        String notNull = messageSource.getMessage("validation.NotNull", null, LocaleContextHolder.getLocale());
        
        logger.info("search end: runtime[{} msec] notNull[{}]", System.currentTimeMillis()-runtime, notNull);
        return ret;
    }
    
	/**
	 * 사용자 저장
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
    @ResponseBody
    @PostMapping
	public void  save(@Valid @RequestBody User user) {
        User userClone = (User)user.clone();
        userClone.setPassword("[Hidden]");
		logger.debug("save begin: user[{}]", CmmUtils.toString(userClone));
		
	    userService.save(ModelUtils.convert(user, new UserDto()));
		
		logger.debug("save end:");
	} 

	/**
	 * 사용자 삭제. Min은 @Validated 가 있어야 적용됨. 
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
    @ResponseBody
    @DeleteMapping(value="/{id}")
	public void delete(@Min(0) @PathVariable("id") Integer id) {
		logger.debug("delete begin: id[{}]", id);
		
		userService.deleteById(id);
		
		logger.debug("delete end:");
	} 
}
