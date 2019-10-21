package qeeka.jake.imagesteganography.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qeeka.jake.imagesteganography.constants.AdminConstant;
import qeeka.jake.imagesteganography.constants.UserConstant;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.admin.Admin;
import qeeka.jake.imagesteganography.http.vo.admin.AdminOperate;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.service.admin.AdminService;
import qeeka.jake.imagesteganography.service.user.UserService;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;

    //登录逻辑 shiro管理
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse login(@RequestBody Admin admin, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();//获得当前subject 即用户信息
        BaseResponse response = new BaseResponse();
        Admin admin1 = adminService.getAdmin(admin);
        if (admin1 == null) {
            response.setMsg("账号不存在");
            return response;
        }
        String encrypt = admin1.getEncrypt();//获取盐
        String encodePassword = new SimpleHash(AdminConstant.ENCRYPTION_TYPE, admin.getPassword(), encrypt, AdminConstant.ENCRYPTION_TIMES).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(admin.getMobile(), encodePassword);//认证
        //执行登录
        try {
            subject.login(token);
            token.setRememberMe(true);
            request.getSession().setAttribute("admin", adminService.getAdmin(admin));//登录成功，在缓存中设置session
            response.setMsg("SUCCESS");
        } catch (IncorrectCredentialsException e) {
            response.setMsg("登录密码错误");
        } catch (ExcessiveAttemptsException e) {
            response.setMsg("登录失败次数过多");
        } catch (LockedAccountException e) {
            response.setMsg("账号已被锁定");
        } catch (DisabledAccountException e) {
            response.setMsg("账号已被禁用");
        } catch (ExpiredCredentialsException e) {
            response.setMsg("账号已过期");
        } catch (UnknownAccountException e) {
            response.setMsg("账号不存在");
        } catch (UnauthorizedException e) {
            response.setMsg("您没有得到相应的授权");
        } catch (Exception e) {
            response.setMsg("用户名或密码错误");
        }
        return response;
    }

    //注册
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

    //根据页码和当前页码数据条数 查询用户
    @RequestMapping(value = "/user/getAllUserList", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse getAllUserList(@RequestParam("limit") Integer limit, @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
        return userService.getUserList(pageable);
    }

    //删除指定用户
    @RequestMapping(value = "/user/deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public void deleteUser(@RequestBody User user) {
        User user1 = userService.getUser(user);
        if (user1 != null) {
            user1.setStatus(UserConstant.USER_STATUS_NOPASS);
            user1.setUpdateTime(new Date());
        }
        userService.saveUser(user1);
    }

    //获取指定用户
    @RequestMapping(value = "/user/showUser", method = RequestMethod.POST)
    @ResponseBody
    public String showUser(@RequestBody User user) {
        return JSONObject.toJSONString(userService.getUser(user));
    }

    //修改用户信息
    @RequestMapping(value = "/user/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateUser(@RequestBody User user) {
        BaseResponse response = new BaseResponse();
        userService.saveUser(setUser(user));
        response.setMsg("用户信息修改成功!");
        return response;
    }

    //模糊查询用户
    @RequestMapping(value = "/user/findUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse findUser(@RequestBody User user) {
        BaseResponse response = new BaseResponse();
        response.setMsg("success");
        List<User> userList = userService.findUser(user);
        if (userList != null) {
            response.setCount(userList.size());
        } else {
            response.setCount(0);
        }
        response.setData(userList);
        return response;
    }

    //增加用户
    @RequestMapping(value = "/user/addUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse addUser(@RequestBody User user) {
        BaseResponse response = userService.saveUser(user);
        response.setMsg("SUCCESS");
        return response;
    }

    //获取所有管理员
    @RequestMapping(value = "/allAdminList", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse allAdminList(@RequestParam("limit") Integer limit, @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
        return adminService.getAllAdmin(pageable);
    }

    //删除管理员
    @RequestMapping(value = "/deleteAdmin", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse deleteAdmin(@RequestBody Admin admin) {
        return adminService.deleteAdmin(admin);
    }

    //获取指定管理员
    @RequestMapping(value = "/showAdmin", method = RequestMethod.POST)
    @ResponseBody
    public String showAdmin(@RequestBody Admin admin) {
        return JSONObject.toJSONString(adminService.getAdmin(admin));
    }

    //修改管理员信息
    @RequestMapping(value = "/updateAdmin", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateAdmin(@RequestBody Admin admin) {
        return adminService.updateAdmin(admin);
    }

    //获取系统动态
    @RequestMapping(value = "/systemDynamic", method = RequestMethod.POST)
    @ResponseBody
    public List<AdminOperate> systemDynamic() {
        return adminService.getSystemDynamic();
    }

    //查询系统动态
    @RequestMapping(value = "/systemDynamic/search", method = RequestMethod.POST)
    @ResponseBody
    public List<AdminOperate> systemDynamicSearch(@RequestParam String key) {
        return adminService.searchSystemDynamic(key);
    }

    private User setUser(User user) {
        User user1 = userService.getUser(user);
        user1.setAccountName(user.getAccountName());
        user1.setRealName(user.getRealName());
        user1.setCity(user.getCity());
        user1.setEmail(user.getEmail());
        user1.setCompany(user.getCompany());
        user1.setCareer(user.getCareer());
        user1.setUpdateTime(new Date());
        return user1;
    }
}
