package com.opentable.sampleapplication.dao;

import com.opentable.sampleapplication.exception.DataPersistException;
import com.opentable.sampleapplication.model.ReservationSummary;
import com.opentable.sampleapplication.model.entity.Reservation;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;

import java.util.List;

public interface ReservationDao {
    Reservation save(Reservation person) throws DataPersistException;
    Reservation findByReservationId(String guestId);
    List<ReservationSummary> getReservations(Predicate filter, List<OrderSpecifier> orderBy, QBean<ReservationSummary> select);
}
