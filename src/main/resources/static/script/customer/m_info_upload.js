var uploadedImgNames=new Array();
$("input[type=file]").change(function(e){
	if(e.target.files[0].size>104857600){
		alert("上传文件不能超过100M");return;
	}
    var fileName=e.target.files[0].name;
        uploadedImgNames.push(fileName);
    var formFile = new FormData();
    formFile.append("file",e.target.files[0]);
    var index = layer.load(1, {
        shade: [0.1,'#fff'] //0.1透明度的白色背景
    });
    $.get("/customer/canUploadMediaM?t="+new Date().getTime(),{asdf:asdf,uuid:uuid},function (result) {
        if(result.result==true){
            $.ajax({
                url : "/customer/upload/uploadFile?t="+new Date().getTime(),
                type : 'POST',
                data : formFile,
                processData : false,
                contentType : false,
                dataType: "json",
                success: function(data){
                    if(data.result==true){
                        var mediaPart= $(e.target).attr("id").substring(9);
                        $.post("/customer/uploadAccidentMediaM?t="+new Date().getTime(),{mediaPart:mediaPart,fileUrl:data.msg,asdf:asdf},function (data) {
                            if(data.result==true){
                                layer.close(index);
                                alert("上传成功，请在一体机上查看");
                        }});
                        $(e.target)[0].value='';
                    }else{
                        layer.close(index);
                        alert(data.msg);
                    }
                }
            });
        }else{
            layer.close(index);
            if ((result.msg!=null)&&(typeof result.msg!="undefined")) {
                layer.alert( result.msg );
            }else{
                layer.alert("系统繁忙！");
            }
        }
    });
});
/*******************上传图标点击效果*******************/
$(".ac-up-file").mousedown(function() {
    $(".ac-up-file").css("opacity",0.8);
});
$(".ac-up-file").mouseup(function() {
    $(".ac-up-file").css("opacity",1);
});