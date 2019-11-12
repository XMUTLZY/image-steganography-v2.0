$(function () {
    personalOrders.bindEvent();
})
var personalOrders = {
    bindEvent: function () {
        personalOrders.method.orderList(null, '#order-list-table');
    },
    event: {

    },
    method: {
        orderList: function (orderStatus, model) {
            layui.use('table', function () {
                if (model == '#order-list-table') {
                    $("#order-list").removeClass('layui-hide');
                    $("#order-deal-list").addClass('layui-hide');
                    $("#order-over-list").addClass('layui-hide');
                }
                if (model == 'order-deal-list-table') {
                    $("#order-deal-list").removeClass('layui-hide');
                    $("#order-list").addClass('layui-hide');
                    $("#order-over-list").addClass('layui-hide');
                }
                if (model == 'order-over-list-table') {
                    $("#order-over-list").removeClass('layui-hide');
                    $("#order-list").addClass('layui-hide');
                    $("#order-deal-list").addClass('layui-hide');
                }
                personalOrders.method.orderPublicTable(orderStatus, model);
            });
        },
        orderPublicTable: function (orderStatus, model) {
            var table = layui.table;
            table.render({
                elem: '#order-list-table'
                , height: 485
                , url: '/order/personalOrders'
                , where: {
                    orderStatus: orderStatus
                }
                , method: 'post'
                , page: true //开启分页
                , limits: [5, 10, 20]
                , limit: 10
                , cols: [[ //表头
                    {field: 'orderNumber', title: '订单号', width: 190}
                    , {
                        field: 'orginalImage', title: '原始图片', width: 100, templet: function (d) {
                            return '<div onclick="personalOrders.method.show_img(this)" ><img src="' + d.orginalImage + '" alt="" width="50px" height="50px"></a></div>';
                        }
                    }
                    , {field: 'hiddenData', title: '藏入信息', width: 220}
                    , {field: 'paymentAmount', title: '已付金额', width: 95}
                    , {
                        field: 'resultImage1', title: '结果图1', width: 100, templet: function (d) {
                            if (d.paymentStatusString == '待支付') {
                                d.resultImage1 = d.resultImage1 + "?x-oss-process=style/resultImage_style2";
                            }
                            return '<div onclick="personalOrders.method.show_img(this)" ><img src="' + d.resultImage1 + '" alt="" width="50px" height="50px"></a></div>';
                        }
                    }
                    , {
                        field: 'resultImage2', title: '结果图2', width: 100, templet: function (d) {
                            if (d.paymentStatusString == '待支付') {
                                d.resultImage2 = d.resultImage2 + "?x-oss-process=style/resultImage_style2";
                            }
                            return '<div onclick="personalOrders.method.show_img(this)" ><img src="' + d.resultImage2 + '" alt="" width="50px" height="50px"></a></div>';
                        }
                    }
                    , {field: 'paymentStatusString', title: '付款状态', width: 100}
                    , {field: 'downloadStatusString', title: '下载状态', width: 100}
                    , {field: 'orderTime', title: '订单生成时间', width: 175}
                    , {field: 'operate', title: '操作', width: 253, toolbar: "#order-list-table-operate"}
                ]]
            });
            table.on('tool(order-list-table-fit)', function (obj) {
                if (obj.event == 'info') {
                    personalOrders.method.downloadForCros(obj.data.resultImage1, obj.data.orderNumber + "_result_1.bmp");
                    personalOrders.method.downloadForCros(obj.data.resultImage2, obj.data.orderNumber + "_result_2.bmp");
                }
                if (obj.event == 'edit') {
                    window.location.href = "/orderView/details";
                }
            });
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