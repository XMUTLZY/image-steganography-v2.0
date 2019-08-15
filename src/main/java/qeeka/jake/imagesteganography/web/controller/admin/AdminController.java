package qeeka.jake.imagesteganography.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.constants.AdminConstant;
import qeeka.jake.imagesteganography.constants.UserConstant;
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
    public BaseResponse login(@RequestBody Admin admin, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        if (adminService.getAdmin(admin) != null) {
            String encrypt = adminService.getAdmin(admin).getEncrypt();//获取盐
            String encodePassword = new SimpleHash(AdminConstant.ENCRYPTION_TYPE, admin.getPassword(), encrypt, AdminConstant.ENCRYPTION_TIMES).toString();
            if (encodePassword.equals(adminService.getAdmin(admin).getPassword()) && adminService.getAdmin(admin).getStatus() == AdminConstant.ADMIN_STATUS_PASS) {
                request.getSession().setAttribute("admin", adminService.getAdmin(admin));
                response.setMsg("success");
                return response;
            }
        }
        response.setMsg("failed");
        return response;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse registerAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        String encrypt = new SecureRandomNumberGenerator().nextBytes().toString();//生成盐
        String encodePassword = new SimpleHash(AdminConstant.ENCRYPTION_TYPE, admin.getPassword(), encrypt, AdminConstant.ENCRYPTION_TIMES).toString();
        admin.setPassword(encodePassword);
        admin.setEncrypt(encrypt);
        adminService.saveAdmin(admin);
        response.setMsg("注册成功");
        return response;
    }

    @RequestMapping(value = "/user/getAllUserList", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse getAllUserList(HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        List<User> list = userService.getUserList();
        response.setData(list);
        response.setCount(list.size());
        return response;
    }

    @RequestMapping(value = "/user/deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public void deleteUser(@RequestBody User user) {
        User user1 = userService.getUser(user);
        user1.setStatus(UserConstant.USER_STATUS_NOPASS);
        userService.saveUser(user1);
    }

    @RequestMapping(value = "/user/showUser", method = RequestMethod.POST)
    @ResponseBody
    public String showUser(@RequestBody User user) {
        return JSONObject.toJSONString(userService.getUser(user));
    }

    @RequestMapping(value = "/user/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateUser(@RequestBody User user) {
        BaseResponse response = new BaseResponse();
        userService.saveUser(setUser(user));
        response.setMsg("用户信息修改成功!");
        return response;
    }

    @RequestMapping(value = "/user/findUser", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse findUser(@RequestBody User user) {
        BaseResponse response = new BaseResponse();
        response.setMsg("success");
        response.setData(userService.findUser(user));
        return response;
    }

    private User setUser(User user) {
        User user1 = userService.getUser(user);
        user1.setAccountName(user.getAccountName());
        user1.setRealName(user.getRealName());
        user1.setCity(user.getCity());
        user1.setEmail(user.getEmail());
        user1.setCompany(user.getCompany());
        user1.setCareer(user.getCareer());
        return user1;
    }
}
