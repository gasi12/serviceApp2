package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.customer.Customer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
//import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table
@Entity
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String description;

    private Status status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private LocalDate endDate;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;

    private Long price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    public ServiceRequest(String description) {
        this.description = description;
    }

    public ServiceRequest(String description, Status status, LocalDate endDate, LocalDate startDate, Long price, Customer customer) {
        this.description = description;
        this.status = status;
        this.endDate = endDate;
        this.startDate = startDate;
        this.price = price;
        this.customer = customer;
    }

    public enum Status {
        PENDING,IN_PROCESS,ON_HOLD,FINISHED
    }
}
