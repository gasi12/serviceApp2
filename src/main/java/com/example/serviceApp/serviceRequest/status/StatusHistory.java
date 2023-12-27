package com.example.serviceApp.serviceRequest.status;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table
@Entity
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)

    private Long id;
    private ServiceRequest.Status status;
    private String comment;
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicerequest_id")
    private ServiceRequest serviceRequest;
    private LocalDateTime time;

    public StatusHistory(ServiceRequest.Status status, String comment) {
        this.status = status;
        this.comment = comment;
    }



    public StatusHistory(ServiceRequest.Status status, String comment, ServiceRequest serviceRequest, LocalDateTime time) {
        this.status = status;
        this.comment = comment;
        this.serviceRequest = serviceRequest;
        this.time = time;
    }
//    public enum Status {
//        PENDING,IN_PROCESS,ON_HOLD,FINISHED
//    }
}
