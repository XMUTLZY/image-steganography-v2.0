//
// /*
// * 登录逻辑实现
// * */
// phone = '未登录';
// phone = $.session.get("userPhone"); //获取手机号
// orginalImage = $.session.get("orginalImage");//获取原始图片
// money = $.getUrlParam("total_amount");//获取金额
// time = $.getUrlParam("out_trade_no");//获取订单时间及订单号
// /*
// * 发送订单金额到后台中保存
// * */
// $.ajax({
//     url: 'setMoney',
//     type: 'post',
//     data: {
//         phone: phone,
//         money: money,
//         time: time,
//         orginalImage: orginalImage
//     },
//     success: function (result) {
//         //下载图片
//         var a = document.createElement('a');
//         var b = document.createElement('a');
//         var event = new MouseEvent('click');
//         var event2 = new MouseEvent('click');
//         a.download = 'image1';
//         b.download = 'image2'
//         a.href = result.image1;
//         b.href = result.image2
//         a.dispatchEvent(event);
//         b.dispatchEvent(event2);
//     }
// })
// if (typeof (phone) == "undefined") {
//     logOrre = '登录/注册';
//     phone = '未登录';
//     link = 'userLogin.html';
// } else {
//     logOrre = '退出登录';
//     link = 'javascript:;';
// }
// /*
// * 修改登录之后的用户名
// * */
// var data1 = {
//     phone: phone
// }
// var html = template(document.getElementById('name_temp').innerHTML, data1);
// document.getElementById('username').innerHTML = html;
// /*
// * 修改登录之后的"登录/注册按钮变更"
// * */
// var data2 = {
//     logOrre: logOrre
// }
// var html = template(document.getElementById('logOrre_temp').innerHTML, data2);
// document.getElementById('loginOrRegister').innerHTML = html;
// /*
// * 导航栏
// * */
// //注意：导航 依赖 element 模块，否则无法进行功能性操作
// layui.use('element', function () {
//     var element = layui.element;
//
//     //…
// });
//
// /*
// * 退出登录
// * */
// function isLogout() {
//     if (logOrre == '退出登录') {
//         $.session.remove("userPhone");
//         logOrre = '登录/注册';
//         phone = '未登录';
//         link = 'userLogin.html';
//         window.location.href = "userIndex.html";
//     }
// }
//
// /*
// * 轮播
// * */
// layui.use('carousel', function () {
//     var carousel = layui.carousel;
//     //建造实例
//     carousel.render({
//         elem: '#test'
//         , width: '100%' //设置容器宽度
//         , arrow: 'always' //始终显示箭头
//         //,anim: 'updown' //切换动画方式
//     });
// });
// /*
// * 监听“生成藏入信息后的图片”按钮
// * */
// $("#btn_generate").click(function () {
//     if (phone == '未登录') {
//         layer.msg('请您先登录哦！')
//     } else {
//         flag = true;
//         //将原始图片存在session中
//         $.session.set("orginalImage", $("#orginalImage").val());
//         generate();
//     }
// })
//
// function generate() {
//     index = layer.load();//进度圈
//     $.ajax({
//         url: 'addInfo',
//         type: 'post',
//         data: {
//             orginalImage: $("#orginalImage").val(),
//             phone: phone,
//             inputInfo: $("#inputInfo").val()
//         },
//         success: function (result) {
//             string = JSON.stringify(result);
//             jsonlist = eval('(' + string + ')');
//             resultl = result.result1;
//             result2 = result.result2;
//             $("#resultImage1").attr('src', resultl);
//             $("#resultImage2").attr('src', result2);
//             layer.close(index);//关闭进度圈
//             layer.msg('信息藏入成功');
//         }
//     })
// }
//
// /*
// * 监听"购买并下载"按钮
// * */
// $("#btn_buy").click(function () {
//     if (phone == '未登录') {
//         layer.msg('请您先登录哦！')
//     }
//     if (flag && phone != '未登录') {
//         layer.open({
//             type: 2,
//             title: '支付宝',
//             shadeClose: true,
//             shade: 0.8,
//             area: ['100%', '80%'],
//             content: 'QRcode.jsp' //iframe的url
//         });
//     }
// })
//
// /*
// * 点击菜单栏1（图像隐写）操作
// * */
// function show1() {
//     $("#menu1").removeClass('layui-hide');
//     $("#menu2").addClass('layui-hide');
//     $("#menu3").addClass('layui-hide');
// }
//
// /*
// * 点击菜单栏2（信息提取操作）
// * */
// function show2() {
//     $("#menu2").removeClass('layui-hide');
//     $("#menu1").addClass('layui-hide');
//     $("#menu3").addClass('layui-hide');
// }
//
// /*
// * 点击菜单栏3（隐写算法）
// * */
// function show3() {
//     $("#menu3").removeClass('layui-hide');
//     $("#menu1").addClass('layui-hide');
//     $("#menu2").addClass('layui-hide');
// }
$(function () {
    userIndexJs.bindEvent();
});
var userIndexJs = {
    bindEvent: function () {
        userIndexJs.event.orginalImageUpload();
        userIndexJs.event.initBanner();
    },
    event: {
        orginalImageUpload: function () {
            layui.use('upload', function () {
                var $ = layui.jquery
                    , upload = layui.upload;
                //普通图片上传
                upload.render({
                    elem: '#image-upload'
                    , url: '/upload/imageUrl'
                    , accept: 'images'
                    , before: function (obj) {
                        layer.load();
                    }
                    , done: function (res) {
                        layer.closeAll('loading');
                        $("#original-image").attr('src', res.msg);
                    }
                    , error: function (index, upload) {
                        layer.msg("错误");
                    }
                });
            });
        },
        initBanner: function () {
            layui.use('carousel', function(){
                var carousel = layui.carousel;
                //建造实例
                carousel.render({
                    elem: '#banner'
                    ,width: '100%' //设置容器宽度
                    ,arrow: 'always' //始终显示箭头
                    //,anim: 'updown' //切换动画方式
                });
            });
        }
    },
    method: {
        generateImage: function () {
            var data = {};
            data.hiddenData = $("#input-info").val();
            data.orginalImage = $("#original-image").attr("src");
            if (data.orginalImage == undefined) {
                layer.msg("请先上传图片");
                return;
            }
            if (data.hiddenData == "") {
                layer.msg("请输入需要藏入的信息");
                return;
            }
            $.ajax({
                url: '/order/generateImage',
                data: JSON.stringify(data),
                type: 'post',
                contentType: 'application/json',
                success: function (result) {
                    
                },
                error: function () {
                    
                }
            })
        }
    }
}
