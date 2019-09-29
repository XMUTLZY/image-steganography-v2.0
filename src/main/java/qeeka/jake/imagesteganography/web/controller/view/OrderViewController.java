package qeeka.jake.imagesteganography.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/orderView")
@Controller
public class OrderViewController {
    @RequestMapping("/details")
    public String orderDetails() {
        return "/order/orderDetails";
    }
}
