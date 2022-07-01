package com.opentable.sampleapplication.model.dto;

import com.opentable.sampleapplication.model.entity.Guest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestDto {

    public String id;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuestDto guestDto = (GuestDto) o;
        return Objects.equals(id, guestDto.id) && Objects.equals(name, guestDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Guest mapToEntity() {
        Guest guest = new Guest();
        guest.setGuestId(this.getId());
        guest.setName(this.getName());
        return guest;
    }
}
