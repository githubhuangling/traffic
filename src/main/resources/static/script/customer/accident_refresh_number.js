// //todo
// var accidentId=getQueryString("accidentId");
// function getQueryString(name) {
//     var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
//     var reg_rewrite = new RegExp("(^|/)" + name + "/([^/]*)(/|$)", "i");
//     var r = window.location.search.substr(1).match(reg);
//     var q = window.location.pathname.substr(1).match(reg_rewrite);
//     if(r != null){
//         return unescape(r[2]);
//     }else if(q != null){
//         return unescape(q[2]);
//     }else{
//         return null;
//     }
// }


$(function() {
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

    //询问框  todo 重新排号上传照片目前暂时屏蔽， 直接排号
    /*    var lay=layer;
        lay.confirm('是否上传新的事故照片？', {
            btn: ['是','否'] //按钮
        }, function(index){
            $('body .header').show();
            $('body .main').show();
            lay.close(index);
        }, function(){
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
        });*/
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




/*********************************下一步***********************************/
function AccidentMedia(drivingLicenceId, mediaPart, url) {
    this.drivingLicenceId = drivingLicenceId;
    this.mediaPart = mediaPart;
    this.url = url;
}

$(".footer").click(function() {

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
    if (part1 == 0 && part2 == 1 && part3 == 2) {
        $.post("/customer/reuploadAccidentMedia?time="+new Date().getTime(),
          {accidentMedias: JSON.stringify(accidentArr)},
          function(data) {
              if (data.result==true) {
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
    if(($("#trafficAccidentAddress").val()!="")&&($("#weather").val()!="")&&(part1 == 0 )&& (part2 == 1) && (part3 == 2)){
        $(".footer>a").removeClass("disable-href");
    }else{
        $(".footer>a").addClass("disable-href");
    }
}