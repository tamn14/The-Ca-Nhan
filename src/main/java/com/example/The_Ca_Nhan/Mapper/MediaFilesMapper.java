package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" )
public interface MediaFilesMapper {

    MediaFileResponse toDTO (MediaFiles mediaFiles)  ;

    @Mapping(target = "imageUrl", ignore = true) // ignore vì MultipartFile không ánh xạ trực tiếp sang entity
    MediaFiles toEntity (MediaFileCreateRequest request) ;
}
