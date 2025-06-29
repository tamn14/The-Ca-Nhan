package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersRequest {
    private String orderType ;
    private int totalAmount ;
    private String status ;
    private String address ;
    private LocalDate ordersDate ;

    private PaymentRequest paymentRequest ;
    private int userId ;
    private int cardId ;




}
