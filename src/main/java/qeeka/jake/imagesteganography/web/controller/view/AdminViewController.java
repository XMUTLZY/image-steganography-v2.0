package qeeka.jake.imagesteganography.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import qeeka.jake.imagesteganography.http.vo.admin.Admin;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/adminView")
public class AdminViewController {
    @RequestMapping("/login")
    public String adminLogin() {
        return "/admin/adminLogin";
    }
    @RequestMapping("/index")
    public String adminIndex(HttpServletRequest request, Model model) {
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        model.addAttribute("admin", admin);
        return "/admin/adminIndex";
    }
    @RequestMapping("/unPrivilege")
    public String unPrivilege() {
        return "/admin/UnPrivilege";
    }
}
