package qeeka.jake.imagesteganography.http.vo.admin;

import qeeka.jake.imagesteganography.constants.AdminConstant;
import java.io.Serializable;
import java.util.Date;

public class AdminOperate implements Serializable {
    private Integer adminId;
    private String ip;
    private String operate;
    private String operateTime;
    private Integer status = AdminConstant.ADMIN_STATUS_PASS;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
