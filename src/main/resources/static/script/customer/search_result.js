$.fn.getResult= function(){
    $("#idCardNumber").html(idCardNumber);
    $("#idCardName").html(idCardName);
    $.get("/customer/listByIdNumber?t="+new Date().getTime(), {idNumber: idCard}, function(data) {
        if (data.result==true) {
            for (var i = 0; i < data.msg.length; i++) {
                var msg = data.msg[i];
                var time = format(msg.occurredTime, "yyyy-MM-dd HH:mm:ss");
                $(".search-main-dtl").append("<tr>\n" +
                                             "                    <td class=\"search-ac-time\">"+time+"</td>\n" +
                                             "                    <td class=\"search-ac-address\">"+msg.location+"</td>\n" +
                                             "                    <td class=\"search-ac-card-no\">"+msg.accidentPartyStr+"</td>\n" +
                                             "                    <td class=\"search-ac-card-no\">"+msg.drivingNumberStr+"</td>\n" +
                                             "                    <td class=\"search-ac-negotiation\">"+msg.processMode+"</td>\n" +
                                             "                    <td class=\"search-ac-org\">"+msg.dataSources+"</td>\n" +
                                             "                    <td class=\"search-ac-preview\"><a href=\"accident_negotiation_print.html\">预览</a></td>\n" +
                                             "</tr>");
            }
            layer.close(index);
        } else {
            $(".search-main-dtl").append("<tr width=\"100%\"><td>没有查询到任何数据</td></tr>");
        }
    });
}
if(idCardNumber){
    // var currentUrl = window.location.href;
    // var currentUrl="http://localhost:8081/public/customer/search_result.html?111111111111111&&张三"; //测试专用
    // var parameterUrl = currentUrl.split("?")[1];
    // var userInformation = parameterUrl.split("&&");
    // var idCard = userInformation[0];
    // var idCardName = userInformation[1];
    $(".page-search-result").removeClass("hide").siblings().addClass("hide").getResult();
}


