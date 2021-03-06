package qeeka.jake.imagesteganography.web.controller.user;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.constants.UserConstant;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.service.user.UserService;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody User user, HttpServletRequest request) {
        if (userService.getUser(user) != null) {
            String encrypt = userService.getUser(user).getEncrypt();//获取盐
            String encodePassword = new SimpleHash(UserConstant.ENCRYPTION_TYPE, user.getPassword(), encrypt, UserConstant.ENCRYPTION_TIMES).toString();
            if (encodePassword.equals(userService.getUser(user).getPassword()) && userService.getUser(user).getStatus() == 1) {
                request.getSession().setAttribute("user", userService.getUser(user));
                return "true";
            }
        }
        return "false";
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    @ResponseBody
    public String getUser(@RequestBody User user) {
        if (userService.getUser(user) == null) {
            return "false";
        }
        return "true";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse registerUser(@RequestBody User user, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        if(request.getSession().getAttribute("code").equals(String.valueOf(user.getCode()))) {
            response = userService.saveUser(user);
            response.setMsg("SUCCESS");
            return response;
        }
        response.setMsg("FAILED");
        return response;
    }
}
