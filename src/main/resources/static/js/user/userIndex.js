$(function () {
    userIndexJs.bindEvent();
});
var userIndexJs = {
    bindEvent: function () {
        userIndexJs.event.isToDownloadImage();
        userIndexJs.event.orginalImageUpload();
        userIndexJs.event.initBanner();
    },
    event: {
        isToDownloadImage: function() {
            $.ajax({
                url: '/order/isDownload',
                type: 'post',
                data: JSON.stringify({}),
                contentType: 'application/json',
                success: function (result) {
                    if (result) {
                        $("#my-order").addClass("layui-badge-dot");
                        $("#personal-select").addClass("layui-badge-dot");
                        layer.open({
                            type: 1
                            ,title: false //不显示标题栏
                            ,closeBtn: false
                            ,area: '300px;'
                            ,shade: 0.8
                            ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                            ,btn: ['立即下载', '残忍拒绝']
                            ,btnAlign: 'c'
                            ,moveType: 1 //拖拽模式，0或者1
                            ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; ' +
                                'color: #fff; font-weight: 300;">亲！<br><br>发现您有已付款但未下载资源的订单哦！' +
                                '<br><br>建议您优先下载 ^_^</div>'
                            ,yes: function(index){
                                layer.close(index);
                                $.ajax({
                                    url: '/order/downloadImage',
                                    type: 'get',
                                    success: function (result) {
                                        for (i = 0; i<result.data.length; i++) {
                                             userIndexJs.method.downloadForCros(result.data[i],  i + ".bmp")
                                        }
                                    },
                                    error: function () {
                                        layer.msg('数据请求异常');
                                    }
                                })
                            }
                            ,btn2: function() {
                            }
                        });
                    }
                },
                error: function () {
                    layer.msg('数据请求异常');
                }
            })
        },
        orginalImageUpload: function () {
            layui.use('upload', function () {
                var $ = layui.jquery
                    , upload = layui.upload;
                //普通图片上传
                upload.render({
                    elem: '#image-upload'
                    , url: '/upload/imageUrlOss'
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
            if (data.orginalImage == "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/banner/%E5%9B%BE%E7%89%87%E4%B8%8A%E4%BC%A0%20%281%29.png") {
                layer.msg("请先上传图片");
                return;
            }
            if (data.hiddenData == "") {
                layer.msg("请输入需要藏入的信息");
                return;
            }
            layer.load();
            $.ajax({
                url: '/order/generateImage',
                data: JSON.stringify(data),
                type: 'post',
                contentType: 'application/json',
                success: function (result) {
                    layer.closeAll('loading');
                    layer.msg('信息藏入成功！')
                    $("#resultImage1").attr("src", result.map["resultImageOne"]);
                    $("#resultImage2").attr("src", result.map["resultImageTwo"]);
                },
                error: function () {
                    layer.msg('数据异常');
                }
            })
        },
        payAndDownload: function () {
            if ($("#original-image").attr("src") == "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/banner/%E5%9B%BE%E7%89%87%E4%B8%8A%E4%BC%A0%20%281%29.png") {
                layer.msg("请先上传图片");
                return;
            }
            if ($("#input-info").val() == "") {
                layer.msg("请先输入您要藏入的信息");
                return;
            }
            if ($("#resultImage1").attr("src") == "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/banner/%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87%20%283%29.png"
                || $("#resultImage2").attr("src") == "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/banner/%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87%20%283%29.png") {
                layer.msg("暂未发现有可支付的订单");
                return;
            }
            window.location.href = "/orderView/details";
        },
        downloadForCros: function (imageUrl, imageName) {//跨域请求OSS图片 并下载
            var x = new XMLHttpRequest();
            x.open("GET", imageUrl, true);
            x.responseType = 'blob';
            x.onload=function(e) {
                var url = window.URL.createObjectURL(x.response)
                var a = document.createElement('a');
                a.href = url
                a.download = imageName;
                a.click()
            }
            x.send();
        }
    }
}
