package com.example.application.dao;

import com.example.application.model.ReservationSummary;
import com.example.application.model.entity.Reservation;
import com.example.application.exception.DataPersistException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;

import java.util.List;

public interface ReservationDao {
    Reservation save(Reservation person) throws DataPersistException;
    Reservation findByReservationId(String guestId);
    List<ReservationSummary> getReservations(Predicate filter, List<OrderSpecifier> orderBy, QBean<ReservationSummary> select);
}
