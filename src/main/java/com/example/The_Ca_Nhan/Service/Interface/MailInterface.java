package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.Entity.Orders;
import com.example.The_Ca_Nhan.Entity.Users;

public interface MailInterface {
    public void SendMessage(String from, String to  , byte[] qrBytes , Users passenger , Orders orders) ;
}
