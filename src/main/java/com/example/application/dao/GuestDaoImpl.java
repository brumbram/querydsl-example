package com.example.application.dao;

import com.example.application.model.entity.Guest;
import com.example.application.exception.DataPersistException;
import com.opentable.sampleapplication.model.entity.QGuest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

/**
 * Dao layer to persist and query data to database
 * JPA EntityManager and JPAQueryFactory is used respectively
 * */
@Repository
public class GuestDaoImpl implements GuestDao{

    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public GuestDaoImpl(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public Guest save(Guest guest) throws DataPersistException {
        try {
            entityManager.persist(guest);
            return guest;
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            throw new DataPersistException("Error while saving Guest entity", ex);
        }
    }

    @Override
    public Guest findByGuestId(String guestId) {
        return (Guest) queryFactory.from(QGuest.guest).where(QGuest.guest.guestId.eq(guestId)).fetchOne();
    }

    @Override
    public Guest findByName(String name) {
        return (Guest) queryFactory.from(QGuest.guest).where(QGuest.guest.name.eq(name)).fetchOne();
    }
}
