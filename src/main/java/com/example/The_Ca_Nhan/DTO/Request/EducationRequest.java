package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationRequest {
    private String schoolName ;
    private String degree ;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private String description ;
    // them education cho user nao
    private int user ;
    // mediaFile cua education
    private List<MediaFileCreateRequest> mediaFiles ;
}
