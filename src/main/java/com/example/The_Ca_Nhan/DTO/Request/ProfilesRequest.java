package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilesRequest {

    private String summary  ;
    private List<String> hobby ;
    private List<String> website ;

    private List<MediaFileCreateRequest> mediaFiles;

}
