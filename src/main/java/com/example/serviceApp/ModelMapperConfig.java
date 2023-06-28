package com.example.serviceApp;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.ServiceRequestWithUserNameDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configure the modelMapper here
        modelMapper.addMappings(new PropertyMap<ServiceRequest, ServiceRequestWithUserNameDto>() {
            @Override
            protected void configure() {
                map().setPhoneNumber(source.getCustomer().getPhoneNumber());
                map().setUserName(source.getCustomer().getUserName());
//                map().setStartDate(source.getStartDate());
            }
        });
        return modelMapper;
    }
}