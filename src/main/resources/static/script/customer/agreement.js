var accidentId=0;
/*****************************图片预加载******************************/
//加载事故信息二维码
$('#agreement-qrcode').attr('src', '/customer/ownAgreementQrcode?'+new Date().getTime());
//获取信息填入
$.ajax({
    type: "GET",
    url: "/customer/ownAgreement?t="+new Date().getTime(),
    dataType: "json",
    success: function(data) {
        //excel
        if (data.result==true) {

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

            //测试完删除
            /*accidentParts.push(data.msg.accidentParties[1]);
             accidentParts.push(data.msg.accidentParties[1]);
             accidentParts.push(data.msg.accidentParties[1]);*/

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
                               + "                                <td style='font-size: 10px;line-height:16px' colspan='2'>"+value.caseNumber+"</td>\n"
                               + "                            </tr>";
                    $('#table-tr-situationAndResponsibility').before(part);
                    var codename = "<td colspan=\"" + (index == 1 ? 6 : 5) + "\">" + code[index]
                                   + "</td>";

                    $('#accident-partslength').append(codename);
                    var brokenparts = "<td>车损<br/>部位</td>\n<td colspan=\""+ (index == 1 ? 5 : 4) + "\" style='text-align: left;text-indent: 20px'>□车头 □左前角 □右前角 □车尾 □左后角 □右后角 □车身左侧 □车身右侧</td>";
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
                    var sig = code[index] + " <button class=\"\n" + "tosign\" partid='"+value.id+"'partname=\"" + value.name
                              + "\"partcode=\"" + code[index] + "\">签字</button><p>"
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
                               + "<td style='font-size: 10px;line-height:16px'  colspan='2'>"+value.caseNumber+"</td>\n"
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
                        var sig = code[index] + " <button class=\"\n" + "tosign\" partid='"+value.id+"' partname=\""
                                  + value.name + "\"partcode=\"" + code[index] + "\">签字</button>";
                        $('#accident-signature').append(sig);
                    }
                });

                var notResponseCodes = "";
                accidentParts.forEach(function(value, index) {
                    if (value.responsibility == "无责任") {
                        notResponseCodes += "<span style='margin-right:20px;'>" + code[index] + "</span>";
                        var now = new Date();
                        var sig = code[index] + " <button class=\"\n" + "tosign\" partid='"+value.id+"' partname=\"" + value.name
                                  + "\"partcode=\"" + code[index] + "\">签字</button>";
                        $('#accident-signature').append(sig);
                    }
                });
                var now = new Date();
                $('#signatureTime').append(format(now.getTime(), "yyyy年MM月dd日"));
                var codename = "<td colspan=\"6\">" + notResponseCodes + "</td>";
                $('#accident-partslength').append(codename);
                var brokenparts = "<td>车损<br/>部位</td><td colspan=\"5\">□车头 □左前角 □右前角 □车尾 □左后角 □右后角 □车身左侧 □车身右侧</td>";
                $('#accident-brokenParts').append(brokenparts);
                var reason = "<td>事故<br/>情形</td><td colspan=\"5\"><p>□停车 □倒车 □逆行 □溜车 □开关车门</p>\n                <p>□违反交通信号灯 □变更车道与其他车辆刮擦 □未保持安全车距与前车追尾</p>\n                <p>□未按规定让行 □其他</p>\n            </td>";
                $('#accident-reason').append(reason);
                var responsibility = "<td>事故<br/>责任</td>" + "            <td colspan=\"5\">"
                                     + "<p>□全部责任&nbsp;&nbsp;☑无责任</p>" + "</td>";
                $('#accident-responsibility').append(responsibility);
            }

        } else {
            if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
                layer.alert( data.msg );
            }else{
                layer.alert("系统繁忙！");
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

//点击完成,更新事故状态为完成
$('#finishedAndGotoIndex').click(function() {
    //$('#buttongroup').hide();
    /*html2canvas(document.getElementById('divtopage')).then(function(canvas) {
     $(document.body.appendChild(canvas)).hide();
     var imgURL = canvas.toDataURL("image/png");
     var formData = new FormData();
     var file = dataURLtoBlob(imgURL);//将base64格式图片转换为文件形式
     var newImg = new Date().getTime() + '.png'; //给图片添加文件名
     formData.append('file', file, newImg); //formData对象添加文件
     $.ajax({
     type: 'POST',
     url: '/customer/upload/uploadFile',
     data: formData,
     async: false,
     processData: false,
     contentType: false,
     success: function(data) {
     if (data.result) {
     var fileUrl = data.msg;
     $.post('/customer/saveAgreement', {
     agreementUrl: fileUrl
     }, function(data) {
     if (data.result) {
     $.ajax({
     type: 'PUT',
     url: '/customer/finishAccident',
     success: function(data) {
     if (data.result) {
     window.location = '/customer/index';
     } else {
     alert(data.msg);

     }
     }
     });
     } else {
     alert(data.msg);
     }

     });
     } else {
     alert(data.msg);
     }
     }

     });
     //3秒后显示按钮
     setTimeout(function() {
     $('#buttongroup').show();
     }, 3000);

     });*/
    //window.print();
	//更新事故为已完成
    updateAccidentWithFinish(function(){
        window.location = '/customer/index';
        $('#buttongroup').show();
    });
    return false;
});

$("#accident-signature")
.on(
  'click',
  '.tosign',
  function() {
      // console.log($(this)[0]);
      popWindow();
      var html = "";
      html = "<div>\n" + "        <header class=\"sign-head\">当事人签字：" + $(this).attr('partname')
             + "</header>\n"
             + "        <div style=\"margin:0 auto;\"><div id=\"canvasDiv\" ></div></div>"
             + "<div class='agree'><input id='agree' type='checkbox'/> <label for='agree'>我同意当前协商内容</label></div>"
             + "        <button id=\"btn_submit\" partid='"+$(this).attr('partid')+"' partname=\"" + $(this).attr('partname')
             + "\" partcode=\"" + $(this).attr('partcode') + "\">完成</button>\n"
             + "        <button id=\"btn_clear\">清除</button>\n" + "    </div>";
      $(".pop-window").append(html);
      $("body").css("overflow","hidden");
      /**********************canvas签字板******************************/

      //完成按钮
      $("#btn_submit")
      .click(
        function() {
            $("body").css("overflow","auto");
            var canvas = $('#canvasDiv canvas')[0];
            var partcode = $(this).attr('partcode');
            var partname = $(this).attr('partname');
            var partid=$(this).attr('partid');
            var url=canvas.toDataURL();
            if ($('#accident-signature button[partcode="' + partcode + '"]:eq(0)').next().length != 0
                && $(
                $(
                  '#accident-signature button[partcode="' + partcode
                  + '"]:eq(0)').next()[0]).eq(0)[0].tagName == "IMG") {
                $(
                  $('#accident-signature button[partcode="' + partcode + '"]:eq(0)')
                  .next()[0]).eq(0).attr('src', url);
            } else {
                $('#accident-signature button[partcode="' + partcode + '"]:eq(0)').hide();
                $('#accident-signature button[partcode="' + partcode + '"]:eq(0)').after(
                  '<img class="tosign" partname="' + partname + '"partcode="'
                  + partcode + '"width="200px;" src="' +url
                  + '" />');
            }


            if ($('#accident-signature>img').length == $('#accident-signature>button').length) {
                $('#printPage').show();
                $("#gotoindex").hide();
                $("#finishedAndGotoIndex").show();
            }
            if ($("#agree").prop("checked")) {

                $("#popBox").remove();
                //上传签名图片
                var formData = new FormData();
                var file = dataURLtoBlob(url);//将base64格式图片转换为文件形式
                var newImg = new Date().getTime() + '.png'; //给图片添加文件名
                formData.append('file', file, newImg); //formData对象添加文件
                $.ajax({
                    type: 'POST',
                    url: '/customer/upload/uploadFile?t='+new Date().getTime(),
                    data: formData,
                    async: false,
                    processData: false,
                    contentType: false,
                    success: function(data) {
                        if (data.result==true) {
                            var fileUrl = data.msg;
                            $.ajax({
                                type:'POST',
                                url:"/customer/accidentPart/"+partid,
                                data:{signaturePic:fileUrl}
                            });
                        }else {
                            if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
                                layer.alert( data.msg );
                            }else{
                                layer.alert("系统繁忙！");
                            }
                        }
                    }
                });


            } else {
                layer.alert('未同意');
                $("#agree").focus();
            }
        });
      //清空画板
      $("#btn_clear").click(function() {
          var cxt=$('#canvasDiv canvas')[0].getContext("2d");
          cxt.beginPath();
          // cxt.fillStyle="#fff";
          cxt.clearRect(0,0,960,460);
          // cxt.fillRect(0,0,960,460);
          cxt.closePath();
      });

      (function(window, document, $) {
          'use strict';
          window.requestAnimFrame = (function(callback) {
              return window.requestAnimationFrame || window.webkitRequestAnimationFrame
                     || window.mozRequestAnimationFrame || window.oRequestAnimationFrame
                     || window.msRequestAnimaitonFrame || function(callback) {
                    window.setTimeout(callback, 1000 / 60);
                };
          })();

          var pluginName = 'jqSignature', defaults = {
              lineColor: '#df4b26',
              lineWidth: 1,
              border: '1px dashed #CCFF99',
              background: '#FFFFFF',
              height: 200,
              autoFit: false
          }, canvasFixture = '<canvas></canvas>';

          function Signature(element, options) {
              this.element = element;
              this.$element = $(this.element);
              this.canvas = false;
              this.$canvas = false;
              this.ctx = false;
              this.drawing = false;
              this.currentPos = {
                  x: 0,
                  y: 0
              };
              this.lastPos = this.currentPos;
              this._data = this.$element.data();
              this.settings = $.extend({}, defaults, options, this._data);
              this.init();
          }

          Signature.prototype = {
              init: function() {
                  this.$canvas = $(canvasFixture).appendTo(this.$element);
                  this.$canvas.attr({
                      width: this.settings.width,
                      height: this.settings.height
                  });
                  this.$canvas.css({
                      boxSizing: 'border-box',
                      width: this.settings.width + 'px',
                      height: this.settings.height + 'px',
                      border: this.settings.border,
                      background: this.settings.background,
                      cursor: 'crosshair'
                  });
                  if (this.settings.autoFit === true) {
                      this._resizeCanvas();
                  }
                  this.canvas = this.$canvas[0];
                  this._resetCanvas();
                  this.$canvas.on('mousedown touchstart', $.proxy(function(e) {
                      this.drawing = true;
                      this.lastPos = this.currentPos = this._getPosition(e);
                  }, this));
                  this.$canvas.on('mousemove touchmove', $.proxy(function(e) {
                      this.currentPos = this._getPosition(e);
                  }, this));
                  this.$canvas.on('mouseup touchend', $.proxy(function(e) {
                      this.drawing = false;
                      var changedEvent = $.Event('jq.signature.changed');
                      this.$element.trigger(changedEvent);
                  }, this));
                  $(document).on('touchstart touchmove touchend', $.proxy(function(e) {
                      if (e.target === this.canvas) {
                          e.preventDefault();
                      }
                  }, this));
                  var that = this;
                  (function drawLoop() {
                      window.requestAnimFrame(drawLoop);
                      that._renderCanvas();
                  })();
              },
              // Clear the canvas
              clearCanvas: function() {
                  this.canvas.width = this.canvas.width;
                  this._resetCanvas();
              },
              // Get the content of the canvas as a base64 data URL
              getDataURL: function() {
                  return this.canvas.toDataURL();
              },
              reLoadData: function() {
                  this.$canvas.remove();
                  this._data = this.$element.data();

                  //for (var i in this.settings) {
                  //    alert(i+":"+this.settings[i]);
                  //}

                  //this.settings = $.extend({}, defaults, this._data);
                  this.init();
              },
              // Get the position of the mouse/touch
              _getPosition: function(event) {
                  var xPos, yPos, rect;
                  rect = this.canvas.getBoundingClientRect();
                  event = event.originalEvent;
                  // Touch event
                  if (event.type.indexOf('touch') !== -1) { // event.constructor === TouchEvent
                      xPos = event.touches[0].clientX - rect.left;
                      yPos = event.touches[0].clientY - rect.top;
                  }
                  // Mouse event
                  else {
                      xPos = event.clientX - rect.left;
                      yPos = event.clientY - rect.top;
                  }
                  return {
                      x: xPos,
                      y: yPos
                  };
              },
              // Render the signature to the canvas
              _renderCanvas: function() {
                  if (this.drawing) {
                      this.ctx.moveTo(this.lastPos.x, this.lastPos.y);
                      this.ctx.lineTo(this.currentPos.x, this.currentPos.y);
                      this.ctx.stroke();
                      this.lastPos = this.currentPos;
                  }
              },
              // Reset the canvas context
              _resetCanvas: function() {
                  this.ctx = this.canvas.getContext("2d");
                  this.ctx.strokeStyle = this.settings.lineColor;
                  this.ctx.lineWidth = this.settings.lineWidth;
              },
              // Resize the canvas element
              _resizeCanvas: function() {
                  var width = this.$element.outerWidth();
                  this.$canvas.attr('width', width);
                  this.$canvas.css('width', width + 'px');
              }
          };

          $.fn[pluginName] = function(options) {
              var args = arguments;
              if (options === undefined || typeof options === 'object') {
                  return this.each(function() {
                      if (!$.data(this, 'plugin_' + pluginName)) {
                          $.data(this, 'plugin_' + pluginName, new Signature(this, options));
                      }
                  });
              } else if (typeof options === 'string' && options[0] !== '_' && options !== 'init') {
                  var returns;
                  this.each(function() {
                      var instance = $.data(this, 'plugin_' + pluginName);
                      if (instance instanceof Signature && typeof instance[options] === 'function') {
                          var myArr = Array.prototype.slice.call(args, 1);
                          returns = instance[options].apply(instance, myArr);
                      }
                      if (options === 'destroy') {
                          $.data(this, 'plugin_' + pluginName, null);
                      }
                      //if (options === 'reLoadData') {
                      //    //this.$canvas.remove();
                      //    $.data(this, 'plugin_' + pluginName, null);
                      //    this._data = this.$element.data();
                      //    this.settings = $.extend({}, defaults, options, this._data);
                      //    this.init();
                      //}
                  });
                  return returns !== undefined ? returns : this;
              }
          };

      })(window, document, jQuery);

      var WritingPad = function() {
          var current = null;
          $(function() {
              // initHtml();
              // initTable();
              initSignature();
              // if ($(".modal")) {
              //     $(".modal").modal("toggle");
              //
              // } else {
              //     alert("没用手写面板");
              // }
              $(document).on("click", "#myClose,.close", null, function() {
                  $('#mymodal').modal('hide');
                  $("#mymodal").remove();

              });
              $(document).on("click", "#mySave", null, function() {
                  var myImg = $('#myImg').empty();
                  var dataUrl = $('.js-signature').jqSignature('getDataURL');
                  var img = $('<img>').attr('src', dataUrl);
                  $(myImg).append($('<p style="text-align:center">').text("下面是您的签名"));
                  $(myImg).append(img);
              });
              $(document).on("click", "#myEmpty", null, function() {
                  $('.js-signature').jqSignature('clearCanvas');

              });
              $(document).on(
                "click",
                "#myBackColor",
                null,
                function() {
                    $('#colorpanel').css('left', '95px').css('top', '45px').css("display",
                      "block").fadeIn();
                    //$("canvas").css("background", "#EEEEEE");
                    $("#btnSave").data("sender", "#myBackColor");
                });
              $(document).on(
                "click",
                "#myColor",
                null,
                function() {
                    $('#colorpanel').css('left', '205px').css('top', '45px').css(
                      "display", "block").fadeIn();
                    $("#btnSave").data("sender", "#myColor");
                });
              $(document).on("mouseover", "#myTable", null, function() {
                  if ((event.srcElement.tagName == "TD") && (current != event.srcElement)) {
                      if (current != null) {
                          current.style.backgroundColor = current._background;
                      }
                      event.srcElement._background = event.srcElement.style.backgroundColor;
                      //$("input[name=DisColor]").css("background-color", event.srcElement.style.backgroundColor);
                      //var color = event.srcElement.style.backgroundColor;
                      //var strArr = color.substring(4, color.length - 1).split(',');
                      //var num = showRGB(strArr);
                      //$("input[name=HexColor]").val(num);
                      current = event.srcElement;
                  }
              });
              $(document).on("mouseout", "#myTable", null, function() {
                  if (current != null) {
                      current.style.backgroundColor = current._background;
                  }
              });
              $(document).on("click", "#myTable", null, function() {
                  if (event.srcElement.tagName == "TD") {
                      var color = event.srcElement._background;
                      if (color) {
                          $("input[name=DisColor]").css("background-color", color);
                          var strArr = color.substring(4, color.length - 1).split(',');
                          var num = showRGB(strArr);
                          $("input[name=HexColor]").val(num);
                      }
                  }
              });
              $(document).on("click", "#btnSave", null, function() {
                  $('#colorpanel').css("display", "none");
                  var typeData = $("#btnSave").data("sender");
                  var HexColor = $("input[name=HexColor]").val();
                  var data = $(".js-signature").data();
                  if (typeData == "#myColor") {
                      data["plugin_jqSignature"]["settings"]["lineColor"] = HexColor;
                      $('.js-signature').jqSignature('reLoadData');
                  }
                  if (typeData == "#myBackColor") {

                      data["plugin_jqSignature"]["settings"]["background"] = HexColor;
                      $('.js-signature').jqSignature('reLoadData');
                  }
              });
              $("#mymodal").on('hide.bs.modal', function() {
                  $("#colorpanel").remove();
                  $("#mymodal").remove();
                  $("#myTable").remove();
              });
          });

          function initHtml() {
              var html = '<div class="modal" id="mymodal">'
                         + '<div class="modal-dialog">'
                         + '<div class="modal-content">'
                         + '<div class="modal-header">'
                         +
                         '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>'
                         + '<h4 class="modal-title">手写面板</h4>'
                         + '</div>'
                         + '<div class="modal-body">'
                         + '<div class="js-signature" style="text-align:center" id="mySignature">'
                         + '</div>'
                         + '<div style="padding: 5px 12px;">'
                         + '<button type="button" class="btn btn-default" id="myEmpty">清空面板</button>'
                         + '<button type="button" class="btn btn-primary right" id="mySave">保存</button>'
                         +
                         '<button type="button" class="btn btn-default right" style="margin-right:10px;" id="myClose">关闭</button>'
                         + '<div id="colorpanel" style="position:absolute;z-index:99;display:none"></div>'
                         + '</div>' + '</div>' + '<div class="modal-footer">'
                         + '<div id="myImg" style="text-align:center">' + '<div>' + '</div>' + '</div>'
                         + '</div>' + '</div>';
              $('body').append(html);
          }

          function initSignature() {
              if (window.requestAnimFrame) {
                  // var signature = $("#mySignature");
                  var signature = $("#canvasDiv");
                  signature.jqSignature({
                      width: 960,
                      height: 460,
                      border: '1px solid #dcdcdc',
                      background: '#fff',
                      lineColor: '#444446',
                      lineWidth: 4,
                      autoFit: false
                  });
                  //{ width: 600, height: 200, border: '1px solid red', background: '#16A085', lineColor: '#ABCDEF', lineWidth: 2, autoFit: true }
              } else {
                  alert("请加载jq-signature.js");
                  return;
              }
          }

          function showRGB(arr) {
              hexcode = "#";
              for (x = 0; x < 3; x++) {
                  var n = arr[x];
                  if (n == "") {
                      n = "0";
                  }
                  if (parseInt(n) != n) {
                      return alert("RGB颜色值不是数字！");
                  }
                  if (n > 255) {
                      return alert("RGB颜色数字必须在0-255之间！");
                  }
                  var c = "0123456789ABCDEF", b = "", a = n % 16;
                  b = c.substr(a, 1);
                  a = (n - a) / 16;
                  hexcode += c.substr(a, 1) + b;
              }
              return hexcode;
          }

          function init() {
          }

          return {
              init: function() {
                  init();
              }
          };
      };
      var wp = new WritingPad();
  });

/**********************将网页保存为图片******************************/
function success_agreementprintCallback(data) {
    $("#loading").addClass("hide");
    $("#shade").addClass("hide");
    if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
        layer.alert( data.msg );
    }else{
        layer.alert("系统繁忙！");
    }
    setTimeout(function(){
    		window.location = '/customer/index';
    },60000);
}
$("#printPage").click(
  function() {
      var imgs = $('#accident-signature img');
      var buttons = $('#accident-signature button');
      if (imgs.length < buttons.length) {
          alert('请先完成签名');
          return false;
      }
      $("#loading").removeClass("hide");
      $("#shade").removeClass("hide");
      //协议书打印

      var script = document.createElement("script");
      script.type = "text/javascript";
      script.src = "http://127.0.0.1:9999/ae0c6017d105b4ce/excel?t="+(new Date().getTime())+"&accidentId=" + accidentId+"&callbackparam=success_agreementprintCallback";
      document.body.appendChild(script);

      $("#printPage").hide();
      updateAccidentWithFinish(function(){});
  });

function updateAccidentWithFinish(FinishResult){
    //更新事故为完成
    $.ajax({
	    type: 'PUT',
	    url: '/customer/finishAccident?t='+new Date().getTime(),
	    success: function(data) {
		    if (data.result==true) {
		    		FinishResult();
		    } else {
	            if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
	                layer.alert( data.msg );
	            }else{
	                layer.alert("系统繁忙！");
	            }
		    }
	    }
    });
}

//将base64格式图片转换为文件形式
function dataURLtoBlob(dataurl) {
    var arr = dataurl.split(',');
    var mime = arr[0].match(/:(.*?);/)[1];
    var bstr = atob(arr[1]);
    var n = bstr.length;
    var u8arr = new Uint8Array(n);
    
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], {
        type: mime
    });
}
setTimeout(function() {
    $("#loading").addClass("hide");
    $("#shade").addClass("hide");
},120000)