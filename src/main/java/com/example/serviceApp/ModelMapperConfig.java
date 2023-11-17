package com.example.serviceApp;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.customer.Dto.CustomerDto;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithCustomerEditorDto;
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
//        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());//todo chujstwo nie dziala
//        // Configure the modelMapper here
       // modelMapper.addMappings(new PropertyMap<ServiceRequest, ServiceRequestWithDetailsDto>() {
//            protected void configure() {
//                map().setCustomerId(source.getCustomer().getId());
//            }
    //    });

//        modelMapper.typeMap(ServiceRequest.class, ServiceRequestDto.class).addMappings(mapper ->
//                mapper.map(src -> src.getUser().getId(), ServiceRequestDto::setUserId)
//        );
      //  modelMapper.typeMap(ServiceRequestDto.class, ServiceRequest.class)
                //.addMappings(mapper-> mapper.skip(ServiceRequest::setUser))
               // .addMappings(mapper-> mapper.skip(ServiceRequest::setCustomer))
               // .addMappings(mapper -> mapper.skip(ServiceRequest::setId));

        modelMapper.typeMap(ServiceRequestWithCustomerEditorDto.class, ServiceRequest.class)
                .addMappings(mapper -> mapper.skip(ServiceRequest::setId));
        modelMapper.typeMap(CustomerDto.class, Customer.class)
                .addMappings(mapper -> mapper.skip(Customer::setId));
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}