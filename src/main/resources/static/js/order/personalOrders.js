$(function () {
    personalOrders.bindEvent();
})
var personalOrders = {
    bindEvent: function () {
        personalOrders.method.orderList();
    },
    event: {
        
    },
    method: {
        orderList: function () {
            layui.use('table', function () {
                $("#order-list").removeClass('layui-hide');
                $("#order-deal-list").addClass('layui-hide');
                $("#order-over-list").addClass('layui-hide');
                personalOrders.method.orderPublicTable(null, '#order-list-table');
            });
        },
        orderPublicTable: function (orderStatus, model) {
            var table = layui.table;
            table.render({
                elem: model
                , height: 485
                , url: '/order/personalOrders'
                , where: {
                    orderStatus: orderStatus
                }
                , method: 'post'
                , page: false //开启分页
                , cols: [[ //表头
                    {field: 'orderNumber', title: '订单号', width: 120}
                    , {
                        field: 'orginalImage', title: '原始图片', width: 100, templet: function (d) {
                            return '<div onclick="personalOrders.method.show_img(this)" ><img src="' + d.orginalImage + '" alt="" width="50px" height="50px"></a></div>';
                        }
                    }
                    , {field: 'hiddenData', title: '藏入信息', width: 110}
                    , {field: 'paymentAmount', title: '已付金额', width: 105}
                    , {
                        field: 'resultImage1', title: '结果图1', width: 100, templet: function (d) {
                            return '<div onclick="personalOrders.method.show_img(this)" ><img src="' + d.resultImage1 + '" alt="" width="50px" height="50px"></a></div>';
                        }
                    }
                    , {
                        field: 'resultImage2', title: '结果图2', width: 100, templet: function (d) {
                            return '<div onclick="personalOrders.method.show_img(this)" ><img src="' + d.resultImage2 + '" alt="" width="50px" height="50px"></a></div>';
                        }
                    }
                    , {field: 'paymentStatusString', title: '付款状态', width: 80}
                    , {field: 'downloadStatusString', title: '下载状态', width: 80}
                    , {field: 'orderTime', title: '订单生成时间', width: 105}
                    , {field: 'operate', title: '操作', width: 147, toolbar: "#order-list-table-operate"}
                ]]
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
        }
    }
}