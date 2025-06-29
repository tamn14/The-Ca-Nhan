package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.UsersRequest;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface UsersMapper {
    @Mapping(source = "userName" ,target = "userName" )
    @Mapping(source = "lastName" ,target = "lastName" )
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "email" ,target = "email" )
    @Mapping(source = "address" ,target = "address" )
    @Mapping(source = "createAt", target = "createAt")
    @Mapping(source = "enable" ,target = "enable" )
    @Mapping(source = "url", target = "url")
    @Mapping(source = "isPayment", target = "isPayment")
    UsersResponse toDTO (Users users)  ;

    @Mapping(source = "userName" ,target = "userName" )
    @Mapping(source = "lastName" ,target = "lastName" )
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "email" ,target = "email" )
    @Mapping(source = "address" ,target = "address" )
    Users toEntity (UsersRequest request)  ;


}
