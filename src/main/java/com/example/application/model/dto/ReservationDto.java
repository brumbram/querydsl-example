package com.example.application.model.dto;

import com.example.application.model.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    public String reservation_id;

    public int party_size;

    public Date scheduled_date;

    public BigDecimal total_spend;

    public GuestDto guest;

    public Reservation mapToEntity() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(this.getReservation_id());
        reservation.setPartySize(this.getParty_size());
        reservation.setTotalSpend(this.getTotal_spend());
        reservation.setScheduledDate(this.getScheduled_date());
        return reservation;
    }
}
