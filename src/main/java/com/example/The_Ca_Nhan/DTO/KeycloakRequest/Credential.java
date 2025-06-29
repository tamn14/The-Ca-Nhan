package com.example.The_Ca_Nhan.DTO.KeycloakRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Credential {
    String type ;
    String value ;
    boolean temporary ;
}
