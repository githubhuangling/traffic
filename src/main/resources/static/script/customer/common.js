/***************************弹窗********************************/
function popWindow() {
    var html ="<div id=\"popBox\">\n    <div class='pop-mask' id='popMask'></div>\n    <div class=\"pop-window\"></div>\n</div>";
    $("body").append(html);
    $("#popMask").click(function (e) {
        $("#popBox").remove();
        $("body").css("overflow","auto");
    });
}

/***********************自定义时间格式****************************/
function format(time, format){
    var t = new Date(time);
    var tf = function(i){return (i < 10 ? '0' : '') + i};
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
        switch(a){
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    })
}
/******引用格式
*  var time = format(value, "yyyy-MM-dd HH:mm:ss");
********************/


/** 获取页面URL参数 */
window.getQueryString = function (name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
};


/*************************屏蔽右键******************************/
document.onmousedown=function(e){
    if(window.event){
        e = window.event;
        if(e.button==2){
            //alert("操作有误");
        }
    }
}
/************************判断是null转换为空字符串*******************************/
function crossNull(msg){
    return msg;
}
/***********************禁用右键、文本选择功能、复制按键***************************/
$(document).bind("contextmenu",function(){return false;});
$(document).bind("selectstart",function(){return false;});

/****************************隔10分钟请求一下******************************/
setInterval(function() {
    $.get("/customer/interval?t="+new Date().getTime(),function (result) {});
},600000);

/******************************增加加载条******************************/
function isLoading(bool,str) {
    if(bool){
        $(str).siblings("span.load").addClass("load-img");
        $(str).prop("readonly",true);
    }else{
        $(str).siblings("span.load").removeClass("load-img");
        $(str).prop("readonly",false);
    }
}

/****************************监听网络状态***********************************/
window.addEventListener('online',  function(){
    layer.alert("网络已恢复！");
});
window.addEventListener('offline', function(){
    layer.alert("网络异常，请稍后重试！");
});