package qeeka.jake.imagesteganography.web.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import qeeka.jake.imagesteganography.constants.AdminConstant;
import qeeka.jake.imagesteganography.domain.admin.AdminOperateEs;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.admin.Admin;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.repository.admin.AdminOperateEsRepository;
import qeeka.jake.imagesteganography.web.config.SystemUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@Component
@Aspect
public class TestAop {
    private static Admin admin;
    private static HttpServletRequest request;

    @Autowired
    private AdminOperateEsRepository adminOperateEsRepository;
    private Logger logger = LoggerFactory.getLogger(TestAop.class);
    /*
    * 指定切入点
    * */
    @Pointcut("execution(public * qeeka.jake.imagesteganography.web.controller.admin.AdminController.*(..))")
    public void webLog() {

    }

    /*
    * 前置通知
    * */
    @Before("webLog()")
    public void doBefore() {
        logger.info("前置通知");
    }

    /*
    * 处理完请求后返回的内容 记录管理员行为
    * */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        String methodName = joinPoint.getSignature().getName();//获取方法名
        recordAdminOperate(methodName, joinPoint, ret);

//        //获取目标方法的参数信息
//        Object[] obj = joinPoint.getArgs();
//        Signature signature = joinPoint.getSignature();
//        //代理的是哪一个方法
//        System.out.println("方法：" + signature.getName());
//        //AOP代理类的名字
//        System.out.println("方法所在包:" + signature.getDeclaringTypeName());
//        //AOP代理类的类（class）信息
//        signature.getDeclaringType();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        String[] strings = methodSignature.getParameterNames();
//        System.out.println("参数名："+ Arrays.toString(strings));
//
//        System.out.println("参数值ARGS : " + Arrays.toString(joinPoint.getArgs()));
//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest req = attributes.getRequest();
//        // 记录下请求内容
//        System.out.println("请求URL : " + req.getRequestURL().toString());
//        System.out.println("HTTP_METHOD : " + req.getMethod());
//        System.out.println("IP : " + req.getRemoteAddr());
//        System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//
//        System.out.println("返回值的内容" + ret);
    }

    private void recordAdminOperate(String methodName, JoinPoint joinPoint, Object ret) {
        switch (methodName) {
            case "login":
                BaseResponse response = (BaseResponse) ret;
                if ("SUCCESS".equals(response.getMsg())) { //登录成功
                    request = (HttpServletRequest) joinPoint.getArgs()[1];
                    admin = (Admin) request.getSession().getAttribute("admin");
                    AdminOperateEs adminOperateEs = new AdminOperateEs();
                    adminOperateEs.setAdminId(admin.getId());
                    adminOperateEs.setIp(request.getRemoteAddr());
                    adminOperateEs.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() + "登录了系统");
                    adminOperateEs.setOperateTime(SystemUtils.dateToFormat(new Date()));
                    adminOperateEs.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                    adminOperateEsRepository.save(adminOperateEs);
                }
                break;
            case "registerAdmin":
                break;
            case "getAllUserList":
                AdminOperateEs adminOperateEs = new AdminOperateEs();
                adminOperateEs.setAdminId(admin.getId());
                adminOperateEs.setIp(request.getRemoteAddr());
                adminOperateEs.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() + "查询了用户列表");
                adminOperateEs.setOperateTime(SystemUtils.dateToFormat(new Date()));
                adminOperateEs.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                adminOperateEsRepository.save(adminOperateEs);
                break;
            case "deleteUser":
                User user = (User) joinPoint.getArgs()[0];
                AdminOperateEs adminOperateEs1 = new AdminOperateEs();
                adminOperateEs1.setAdminId(admin.getId());
                adminOperateEs1.setIp(request.getRemoteAddr());
                adminOperateEs1.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() +
                        "删除了手机号为" + user.getMobile() + "的用户");
                adminOperateEs1.setOperateTime(SystemUtils.dateToFormat(new Date()));
                adminOperateEs1.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                adminOperateEsRepository.save(adminOperateEs1);
                break;
            case "showUser":
                break;
            case "updateUser":
                User user1 = (User) joinPoint.getArgs()[0];
                AdminOperateEs adminOperateEs2 = new AdminOperateEs();
                adminOperateEs2.setAdminId(admin.getId());
                adminOperateEs2.setIp(request.getRemoteAddr());
                adminOperateEs2.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() +
                        "修改了手机号为" + user1.getMobile() + "用户的基本资料");
                adminOperateEs2.setOperateTime(SystemUtils.dateToFormat(new Date()));
                adminOperateEs2.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                adminOperateEsRepository.save(adminOperateEs2);
                break;
            case "findUser":
                break;
            case "addUser":
                User user2 = (User) joinPoint.getArgs()[0];
                AdminOperateEs adminOperateEs3 = new AdminOperateEs();
                adminOperateEs3.setAdminId(admin.getId());
                adminOperateEs3.setIp(request.getRemoteAddr());
                adminOperateEs3.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() +
                        "增加了手机号为" + user2.getMobile() + "的用户");
                adminOperateEs3.setOperateTime(SystemUtils.dateToFormat(new Date()));
                adminOperateEs3.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                adminOperateEsRepository.save(adminOperateEs3);
                break;
            case "allAdminList":
                AdminOperateEs adminOperateEs4 = new AdminOperateEs();
                adminOperateEs4.setAdminId(admin.getId());
                adminOperateEs4.setIp(request.getRemoteAddr());
                adminOperateEs4.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() +
                        "查询了管理员列表");
                adminOperateEs4.setOperateTime(SystemUtils.dateToFormat(new Date()));
                adminOperateEs4.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                adminOperateEsRepository.save(adminOperateEs4);
                break;
            case "deleteAdmin":
                Admin admin1 = (Admin) joinPoint.getArgs()[0];
                AdminOperateEs adminOperateEs5 = new AdminOperateEs();
                adminOperateEs5.setAdminId(admin.getId());
                adminOperateEs5.setIp(request.getRemoteAddr());
                adminOperateEs5.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() +
                        "删除了手机号为" + admin1.getMobile() + "的管理员");
                adminOperateEs5.setOperateTime(SystemUtils.dateToFormat(new Date()));
                adminOperateEs5.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                adminOperateEsRepository.save(adminOperateEs5);
                break;
            case "showAdmin":
                break;
            case "updateAdmin":
                Admin admin2 = (Admin) joinPoint.getArgs()[0];
                AdminOperateEs adminOperateEs6 = new AdminOperateEs();
                adminOperateEs6.setAdminId(admin.getId());
                adminOperateEs6.setIp(request.getRemoteAddr());
                adminOperateEs6.setOperate("身份：" + admin.getRoleName() + ",用户名:" + admin.getUserName() +
                        "修改了手机号为" + admin2.getMobile() + "的管理员信息");
                adminOperateEs6.setOperateTime(SystemUtils.dateToFormat(new Date()));
                adminOperateEs6.setStatus(AdminConstant.ADMIN_STATUS_PASS);
                adminOperateEsRepository.save(adminOperateEs6);
                break;
            default:
                break;
        }
    }

}
