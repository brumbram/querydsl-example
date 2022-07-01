package com.opentable.sampleapplication.service;

import com.opentable.sampleapplication.model.dto.ReservationDto;

import java.util.List;

public interface CrudService {

    void saveAllReservations(List<ReservationDto> reservationDtoList);
}
