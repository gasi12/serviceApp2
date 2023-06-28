package com.example.serviceApp.Customer;


import java.util.List;
import java.util.stream.Collectors;

public class CustomerDtoMapper {
    public static List<CustomerDto> mapToCustomersDtos(List<Customer> customers) {
        return customers.stream().map(CustomerDtoMapper::mapToCustomerDto).
                collect(Collectors.toList());
    }

    public static CustomerDto mapToCustomerDto(Customer user) {
        return CustomerDto.builder()
                .id(user.getId())
                .name(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
