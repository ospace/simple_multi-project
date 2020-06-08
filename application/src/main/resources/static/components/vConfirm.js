'use strict';

//Ref: https://kr.vuejs.org/v2/api/index.html

(function(w, d) {
	let modal = undefined;
	let confirm = w.confirm;
	
	w.confirm = function(content) {
		return new Promise(function(resolve) {
			if(undefined === modal) {
				resolve(confirm(content));
			} else if (!modal.$props.isShow) {
				Vue.set(modal, 'content', content);
				Vue.set(modal, 'isShow', true);
				modal.$once('complete', resolve);
			} else {
			    resolve();
			}
		});
	};
		
//	httpVueLoader('/static/components/vConfirm.vue')().then(function(vConfirm)  {
//		let ConfirmVue = Vue.extend(vConfirm);
//		modal = new ConfirmVue({ el: document.createElement('div') });
//		d.body.appendChild(modal.$el);
//	});
})(window, document);
