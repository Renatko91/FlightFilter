package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<Flight> list = FlightBuilder.createFlights();

        LocalDateTime now = LocalDateTime.now();

        FilterByTimeSegment filterByTimeSegment = new FilterByTimeSegment(list);
        FilterByFlight filterByFlight = new FilterByFlight(list);
        FilterByGround filterByGround = new FilterByGround(list);

        CheckFilter checkFilter = new CheckFilter();
        checkFilter.checkArrivalDate(filterByFlight, now, Condition.BEFORE);

        System.out.println("-------------------------------");

        filterByTimeSegment.getByDepartingInThePast();

        System.out.println("-------------------------------");

        checkFilter.checkDuration(filterByGround, 120, DurationSelect.MORE);
    }
}
