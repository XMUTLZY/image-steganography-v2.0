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
import qeeka.jake.imagesteganography.domain.admin.AdminOperateEs;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.admin.Admin;
import qeeka.jake.imagesteganography.repository.admin.AdminOperateEsRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@Component
@Aspect
public class TestAop {
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
                    AdminOperateEs adminOperateEs = new AdminOperateEs();
                    adminOperateEs.setAdminId(1);
                    adminOperateEs.setIp("test");
                    adminOperateEs.setOperate("test");
                    adminOperateEs.setOperateTime(new Date());
                    adminOperateEs.setStatus(1);
                    adminOperateEsRepository.save(adminOperateEs);
                } else {

                }
                break;
            case "registerAdmin":
                break;
            case "getAllUserList":
                break;
            case "deleteUser":
                break;
            case "showUser":
                break;
            case "updateUser":
                break;
            case "findUser":
                break;
            case "addUser":
                break;
            case "allAdminList":
                break;
            case "deleteAdmin":
                break;
            case "showAdmin":
                break;
            case "updateAdmin":
                break;
            default:
                break;
        }
    }
}
