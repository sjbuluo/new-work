package com.sun.health.newwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = {"com.sun.health.newwork.zookeeper"})
@SpringBootApplication
public class NewWorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewWorkApplication.class, args);
	}

}
