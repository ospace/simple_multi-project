'use strict';

//Ref: https://kr.vuejs.org/v2/api/index.html

var alertModal;
(function (w, d) {
	let modal = undefined;
	let alert = w.alert;
	
	w.alert = function(content) {
		return new Promise((resolve) => {
			if(undefined === modal) {
				alert(content);
				resolve();
			} else if (!modal.$props.isShow) {
				Vue.set(modal, 'content', content);
				Vue.set(modal, 'isShow', true);
				modal.$once('complete', resolve);
			} else {
                resolve();
            }
		});
	};
	
//	httpVueLoader('/static/components/vAlert.vue')().then(function(vAlert)  {
//		let AlertVue = Vue.extend(vAlert);
//		modal = new AlertVue({ el: document.createElement('div') });
//		d.body.appendChild(modal.$el);
//		
//		alertModal = modal;
//	});
})(window, document);
