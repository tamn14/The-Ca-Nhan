package com.example.The_Ca_Nhan.DTO.KeycloakResponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // mapping lai theo Json ma keycloak tra ve
public class UserTokenExchangeResponse {
    String accessToken;
    String refreshToken;
    String expiresIn;
    String refreshExpiresIn;
    String tokenType;
    String idToken;
    String scope;
}
