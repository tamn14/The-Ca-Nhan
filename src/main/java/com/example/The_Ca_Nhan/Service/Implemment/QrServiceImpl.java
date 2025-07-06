package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.Properties.PaymentProperties;
import com.example.The_Ca_Nhan.Properties.VietQrTemplate;
import com.example.The_Ca_Nhan.Service.Interface.QrInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class QrServiceImpl implements QrInterface {

    @Override
    public byte[] generateQRCodeToFile(String json, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter() ;
        Map<EncodeHintType , Object> hintTypeObjectMap = new HashMap<>() ;
        hintTypeObjectMap.put(EncodeHintType.CHARACTER_SET , "UTF-8") ;

        BitMatrix bitMatrix = qrCodeWriter.encode(json, BarcodeFormat.QR_CODE, width, height, hintTypeObjectMap);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();

    }

    @Override
    public String buildVietQRUrl(String bankCode, String accountNumber, int amount, int orderId) {
        String baseUrl = "https://img.vietqr.io/image"; // Url vietQr
        String template = VietQrTemplate.COMPACT2.getValue();
        return String.format("%s/%s-%s-%s.png?amount=%d&addInfo=DH%s",
                baseUrl, bankCode, accountNumber, template, amount, orderId);
    }
}










