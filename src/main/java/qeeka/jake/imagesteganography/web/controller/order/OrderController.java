package qeeka.jake.imagesteganography.web.controller.order;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.constants.AlipayConstant;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import qeeka.jake.imagesteganography.http.request.OrderDetailsRequest;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.service.order.OrderService;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

    //判断该用户是否有已支付但未下载的订单
    @RequestMapping(value = "isDownLoad", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse isDownLoad(@RequestBody User user) {
        return orderService.isDownloadImage(user);
    }

    @RequestMapping(value = "downloadImage", method = RequestMethod.POST)
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserOrderEntity userOrderEntity = (UserOrderEntity)request.getSession().getAttribute("order");
        URL imageUrl = new URL(userOrderEntity.getResultImage1());
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        byte[] bs = new byte[1024];
        int len = 0;
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/jpeg;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" +
                new String("resultImageOne.bmp".getBytes("UTF-8"), "ISO8859-1") + "\"");
        ServletOutputStream outputStream = response.getOutputStream();
        while ((len = inputStream.read(bs)) != -1) {
            outputStream.write(bs, 0, len);
        }
    }
}
