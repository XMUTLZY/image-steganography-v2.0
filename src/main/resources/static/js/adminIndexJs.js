//JavaScript代码区域
layui.use('element', function () {
    var element = layui.element;
    /*
     * 登录逻辑实现
     * */
    phone = '未登录';
    // phone = window.location.href;
    // var twodata = phone.split("="); //截取 url中的“=”,获得“=”后面的参数
    // phone = decodeURI(twodata[1]); //decodeURI解码
    phone = $.session.get("adminPhone");
    if (phone == "undefined"||phone == null) {
        logOrre = '登录';
        phone = '未登录';
        link = 'adminLogin.html';
        layer.msg('您还未登录哦');
    } else {
        logOrre = '退出登录';
        link = 'javascript:;';
        //获取该管理员的用户名
        $.ajax({
            url: 'getName',
            type: 'get',
            data: {
                phone: phone
            },
            async: false,
            dataType: 'html',
            success: function (result) {
                phone = result;
                layer.msg('欢迎您' + phone);
            }
        })
    }
    /*
    * 修改登录之后的用户名
    * */
    var data1 = {
        phone: phone
    }
    var html = template(document.getElementById('name_temp').innerHTML, data1);
    document.getElementById('username').innerHTML = html;
    /*
    * 修改登录之后的"登录"按钮变更
    * */
    var data2 = {
        logOrre: logOrre
    }
    var html = template(document.getElementById('logOrre_temp').innerHTML, data2);
    document.getElementById('loginOrRegister').innerHTML = html;
});

/*
 * 退出登录
 * */
function isLogout() {
    if (logOrre == '退出登录') {
        logOrre = '登录';
        phone = '未登录';
        link = 'adminLogin.html';
        $.session.remove("adminPhone")
        window.location.href = "adminLogin.html";
    }
}

/*
 * 显示用户列表
 * */
function user1() {
    layui.use('table', function () {
        var table = layui.table;
        $("#user1").removeClass('layui-hide');
        $("#admin1").addClass('layui-hide');
        $("#user2").addClass('layui-hide');
        //第一个实例
        table.render({
            elem: '#demo'
            , height: 500
            , url: 'userList' //数据接口
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                , {field: 'phone', title: '手机号', width: 120}
                , {field: 'code', title: '验证码', width: 100}
                , {field: 'password', title: '密码', width: 150}
                , {field: 'name', title: '用户名', width: 100}
                , {field: 'sex', title: '性别', width: 70, sort: true}
                , {field: 'city', title: '城市', width: 120}
                , {field: 'email', title: '邮箱', width: 180}
                , {field: 'company', title: '单位', width: 180, sort: true}
                , {field: 'career', title: '职业', width: 80, sort: true}
                , {field: 'wealth', title: '积分', width: 80, sort: true}
                , {field: 'operate', title: '操作', width: 147, fixed: 'right', toolbar: "#operate"}
            ]]
        });
        /*
        * 监听用户列表的编辑和删除按钮
        * */
        table.on('tool(user1)', function (obj) {
            if (obj.event === 'del') {
                layer.confirm('确定删除该用户？', function (index) {
                    $.ajax({
                        url: 'deleteUser',
                        data: {
                            id: obj.data.id
                        },
                        async: false,
                        type: 'post',
                        success: function () {
                            layer.msg("删除成功");
                            user1();
                        }
                    })
                })
            } else {
                $.ajax({
                    url: 'showUser',
                    data: {
                        phone: obj.data.phone,
                    },
                    type: 'post',
                    async: false,
                    success: function (result) {
                        var string = JSON.stringify(result);//将json转化成字符串
                        var jsonlist = eval('(' + string + ')');//解析json
                        layer.open({
                            type: 1,
                            title: '修改用户信息',
                            shift: 7,
                            area: 'auto',
                            maxWidth: 600,
                            maxHeight: 800,
                            shadeClose: true,
                            content: "<div class='layui-form'>\n" +
                                "<div class=\"layui-form-item\">\n" +
                                "       <label class=\"layui-form-label\">手机号</label>\n" +
                                "       <div class=\"layui-input-inline\">\n" +
                                "           <input type=\"phone\" id=\"showphone\" disabled=\"disabled\" required lay-verify=\"required\" value=" + jsonlist.phone + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "       </div>\n" +
                                "       <div class=\"layui-form-mid layui-word-aux\" style='color: red;'>无法更改</div>\n" +
                                "      </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">密码</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"text\" id=\"showpassword\" required lay-verify=\"required\" value=" + jsonlist.password + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">用户名</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"showname\" required lay-verify=\"required\" value=" + jsonlist.name + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">城市</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"showcity\" required lay-verify=\"required\" value=" + jsonlist.city + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">邮箱</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"email\" id=\"showemail\" required lay-verify=\"required\" value=" + jsonlist.email + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">单位</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"showcompany\" required lay-verify=\"required\" value=" + jsonlist.company + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">职业</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"showcareer\" required lay-verify=\"required\" value=" + jsonlist.career + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class='layui-form-item'>\n" +
                                "    <label class='layui-form-label'>性别</label>\n" +
                                "    <div class='layui-inline'>\n" +
                                "      <input type=\"name\" id=\"showsex\" required lay-verify=\"required\" value=" + jsonlist.sex + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>\n" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "     <button style='margin-left: 150px;' type='button' class='layui-btn' onclick='updateUser1()'>提交</button></div>\n" +
                                "    </div>\n" +
                                "  </div>\n" +
                                "</div>\n"
                        });
                    }
                })
            }
        })
    });
}

/*
* 用户查询
* */
function userSearch() {
    layui.use('table', function () {
        var table = layui.table;
        //第一个实例
        table.render({
            elem: '#demo'
            , height: 500
            , where: {
                email: $("#user1email").val(),
                name: $("#user1name").val(),
                phone: $("#user1phone").val()
            }
            , url: 'getUser' //数据接口
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                , {field: 'phone', title: '手机号', width: 120}
                , {field: 'code', title: '验证码', width: 100}
                , {field: 'password', title: '密码', width: 150}
                , {field: 'name', title: '用户名', width: 100}
                , {field: 'sex', title: '性别', width: 70, sort: true}
                , {field: 'city', title: '城市', width: 120}
                , {field: 'email', title: '邮箱', width: 180}
                , {field: 'company', title: '单位', width: 180, sort: true}
                , {field: 'career', title: '职业', width: 80, sort: true}
                , {field: 'wealth', title: '积分', width: 80, sort: true}
                , {field: 'operate', title: '操作', width: 147, fixed: 'right',toolbar: "#operate"}
            ]]
        });
    });
}

/*
* 添加用户
* */
function addUser1() {
    layer.open({
        type: 1,
        title: '添加用户',
        shift: 7,
        area: 'auto',
        maxWidth: 600,
        maxHeight: 600,
        shadeClose: true,
        content: "<div class='layui-form'>\n" +
            "<div class=\"layui-form-item\">\n" +
            "       <label class=\"layui-form-label\">手机号</label>\n" +
            "       <div class=\"layui-input-inline\">\n" +
            "           <input type=\"phone\" id=\"addphone\" required lay-verify=\"required\" placeholder=\"请输入手机号\" autocomplete=\"off\" class=\"layui-input\">\n" +
            "       </div>\n" +
            "       <div class=\"layui-form-mid layui-word-aux\">辅助文字</div>\n" +
            "      </div>" +
            "  <div class=\"layui-form-item\">\n" +
            "    <label class=\"layui-form-label\">密码</label>\n" +
            "    <div class=\"layui-input-inline\">\n" +
            "      <input type=\"text\" id=\"addpassword\" required lay-verify=\"required\" placeholder=\"请输入密码\" autocomplete=\"off\" class=\"layui-input\">\n" +
            "    </div>\n" +
            "  </div>" +
            "  <div class=\"layui-form-item\">\n" +
            "    <label class=\"layui-form-label\">用户名</label>\n" +
            "    <div class=\"layui-input-inline\">\n" +
            "      <input type=\"name\" id=\"addname\" required lay-verify=\"required\" placeholder=\"请输入用户名\" autocomplete=\"off\" class=\"layui-input\">\n" +
            "    </div>\n" +
            "  </div>" +
            "  <div class=\"layui-form-item\">\n" +
            "    <div class=\"layui-input-inline\">\n" +
            "     <button style='margin-left: 150px;' type='button' class='layui-btn' onclick='subUser1()'>提交</button></div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n"
    });
}

/*
* 添加用户弹出框的 提交按钮监听
* */
function subUser1() {
    layer.close(layer.index);
    $.ajax({
        url: 'subUser',
        type: 'post',
        data: {
            phone: $("#addphone").val(),
            password: $("#addpassword").val(),
            name: $("#addname").val()
        },
        dataType: 'html',
        success: function (result) {
            layer.msg('添加用户成功');
            user1();
        }
    })
}

/*
* 监听编辑提示框的提交按钮
* */
function updateUser1() {
    layer.close(layer.index);
    $.ajax({
        url: 'updateUser',
        data: {
            phone: $("#showphone").val(),
            password: $("#showpassword").val(),
            name: $("#showname").val(),
            city: $("#showcity").val(),
            email:$("#showemail").val(),
            company:$("#showcompany").val(),
            career:$("#showcareer").val(),
            sex:$("#showsex").val()
        },
        success:function (result) {
            layer.msg("成功修改用户信息");
            user1();
        }
    })
}

/*
* 显示所有订单信息
* */
function user2() {
    layui.use('table', function () {
        var table = layui.table;
        $("#user2").removeClass('layui-hide');
        $("#admin1").addClass('layui-hide');
        $("#user1").addClass('layui-hide');
        //第一个实例
        table.render({
            elem: '#demo3'
            , height: 500
            , url: 'payList' //数据接口
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                , {field: 'phone', title: '手机号', width: 120}
                , {field: 'orginalImage', title: '原始图片', width: 100,templet:function (d) {
                        return '<div onclick="show_img(this)" ><img src="'+d.orginalImage+'" alt="" width="50px" height="50px"></a></div>';
                    }}
                , {field: 'inputInfo', title: '藏入信息', width: 110}
                , {field: 'money', title: '已付金额', width: 105}
                , {field: 'pay_time', title: '订单号', width: 145}
                , {field: 'star', title: '满意度', width: 80}
                , {field: 'evaluate', title: '评价', width: 140}
                , {field: 'evaluate_time', title: '评价时间', width: 105}
                , {field: 'operate', title: '操作', width: 147, toolbar: "#operate"}
            ]]
        });
    });
}

/*
* 显示所有管理员信息
* */
function admin1() {
    layui.use('table', function () {
        var table = layui.table;
        $("#admin1").removeClass('layui-hide');
        $("#user1").addClass('layui-hide');
        $("#user2").addClass('layui-hide');
        //第一个实例
        table.render({
            elem: '#demo2'
            , height: 500
            , url: 'adminList' //数据接口
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                , {field: 'phone', title: '手机号', width: 190}
                , {field: 'name', title: '用户名', width: 200}
                , {field: 'password', title: '密码', width: 180}
                , {field: 'role', title: '角色', width: 170}
                , {field: 'jointime', title: '加入时间', width: 150, sort: true}
                , {field: 'operate', title: '操作', width: 167, toolbar: "#operate2"}
            ]]
        });
    });
}
/*
* 站长添加管理员信息
* */
function addAdmin1() {
    layer.open({
        type: 1,
        title: '添加管理员',
        shift: 7,
        area: 'auto',
        maxWidth: 600,
        maxHeight: 600,
        shadeClose: true,
        content: "<div class='layui-form'>\n" +
            "<div class=\"layui-form-item\">\n" +
            "       <label class=\"layui-form-label\">手机号</label>\n" +
            "       <div class=\"layui-input-inline\">\n" +
            "           <input type=\"phone\" id=\"addphone\" required lay-verify=\"required\" placeholder=\"请输入手机号\" autocomplete=\"off\" class=\"layui-input\">\n" +
            "       </div>\n" +
            "       <div class=\"layui-form-mid layui-word-aux\">辅助文字</div>\n" +
            "      </div>" +
            "  <div class=\"layui-form-item\">\n" +
            "    <label class=\"layui-form-label\">密码</label>\n" +
            "    <div class=\"layui-input-inline\">\n" +
            "      <input type=\"text\" id=\"addpassword\" required lay-verify=\"required\" placeholder=\"请输入密码\" autocomplete=\"off\" class=\"layui-input\">\n" +
            "    </div>\n" +
            "  </div>" +
            "  <div class=\"layui-form-item\">\n" +
            "    <label class=\"layui-form-label\">用户名</label>\n" +
            "    <div class=\"layui-input-inline\">\n" +
            "      <input type=\"name\" id=\"addname\" required lay-verify=\"required\" placeholder=\"请输入用户名\" autocomplete=\"off\" class=\"layui-input\">\n" +
            "    </div>\n" +
            "  </div>" +
            "  <div class=\"layui-form-item\">\n" +
            "    <label class=\"layui-form-label\">角色</label>\n" +
            "    <select name=\"admin1role\" lay-verify=\"required\" autocomplete=\"off\">\n" +
            "      <option value=\"\"></option>\n" +
            "      <option value=\"0\">副站长</option>\n" +
            "      <option value=\"1\">超级管理员</option>\n" +
            "      <option value=\"2\">管理员</option>\n" +
            "      <option value=\"3\">游客</option>\n" +
            "    </div>\n" +
            "  </div>" +
            "  <div class=\"layui-form-item\">\n" +
            "    <div class=\"layui-input-inline\">\n" +
            "     <button style='margin-left: 150px;' type='button' class='layui-btn' onclick='subUser1()'>提交</button></div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n"
    });
}

/*
* 显示大图片
* */
function show_img(t) {
    var t = $(t).find("img");
    //页面层
    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['80%', '80%'], //宽高
        shadeClose: true, //开启遮罩关闭
        end: function (index, layero) {
            return false;
        },
        content: '<div style="text-align:center"><img src="' + $(t).attr('src') + '" /></div>'
    });
}