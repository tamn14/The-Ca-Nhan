package com.example.The_Ca_Nhan.DTO.Response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationResponse {
    private String schoolName ;
    private String degree ;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private String description ;
    private UsersResponse usersResponse ;
    private List<MediaFileResponse> mediaFiles ;
}
