<#ftl encoding='UTF-8'>

<#--pagination.ftl-->
<#macro pagination offset limit total size>

<#assign
    pageNo = (offset/limit)?floor + 1
    pageIndex = ((pageNo-1)/size)?floor
    beginPage =  pageIndex * size + 1
    totalPage = (total/limit)?ceiling
    endPage = [(pageIndex+1)*size, totalPage]?min
>

<ul class="pagination justify-content-center">
    <#if 1 == beginPage>
        <li class='page-item disabled'><a class='page-link' href='#'>First</a></li>
        <li class='page-item disabled'><a class='page-link' href='#'>Prev</a></li>
    <#else>
        <li class='page-item'><a href='javascript:goPage(1)' class='page-link'>First </a></li>
        <li class='page-item'><a href='javascript:goPage(${beginPage-1})' class='page-link'>Prev</a></li>
    </#if>
    
    <#list beginPage..endPage as page>
        <#if pageNo == page>
            <li class='page-item active'><a href='#' class='page-link' >${page}<span class='sr-only'>(current)</span></a></li>
        <#else>        
            <li class='page-item'><a href='javascript:goPage(${page})' class='page-link'>${page}</a></li>
        </#if>
    </#list>
    
    <#if endPage == totalPage>
        <li class='page-item disabled'><a class='page-link' href='#'>Next</a></li>
        <li class='page-item disabled'><a class='page-link' href='#'>Last </a></li>
    <#else>
        <li class='page-item'><a href='javascript:goPage(${endPage+1})' class='page-link'>Next</a></li>
        <li class='page-item'><a href='javascript:goPage(${totalPage})' class='page-link'>Last </a></li>
    </#if>
</ul>
 
</#macro>