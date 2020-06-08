<#import "/org/springframework/web/servlet/view/freemarker/spring.ftl" as spring/>
<#assign cmmUtils=statics['com.tistory.ospace.core.util.CmmUtils']/>

<#macro json_string object default="">
<@compress single_line=true> 
    <#if object?? && object?has_content>
        ${cmmUtils.toString(object)}
    <#else>
        ${default!}
    </#if>
    <#return>
</@compress>
</#macro>