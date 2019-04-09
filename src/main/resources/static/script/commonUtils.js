	//初始化下拉框的公共方法,obj为下拉框的id数组
	function initSelects(obj){
		
		if(!obj||obj.length<1){
			return;
		}
		//清空下拉框
		for(var i=0; i<obj.length; i++){
			$("#"+obj[i]).empty();
			getSelect(obj[i]);
		}
		
	}
	function getSelect(selectid){
		$.ajax({
			type:"get",
			url:"/s/Selects/person/getSelects?t="+new Date().getTime(),
			data:{'flag':selectid},
			dataType:"json",
			success:function(data){
				var degree = '<option disabled selected>'+'请选择'+'</option>';
				for(i=0; i<data.content.length; i++){
					degree += '<option value='+"'"+data.content[i].value+"'"+'>'+data.content[i].name+'</option>';
				}
				$("#"+selectid).append(degree);
			} 
		
		});
	}