package com.opentable.sampleapplication.service;

import com.opentable.sampleapplication.model.ReservationReportRequest;
import com.opentable.sampleapplication.model.VisitSummary;

public interface ReservationReportService {

    VisitSummary getVisitSummary(ReservationReportRequest input);
}
