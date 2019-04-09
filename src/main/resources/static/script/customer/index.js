/**********************************鼠标按下去的效果***************************************/
$("#button_search").mousedown(function() {
    $(this).attr("src", "/image/customer/button/button_search_down.png");
});
$("#button_search").mouseup(function() {
    $(this).attr("src", "/image/customer/button/button_search_up.png");
    //    location.href = "../public/customer/identification.html";
});
$("#button_write").mousedown(function() {
    $(this).attr("src", "/image/customer/button/button_write_down.png");
});
$("#button_write").mouseup(function() {
    $(this).attr("src", "/image/customer/button/button_write_up.png");
    //    location.href = "../public/customer/instructions.html";
});
/***********************************20s不动界面出现一个广告框*******************************************/
// var count=0;
// var op=0;
// function setTime(){
//     var timer=setInterval(function() {
//         count++;
//         if(count==20&&op==0){
//             op = 1;
//             layer.open({
//                 type: 2,
//                 title: '20s不动屏幕，自动跳出一个弹窗',
//                 shadeClose: true,
//                 shade: false,
//                 maxmin: true, //开启最大化最小化按钮
//                 area: ['893px', '600px'],
//                 content: '//fly.layui.com/',
//             });
//         }else if(op==1){
//             clearInterval(timer);
//         }
//     },1000);
// }
// setTime();
// $(document).click(function() {
//     clearInterval(timer);
//     op = 0;
//     count=0;
//     setTime();
// });
/*****************************************获取键盘序列号**********************************************/
var seviceAvailable = false;
$("#button_search").click(function() {
    var script = document.createElement("script");
    script.type = "text/javascript";
    script.src = "http://127.0.0.1:9999/ae0c6017d105b4ce/getHdSerial?t=" + (new Date().getTime()) +
                 "&callbackparam=success_checkCallback";
    document.body.appendChild(script);
    setTimeout(function() {
        if (!seviceAvailable) {
            layer.alert("服务超时，请回到首页重新进入！");
            return false;
        }
    }, 3000);
    return false;
});

function success_checkCallback(data) {
    // alert(data);
    seviceAvailable = true;
    $.get("/customer/validConductCenter?t=" + (new Date().getTime()), {code: data}, function(result) {
        if (result.result==true) {
            location.href = "identification.html";
        } else {
            if ((result.msg!=null)&&(typeof result.msg!="undefined")) {
                layer.alert( result.msg );
            }else{
                layer.alert("系统繁忙！");
            }
        }
    });
}