package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersRequest {
    private String userName ;
    private String lastName ;
    private String firstName ;
    private String email ;
    private String address ;
}
