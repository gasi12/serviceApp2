package com.example.serviceApp.customer.Dto.devicedto;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.device.Device;
import com.example.serviceApp.device.dto.DeviceDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link Customer}
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCreationDto2 implements Serializable {
    private CustomerCreationDto customer;
    private DeviceCreationDto device;
    private ServiceRequestCreationDto serviceRequest;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerCreationDto implements Serializable{
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Long id;
        @NotNull(message = "First name is required.")
        private String firstName;
        @NotNull(message = "Last name is required.")
        private String lastName;
        @NotNull(message = "Phone number is required.")
        private Long phoneNumber;
    }

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeviceCreationDto implements Serializable {
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Long id;
        @NotNull(message = "Device name is required.")
        private String deviceName;
        @NotNull(message = "Device serial number is required.")
        private String deviceSerialNumber;
        @NotNull(message = "Device type  is required.")
        private Device.deviceType deviceType= null;

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceRequestCreationDto implements Serializable {
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Long id;
        @NotBlank(message = "Description is required.")
        private String description;
        @NotNull(message = "Price is required.")
        private Long price;
    }
}