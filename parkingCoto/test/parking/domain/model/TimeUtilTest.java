package parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Unit tests for {@link TimeUtil}: any fraction of an hour bills as a full hour. */
class TimeUtilTest {

    @ParameterizedTest(name = "{0} minutes -> {1} billable hours")
    @CsvSource({
            "-5, 0",
            "0, 0",
            "1, 1",
            "35, 1",
            "60, 1",
            "61, 2",
            "70, 2",
            "120, 2",
            "121, 3",
            "180, 3",
            "1440, 24"
    })
    void billableHoursRoundsUpToFullHours(long minutes, long expectedHours) {
        assertEquals(expectedHours, TimeUtil.billableHours(minutes));
    }
}
