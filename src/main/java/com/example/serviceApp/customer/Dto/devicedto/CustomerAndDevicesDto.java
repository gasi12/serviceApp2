package com.example.serviceApp.customer.Dto.devicedto;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.device.Device;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Customer}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAndDevicesDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private List<DeviceDto> devices;

    /**
     * DTO for {@link com.example.serviceApp.device.Device}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeviceDto implements Serializable {
        private Long id;
        private String deviceName;
        private String deviceSerialNumber;
        private Device.deviceType deviceType;

    }
}