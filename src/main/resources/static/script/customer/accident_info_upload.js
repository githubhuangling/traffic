/************************日期触发事件**************************/
$(function() {
    var time = format(new Date().getTime(), "yyyy-MM-dd");
    $("#h3Ele").val(time);
})
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

$("#idCard").on("input", function() {
    // TODO: 添加键盘支持
});

//监听小时选择,若日期未选择,这必须先选择日期
$('#hour').change(function() {
    if ($('#h3Ele').val() == "") {
        $('#hour').val(0);
        layer.alert('请先选择日期');
        $('#h3Ele').click();
        return false;
    }
    var typeinArr = $('#h3Ele').val().split('-');
    var typeinDate = new Date(typeinArr[0], typeinArr[1] - 1, typeinArr[2]);
    var nowDate = new Date();
    var todayEnd = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate());
    if (typeinDate > todayEnd) {
        layer.alert('请选择正确的时间');
    }
    if (typeinDate.getTime() == todayEnd.getTime()){
        if (($('#hour').val()) > (nowDate.getHours())){
            layer.alert('请选择正确的时间');
            $('#hour').val(0);
        }
    }
});

/************************图片上传事件*************************/
$(function() {
    $.get("/customer/getDrivingLicenceList?time="+new Date().getTime(), function(data) {
        if (data.result==true) {
            drivingLicenceList = data.msg;
            // console.log(data);
            for (var i = 0; i < drivingLicenceList.length; i++) {
                $("select.drivingLicence").append("<option value='+drivingLicenceList[i].id+'>" +
                                                  drivingLicenceList[i].number + "</option>");
            }
        }
    });
});
var drivingLicenceList = [];
var uploadedImgNames = new Array();
$("input[type=file]").change(function(e) {
    if(e.target.files[0]==null){
        return;
    }
    var fileName = e.target.files[0].name;
    //判断文件名称是否已存在名单中
        uploadedImgNames.push(fileName);

    var formFile = new FormData();
    formFile.append("file", e.target.files[0]);
    $.ajax({
        url: "/customer/upload/uploadFile",
        type: 'POST',
        data: formFile,
        processData: false,
        contentType: false,
        dataType: "json",
        success: function(data) {
            if (data.result==true) {
                var mediaPart = $(e.target).attr("id").substring(9);
                // console.log(e.target);
                $(e.target).parent().before("<li name='"+mediaPart+"' class=\"add-accident\" fileName='"+fileName+"'>\n" +
                                            "                    <div class='img'>\n" +
                                            "                        <img src='/static/image/customer/upload.png' class='acUpFile'/> \n" +
                                            "                        <span class='ac-delete acDelete'>×</span> \n" +
                                            "                        <footer class='ac-footer'>\n" +
                                            "                            <select class=\"drivingLicence\"></select>\n" +
                                            "                        </footer>\n" +
                                            "                        <div class='clear'></div>\n" +
                                            "                    </div>\n" +
                                            "                </li>");
                // console.log($(e.target).parent().parent().children().eq(1));
                if (($(e.target).parent().parent().children().eq(0).find("select.drivingLicence").length) <
                    drivingLicenceList.length) {
                    for (var i = 0; i < drivingLicenceList.length; i++) {
                        $(e.target).parent().parent().children().eq($(e.target).parent().parent().children().length -
                                                                    2).find("select.drivingLicence").append("<option value='"+drivingLicenceList[i].id+"'>"+drivingLicenceList[i].number+"</option>");
                    }
                }
                $(e.target).parent().parent().children().eq($(e.target).parent().parent().children().length - 2).find(
                  ".img>img").attr("src", data.msg);
                $(e.target)[0].value='';
            }
            $("span.acDelete").click(function(e) {
                var index = uploadedImgNames.indexOf($(e.target).parent().parent().attr('fileName'));
                uploadedImgNames.splice(index, 1);
                $(e.target).parent().parent().remove();
            });
        }
    });
    canNext();
});
/***************************手机端图片上传事件联动********************************/
var timer = setInterval(function() {
    $.get("/customer/getUploadMediaM?time="+new Date().getTime(), function(data) {
        if (data.result==true) {
            if (data.msg!=null && data.msg.length > 0) {
                for (var i = 0; i < data.msg.length; i++) {
                    if (data.msg[i].urls.length > 0) {
                        for (var j = 0; j < data.msg[i].urls.length; j++) {
                            var m = "#mediaPart" + data.msg[i].part;
                            $(m).parent().before("<li class=\"add-accident\" name=\""+data.msg[i].part+"\">\n" +
                                                 "<div class='img'>\n" +
                                                 "<img src='"+data.msg[i].urls[j]+"' class='acUpFile'/> \n" +
                                                 "<span class='ac-delete acDelete'>×</span> \n" +
                                                 "<footer class='ac-footer'>\n" +
                                                 "<select class=\"drivingLicence\"></select>\n" +
                                                 "</footer>\n" +
                                                 "<div class='clear'></div>\n" +
                                                 "</div>\n" +
                                                 "</li>");
                            if (($(m).parent().parent().children().eq(0).find("select.drivingLicence").length) < drivingLicenceList.length) {
                                for (var k = 0; k < drivingLicenceList.length; k++) {
                                    $(m).parent().parent().children().eq($(m).parent().parent().children().length - 2).find("select.drivingLicence").append(
                                      "<option value='"+drivingLicenceList[k].id+"'>"+drivingLicenceList[k].number+"</option>");
                                }
                            }
                        }
                    }
                }
                $("span.acDelete").click(function(e) {
                    var index = uploadedImgNames.indexOf($(e.target).parent().parent().attr('fileName'));
                    uploadedImgNames.splice(index, 1);
                    $(e.target).parent().parent().remove();
                });
            }

        }
        else {

        }
    });
    canNext();
}, 1000);
/***************************图片查看详情****************************/
$("body").on("click", ".add-accident img", function(e) {
    var imgUrl = $(e.target).attr("src");
    popWindow();
    $(".pop-window").append("<img src='" + imgUrl + "'/>");
});

/***********************获取可选天气**************************/
$(function() {
    $.get("/customer/getWeatherList?t="+new Date().getTime(), {}, function(data) {
        if (data.result==true) {
            var weatherList = data.msg;
            for (var i = 0; i < weatherList.length; i++) {
                var liNode = $("<li data-name=\"" + weatherList[i].name + "\"></li>");
                var maskNode = $("<div class='maskImg'></div>");
                var pNode = $("<p>" + weatherList[i].name + "</p>");
                var imgNode = $("<img src=\"" + weatherList[i].iconUrl + "\" />");
                var divNode = $("<div class='img'></div>");
                divNode.append(imgNode);
                liNode.append(divNode).append(pNode).append(maskNode);
                maskNode.on("click", function(e) {
                    var c = $(e.target).parent().data("name");
                    $("#weather").val(c).next().addClass("hide");
                });
                $(".weatherImg").append(liNode).addClass("hide");
            }
        }
    });
});
$("#weather").click(function() {
    $(".weatherImg").removeClass("hide");
    canNext();
});

/***********************获取分局的管辖范围**************************/
var substations = [];
$(function() {
    $.get("/customer/listSubstation?t="+new Date().getTime(), {}, function(data) {
        if (data.result==true) {
            substations = data.msg;
        }
    });
});
$("#weather").click(function() {
    $(".weatherImg").removeClass("hide");
});

/***************************获取地址*******************************/
var addressLng=null;
var addressLat=null;
function openMap(){
    popWindow();
    var html = '<iframe id="contentIframe" src="addressMap.html?t='+(new Date().getTime())+'" width="100%"></iframe>';
    $(".pop-window").append(html);
    var iframe = document.getElementById("contentIframe");
    $(iframe).height($(".pop-window").height());
}
$("#addressIcon").click(openMap);
$("#trafficAccidentAddress").click(openMap);

/*********************************下一步上传事故时间、地点、天气***********************************/
function AccidentMedia(drivingLicenceId, mediaPart, url) {
    this.drivingLicenceId = drivingLicenceId;
    this.mediaPart = mediaPart;
    this.url = url;
}

$(".footer").click(function() {
    var yearMoth = $("#h3Ele").val().replace(/-/g, "/");
    var hour = $("#hour").val();
    hour = (hour < 10 ? "0" : "") + hour;
    var seconds = $("#seconds").val();
    var data = yearMoth + " " + hour + ":" + seconds;
    var weather = $("#weather").val();
    var address = $("#trafficAccidentAddress").val();
    if (yearMoth == "" || hour == "" || seconds == "") {
        $("#h3Ele").focus();
        return false;
    }
    if (address == "") {
        $("#weather").focus();
        return false;
    }
    if (weather == "") {
        $("#address").focus();
        return false;
    }
    var accidentArr = [];
    var partArr = [];
    for (var i = 0; i < $("li.add-accident").length; i++) {
        var drivingLicenceUrl = $("li.add-accident").eq(i).find("img").attr("src");
        var drivingLicenceId = $("li.add-accident").eq(i).find(".drivingLicence").val();
        var mediaPart = $("li.add-accident").eq(i).attr("name");
        accidentArr.push(new AccidentMedia(drivingLicenceId, mediaPart, drivingLicenceUrl));
        partArr.push(mediaPart);
    }
    var part1 = -1, part2 = -1, part3 = -1;
    for (var i = 0; i < partArr.length; i++) {
        if (partArr[i] == 0) {
            part1 = 0;
        } else if (partArr[i] == 1) {
            part2 = 1;
        } else if (partArr[i] == 2) {
            part3 = 2;
        }
    }
    /*if (part1 == 0 && part2 == 1 && part3 == 2) {
        $.post("/customer/uploadAccidentMedia?time="+new Date().getTime(),
     {date: data, location: address, weather: weather, accidentMedias: JSON.stringify(accidentArr),longitude:addressLng,latitude:addressLat},
     function(data) {
     if (data.result==true) {
     location.href = "incident_information.html";
     } else {
     if ((data.msg!==null)&&(typeof data.msg!=="undefined")) {
     layer.alert(data.msg);
     }else{
     layer.alert("系统繁忙!");
     }
     }
     });
    } else {
        if (part1 == -1) {
            $("#mediaPart1").focus();
        } else if (part2 == -1) {
            $("#mediaPart2").focus();
        } else if (part3 == -1) {
            $("#mediaPart3").focus();
        }
    }*/
    
    if ($(".footer>a").hasClass('disable-href')){
        return;
    }else{
      $.post("/customer/uploadAccidentMedia?time="+new Date().getTime(),
          {date: data, location: address, weather: weather, accidentMedias: JSON.stringify(accidentArr),longitude:addressLng,latitude:addressLat},
          function(data) {
            if (data.result==true) {
              location.href = "incident_information.html";
            } else {
              if ((data.msg!==null)&&(typeof data.msg!=="undefined")) {
                layer.alert(data.msg);
              }else{
                layer.alert("系统繁忙!");
              }
            }
          });
    }

});
/***************************************判断页面上的元素是否填满下一步可点击************************************/
function canNext(){
    var accidentArr = [];
    var partArr = [];
    for (var i = 0; i < $("li.add-accident").length; i++) {
        var drivingLicenceUrl = $("li.add-accident").eq(i).find("img").attr("src");
        var drivingLicenceId = $("li.add-accident").eq(i).find(".drivingLicence").val();
        var mediaPart = $("li.add-accident").eq(i).attr("name");
        accidentArr.push(new AccidentMedia(drivingLicenceId, mediaPart, drivingLicenceUrl));
        partArr.push(mediaPart);
    }
    console.log('~~~~~~~~~~~~~~~~');
    //当事人数量
  if ($('select.drivingLicence').length==0){
        return;
  }
  var partyLength=$('select.drivingLicence')[0].length;
    var picCheck=true;
        //每个部位图片数量必须大于等于1
    for (var i=0;i<$('.ac-upload-photos.must').length;i++){
        if ($($('.ac-upload-photos.must')[i]).find('.add-accident').length<1){
          picCheck=false;
        }
      //每个当事人至少上传了一张图片
/*      var carMarks=$($('.ac-upload-photos.must')[i]).find('.drivingLicence');
      var accid=new Array();
      for(x = 0;x<carMarks.length;x++){
        accid.push(carMarks[x].value);
      }
      if (uniqueArray(accid).length<partyLength){
        picCheck=false;
      }*/

    }

    



    /*var part1 = -1, part2 = -1, part3 = -1;
    for (var i = 0; i < partArr.length; i++) {
        if (partArr[i] == 0) {
            part1 = 0;
        } else if (partArr[i] == 1) {
            part2 = 1;
        } else if (partArr[i] == 2) {
            part3 = 2;
        }
    }*/
      if(($("#trafficAccidentAddress").val()!="")&&($("#weather").val()!="")&&picCheck){
        $(".footer>a").removeClass("disable-href");
    }else{
        $(".footer>a").addClass("disable-href");
    }
}
//数组去重
function uniqueArray(arr){
  var hash=[];
  for (var i = 0; i < arr.length; i++) {
    for (var j = i+1; j < arr.length; j++) {
      if(arr[i]===arr[j]){
        ++i;
      }
    }
    hash.push(arr[i]);
  }
  return hash;
}