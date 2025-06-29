package com.example.The_Ca_Nhan.Service.Interface;



import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.MediaFileUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;

import java.util.List;

public interface OrdersInterface {
    public OrdersResponse insertOrders(OrdersRequest request) ;
    public void deleteOrders(int Id);
    public List<MediaFileResponse> findAll() ;
    public MediaFileResponse findById(int id) ;

}
