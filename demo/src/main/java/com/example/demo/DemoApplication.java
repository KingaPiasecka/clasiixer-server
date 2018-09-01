package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/*@Controller*/
@SpringBootApplication
@EnableJpaAuditing
public class DemoApplication {

/*	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}*/

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
