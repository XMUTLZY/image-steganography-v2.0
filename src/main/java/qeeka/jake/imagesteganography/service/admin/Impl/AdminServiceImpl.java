package qeeka.jake.imagesteganography.service.admin.Impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.domain.admin.AdminEntity;
import qeeka.jake.imagesteganography.domain.admin.AdminPrivilegeEntity;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.admin.Admin;
import qeeka.jake.imagesteganography.http.vo.admin.AdminPrivilege;
import qeeka.jake.imagesteganography.repository.admin.AdminPrivilegeRepository;
import qeeka.jake.imagesteganography.repository.admin.AdminPrivilegeRoleRepository;
import qeeka.jake.imagesteganography.repository.admin.AdminRepository;
import qeeka.jake.imagesteganography.repository.admin.AdminRoleRepository;
import qeeka.jake.imagesteganography.service.admin.AdminService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminRoleRepository adminRoleRepository;
    @Autowired
    private AdminPrivilegeRoleRepository adminPrivilegeRoleRepository;
    @Autowired
    private AdminPrivilegeRepository adminPrivilegeRepository;

    @Override
    public Admin getAdmin(Admin admin) {
        if (adminRepository.findByMobile(admin.getMobile()) == null)
            return null;
        Admin admin1 = new Admin();
        BeanUtils.copyProperties(adminRepository.findByMobile(admin.getMobile()), admin1);
        return admin1;
    }

    @Override
    public void saveAdmin(Admin admin) {
        AdminEntity entity = new AdminEntity();
        BeanUtils.copyProperties(admin, entity);
        adminRepository.save(entity);
    }

    @Override
    public BaseResponse getAllAdmin(Pageable pageable) {
        BaseResponse response = new BaseResponse();
        Page<AdminEntity> page = adminRepository.findAll(pageable);
        List<Admin> adminList = new ArrayList<>();
        if (page == null && !page.isEmpty())
            return null;
        for (AdminEntity adminEntity : page) {
            Admin admin = new Admin();
            BeanUtils.copyProperties(adminEntity, admin);
            String roleName = adminRoleRepository.findAllById(adminEntity.getRoleId()).getName();
            admin.setRoleName(roleName);
            adminList.add(admin);
        }
        List<Admin> list = convertToAdminList(adminRepository.findAll());//获取全部管理员
        response.setData(adminList);
        response.setCount(list.size());
        return response;
    }

    @Override
    public List<AdminPrivilege> getAllAdminPrivilege(String mobile) {
        AdminEntity adminEntity = adminRepository.findByMobile(mobile);
        List<Integer> roleIdList = adminPrivilegeRoleRepository.findPrivilegeIdList(adminEntity.getRoleId());
        List<AdminPrivilege> entityList = convertToAdminPrivilegeList(adminPrivilegeRepository.getPrivilegeList(roleIdList));
        return entityList;
    }

    private List<Admin> convertToAdminList(List<AdminEntity> adminEntityList) {
        List<Admin> list = new ArrayList<>();
        if (adminEntityList == null || adminEntityList.isEmpty())
            return null;
        for (AdminEntity adminEntity : adminEntityList) {
            Admin admin = new Admin();
            BeanUtils.copyProperties(adminEntity, admin);
            list.add(admin);
        }
        return list;
    }

    private List<AdminPrivilege> convertToAdminPrivilegeList(List<AdminPrivilegeEntity> entityList) {
        List<AdminPrivilege> adminPrivilegeList = new ArrayList<>();
        if (entityList == null || entityList.isEmpty())
            return null;
        for (AdminPrivilegeEntity adminPrivilegeEntity : entityList ) {
            AdminPrivilege adminPrivilege = new AdminPrivilege();
            BeanUtils.copyProperties(adminPrivilegeEntity, adminPrivilege);
            adminPrivilegeList.add(adminPrivilege);
        }
        return adminPrivilegeList;
    }
}
