//事故信息
$( function() {
    $( "#back" ).click( function() {
        history.back();
    } )
} )
$.ajax( {
    type : "GET",
    url : "/customer/accident?t=" + new Date().getTime(),
    dataType : "json",
    success : function( data ) {
        if (data.result==true) {
            $( '.ac-situation-time>span' ).html( format( data.msg.occurredTime , "yyyy年MM月dd日 HH时mm分" ) );
            $( '.ac-situation-address>span' ).html( data.msg.location );
            $( '.ac-situation-weather>span' ).html( data.msg.weather );
        } else {
            if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
                layer.alert( data.msg );
            }else{
                layer.alert("系统繁忙！");
            }
        }
    }
} );

//事故原因及分类
$.ajax( {
    type : "GET",
    url : "/customer/accidentReason/list?t=" + new Date().getTime(),
    dataType : "json",
    success : function( data ) {
        if (data.result==true) {
            data.msg.forEach( function( value ) {
                $( '.ac-situation-content-l' ).append(
                        '<a href="#dc" categoryId="' + value.id + '">' + value.name + '</a>' );
            } );
            //绑定点击事件
            $( '.ac-situation-content-l' ).on(
                    'click' ,
                    'a' ,
                    function() {
                        $( '.ac-situation-content-l>a' ).removeClass( "active" );
                        $( this ).addClass( "active" );
                        $( '#dc' ).empty();
                        var categoryId = $( this ).attr( 'categoryId' );
                        data.msg.forEach( function( value ) {
                            if (value.id == categoryId) {
                                value.pics.forEach( function( value ) {
                                    var li = '<li accident-reasonId="' + value.id + '">'
                                            + '      <div class="img"><img src="' + value.url + '" alt=""></div>'
                                            + '      <p>' + value.description + '</p>\n' + '</li>';
                                    $( '#dc' ).append( li );
                                } );
                            }
                        } );
                    } );
        } else {
            if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
                layer.alert( data.msg );
            }else{
                layer.alert("系统繁忙！");
            }
        }
    }
} );
//事故原因选中
$( '#dc' ).on( 'click' , 'li' , function() {
    var lis = $( '#dc>li' );
    for (var i = 0; i < lis.length; i++) {
        $( lis[i] ).removeClass( 'checked-img' );
    }
    $( this ).addClass( 'checked-img' );
    canNext();
} );

//下一步
$( "#next" )
        .click(
                function() {
                    //事故原因id
                    var reasonId = $( $( '#dc .checked-img' )[0] ).attr( 'accident-reasonid' );
                    //当事人数量
                    var partCount = $( '#ac-part' ).attr( 'partCount' );
                    var responsibilities = new Array();
                    function Responsibility( id , responsibility ) {
                        this.id = id;
                        this.responsibility = responsibility;
                    }
                    if (partCount == 2) {//2位当事人
                        var lis = $( '#ac-part li' );
                        for (var i = 0; i < lis.length; i++) {
                            var id = $( lis[i] ).attr( 'partId' );
                            var responsibility = $( '#ac-part li[partId="' + id + '"] img[chosed="true"]' ).attr(
                                    "resonSignId" );
                            responsibilities.push( new Responsibility( id , responsibility ) );
                        }

                    } else if (partCount > 2) {//多位当事人
                        //全部责任
                        var allResponsibilityPartId = $(
                                $( '#all-responsebility-partchose img[src="../../static/image/customer/icon/Refresh.png"]' )[0] )
                                .attr( 'accidentPartId' );
                        responsibilities.push( new Responsibility( allResponsibilityPartId , 0 ) );
                        //无责任
                        for (var i = 0; i < partIds.length; i++) {
                            if (partIds[i] == allResponsibilityPartId) {
                                continue;
                            }
                            responsibilities.push( new Responsibility( partIds[i] , 2 ) );
                        }

                    }
                    if (reasonId) {
                        $.ajax( {
                            type : "POST",
                            url : "/customer/accidentReason?t=" + new Date().getTime(),
                            data : {
                                accidentReasonId : reasonId,
                                responsibilities : JSON.stringify( responsibilities )
                            },
                            dataType : "json",
                            success : function( data ) {
                                if (data.result==true) {
                                    location.href = "agreement.html";
                                } else {
                                    if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
                                        layer.alert( data.msg );
                                    }else{
                                        layer.alert("系统繁忙！");
                                    }
                                }
                            }
                        } );
                        return false;
                    } else {
                        layer.alert( "请选择事故原因" );
                        return false;
                    }
                } );
//获取所有当事人
var partIds;
var changeChecked = 0;
$
        .ajax( {
            type : "GET",
            url : "/customer/accidentParty?t=" + new Date().getTime(),
            dataType : "json",
            success : function( data ) {
                if (data.result==true) {
                    if (data.msg.length == 2) {//两位当事人
                        // console.log('2位当事人');

                        //标记人数
                        $( '#ac-part' ).attr( 'partCount' , data.msg.length );
                        partIds = new Array();
                        data.msg
                                .forEach( function( value ) {
                                    //记录事故所有当事人id
                                    partIds.push( value.id );
                                    var li = "<li partId=\""
                                            + value.id
                                            + "\">\n"
                                            + "                                    <div class=\"ac-situation-name\">"
                                            + value.name
                                            + ": "
                                            + value.drivingLicence.number
                                            + "</div>\n"
                                            + "                                    <div class=\"ac-situation-responsibility all-responsibility\">\n"
                                            + "                                        <img id=\"all-"
                                            + value.id
                                            + "\" resonSignId=\"0\" src=\"../../static/image/customer/icon/Unrefresh.png\"alt=\"\">\n"
                                            + "                                        <label for=\"all-"
                                            + value.id
                                            + "\">全部责任</label>\n"
                                            + "                                    </div>\n"
                                            + "                                    <div class=\"ac-situation-responsibility half-responsibility\">\n"
                                            + "                                        <img id=\"part-1-"
                                            + value.id
                                            + "\" resonSignId=\"1\" src=\"../../static/image/customer/icon/Unrefresh.png\" alt=\"\">\n"
                                            + "                                        <label for=\"part-1-"
                                            + value.id
                                            + "\">同等责任</label>\n"
                                            + "                                    </div>\n"
                                            + "                                    <div class=\"ac-situation-responsibility none-responsibility\">\n"
                                            + "                                        <img id=\"part-2-"
                                            + value.id
                                            + "\" resonSignId=\"2\" src=\"../../static/image/customer/icon/Unrefresh.png\"  alt=\"\">\n"
                                            + "                                        <label for=\"part-2-" + value.id
                                            + "\">无责任</label>\n" + "                                    </div>\n"
                                            + "                                </li>";
                                    $( '#ac-part' ).append( li );
                                } );
                        //当事人责任选择
                        $( '#ac-part li' )
                                .on(
                                        'click' ,
                                        '.ac-situation-responsibility' ,
                                        function() {
                                            var others = $( this ).siblings( '.ac-situation-responsibility' );
                                            for (var i = 0; i < others.length; i++) {
                                                $( others[i] ).children( 'img' ).attr( 'src' ,
                                                        '../../static/image/customer/icon/Unrefresh.png' );
                                                $( others[i] ).children( 'img' ).attr( 'chosed' , 'false' );
                                            }
                                            $( this ).children( 'img' ).attr( 'src' ,
                                                    '../../static/image/customer/icon/Refresh.png' );
                                            $( this ).children( 'img' ).attr( 'chosed' , 'true' );

                                            if ($( this ).children( 'img' ).attr( 'id' ).search( 'all' ) != -1) {
                                                $( $( this ).parent().siblings( 'li' )[0] ).children(
                                                        '.none-responsibility' )[0].click();
                                            } else if ($( this ).children( 'img' ).attr( 'id' ).search( 'part-1' ) != -1) {
                                                $( $( this ).parent().siblings( 'li' )[0] ).children(
                                                        '.half-responsibility' )[0].click();
                                            } else if ($( this ).children( 'img' ).attr( 'id' ).search( 'part-2' ) != -1) {
                                                $( $( this ).parent().siblings( 'li' )[0] ).children(
                                                        '.all-responsibility' )[0].click();
                                            }
                                            changeChecked = 1;
                                            canNext();
                                        } );
                    } else {//多位当事人
                        console.log( '多位当事人' );
                        //标记人数
                        $( '#ac-part' ).attr( 'partCount' , data.msg.length );
                        var allli = "<li id=\"all-responsebility-partchose\">\n                                <div class=\"ac-situation-name\">全部责任：</div>\n                            </li>";
                        var noneli = "<li id=\"none-responsebility-partchose\">\n                                <div class=\"ac-situation-name\">无责任：</div>\n                            </li>";
                        $( '#ac-part' ).append( allli );
                        $( '#ac-part' ).append( noneli );
                        partIds = new Array();
                        data.msg.forEach( function( value ) {
                            //记录所有当事人id
                            partIds.push( value.id );

                            var div = "<div class=\"ac-situation-responsibility all-responsibility-part\">\n"
                                    + "                                    <img accidentPartId=\"" + value.id
                                    + "\" id=\"part-" + value.id
                                    + "\" src=\"../../static/image/customer/icon/Unrefresh.png\"/>\n"
                                    + "                                    <label for=\"part-" + value.id + "\">"
                                    + value.name + "(" + value.drivingLicence.number + ")</label>\n"
                                    + "                                </div>";
                            $( '#all-responsebility-partchose' ).append( div );
                        } );

                        $( '#all-responsebility-partchose' )
                                .on(
                                        'click' ,
                                        '.all-responsibility-part' ,
                                        function() {
                                            $( this ).children( 'img' ).attr( 'src' ,
                                                    '../../static/image/customer/icon/Refresh.png' );
                                            var others = $( this ).siblings( '.all-responsibility-part' );
                                            $( '#none-responsebility-partchose' ).empty();
                                            $( '#none-responsebility-partchose' ).append(
                                                    '<div class="ac-situation-name">无责任：</div>' );
                                            for (var i = 0; i < others.length; i++) {
                                                $( others[i] ).children( 'img' ).attr( 'src' ,
                                                        '../../static/image/customer/icon/Unrefresh.png' );
                                                var nonepart = '<div class="ac-situation-responsibility all-responsibility-part">\n'
                                                        + '        <img></img><label class="more-tips">'
                                                        + $( $( others[i] ).children( 'label' ) ).html()
                                                        + '</label>\n'
                                                        + '  </div>';
                                                $( '#none-responsebility-partchose' ).append( nonepart );
                                            }
                                            changeChecked = 1;
                                            canNext();
                                        } );
                    }
                } else {
                    if ((data.msg!=null)&&(typeof data.msg!="undefined")) {
                         layer.alert( data.msg );
                    }else{
                        layer.alert("系统繁忙！");
                    }
                }
            }
        } );
function canNext() {
    $( ".ac-situation-content-l" ).click( function() {
        $( "#next" ).addClass( "disable-href" ).prop("disabled",true);
    } );
    var reasonId = $( $( '#dc .checked-img' )[0] ).attr( 'accident-reasonid' );
    if (reasonId > 0 && (changeChecked == 1)) {
        $( "#next" ).removeClass( "disable-href" ).prop("disabled",false);
    } else {
        $( "#next" ).addClass( "disable-href" ).prop("disabled",true);
    }
}
//TODO-POST :  自主协商转远程协商