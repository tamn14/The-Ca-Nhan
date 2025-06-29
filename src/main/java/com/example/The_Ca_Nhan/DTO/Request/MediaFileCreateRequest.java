package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFileCreateRequest {
    private MediaEntityType entityType;
    private int entityId;
    private String fileType;
    private String fileName;
    private String link;
}
