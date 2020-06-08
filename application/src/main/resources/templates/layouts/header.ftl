<#ftl encoding='UTF-8'>
<#import "/org/springframework/web/servlet/view/freemarker/spring.ftl" as spring/>
<#--  https://vorba.ch/2018/spring-boot-freemarker-security-jsp-taglib.html  -->
<#-- <#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/> -->
<#--  <@security.authentication  property="principal.username"/>  -->


<a class="nav-link" href="/"><h1>Hello Simple Project!</h1></a>
<div class="text-right">
    <a href="/logout">
        <span><@spring.messageText "label.logout", "logout" /></span>
    </a>
</div>