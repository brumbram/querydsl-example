package com.opentable.sampleapplication.dao;

import com.opentable.sampleapplication.exception.DataPersistException;
import com.opentable.sampleapplication.model.entity.Guest;

public interface GuestDao {

    Guest save(Guest person) throws DataPersistException;
    Guest findByGuestId(String guestId);
    Guest findByName(String name);
}
