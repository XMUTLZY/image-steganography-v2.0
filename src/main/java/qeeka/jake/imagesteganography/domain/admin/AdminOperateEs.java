package qeeka.jake.imagesteganography.domain.admin;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.util.Date;

@Document(indexName = "image-steganography", type = "adminOperate", shards = 1, replicas = 0)
public class AdminOperateEs {
    @Id
    private Integer adminId;
    private String ip;
    private String operate;
    private Date operateTime;
    private Integer status;

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

    @Override
    public String toString() {
        return "AdminOperateEs{" +
                "adminId=" + adminId +
                ", ip='" + ip + '\'' +
                ", operate='" + operate + '\'' +
                ", operateTime=" + operateTime +
                ", status=" + status +
                '}';
    }
}
