package qeeka.jake.imagesteganography.domain.order;


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
    private Integer orderNumber;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "orginal_image")
    private String orginalImage;
    @Column(name = "hidden_data")
    private String hiddenData;
    @Column(name = "payment_amount")
    private Double paymentAmount;
    @Column(name = "result_image1")
    private String resultImage1;
    @Column(name = "result_image2")
    private String resultImage2;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_time")
    private Date orderTime;

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

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
