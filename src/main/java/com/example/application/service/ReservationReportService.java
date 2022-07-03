package com.example.application.service;

import com.example.application.model.ReservationReportRequest;
import com.example.application.model.VisitSummary;

public interface ReservationReportService {

    VisitSummary getVisitSummary(ReservationReportRequest input);
}
