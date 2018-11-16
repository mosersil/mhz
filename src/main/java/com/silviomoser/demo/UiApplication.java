package com.silviomoser.demo;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.silviomoser.demo")
public class UiApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}


	@Override
	public void run(String... strings) throws Exception {
	}


}
