package com.departement.fichedevoeux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FichedevoeuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(FichedevoeuxApplication.class, args);
		
		 var encoder = new BCryptPasswordEncoder();
	        System.out.println( encoder.encode("123456") );  
	}

}
