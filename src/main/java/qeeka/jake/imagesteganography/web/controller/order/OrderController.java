package qeeka.jake.imagesteganography.web.controller.order;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        return orderService.payment(request, httpServletRequest);
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

    @RequestMapping(value = "alipayReturnNotice", method = RequestMethod.GET)
    public void alipayReturnNotice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(orderService.payResult(request));//重定向到主页
    }
}
