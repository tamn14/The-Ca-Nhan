package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Response.MediaFileResponse;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.Entity.Card;
import com.example.The_Ca_Nhan.Entity.Orders;
import com.example.The_Ca_Nhan.Entity.Payment;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.OrdersMapper;
import com.example.The_Ca_Nhan.Mapper.PaymentMapper;
import com.example.The_Ca_Nhan.Properties.PaymentProperties;
import com.example.The_Ca_Nhan.Repository.CardRepository;
import com.example.The_Ca_Nhan.Repository.OrdersRepository;
import com.example.The_Ca_Nhan.Repository.PaymentRepository;
import com.example.The_Ca_Nhan.Service.Interface.MailInterface;
import com.example.The_Ca_Nhan.Service.Interface.OrdersInterface;
import com.example.The_Ca_Nhan.Service.Interface.PaymentInterface;
import com.example.The_Ca_Nhan.Service.Interface.QrInterface;
import com.example.The_Ca_Nhan.Util.Extract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersInterface {

    private final OrdersMapper ordersMapper ;
    private final OrdersRepository ordersRepository ;
    private final CardRepository cardRepository  ;
    private final Extract extract ;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final QrInterface qrInterface ;
    private final PaymentProperties paymentProperties ;

    @Value("${Qr.width}")
    private int widthQr ;
    @Value("${Qr.height}")

    private Card getCardById(int id ) {
        return cardRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CARD_NOT_FOUND)) ;
    }

    private Orders getOrdersById(int id ) {
        return ordersRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.ORDERS_NOT_FOUND)) ;
    }


    @Override
    public OrdersResponse insertOrders(OrdersRequest request) {
        // kiem tra card ton tai
        Card card = getCardById(request.getCardId()) ;
        // chuyen request thanh entity
        Orders orders = ordersMapper.toEntity(request) ;
        // lay user
        Users users = extract.getUserInFlowLogin();
        // chuyen payment ve entity
        Payment payment = paymentMapper.toEntity(request.getPaymentRequest()) ;
        // set quan he hai chieu
        orders.setPayment(payment);
        orders.setUsers(users);

        paymentRepository.save(payment) ;
        Orders ordOrdersInsert = ordersRepository.save(orders) ;

        return ordersMapper.toDTO(ordOrdersInsert) ;
    }

    @Override
    public void deleteOrders(int Id) {
        // kiem tra Orders ton tai
        Orders orders = getOrdersById(Id) ;
        // xoa quan he hai chieu
        orders.setPayment(null);
        orders.setUsers(null);
        ordersRepository.deleteById(Id);
    }

    @Override
    public List<OrdersResponse> findAll() {
        return ordersRepository.findAll().stream().map(ordersMapper::toDTO).toList();
    }

    @Override
    public OrdersResponse findById(int id) {
        // kiem tra Orders ton tai
        Orders orders = getOrdersById(id) ;
        return ordersMapper.toDTO(orders) ;
    }

    @Override
    public byte[] QrForPayment( int width, int height , int ordersId) {
        // kiem tra Orders ton tai
        Orders orders = getOrdersById(ordersId) ;
        // tao qrUrl
        String qrUrl = qrInterface.buildVietQRUrl(paymentProperties.getBankCode(),
                paymentProperties.getBankAccount(), orders.getTotalAmount() , ordersId) ;

        try {
            return qrInterface.generateQRCodeToFile(qrUrl ,width, height );
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_CREATE_QR);
        }

    }
}
