(function() {
    var hintText = {
        phone : {
            hint : "请输入11位电话号码",
            right : "√电话号码输入正确",
            wrong : "×电话号码输入有误，请重新输入"
        },
        password : {
            hint : "请输入6位以上密码",
            right : "√密码格式正确",
            wrong : "×请输入符合格式的密码"
        },
        repassword : {
            hint : "请再次输入密码",
            right : "√再次输入密码正确",
            wrong : "×两次输入不一致或密码格式不正确，请重新输入或密码格式不正确"
        },
        checkCode : {
            hint : "请输入4位短信验证码",
            right : "√",
            wrong : "x验证码格式错误"
        },
        loginPhone : {
            hint : "请输入登录手机号码",
            right : "",
            wrong : ""
        },
        loginPassword : {
            hint : "请输入登录密码",
            right : "",
            wrong : ""
        }
    };
    var regEvent = function( node , event , func ) {
        if (node.addEventListener)
            node.addEventListener( event , func );
        else if (node.attachEvent)
            node.attachEvent( "on" + event , func );
        else
            node["on" + event] = func;
    };
    function regValue( id , i ) {
        var flag = false, input = document.getElementById( id ), value = input.value;
        switch (id) {
        case "login_user_name":
            flag = /^[1][3,4,5,7,8][0-9]{9}$/.test( value );
            id = "loginPhone";
            break;

        case "password":
            flag = /^\S{6,16}$/.test( value );
            id = "password";
            break;
        case "login_password":
            flag = /^\S{6,16}$/.test( value );
            id = "loginPassword";
            break;
        case "repassword":
            flag = document.getElementById( "password" ).value == value && value != "" && value != null
                    && (/^\S{6,16}$/.test( value ));
            break;

        case "phone":
            flag = /^[1][3,4,5,7,8][0-9]{9}$/.test( value );
            id = "phone";
            break;

        case "checkCode":
            flag = /^\d{4}$/.test( value );
            id = "checkCode";
            break;

        default:
            break;
        }
        if (flag) {
            index = 0;
            input.className = "right input";
            hint[i].className = "hint hint_right";
            hint[i].innerHTML = hintText[id].right;
        } else {
            input.className = "wrong input";
            hint[i].className = "hint hint_wrong";
            hint[i].innerHTML = hintText[id].wrong;
            index = 1;
        }
    }
    ;
    var inputs = document.getElementsByClassName( "input" ), id, hint = document.getElementsByClassName( "hint" ), index = 0;

    for (var j = 0; j < inputs.length; j++) {
        (function( i ) {
            regEvent( inputs[i] , "focus" , function() {
                hint[i].style.visibility = "visible";
                id = inputs[i].id;
            } );
            regEvent( inputs[i] , "blur" , function() {
                regValue( id , i );
            } );
        })( j )
    }
    regEvent( document.getElementById( "submit" ) , "click" , function( e ) {

        //判断所有都填写过，填写格式是否正确
        var typed = true;
        if ($( "#phone" ).val() == '') {
            $( "#phone" ).addClass( 'wrong input' );
            typed = false;
        }
        if ($( "#password" ).val() == '') {
            $( "#password" ).addClass( 'wrong input' );
            typed = false;
        }
        if ($( "#repassword" ).val() == '') {
            $( "#repassword" ).addClass( 'wrong input' );
            typed = false;
        }
        if ($( "#checkCode" ).val() == '') {
            $( "#checkCode" ).addClass( 'wrong input' );
            typed = false;
        }
        if ($( '#div-register input' ).hasClass( 'wrong input' ) || !typed) {
            console.log( '阻止了事件' );
            return false;
        }
        var rurl = "/c/customer/publicRegister";
        $.post( rurl , {
            phone : $( "#phone" ).val(),
            password : $( "#password" ).val(),
            checkCode : $( "#checkCode" ).val()
        } , function( data ) {
            if (data.success) {
                alert( data.msg );
                $( '.signin' ).click();
                $( "#login_user_name" ).val( data.content.phone );
            } else {
                alert( data.msg );
            }
        } , "json" );
        e.preventDefault();
    } );
    regEvent( document.getElementById( "button" ) , "click" , function( e ) {
        var typed = true;
        if ($( "#login_user_name" ).val() == '') {
            $( "#login_user_name" ).addClass( 'wrong input' );
            typed = false;
        }
        if ($( "#login_password" ).val() == '') {
            $( "#login_password" ).addClass( 'wrong input' );
            typed = false;
        }
        if ($( '#div-login input' ).hasClass( 'wrong input' ) || !typed) {
            e.preventDefault();
            alert( "您的输入有误，请检查并重新输入！" );
            return false;
        }
        var lurl = "/c/customer/publicLogin";
        $.post( lurl , {
            username : $( "#login_user_name" ).val(),
            password : $( "#login_password" ).val()
        } , function( data ) {
            if (data.success) {
                $.cookie( 'userId' , data.content.userId );
                $.cookie( 'identity' , 'customer' );
                window.location.href = "/manage/sys_index.html";
            } else {
                alert( data.msg );
            }
        } , "json" );
        e.preventDefault();
    } );
})();