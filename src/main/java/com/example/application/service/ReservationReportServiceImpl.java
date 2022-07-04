package com.example.application.service;

import com.example.application.dao.ReservationDao;
import com.example.application.helper.QueryHelper;
import com.example.application.helper.ReservationReportMetaData;
import com.example.application.model.DateRange;
import com.example.application.model.ReservationReportRequest;
import com.example.application.model.ReservationSummary;
import com.example.application.model.VisitSummary;
import com.example.application.model.entity.QGuest;
import com.example.application.model.entity.QReservation;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationReportServiceImpl implements ReservationReportService {

    private ReservationDao reservationDao;
    private ReservationReportMetaData metaData;

    @Autowired
    public ReservationReportServiceImpl(ReservationDao reservationDao, ReservationReportMetaData metaData) {

        this.reservationDao = reservationDao;
        this.metaData = metaData;
    }

    /**
     * Service method to generate report for visit enlisting the reservation summary
     * @param input {@code ReservationReportRequest} representing the input provided by the user
     * @return {@code VisitSummary}
     */
    @Override
    public VisitSummary getVisitSummary(ReservationReportRequest input) {

        var visitSummary = new VisitSummary();

        var filter = buildPredicates(input.getDateRange());
        var orderSpecifiers = QueryHelper.buildOrderSpecifiers(input.getSortExpressions(), metaData.getSelectExpression());
        var selectCriteria = buildSelectCriteria();

        visitSummary.setReservationSummaries(reservationDao.getReservations(filter, orderSpecifiers, selectCriteria));
        return visitSummary;
    }

    /**
     * Method to build search criteria and build an object representation for select criteria
     * @return {@code QBean} of type {@code ReservationSummary}
     */
    public QBean<ReservationSummary> buildSelectCriteria() {

        return Projections.bean(ReservationSummary.class,
                QGuest.guest.name.as(metaData.getSelectExpression().get(metaData.GUEST_NAME)),
                QReservation.reservation.count().as(metaData.getSelectExpression().get(metaData.TOTAL_VISITS)),
                QReservation.reservation.totalSpend.sum().as(metaData.getSelectExpression().get(metaData.TOTAL_SPEND)));
    }

    /**
     * Method to build the where clause using predicate
     * @param input {@code DateRange} Range of date input
     * @return
     */
    public  Predicate buildPredicates(DateRange input) {
        return ExpressionUtils.allOf(QReservation.reservation.scheduledDate.between(input.getStart(), input.getEnd()));
    }
}
