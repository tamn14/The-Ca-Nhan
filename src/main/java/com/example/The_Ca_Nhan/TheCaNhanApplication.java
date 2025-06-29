package com.example.The_Ca_Nhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TheCaNhanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheCaNhanApplication.class, args);
	}

}
