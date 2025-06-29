package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRequest {
    private String name ;
    private String description ;
    private Integer price  ;
}
