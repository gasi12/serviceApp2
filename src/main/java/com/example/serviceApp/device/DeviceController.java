package com.example.serviceApp.device;


import com.example.serviceApp.device.dto.DeviceDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
@SecurityRequirement(name = "bearerAuth")
public class DeviceController {
    private final DeviceService deviceService;
    @GetMapping("/serialNumber/{serialNumber}")
    public DeviceDto findBySerialNumber(@PathVariable String serialNumber){
     return deviceService.findBySerialNumber(serialNumber);
    }
@PostMapping("/{customerId}")
    public DeviceDto addDeviceToCustomer(@PathVariable Long customerId,@RequestBody DeviceDto deviceDto){
      return   deviceService.addOrUpdateDeviceToCustomer(customerId,deviceDto);
    }

    @DeleteMapping("/{id}")//ok
    public void deleteServiceRequest(@PathVariable Long id) {
        deviceService.deleteById(id);
    }
}
