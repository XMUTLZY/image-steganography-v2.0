package qeeka.jake.imagesteganography.web.controller.admin;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.pojo.admin.Admin;
import qeeka.jake.imagesteganography.service.admin.AdminService;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    /*
    * 登录
    * */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody Admin admin, HttpServletRequest request) {
        if (adminService.getAdmin(admin) != null) {
            //String encodePassword = new Md5Hash(admin.getPassword()).toString();
            if (admin.getPassword().equals(adminService.getAdmin(admin).getPassword()) && adminService.getAdmin(admin).getStatus() == 1) {
                request.getSession().setAttribute("admin", adminService.getAdmin(admin));
                return "true";
            }
        }
        return "false";
    }

    /*
     * 注册
     * */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String registerAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        String encodePassword = new Md5Hash(admin.getPassword()).toString();
        admin.setPassword(encodePassword);
        adminService.saveAdmin(admin);
        return "true";
    }
}
