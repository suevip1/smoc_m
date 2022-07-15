package com.smoc.cloud.iot.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "iot_product_cards")
public class IotProductCard {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "PRODUCT_ID", nullable = false, length = 32)
    private String productId;

    @Column(name = "CARD_MSISDN", nullable = false, length = 32)
    private String cardMsisdn;

    @Column(name = "CARD_IMSI", nullable = false, length = 32)
    private String cardImsi;

    @Column(name = "CARD_ICCID", nullable = false, length = 32)
    private String cardIccid;

    @Column(name = "STATUS", nullable = false, length = 32)
    private String status;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardIccid() {
        return cardIccid;
    }

    public void setCardIccid(String cardIccid) {
        this.cardIccid = cardIccid;
    }

    public String getCardImsi() {
        return cardImsi;
    }

    public void setCardImsi(String cardImsi) {
        this.cardImsi = cardImsi;
    }

    public String getCardMsisdn() {
        return cardMsisdn;
    }

    public void setCardMsisdn(String cardMsisdn) {
        this.cardMsisdn = cardMsisdn;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}