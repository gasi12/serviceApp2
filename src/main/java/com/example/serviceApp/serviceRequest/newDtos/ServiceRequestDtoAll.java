package com.example.serviceApp.serviceRequest.newDtos;

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

/**
 * DTO for {@link ServiceRequest}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestDtoAll implements Serializable {
    private Long id;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long price;
    private DeviceDtoAll device;
    private ServiceRequest.Status lastStatus;
    private List<StatusHistoryDtoAll> statusHistoryList;
    private DeviceDtoAll.CustomerDtoAll customer;

    /**
     * DTO for {@link com.example.serviceApp.device.Device}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeviceDtoAll implements Serializable {
        private Long id;
        private String deviceName;
        private String deviceSerialNumber;
        private Device.deviceType deviceType;


        /**
         * DTO for {@link com.example.serviceApp.customer.Customer}
         */
        @Data
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CustomerDtoAll implements Serializable {
            private Long id;
            private String firstName;
            private String lastName;
            private Long phoneNumber;
        }
    }

    /**
     * DTO for {@link com.example.serviceApp.serviceRequest.status.StatusHistory}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatusHistoryDtoAll implements Serializable {
        private Long id;
        private ServiceRequest.Status status;
        private String comment;
        private LocalDateTime time;
    }
}