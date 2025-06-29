package com.example.The_Ca_Nhan.DTO.Response;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExperiencesResponse {
    private String name ;
    private int position  ;
    private String description ;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private UsersResponse usersResponse ;
    private List<MediaFileResponse> mediaFiles ;
}
