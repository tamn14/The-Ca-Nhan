package com.example.The_Ca_Nhan.DTO.Response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {
    private String title  ;
    private String description ;
    private UsersResponse usersResponse ;
    private List<MediaFileResponse> mediaFiles ;
}
