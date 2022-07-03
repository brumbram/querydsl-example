package com.example.application;

import com.example.application.dao.GuestDao;
import com.example.application.dao.ReservationDao;
import com.example.application.model.dto.GuestDto;
import com.example.application.model.dto.ReservationDto;
import com.example.application.model.entity.Guest;
import com.example.application.model.entity.Reservation;
import com.example.application.service.CrudServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CrudServiceTest {

    private CrudServiceImpl crudService;

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private GuestDao guestDao;

    @BeforeEach
    void init() {
        crudService = new CrudServiceImpl(reservationDao, guestDao);
    }

    @ParameterizedTest
    @MethodSource("mappedData")
    void shouldGetMappedData(List<ReservationDto> inputReservationDto, Map<GuestDto, List<ReservationDto>> mappedDto){

        assertThat(crudService.getMappedData(inputReservationDto))
                .containsExactlyInAnyOrderEntriesOf(mappedDto);
    }

    @ParameterizedTest
    @MethodSource("populateReservations")
    void shouldPopulateReservations(List<ReservationDto> inputReservationDto, Guest inputGuest, List<Reservation> outputReservations){

        when(reservationDao.findByReservationId(anyString()))
                .thenReturn(any(Reservation.class));

        assertThat(crudService.populateReservations(inputReservationDto, inputGuest))
                .usingRecursiveFieldByFieldElementComparatorOnFields("reservationId")
                .containsExactlyInAnyOrderElementsOf(outputReservations);
    }

    @ParameterizedTest
    @MethodSource("persistReservations")
    void shouldPersistGuestReservation(Map.Entry<GuestDto, List<ReservationDto>> entry){
        when(guestDao.findByGuestId(anyString()))
                .thenReturn(any(Guest.class));

        crudService.persistGuestReservation(entry);
        verify(guestDao, times(1)).findByGuestId(anyString());
        verify(guestDao, times(1)).save(any(Guest.class));
    }

    private static Stream<Arguments> mappedData() {
        var guestDto1 = new GuestDto("guest1","Guest 1");
        var guestDto2 = new GuestDto("quest2","Guest 2");
        var reservationDto1 = new ReservationDto("res1", 2, new Date(), new BigDecimal("10"), guestDto1);
        var reservationDto2 = new ReservationDto("res2", 2, new Date(), new BigDecimal("10"), guestDto1);
        var reservationDto3 = new ReservationDto("res3", 2, new Date(), new BigDecimal("10"), guestDto2);

        return Stream.of(
                Arguments.of(List.of(reservationDto1, reservationDto2), Map.of(guestDto1, List.of(reservationDto1, reservationDto2))),
                Arguments.of(List.of(reservationDto3), Map.of(guestDto2, List.of(reservationDto3)))
        );
    }

    private static Stream<Arguments> populateReservations() {
        var guestDto1 = new GuestDto("guest1","Guest 1");
        var guestDto2 = new GuestDto("quest2","Guest 2");

        var reservationDto1 = new ReservationDto("res1", 2, new Date(), new BigDecimal("10"), guestDto1);
        var reservationDto2 = new ReservationDto("res2", 2, new Date(), new BigDecimal("10"), guestDto1);
        var reservationDto3 = new ReservationDto("res3", 2, new Date(), new BigDecimal("10"), guestDto2);

        var guest1 = guestDto1.mapToEntity();
        var guest2 = guestDto2.mapToEntity();

        var reservation1 = reservationDto1.mapToEntity();
        var reservation2 = reservationDto2.mapToEntity();
        var reservation3 = reservationDto3.mapToEntity();

        return Stream.of(
                Arguments.of(List.of(reservationDto1, reservationDto2), guest1, List.of(reservation1, reservation2)),
                Arguments.of(List.of(reservationDto3), guest2, List.of(reservation3))
        );
    }

    private static Stream<Arguments> persistReservations() {
        var guestDto1 = new GuestDto("guest1","Guest 1");
        var guestDto2 = new GuestDto("quest2","Guest 2");

        var reservationDto1 = new ReservationDto("res1", 2, new Date(), new BigDecimal("10"), guestDto1);
        var reservationDto2 = new ReservationDto("res2", 2, new Date(), new BigDecimal("10"), guestDto1);
        var reservationDto3 = new ReservationDto("res3", 2, new Date(), new BigDecimal("10"), guestDto2);

        return Stream.of(
                Arguments.of(Map.entry(guestDto1, List.of(reservationDto1, reservationDto2))),
                Arguments.of(Map.entry(guestDto2, List.of(reservationDto3)))
        );
    }
}
