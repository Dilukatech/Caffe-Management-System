package com.example.Cafe.Management.System;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CafeManagementSystemApplication {

//	@Autowired
//	EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(CafeManagementSystemApplication.class, args);

	}
//	@EventListener(ApplicationReadyEvent.class)
//	public void sendMail(){
//		senderService.sendEmail("dilukadeshan69@gmail.com",
//				"subject",
//				"this is body");
//	}


}
