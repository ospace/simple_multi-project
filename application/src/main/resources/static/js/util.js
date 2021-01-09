(function umd(root, factory) {
  if ('function' === typeof define && define.amd) {
    define([], factory);
  } else if ('object' === typeof module && module.exports) {
    module.exports = factory();
  } else {
    root.util = factory();
  }
}(this, function () {

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
	/**
	 * 페이지 이동
	 */
	movePage: function (url, data) {
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