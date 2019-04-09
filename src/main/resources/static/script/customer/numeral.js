$(function() {
    $.post("/customer/seqNumber", function(result) {
        if (result.result==true) {
        		var num = autoFill(result.msg, 3);
            $("#number").html(num);
        		//小票打印
  		    var script = document.createElement("script");
			script.type = "text/javascript";
			script.src = "http://127.0.0.1:9999/ae0c6017d105b4ce/ticket?t="+(new Date().getTime())+"&seqNumber="+num;
			document.body.appendChild(script);

            // $("#number").html(result.msg.totalTime);
            var count=10;
            var timer=setInterval(function() {
                if(count>0){
                    $("#countDown").html(count+"秒钟后自动返回主页")
                    count--;
                }else{
                    count = 0;
                    clearInterval(timer);
                    location.href = "/customer/index";
                }
            },1000)
        } else {
            // alert("系统繁忙，稍后重试！");
            layer.alert(result.msg);
        }
    });

    function autoFill(number, num) {
        number += "";
        while (number.length < num) {
            number = "0" + number;
        }
        return number;
    }
});