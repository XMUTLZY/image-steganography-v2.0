package qeeka.jake.imagesteganography.service.admin.AdminServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.domain.admin.AdminEntity;
import qeeka.jake.imagesteganography.pojo.admin.Admin;
import qeeka.jake.imagesteganography.repository.admin.AdminRepository;
import qeeka.jake.imagesteganography.service.admin.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin getAdmin(Admin admin) {
        if (adminRepository.findByMobile(admin.getMobile()) != null) {
            Admin admin1 = new Admin();
            BeanUtils.copyProperties(adminRepository.findByMobile(admin.getMobile()), admin1);
            return admin1;
        }
        return null;
    }

    @Override
    public void saveAdmin(Admin admin) {
        AdminEntity entity = new AdminEntity();
        BeanUtils.copyProperties(admin, entity);
        adminRepository.save(entity);
    }
}
