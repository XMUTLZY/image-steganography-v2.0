package qeeka.jake.imagesteganography.domain.admin;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "image-steganography", type = "adminOperate", shards = 1, replicas = 0)
public class AdminOperateEs {
    @Id
    private Integer id;
    private Integer adminId;
    private String ip;
    private String operate;
    private String operateTime;
    private Integer status;

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
