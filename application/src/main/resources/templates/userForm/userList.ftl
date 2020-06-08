<#include "/layouts/util.ftl"/>
<#include "/component/pagination.ftl" />

<#assign
    offset=search.offset!0
    limit=search.limit!4
    YN=enums["com.tistory.ospace.simpleproject.util.YN"]
>

<div class="container">

    <form name="frm" action="/accountForm/list" method="POST" class="form-inline">
    	<div class="form-group">
            <input type="text" class="form-control" name="searchKeyword" id="keyword" value="${(search.searchKeyword)!}">
    	    <button type="submit" class="btn btn-primary btn-sm">검색</button>
    	</div>
    	<input type="hidden" name="offset" value="${offset}">
    	<input type="hidden" name="limit" value="${limit}">
    	<input type="hidden" name="id">
    </form>
    
    <div class="pull-right">Total : ${total} [ ${(offset / limit)?ceiling+1} / ${(total/limit)?ceiling} ]</div>
    
    <table class="table table-hover">
    	<colgroup>
    		<col width="5%">
    		<col width="10%">
    		<col width="10%">
    		<col width="10%">
    		<col width="10%">
    		<col width="10%">
    	</colgroup>
    	<thead>
    		<tr>
    			<th class="text-center">번호</th>
    			<th class="text-center">아이디</th>
    			<th class="text-center">사용자이름</th>
    			<th class="text-center">사용여부</th>
    			<th class="text-center">등록/수정자</th>
    			<th class="text-center">등록/수정일</th>
    		</tr>
    	</thead>
    	<tbody>
    		<#list data as each>
    			<tr>
    				<td class="text-center">${(offset)+each_index+1}</td>
    				<td class="text-center"><a href="javascript:goForm(${each.id })">${each.loginId }</a></td>
    				<td class="text-center">${each.username}</td>
    			    <td class="text-center">${(each.enable)?string('사용','미사용')} <#--  ${YN.find(each.useYn)!} --></td>
    				<td class="text-center">${each.modifierName}</td>
    				<td class="text-center">${each.modifyDate}</td>
    			</tr>
    		</#list>
    		<#if !data?has_content>
    			<tr>
    				<td colspan="6" style="text-align: center;">
    				   자료가 없습니다.
    			   </td>
    			</tr>
    		</#if>
    	</tbody>
    </table>
    
    <nav aria-label="Page navigation">
        <@pagination limit=limit offset=offset total=total size=10/>
    </nav>
    
    <button type="submit" class="btn btn-primary btn-sm" onClick="goForm()">등록</button>
</div>

<script>
	function goPage(pageNo) {
		document.frm.action = "/userForm/list";
		document.frm.offset.value = (pageNo-1)*${limit};
		document.frm.submit();
	}

	function goForm(id) {
		document.frm.action = "/userForm/form"
		if(id) document.frm.id.value = id;
		document.frm.submit();
	}
</script>