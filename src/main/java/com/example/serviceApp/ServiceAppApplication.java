package com.example.serviceApp;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

//@EnableWebMvc

@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
@EnableCaching
@SecurityScheme(
		name = "bearerAuth",
		scheme = "bearer",
		bearerFormat = "JWT",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER
)
public class ServiceAppApplication {
	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("public")
				.pathsToMatch("/customer/**", "/services/**","/auth/**","/device/**")
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceAppApplication.class, args);
	}


	@Bean
	public Cache<Long, List<String>> userCache(){
		return  CacheBuilder.newBuilder()
				.maximumSize(100)
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build();
	}
}
