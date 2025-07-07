package com.meng.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class OrderWNicknameDTO extends OrderDTO {
    private String nickname;

    public OrderWNicknameDTO(OrderDTO dto) {
        this.setOrderId(dto.getOrderId());
        this.setUserId(dto.getUserId());
        this.setDateCreate(dto.getDateCreate());
        this.setDateModify(dto.getDateModify());
        this.setOrderState(dto.getOrderState());
        this.setTaxYear(dto.getTaxYear());
        this.setBalance(dto.getBalance());
        this.setPaymentState(dto.getPaymentState());
        this.setDatePay(dto.getDatePay());
        this.setAdmin(dto.getAdmin());
        this.nickname = null;
    }

    public OrderWNicknameDTO(OrderDTO dto, String nickname) {
        this.setOrderId(dto.getOrderId());
        this.setUserId(dto.getUserId());
        this.setDateCreate(dto.getDateCreate());
        this.setDateModify(dto.getDateModify());
        this.setOrderState(dto.getOrderState());
        this.setTaxYear(dto.getTaxYear());
        this.setBalance(dto.getBalance());
        this.setPaymentState(dto.getPaymentState());
        this.setDatePay(dto.getDatePay());
        this.setAdmin(dto.getAdmin());
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
