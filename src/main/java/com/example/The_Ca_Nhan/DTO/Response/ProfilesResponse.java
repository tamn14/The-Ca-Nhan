package com.example.The_Ca_Nhan.DTO.Response;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilesResponse {
    
    private String summary  ;
    private List<String> hobby ;
    private List<String> website ;
    private UsersResponse usersResponse ;
    private List<MediaFileResponse> mediaFiles;
}
