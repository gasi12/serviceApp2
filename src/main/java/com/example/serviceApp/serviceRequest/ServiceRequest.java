package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.appUser.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table
@Entity

public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String description;

    private Status status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private LocalDate endDate;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;

    private Long price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id")
    @JsonIgnore
    private AppUser appUser;

    public ServiceRequest(String description) {
        this.description = description;
    }

    public ServiceRequest(String description, Status status, LocalDate endDate, LocalDate startDate, Long price, AppUser appUser) {
        this.description = description;
        this.status = status;
        this.endDate = endDate;
        this.startDate = startDate;
        this.price = price;
        this.appUser = appUser;
    }

    public enum Status {
        PENDING,IN_PROCESS,ON_HOLD,FINISHED
    }
}
