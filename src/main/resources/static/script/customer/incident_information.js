$("#accidentXS").click(function() {
    $.get("/customer/accidentParty?time="+new Date().getTime(),function (result) {
        if(result.result==true){
            var resultData=result.msg;
            // console.log(resultData);
            var canEnter;
            for(var i=0;i<resultData.length;i++){
                // console.log(name);
                if(resultData[i].drivingLicence.notcustom==false){
                    canEnter=0;
                    layer.alert("未查到车主姓名，请选择在线认定办理！");
                    break;
                }else{
                    canEnter=1;
                }
            }
            if(canEnter){
                $.ajax({
                    type: "PUT",
                    url: "/customer/updateProcessModeAndProcessStatus",
                    dataType: "json",
                    data:{processMode:0,processStatus:0},
                    success: function(data) {
                        if (data.result==true){
                            location.href="accident_situation.html";
                        }else {
                            if(( data.msg!==null)&&(typeof data.msg!=="undefined")) {
                                layer.alert(data.msg);
                            }else{
                                layer.alert("系统繁忙!");
                            }
                        }
                    }
                });
            }
        }else{
            if(( reslut.msg!==null)&&(typeof reslt.msg!=="undefined")) {
                layer.alert(result.msg);
            }else{
                layer.alert("系统繁忙!");
            }
        }
    });
    return false;
});

$('#accidentTJ').click(function() {
    $.ajax({
        type: "PUT",
        url: "/customer/updateProcessMode?time="+new Date().getTime(),
        dataType: "json",
        data:{processMode:1},
        success: function(data) {
            if (data.result==true){
                location.href = "numeral.html";
            }else {
                if(( data.msg!==null)&&(typeof data.msg!=="undefined")) {
                    layer.alert(data.msg);
                }else{
                    layer.alert("系统繁忙!");
                }
            }
        }
    });
    return false;
});
$(function() {
    $.get("/customer/getAccidentTotalCount?time="+new Date().getTime(),function (result) {
        if(result.result==true) {
            $("#waitCount").html(result.msg.count);
            $("#waitTime").html(result.msg.totalTime);
        }else{
            layer.alert("系统繁忙，请耐心等待！")
        }
    });
})
/*********************按钮按下去的效果*****************************/
$("#button_consensus").mousedown(function() {
    $(this).attr("src","/image/customer/button/button_consensus_down.png");
    return false
}).mouseup(function() {
    $(this).attr("src","/image/customer/button/button_consensus_up.png");
    return false
});
$("#button_mediate").mousedown(function() {
    $(this).attr("src","/image/customer/button/button_mediate_down.png");
    return false
}).mouseup(function() {
    $(this).attr("src","/image/customer/button/button_mediate_up.png");
    return false
});