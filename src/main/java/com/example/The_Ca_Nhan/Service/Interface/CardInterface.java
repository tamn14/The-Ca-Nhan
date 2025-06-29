package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;

import java.util.List;

public interface CardInterface {
    public CardResponse insertCard(CardRequest request) ;
    public CardResponse updateCard(CardRequest request , int cardId) ;
    public void deleteCard(int cardId);
    public List<CardResponse> findAll() ;
    public CardResponse findById(int id) ;
    public List<CardResponse> findByName( String name) ;

}
