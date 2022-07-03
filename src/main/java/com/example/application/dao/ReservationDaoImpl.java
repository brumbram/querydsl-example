package com.example.application.dao;

import com.example.application.model.ReservationSummary;
import com.example.application.model.entity.Reservation;
import com.example.application.exception.DataPersistException;
import com.opentable.sampleapplication.model.entity.QGuest;
import com.opentable.sampleapplication.model.entity.QReservation;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import java.util.List;

/**
 * Dao layer to persist and query data to database
 * JPA EntityManager and JPAQueryFactory is used respectively
 * */
@Repository
public class ReservationDaoImpl implements ReservationDao{

    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public ReservationDaoImpl(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public Reservation save(Reservation reservation) throws DataPersistException {
        try {
            entityManager.persist(reservation);
            return reservation;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            throw new DataPersistException("Error while saving Reservation entity", ex);
        }
    }

    @Override
    public Reservation findByReservationId(String reservationId) {

        return (Reservation) queryFactory.from(QReservation.reservation)
                                         .where(QReservation.reservation.reservationId.eq(reservationId))
                                         .fetchOne();
    }

    /**
     * Database query to fetch {@code Guest} and their {@code Reservation} based on different criteria
     * @param filter {@code Predicate} provided by the service layer
     * @param orderBy A List of {@code OrderSpecifier} providing order by clauses
     * @param selectCriteria {@code QBean} to represent the select criteria
     * @return
     */
    @Override
    public List<ReservationSummary> getReservations(Predicate filter, List<OrderSpecifier> orderBy, QBean<ReservationSummary> selectCriteria) {

        return queryFactory.selectFrom(QReservation.reservation)
                    .join(QReservation.reservation.guest, QGuest.guest)
                    .select(selectCriteria)
                    .where(filter)
                    .groupBy(QGuest.guest.guestId)
                    .orderBy(orderBy.toArray(new OrderSpecifier[orderBy.size()]))
                    .fetch();
    }
}
