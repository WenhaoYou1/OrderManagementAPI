package com.meng.orderservice.dto;

import com.meng.orderservice.entity.OrderState;
import com.meng.orderservice.entity.PayState;
import lombok.*;


import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String userId;
    private OffsetDateTime dateCreate;
    private OffsetDateTime dateModify;
    private OrderState orderState;
    private Integer taxYear;
    private Double balance;
    private PayState paymentState;
    private OffsetDateTime datePay;

    private String admin;

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public OffsetDateTime getDateCreate() {
        return dateCreate;
    }
    public void setDateCreate(OffsetDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }
    public OffsetDateTime getDateModify() {
        return dateModify;
    }
    public void setDateModify(OffsetDateTime dateModify) {
        this.dateModify = dateModify;
    }
    public OrderState getOrderState() {
        return orderState;
    }
    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
    public Integer getTaxYear() {
        return taxYear;
    }
    public void setTaxYear(Integer taxYear) {
        this.taxYear = taxYear;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public PayState getPaymentState() {
        return paymentState;
    }
    public void setPaymentState(PayState paymentState) {
        this.paymentState = paymentState;
    }
    public OffsetDateTime getDatePay() {
        return datePay;
    }
    public void setDatePay(OffsetDateTime datePay) {
        this.datePay = datePay;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
