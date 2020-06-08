package com.tistory.ospace.simpleproject.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.freemarker.TilesSharedVariableFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.freemarker.render.FreemarkerRenderer;
import org.apache.tiles.request.freemarker.render.FreemarkerRendererBuilder;
import org.apache.tiles.request.freemarker.servlet.SharedVariableLoaderFreemarkerServlet;
import org.apache.tiles.request.render.BasicRendererFactory;
import org.apache.tiles.startup.DefaultTilesInitializer;

public class FreeMarkerTilesInitializer extends DefaultTilesInitializer {
    private String tileDefinitionPath = "classpath:/tiles-*.xml";
    private String tempaltePath = "resource:/templates/";
    private String noCache = "true";
    private String contentType = "text/html;charset=UTF-8";
    private String templateUpdateDelay = "0";
    private String defaultEncoding = "UTF-8";
    private String numberFormat = "0.##########";
    
    public void setTemplatePath(String tileDefinitionPath) {
        this.tileDefinitionPath = tileDefinitionPath;
    }
    
    public void setTempaltePath(String tempaltePath) {
        this.tempaltePath = tempaltePath;
    }

    public void setTileDefinitionPath(String tileDefinitionPath) {
        this.tileDefinitionPath = tileDefinitionPath;
    }

    public void setNoCache(String noCache) {
        this.noCache = noCache;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setTemplateUpdateDelay(String templateUpdateDelay) {
        this.templateUpdateDelay = templateUpdateDelay;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
        return new BasicTilesContainerFactory() {
            @Override
            protected List<ApplicationResource> getSources(ApplicationContext applicationContext) {
                List<ApplicationResource> retValue = new ArrayList<ApplicationResource>(1);
                retValue.add(applicationContext.getResource(tileDefinitionPath));
                return retValue;
            }

            @Override
            protected void registerAttributeRenderers(final BasicRendererFactory rendererFactory,
                    final ApplicationContext applicationContext, final TilesContainer container,
                    final AttributeEvaluatorFactory attributeEvaluatorFactory) {

                super.registerAttributeRenderers(rendererFactory, applicationContext, container, attributeEvaluatorFactory);

                FreemarkerRenderer freemarkerRenderer = FreemarkerRendererBuilder.createInstance()
                        .setApplicationContext(applicationContext)
                        .setParameter("TemplatePath", tempaltePath)
                        .setParameter("NoCache", noCache)
                        .setParameter("ContentType", contentType)
                        .setParameter("templateUpdateDelay", templateUpdateDelay)
                        .setParameter("defaultEncoding", defaultEncoding)
                        .setParameter("numberFormat", numberFormat)
                        // force로 해줘도 타이밍상 변경이 불가능하기 때문에 적용안됨
//                      .setParameter("ResponseCharacterEncoding", "force UTF-8") //"legacy", "fromTemplate", "doNotSet", "force ${charsetName}"
                        .setParameter(SharedVariableLoaderFreemarkerServlet.CUSTOM_SHARED_VARIABLE_FACTORIES_INIT_PARAM,
                                "tiles," + TilesSharedVariableFactory.class.getName()) //"name1,class name1;name2, class name2" 형태로 추가
                        .build();
                rendererFactory.registerRenderer("freemarker", freemarkerRenderer);
            }
        };
    }
}
