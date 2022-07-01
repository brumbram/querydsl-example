package com.opentable.sampleapplication.service;

import com.opentable.sampleapplication.dao.GuestDao;
import com.opentable.sampleapplication.dao.ReservationDao;
import com.opentable.sampleapplication.model.dto.GuestDto;
import com.opentable.sampleapplication.model.dto.ReservationDto;
import com.opentable.sampleapplication.model.entity.Guest;
import com.opentable.sampleapplication.model.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CrudServiceImpl implements CrudService {

    private ReservationDao reservationDao;
    private GuestDao guestDao;

    @Autowired
    public CrudServiceImpl(ReservationDao reservationDao, GuestDao guestDao) {
        this.reservationDao = reservationDao;
        this.guestDao = guestDao;
    }

    @Override
    @Transactional(readOnly = false)
    public void saveAllReservations(List<ReservationDto> reservationDtoList){

        getMappedData(reservationDtoList).entrySet()
                                         .stream()
                                         .forEach(entry -> persistGuestReservation(entry));
    }

    public void persistGuestReservation(Map.Entry<GuestDto, List<ReservationDto>> entry) {

        var guest = entry.getKey().mapToEntity();
        var existingEntry = guestDao.findByGuestId(guest.getGuestId());

        if (existingEntry != null) {
            guest = existingEntry;
            guest.setName(entry.getKey().getName());
        }

        List<Reservation> newReservations = populateReservations(entry.getValue(), guest);

        if (newReservations.size() > 0) {
            guest.getReservations().addAll(newReservations);
        }
        guestDao.save(guest);
    }

    public List<Reservation> populateReservations(List<ReservationDto> entries, Guest guest) {

        return entries.stream()
               .filter(y -> reservationDao.findByReservationId(y.getReservation_id()) == null)
               .map(x -> {
                   Reservation reservation = x.mapToEntity();
                   reservation.setGuest(guest);
                   return reservation;
               }).collect(Collectors.toList());
    }

    public Map<GuestDto, List<ReservationDto>> getMappedData(List<ReservationDto> reservationDtoList) {

        return reservationDtoList.stream()
                                 .collect(Collectors.groupingBy(ReservationDto::getGuest,
                                                                HashMap::new,
                                                                Collectors.toCollection(ArrayList::new)));
    }
}
