package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


class FilterByTimeSegment extends FilterTemplate implements FilterDateInterface {

    FilterByTimeSegment(List<Flight> flights) {
        super(flights);
    }

    @Override
    public List<List<Segment>> getByDepartureDate(LocalDateTime date, Condition condition) {
        Predicate<Segment> predicate = null;

        if(condition == Condition.BEFORE) {
            predicate = x -> Duration.between(date, x.getDepartureDate()).toMinutes() < 0;
        } else if(condition == Condition.AFTER) {
            predicate = x -> Duration.between(date, x.getDepartureDate()).toMinutes() > 0;
        } else {
            predicate = x -> Duration.between(date, x.getDepartureDate()).toMinutes() == 0;
        }

        for(List<Segment> segment : segments) {
            if(segment.stream().filter(predicate).count() > 0) {
                flightsAfterFilter.add(segment);
            }

        }
        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }

    @Override
    public List<List<Segment>> getByArrivalDate(LocalDateTime date, Condition condition) {
        Predicate<Segment> predicate = null;

        if(condition == Condition.BEFORE) {
            predicate = x -> Duration.between(date, x.getArrivalDate()).toMinutes() < 0;
        } else if(condition == Condition.AFTER) {
            predicate = x -> Duration.between(date, x.getArrivalDate()).toMinutes() > 0;
        } else {
            predicate = x -> Duration.between(date, x.getArrivalDate()).toMinutes() == 0;
        }

        for(List<Segment> segment : segments) {
            if(segment.stream().filter(predicate).count() > 0) {
                flightsAfterFilter.add(segment);
            }

        }
        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }

    @Override
    public List<List<Segment>> getByDuration(long minutesDuration, DurationSelect durationSelect) {
        Predicate<Segment> predicate = null;

        if(durationSelect == DurationSelect.LESS) {
            predicate = x -> Math.abs(Duration.between(x.getArrivalDate(), x.getDepartureDate()).toMinutes()) < minutesDuration;
        } else if(durationSelect == DurationSelect.MORE) {
            predicate = x -> Math.abs(Duration.between(x.getArrivalDate(), x.getDepartureDate()).toMinutes()) > minutesDuration;
        } else {
            predicate = x -> Math.abs(Duration.between(x.getArrivalDate(), x.getDepartureDate()).toMinutes()) == minutesDuration;
        }

        for(List<Segment> segment : segments) {
            if(segment.stream().filter(predicate).count() > 0) {
                flightsAfterFilter.add(segment);
            }
        }
        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }

    public List<List<Segment>> getByDepartingInThePast() {
        Predicate<Segment> predicateMinutes = x -> Duration.between(x.getArrivalDate(), x.getDepartureDate()).toMinutes() > 0;

        for(List<Segment> segment : segments) {
            if(segment.stream().filter(predicateMinutes).count() > 0) {
                flightsAfterFilter.add(segment);
            }
        }

        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }
}

class FilterByFlight extends FilterTemplate implements FilterDateInterface {
    FilterByFlight(List<Flight> flights) {
        super(flights);
    }

    @Override
    public List<List<Segment>> getByDepartureDate(LocalDateTime date, Condition condition) {
        for (List<Segment> segment : segments) {
            if(segment.get(0).getDepartureDate().isBefore(date) && condition == Condition.BEFORE) {
                flightsAfterFilter.add(segment);
            }
            if(segment.get(0).getDepartureDate().isAfter(date) && condition == Condition.AFTER) {
                flightsAfterFilter.add(segment);
            }
            if(segment.get(0).getDepartureDate().isEqual(date) && condition == Condition.IN) {
                flightsAfterFilter.add(segment);
            }
        }
        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }

    @Override
    public List<List<Segment>> getByArrivalDate(LocalDateTime date, Condition condition) {
        for (List<Segment> segment : segments) {
            if(segment.get(segment.size() - 1).getArrivalDate().isBefore(date) && condition == Condition.BEFORE) {
                flightsAfterFilter.add(segment);
            }
            if(segment.get(segment.size() - 1).getArrivalDate().isAfter(date) && condition == Condition.AFTER) {
                flightsAfterFilter.add(segment);
            }
            if(segment.get(segment.size() - 1).getArrivalDate().isEqual(date) && condition == Condition.IN) {
                flightsAfterFilter.add(segment);
            }
        }
        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }

    @Override
    public List<List<Segment>> getByDuration(long duration, DurationSelect durationSelect) {
        for (List<Segment> segment : segments) {
            if(Math.abs(
                    Duration.between(
                            segment.get(0).getDepartureDate(),
                            segment.get(segment.size() - 1).getArrivalDate()
                    ).toMinutes()
            ) < duration && durationSelect == DurationSelect.LESS) {
                flightsAfterFilter.add(segment);
            }

            if(Math.abs(
                    Duration.between(
                            segment.get(0).getDepartureDate(),
                            segment.get(segment.size() - 1).getArrivalDate()
                    ).toMinutes()
            ) > duration && durationSelect == DurationSelect.MORE) {
                flightsAfterFilter.add(segment);
            }

            if(Math.abs(
                    Duration.between(
                            segment.get(0).getDepartureDate(),
                            segment.get(segment.size() - 1).getArrivalDate()
                    ).toMinutes()
            ) == duration && durationSelect == DurationSelect.EQUAL) {
                flightsAfterFilter.add(segment);
            }
        }

        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }
}

class FilterByGround extends FilterTemplate {
    List<Long> durationList = new ArrayList<>();
    long dur = 0;

    FilterByGround(List<Flight> flights) {
        super(flights);
        for(int i = 0; i < segments.size(); i++) {
            if(segments.get(i).size() < 2) {
                durationList.add((long)0);
            }else {
                for(int j = 0; j < segments.get(i).size() - 1; j++) {
                    dur += Duration.between(segments.get(i).get(j).getArrivalDate(), segments.get(i).get(j + 1).getDepartureDate()).toMinutes();
                }
                durationList.add(dur);
                dur = 0;
            }
        }
    }

    @Override
    public List<List<Segment>> getByDuration(long duration, DurationSelect durationSelect) {
        for(int i = 0; i < durationList.size(); i++) {
            if(durationList.get(i) < duration && durationSelect == DurationSelect.LESS) {
                flightsAfterFilter.add(segments.get(i));
            }
            if(durationList.get(i) > duration && durationSelect == DurationSelect.MORE) {
                flightsAfterFilter.add(segments.get(i));
            }
            if(durationList.get(i) == duration && durationSelect == DurationSelect.EQUAL) {
                flightsAfterFilter.add(segments.get(i));
            }
        }

        flightsAfterFilter.forEach(System.out::println);
        return flightsAfterFilter;
    }
}

enum Condition {
    BEFORE, AFTER, IN
}

enum DurationSelect {
    LESS, MORE, EQUAL
}

abstract class FilterTemplate implements FilterDurationInterface {
    List<List<Segment>> segments = new ArrayList<>();
    List<List<Segment>> flightsAfterFilter = new ArrayList<>();

    FilterTemplate(List<Flight> flights) {
        for(Flight flight : flights) {
            this.segments.add(flight.getSegments());
        }
    }

    FilterTemplate() {}
}

interface FilterDateInterface {
    public List<List<Segment>> getByDepartureDate(LocalDateTime date, Condition condition);

    public List<List<Segment>> getByArrivalDate( LocalDateTime date, Condition condition);
}

interface FilterDurationInterface {
    public List<List<Segment>> getByDuration(long duration, DurationSelect durationSelect);
}

