package com.example.The_Ca_Nhan.DTO.Request;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFileUpdateRequest {
    private int mediaId; // để biết file nào đang sửa
    private String fileType;
    private String fileName;
    private String link;
    private LocalDate updateDate;
}
