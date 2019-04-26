function isCache() {
  return null != cmmGetData('cacheKey');
}

function enableCache() {
  isCache() ? true : cmmSetData('cacheKey', []);
}

function disableCache() {
  if(!isCache()) return;
  clearCache();
  cmmRemoveData('cacheKey');
}

function getCache(key) {
     return isCache() ? cmmGetData('cache_'+key) : null;
}

function setCache(key, val) {
    if(!isCache()) return;
    var cacheKey = cmmGetData('cacheKey');
    ~cacheKey.indexOf(key) ? true : cacheKey.push(key);
    cmmSetData('cacheKey', cacheKey);
    cmmSetData('cache_'+key, val);
}

function clearCache() {
    if(!isCache()) return;
    var cacheKey = cmmGetData('cacheKey');
    cacheKey.forEach(v=>{
        cmmRemoveData('cache_'+v);
        cmmSetData('cacheKey', []);
    });
}

function checkStorate() {
    if(typeof(Storage) === 'undefined') throw "MUST use recently Web Browser";
}

function cmmGetData(key) {
    checkStorate();
    return JSON.parse(localStorage.getItem(key));
}

function cmmSetData(key, value) {
    checkStorate();
    localStorage[key] = JSON.stringify(value);
}

function cmmRemoveData(key) {
    checkStorate();
    localStorage.removeItem(key);
}

function postUrl(host, url, arg, on_success, on_error) {
	sendMessage(host+url, "POST", {body:{version:'0.1', request:arg}}, function afterSucess(res) {
    if(res.body.status == 0) {
      return 'function' === typeof on_success && on_success(res);
    } else {
      return 'function' === typeof on_error ? on_error({
        code: res.body.status,
        msg: res.body.message
      }):lcc_alert('오류: '+ errMsg);
    }
  }, on_error);
}

function sendMessage(url, method, arg, on_success, on_error) {
  let xhr = new XMLHttpRequest();
  xhr.onreadystatechange = function() {
    if (xhr.readyState === xhr.DONE) {
      if (xhr.status === 200 || xhr.status === 401) {
        on_success({
          headers: {sessionid: xhr.getResponseHeader('sessionid')},
          body: JSON.parse(xhr.response)
        });
      } else {
        on_error({status:xhr.status, text:xhr.statusText, cat:'통신오류'}, '통신오류');
      }
    }
  };
  //xhr.ontimeout = function () { };
  // xhr.withCredentials = true;

  xhr.open(method, url, true);
  //xhr.timeout = 2000; // msec
  xhr.setRequestHeader('Content-Type', 'application/json;charset="UTF-8"');
  let header = arg && arg.header;
  
  if(!isEmpty(header)) {
    xhr.setRequestHeader('sessionid', header.sessionid);
  }

  xhr.send(arg && arg.body && JSON.stringify(arg.body));
}

function loadJSON(path) {
  return new Promise((resolve, reject) => {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          resolve(JSON.parse(xhr.responseText));
        } else {
          reject(xhr);
        }
      }
    };

    xhr.open("GET", path, true);
    xhr.send();
  })
}

function lcc_alert(msg) {
    alert(msg);
}

//ref: http://joncom.be/code/javascript-json-formatter/
function FormatJSON(oData, sIndent) {
    if (arguments.length < 2) {
        var sIndent = "";
    }
    var sIndentStyle = "    ";
    var sDataType = RealTypeOf(oData);

    // open object
    if (sDataType == "array") {
        if (oData.length == 0) {
            return "[]";
        }
        var sHTML = "[";
    } else {
        var iCount = 0;
        $.each(oData, function() {
            iCount++;
            return;
        });
        if (iCount == 0) { // object is empty
            return "{}";
        }
        var sHTML = "{";
    }

    // loop through items
    var iCount = 0;
    $.each(oData, function(sKey, vValue) {
        if (iCount > 0) {
            sHTML += ",";
        }
        if (sDataType == "array") {
            sHTML += ("\n" + sIndent + sIndentStyle);
        } else {
            sHTML += ("\n" + sIndent + sIndentStyle + "\"" + sKey + "\"" + ": ");
        }

        // display relevant data type
        switch (RealTypeOf(vValue)) {
            case "array":
            case "object":
                sHTML += FormatJSON(vValue, (sIndent + sIndentStyle));
                break;
            case "boolean":
            case "number":
                sHTML += vValue.toString();
                break;
            case "null":
                sHTML += "null";
                break;
            case "string":
                sHTML += ("\"" + vValue + "\"");
                break;
            default:
                sHTML += ("TYPEOF: " + typeof(vValue));
        }

        // loop
        iCount++;
    });

    // close object
    if (sDataType == "array") {
        sHTML += ("\n" + sIndent + "]");
    } else {
        sHTML += ("\n" + sIndent + "}");
    }

    // return
    return sHTML;
}

function RealTypeOf(v) {
  if (typeof(v) == "object") {
    if (v === null) return "null";
    if (v.constructor == (new Array).constructor) return "array";
    if (v.constructor == (new Date).constructor) return "date";
    if (v.constructor == (new RegExp).constructor) return "regex";
    return "object";
  }
  return typeof(v);
}

function getDate(val) {
  if(!val) return;
  var date = new Date(val);
  if(isNaN(date)) {
    if(isNaN(val)) val = val.replace(/[^0-9]/g,'');
    date = new Date(val.substr(0, 4), val.substr(4, 2) - 1, val.substr(6, 2), val.substr(8, 2), val.substr(10, 2));
  }

  return date;
}

function getDayNextWeek() {
  var date = new Date();
  date.setDate(date.getDate()+7);
  return date;
}

function digitStr(num) {
    return 9 < num ? ''+num : '0'+num;
}

function reqDateStr(date) {
  date = getDate(date);
  return ''+date.getFullYear()+digitStr(date.getMonth()+1)+digitStr(date.getDate());
}

function dateStr(date) {
  date = getDate(date);
  return ''+date.getFullYear()+'-'+digitStr(date.getMonth()+1)+'-'+digitStr(date.getDate());
}

function timeStr(date) {
  date = getDate(date);
  return ''+digitStr(date.getHours())+':'+digitStr(date.getMinutes());
}

function dateTimeStr(date) {
  date = getDate(date);
  return ''+dateStr(date)+' '+timeStr(date);
}

function isEmpty(value) {
  if (value == null || value == "" || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
    return true
  } else {
    return false
  }
};

function setPhoneFormat(number) {
  return number.toString().replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
}

function setMoneyFormat(money) {
  if(!Number.isInteger(parseInt(money))) return "";
  return Number(money).toLocaleString();
}

function setTimeFormat(time) {
  if(isNaN(time)) return time;
  var hours = Math.floor(time/3600);
  var minutes = Math.floor((time%3600)/60);
  return hours + '시간' + (minutes>0?minutes+'분':'');
}

function compareArray(arr1, arr2) {
  if(arr1.length!=arr2.length) return false;
  if(RealTypeOf(arr1[0])=='object' && RealTypeOf(arr2[0])=='object') {
    return arr1.every((v, i) => compareJson(v, arr2[i]));
  } else {
    return arr1.every((v, i) => v == arr2[i]);
  }
}

function compareJson(jsonObj1, jsonObj2, args) {
  var j1 = deepCopy(jsonObj1);
  var j2 = deepCopy(jsonObj2);
  var flag = true;

  !isEmpty(args) && args.forEach(arg => {
    j1[arg] = null;
    j2[arg] = null;
  });

  if(Object.keys(j1).length != Object.keys(j2).length) return false;

  for(key in j1) {
    if(RealTypeOf(j1[key]) == 'object') flag = compareJson(j1[key], j2[key]);
    else if(RealTypeOf(j1[key]) == 'array') flag = compareArray(j1[key], j2[key]);
    else flag = j1[key]==j2[key];

    if(!flag) break;
  }

  return flag;
}

function compareData(data1, data2) {
  if(RealTypeOf(data1) != RealTypeOf(data2)) return false;

  switch (RealTypeOf(data1)) {
    case 'array':
      return compareArray(data1, data2);
    case 'object':
      return compareJson(data1, data2);
    default:
      return data1 == data2;
  }
}

function getAllMatchesIndex(arr, value) {
  return arr.reduce((rt, v, idx) => {
    var isEqual = RealTypeOf(v)=="object"?compareJson(v, value):(v==value);

    if(isEqual) return rt.concat(idx);
    else return rt;
  }, []);
}

function checkNestedValue(arr) {
  return arr.some(v => getAllMatchesIndex(arr, v).length>1);
}

function deepCopy(json) {
  return JSON.parse(JSON.stringify(json));
}

function isNumber(value) {
  var numberPattern = /^[-]?\d+(?:[.]\d+)?$/;
  return numberPattern.test(value);
}


function cmdAsync(cmd, req, res) {
    if(undefined == cmd || undefined == req) {
        lcc_alert("LCC : invalid request data");
        return;
    }

    if (undefined == res) {
        return new Promise((resolve,reject)=>postLcc(cmd, req, res=>resolve(res.body.response), err=>reject(err)));
    } else {
        postLcc(cmd, req, res=>res(res.body.response));
    }
}

function postLcc(cmd, req, on_success, on_error) {
	sendMessage(generateUrl(cmd), 'POST', {header:null, body:{version:'0.1', request:req}}, res=>{
        let sessionid = res.headers.sessionid;
        if(res.body.status == 0) {
            return 'function' === typeof on_success && on_success(res);
        } else {
            return 'function' === typeof on_error ? on_error({
            status: res.body.status,
            message: res.body.message
            }):lcc_alert('오류(' + res.body.status + "): " + res.body.message);
        }
    }, (err)=>{
        'function' === typeof on_error ? on_error({
            status: err.status,
            message: err.text
        }) : alert(cat + ': '+ JSON.stringify(err));
    });
}