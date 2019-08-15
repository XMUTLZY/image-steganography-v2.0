/*
 * 显示用户列表
 * */
function userList() {
    layui.use('table', function () {
        var table = layui.table;
        $("#user-list").removeClass('layui-hide');
        $("#admin1").addClass('layui-hide');
        $("#user2").addClass('layui-hide');
        //第一个实例
        table.render({
            elem: '#user-list-table'
            , height: 500
            , url: '/admin/user/getAllUserList'
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                , {field: 'mobile', title: '手机', width: 120}
                , {field: 'accountName', title: '用户名', width: 100}
                , {field: 'realName', title: '姓名', width: 100}
                , {field: 'city', title: '城市', width: 100}
                , {field: 'status', title: '状态', width: 70, sort: true}
                , {field: 'email', title: '邮箱', width: 130}
                , {field: 'company', title: '单位', width: 180}
                , {field: 'career', title: '职业', width: 110}
                , {field: 'portrait', title: '头像', width: 100}
                , {field: 'createTime', title: '创建时间', width: 180, sort: true}
                , {field: 'updateTime', title: '更新时间', width: 180, sort: true}
                , {field: 'operate', title: '操作', width: 147, fixed: 'right', toolbar: "#user-list-table-operate"}
            ]]
        });
        table.on('tool(user-list-table-fit)', function (obj) {
            if (obj.event === 'del') {
                layer.confirm('确定删除该用户？', function (index) {
                    var dataRequest = {};
                    dataRequest.mobile = obj.data.mobile;
                    $.ajax({
                        url: '/admin/user/deleteUser',
                        data: JSON.stringify(dataRequest),
                        contentType: 'application/json',
                        type: 'post',
                        success: function () {
                            layer.msg("删除成功");
                            userList();
                        }
                    })
                })
            } else {
                var data = {};
                data.mobile = obj.data.mobile;
                $.ajax({
                    url: '/admin/user/showUser',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    type: 'post',
                    success: function (result) {
                        var jsonlist = eval('(' + result + ')');//解析json
                        layer.open({
                            type: 1,
                            title: '修改用户信息',
                            shift: 7,
                            area: 'auto',
                            maxWidth: 800,
                            maxHeight: 1200,
                            shadeClose: true,
                            content: "<div class='layui-form'>\n" +
                                "  <div class=\"layui-form-item\">\n" +
                                "     <label class=\"layui-form-label\">手机</label>\n" +
                                "     <div class=\"layui-input-inline\">\n" +
                                "        <input type=\"phone\" id=\"show-mobile\" disabled=\"disabled\" required lay-verify=\"required\" value=" + jsonlist.mobile + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "     </div>\n" +
                                "     <div class=\"layui-form-mid layui-word-aux\" style='color: red;'>无法更改</div>\n" +
                                "   </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">用户名</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"show-account-name\" required lay-verify=\"required\" value=" + jsonlist.accountName + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">姓名</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"show-real-name\" required lay-verify=\"required\" value=" + jsonlist.realName + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">城市</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"show-city\" required lay-verify=\"required\" value=" + jsonlist.city + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">邮箱</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"email\" id=\"show-email\" required lay-verify=\"required\" value=" + jsonlist.email + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">单位</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"show-company\" required lay-verify=\"required\" value=" + jsonlist.company + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <label class=\"layui-form-label\">职业</label>\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "      <input type=\"name\" id=\"show-career\" required lay-verify=\"required\" value=" + jsonlist.career + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                "    </div>\n" +
                                "  </div>" +
                                "  <div class=\"layui-form-item\">\n" +
                                "    <div class=\"layui-input-inline\">\n" +
                                "     <button style='margin-left: 150px;' type='button' class='layui-btn' onclick='updateUserBtn()'>提交</button></div>\n" +
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
* 监听编辑提示框的提交按钮
* */
function updateUserBtn() {
    layer.close(layer.index);
    var data = {};
    data.mobile = $("#show-mobile").val();
    data.accountName = $("#show-account-name").val();
    data.realName = $("#show-real-name").val();
    data.city = $("#show-city").val();
    data.email = $("#show-email").val();
    data.company = $("#show-company").val();
    data.career = $("#show-career").val();
    $.ajax({
        url: '/admin/user/updateUser',
        data: JSON.stringify(data),
        contentType: 'application/json',
        type: 'post',
        success: function (result) {
            layer.msg(result.msg);
            userList();
        }
    })
}

/*
* 用户查询
* */
function userSearch() {
    var data = {};
    data.mobile = $("#search-mobile").val();
    data.company = $("#search-company").val();
    data.accountName = $("#search-account-name").val();
    layui.use('table', function () {
        var table = layui.table;
        //第一个实例
        table.render({
            elem: '#user-list-table'
            , height: 500
            , where: JSON.stringify(data)
            , url: '/admin/user/findUser'
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                , {field: 'mobile', title: '手机', width: 120}
                , {field: 'accountName', title: '用户名', width: 100}
                , {field: 'realName', title: '姓名', width: 100}
                , {field: 'city', title: '城市', width: 100}
                , {field: 'status', title: '状态', width: 70, sort: true}
                , {field: 'email', title: '邮箱', width: 130}
                , {field: 'company', title: '单位', width: 180}
                , {field: 'career', title: '职业', width: 110}
                , {field: 'portrait', title: '头像', width: 100}
                , {field: 'createTime', title: '创建时间', width: 180, sort: true}
                , {field: 'updateTime', title: '更新时间', width: 180, sort: true}
                , {field: 'operate', title: '操作', width: 147, fixed: 'right', toolbar: "#user-list-table-operate"}
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
                , {
                    field: 'orginalImage', title: '原始图片', width: 100, templet: function (d) {
                        return '<div onclick="show_img(this)" ><img src="' + d.orginalImage + '" alt="" width="50px" height="50px"></a></div>';
                    }
                }
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