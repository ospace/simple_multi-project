<#ftl encoding='UTF-8'>
<#include "/layouts/util.ftl"/>

<#if 0 < state!0>
    <div class="alert alert-danger" role="alert">
        에러(${state}) - ${message}
    </div>
</#if>
<div>
    <form name="listFrm" method="POST">
        <input type="hidden" name="searchKeyword" value="${search.searchKeyword!''}">
        <input type="hidden" name="offset" value="${search.offset!0}">
        <input type="hidden" name="limit" value="${search.limit!10}">
    </form>  
    
    <form name="frm" id="frm" action="/userForm/save" method="POST">
	   <input type="hidden" name="id" value="${(user.id)!}">
    	<table class="table">
    	    <colgroup>
        	    <col width="30%">
        	    <col width="70%">
    	    </colgroup>
        	<tbody>
        	<tr>
        		<th>아이디</th>
        		<td><input type="text" class="form-control" name="loginId"
        		    value="${(user.loginId)!}" ${(user.id)???string('readOnly','')}></td>
    		    </td>
        		    
        	</tr>
        	<tr>
        		<th>패스워드</th>
        		<td><input type="password" class="form-control" name="password" value=""></td>
        	</tr>
        	<tr>
        		<th>패스워드<br>(확인)</th>
        		<td><input type="password" class="form-control" name="password2" value=""></td>
        	</tr>
        
        	<tr>
        		<th>성명</th>
        		<td><input type="text" class="form-control" name="username" value="${(user.username)!''}"></td>
        	</tr>
        	<tr>
        		<th>사용여부</th>
        		<td>
        			<select name="enable" class="form-control">
        				<option value="true" ${(user.enable)???string("selected", "") }>사용</option>
        				<option value="false" ${(user.enable)???string("", "selected") }>미사용</option>
        			</select>
        		</td>
        	</tr>
        	</tbody>
    	</table> 
	</form>
	
	<button type="button" class="btn btn-primary btn-sm" onClick="goList()">목록</button>
	<button type="button" class="btn btn-primary btn-sm" onClick="goSave()">저장</button>
	<#if (user.id)??>
	    <button type="button" class="btn btn-warning btn-sm pull-right" onClick="deleteUser()">삭제</button>
    </#if>
</div>    

<script>

function goList(){
	document.listFrm.action = "/userForm/list";
	document.listFrm.submit();
}

function goSave() {
    if (!validate()) return;
    document.frm.submit();
}

function validate(){
	let frm = document.frm;
	
	if (!frm.loginId.value.trim()){
		alert("아이디를 입력해주세요");
		return false;
	}
	
	if (!frm.id.value) {
    	if (!frm.password.value.trim()) {
    		alert("패스워드를 입력해주세요");
    		return false;
    	}
    }
    	
    if(frm.password.value.trim()) {
    	if (!frm.password2.value.trim()) {
    		alert("확인 패스워드를 입력해주세요");
    		return false;
    	}
    	
    	if (frm.password.value !== frm.password2.value) {
    		alert("패스워드가 일치하지 않습니다.");
    		return false;
    	}
	}
 
	if (!frm.username.value.trim()) {
		alert("이름을 입력해주세요");
		return false;
	}
	
	return true;
}

function deleteUser(){
    if(confirm("삭제하겠습니까?") == false) return;

	document.frm.action = "/userForm/delete";
	document.frm.submit();
}

</script>

