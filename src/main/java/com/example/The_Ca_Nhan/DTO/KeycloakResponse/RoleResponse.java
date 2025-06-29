package com.example.The_Ca_Nhan.DTO.KeycloakResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
     String id;
     String name;
     String description;
     boolean composite;
     String containerId;
}
