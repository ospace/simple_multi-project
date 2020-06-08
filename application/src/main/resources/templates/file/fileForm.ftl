<#ftl encoding='UTF-8'>
<#include "/layouts/util.ftl"/>

<div id="app" v-cloak>  
    <div class="form-group">
    	<table class="table">
    	  <colgroup>
    	    <col width="30%">
    	    <col width="70%">
    	  </colgroup>
    	<tbody>
        	<tr>
        		<th>파일</th>
        		<td>
        		    <input type="text" title="파일명" class="form-control" v-model="file.filename">
        		    <input type="file" class="form-control" id="fileUpload" ref="file" @change="onChangeFile">
        		    <label for="fileUpload">파일 업로드</label>
        	    </td>
        	</tr>
        	<tr>
                <th scope="row">미리보기</th>
                <td><img :src="url" alt=""></td>
            </tr>
    	</tbody>
    	</table>
	</div> 

	<button type="button" class="btn btn-primary btn-sm" @click.prevent.self="onGoList">목록</button>
	<button type="button" class="btn btn-primary btn-sm" :key="saveKey" @click.once="save">저장</button>
	<button v-if="file.id" type="button" class="btn btn-warning btn-sm pull-right pull-margin" @click.prevent.self="onDelete">삭제</button>
	
</div>

<script>
var app = new Vue({
    el: '#app',
    data: {
        search: <@json_string search!"" "{offset:0, limit:4}"/>,
        file: <@json_string file!"" "{}"/>,
        url: null,
    	saveKey: 1
    },
    methods: {
    	onGoList: function() {
    		util.movePage('/file/list');
    	},
    	save: function() {
    		let self = this;
    		let formData = new FormData();
    		formData.append('file', this.fileData);
    		util.postForm('/file', formData)
    		.then(function(res) {
    			self.onGoList();
    		})
    		.catch(async function(err) {
    		    if(500 !== err.status) return;
    		    let res = err.responseJSON;
    		    await alert(`파일등록 에러(${r"${res.status}"}): ${r"${res.message}"}`);
    			++self.saveKey;
    		});
    	},
    	onChangeFile: function(ev) {
   		    let file = this.$refs.file.files[0];
   		    
   		    this.$set(this.file, 'filename', file.name);
   		    this.url =  URL.createObjectURL(file);
   		    this.fileData = file;
    	},
    	onDelete: function() {
    		console.log('>> onDelete');
    		++self.keyNo;
    	}
    }
});
</script>

