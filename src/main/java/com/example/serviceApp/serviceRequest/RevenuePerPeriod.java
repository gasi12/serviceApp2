package com.example.serviceApp.serviceRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenuePerPeriod {
    public Integer month;
    public Integer year;
    public Long totalRevenue;


}
