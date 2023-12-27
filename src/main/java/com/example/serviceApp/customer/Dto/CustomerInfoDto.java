package com.example.serviceApp.customer.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerInfoDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
}
