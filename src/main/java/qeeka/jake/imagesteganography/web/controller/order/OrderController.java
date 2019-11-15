package qeeka.jake.imagesteganography.web.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import qeeka.jake.imagesteganography.http.request.OrderDetailsRequest;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.service.order.OrderService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

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

    @RequestMapping(value = "/alipayNotifyNotice", method = RequestMethod.POST)
    @ResponseBody
    public String alipayNotifyNotice(HttpServletRequest request, HttpServletResponse response) {
        return "success";
    }

    @RequestMapping(value = "/alipayReturnNotice", method = RequestMethod.GET)
    public void alipayReturnNotice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(orderService.payResult(request));//重定向到主页
    }

    //判断该用户是否有已支付但未下载的订单
    @RequestMapping(value = "/isDownload", method = RequestMethod.POST)
    @ResponseBody
    public Boolean isDownLoad(@RequestBody User user, HttpServletRequest request) {
        user.setId(((User) request.getSession().getAttribute("user")).getId());
        BaseResponse response = orderService.isDownloadImage(user);
        if (response.getData().size() == 0 && response.getData().isEmpty()) {
            return false;
        }
        return true;
    }

    //下载图片 (主页)
    @RequestMapping(value = "/downloadImage", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse download(HttpServletRequest request, @RequestParam(value = "orderNumber", required = false) String orderNumber) {
        BaseResponse response = new BaseResponse();
        User user = new User();
        user.setId(((User) request.getSession().getAttribute("user")).getId());
        if (StringUtils.hasText(orderNumber)) {
            orderService.downloadImageByNumber(orderNumber);
        } else {
            response.setData(convertToString(orderService.isDownloadImage(user).getData()));
            orderService.updateDownloadStatus(user);//修改订单下载状态
        }
        return response;
    }

    @RequestMapping(value = "/personalOrders", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse personalOrders(@RequestParam("limit") Integer limit, @RequestParam("page") Integer page,
                                       @RequestParam("orderStatus") Integer orderStatus, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page-1, limit, Sort.Direction.ASC, "orderNumber");
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        order.setUserId(((User) request.getSession().getAttribute("user")).getId());
        return orderService.getPersonalOrders(order, pageable);
    }

    private List<String> convertToString(List<UserOrderEntity> userOrderEntityList) {
        List<String> list = new ArrayList<>();
        for (UserOrderEntity userOrderEntity : userOrderEntityList) {
            list.add(userOrderEntity.getResultImage1());
            list.add(userOrderEntity.getResultImage2());
        }
        return list;
    }
}
