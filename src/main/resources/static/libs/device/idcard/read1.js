var socket = io.connect('http://localhost:5000', {
    'timeout': 100000,
    'reconnectionDelayMax': 1000,
    'reconnectionDelay': 500
});
socket.on('connect', function() {
    socket.emit('startRead');
});
setInterval(function() {
    socket.emit('isReading','', function(data) {
        if(data==false){
            socket.emit('startRead');
        }
    });
},1000);
var idCardNumber = 0;
var idCardName = "";
socket.on('card message', function(msg) {
    var base = new Base64();
    var result1 = base.decode(msg);
    var data = eval("(" + result1 + ")");
    idCardNumber = data.cardno;
    idCardName = data.name;
    $(".page-search-result").removeClass("hide").siblings().addClass("hide");
    $("#idCardNumber").html(idCardNumber);
    $("#idCardName").html(idCardName);
    $.get("/customer/listByIdNumber?t="+new Date().getTime(), {idNumber: idCardNumber}, function(data) {
        $("#tbody").empty();
        socket.emit("stopRead");
        // layer.close(index);
        if (data.result==true) {
            if (data.msg.length > 0) {
                for (var i = 0; i < data.msg.length; i++) {
                    var msg = data.msg[i];
                    var time = format(msg.occurredTime, "yyyy-MM-dd HH:mm:ss");
                    // msg.drivingNumberStr = msg.drivingNumberStr.replace(/,/g, "<br/>");
                    if(msg.drivingNumber.length>2){
                        msg.drivingNumber.length = 3;
                        msg.drivingNumber[2]="......";
                        msg.accidentParties.length=3;
                        msg.accidentParties[2]="......";
                    }
                    msg.drivingNumber = msg.drivingNumber.join(",");
                    msg.accidentParties = msg.accidentParties.join(",");
                    msg.drivingNumber = msg.drivingNumber.replace(/,/g, "<br/>");
                    msg.accidentParties = msg.accidentParties.replace(/,/g, "<br/>");
                    // msg.accidentPartyStr = msg.accidentPartyStr.replace(/,/g, "<br/>");
                    $("#tbody").html($("#tbody").html() + ('<tr>' +
                                         '<td class="search-ac-time">' + crossNull(time) + '</td>' +
                                         '<td class="search-ac-card-no">' + crossNull(msg.accidentParties) + '</td>' +
                                         ' <td class="search-ac-card-no">' + crossNull(msg.drivingNumber) + '</td>' +
                                         ' <td class="search-ac-address">' + crossNull(msg.location) + '</td>' +
                                         ' <td class="search-ac-negotiation">' + crossNull(msg.processMode) + '</td>' +
                                         ' <td     ="search-ac-org">' + crossNull(msg.dataSources) + '</td>' +
                                         '<td class="search-ac-preview">' +
                                             '<a class="yl ' +
                                                         ( msg.processStatus == "处理完毕" ? "" : " hide") + '" data-id="' + msg.idString +'" >预览</a>' +
                                             '<a data-id="' + msg.idString +  '" class="xs ' +
                                                         ( msg.processStatus == "处理完毕" ? " hide" : "" ) + '">自行协商</a>' +
                                             '<a class="again ' +( msg.refreshSeqNumber == true ? " " : "hide " )+'" data-id="'+msg.idString+'" >重新排号</a>' +

                                         '</td>' +
                                     '</tr>'));
                }
                $(".xs").click(function() {
                    var btn=$(this);
                    AccidentFrom(btn, function(id) {
                        $.get("/customer/accidentParty?time="+new Date().getTime(),{accidentId:id},function (result) {
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
                                    AccidentFrom(btn, function(id) {
                                        location.href = "accident_situation.html";
                                    });
                                }
                            }else{
                                layer.alert(result.msg);
                            }
                        });
                        return false;
                    });
                });
                $(".yl").click(Object, function() {
                    AccidentFrom($(this), function(id) {
                        location.href = "preview_agreement.html?accidentid="+id+"&idCardNumber="+idCardNumber;
                    });
                });
                $(".again").click(Object, function() {
                    AccidentFrom($(this), function(id) {
                        $.get("/customer/accident/setAccindetInSession/"+id,function(data){
                            if (data.result){
                                location.href = "/customer/refreshSeqNumber.html";
                            }else{
                                layer.alert(data.msg);
                            }
                        });

                    });
                });
                function AccidentFrom(obj, callback) {
                    $.get("/customer/accident/from/" + obj.attr('data-id')+"?t="+new Date().getTime(), function(result) {
                        if (result.result==true) {
                            callback(obj.attr('data-id'));
                        } else {
                            alert('系统繁忙');
                        }
                    });
                    return false;
                }

            }
            else {
                $("#tbody").html(
                  '<tr width="100%"><td style="width:100%;border:1px solid #CACAE1;text-align: center" class="noMsg" colspan="7">没有查询到任何数据</td></tr>');
            }
        } else {
            $("#tbody").html(
              '<tr width="100%"><td style="width:100%;border:1px solid #CACAE1;text-align: center" class="noMsg" colspan="7">' +
              data.msg + '</td></tr>');
        }
    });
});
// test();
// function test() {
//   idCardNumber = '511602199301056292';
//   idCardName = '蒋敏';
//   $(".page-search-result").removeClass("hide").siblings().addClass("hide");
//   $("#idCardNumber").html(idCardNumber);
//   $("#idCardName").html(idCardName);
//   $.get("/customer/listByIdNumber?t="+new Date().getTime(), {idNumber: idCardNumber}, function(data) {
//     $("#tbody").empty();
//     socket.emit("stopRead");
//     // layer.close(index);
//     if (data.result==true) {
//       if (data.msg.length > 0) {
//         for (var i = 0; i < data.msg.length; i++) {
//           var msg = data.msg[i];
//           var time = format(msg.occurredTime, "yyyy-MM-dd HH:mm:ss");
//           // msg.drivingNumberStr = msg.drivingNumberStr.replace(/,/g, "<br/>");
//           if(msg.drivingNumber.length>2){
//             msg.drivingNumber.length = 3;
//             msg.drivingNumber[2]="......";
//             msg.accidentParties.length=3;
//             msg.accidentParties[2]="......";
//           }
//           msg.drivingNumber = msg.drivingNumber.join(",");
//           msg.accidentParties = msg.accidentParties.join(",");
//           msg.drivingNumber = msg.drivingNumber.replace(/,/g, "<br/>");
//           msg.accidentParties = msg.accidentParties.replace(/,/g, "<br/>");
//           // msg.accidentPartyStr = msg.accidentPartyStr.replace(/,/g, "<br/>");
//           $("#tbody").html($("#tbody").html() + ('<tr>' +
//                            '<td class="search-ac-time">' + crossNull(time) + '</td>' +
//                            '<td class="search-ac-card-no">' + crossNull(msg.accidentParties) + '</td>' +
//                            ' <td class="search-ac-card-no">' + crossNull(msg.drivingNumber) + '</td>' +
//                            ' <td class="search-ac-address">' + crossNull(msg.location) + '</td>' +
//                            ' <td class="search-ac-negotiation">' + crossNull(msg.processMode) + '</td>' +
//                            ' <td     ="search-ac-org">' + crossNull(msg.dataSources) + '</td>' +
//                            '<td class="search-ac-preview">' +
//                            '<a class="yl ' +
//                            ( msg.processStatus == "处理完毕" ? "" : " hide") + '" data-id="' + msg.idString +'" >预览</a>' +
//                            '<a data-id="' + msg.idString +  '" class="xs ' +
//                            ( msg.processStatus == "处理完毕" ? " hide" : "" ) + '">自行协商</a>' +
//                            '<a class="again ' +( msg.refreshSeqNumber == true ? " " : "hide " )+'" data-id="'+msg.idString+'" >重新排号</a>' +
//
//                            '</td>' +
//                            '</tr>'));
//         }
//         $(".xs").click(function() {
//           var btn=$(this);
//           AccidentFrom(btn, function(id) {
//             $.get("/customer/accidentParty?time="+new Date().getTime(),{accidentId:id},function (result) {
//               if(result.result==true){
//                 var resultData=result.msg;
//                 // console.log(resultData);
//                 var canEnter;
//                 for(var i=0;i<resultData.length;i++){
//                   // console.log(name);
//                   if(resultData[i].drivingLicence.notcustom==false){
//                     canEnter=0;
//                     layer.alert("未查到车主姓名，请选择在线认定办理！");
//                     break;
//                   }else{
//                     canEnter=1;
//                   }
//                 }
//                 if(canEnter){
//                   AccidentFrom(btn, function(id) {
//                     location.href = "accident_situation.html";
//                   });
//                 }
//               }else{
//                 layer.alert(result.msg);
//               }
//             });
//             return false;
//           });
//         });
//         $(".yl").click(Object, function() {
//           AccidentFrom($(this), function(id) {
//             location.href = "preview_agreement.html?accidentid="+id+"&idCardNumber="+idCardNumber;
//           });
//         });
//         $(".again").click(Object, function() {
//           AccidentFrom($(this), function(id) {
//             $.get("/customer/accident/setAccindetInSession/"+id,function(data){
//               if (data.result){
//                 location.href = "/customer/refreshSeqNumber.html";
//               }else{
//                 layer.alert(data.msg);
//               }
//             });
//
//           });
//         });
//         function AccidentFrom(obj, callback) {
//           $.get("/customer/accident/from/" + obj.attr('data-id')+"?t="+new Date().getTime(), function(result) {
//             if (result.result==true) {
//               callback(obj.attr('data-id'));
//             } else {
//               alert('系统繁忙');
//             }
//           });
//           return false;
//         }
//
//       }
//       else {
//         $("#tbody").html(
//             '<tr width="100%"><td style="width:100%;border:1px solid #CACAE1;text-align: center" class="noMsg" colspan="7">没有查询到任何数据</td></tr>');
//       }
//     } else {
//       $("#tbody").html(
//           '<tr width="100%"><td style="width:100%;border:1px solid #CACAE1;text-align: center" class="noMsg" colspan="7">' +
//           data.msg + '</td></tr>');
//     }
//   });
// }


function crossNull(msg) {
    return msg == null ? "" : msg;
}

function format(time, format) {
    var t = new Date(time);
    var tf = function(i) {return (i < 10 ? '0' : '') + i;};
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
        switch (a) {
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
    });
}

function Base64() {
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    // public method for decoding
    this.decode = function(input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    };

    // private method for UTF-8 decoding
    _utf8_decode = function(utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while (i < utftext.length) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i + 1);
                c3 = utftext.charCodeAt(i + 2);
                string += String.fromCharCode(((c & 15) << 12)
                                              | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    };
}