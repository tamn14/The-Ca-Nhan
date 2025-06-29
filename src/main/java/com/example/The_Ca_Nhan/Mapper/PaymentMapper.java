package com.example.The_Ca_Nhan.Mapper;

import com.example.The_Ca_Nhan.DTO.Request.PaymentRequest;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.DTO.Response.PaymentResponse;
import com.example.The_Ca_Nhan.Entity.Orders;
import com.example.The_Ca_Nhan.Entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" )
public interface PaymentMapper {
    // mapping from Entity to DTO
    @Mapping(source = "method" ,target = "method" )
    @Mapping(source = "status" ,target = "status" )
    @Mapping(source = "payDate" ,target = "payDate" )
    PaymentResponse toDTO (Payment payment)  ;

    @Mapping(source = "method" ,target = "method" )
    @Mapping(source = "status" ,target = "status" )
    @Mapping(source = "payDate" ,target = "payDate" )
    Payment toEntity (PaymentRequest request)  ;
}
