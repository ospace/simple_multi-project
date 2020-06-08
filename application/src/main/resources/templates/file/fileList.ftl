<#ftl encoding='UTF-8'>
<#include "/layouts/util.ftl"/>

<div id="app" style="width: 90%;" v-cloak>

	<div class="form-inline">

		<div class="form-group">
			<input type="text" class="form-control" v-model="search.searchKeyword">
		</div>
		<div class="form-group">
			<button class="btn btn-primary btn-sm" @click.prevent.self="onSearch">검색</button>
		</div>
	</div>

	<div class="pull-right">Total : {{total}} [ {{pageNo}} / {{totalPage}} ]</div>
 
	<table class="table table-hover">
		<colgroup>
			<col width="5%">
			<col width="20%">
			<col width="10%">
			<col width="10%">
			<col width="10%">
			<col width="8%">
			<col width="10%">
			<col width="10%">
		</colgroup>
		<thead>
			<tr>
				<th class="text-center">No.</th>
				<th class="text-center">파일명</th>
				<th class="text-center">타입</th>
				<th class="text-center">크기</th>
				<th class="text-center">URL</th>
				<th class="text-center">다운로드</th>
				<th class="text-center">삭제</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="(each, index) in files" v-bind:key="each.id">
				<td class="text-center">{{search.offset + index + 1}}</td>
				<td class="text-center"><a :href="'/file/'+each.id" target="_blank">{{each.originalFilename}}</a></td>
				<td class="text-center">{{each.type}}</td>
				<td class="text-center">{{each.size}}</td>
				<td class="text-center"><a :href="each.url" target="_blank">열기</a></td>
				<td class="text-center"><a v-if="0<each.size" :href="'/file/download/'+each.id" target="_black">다운로드</a></td>
				<td class="text-center"><button @click.prevent.self="onDelete(each.id)">삭제</button></td>
			</tr>
			<tr v-if="files&&!files.length">
				<td colspan="7" style="text-align: center;">
                    자료가 없습니다.
                </td>
			</tr>
		</tbody>
	</table>
	<nav aria-label="Page navigation" class="text-center">
	   <pagination :offset="search.offset" :limit="search.limit" :total="total" @change="onChangePage"></pagination>
    </nav>
	<button type="button" class="btn btn-primary btn-sm" @click.prevent.self="onCreate">등록</button>
</div>

<script>
    httpVueLoader.register(Vue, '/components/pagination.vue');
    
	var app = new Vue({
		el: '#app',
		data: {
		    search: <@json_string search!"" "{offset:0, limit:4}"/>,
			files : [],
			total : 0
		},
		mounted: function() {
			this.reload();
        },
		computed: {
            totalPage: function() {
                return Math.ceil(this.total / this.search.limit);
            },
            pageNo: function() {
            	return 0==this.total? 0 : this.search.offset / this.search.limit + 1;
            }
        },
		methods: {
			onSearch: function() {
				this.search.offset = 0;
				this.reload();
			},
			reload: function() {
				let self = this;
				util.getJson('/file/search', this.search)
				.then(function(res) {
                    self.total = res.total;
                    self.files = res.data ? res.data : [];
                })
                .catch(function(err) {
                	let res = err.responseJSON;
                	if(res && 0 < res.status) {
                		alert(`status: ${r"${res.status}"} - ${r"${res.message}"}`);
                	}
                });
			},
			onChangePage: function(offset) {
			    this.search.offset = offset;
			    this.reload();
			},
			onCreate: function() {
				util.movePage('/file/form');
			},
			onDelete: async function(id) {
				let self = this;
				
				if(!await confirm('삭제하시겠습니까?')) return; 
				
				util.deleteJson(`/file/${r"${id}"}`)
				.then(function(res) {
					self.reload();
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