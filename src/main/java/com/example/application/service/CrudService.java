package com.example.application.service;

import com.example.application.model.dto.ReservationDto;

import java.util.List;

public interface CrudService {

    void saveAllReservations(List<ReservationDto> reservationDtoList);
}
