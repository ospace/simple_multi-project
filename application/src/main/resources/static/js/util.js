(function umd(root, factory) {
  if ('function' === typeof define && define.amd) {
    define(['jquery'], factory);
  } else if ('object' === typeof module && module.exports) {
    module.exports = factory(require('jquery'));
  } else {
    root.util = factory(root.jQuery);
  }
}(this, function ($) {

/*
if(undefined === $ || null === $) {
	throw new Error('util을 사용하기 위해서는 반드시 사전에 jQeury을 포함해야 합니다.');
}

//--------------------------------------------------------------------  Jquery plugin
$.fn.clickToggle = function(func1, func2) {
	var funcs = [func1, func2];
	this.data('toggleclicked', 0);
	this.click(function() {
		var data = $(this).data();
		var tc = data.toggleclicked;
		$.proxy(funcs[tc], this)();
		data.toggleclicked = (tc + 1) % 2;
	});
	return this;
};

$.fn.serializeObject = function() {
	var obj = null;
	try {
		if (this[0].tagName && this[0].tagName.toUpperCase() == "FORM") {
			var arr = this.serializeArray();
			if (arr) {
				obj = {};
				jQuery.each(arr, function() {
					obj[this.name] = this.value;
				});
			}
		}
	} catch (e) {
		alert(e.message);
	}

	return obj;
};

$.postJson = function(url, data, success, args) {
	return $.ajax({
		url: url,
		type: 'POST',
		data: JSON.stringify(data),
		//dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: success
	}, args);
};

$.deleteJson = function(url, data, success, args) {
	return $.ajax({
		url: url,
		type: 'DELETE',
		data: JSON.stringify(data),
		// dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: success
	}, args);
};

$.postMultipart = function(url, data, progress, args) {
	let formData = new FormData();
	for(let k in data) {
		formData.append(k, data[k]);
	}

	return $.ajax({
		url: url,
		type: 'POST',
		data: formData,
		enctype: 'multipart/form-data',
		// dataType: 'json',
		processData: false,
		contentType: false,
		// success: success,
		xhr: progress && function() {
		    let xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = progress; //ev.total, evt.loaded
            return xhr;
		}
	}, args);
};
*/
return {
	isUndef: function(obj) {
		return undefined == obj || null == obj;
	},
	/**
	 * 숫자 체크
	 */
	isNumber: function (str){
		var reg =  /^[0-9]+$/ ;
		return reg.test(str);
	},
	/**
	 * 메일 체크
	 */
	isEmail : function(str){
		var reg = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/ ;
		return reg.test(str);
	},
	/**
	 * 윈도우 팝업창
	 */
	wPopup : function(params){
		
		var options = {
			title : "관리도구",
			width : 600,
			height : 400,
			scrollbars : "no"
		};
		
		$.extend( options, params );
		
		var left = (screen.width/2)-(options.width /2);
		var top = (screen.height/2)-( options.height /2);
		if( options.left != undefined ) left = left +  options.left;
		if( options.top != undefined ) top = top +  options.top; 
		
		var winObj = window.open(options.url, options.title.replace(/ /g,'_'), 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=' + options.scroll + ', width='+options.width+', height='+options.height+', top='+top+', left='+left);
		winObj.document.title = options.title
	},
	formatBytes : function(a,b){
		if(0==a)return"0 Bytes";
		var c=1024,
		d=b||2,e=["Bytes","KB","MB","GB","TB","PB","EB","ZB","YB"],
		f=Math.floor(Math.log(a)/Math.log(c));
		return parseFloat((a/Math.pow(c,f)).toFixed(d))+" "+e[f]
	},
	formatNumber : function(p){
		return p.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	},
	setFormatNumber : function(obj){
		$(obj).val( $(obj).val().replace(/,/g,'').replace(/\B(?=(\d{3})+(?!\d))/g, ",") );
	},
	/**
	 *  리스트 데이터 Object  MVC 에서 받을 수 있는 Object  로 변환
	 *  dataList[0].dataTitle : 'abcd'
	 *  dataList[1].dataTitle : 'efgh'
	 *  ...
	 */
	converListToObject : function(name, objList){
		
		var obj = {};
		$(objList).each(function(i,val){
			$.each(val,function(k,v){
				obj[name + "[" + i + "]." + k] = v;
			});
			
		});
		//console.log( obj )
		return obj
		
	},
	/**
	 * textarea 높이 내용에 맞게 조정
	 */
	autoHeight : function(obj){
		$(obj).height( $(obj).prop("scrollHeight") + 16)
	},
	/**
	 * 페이지 이동
	 */
	movePage: function (url, data) {
	    /*
		let form = $('<form/>', {method:'post', action:url});
		for(let k in data) {
			form.append($('<input/>', {type:'hidden', name:k, value:data[k]}));
		}
		form.appendTo(document.body);
		*/
		let form = this.createElement('form', {method:'post', action:url});
		for(let k in data) {
		    let child = this.createElement('input', {type:'hidden', name:k, value:data[k]});
		    this.appendChild(form, child);
		}
		this.appendChild(document.body, form);
		form.submit();
	},
	createElement(tag, attrs) {
	    let elem = document.createElement(tag);
	    for(let k in attrs) {
	        elem.setAttribute(k, attrs[k]);
	    }
	    return elem;
	},
	appendChild(parent, child) {
	    if(!parent) return;
	    parent.appendChild(child);
	},
	on: function(obj, event, callback) {
		obj && obj.addEventListener(event, callback);
	},
	off: function(obj, event, callback) {
        obj && obj.removeEventListener(event, callback);
	},
	getById: function(id) {
		return document.getElementById(id);
	},
	show: function(obj) {
		obj&&(obj.style.display ='inherit');
	},
    hide: function(obj) {
		obj&&(obj.style.display ='none');
    },
    addClass: function(obj, className) {
        obj&&obj.classList.add(className);
    },
    removeClass: function(obj, className) {
        obj&&obj.classList.remove(className);
    },
    findParentNode: function(obj, nodeName) {
        if(!obj || !nodeName) return undefined;
        let ret = obj;
        do {
            ret = ret.parentNode;
        } while(ret && ret.nodeName !== nodeName);
        return ret;
    },
    isEmpty: function (obj){
		return 0 == obj.value.length;
	},
	xmlParser: function(text) {
		let parser = new DOMParser();  
		return parser.parseFromString(text, 'text/xml').responseXML;
    },
    currency: function(value) {
        let num = new Number(value);
        return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,');
    },
    queryString: function(obj) {
        return Object.keys(obj).map(function(key) {
            return key + '=' + encodeURIComponent(obj[key]);
        }).join('&');
    },
	ajax: function(url, options) {
		if('object' === typeof url) {
			options = url;
			url = undefined;
		}

		options = Object.assign({
			//method: 'GET',
			//contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			async: true
		}, options);

		const xhr = new XMLHttpRequest();
		xhr.open(options.method, url || options.url, options.async);

		const headerKeys = Object.entries({
			contentType: 'Content-type',
			accept: 'Accept'
		});

		headerKeys.forEach(function([key, value]) {
			if(undefined === options[key] || null === options[key]) return; 
            xhr.setRequestHeader(value, options[key]);
		});
		
		/*
		 * 이벤트 핸들러들
		 * XMLHttpRequest.onreadystatechange: attribute가 변경될때마다 호출
		 * XMLHttpRequestEventTarget.ontimeout: 요청시간 초과될 때 호출됨 
		 * onload, onerror, onprogress는 브라우저마다 다양하게 구현됨
		 * onload: load 이벤트, XMLHttpRequest 처리가 성공이면 발생됨
		 * onabort: abort 이벤트, XMLHttpRequest.abort()호출로  요청 중단
		 * onerror: error 이벤트, 요청에서 에러가 발생
		 * onloadend: loadend 이벤트, 요청이 성공이든 실패이든 완료되면 발생
		 * onloadstart: loadstart 이벤트, 데이터 수신하면 발생
		 * onprogress: progress 이벤트, 데이터 수신동안 주기적으로 발생
		 */

		xhr.send(options.data);

		/*
		xml, html, json -> xml, text, json
		"text": String,
		"json": JSON.parse,
		"xml": this.xmlParser

		*/
		return new Promise(function(resolve, reject) {
			xhr.onreadystatechange = function (e) {
				if(XMLHttpRequest.DONE !== xhr.readyState) return ;

				if(200 === xhr.status) {
					resolve(xhr.responseText);
				} else {
					reject({responseText:xhr.responseText, status:xhr.status});
				}
			};
		});
	},
	getJson: function(url, req) {
	    let self = this;
	    ret = req || {};
	    req.t = new Date().getTime();
	    url = url + '?' + this.queryString(req);
	    return new Promise(function(resolve, reject) {
	        self.ajax({
	            url:url,
	            method:'GET',
	            contentType:'application/json;charset=utf-8'
            })
	        .then(function(res) {
	            resolve(JSON.parse(res));
	        })
	        .catch(function(err) {
	            if(err.responseText) {
                   err.responseJSON = JSON.parse(err.responseText);
                }
	            reject(err);
	        });
	    });
	},
	postJson: function(url, req) {
	    let self = this;
        url = url + '?t=' + new Date().getTime();
        return new Promise(function(resolve, reject) {
            self.ajax({
                url:url,
                method:'POST',
                contentType:'application/json;charset=utf-8',
                data: JSON.stringify(req)
            })
            .then(function(res) {
                resolve(res && JSON.parse(res));
            })
            .catch(function(err) {
                if(err.responseText) {
                    err.responseJSON = JSON.parse(err.responseText);
                }
                reject(err);
            });
        });
	},
	postForm: function(url, formData) {
	    let self = this;
	    let boundary = 'UdOlLuLOEOUpLb3y';
	    return new Promise(function(resolve, reject) {
	       self.ajax({
	           url: url,
	           method:'POST',
	           data: formData
	       })
	       .then(resolve)
	       .catch(function(err) {
	           if(err.responseText) {
	               err.responseJSON = JSON.parse(err.responseText);
	           }
               reject(err);
	       });
	    });
	},
	deleteJson: function(url, req) {
	    let self = this;
        url = url + '?t=' + new Date().getTime();
        return new Promise(function(resolve, reject) {
            self.ajax({
                url:url,
                method:'DELETE',
                contentType:'application/json;charset=utf-8',
                data: JSON.stringify(req)
            })
            .then(function(res) {
                resolve(res && JSON.parse(res));
            })
            .catch(function(err) {
                if(err.responseText) {
                    err.responseJSON = JSON.parse(err.responseText);
                }
                reject(err);
            });
        });
	}
};

})); //End of line