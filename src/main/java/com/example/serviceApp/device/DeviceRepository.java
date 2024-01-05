package com.example.serviceApp.device;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    @Transactional
    @Modifying
    @Query("update Device d set d.serviceRequestList = ?1 where d.deviceName = ?2")
    int updateServiceRequestListByDeviceName(ServiceRequest serviceRequestList, String deviceName);
    long deleteByDeviceSerialNumber(String deviceSerialNumber);
@EntityGraph(attributePaths = {"serviceRequestList"})
    Optional<Device> getDeviceByDeviceSerialNumber(String serialNumber);

Boolean existsByDeviceSerialNumber(String serialNumber);
Optional<Device> findByDeviceSerialNumber(String serialNumber);
}
