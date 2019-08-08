package qeeka.jake.imagesteganography.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminView")
public class AdminViewController {
    @RequestMapping("/login")
    public String adminLogin() {
        return "/admin/adminLogin.html";
    }
    @RequestMapping("/index")
    public String adminIndex() {
        return "/admin/adminIndex.html";
    }
}
