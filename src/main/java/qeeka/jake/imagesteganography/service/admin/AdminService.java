package qeeka.jake.imagesteganography.service.admin;

import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.pojo.admin.Admin;
import qeeka.jake.imagesteganography.pojo.admin.AdminPrivilege;

import java.util.List;

public interface AdminService {
    Admin getAdmin(Admin admin);
    void saveAdmin(Admin admin);
    BaseResponse getAllAdmin();
    List<AdminPrivilege> getAllAdminPrivilege(String mobile);
}
