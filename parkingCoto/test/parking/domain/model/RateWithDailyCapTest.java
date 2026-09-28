package parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link RateWithDailyCap}, the decorator holding the
 * "maximum daily rate from 10 hours" business rule. Uses a 900/hour base
 * rate with a 7000 cap (the car values from the assignment).
 */
class RateWithDailyCapTest {

    private final Rate rate = new RateWithDailyCap(new HourlyRate(900), 10, 7000);

    @ParameterizedTest(name = "{0} minutes -> {1}")
    @CsvSource({
            "0, 0",
            "1, 900",
            "60, 900",
            "61, 1800",
            "180, 2700",
            "540, 8100",     // 9 h: below the 10 h threshold, no cap yet
            "541, 7000",     // 10 h billed: cap applies (9000 -> 7000)
            "600, 7000",
            "660, 7000",     // 11 h: 9900 -> 7000
            "1440, 7000",    // exactly 24 h: one period
            "1441, 14000",   // 25 h: two periods (22500 -> 14000)
            "2880, 14000"    // 48 h: two periods
    })
    void appliesDailyCapFromTenHours(long minutes, long expected) {
        assertEquals(expected, rate.calculateAmount(minutes));
    }

    @Test
    void neverChargesMoreThanTheBaseRate() {
        // Cap so high it never bites: result must equal the base rate.
        Rate generous = new RateWithDailyCap(new HourlyRate(900), 10, 1_000_000);
        assertEquals(9000, generous.calculateAmount(600));
    }

    @Test
    void carMotorcycleAndCargoUseTheirOwnCaps() {
        long tenHours = 10 * 60;
        assertEquals(7000, new Car("A1", "b", "m", "Red").getRate().calculateAmount(tenHours));
        assertEquals(4000, new Motorcycle("A2", "b", "m", "Red").getRate().calculateAmount(tenHours));
        assertEquals(11000, new CargoVehicle("A3", "b", "m", "Red").getRate().calculateAmount(tenHours));
    }
}
