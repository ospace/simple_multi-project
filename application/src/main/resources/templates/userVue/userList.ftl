<#ftl encoding='UTF-8'>
<#include "/layouts/util.ftl"/>

<div class="container" id="app" v-cloak>
    
    <div class="form-inline">
        <div class="form-group">
            <input type="text" class="form-control" title="검색어 입력" placeholder="검색어 입력" v-model="searchKeyword">
            <button class="btn btn-primary btn-sm" @click.prevent.self="onSearch">검색</button>
        </div>
    </div>

    <div class="pull-right">Total : {{total}} [ {{pageNo}} / {{totalPage}} ]</div>

    <table class="table table-hover">
        <colgroup>
            <col style="width:60px">
            <col style="width:auto">
            <col style="width:150px">
            <col style="width:auto">
            <col style="width:auto">
            <col style="width:120px">
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
            <tr v-for="(each, index) in users" v-bind:key="each.id" @click.prevent="onUpdate(each.id)">
                <td class="text-center">{{offset + index + 1}}</td>
                <td class="text-center">{{each.loginId}}</td>
                <td class="text-center">{{each.username}}</td>
                <td class="text-center">{{each.enable | enableName}}</td>
                <td class="text-center">{{each.modifierName}}</td>
                <td class="text-center">{{each.modifyDate}}</td>
            </tr>
            <tr v-if="users&&!users.length">
                <td colspan="6" style="text-align: center;">
                    자료가 없습니다.
                </td>
            </tr>
        </tbody>
    </table>

    <!--// colTable -->
    <!-- btnRight -->
    <div class="btnRight">
        <button class="btn btn-primary btn-sm" @click.prevent.self="onCreate">회원 등록</button>
    </div>
    <!--// btnRight -->
    <!-- paging -->
    <nav aria-label="Page navigation">
       <pagination :offset="offset" :limit="limit" :total="total" @change="onChangePage"></pagination>
    </nav>
    <!--// paging -->
</div>

<script>
    httpVueLoader.register(Vue, '/components/pagination.vue');
    
	var app = new Vue({
		el: '#app',
		data: {
			users: null,
			searchKeyword: '',
			total: 0,
			offset: ${ search.offset!0 },
			limit: ${ search.limit!4 }
		},
		filters: {
		    enableName: cmm.enableName
		},
		mounted: function() {
			this.reload();
        },
		computed: {
			totalPage: function() {
                return Math.ceil(this.total / this.limit);
            },
            pageNo: function() {
                return 0==this.total? 0 : this.offset / this.limit + 1;
            }
        },
		methods: {
			onSearch: function() {
				this.offset = 0;
				this.reload();
			},
			reload: function() {
				let self = this;
				let dataRq = {
			        searchKeyword: self.searchKeyword,
			        offset: self.offset,
			        limit: self.limit
		        };
		        util.getJson('/userVue/search.json', dataRq)
		        .then(function(res) {
                    self.total = res.total;
                    self.users = res.data ? res.data : [];
                }).catch(async function(err) {
                    if(500 !== err.status) return;
                    let res = err.responseJSON; 
                    await alert(`검색실패(${r"${res.status}"})): ${r"${res.message}"}`);
                });
			},
			onChangePage: function(offset) {
			    this.offset = offset;
			    this.reload();
			},
			onCreate: function() {
				util.movePage('/userVue/form');
			},
			onUpdate: function(id) {
			    let dataRq = {
		            id: id,
		            searchKeyword: this.searchKeyword,
		            offset: this.offset,
		            limit: this.limit,
		            t: new Date().getTime()
	            };
				util.movePage('/userVue/form', dataRq);
			},
			onDelete: function(id) {
				let self = this;
				util.deleteJson(`/usrVue/${r"{id}"}.json`)
				.then(function() {
					self.reload();
				})
				.catch(async function(err) {
				    if(500 !== err.status) return;
				    let res = err.responseJSON; 
	                await alert(`삭제실패(${r"${res.status}"}): ${r"${res.message}"}`);
				});
			}
		}
	});
</script>