package com.example.The_Ca_Nhan.DTO.Response;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponse {
    private String userName ;
    private String lastName ;
    private String firstName ;
    private String email ;
    private String address ;
    private Date createAt ;
    private boolean enable;
    private String url ;
    private boolean isPayment ;
}
