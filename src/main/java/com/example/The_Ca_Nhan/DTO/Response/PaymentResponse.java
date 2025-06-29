package com.example.The_Ca_Nhan.DTO.Response;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private String method ;
    private String status ;
    private LocalDate payDate ;
}
