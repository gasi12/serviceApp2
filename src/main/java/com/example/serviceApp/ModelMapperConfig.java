package com.example.serviceApp;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.Dto.CustomerInfoDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithCustomerEditorDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithDetailsDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithUserNameDto;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(ServiceRequestWithCustomerEditorDto.class, ServiceRequest.class)
                .addMappings(mapper -> mapper.skip(ServiceRequest::setId));
        modelMapper.typeMap(ServiceRequest.class, ServiceRequestWithUserNameDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getCustomer().getFirstName(), ServiceRequestWithUserNameDto::setCustomerFirstName);
                    mapper.map(src -> src.getCustomer().getLastName(), ServiceRequestWithUserNameDto::setCustomerLastName);
                    mapper.map(src -> src.getCustomer().getPhoneNumber(), ServiceRequestWithUserNameDto::setCustomerPhoneNumber);
                });
        modelMapper.typeMap(ServiceRequest.class, ServiceRequestWithDetailsDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getCustomer().getFirstName(), ServiceRequestWithDetailsDto::setCustomerFirstName);
                    mapper.map(src -> src.getCustomer().getLastName(), ServiceRequestWithDetailsDto::setCustomerLastName);
                    mapper.map(src -> src.getCustomer().getPhoneNumber(), ServiceRequestWithDetailsDto::setCustomerPhoneNumber);
                    mapper.map(src -> src.getCustomer().getPhoneNumber(), ServiceRequestWithDetailsDto::setCustomerPhoneNumber);
                });

        modelMapper.typeMap(CustomerInfoDto.class, Customer.class)
                .addMappings(mapper -> mapper.skip(Customer::setId));
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}