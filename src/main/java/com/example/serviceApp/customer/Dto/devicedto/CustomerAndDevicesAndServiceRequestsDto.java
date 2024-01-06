package com.example.serviceApp.customer.Dto.devicedto;

import com.example.serviceApp.device.Device;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAndDevicesAndServiceRequestsDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private DeviceDto device;

    /**
     * DTO for {@link com.example.serviceApp.device.Device}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeviceDto implements Serializable {
        private Long id;
        private String deviceName;
        private String deviceSerialNumber;

        private Device.deviceType deviceType;

        private List<ServiceRequestDto> serviceRequestList;

        /**
         * DTO for {@link com.example.serviceApp.serviceRequest.ServiceRequest}
         */
        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ServiceRequestDto implements Serializable {
            private Long id;
            private String description;
            private ServiceRequest.Status lastStatus;
            private LocalDateTime endDate;
            private LocalDateTime startDate;
            private Long price;
        }
    }
}