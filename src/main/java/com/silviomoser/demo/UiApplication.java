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



	@Autowired
	UserRepository userRepository;

	//@Autowired
    //PasswordEncoder passwordEncoder;

	@Autowired
    CalendarEventRepository repository;

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/calendar")
	public List<CalendarEvent> home() {
		return repository.findAll();
	}

	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}



	private Date parseDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);
		LocalDate dateLocal = LocalDate.parse(dateString, formatter);
		Date date = Date.from(dateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}

	@Override
	public void run(String... strings) throws Exception {
		//repository.save(new CalendarEvent("Test Anlass", LocalDateTime.now()));
			//repository.save(new CalendarEvent("Test event 2", new Date()));

		PasswordEncoder encoder = new BCryptPasswordEncoder(11);
//		userRepository.save(new User("demo", encoder.encode("demo")));
	}


}
