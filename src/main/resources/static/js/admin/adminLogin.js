$(document).ready(function () {
    //登录操作
    $(".layui-btn-fluid").click(function () {
        var data = {};
        data.mobile = $("#LAY-user-login-username").val();
        data.password = $("#LAY-user-login-password").val();
        $.ajax({
            url: '/admin/login',
            data: JSON.stringify(data),
            contentType: 'application/json',
            type: 'post',
            success: function (result) {
                if (result.msg == "success") {
                    setTimeout(function () {
                        location.href = "/adminView/index";
                    },1000)
                } else {
                    layer.msg('管理员账号密码有误');
                }
            },
            error: function () {
                layer.msg('数据请求异常');
            }
        })
    })
})