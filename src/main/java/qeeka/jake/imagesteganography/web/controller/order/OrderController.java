package qeeka.jake.imagesteganography.web.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.service.order.Impl.OrderService;

import javax.servlet.http.HttpServletRequest;

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
}
