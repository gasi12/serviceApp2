package com.example.serviceApp;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

//@EnableWebMvc

@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class ServiceAppApplication {


	public static void main(String[] args) {
		SpringApplication.run(ServiceAppApplication.class, args);
	}

	@Bean
	public ConcurrentHashMap<Long, List<String>> userOrdersCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Cache<Long, List<String>> userCache(){
		return  CacheBuilder.newBuilder()
				.maximumSize(100)
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build();
	}
}
