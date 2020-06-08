<#ftl encoding='UTF-8'>
<#include "/layouts/util.ftl"/>

<div class="contant" id="app" v-cloak>
    <div class="form-group">
        <table class="table">
            <colgroup>
                <col style="width:150px">
                <col style="width:auto">
            </colgroup>
            <tbody>
                <tr>
                    <th scope="row">아이디</th>
                    <td>
                        <input type="text" class="form-control" title="아이디 입력"
                            ref="login" v-model="user.loginId"
                            autocapitalize="off" :disabled="0<user.id">
                        <p class="msg" v-if="errors.login && errors.login.length">아이디를 입력해 주세요</p>
                    </td>
                </tr>
                <tr>
                    <th scope="row">이름</th>
                    <td>
                        <input type="text" class="form-control" title="이름 입력"
                            ref="username" v-model="user.username" autocapitalize="off">
                        <p class="msg" v-if="errors.username && errors.username.length">2~50자의 한글, 영문, 숫자를 사용하세요.</p>
                    </td>
                </tr>
                <tr>
                    <th scope="row">비밀번호</th>
                    <td>
                        <input type="password" class="form-control" title="비밀번호 입력"
                            ref="passwd" v-model="user.password"
                            autocomplete="new-password">
                        <p class="msg" v-if="errors.passwd && errors.passwd.length">비밀번호를 입력해 주세요 (6~10자리)</p>
                    </td>
                 </tr>
                 <tr>
                    <th scope="row">비밀번호 확인</th>
                    <td>
                        <input type="password" class="form-control" title="비밀번호 재입력"
                            ref="passwd2" v-model="user.password2">
                        <p class="msg" v-if="errors.passwd2 && errors.passwd2.length">비밀번호가 일치하지 않습니다.</p>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!--// rowTable -->
    <!-- btnRight -->
    <div class="btnRight">
        <button class="btn btn-primary btn-sm" :key="'S'+keyNo" @click.once="onSave">저장</button>
        <button class="btn btn-primary btn-sm" @click.prevent.self="onGoList">목록</button>
    </div>
    <!--// btnRight -->
</div>

<script>
var app = new Vue({
    el: '#app',
    data: {
    	searchKeyword: <@json_string search!"" "{}"/>,
    	user: <@json_string user!"" "{loginId:''}"/>,
    	keyNo: 1,
    	errors: {}
    },
    created: function() {
    	//let parsed = util.isUndef(this.user.birthday) || this.user.birthday.split('-');
        //this.user.parsedBirthday = 3===parsed.length ? parsed : ['','01',''];
    },
    methods: {
    	onGoList: function() {
    		util.movePage('/userVue/list', this.searchKeyword);
    	},
    	onSave: function() {
    		let self = this;
    		if(!this.checkValid()) {
    			++self.keyNo;
    			return;
    		}
    		let dataRq = {
    			id: this.user.id,
    			loginId: this.user.loginId,
    			password: this.user.password,
    			username: this.user.username,
    			email: this.user.email,
    			//gender: this.user.gender,
    			//birthday: this.user.parsedBirthday.join('-')
    		};
    		
    		util.postJson('/userVue.json', dataRq)
    		.then(async function(res) {
   				await alert("저장 되었습니다");
    			self.onGoList();
    		})
    		.catch(async function(err) {
    		    if(500 !== err.status) return;
    			let res = err.responseJSON;
				if(1002 == res.status) {
                    await alert('등록된 아이디가 있습니다. 다시등록해주시기 바랍니다.');
                } else {
                    await alert(`등록중에 에러가 발생했습니다(${r"${res.status}"}): ${r"${res.message}"}`);
                }
    			
    			++self.keyNo;
    		});
    	},
    	checkValid: function() {
    		this.errors = {};
    		let refs = this.$refs;
    		let user = this.user;
    		let errors = this.errors;
    		let ret = true;
    		
    		if(!user.loginId) {
    			errors.login = '아이디를 입력해 주세요.';
    			//refs.login.focus();
    			ret = false;
    		}
    		
    		if(!user.id) {
	    		if(!user.password || user.password.length < 6 || user.password.length > 10) {
	                errors.passwd = '8~30자의 영문 대/소문자, 숫자, 특수문자를 사용하세요.';
	                //refs.passwd.focus();
	                ret = false;
	            }
	    		
	    		if(user.password != user.password2) {
	                errors.passwd2 = '비밀번호가 일치하지 않습니다.';
	                //refs.passwd2.focus();
	                ret = false;
	            }
    		}
    		
    		if(!user.username || 2>user.username.length || 50 < user.username.length) {
                errors.username = '2~50자의 한글, 영문, 숫자를 사용하세요.';
                //refs.username.focus();
                ret = false;
            }
            
    		return ret;
    	},
        onDelete: async function(id) {
            let self = this;
            
            if(!await confirm('회원 정보를 삭제하시겠습니까?')) {
            	++self.keyNo;
            	return;
            }
            
            util.deleteJson(`/userVue/${r"${id}"}`)
            .then(function() {
            	self.searchKeyword.offset = 0;
                self.onGoList();
            })
            .catch(async function(err) {
                if(500 !== err.status) return;
                let res = err.responseJSON; 
                await alert(`삭제실패(${r"${res.status}"}): ${r"${res.message}"}`);
            	++self.keyNo;
            });
        }
    }
});
</script>

