package qeeka.jake.imagesteganography.web.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import qeeka.jake.imagesteganography.domain.admin.AdminPrivilegeEntity;
import qeeka.jake.imagesteganography.repository.admin.AdminPrivilegeRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    private AdminPrivilegeRepository adminPrivilegeRepository;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置登录的url
        shiroFilterFactoryBean.setLoginUrl("/adminView/login");
        //设置登录成功跳转的url
        shiroFilterFactoryBean.setSuccessUrl("/adminView/index");
        //未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/adminView/unPrivilege");
        //拦截器
        Map<String, String> filterMap = new LinkedHashMap<>();
//        List<AdminPrivilegeEntity> adminPrivilegeEntityList = adminPrivilegeRepository.findAll();//动态设置所有权限
//        for (AdminPrivilegeEntity adminPrivilegeEntity : adminPrivilegeEntityList) {
//            filterMap.put(adminPrivilegeEntity.getPrivilegeUrl(), adminPrivilegeEntity.getPrivilege());
//        }
//        //设置值不拦截url
//        filterMap.put("/**","authc");
        filterMap.put("/admin/user/updateUser", "perms[编辑用户]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    //注入SecurityManager
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /*
    * 自定义身份认证realm
    * 必须加上这个类，并加上@Bean注解，目的是注入CustomRealm
    * 否则会影响CustomRealm类中其他类的依赖注入
    * */
    @Bean
    public MyShiroRealm myShiroRealm() {
        return new MyShiroRealm();
    }
}
