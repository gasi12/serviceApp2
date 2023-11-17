package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.security.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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



    //h2 @Lob
    @Column(columnDefinition = "text") //postgres
    private String description;

    private Status status = Status.PENDING;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private LocalDate endDate;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;

    private Long price;
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference

    private Customer customer;
    @ManyToOne//(fetch = FetchType.LAZY)

    @JoinColumn(name = "user_id")
    private User user;
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
    @JsonProperty("customerId")
    public Long getCustomerId() {
        if (customer != null) {
            return customer.getId();
        }
        return null;
    }


    @Override
    public String toString() {
        return "ServiceRequest{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", endDate=" + endDate +
                ", startDate=" + startDate +
                ", price=" + price +
                '}';
    }
}
