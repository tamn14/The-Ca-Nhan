package com.example.The_Ca_Nhan.DTO.KeycloakRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationParam {
    String userName ;
    String enabled ;
    String email ;

    boolean emailVerified ;
    String firstName ;
    String lastName ;

    List<Credential> credentialList ;
}
