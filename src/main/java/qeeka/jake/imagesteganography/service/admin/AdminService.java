package qeeka.jake.imagesteganography.service.admin;

import qeeka.jake.imagesteganography.pojo.admin.Admin;

public interface AdminService {
    Admin getAdmin(Admin admin);
    void saveAdmin(Admin admin);
}
