package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.ExperiencesRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;
import com.example.The_Ca_Nhan.Entity.Educations;
import com.example.The_Ca_Nhan.Entity.Experiences;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {
        UsersMapper.class,
        MediaFilesMapper.class
})
public interface ExperiencesMapper {
    // mapping from Entity to DTO
    @Mapping(source = "name" ,target = "name" )
    @Mapping(source = "position" ,target = "position" )
    @Mapping(source = "description" ,target = "description" )
    @Mapping(source = "startDate" ,target = "startDate" )
    @Mapping(source = "endDate" ,target = "endDate" )
    @Mapping(source = "users", target = "usersResponse")
    @Mapping(source = "mediaFiles", target = "mediaFiles")
    ExperiencesResponse toDTO (Experiences experiences, List<MediaFiles> mediaFiles)  ;


    // mapping from DTO to entity
    default Experiences toEntity (ExperiencesRequest request) {
        return Experiences.builder()
                .name(request.getName())
                .position(request.getPosition())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build() ;

    }
}
