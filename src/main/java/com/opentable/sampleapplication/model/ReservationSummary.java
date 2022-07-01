package com.opentable.sampleapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSummary {

    private String guestName;
    private Long totalVisits;
    private BigDecimal totalSpend;
}
