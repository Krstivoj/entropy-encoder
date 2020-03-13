package com.encoder.entropy_encoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EntropyEncoderApplication {
	@GetMapping("/hello")
	public String greeting(){
		return "Hello from server";
	}
	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path","/encoder_app");
		SpringApplication.run(EntropyEncoderApplication.class, args);
	}
}
