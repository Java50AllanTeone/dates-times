package telran.time;

import java.time.DayOfWeek;
import java.time.temporal.*;

public class NextFriday13 implements TemporalAdjuster {

    @Override
    public Temporal adjustInto(Temporal temporal) {
        if(!temporal.isSupported(ChronoUnit.MONTHS)) {
            throw new UnsupportedTemporalTypeException("must support years");
        }

        do {
            temporal = temporal.plus(1, ChronoUnit.DAYS);
        } while (temporal.get(ChronoField.DAY_OF_MONTH) != 13);

        while (temporal.get(ChronoField.DAY_OF_WEEK) != 5) {
            temporal = temporal.plus(1, ChronoUnit.MONTHS);
        }
        return temporal;
    }

}
