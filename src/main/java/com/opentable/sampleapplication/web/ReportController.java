package com.opentable.sampleapplication.web;

import com.opentable.sampleapplication.model.ReservationReportRequest;
import com.opentable.sampleapplication.model.VisitSummary;
import com.opentable.sampleapplication.service.ReservationReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("reports")
public class ReportController {

    private ReservationReportService reportService;

    @Autowired
    public ReportController(ReservationReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Get list of visitSummary for valid input of date range and sorting strategy
     * @param request {@code ReportRequest}
     * @return a {@code ResponseEntity<VisitSummary>}
     */
    @GetMapping(path = "/visits", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VisitSummary> getVisits(@RequestBody @Valid ReservationReportRequest request) {

        log.info("Request received for date range {} and {}", request.getDateRange().getStart(), request.getDateRange().getEnd());
        var report = reportService.getVisitSummary(request);
        return ResponseEntity.ok(report);
    }
}
