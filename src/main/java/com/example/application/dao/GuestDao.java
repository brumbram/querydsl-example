package com.example.application.dao;

import com.example.application.model.entity.Guest;
import com.example.application.exception.DataPersistException;

public interface GuestDao {

    Guest save(Guest person) throws DataPersistException;
    Guest findByGuestId(String guestId);
    Guest findByName(String name);
}
