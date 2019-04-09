//自定义日期格式
	var format = function(time, format){
	    var t = new Date(time);
	    var tf = function(i){return (i < 10 ? '0' : '') + i};
	    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
	    switch(a){
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
		    }) 
	}
	
	//功能介绍：检查是否为日期时间 
	function CheckDateTime(str){
		var reg = /^(\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
		var r = str.match(reg); 
		if(r==null)return false; 
		r[2]=r[2]-1; 
		var d= new Date(r[1], r[2],r[3], r[4],r[5], r[6]);
		if(d.getFullYear()!=r[1])
			return false; 
		if(d.getMonth()!=r[2])return false;
		if(d.getDate()!=r[3])return false; 
		if(d.getHours()!=r[4])return false;
		if(d.getMinutes()!=r[5])return false; 
		if(d.getSeconds()!=r[6])return false; 
		return true; 
	}
	//String 转date
	var date=function (str){
		str = str.replace(/-/g,"/");
		var date = new Date(str);
		return date;
	} 