package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" )
public interface MediaFilesMapper {
    @Mapping(source = "mediaId" ,target = "mediaId" )
    @Mapping(source = "entityType" ,target = "entityType" )
    @Mapping(source = "entityId" ,target = "entityId" )
    @Mapping(source = "fileType", target = "fileType")
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "link", target = "link")
    @Mapping(source = "updateDate", target = "updateDate")
    MediaFileResponse toDTO (MediaFiles mediaFiles)  ;


    @Mapping(source = "entityType" ,target = "entityType" )
    @Mapping(source = "entityId" ,target = "entityId" )
    @Mapping(source = "fileType", target = "fileType")
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "link", target = "link")
    MediaFiles toEntity (MediaFileCreateRequest request) ;
}
