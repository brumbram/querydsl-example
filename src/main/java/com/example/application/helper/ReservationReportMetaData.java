package com.example.application.helper;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class ReservationReportMetaData{

    public static final String GUEST_NAME = "guestName";
    public static final String TOTAL_VISITS = "totalVisits";
    public static final String TOTAL_SPEND = "totalSpend";

    private Map<String, Path> selectExpression = Map.of(GUEST_NAME, Expressions.path(String.class, GUEST_NAME),
                                                        TOTAL_VISITS, Expressions.numberPath(Long.class, TOTAL_VISITS),
                                                        TOTAL_SPEND, Expressions.numberPath(BigDecimal .class, TOTAL_SPEND));

    public Map<String, Path> getSelectExpression(){
        return selectExpression;
    }
}
