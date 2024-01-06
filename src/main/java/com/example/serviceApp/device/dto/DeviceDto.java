package com.example.serviceApp.device.dto;

import com.example.serviceApp.device.Device;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Device}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDto implements Serializable {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull(message = "name is required.")
    private String deviceName;
    @NotNull(message = "serial number is required.")
    private String deviceSerialNumber;

    private Device.deviceType deviceType;

    private List<ServiceRequestDto> serviceRequestList;

    /**
     * DTO for {@link com.example.serviceApp.serviceRequest.ServiceRequest}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceRequestDto implements Serializable {
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Long id;
        @NotNull(message = "Description is required.")
        private String description;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private ServiceRequest.Status lastStatus;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private LocalDateTime endDate;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private LocalDateTime startDate;

        private Long price;

    }
}