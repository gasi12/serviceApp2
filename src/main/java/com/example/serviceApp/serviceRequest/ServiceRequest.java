package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.device.Device;
import com.example.serviceApp.security.User.User;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(name = "ServiceRequest.deviceAndCustomer", attributeNodes = {
        @NamedAttributeNode(value = "device", subgraph = "device-subgraph")
}, subgraphs = {
        @NamedSubgraph(name = "device-subgraph", attributeNodes = {
                @NamedAttributeNode("customer")
        })
})
@Table
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    private Long id;




    @Column(columnDefinition = "text") //postgres
    private String description;

    private Status lastStatus = Status.PENDING;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private LocalDateTime endDate;
    @CreationTimestamp
    //@Temporal(TemporalType.DATE)
    private LocalDateTime startDate;

    private Long price;

    private String deviceName;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "device_id")
    private Device device;
    @ManyToOne//(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusHistory> statusHistory;


    public ServiceRequest(String description, Status lastStatus, LocalDateTime endDate, LocalDateTime startDate, Long price, Device device) {
        this.description = description;
        this.lastStatus = lastStatus;
        this.endDate = endDate;
        this.startDate = startDate;
        this.price = price;
        this.device= device;

    }

    public enum Status {
        PENDING,IN_PROCESS,ON_HOLD,FINISHED
    }



}
