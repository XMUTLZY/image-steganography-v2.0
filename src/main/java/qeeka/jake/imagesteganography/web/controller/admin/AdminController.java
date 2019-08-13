package qeeka.jake.imagesteganography.web.controller.admin;

import com.alibaba.fastjson.JSON;
import jdk.internal.dynalink.linker.LinkerServices;
import net.sf.json.JSONObject;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.constants.AdminConstant;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.pojo.admin.Admin;
import qeeka.jake.imagesteganography.pojo.user.User;
import qeeka.jake.imagesteganography.service.admin.AdminService;
import qeeka.jake.imagesteganography.service.user.UserService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody Admin admin, HttpServletRequest request) {
        if (adminService.getAdmin(admin) != null) {
            String encrypt = adminService.getAdmin(admin).getEncrypt();//获取盐
            String encodePassword = new SimpleHash(AdminConstant.ENCRYPTION_TYPE, admin.getPassword(), encrypt, AdminConstant.ENCRYPTION_TIMES).toString();
            if (encodePassword.equals(adminService.getAdmin(admin).getPassword()) && adminService.getAdmin(admin).getStatus() == 1) {
                request.getSession().setAttribute("admin", adminService.getAdmin(admin));
                return "true";
            }
        }
        return "false";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String registerAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        String encrypt = new SecureRandomNumberGenerator().nextBytes().toString();//生成盐
        String encodePassword = new SimpleHash(AdminConstant.ENCRYPTION_TYPE, admin.getPassword(), encrypt, AdminConstant.ENCRYPTION_TIMES).toString();
        admin.setPassword(encodePassword);
        admin.setEncrypt(encrypt);
        adminService.saveAdmin(admin);
        return "true";
    }

    @RequestMapping(value = "/user/getAllUserList")
    @ResponseBody
    public BaseResponse getAllUserList(HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        List<User> list = userService.getUserList();
        response.setData(list);
        response.setCount(list.size());
        return response;
    }
}
