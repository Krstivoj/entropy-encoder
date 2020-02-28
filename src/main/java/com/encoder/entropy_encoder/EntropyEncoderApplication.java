package com.encoder.entropy_encoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EntropyEncoderApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path","/encoder_app");
		SpringApplication.run(EntropyEncoderApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		return builder.sources(EntropyEncoderApplication.class);
	}
}
