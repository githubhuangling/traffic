
function InitCamOCX()
{
	var ret = CamSDKOCX.InitCameraLib();
	if(ret)
	{
		if(ret == 263)
		{
			alert("ocx初始化成功，但是没有授权");
		}
		else
		{
			alert("OCX初始化失败，错误号：" + ret);
			return;
		}
	}
	//开启0号摄像头
	StartVideo();
// 	setTimeout("StartVideo()",200);
}
//<!--反初始化-->
function UnInitCamOCX() 
{
	CamSDKOCX.UnInitCameraLib();
}
//<!--清空列表-->
function clean(list)
{
　　while (list.options.length>0)
　　{
　　 list.options.remove(0);
　　}
}
//<!--设置目录-->
function SetSaveFolder()
{
	saveFolder.href = path.value;
}
//<!--更新设备列表-->
function AddDevice() 
{
	clean(DeviceList);
    var mainDevCount = 0;
    var total = CamSDKOCX.GetDevCount();
	for(var i = 0 ; i < total ; i++ )
	{
	    var devtype = CamSDKOCX.GetDevType(i);
	    //if (devtype == 0) {//0 主头
	        var DevEle = CamSDKOCX.GetDevName(i);
			DeviceList.options.add(new Option(DevEle,i));
	        //DeviceList.options[mainDevCount].text = DevEle;
	        mainDevCount++;       
        //}	
	}
	if(mainDevCount > 0)
	{
		mainIndex = 0;
		DeviceList.options[0].selected = true;
	}
	
}
//<!--更新媒体类型列表-->
function AddMediaType(list)
{
	clean(list);
	total = CamSDKOCX.GetMediaTypeCount();
	for(var j = 0 ; j < total ; j++ )
	{   
		var mediatypeName = CamSDKOCX.GetMediaTypeName(j);
		list.options.add(new Option(mediatypeName));
		//list.options[j].text=mediatypeName ;
    }
    list.options[0].selected = true;
}
//<!--设置媒体类型-->
function SetMediaType(MediaType)
{
	//var obj=document.getElementById("MediaType") ;
	var index=MediaType.selectedIndex;
	CamSDKOCX.SetMediaType(index);
}
//<!--更新分辨率列表-->
function AddResolution2Comb(list)
{
	clean(list);
	var total = CamSDKOCX.GetResolutionCount();
	for(var i = 0 ; i < total ; i++ )
	{   
		var width = CamSDKOCX.GetResolutionWidth(i);
		var height = CamSDKOCX.GetResolutionHeight(i); 
		var resolution = width+" x "+height;
		list.options.add(new Option(resolution));
		//list.options[i].text=resolution;
    }
    list.options[0].selected = true;
}

//<!--设置分辨率-->
function SetResolution(Resolution)
{   
	//var obj=document.getElementById("Resolution") ;
	var index=Resolution.selectedIndex;
	var width = CamSDKOCX.GetResolutionWidth(index);
	var height = CamSDKOCX.GetResolutionHeight(index); 
	CamSDKOCX.SetResolution(width,height);
}

//<!--预览缩放-->

function ZoomIN()
{
	CamSDKOCX.ZoomIn();
}
function Zoomout()
{
	CamSDKOCX.Zoomout();
}
function OriginalPreview()
{
	CamSDKOCX.OriginalPreview();
}
function OptimalPreview()
{
	CamSDKOCX.OptimalPreview();
}

//<!--切换摄像头-->
function ChangeDevice()
{	
	CamSDKOCX.CloseDev();
	var obj = document.getElementById("DeviceList") ;
	var index = obj.selectedIndex;
	if(index >= 0)
	{
		CamSDKOCX.openDev(index,0,0,0);	
		AddMediaType(MediaType);
		AddResolution2Comb(Resolution);
	}
}
//<!-- 打开UVC驱动设置窗口 -->
function ShowDevSettingWindow()
{
	CamSDKOCX.ShowDevSettingWindow();
}
function ShowImageSettingWindow()
{
	CamSDKOCX.ShowImageSettingWindow();
}
//<!--蜂鸣-->
function EnableBuzzer()
{
	//鸣响两声，每次响300毫秒，间隔500毫秒
	CamSDKOCX.EnableBuzzer(2,300,500);
}
function AutoFocus()
{
	//一键对焦
	CamSDKOCX.AutoFocus(0);
}
//<!--图片色彩-->
function SetColorStyle(obj)
{
	var type = obj.selectedIndex;
	CamSDKOCX.SetColorStyle(type);
}
//<!--图片效果-->
function SetAdjust(obj)
{
	var type = obj.selectedIndex;
	CamSDKOCX.SetAdjust(type);
}
//<!--图片扩展名-->
//function SetAdjust(obj);
//{
//	var type = obj.selectedIndex;
//	CamSDKOCX.SetAdjust(type);
//}
//<!--图片质量-->
function SetJPGQuanlity(obj)
{
	CamSDKOCX.SetJPGQuanlity(obj.value);
}
//<!--裁切类型-->
function SetAutoCrop(obj)
{
	//var obj=document.getElementById("autocrop") ;
	var index = obj.selectedIndex;
	CamSDKOCX.SetAutoCrop(index);
}
//<!--旋转效果-->
function SetRotateState(obj)
{
	var index=obj.selectedIndex;
	CamSDKOCX.SetImageRotateMode(index);
}
//<!--手动裁切-->
function SetCusCrop(obj)
{
	if(obj.checked)
	{
		CamSDKOCX.SetCusCrop(1);
	}
	else
	{
		CamSDKOCX.SetCusCrop(0);
	}
}
//<!--设置手动裁切区域-->
function SetImageCusCropRect()
{
	//参数是坐标百分比
	CamSDKOCX.SetImageCusCropRect(leftValue.value,topValue.value,rightValue.value,rightValue.value);
}
//<!--设置DPI-->
function SetImageDPI()
{
	CamSDKOCX.SetImageDPI(xdpi.value, ydpi.value);
}
//<!--去噪-->
function SetDenoise(obj)
{
	if(obj.checked)
	{
		CamSDKOCX.SetDenoise(1);
	}
	else
	{
		CamSDKOCX.SetDenoise(0);
	}
	
}
//<!--防篡改-->
function SetImageFileSign(obj)
{
	if(obj.checked)
	{	
		CamSDKOCX.SetImageFileSign(1);
	}	
	else
	{
		CamSDKOCX.SetImageFileSign(0);
	}
}
function CheckImageFileSign()
{
	if(CamSDKOCX.CheckImageFileSign(document.getElementById("CheckFile").value) == 1)
	{
		alert("加入防篡改文件");
	}
	else
	{
		alert("文件未标记，或者已经修改");
	}
}
 
/*
myDate.getYear(); //获取当前年份(2位)
myDate.getFullYear(); //获取完整的年份(4位,1970-????)
myDate.getMonth(); //获取当前月份(0-11,0代表1月)
myDate.getDate(); //获取当前日(1-31)
myDate.getDay(); //获取当前星期X(0-6,0代表星期天)
myDate.getTime(); //获取当前时间(从1970.1.1开始的毫秒数)
myDate.getHours(); //获取当前小时数(0-23)
myDate.getMinutes(); //获取当前分钟数(0-59)
myDate.getSeconds(); //获取当前秒数(0-59)
myDate.getMilliseconds(); //获取当前毫秒数(0-999)
myDate.toLocaleDateString(); //获取当前日期
var mytime=myDate.toLocaleTimeString(); //获取当前时间
myDate.toLocaleString(); //获取日期与时间
*/
//<!--拍照-->
function Capture()
{
	var fileext = document.getElementById("filetype").value;
	var strFolder = document.getElementById("path").value;
	var myDate = new Date();
	var myName = "Image_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
	//myDate.getTime()
	var newFile = strFolder + "\\" + myName + fileext ;
	var files = CamSDKOCX.CaptureImage(newFile);
	//alert(newFile);
	return newFile;
}
//<!--拍照（显示）-->
function CaptureShow(obj)
{
	var files = Capture();
	var strs= new Array(); //定义一数组 
	strs=files.split(";"); //字符分割 
    var strShow=new Array();
	for (i=0;i<strs.length ;i++ ) 
	{ 
       var cropType= document.getElementById("autocrop").value;
       if(cropType=="2")
       {
         strShow=strs[i].split(".");
		CamSDKOCX.ShowImage(strShow[0]+"_"+i+"."+strShow[1]);
       }else
       {
         CamSDKOCX.ShowImage(strs[i]);
       }
       
	} 

}
//<!--拍照（base64）-->

function showImageBase64(strBase64)
{
   document.getElementById("imgPreview").src = "data:image/jpeg;base64,"+strBase64;
}
function CaptureBase64(obj)
{
	var files = Capture();
	var strs= new Array(); //定义一数组 
	strs=files.split(";"); //字符分割 
	for (i=0;i<strs.length ;i++ ) 
	{ 
		var strBase64 = CamSDKOCX.EncodeBase64(strs[i]);
		//alert(strBase64);
		//显示到窗口
		showImageBase64(strBase64);	
	} 
}

function CaptureBarcode()
{
	var files = Capture();
	var strs= new Array(); //定义一数组 
	strs=files.split(";"); //字符分割 
	for (i=0;i<strs.length ;i++ ) 
	{ 
		alert(CamSDKOCX.RecognizeBarcode(strs[i],0,0,0,0));
	} 
}

//<!--连拍-->
function AutoCapture(mode)
{
	var ret = CamSDKOCX.StartAutoCapture(path.value,mode,5);
	if(ret)
	{
		alert("连拍启动失败，错误码："+ret);
	}
}
//<!--麦克风-->
function AddAudioDev(obj)
{
	var nCount = CamSDKOCX.GetAudioDevCount();
	for(var i = 0 ; i < nCount ; i++ )
	{
	    var DevEle = CamSDKOCX.GetAudioDevName(i);
		obj.options.add(new Option(DevEle));
	}
	if(nCount > 0)
	{
		obj.options[0].selected = true;
	}
}
//<!--录像格式-->
//function ();
//{
//}

var dom = 0;
//dom = window.setInterval(GetMicrophoneVolumeLevel(),5);
//<!--音量-->
function GetMicrophoneVolumeLevel()
{
    //alert("1");
	volume.value = CamSDKOCX.GetMicrophoneVolumeLevel();
}
//<!--开始录像-->
function StartRecord(obj)
{
	var fileext = document.getElementById("RecordType").value;
	var strFolder = document.getElementById("path").value;
	var myDate = new Date();
	var myName = "video_"+myDate.getFullYear()+(myDate.getMonth()+1)+myDate.getDate()+"_"+myDate.getHours()+myDate.getMinutes()+myDate.getSeconds()+myDate.getMilliseconds();
	
	var newFile = strFolder + "\\" + myName + fileext ;
	//新SDK,videoFormat暂无意义
	var ret = CamSDKOCX.StartRecord(newFile,AudioList.selectedIndex,0);
	if(ret)
	{
		alert("开始录像失败，错误号:" + ret);
		return;
	}
	dom = window.setInterval(GetMicrophoneVolumeLevel, 1000); 
}
//<!--停止录像-->
function StopRecord()
{
	CamSDKOCX.StopRecord();
	window.clearInterval(dom); 
	volume.value = 0;
}

//<!-- 选择目录 -->
function browseFolder(path) {
    try {
        var Message = "\u8bf7\u9009\u62e9\u6587\u4ef6\u5939"; //选择框提示信息
        var Shell = new ActiveXObject("Shell.Application");
        //var Folder = Shell.BrowseForFolder(0, Message, 64, 17); //起始目录为：我的电脑
        var Folder = Shell.BrowseForFolder(0, Message, 0); //起始目录为：桌面
        if (Folder != null) {
            Folder = Folder.items(); // 返回 FolderItems 对象
            Folder = Folder.item(); // 返回 Folderitem 对象
            Folder = Folder.Path; // 返回路径
            if (Folder.charAt(Folder.length - 1) != "\\") {
                Folder = Folder + "\\";
            }
            document.getElementById(path).value = Folder;
            return Folder;
        }
    }
    catch (e) {
        alert(e.message);
    }
}

//<!-- 打开摄像头 -->
function StartVideo() {
	AddDevice();
	//打开列表所选摄像头
	ChangeDevice();
	AddAudioDev(AudioList);
	SetImageCusCropRect();
	ZoomIN();ZoomIN();ZoomIN();ZoomIN();ZoomIN();
	setTimeout(function() {
      $("#driverUrl").attr("onclick", "takePhoto(1)");
      $("#driverUrlp").attr("onclick", "takePhoto(1)").removeClass("disable-click");
      $("#drivingUrl").attr("onclick", "takePhoto(2)");
      $("#drivingUrlp").attr("onclick", "takePhoto(2)").removeClass("disable-click");
  },5000);
}
var countImage = 0;