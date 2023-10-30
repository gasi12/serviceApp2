package com.example.serviceApp;

import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithDetailsDto;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());//todo chujstwo nie dziala
//        // Configure the modelMapper here
        modelMapper.addMappings(new PropertyMap<ServiceRequest, ServiceRequestWithDetailsDto>() {
            protected void configure() {
                map().setCustomerId(source.getCustomer().getId());
            }
        });

        return modelMapper;
    }
}