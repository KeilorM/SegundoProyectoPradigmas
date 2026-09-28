package parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link HourlyRate}. */
class HourlyRateTest {

    @Test
    void chargesValuePerBillableHour() {
        Rate rate = new HourlyRate(900);
        assertEquals(900, rate.calculateAmount(1));
        assertEquals(900, rate.calculateAmount(60));
        assertEquals(1800, rate.calculateAmount(61));
        assertEquals(2700, rate.calculateAmount(180));
    }

    @Test
    void zeroMinutesCostNothing() {
        assertEquals(0, new HourlyRate(900).calculateAmount(0));
    }

    @Test
    void exposesConfiguredValue() {
        assertEquals(500, new HourlyRate(500).getValuePerHour());
    }

    @Test
    void rejectsNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new HourlyRate(-1));
    }
}
