package com.example.application.model;

import lombok.Data;

import java.util.List;

@Data
public class VisitSummary {
    List<ReservationSummary> reservationSummaries;
}
