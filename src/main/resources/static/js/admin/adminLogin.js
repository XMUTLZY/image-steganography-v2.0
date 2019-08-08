$(document).ready(function () {
    //登录操作
    $(".layui-btn-fluid").click(function () {
            $.ajax({
                url:'/admin/login',
                data:{
                    phone:$("#LAY-user-login-username").val(),
                    password:$("#LAY-user-login-password").val()
                },
                type:'post',
                success:function (result) {
                    if(result=="true"){
                        layer.msg('登录成功');
                        var data = $("#LAY-user-login-username").val();
                        alert(data);
                        location.href = "adminIndex.html";
                    }else{
                        layer.msg('用户名或密码错误');
                    }
                },
                error:function (result) {
                    layer.msg('数据请求异常');
                }
            })
    })
})