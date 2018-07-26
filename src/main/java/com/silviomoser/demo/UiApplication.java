package com.silviomoser.demo;


import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
@ComponentScan(basePackages = "com.silviomoser.demo")
public class UiApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}


	@Override
	public void run(String... strings) throws Exception {
	}

}
