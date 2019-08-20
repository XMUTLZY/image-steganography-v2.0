$(document).ready(function () {
    //监听手机号是否已经被注册
    $("#LAY-user-login-cellphone").blur(function () {
        flag = false;
        var data = {};
        data.mobile = $("#LAY-user-login-cellphone").val();
        $.ajax({
            url:'/user/getUser',
            data: JSON.stringify(data),
            contentType: "application/json",
            type:'post',
            success:function (result) {
                if(result == "true"){
                    layer.msg('该手机号已被注册');
                }else{
                    //给定flag，用于判断是否进行验证码发送
                    flag = true;
                }
            },
            error:function (result) {
                layer.msg('数据请求异常');
            }
        })
    })
    //发送验证码按钮监听
    var nums = 60;
    $("#LAY-user-getsmscode").click(function () {
        if(flag){
            var data = {};
            data.mobile = $("#LAY-user-login-cellphone").val();
            //调用短信api接口发送验证码
            $.ajax({
                url: '/sendCode',
                data: JSON.stringify(data),
                contentType: 'application/json',
                type: 'post',
                success: function (result) {
                    if(result=="1"){
                        layer.msg('验证码发送成功');
                        settime();
                    }
                    if(result=="-4"){
                        layer.msg('手机号码格式不正确');
                    }
                    if(result=="-41"){
                        layer.msg('请输入手机号码');
                    }
                },
                error: function (result) {
                    layer.msg('数据异常');
                }
            })
        }
    })
    //60s倒计时
    function settime() {
        if(nums == 0){
            $("#LAY-user-getsmscode").attr("disabled",false);//设置按钮可点击
            $("#LAY-user-getsmscode").html("获取验证码"); //设置按钮内容
            nums = 60;
        }else{
            $("#LAY-user-getsmscode").attr("disabled",true);//设置按钮不可点击
            $("#LAY-user-getsmscode").html(nums+"s后重新获取"); //设置按钮内容
            nums--;
            setTimeout(function () {
                settime()
            },1000)
        }
    }
    var flag1 = true;
    //监听两次输入密码是否一致
    $("#LAY-user-login-repass").blur(function () {
        var password = $("#LAY-user-login-password").val();
        var repass = $("#LAY-user-login-repass").val();
        if(password!=repass){
            layer.msg('两次输入的密码不一致');
            flag1 = false;
        }else{
            flag1 = true;
        }

    })
    //监听注册按钮
    $("#register-button").click(function () {
        var data = {};
        data.mobile = $("#LAY-user-login-cellphone").val();
        data.code = $("#LAY-user-login-vercode").val();
        data.password = $("#LAY-user-login-password").val();
        data.accountName= $("#LAY-user-login-nickname").val();
        if(!flag1){
            layer.msg('两次输入的密码不一致');
        }else{
            $.ajax({
                url: '/user/register',
                data: JSON.stringify(data),
                contentType: 'application/json',
                type: 'post',
                success:function (result) {
                    if(result.msg == "SUCCESS"){
                        layer.msg('注册成功');
                        setTimeout(function () {
                            location.href = "/userView/login";
                        },1000)
                    }else{
                        layer.msg('验证码输入错误')
                    }
                },
                error:function () {
                    layer.msg('数据异常');
                }
            })
        }
    })
})