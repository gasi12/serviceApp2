package com.example.serviceApp;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithUserNameDto;
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
                modelMapper.typeMap(ServiceRequestWithUserNameDto.class, ServiceRequest.class)
                        .addMappings(mapper -> {
                            mapper.skip(ServiceRequest::setId);
                            mapper.skip(ServiceRequest::setStartDate);
                            mapper.skip(ServiceRequest::setEndDate);
                        });
                modelMapper.typeMap(ServiceRequestWithUserNameDto.class, Customer.class)
                        .addMappings(mapper -> mapper.skip(Customer::setId));

            }
        });
        return modelMapper;
    }
}