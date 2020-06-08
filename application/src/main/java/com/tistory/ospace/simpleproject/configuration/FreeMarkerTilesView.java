package com.tistory.ospace.simpleproject.configuration;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.tiles3.TilesView;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;

public class FreeMarkerTilesView extends TilesView {
    public static final String SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE = "springMacroRequestContext";
    public static final String JAVA_ENUM_CONTEXT_ATTRIBUTE = "enums";
    public static final String JAVA_STATICS_CONTEXT_ATTRIBUTE = "statics";
    
    private boolean exposeSpringMacroHelpers = true;
    
    public FreeMarkerTilesView() {
        this.setExposeJstlAttributes(false);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    
        if (this.exposeSpringMacroHelpers) {
            if (model.containsKey(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE)) {
                throw new ServletException("Cannot expose bind macro helper '" +SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE
                        +"' because of an existing model object of the same name");
            }
            model.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, new RequestContext(request, response, getServletContext(), model));
        }
        
        BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
        
        if (!model.containsKey(JAVA_ENUM_CONTEXT_ATTRIBUTE)) {
            model.put(JAVA_ENUM_CONTEXT_ATTRIBUTE, beansWrapper.getEnumModels());
        }
        
        if (!model.containsKey(JAVA_STATICS_CONTEXT_ATTRIBUTE)) {
            model.put(JAVA_STATICS_CONTEXT_ATTRIBUTE, beansWrapper.getStaticModels());
        }
        
        // 강제로 설정함. 그렇지 않으면 중단에 render에서 의해서 getWriter()호출로 encoding 설정시 반영이 안됨. 
        // response.setCharacterEncoding("UTF-8");
        
        super.renderMergedOutputModel(model, request, response);
    }
}

