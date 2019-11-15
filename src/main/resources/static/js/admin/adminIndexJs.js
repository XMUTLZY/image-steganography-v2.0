$(function () {
    adminIndexJs.bindEvent();
});
var adminIndexJs = {
    bindEvent: function () {
        adminIndexJs.event.userList();
    },
    event: {
        userList: function () {
            layui.use('table', function () {
                var table = layui.table;
                $("#user-list").removeClass('layui-hide');
                $("#admin-list").addClass('layui-hide');
                $("#order-list").addClass('layui-hide');
                $("#system-list").addClass('layui-hide');
                //第一个实例
                table.render({
                    elem: '#user-list-table'
                    , height: 485
                    , url: '/admin/user/getAllUserList'
                    , page: true //开启分页
                    , limits: [5, 10, 20]
                    , limit: 10
                    , cols: [[ //表头
                        {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                        , {field: 'mobile', title: '手机', width: 120}
                        , {field: 'accountName', title: '用户名', width: 100}
                        , {field: 'realName', title: '姓名', width: 100}
                        , {field: 'city', title: '城市', width: 100}
                        , {field: 'status', title: '状态', width: 80, sort: true}
                        , {field: 'email', title: '邮箱', width: 130}
                        , {field: 'company', title: '单位', width: 150}
                        , {field: 'career', title: '职业', width: 110}
                        , {
                            field: 'portrait', title: '头像', width: 100, templet: function (d) {
                                return '<div onclick="adminIndexJs.method.show_img(this)" ><img src="' + d.portrait + '" alt="" width="50px" height="50px"></a></div>';
                            }
                        }
                        , {field: 'createTime', title: '创建时间', width: 180, templet:'<div>{{ layui.util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss") }}</div>', sort: true}
                        , {field: 'updateTime', title: '更新时间', width: 180, templet:'<div>{{ layui.util.toDateString(d.updateTime, "yyyy-MM-dd HH:mm:ss") }}</div>', sort: true}
                        , {
                            field: 'operate',
                            title: '操作',
                            width: 147,
                            fixed: 'right',
                            toolbar: "#user-list-table-operate"
                        }
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
                                    adminIndexJs.event.userList();
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
                                        "     <button style='margin-left: 150px;' type='button' class='layui-btn' onclick='adminIndexJs.method.updateUserBtn();'>提交</button></div>\n" +
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
    },
    method: {
        updateUserBtn: function () {
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
                    adminIndexJs.event.userList();
                }
            })
        },
        userSearch: function () {
            var data = {};
            data.mobile = $("#search-mobile").val();
            data.company = $("#search-company").val();
            data.accountName = $("#search-account-name").val();
            layui.use('table', function () {
                var table = layui.table;
                //第一个实例
                table.render({
                    elem: '#user-list-table'
                    , height: 485
                    , where: {
                        mobile: $("#search-mobile").val(),
                        company: $("#search-company").val(),
                        accountName: $("#search-account-name").val()
                    }
                    , method: 'post'
                    , contentType: 'application/json'
                    , url: '/admin/user/findUser'
                    , page: true //开启分页
                    , limits: [5, 10, 20]
                    , limit: 10
                    , skin: 'line'
                    , cols: [[ //表头
                        {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                        , {field: 'mobile', title: '手机', width: 120}
                        , {field: 'accountName', title: '用户名', width: 100}
                        , {field: 'realName', title: '姓名', width: 100}
                        , {field: 'city', title: '城市', width: 100}
                        , {field: 'status', title: '状态', width: 80, sort: true}
                        , {field: 'email', title: '邮箱', width: 130}
                        , {field: 'company', title: '单位', width: 150}
                        , {field: 'career', title: '职业', width: 110}
                        , {
                            field: 'portrait', title: '头像', width: 100, templet: function (d) {
                                return '<div onclick="adminIndexJs.method.show_img(this)" ><img src="' + d.portrait + '" alt="" width="50px" height="50px"></a></div>';
                            }
                        }
                        , {field: 'createTime', title: '创建时间', width: 180, sort: true}
                        , {field: 'updateTime', title: '更新时间', width: 180, sort: true}
                        , {
                            field: 'operate',
                            title: '操作',
                            width: 147,
                            fixed: 'right',
                            toolbar: "#user-list-table-operate"
                        }
                    ]]
                });
            });
        },
        addUser1: function () {
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
                    "     <button style='margin-left: 150px;' type='button' class='layui-btn' onclick='adminIndexJs.method.subUser1()'>提交</button></div>\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>\n"
            });
        },
        subUser1: function () {
            layer.close(layer.index);
            var data = {};
            data.mobile = $("#addphone").val();
            data.password = $("#addpassword").val();
            data.accountName = $("#addname").val()
            $.ajax({
                url: '/admin/user/addUser',
                type: 'post',
                data: JSON.stringify(data),
                contentType: 'application/json',
                success: function (result) {
                    if (result.msg == "SUCCESS") {
                        layer.msg('添加用户成功');
                        adminIndexJs.event.userList();
                        return;
                    }
                    layer.msg('添加用户失败');
                },
                error: function () {
                    layer.msg('数据异常');
                }
            })
        },
        orderList: function () {
            layui.use('table', function () {
                var table = layui.table;
                $("#order-list").removeClass('layui-hide');
                $("#admin-list").addClass('layui-hide');
                $("#user-list").addClass('layui-hide');
                $("#system-list").addClass('layui-hide');
                //第一个实例
                table.render({
                    elem: '#demo3'
                    , height: 485
                    , url: 'payList' //数据接口
                    , page: true //开启分页
                    , limits: [5, 10, 20]
                    , limit: 10
                    , cols: [[ //表头
                        {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                        , {field: 'phone', title: '手机号', width: 120}
                        , {
                            field: 'orginalImage', title: '原始图片', width: 100, templet: function (d) {
                                return '<div onclick="adminIndexJs.method.show_img(this)" ><img src="' + d.orginalImage + '" alt="" width="50px" height="50px"></a></div>';
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
        },
        adminList: function () {
            layui.use('table', function () {
                var table = layui.table;
                $("#admin-list").removeClass('layui-hide');
                $("#user-list").addClass('layui-hide');
                $("#order-list").addClass('layui-hide');
                $("#system-list").addClass('layui-hide');
                //第一个实例
                table.render({
                    elem: '#admin-list-table'
                    , height: 485
                    , url: '/admin/allAdminList' //数据接口
                    , page: true //开启分页
                    , limits: [5, 10, 20]
                    , limit: 10
                    , cols: [[ //表头
                        {field: 'id', title: 'ID', width: 70, sort: true, fixed: 'left'}
                        , {field: 'userName', title: '用户名', width: 120}
                        , {field: 'mobile', title: '手机号', width: 120}
                        , {field: 'realName', title: '姓名', width: 90}
                        , {field: 'roleName', title: '角色', width: 130}
                        , {field: 'status', title: '状态', width: 70}
                        , {field: 'email', title: '邮箱', width: 150}
                        , {
                            field: 'portrait', title: '头像', width: 90, templet: function (d) {
                                return '<div onclick="adminIndexJs.method.show_img(this)" ><img src="' + d.portrait + '" alt="" width="50px" height="50px"></a></div>';
                            }
                        }
                        , {field: 'createTime', title: '创建时间', width: 180, templet:'<div>{{ layui.util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss") }}</div>', sort: true}
                        , {field: 'updateTime', title: '修改时间', width: 180, templet:'<div>{{ layui.util.toDateString(d.updateTime, "yyyy-MM-dd HH:mm:ss") }}</div>', sort: true}
                        , {
                            field: 'operate',
                            title: '操作',
                            width: 147,
                            fixed: 'right',
                            toolbar: "#admin-list-table-operate",
                        }
                    ]]
                });
                table.on('tool(admin-list-table-fit)', function (obj) {
                    if (obj.event === 'del') {
                        layer.confirm('确定删除该管理员？', function (index) {
                            var dataRequest = {};
                            dataRequest.mobile = obj.data.mobile;
                            $.ajax({
                                url: '/admin/deleteAdmin',
                                data: JSON.stringify(dataRequest),
                                contentType: 'application/json',
                                type: 'post',
                                success: function () {
                                    layer.msg("删除成功");
                                    adminIndexJs.method.adminList();
                                }
                            })
                        })
                    } else {
                        var data = {};
                        data.mobile = obj.data.mobile;
                        $.ajax({
                            url: '/admin/showAdmin',
                            data: JSON.stringify(data),
                            contentType: 'application/json',
                            type: 'post',
                            success: function (result) {
                                var jsonlist = eval('(' + result + ')');//解析json
                                layer.open({
                                    type: 1,
                                    title: '修改管理员信息',
                                    shift: 7,
                                    area: 'auto',
                                    maxWidth: 800,
                                    maxHeight: 1200,
                                    shadeClose: true,
                                    content: "<div class='layui-form'>\n" +
                                        "  <div class=\"layui-form-item\">\n" +
                                        "     <label class=\"layui-form-label\">手机</label>\n" +
                                        "     <div class=\"layui-input-inline\">\n" +
                                        "        <input type=\"phone\" id=\"show-admin-mobile\" disabled=\"disabled\" required lay-verify=\"required\" value=" + jsonlist.mobile + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                        "     </div>\n" +
                                        "     <div class=\"layui-form-mid layui-word-aux\" style='color: red;'>无法更改</div>\n" +
                                        "   </div>" +
                                        "  <div class=\"layui-form-item\">\n" +
                                        "    <label class=\"layui-form-label\">用户名</label>\n" +
                                        "    <div class=\"layui-input-inline\">\n" +
                                        "      <input type=\"name\" id=\"show-admin-userName\" required lay-verify=\"required\" value=" + jsonlist.userName + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                        "    </div>\n" +
                                        "  </div>" +
                                        "  <div class=\"layui-form-item\">\n" +
                                        "    <label class=\"layui-form-label\">姓名</label>\n" +
                                        "    <div class=\"layui-input-inline\">\n" +
                                        "      <input type=\"name\" id=\"show-admin-realName\" required lay-verify=\"required\" value=" + jsonlist.realName + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                        "    </div>\n" +
                                        "  </div>" +
                                        "  <div class=\"layui-form-item\">\n" +
                                        "    <label class=\"layui-form-label\">邮箱</label>\n" +
                                        "    <div class=\"layui-input-inline\">\n" +
                                        "      <input type=\"email\" id=\"show-admin-email\" required lay-verify=\"required\" value=" + jsonlist.email + " autocomplete=\"off\" class=\"layui-input\">\n" +
                                        "    </div>\n" +
                                        "  </div>" +
                                        "  <div class=\"layui-form-item\">\n" +
                                        "    <div class=\"layui-input-inline\">\n" +
                                        "     <button style='margin-left: 150px;' type='button' class='layui-btn' onclick='adminIndexJs.method.updateAdminBtn();'>提交</button></div>\n" +
                                        "    </div>\n" +
                                        "  </div>\n" +
                                        "</div>\n"
                                });
                            }
                        })
                    }
                })
            });
        },
        addAdmin: function () {
            layui.use('layer', function (layer) {
                layer.open({
                    type: 1,
                    title: '添加管理员',
                    shift: 7,
                    area: 'auto',
                    maxWidth: 1000,
                    maxHeight: 800,
                    shadeClose: true,
                    content: $("#add-admin-panel")
                });
            });
        },
        subAdmin: function () {
            layer.close(layer.index);
            var data = {};
            data.mobile = $("#add-admin-mobile").val();
            data.password = $("#add-admin-password").val();
            data.userName = $("#userName").val();
            data.role = $("#add-admin-role").val();
            $.ajax({
                url: '/admin/register',
                type: 'post',
                data: JSON.stringify(data),
                contentType: 'application/json',
                success: function (result) {
                    if (result.msg == "注册成功") {
                        layer.msg('添加管理员成功');
                        adminIndexJs.method.adminList();
                        return;
                    }
                    layer.msg('添加管理员失败');
                },
                error: function () {
                    layer.msg('数据异常');
                }
            })
        },
        updateAdminBtn: function () {
            layer.close(layer.index);
            var data = {};
            data.mobile = $("#show-admin-mobile").val();
            data.userName = $("#show-admin-userName").val();
            data.realName = $("#show-admin-realName").val();
            data.email = $("#show-admin-email").val();
            $.ajax({
                url: '/admin/updateAdmin',
                data: JSON.stringify(data),
                contentType: 'application/json',
                type: 'post',
                success: function (result) {
                    layer.msg(result.msg);
                    adminIndexJs.method.adminList();
                }
            })
        },
        systemDynamicsList: function () {
            $("#system-list").removeClass('layui-hide');
            $("#admin-list").addClass('layui-hide');
            $("#user-list").addClass('layui-hide');
            $("#order-list").addClass('layui-hide');
            layui.use('laytpl', function () {
                var laytpl = layui.laytpl;
                var getTpl = document.getElementById("system-dynamic-list").innerHTML;
                $.ajax({
                    url: '/admin/systemDynamic',
                    type: 'post',
                    success: function (result) {
                        laytpl(getTpl).render(result, function (html) {
                            document.getElementById("system-list").innerHTML = html;
                        });
                    },
                    error: function () {
                        layer.msg("数据请求异常");
                    }
                });
            })
        },
        systemDynamicsSearchBtn: function () {
            layui.use('laytpl', function () {
                var laytpl = layui.laytpl;
                var getTpl = document.getElementById("system-dynamic-list").innerHTML;
                layer.load();
                $.ajax({
                    url: '/admin/systemDynamic/search',
                    data: {
                        key: $("#key-search").val()
                    },
                    type: 'post',
                    success: function (result) {
                        layer.closeAll('loading');
                        laytpl(getTpl).render(result, function (html) {
                            document.getElementById("system-list").innerHTML = html;
                        });
                    },
                    error: function () {
                        layer.msg("数据请求异常");
                    }
                })
            })
        },
        show_img: function (t) {
            var t = $(t).find("img");
            //页面层
            layer.open({
                type: 1,
                title: '头像',
                skin: 'layui-layer-rim', //加上边框
                area: ['80%', '80%'], //宽高
                shadeClose: true, //开启遮罩关闭
                end: function (index, layero) {
                    return false;
                },
                content: '<div style="text-align:center"><img src="' + $(t).attr('src') + '" /></div>'
            });
        }
    }
}
