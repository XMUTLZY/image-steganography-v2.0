package qeeka.jake.imagesteganography.http.vo.order;

import java.util.Date;

public class Order {
    private Integer id;
    private Integer orderNumber;
    private Integer userId;
    private String orginalImage;
    private String hiddenData;
    private Double paymentAmount;
    private String resultImage1;
    private String resultImage2;
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
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

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
