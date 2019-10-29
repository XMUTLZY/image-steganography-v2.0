package qeeka.jake.imagesteganography.http.vo.order;

import qeeka.jake.imagesteganography.constants.OrderConstant;
import java.util.Date;

public class Order {
    private Integer id;
    private String orderNumber;
    private Integer userId;
    private String orginalImage;
    private String hiddenData;
    private String paymentAmount;
    private String resultImage1;
    private String resultImage2;
    private Integer downloadStatus = OrderConstant.DOWNLOAD_NO;
    private Integer paymentStatus = OrderConstant.PAYMENT_STATUS_NO;
    private Integer orderStatus = OrderConstant.ORDER_STATUS_EXIT;
    private Date date;
    private String downloadStatusString;
    private String paymentStatusString;
    private String orderTime;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDownloadStatusString() {
        return downloadStatusString;
    }

    public void setDownloadStatusString(String downloadStatusString) {
        this.downloadStatusString = downloadStatusString;
    }

    public String getPaymentStatusString() {
        return paymentStatusString;
    }

    public void setPaymentStatusString(String paymentStatusString) {
        this.paymentStatusString = paymentStatusString;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
