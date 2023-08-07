package com.example.serviceApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.core.jackson.ModelResolver;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableWebMvc
@SpringBootApplication
public class ServiceAppApplication {

//	@Bean
//	public ModelMapper modelMapper() {
//		return new ModelMapper();
//
//	}
@Bean
public ObjectMapper objectMapper() {
	ObjectMapper objectMapper = new ObjectMapper();
	objectMapper.registerModule(new JavaTimeModule());
//	objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
	return objectMapper;
}
//	@Bean
//	public ModelResolver modelResolver(ObjectMapper objectMapper) {
//
//		return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE));
//	}
	public static void main(String[] args) {
		SpringApplication.run(ServiceAppApplication.class, args);
	}

}
