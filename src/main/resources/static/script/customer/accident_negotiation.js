$(".signature").on("click",function (e) {
    popWindow();
    var html="";
    html="<div>\n        <header class=\"sign-head\">当事人签字：张三</header>\n        <div style=\"margin:0 auto;\"><div id=\"canvasDiv\" ></div></div>\n        <button id=\"btn_submit\">完成</button>\n        <button id=\"btn_clear\">清除</button>\n    </div>";
    $(".pop-window").append(html);
    /**********************canvas签字板******************************/
    function onDocumentTouchStart( event ) {
        if( event.touches.length == 1 ) {
            event.preventDefault();
            var now = new Date().getTime();
            if ( now - timeOfLastTouch  < 250 ) {
                reset();
                return;
            }
            timeOfLastTouch = now;
            mouseX = event.touches[ 0 ].pageX;
            mouseY = event.touches[ 0 ].pageY;
            isMouseDown = true;
        }
    }
    function onDocumentTouchMove( event ) {
        if ( event.touches.length == 1 ) {
            event.preventDefault();
            mouseX = event.touches[ 0 ].pageX;
            mouseY = event.touches[ 0 ].pageY;
        }
    }
    function onDocumentTouchEnd( event ) {
        if ( event.touches.length == 0 ) {
            event.preventDefault();
            isMouseDown = false;
        }
    }
    var canvasDiv = document.getElementById('canvasDiv');
    var canvas = document.createElement('canvas');
    var canvasWidth = 960;
    var canvasHeight=460;
    document.addEventListener( 'touchmove', onDocumentTouchMove, false );
    var point = {};
    point.notFirst = false;
    canvas.setAttribute('width', canvasWidth);
    canvas.setAttribute('height', canvasHeight);
    canvas.setAttribute('id', 'canvas');
    canvasDiv.appendChild(canvas);
    if(typeof G_vmlCanvasManager != 'undefined') {
        canvas = G_vmlCanvasManager.initElement(canvas);
    }
    var context = canvas.getContext("2d");
    canvas.addEventListener("touchstart", function(e){
        var mouseX = e.pageX - this.offsetLeft;
        var mouseY = e.pageY - this.offsetTop;
        paint = true;
        addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
        redraw();
    });
    canvas.addEventListener("touchend", function(e){
        paint = false;
    });
    canvas.addEventListener("touchmove", function(e){
        if(paint){
            addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, true);
            redraw();
        }
    });
    canvas.addEventListener("mousedown", function(e){
        // var mouseX = e.pageX - this.offsetLeft;
        var mouseX = e.pageX - $("#canvas").offset().left;
        // var mouseY = e.pageY - this.offsetTop + 100;
        var mouseY = e.pageY - $("#canvas").offset().top;
        paint = true;
        addClick(mouseX, mouseY);
        redraw();
    });
    canvas.addEventListener("mousemove", function(e){
        // if(paint){
        //     addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, true);
        //     redraw();
        // }
        if(paint){
            addClick(e.pageX - $("#canvas").offset().left,e.pageY -  $("#canvas").offset().top, true);
            redraw();
        }
    });
    canvas.addEventListener("mouseup", function(e){
        paint = false;
    });
    canvas.addEventListener("mouseleave", function(e){
        paint = false;
    });
    var clickX = new Array();
    var clickY = new Array();
    var clickDrag = new Array();
    var paint;
    function addClick(x, y, dragging)
    {
        clickX.push(x);
        clickY.push(y);
        clickDrag.push(dragging);
    }
    function redraw(){
        //canvas.width = canvas.width; // Clears the canvas
        context.strokeStyle = "#df4b26";
        context.lineJoin = "round";
        context.lineWidth = 2;
        while (clickX.length > 0 ) {
            point.bx = point.x;
            point.by = point.y;
            point.x = clickX.pop();
            point.y = clickY.pop();
            point.drag = clickDrag.pop();
            context.beginPath();
            if (point.drag && point.notFirst) {
                context.moveTo(point.bx, point.by);
            } else {
                point.notFirst = true;
                context.moveTo(point.x - 1, point.y);
            }
            context.lineTo(point.x, point.y);
            context.closePath();
            context.stroke();
        }
    }
    var clear = document.getElementById("btn_clear");
    var submit = document.getElementById("btn_submit");
    clear.addEventListener("click", function(){
        canvas.width = canvas.width;
    });
    submit.addEventListener("click", function(){
        $("#qmimg").attr("src",canvas.toDataURL("image/png"));
    });
    $("#btn_submit").click(function () {
        //还没有设置提交图片的功能
        location.href="../../../public/customer/accident_negotiation_print.html";
        $("#popBox").remove();
    });
});