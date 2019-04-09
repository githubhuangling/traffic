var accidentId=0;
var urladdress=window.location.href.toString();
//var idCardNumber =urladdress.substring(urladdress.indexOf('idCardNumber')+'idCardNumber'.length+1);
//var num=urladdress.indexOf("?")
//var urladdress=urladdress.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]
var idCardNumber=getQueryString("idCardNumber");
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var reg_rewrite = new RegExp("(^|/)" + name + "/([^/]*)(/|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    var q = window.location.pathname.substr(1).match(reg_rewrite);
    if(r != null){
        return unescape(r[2]);
    }else if(q != null){
        return unescape(q[2]);
    }else{
        return null;
    }
}



//加载事故信息二维码
$('#agreement-qrcode').attr('src', '/customer/ownAgreementQrcode?'+new Date().getTime());
//获取信息填入
$.ajax({
    type: "GET",
    url: "/customer/ownAgreement?t="+new Date().getTime(),
    dataType: "json",
    success: function(data) {
        //excel
        if (data.result) {
            accidentId=data.msg.id;
            //获取事故编号
            var serialNumber = data.msg.serialNumber;
            $('#agree-serialNumber').html("编号：" + serialNumber);
            //获取所有事故情形并选中对应的
            data.msg.reasonCategories.forEach(function(value) {
                if (value.name == data.msg.accidentReason) {
                    $('#accident-reasoncate').append('<p>☑' + value.name + '</p>');
                } else {
                    $('#accident-reasoncate').append('<p>□' + value.name + '</p>');
                }
            });
            var time = format(data.msg.time, "yyyy年MM月dd日 HH时mm分");
            $('#accident-occuredtime').html(time);
            var location = data.msg.location;
            $('#accident-location').html(location);
            var weather = data.msg.weather;
            $('#accident-weather').html(weather);
            var code = new Array("甲", "乙", "丙", "丁", "戊");
            var accidentParts = data.msg.accidentParties;

            if (accidentParts.length == 2) { //两位当事人
                accidentParts.forEach(function(value, index) {

                    var insuranceTime = format(value.insuranceDate, "yyyy-MM-dd");
                    var part = "<tr>\n" + "                                <td>" + code[index]
                               + "</td>\n" + "                                <td>" + value.name
                               + "</td>\n" + "                                <td>" + value.driverNumber
                               + "</td>\n" + "                                <td>" + value.phone
                               + "</td>\n" + "                                <td>" + value.drivingNumber
                               + "</td>\n" + "                                <td>"
                               + getCarType(value.carType) + "</td>\n"
                               + "                                <td style='font-size: 10px;line-height:16px'>" + value.insuranceCompany + "</td>\n"
                               + "                                <td style='font-size: 10px;line-height:16px'>" + value.insuranceCompulsory + "</td>\n"
                               + "                                <td>" + insuranceTime + "</td>\n"
                               + "                                <td colspan='2' style='font-size: 10px;line-height:16px'>"+value.caseNumber+"</td>\n"
                               + "                            </tr>";
                    $('#table-tr-situationAndResponsibility').before(part);
                    var codename = "<td colspan=\"" + (index == 1 ? 6 : 5) + "\">" + code[index]
                                   + "</td>";
                    $('#accident-partslength').append(codename);
                    var brokenparts = "<td>车损<br/>部位</td>\n                                 <td colspan=\""
                                      + (index == 1 ? 5 : 4) + "\" style='text-align: left;text-indent: 20px'>□车头 □左前角 □右前角 □车尾 □左后角 □右后角 □车身左侧 □车身右侧</td>";
                    $('#accident-brokenParts').append(brokenparts);
                    var reason = "<td>事故<br/>情形</td>\n            <td colspan=\""
                                 + (index == 1 ? 5 : 4)
                                 +
                                 "\">\n                <p>□停车 □倒车 □逆行 □溜车 □开关车门</p>\n                <p>□违反交通信号灯 □变更车道与其他车辆刮擦 □未保持安全车距与前车追尾</p>\n                <p>□未按规定让行 □其他</p>\n            </td>";
                    $('#accident-reason').append(reason);
                    var p = '□全部责任&nbsp;&nbsp;□有责任&nbsp;&nbsp; □无责任';
                    var pastrespon = '□' + value.responsibility;
                    var newrespon = '☑' + value.responsibility;
                    var newp = p.replace(pastrespon, newrespon);
                    var responsibility = "<td>事故<br/>责任</td>\n" + "            <td colspan=\""
                                         + (index == 1 ? 5 : 4) + "\">\n" + "                <p>" + newp + "</p>\n"
                                         + "            </td>";
                    var now = new Date();
                    $('#accident-responsibility').append(responsibility);
                    var partid=value.id
                    var sig = code[index] + "<img src='"+value.signaturePic+"'/><p>"
                              + format(now.getTime(), "yyyy年MM月dd日") + "</p>";
                    $('#accident-signature').append(sig);
                });
            } else {//多位当事人
                accidentParts.forEach(function(value, index) {
                    var insuranceTime = format(value.insuranceDate, "yyyy-MM-dd");
                    var part = "<tr>\n" + "                                <td>" + code[index]
                               + "</td>\n" + "                                <td>" + value.name
                               + "</td>\n" + "                                <td>" + value.driverNumber
                               + "</td>\n" + "                                <td>" + value.phone
                               + "</td>\n" + "                                <td>" + value.drivingNumber
                               + "</td>\n" + "                                <td>"
                               + getCarType(value.carType) + "</td>\n"
                               + "<td style='font-size: 10px;line-height:16px'>" + value.insuranceCompany + "</td>\n"
                               + "<td style='font-size: 10px;line-height:16px'>" + value.insuranceCompulsory + "</td>\n"
                               + "<td>" + insuranceTime + "</td>\n"
                               + "<td colspan='2' style='font-size: 10px;line-height:16px'>"+value.caseNumber+"</td>\n"
                               + "</tr>";
                    $('#table-tr-situationAndResponsibility').before(part);

                    if (value.responsibility == "全部责任") {
                        var codename = "<td colspan=\"" + (index == 1 ? 5 : 6) + "\">" + code[index]
                                       + "</td>";
                        $('#accident-partslength').append(codename);
                        var brokenparts = "<td>车损<br/>部位</td>\n                                 <td colspan=\""
                                          + (index == 1 ? 4 : 5)
                                          + "\" style='text-align: left;text-indent: 20px'>□车头 □左前角 □右前角 □车尾 □左后角 □右后角 □车身左侧 □车身右侧</td>";
                        $('#accident-brokenParts').append(brokenparts);
                        var reason = "<td>事故<br/>情形</td>\n            <td colspan=\""
                                     + (index == 1 ? 4 : 5)
                                     +
                                     "\">\n                <p>□停车 □倒车 □逆行 □溜车 □开关车门</p>\n                <p>□违反交通信号灯 □变更车道与其他车辆刮擦 □未保持安全车距与前车追尾</p>\n                <p>□未按规定让行 □其他</p>\n            </td>";
                        $('#accident-reason').append(reason);
                        var p = '□全部责任&nbsp;&nbsp;□无责任';
                        var pastrespon = '□' + value.responsibility;
                        var newrespon = '☑' + value.responsibility;
                        var newp = p.replace(pastrespon, newrespon);
                        var responsibility = "<td>事故<br/>责任</td>\n" + "            <td colspan=\""
                                             + (index == 1 ? 4 : 5) + "\">\n" + "                <p>" + newp
                                             + "</p>\n" + "            </td>";
                        $('#accident-responsibility').append(responsibility);
                        var sig = code[index] + "<img src='"+value.signaturePic+"'/>";
                        $('#accident-signature').append(sig);
                    }
                });

                var notResponseCodes = "";
                accidentParts.forEach(function(value, index) {
                    if (value.responsibility == "无责任") {
                        notResponseCodes += "<span style='margin-right:20px;'>" + code[index] + "</span>";
                        var now = new Date();
                        var sig = code[index] + "<img src='"+value.signaturePic+"'/>";
                        $('#accident-signature').append(sig);
                    }
                });
                var now = new Date();
                $('#signatureTime').append(format(now.getTime(), "yyyy年MM月dd日"));
                var codename = "<td colspan=\"5\">" + notResponseCodes + "</td>";
                $('#accident-partslength').append(codename);
                var brokenparts = "<td>车损<br/>部位</td><td colspan=\"4\">□车头 □左前角 □右前角 □车尾 □左后角 □右后角 □车身左侧 □车身右侧</td>";
                $('#accident-brokenParts').append(brokenparts);
                var reason = "<td>事故<br/>情形</td><td colspan=\"4\"><p>□停车 □倒车 □逆行 □溜车 □开关车门</p>\n                <p>□违反交通信号灯 □变更车道与其他车辆刮擦 □未保持安全车距与前车追尾</p>\n                <p>□未按规定让行 □其他</p>\n            </td>";
                $('#accident-reason').append(reason);
                var responsibility = "<td>事故<br/>责任</td>" + "            <td colspan=\"4\">"
                                     + "<p>□全部责任&nbsp;&nbsp;☑无责任</p>" + "</td>";
                $('#accident-responsibility').append(responsibility);
            }
        } else {
            if (data.msg!==null&&typeof data.msg!=="undefined") {
                 layer.alert(data.msg);
            }else{
                layer.alert("系统繁忙!");
            }
        }
    }
});

function getCarType(carType) {
    var type = "";
    for (var i = 0; i < carTypeArr.length; i++) {
        if (carTypeArr[i].id == carType) {
            type = carTypeArr[i].name;
            break;
        }
    }
    return type;
}

var carTypeArr = new Array();
carTypeArr.push(new cartype());

function cartype(id, name) {
    this.id = id;
    this.name = name;
}

carTypeArr.push(new cartype("02", "小型汽车"));
carTypeArr.push(new cartype("52", "小型新能源汽车"));
carTypeArr.push(new cartype("01", "大型汽车"));
carTypeArr.push(new cartype("51", "大型新能源汽车"));
carTypeArr.push(new cartype("07", "普通摩托车"));
carTypeArr.push(new cartype("16", "教练汽车"));
carTypeArr.push(new cartype("31", "武警号牌"));
carTypeArr.push(new cartype("23", "警用汽车"));
carTypeArr.push(new cartype("15", "挂车"));
carTypeArr.push(new cartype("03", "使馆汽车"));
carTypeArr.push(new cartype("04", "领馆汽车"));
carTypeArr.push(new cartype("05", "境外汽车"));
carTypeArr.push(new cartype("06", "外籍汽车"));
carTypeArr.push(new cartype("08", "轻便摩托车"));
carTypeArr.push(new cartype("09", "使馆摩托车"));
carTypeArr.push(new cartype("10", "领馆摩托车"));
carTypeArr.push(new cartype("11", "境外摩托车"));
carTypeArr.push(new cartype("12", "外籍摩托车"));
carTypeArr.push(new cartype("13", "低速车"));
carTypeArr.push(new cartype("14", "拖拉机"));
carTypeArr.push(new cartype("17", "教练摩托车"));
carTypeArr.push(new cartype("18", "试验汽车"));
carTypeArr.push(new cartype("19", "试验摩托车"));
carTypeArr.push(new cartype("20", "临时入境汽车"));
carTypeArr.push(new cartype("21", "临时入境摩托车"));
carTypeArr.push(new cartype("22", "临时行驶车"));
carTypeArr.push(new cartype("24", "警用摩托"));
carTypeArr.push(new cartype("25", "原农机号牌"));
carTypeArr.push(new cartype("26", "香港入出境车"));
carTypeArr.push(new cartype("27", "澳门入出境车"));
carTypeArr.push(new cartype("32", "军队号牌"));

function success_printCallback(data) {
    $("#loading").addClass("hide");
    $("#shade").addClass("hide");
    if ((data.msg!==null)&&(typeof data.msg!=="undefined")) {
        layer.alert(data.msg);
    }else{
        layer.alert("系统繁忙!");
    }
}

$('#printPage').click(function() {
    var imgs = $('#accident-signature img');
    var buttons = $('#accident-signature button');
    if (imgs.length < buttons.length) {
        alert('请先完成签名');
        return false;
    }
    $("#loading").removeClass("hide");
    $("#shade").removeClass("hide");
    //协议书打印
    var accidentId=urladdress.substring(urladdress.indexOf('accidentid=')+'accidentid='.length,urladdress.indexOf('&'));
    var script = document.createElement("script");
    script.type = "text/javascript";
    script.src = "http://127.0.0.1:9999/ae0c6017d105b4ce/excel?t="+(new Date().getTime())+"&accidentId=" + accidentId+"&idCardNumber="+idCardNumber+"&callbackparam=success_printCallback";

    document.body.appendChild(script);




    $("#printPage").hide();



});