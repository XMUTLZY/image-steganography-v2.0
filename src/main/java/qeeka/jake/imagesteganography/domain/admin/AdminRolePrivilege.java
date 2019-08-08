package qeeka.jake.imagesteganography.domain.admin;

import javax.persistence.Column;
import java.io.Serializable;

public class AdminRolePrivilege implements Serializable {
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "privilege_id")
    private Integer privilegeId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }
}
