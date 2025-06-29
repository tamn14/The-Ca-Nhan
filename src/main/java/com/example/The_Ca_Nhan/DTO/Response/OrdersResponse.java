package com.example.The_Ca_Nhan.DTO.Response;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersResponse {
    private String orderType ;
    private int totalAmount ;
    private String status ;
    private String address ;
    private LocalDate ordersDate ;

    private PaymentResponse paymentResponse ;
    private UsersResponse usersResponse ;
    private CardResponse cardResponse ;





}
