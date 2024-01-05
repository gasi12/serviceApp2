package com.example.serviceApp.serviceRequest.status.dto;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import com.example.serviceApp.serviceRequest.status.StatusHistory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link StatusHistory}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusHistoryDto implements Serializable {
    private ServiceRequest.Status status;
    private String comment;
    @JsonProperty()
    private LocalDateTime time;
}