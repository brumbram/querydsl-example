package com.opentable.sampleapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentable.sampleapplication.exception.UnsupportedInputException;
import com.opentable.sampleapplication.model.ReservationReportRequest;
import com.opentable.sampleapplication.model.ReservationSummary;
import com.opentable.sampleapplication.model.VisitSummary;
import com.opentable.sampleapplication.service.ReservationReportService;
import com.opentable.sampleapplication.web.ReportController;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@ActiveProfiles("test")
public class ReportControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationReportService reportService;


    @ParameterizedTest
    @MethodSource("incorrectSortingDirection")
    public void incorrectSortingDirectionShouldReturnBadRequest(String requestJson) throws Exception {
        ReservationReportRequest input = objectMapper.readValue(requestJson, ReservationReportRequest.class);

        when(reportService.getVisitSummary(input)).thenThrow(new UnsupportedInputException("Unsupported sort order dummy"));

        this.mockMvc.perform(get("/reports/visits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", containsString("Unsupported sort order dummy")));
    }

    @ParameterizedTest
    @MethodSource("incorrectDateRange")
    public void incorrectDateRangeShouldReturnBadRequest(String requestJson) throws Exception {
        ReservationReportRequest input = objectMapper.readValue(requestJson, ReservationReportRequest.class);

        this.mockMvc.perform(get("/reports/visits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[*].message",
                            containsInAnyOrder("Date is out of bound. Please provide a valid date range where start is not greater than end")));
    }

    @ParameterizedTest
    @MethodSource("correctSortingDirection")
    public void correctSortingShouldReturnOkResponse(String requestJson) throws Exception {
        ReservationReportRequest input = objectMapper.readValue(requestJson, ReservationReportRequest.class);
        VisitSummary visits = new VisitSummary();
        visits.setReservationSummaries(List.of(new ReservationSummary("Guest", 2l, new BigDecimal("500.00"))));

        when(reportService.getVisitSummary(input)).thenReturn(visits);

        this.mockMvc.perform(get("/reports/visits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reservationSummaries").isArray());
    }

    private static Stream<Arguments> incorrectSortingDirection() {
        return Stream.of(
                Arguments.of("{\"dateRange\":{\"start\":\"2010-11-20\",\"end\":\"2020-11-20\"},\"sortExpressions\":[{\"fieldName\": \"dummy\", \"sortOrder\": \"asc\"}]}"),
                Arguments.of("{\"dateRange\":{\"start\":\"2010-11-20\",\"end\":\"2020-11-20\"},\"sortExpressions\":[{\"fieldName\": \"guestName\", \"sortOrder\": \"dummy\"}]}")
        );
    }

    private static Stream<Arguments> correctSortingDirection() {
        return Stream.of(
                Arguments.of("{\"dateRange\":{\"start\":\"2010-11-20\",\"end\":\"2020-11-20\"},\"sortExpressions\":[]}"),
                Arguments.of("{\"dateRange\":{\"start\":\"2010-11-20\",\"end\":\"2020-11-20\"},\"sortExpressions\":[{\"fieldName\": \"guestName\", \"sortOrder\": \"asc\"}]}"),
                Arguments.of("{\"dateRange\":{\"start\":\"2010-11-20\",\"end\":\"2020-11-20\"},\"sortExpressions\":[{\"fieldName\": \"guestName\", \"sortOrder\": \"asc\"}]}")
        );
    }

    private static Stream<Arguments> incorrectDateRange() {
        return Stream.of(
                Arguments.of("{\"dateRange\":{\"start\":\"3010-11-20\",\"end\":\"2020-11-20\"},\"sortExpressions\":[{\"fieldName\": \"guestName\", \"sortOrder\": \"asc\"}]}"),
                Arguments.of("{\"dateRange\":{\"start\":\"2021-11-20\",\"end\":\"2020-11-20\"},\"sortExpressions\":[{\"fieldName\": \"guestName\", \"sortOrder\": \"asc\"}]}")
        );
    }
}
