package qeeka.jake.imagesteganography.domain.order;

import qeeka.jake.imagesteganography.constants.OrderConstant;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "user_order")
@Entity
public class UserOrderEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "order_number")
    private String orderNumber;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "orginal_image")
    private String orginalImage;
    @Column(name = "hidden_data")
    private String hiddenData;
    @Column(name = "payment_amount")
    private String paymentAmount;
    @Column(name = "result_image1")
    private String resultImage1;
    @Column(name = "result_image2")
    private String resultImage2;
    @Column(name = "download_status")
    private Integer downloadStatus = OrderConstant.DOWNLOAD_NO;
    @Column(name = "payment_status")
    private Integer paymentStatus = OrderConstant.PAYMENT_STATUS_NO;
    @Column(name = "order_status")
    private Integer orderStatus = OrderConstant.ORDER_STATUS_EXIT;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_time")
    private Date orderTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrginalImage() {
        return orginalImage;
    }

    public void setOrginalImage(String orginalImage) {
        this.orginalImage = orginalImage;
    }

    public String getHiddenData() {
        return hiddenData;
    }

    public void setHiddenData(String hiddenData) {
        this.hiddenData = hiddenData;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getResultImage1() {
        return resultImage1;
    }

    public void setResultImage1(String resultImage1) {
        this.resultImage1 = resultImage1;
    }

    public String getResultImage2() {
        return resultImage2;
    }

    public void setResultImage2(String resultImage2) {
        this.resultImage2 = resultImage2;
    }

    public Integer getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(Integer downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
