package qeeka.jake.imagesteganography.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import qeeka.jake.imagesteganography.http.vo.user.User;
import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/orderView")
@Controller
public class OrderViewController {
    @RequestMapping("/details")
    public String orderDetails() {
        return "/order/orderDetails";
    }

    @RequestMapping("/href")
    public String test(HttpServletRequest request, Model model) {
        String payIndex = (String) request.getSession().getAttribute("payIndex");
        model.addAttribute("payIndex", payIndex);
        return "/order/href";
    }

    @RequestMapping("/personalOrders")
    public String userIndex(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "/order/personalOrders";
    }
}
