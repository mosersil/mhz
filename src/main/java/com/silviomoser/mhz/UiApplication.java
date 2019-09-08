package com.silviomoser.mhz;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UiApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}


	@Override
	public void run(String... strings) throws Exception {
	}


}
