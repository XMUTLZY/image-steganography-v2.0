$(function () {
    orderDetails.bindEvent();
})
var orderDetails = {
    bindEvent: function () {
        orderDetails.event.getDateNow()
    },
    event: {
        getDateNow: function () {
            var vNow = new Date();
            var sNow = "";
            sNow += String(vNow.getFullYear());
            sNow += String(vNow.getMonth() + 1);
            sNow += String(vNow.getDate());
            sNow += String(vNow.getHours());
            sNow += String(vNow.getMinutes());
            sNow += String(vNow.getSeconds());
            sNow += String(vNow.getMilliseconds());
            $("#WIDout_trade_no").val(sNow);
            $("#WIDsubject").val("图像隐写在线服务平台资源下载支付");
            $("#WIDtotal_amount").val("5.00");
            $("#WIDbody").val("在您完成付款之后，平台会自动将生成的图片下载到您本地");
        }
    },
    method: {
        payment: function () {
            var data = {};
            data.out_trade_no = $("#WIDout_trade_no").val();
            data.total_amount = $("#WIDtotal_amount").val();
            data.subject = $("#WIDsubject").val();
            data.body = $("#WIDbody").val();
            $.ajax({
                url: '/order/pay',
                type: 'post',
                data: JSON.stringify(data),
                contentType: 'application/json',
                success: function (result) {
                    location.href = "/orderView/href";
                },
                error: function () {
                    layer.msg('数据异常')
                }
            })
        }
    }
}