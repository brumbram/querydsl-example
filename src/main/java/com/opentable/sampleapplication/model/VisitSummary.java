package com.opentable.sampleapplication.model;

import lombok.Data;

import java.util.List;

@Data
public class VisitSummary {
    List<ReservationSummary> reservationSummaries;
}
