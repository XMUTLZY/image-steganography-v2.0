package qeeka.jake.imagesteganography.service.admin.AdminServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import qeeka.jake.imagesteganography.domain.admin.AdminEntity;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.pojo.admin.Admin;
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
    public BaseResponse getAllAdmin() {
        BaseResponse response = new BaseResponse();
        List<Admin> list = convertToAdminList(adminRepository.findAll());
        response.setData(list);
        response.setCount(list.size());
        return response;
    }

    private List<Admin> convertToAdminList(List<AdminEntity> adminEntityList) {
        List<Admin> list = new ArrayList<>();
        if (adminEntityList == null || adminEntityList.isEmpty())
            return null;
        for (AdminEntity adminEntity : adminEntityList) {
            Admin admin = new Admin();
            BeanUtils.copyProperties(adminEntity, admin);
            String roleName = adminRoleRepository.findAllById(admin.getRoleId()).getName();
            if (StringUtils.hasText(roleName))
                admin.setRoleName(roleName);
            list.add(admin);
        }
        return list;
    }
}
