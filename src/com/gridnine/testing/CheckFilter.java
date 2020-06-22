package com.gridnine.testing;

import java.time.LocalDateTime;

public class CheckFilter {
    public void checkDepartureDate(FilterDateInterface filterDateInterface, LocalDateTime date, Condition condition) {
        filterDateInterface.getByDepartureDate(date, condition);
    }

    public void checkArrivalDate(FilterDateInterface filterDateInterface, LocalDateTime date, Condition condition) {
        filterDateInterface.getByArrivalDate(date, condition);
    }

    public void checkDuration(FilterDurationInterface filterDateInterface, long duration, DurationSelect durationSelect) {
        filterDateInterface.getByDuration(duration, durationSelect);
    }
}
