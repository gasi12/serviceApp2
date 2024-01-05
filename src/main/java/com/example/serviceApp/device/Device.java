package com.example.serviceApp.device;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Table
@Entity
@Builder
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    private Long id;
    private String deviceName;
    private String deviceSerialNumber;
    @JsonManagedReference
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceRequest> serviceRequestList;
    private deviceType deviceType;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Device(String deviceName, String deviceSerialNumber) {
        this.deviceName = deviceName;
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public Device(String deviceName, String deviceSerialNumber, List<ServiceRequest> serviceRequestList, Customer customer) {
        this.deviceName = deviceName;
        this.deviceSerialNumber = deviceSerialNumber;
        this.serviceRequestList = serviceRequestList;
        this.customer = customer;
    }
    public enum deviceType{
        LAPTOP,
        DESKTOP,
        SMARTPHONE,
        TV,
        PRINTER,
        MONITOR,
        GAME_CONSOLE,
        OTHER
    }
}
