package com.example.flx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWebfluxApplication.class, args);
//		SpringApplication app = new SpringApplication(DemoWebfluxApplication.class);
//		app.setWebApplicationType(WebApplicationType.REACTIVE);
//		app.run(args);
	}

}
