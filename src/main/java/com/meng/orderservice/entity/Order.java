package com.meng.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreate= OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime dateModify=OffsetDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Column(nullable = false)
    private Double balance = 0.0;

    @Column
    private OffsetDateTime datePay;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayState paymentState = PayState.UNPAID;

    @Column(nullable = false)
    private Integer taxYear;

    @Column
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

    public OffsetDateTime getDatePay() {
        return datePay;
    }

    public void setDatePay(OffsetDateTime datePay) {
        this.datePay = datePay;
    }

    public PayState getPaymentState() {
        return paymentState;
    }
    public void setPaymentState(PayState paymentState) {
        this.paymentState = paymentState;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
