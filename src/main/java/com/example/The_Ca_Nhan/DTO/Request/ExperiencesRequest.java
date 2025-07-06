package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExperiencesRequest {
    private String name ;
    private Integer position  ;
    private String description ;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private List<MediaFileCreateRequest> mediaFiles ;
}
