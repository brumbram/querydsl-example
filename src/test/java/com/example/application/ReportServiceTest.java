package com.example.application;

import com.example.application.dao.ReservationDao;
import com.example.application.model.DateRange;
import com.example.application.model.ReservationReportRequest;
import com.example.application.model.ReservationSummary;
import com.example.application.model.SortField;
import com.opentable.sampleapplication.model.entity.QGuest;
import com.opentable.sampleapplication.model.entity.QReservation;
import com.example.application.helper.QueryHelper;
import com.example.application.helper.ReservationReportMetaData;
import com.example.application.service.ReservationReportServiceImpl;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ReportServiceTest {

    private ReservationReportServiceImpl reportService;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private ReservationReportMetaData metaData;

    @Mock
    private ReservationDao reservationDao;

    @BeforeEach
    void init() {
        metaData = new ReservationReportMetaData();
        reportService = new ReservationReportServiceImpl(reservationDao, metaData);
        formatter.setTimeZone(TimeZone.getDefault());
    }

    @ParameterizedTest
    @MethodSource("buildPredicates")
    void shouldBuildPredicates(DateRange input, String whereClause) {
        assertEquals(whereClause, reportService.buildPredicates(input).toString());
    }

    @Test
    void shouldBuildSelectCriteria() {

        var res = reportService.buildSelectCriteria();
        var expr = res.getArgs();

        assertThat(res.getArgs()).containsExactlyElementsOf(
                          List.of(QGuest.guest.name.as(Expressions.path(String.class, "guestName")),
                                    QReservation.reservation.count().as(Expressions.numberPath(Long.class, "totalVisits")),
                                    QReservation.reservation.totalSpend.sum().as(Expressions.numberPath(BigDecimal.class, "totalSpend"))));

    }

    @ParameterizedTest
    @MethodSource("buildVisitSummary")
    void shouldCreateVisitSummary(ReservationReportRequest input, List<ReservationSummary> summaries) {

        var filter = reportService.buildPredicates(input.getDateRange());
        var orderSpecifiers = QueryHelper.buildOrderSpecifiers(input.getSortExpressions(), metaData.getSelectExpression());
        var selectCriteria = reportService.buildSelectCriteria();
        when(reservationDao.getReservations(filter, orderSpecifiers,selectCriteria)).thenReturn(summaries);

        assertThat(reportService.getVisitSummary(input).getReservationSummaries())
                .usingRecursiveFieldByFieldElementComparatorOnFields("guestName", "totalVisits", "totalSpend")
                .containsExactlyElementsOf(summaries);
    }

    private static Stream<Arguments> buildPredicates() throws ParseException {

        var range1 = new DateRange(formatter.parse("2020-11-20")  , formatter.parse("2021-11-20"));
        var range2 = new DateRange(formatter.parse("2020-04-05")  , formatter.parse("2021-12-20"));

        return Stream.of(
                Arguments.of(range1, "reservation.scheduledDate between Fri Nov 20 00:00:00 AEDT 2020 and Sat Nov 20 00:00:00 AEDT 2021"),
                Arguments.of(range2, "reservation.scheduledDate between Sun Apr 05 00:00:00 AEDT 2020 and Mon Dec 20 00:00:00 AEDT 2021")
        );
    }

    private static Stream<Arguments> buildVisitSummary() throws ParseException {

        var request1 = new ReservationReportRequest(new DateRange(formatter.parse("2020-11-20")  , formatter.parse("2021-11-20")), null);
        var request2 = new ReservationReportRequest(new DateRange(formatter.parse("2020-04-05")  , formatter.parse("2021-12-20")), List.of(new SortField("guestName", "asc")));
        var request3 = new ReservationReportRequest(new DateRange(formatter.parse("2020-04-05")  , formatter.parse("2021-12-20")), List.of(new SortField("totalVisits", "asc")));

        var summary1 = new ReservationSummary("guest1", 3l, new BigDecimal("300"));
        var summary2 = new ReservationSummary("guest2", 5l, new BigDecimal("200"));
        var summary3 = new ReservationSummary("guest3", 2l, new BigDecimal("500"));
        var summary4 = new ReservationSummary("guest4", 4l, new BigDecimal("100"));
        var summary5 = new ReservationSummary("guest5", 6l, new BigDecimal("300"));


        return Stream.of(
                Arguments.of(request1, List.of(summary1, summary2, summary3)),
                Arguments.of(request2, List.of(summary1, summary2, summary3, summary4, summary5)),
                Arguments.of(request3, List.of(summary5, summary2, summary1, summary4, summary3))
        );
    }

}
