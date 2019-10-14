package qeeka.jake.imagesteganography.http.vo.admin;

import qeeka.jake.imagesteganography.constants.AdminConstant;
import java.io.Serializable;
import java.util.Date;

public class AdminOperate implements Serializable {
    private Integer id;
    private Integer adminId;
    private String ip;
    private String operate;
    private Date operateTime;
    private Integer status = AdminConstant.ADMIN_STATUS_PASS;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
