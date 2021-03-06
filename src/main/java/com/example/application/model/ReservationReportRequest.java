package com.example.application.model;

import com.example.application.validators.ValidBoundDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationReportRequest {

    @ValidBoundDate
    public DateRange dateRange;

    @Nullable
    public List<SortField> sortExpressions;

}
