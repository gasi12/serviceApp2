package com.example.serviceApp;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.Dto.devicedto.CustomerCreationDto2;
import com.example.serviceApp.customer.Dto.devicedto.CustomerInfoDto;

import com.example.serviceApp.device.Device;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.newDtos.ServiceRequestDtoAll;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(ServiceRequest.class, ServiceRequestDtoAll.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getDevice().getCustomer(), ServiceRequestDtoAll::setCustomer);
                });


//        modelMapper.typeMap(ServiceRequest.class, ServiceRequestWithUserNameDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(src -> src.getCustomer().getFirstName(), ServiceRequestWithUserNameDto::setCustomerFirstName);
//                    mapper.map(src -> src.getCustomer().getLastName(), ServiceRequestWithUserNameDto::setCustomerLastName);
//                    mapper.map(src -> src.getCustomer().getPhoneNumber(), ServiceRequestWithUserNameDto::setCustomerPhoneNumber);
//                });
//        modelMapper.typeMap(ServiceRequest.class, ServiceRequestWithDetailsDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(src -> src.getCustomer().getFirstName(), ServiceRequestWithDetailsDto::setCustomerFirstName);
//                    mapper.map(src -> src.getCustomer().getLastName(), ServiceRequestWithDetailsDto::setCustomerLastName);
//                    mapper.map(src -> src.getCustomer().getPhoneNumber(), ServiceRequestWithDetailsDto::setCustomerPhoneNumber);
//                    mapper.map(src -> src.getCustomer().getPhoneNumber(), ServiceRequestWithDetailsDto::setCustomerPhoneNumber);
//                });

        modelMapper.typeMap(CustomerInfoDto.class, Customer.class)
                .addMappings(mapper -> mapper.skip(Customer::setId));
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}