package qeeka.jake.imagesteganography.web.controller.order;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.constants.AlipayConstant;
import qeeka.jake.imagesteganography.http.request.OrderDetailsRequest;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.service.order.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/order")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/generateImage", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse generateImage(@RequestBody Order order, HttpServletRequest request) {
        order.setUserId(((User)request.getSession().getAttribute("user")).getId());
        return orderService.generateImage(order);
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public String payment(@RequestBody OrderDetailsRequest request, HttpServletRequest httpServletRequest) {
        String result = null;
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConstant.GATEWAR_URL, AlipayConstant.APP_ID, AlipayConstant.MECHART_PRIVATE_KEY,
                "json", AlipayConstant.CHARSET, AlipayConstant.APLPAY_PUBLIC_KEY, AlipayConstant.SIGN_TYPE);
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        alipayTradePagePayRequest.setReturnUrl(AlipayConstant.RETURN_URL);
        alipayTradePagePayRequest.setNotifyUrl(AlipayConstant.NOTIFY_URL);
        alipayTradePagePayRequest.setBizContent("{\"out_trade_no\":\""+ request.getOut_trade_no() +"\","
                + "\"total_amount\":\""+ request.getTotal_amount() +"\","
                + "\"subject\":\""+ request.getSubject() +"\","
                + "\"body\":\""+ request.getBody() +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        try {
            result = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpServletRequest.getSession().setAttribute("payIndex", result);
        return result;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse test() {
        Order order = new Order();
        return orderService.generateImage(order);
    }

    @RequestMapping(value = "alipayNotifyNotice", method = RequestMethod.POST)
    @ResponseBody
    public String alipayNotifyNotice(HttpServletRequest request, HttpServletResponse response) {
        return "success";
    }

    @RequestMapping(value = "alipayReturnNotice", method = RequestMethod.POST)
    @ResponseBody
    public String alipayReturnNotice(HttpServletRequest request, HttpServletResponse response) {
        return "success";
    }
}
