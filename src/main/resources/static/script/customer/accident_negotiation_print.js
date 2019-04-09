/***************************打印协议书界面**************************/
$("#print").click(function (e) {
    popWindow();
    var html="<div>\n<img id='printImg' src='../../image/customer/form1.png'>\n<footer class=\"footer footer-inner\">\n        <a href=\"../../../templates/index.html\"><i class=\"print\"></i>打印协议书</a>\n    </footer>\n</div>";
    $(".pop-window").append(html);
});