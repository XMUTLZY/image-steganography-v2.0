package qeeka.jake.imagesteganography.web.config;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import qeeka.jake.imagesteganography.constants.AdminConstant;
import qeeka.jake.imagesteganography.pojo.admin.Admin;
import qeeka.jake.imagesteganography.pojo.admin.AdminPrivilege;
import qeeka.jake.imagesteganography.service.admin.AdminService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    AdminService adminService;
    //获取权限信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取管理员
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取该管理员角色
        Set<String> setRole = new HashSet<>();
        setRole.add(admin.getRoleName());
        info.setRoles(setRole);
        //获取该管理员权限 url
        List<AdminPrivilege> adminPrivilegeList = JSONObject.parseArray(adminService.getAllAdminPrivilege(admin.getMobile()).getData().toString(), AdminPrivilege.class);
        List<String> perUrlList = new ArrayList<>();
        for (AdminPrivilege adminPrivilege : adminPrivilegeList) {
            perUrlList.add(adminPrivilege.getPrivilegeUrl());
        }

        Set<String> setPerUrl = new HashSet<>();
        for (String  s : perUrlList) {
            setPerUrl.add(s);
        }
        info.setStringPermissions(setPerUrl);
        return info;
    }

    //验证管理员身份
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //从数据库获取该管理员信息
        Admin admin = new Admin();
        admin.setMobile(token.getUsername());
        Admin admin1 = adminService.getAdmin(admin);
        String password = new String((char[]) token.getCredentials());
        if (!password.equals(admin1.getPassword())) {
            throw new AccountException("查询不到该管理员");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }
}
