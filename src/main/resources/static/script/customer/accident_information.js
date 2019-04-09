$(function() {
    $("#idCard").focus();
    imgUrlL = $(".upload-photo-l-img").attr("src");
    imgUrlR = $(".upload-photo-r-img").attr("src");
});
/*******************************优化HTML页面代码(键盘优化)*********************************/
var carTypeLibrary = [
    {name: "小型汽车", code: "02"},
    {name: "小型新能源汽车", code: "52"},
    {name: "大型汽车", code: "01"},
    {name: "大型新能源汽车", code: "51"},
    {name: "普通摩托车", code: "07"},
    {name: "教练汽车", code: "16"},
    {name: "武警号牌", code: "31"},
    {name: "警用汽车", code: "23"},
    {name: "挂车", code: "15"},
    {name: "使馆汽车", code: "03"},
    {name: "领馆汽车", code: "04"},
    {name: "境外汽车", code: "05"},
    {name: "外籍汽车", code: "06"},
    {name: "轻便摩托车", code: "08"},
    {name: "使馆摩托车", code: "09"},
    {name: "领馆摩托车", code: "10"},
    {name: "境外摩托车", code: "11"},
    {name: "外籍摩托车", code: "12"},
    {name: "低速车", code: "13"},
    {name: "拖拉机", code: "14"},
    {name: "教练摩托车", code: "17"},
    {name: "试验汽车", code: "18"},
    {name: "试验摩托车", code: "19"},
    {name: "临时入境汽车", code: "20"},
    {name: "临时入境摩托车", code: "21"},
    {name: "临时行驶车", code: "22"},
    {name: "警用摩托", code: "23"},
    {name: "原农机号牌", code: "25"},
    {name: "香港入出境车", code: "26"},
    {name: "澳门入出境车", code: "27"},
    {name: "军队号牌", code: "32"}];
for (var i = 0; i < carTypeLibrary.length; i++) {
    $("#species").append("<option value='" + carTypeLibrary[i].code + "'>" + carTypeLibrary[i].name + "</option>");
}

function creatLi(arr, str) {
    for (var i = 0; i < arr.length; i++) {
        $(str).append("<li name='" + arr[i] + "'>" + arr[i] + "</li>");
    }
}

var keyNumberLibrary = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "X"];
var keyPhoneLibrary = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"];
var keyCarWordLibrary = [
    "川",
    "京",
    "津",
    "冀",
    "晋",
    "蒙",
    "辽",
    "吉",
    "黑",
    "沪",
    "苏",
    "浙",
    "皖",
    "闽",
    "赣",
    "鲁",
    "豫",
    "鄂",
    "湘",
    "粤",
    "桂",
    "琼",
    "渝",
    "贵",
    "云",
    "藏",
    "陕",
    "甘",
    "青",
    "宁",
    "新"];
var keyCarLitterLibrary = [
    "A",
    "B",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "J",
    "K",
    "L",
    "M",
    "N",
    "P",
    "Q",
    "R",
    "S",
    "T",
    "U",
    "V",
    "W",
    "X",
    "Y",
    "Z",
    "警",
    "学",
    "挂",
    "港",
    "澳",
    "使",
    "领",
    "消",
    "边",
    "通",
    "森",
    "金",
    "电",
    "备"];
var keycaseLitterLibrary = [
    "A",
    "B",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "J",
    "K",
    "L",
    "M",
    "N",
    "O",
    "P",
    "Q",
    "R",
    "S",
    "T",
    "U",
    "V",
    "W",
    "X",
    "Y",
    "Z"];
creatLi(keyNumberLibrary, ".key-number");
$(".key-number").append("<li id='delNumber' class='delNumber'>删除</li>");
creatLi(keyPhoneLibrary, ".key-phone");
$(".key-phone").append("<li id='delPhone' class='delPhone'>删除</li>");
creatLi(keyCarWordLibrary, ".key-car-word");
creatLi(keyCarLitterLibrary, ".key-car-all .key-car-litter");
creatLi(keycaseLitterLibrary, ".insurance-policy-no .key-car-litter");
creatLi(keyPhoneLibrary, ".key-car-no");
$(".key-car-number>.key-car-no").append("<li id='delCarNo' class='delCarNo'>删除</li>");
$(".insurance-policy-no>.key-car-no").append("<li id='policyNo' class='delCarNo'>删除</li>");
creatLi(keycaseLitterLibrary, ".key-case-litter");
creatLi(keyPhoneLibrary, ".key-case-no");
$(".key-case-no").append("<li id='delCaseNumber' class='delCarNo'>删除</li>");

/**************************************end*********************************************/
var imgUrlL;
var imgUrlR;
var imgUrlCom = "../../static/image/customer/icon/bg_img.gif";
/************************日期触发事件**************************/
$(".ac-date-box").click(function() {
    $(".boxshaw,.mask").removeClass("hide");
});
$(".mask").click(function() {
    $(".boxshaw,.mask").addClass("hide");
});
$('#schedule-box').on('click', '.current-month', function() {
    $(".boxshaw,.mask").toggleClass("hide");
    return false;
});
/******************************* 证件号输入 *************************************/
$("#idCard").on("click", function() {
    $(".key").find(".key-number").removeClass("hide").siblings().addClass("hide");
    return false;
});
$("#idCard").focus(function() {
    $(".key").find(".key-number").removeClass("hide").siblings().addClass("hide");
    return false;
});

// 验证身份证
function isCardNo(card) {
    var reg = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    return reg.test(card);
}

var idCardNotCustom = true;//身份证是否通过六合一验证
var idCardState = {
    "A": "正常",
    "B": "超分",
    "C": "转出",
    "D": "暂扣",
    "E": "撤销",
    "F": "吊销",
    "G": "注销",
    "H": "违法未处理",
    "I": "事故未处理",
    "J": "停止使用",
    "K": "扣押",
    "L": "锁定",
    "M": "逾期未换证",
    "N": "延期换证",
    "P": "延期体检",
    "R": "注销可恢复",
    "S": "逾期未审验",
    "T": "逾期未审验",
    "U": "扣留"
};

function getIdName() {
    if (isCardNo($.trim($('#idCard').val()))) {
        $("#phone").focus();
        isLoading(true, "#uName");
        // $("#uName").siblings("span.load").toggleClass("load-img");
        $.get("/customer/validUniqueIdNumber?t=" + new Date().getTime(), {idNumber: $('#idCard').val()},
          function(result) {
              // isLoading(false,"#uName");
              if (result.result == true) {
                  $.get("/customer/validIdNumber?t=" + new Date().getTime(), {idNumber: $('#idCard').val()},
                    function(result) {
                        result = result.msg;//{"total":1,"data":{},"statusText":"成功","status":10000}
                        if (result != null && typeof result != "undefined") {
                            if (result.status && result.status == 1000) {//如果正常返回，有status属性，如果网络异常，则没有status属性
                                var zt = result.data.zt;
                                if (zt == "D" || zt == "E" || zt == "F" || zt == "G" || zt == "J" || zt == "K" || zt ==
                                    "L" || zt == "M" || zt == "S" || zt == "U") {
                                    layer.alert("您的驾驶证已 " + idCardState[zt] + ",不能快处处理。");
                                    $("#idCard").val("");
                                    $("#uName").val("");
                                    canNext();
                                    return false;
                                } else {
                                    $("#uName").val(result.data.xm);
                                    $("#helpMsg").html("查询成功").removeClass("help-msg-red");
                                    canNext();
                                }
                                idCardNotCustom = true;
                                isLoading(false, "#uName");
                                canNext();
                            } else {
                                idCardNotCustom = false;
                                // $("#uName").val("");
                                $("#idCard").focus();
                                if (result.statusText && result.statusText != "") {
                                    canNext();
                                    layer.alert(result.statusText);
                                } else {
                                    isLoading(false, "#uName");
                                    canNext();
                                    layer.alert("交警六合一系统异常，请稍侯再试或手动输入");
                                }
                                $("#helpMsg").html("没有记录").addClass("help-msg-red");
                                isLoading(false, "#uName");
                                canNext();
                                return false;
                            }
                            canNext();
                        } else {
                            isLoading(false, "#uName");
                            layer.alert("交警六合一系统异常，请稍侯再试或手动输入");
                            return false;
                        }
                    });
                  canNext();
              } else {
                  isLoading(false, "#uName");
                  if (result.msg != null && typeof result.msg != "undefined") {
                      canNext();
                      layer.alert(result.msg);
                  } else {
                      canNext();
                      layer.alert("系统繁忙,请重试。");
                  }
                  $("#idCard").val("");
                  $("#uName").val("");
                  $("#helpMsg").html("");
                  $("#idCard").focus();
                  canNext();
                  return false;
              }
              canNext();
          });
        canNext();
    } else {
        $("#idCard").focus();
        canNext();
    }
    canNext();
}

$("#idCard").keyup(function(e) {
    e = e || window.event;
    if (e.keyCode == 8 || e.keyCode == 46) {//删除键
        //不处理
    } else if ($("#idCard").val().length == 18) {
        getIdName();
        canNext();
    }
    canNext();
});
$("ul.key-number>li").click(function() {
    // var idNumber = $("#idCard").val();
    var keyNumber = $(this).html();
    if (keyNumber === "删除") {
        $("#idCard").val($("#idCard").val().substring(0, $("#idCard").val().length - 1));
        $("#uName").val("");
        $("#helpMsg").html("");
        canNext();
    } else {
        if ($("#idCard").val().length < 18) {
            $("#idCard").val($("#idCard").val() + keyNumber);
            canNext();
        }
        canNext();
    }
    // $("#idCard").val(idNumber);
    getIdName();
    canNext();
    return false;
});

// TODO :手机号码自动添加事件
/******************************电话号码验证*********************************/
function isPhone(phone) {
    var reg = /^[1][0-9]{10}$/;
    return reg.test(phone);
}

/****************************** 电话号码输入 **********************************/
$("#phone").click(function() {
    $(".key").find(".key-phone").removeClass("hide").siblings().addClass("hide");
    return false;
});
$("#phone").focus(function() {
    $(".key").find(".key-phone").removeClass("hide").siblings().addClass("hide");
    return false;
});
// var phoneNumber="";
// $("#phone").keyup(function(){
//     phoneNumber=$("#phone").val();
// });

/***************监控电话号码框的变化*****************/
$("#phone").change(function() {
    canNext();
});
$("#phone").keyup(function() {
    canNext();
});

$("ul.key-phone>li").click(function() {
    // var phone = $("#phone");
    // var phoneNumber = phone.val();
    if (isPhone($("#phone").val())) {
        var keyNumber = $(this).html();
        if (keyNumber === "删除") {
            $("#phone").val($("#phone").val().substring(0, $("#phone").val().length - 1));
            $("#phone").focus();
        } else {
            $("#phone").blur();
        }
        canNext();
    } else {
        var keyNumber = $(this).html();
        if (keyNumber === "删除") {
            $("#phone").val($("#phone").val().substring(0, $("#phone").val().length - 1));
            $("#phone").focus();
            canNext();
        } else {
            if ($("#phone").val().length < 11) {
                $("#phone").val($("#phone").val() + keyNumber);
                $("#phone").focus();
                canNext();
            } else {
                $("#phone").blur();
                canNext();
            }
        }
        canNext();
    }
    // phone.val(phoneNumber);
    canNext();
    return false;
});

/******************************* 事故车辆信息录入 ***********************************/
function hasChuan() {
    if ($("#license").val().substring(0, 1) == "川") {
        $(".key-car-word").addClass("hide");
        $(".key-car-litter,.key-car-no").removeClass("hide");
    } else {
        $(".key-car-word").removeClass("hide");
        $(".key-car-litter,.key-car-no").removeClass("hide");
    }
    canNext();
}

$("#license").click(function() {
    $(".key-car-number").removeClass("hide").siblings().addClass("hide");
    hasChuan();
    canNext();
    return false;
});
// var carNumber = $("#license").val();
$("#license").keyup(function() {
    hasChuan();
    if ($("#species").val() == "02") {
        $("#license").attr("maxlength", 7);
    } else if ($("#species").val() == "52") {
        $("#license").attr("maxlength", 8);
    } else {
        $("#license").attr("maxlength", "");
    }
    canNext();
    // carNumber=$("#license").val();
});

$(".key-car-number li:not('#delCarNo')").click(function(e) {
    // carNumber = $("#license").val();
    var c = $(e.target).attr("name");
    if ($("#species").val() == "02" && $("#license").val().length > 6) {
        return false;
    } else if ($("#species").val() == "52" && $("#license").val().length > 7) {
        return false;
    }
    $("#license").val($("#license").val() + c);
    // $("#license").val(carNumber);
    hasChuan();
    canNext();
    return false;
});
$("#delCarNo").click(function() {
    // carNumber = $("#license").val();
    $("#license").val($("#license").val().substring(0, $("#license").val().length - 1));
    // $("#license").val(carNumber);
    $("#carName").val("");
    hasChuan();
    canNext();
    return false;
});

/***************监控名字框的变化*****************/
$("#species").change(function() {
    canNext();
});
$("#uName").change(function() {
    canNext();
});
$("#uName").keyup(function() {
    canNext();
});

/***************监控名字框的变化*****************/
$("#carName").change(function() {
    canNext();
});
$("#carName").keyup(function() {
    canNext();
});

var carNotCustom = true;//驾驶证是否通过六合一验证
var carName = "";
$(".search").click(function() {
    var carNumber = $("#license").val();
    var carType = $("#species").val();
    $(".search").addClass("disable-href").siblings("span").addClass("load-search");
    $(".search").html("");
    $.get("/customer/validUniqueIdNumber?t=" + new Date().getTime(), {carNumber: carNumber},
      function(result) {
          if (result.result == true) {
              $.get("/customer/validCarNumber?t=" + new Date().getTime(),
                {carType: $("#species").val(), carNumber: carNumber}, function(result) {
                    $(".search").removeClass("disable-href").siblings("span").removeClass("load-search");
                    $(".search").html("查询");
                    result = result.msg;//{"total":1,"data":{},"statusText":"成功","status":10000}
                    if (result != null && typeof result != "undefined") {
                        if (result.status && result.status == 1000) {//如果正常返回，有status属性，如果网络异常，则没有status属性
                            $("#carName").val("*" + result.data.syr.substring(1, result.data.syr.length));
                            carName = result.data.syr;
                            canNext();
                        } else {
                            carName = "";
                            if (result.statusText && result.statusText != "") {
                                canNext();
                                layer.alert(result.statusText);
                            } else {
                                canNext();
                                layer.alert("交警六合一系统异常，请稍侯再试或手动输入");
                                return false;
                            }
                            //                			if(result.msg!=null&&typeof result.msg!="undefined"){
                            //                            $("#carName").val(result.statusText);
                            //                    		}else{
                            //                    			layer.alert("系统繁忙，请稍侯再试");
                            //                    		}
                            canNext();
                        }
                        canNext();
                    } else {
                        canNext();
                        layer.alert("交警六合一系统异常，请稍侯再试或手动输入");
                        return false;
                    }
                    canNext();
                });
              carNotCustom = true;
              canNext();
          } else {
              $(".search").removeClass("disable-href").siblings("span").removeClass("load-search");
              $(".search").html("查询");
              carNotCustom = false;
              if ((result.msg !== null) && (typeof result.msg !== "undefined")) {
                  layer.alert(result.msg);
                  canNext();
              } else {
                  canNext();
                  layer.alert("系统繁忙,请重试。");
              }
              $("#species").focus();
              $("#carName").val("");
              canNext();
              return false;
          }
          canNext();
      });
    $.get("/customer/accident/getInsuranceInfoByCarMark?t=" + new Date().getTime(),
      {carMark: carNumber, carType: carType},
      function(result) {
          if (result.result == true && result.msg != null) {
              $("#insurance").find("option[data-code='" + result.msg.code + "']").attr("selected", "selected");
          }
      });
    return false;
});
$("#carName").focus(function() {
    if ($("#carName").val() === "没有查询到相关机动车信息") {
        $("#carName").val("");
        return false;
    }
    canNext();
});

/*****************************录入投保公司信息*******************************/
$(function() {
    $.get("/customer/listInsuranceCompany?time=" + new Date().getTime(), function(result) {
        if (result.result == true) {
            for (var i = 0; i < result.msg.length; i++) {
                $("#insurance").append(
                  "<option data-code='" + result.msg[i].code + "' value='" + result.msg[i].name + "'>" +
                  result.msg[i].name +
                  "</option>");
            }
        }
    });
});
$("#InsurancePolicyNo").click(function() {
    $(".insurance-policy-no").removeClass("hide").siblings().addClass("hide");
    return false;
});
// var InsurancePolicyNo = $("#InsurancePolicyNo").val();

// $("#InsurancePolicyNo").keyup(function(){
//     InsurancePolicyNo=$("#InsurancePolicyNo").val();
// });

$(".insurance-policy-no li:not('#policyNo')").click(function(e) {
    // InsurancePolicyNo = $("#InsurancePolicyNo").val();
    var c = $(e.target).attr("name");
    $("#InsurancePolicyNo").val($("#InsurancePolicyNo").val() + c);
    // $("#InsurancePolicyNo").val(InsurancePolicyNo);
    canNext();
    return false;
});
$("#policyNo").click(function() {
    // InsurancePolicyNo = $("#InsurancePolicyNo").val();
    $("#InsurancePolicyNo").val($("#InsurancePolicyNo").val().substring(0, $("#InsurancePolicyNo").val().length - 1));
    // $("#InsurancePolicyNo").val(InsurancePolicyNo);
    canNext();
    return false;
});

//备案号
// var caseNumber=$("#caseNumber").val();
//
// $("#caseNumber").keyup(function(){
//     caseNumber=$("#caseNumber").val();
// });

$("#caseNumber").click(function() {
    $(".key-case-number").removeClass("hide").siblings().addClass("hide");
    return false;
});
$(".key-case-number li:not('#delCaseNumber')").click(function(e) {
    // caseNumber = $("#caseNumber").val();
    var c = $(e.target).attr("name");
    $("#caseNumber").val($("#caseNumber").val() + c);
    // $("#caseNumber").val(caseNumber);
    canNext();
    return false;
});
$("#delCaseNumber").click(function() {
    // caseNumber = $("#caseNumber").val();
    $("#caseNumber").val($("#caseNumber").val().substring(0, $("#caseNumber").val().length - 1));
    // $("#caseNumber").val(caseNumber);
    canNext();
    return false;
});

/**************************保存驾驶人和车辆信息*************************/
var currentUrl = window.location.href;
var ct = currentUrl.split("?");
var count = getQueryString("count");
if (count == null || count == "") {
    count = 0;
}
if (count == 4) {
    $('#next').hide();
} else {
    $('#next').show();
}
if (count >= 1) {
    $("#finish").removeClass("hide");
} else {
    $("#finish").addClass("hide");
}
if (count > 1) {
    $("#finish").removeClass("disable-href");
}
var identity = [];
var identityNumber = [
    {"person": "[当事人：甲方]"},
    {"person": "[当事人：乙方]"},
    {"person": "[当事人：丙方]"},
    {"person": "[当事人：丁方]"},
    {"person": "[当事人：戊方]"}];
var identityPerson = ["乙方", "丙方", "丁方", "戊方"];
//刷新当事人
$("#next").html("录入" + identityPerson[count]);
$("#identity").html(identityNumber[count].person);

function isNotEmpty() {
    var imgUrlL1 = $(".upload-photo-l-img").attr("src");
    var imgUrlR1 = $(".upload-photo-r-img").attr("src");
    if (($("#idCard").val().length !== 18) || ($("#uName").val() === "") || ($("#license").val() === "") ||
        ($("#phone").val() === "") ||
        (imgUrlL === imgUrlL1) || (imgUrlR === imgUrlR1) || ($("#insurance").val() === "") ||
        ($("#InsurancePolicyNo").val() === "") || ($("#h3Ele").val() === "")) {
        if ($("#idCard").val().length !== 18) {
            $("#idCard").addClass("reminder");
        } else {
            $("#idCard").removeClass("reminder");
        }
        if ($("#uName").val() === "") {
            $("#uName").addClass("reminder");
        } else {
            $("#uName").removeClass("reminder");
        }
        if (($("#phone").val() === "")) {
            $("#phone").addClass("reminder");
        } else {
            $("#phone").removeClass("reminder");
        }
        if ($("#license").val() === "") {
            $("#license").addClass("reminder");
        } else if (($("#species").val() == "52" || $("#species").val() == "51") && $("#license").val().length !== 8) {
            $("#license").addClass("reminder");
        } else if (($("#species").val() != "52" && $("#species").val() != "51") && $("#license").val().length !== 7) {
            $("#license").addClass("reminder");
        } else {
            $("#license").removeClass("reminder");
        }
        if ($("#carName").val() === "") {
            $("#carName").addClass("reminder");
        } else {
            $("#carName").removeClass("reminder");
        }
        if ($("#insurance").val() === "") {
            $("#insurance").addClass("reminder");
        } else {
            $("#insurance").removeClass("reminder");
        }
        if ($("#InsurancePolicyNo").val() === "") {
            $("#InsurancePolicyNo").addClass("reminder");
        } else {
            $("#InsurancePolicyNo").removeClass("reminder");
        }
        if ($("#h3Ele").val() === "") {
            $("#h3Ele").addClass("reminder");
        } else {
            $("#h3Ele").removeClass("reminder");
        }
        if (imgUrlL === imgUrlL1) {
            $(".upload-photo-tip-l").removeClass("hide");
        } else {
            $(".upload-photo-tip-l").addClass("hide");
        }
        if (imgUrlR === imgUrlR1) {
            $(".upload-photo-tip-r").removeClass("hide");
        } else {
            $(".upload-photo-tip-r").addClass("hide");
        }
        return false;
    } else {
      if (imgUrlL === imgUrlL1) {
        $(".upload-photo-tip-l").removeClass("hide");
      } else {
        $(".upload-photo-tip-l").addClass("hide");
      }
      if (imgUrlR === imgUrlR1) {
        $(".upload-photo-tip-r").removeClass("hide");
      } else {
        $(".upload-photo-tip-r").addClass("hide");
      }

        return true;
    }
}

function uploadDriverAndDriving(callback) {
    var caseNumber = $("#caseNumber").val();
    var insuranceCompany = $("#insurance").val();
    var insuranceCompanyCode=$("#insurance option:selected").attr('data-code');
    var insuranceCompulsory = $("#InsurancePolicyNo").val();
    var insuranceDate = $("#h3Ele").val().replace("-", "/").replace("-", "/");
    var idNumber = $("#idCard").val();
    var name = $("#uName").val();
    var driverUrl = $("#driverUrl").attr("src");
    var drivingUrl = $("#drivingUrl").attr("src");

    // if(!indexOf("*")){
    //     carName = $("#carName").val();
    // }
    if (carName == "") {
        carName = $("#carName").val();
    }
    // var ownName = carName;
    // if ((ownName == "") || (ownName == "undefined")) {
    //     ownName = "未查询到相关信息";
    // }
    var carNumber = $("#license").val();
    if (isPhone($.trim($("#phone").val())) == true) {
        $("#phone").removeClass("reminder");
        var phone = $("#phone").val();
        if (idNumber && carNumber) {
            $.post("/customer/driverAndDriving?time=" + new Date().getTime(),
              {
                  driver: JSON.stringify(
                    {
                        idNumber: idNumber,
                        name: name,
                        phone: phone,
                        driverLicencePicUrl: driverUrl,
                        accidentIndex: count,
                        idCardNotCustom: idCardNotCustom,
                        carNotCustom: carNotCustom
                    }),
                  driving: JSON.stringify({
                      carType: $("#species").val(),
                      carNumber: carNumber,
                      ownName: carName,
                      drivingLicencePicUrl: drivingUrl,
                      insuranceCompanyCode:insuranceCompanyCode,
                      insuranceCompany: insuranceCompany,
                      insuranceCompulsory: insuranceCompulsory,
                      insuranceDate: insuranceDate,
                      caseNumber: caseNumber
                  })
              }, function(data) {
                  if (data.result == true) {
                      callback();
                  } else {
                      layer.alert("系统繁忙，稍后再试！");
                  }
              });
        }
    } else {
        $("#phone").addClass("reminder");
        $("#phone").focus();
    }
}

var nextButtonEnable = true;
$("#next").click(function() {
    if (!nextButtonEnable) {
        return;
    }
    nextButtonEnable = false;
    setTimeout(function() {nextButtonEnable = true;}, 5000);
    $.get("/customer/validUniqueIdNumber?t=" + new Date().getTime(), {idNumber: $('#idCard').val()}, function(result) {
        if (result.result == true) {
            if (isNotEmpty()) {
                count++;
                uploadDriverAndDriving(function() {
                    location.href = "accident_information.html?count=" + count + "";
                });
            } else {
                isNotEmpty();
            }
        } else {
            if ((result.msg !== null) && (typeof result.msg !== "undefined")) {
                layer.alert(result.msg);
            } else {
                layer.alert("系统繁忙!");
            }
            $("#species").focus();
            $("#carName").val("");
            return false;
        }
    });
});
$("#finish").click(function() {
    if (!nextButtonEnable) {
        return;
    }
    nextButtonEnable = false;
    setTimeout(function() {nextButtonEnable = true;}, 5000);
    if (count > 1) {
        if (isNotEmpty()) {
            count++;
            uploadDriverAndDriving(function() {
                location.href = "accident_info_upload.html";
            });
        } else {
            layer.confirm('是否放弃当前当事人（' + identityNumber[count].person.substring(5, 7) + '）信息录入！', {
                btn: ['是', '否'] //按钮
            }, function() {
                location.href = "accident_info_upload.html";
            }, function() {
                isNotEmpty();
            });
        }
    } else {
        if (isNotEmpty()) {
            count++;
            uploadDriverAndDriving(function() {
                location.href = "accident_info_upload.html";
            });
        } else {
            isNotEmpty();
        }
    }
});

/*********************************判断页面上的元素是否填满下一步可点击*************************************/
function canNext() {
    isNotEmpty();
    if (isPhone($.trim($("#phone").val())) == true) {
        $("#phone").removeClass("reminder");
    } else {
        $("#phone").addClass("reminder");
    }
    var imgUrlL1 = $(".upload-photo-l-img").attr("src");
    var imgUrlR1 = $(".upload-photo-r-img").attr("src");
    if (count > 1) {
        if (($("#idCard").val().length == 18) && ($("#uName").val() !== "") && ($("#carName").val() !== "") &&
            ($("#phone").val().length == 11) && ($("#license").val().length > 6) &&
            ($("#InsurancePolicyNo").val() != "") && ($("#h3Ele").val() != "") && (imgUrlL1 != imgUrlL) &&
            (imgUrlR1 != imgUrlR) && (imgUrlL1 != imgUrlCom) && (imgUrlR1 != imgUrlCom)) {
            $("#next").removeClass("disable-href").prop("disabled", false);
        } else {
            $("#next").addClass("disable-href").prop("disabled", true);
        }
    } else {
        if (($("#idCard").val().length == 18) && ($("#uName").val() !== "") && ($("#carName").val() !== "") &&
            ($("#phone").val().length == 11) && ($("#license").val().length > 6) &&
            ($("#InsurancePolicyNo").val() != "") && ($("#h3Ele").val() != "") && (imgUrlL1 != imgUrlL) &&
            (imgUrlR1 != imgUrlR) && (imgUrlL1 != imgUrlCom) && (imgUrlR1 != imgUrlCom)) {
            $("#next").removeClass("disable-href").prop("disabled", false);
            $("#finish").removeClass("disable-href").prop("disabled", false);
        } else {
            $("#next").addClass("disable-href").prop("disabled", true);
            $("#finish").addClass("disable-href").prop("disabled", true);
        }
    }
}
