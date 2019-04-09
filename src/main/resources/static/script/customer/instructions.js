// $(function(){
//     $.get("/customer/readInstruction",
//         function(result){
//         if(result.result){
//             $(".footer").show();
//             var data=result.msg;
//             $(".title").html(data.name);
//             $(".content>p").html(data.value);
//         }else{
//             $(".footer").hide();
//             layer.alert("系统繁忙，请稍后重试！");
//         }
//     });
// })
var content="已阅读并同意以上须知";
var count=4;
var canNext=0;
var seviceAvailable = false;
var countDown=setInterval(function () {
    if(count>0){
        $("#agree").html(content+"（"+count+"）");
        count--;
    }else{
        clearInterval(countDown);
        $("#agree").html(content);
        $("#agree").removeClass("disable-href");
        count = 0;
        if(count==0){
            $("#agree").click(function (e) {
                // var script = document.createElement("script");
                // script.type = "text/javascript";
                // script.src = "http://127.0.0.1:9999/ae0c6017d105b4ce/getHdSerial?t="+(new Date().getTime())+"&callbackparam=success_jsonpCallback";
                // document.body.appendChild(script);
                setTimeout(function() {
                    if (!seviceAvailable) {
                        layer.alert("服务超时，请回到首页重新进入！");
                        return false;
                    }
                }, 3000);
                if(canNext==1){
                    location.href="accident_information.html";
                }     
                return false;
            });
        }
    }
},1000);
var script = document.createElement("script");
script.type = "text/javascript";
script.src = "http://127.0.0.1:9999/ae0c6017d105b4ce/getHdSerial?t="+(new Date().getTime())+"&callbackparam=success_knowedCallback";
document.body.appendChild(script);
function success_knowedCallback(data) {
    seviceAvailable = true;
    $.get("/customer/validConductCenter?t="+(new Date().getTime()),{code:data},function (result) {
        if(result.result==true){
            $.post("/customer/accident?t="+(new Date().getTime()),function(data){
                // console.log(data);
                if(data.result==true){
                    canNext=1;
                }else{
                    if(( data.msg!==null)&&(typeof data.msg!=="undefined")) {
                        layer.alert(data.msg);
                    }else{
                        layer.alert("系统繁忙!");
                    }
                    return false;
                }
            });
        }else{
            if(( result.msg!==null)&&( typeof result.msg!=="undefined")) {
                layer.alert(result.msg);
            }else{
                layer.alert("系统繁忙!");
            }
        }
    });
}
