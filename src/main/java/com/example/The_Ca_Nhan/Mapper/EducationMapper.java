package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.Entity.Educations;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {
        UsersMapper.class ,
        MediaFilesMapper.class
})
public interface EducationMapper {
    // mapping from Entity to  DTO
    @Mapping(source = "schoolName" ,target = "schoolName" )
    @Mapping(source = "degree" ,target = "degree" )
    @Mapping(source = "startDate" ,target = "startDate" )
    @Mapping(source = "endDate" ,target = "endDate" )
    @Mapping(source = "description" ,target = "description" )
    @Mapping(source = "users", target = "usersResponse")
    @Mapping(source = "mediaFiles", target = "mediaFiles")
    EducationResponse toDTO (Educations educations , List<MediaFiles> mediaFiles)  ;

    // mapping from DTO to Entity
    default Educations toEntity (EducationRequest request) {
       return Educations.builder()
               .schoolName(request.getSchoolName())
               .degree(request.getDegree())
               .startDate(request.getStartDate())
               .description(request.getDescription())
               .endDate(request.getEndDate())
               .build() ;
    } ;

}
