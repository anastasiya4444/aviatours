package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Payment;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentDTO {
    private Integer id;
    private String paymentMethod;
    private String paymentStatus;
    private BigDecimal amount;
    private Instant createdAt;

    public PaymentDTO() {}

    public PaymentDTO(Integer id, String paymentMethod, String paymentStatus, BigDecimal amount, Instant createdAt) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Payment toEntity(){
        Payment payment = new Payment();
        payment.setId(id);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(paymentStatus);
        payment.setAmount(amount);
        payment.setCreatedAt(createdAt);
        return payment;
    }

    public static PaymentDTO fromEntity(Payment payment){
        PaymentDTO paymentDTO = new PaymentDTO();
        if(payment.getId() != null){
            paymentDTO.setId(payment.getId());
            paymentDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentDTO.setPaymentStatus(payment.getPaymentStatus());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setCreatedAt(payment.getCreatedAt());
        }
        return paymentDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}