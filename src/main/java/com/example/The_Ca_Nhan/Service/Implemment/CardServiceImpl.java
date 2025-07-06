package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.Entity.Card;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.CardMapper;
import com.example.The_Ca_Nhan.Repository.CardRepository;
import com.example.The_Ca_Nhan.Service.Interface.CardInterface;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CardServiceImpl implements CardInterface {
    private final CardMapper cardMapper ;
    private final CardRepository cardRepository ;
    private final S3Interface s3Interface ;

    @Autowired
    public CardServiceImpl(CardMapper cardMapper, CardRepository cardRepository, S3Interface s3Interface) {
        this.cardMapper = cardMapper;
        this.cardRepository = cardRepository;
        this.s3Interface = s3Interface ;
    }

    private void checkImageIsNull(CardRequest request) {
        if(request.getImageUrl() == null || request.getImageUrl().isEmpty()) {
            throw new AppException(ErrorCode.FILE_IMAGE_EMPTY) ;
        }
    }
    private Card getCardById (int id ) {
        Card card = cardRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CARD_NOT_FOUND)) ;
        return card ;

    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public CardResponse insertCard(CardRequest request) {
        // kiem tra Image ton tai
        checkImageIsNull(request);
        // chuyen card ve entity
        Card card = cardMapper.toEntity(request) ;
        // luu Image vao AWS - nhan lai duong dan
        String imageUrl  = s3Interface.uploadFile(request.getImageUrl()) ;
        // set duong dan vao card
        card.setUrl(imageUrl);
        return cardMapper.toDTO(cardRepository.save(card)) ;
    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public CardResponse updateCard(CardRequest request, int cardId) {
        // get card
        Card card = getCardById(cardId) ;
        // check value name in request
        if(request.getName() != null) {
            card.setName(request.getName());
        }
        // check value description in request
        if(request.getDescription() != null) {
            card.setDescription(request.getDescription());
        }
        // check value price in request
        if(request.getPrice() != null) {
            card.setPrice(request.getPrice());
        }
        // check image
        if(request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            // delete old image
            s3Interface.deleteImage(card.getUrl());

            // insert new image
            String url = s3Interface.uploadFile(request.getImageUrl()) ;
            card.setUrl(url);
        }
        // save into db and return
        return cardMapper.toDTO(cardRepository.save(card)) ;

    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public void deleteCard(int cardId) {
        // check card is existed
        Card card = getCardById(cardId) ;
        // delete image in AWS
        s3Interface.deleteImage(card.getUrl());
        cardRepository.deleteById(cardId);

    }

    @Override
    public List<CardResponse> findAll() {
        return cardRepository.findAll().stream().map(cardMapper::toDTO).toList() ;
    }

    @Override
    public CardResponse findById(int id) {
        // check card is existed
        Card card = getCardById(id) ;
        return cardMapper.toDTO(card) ;
    }

    @Override
    public List<CardResponse> findByName(String name) {
        return cardRepository.findByName(name).stream().map(cardMapper::toDTO).toList() ;
    }
}
