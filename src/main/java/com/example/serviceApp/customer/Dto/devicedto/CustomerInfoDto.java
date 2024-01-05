package com.example.serviceApp.customer.Dto.devicedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerInfoDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
}
