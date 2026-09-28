package parking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.domain.exception.TicketNotActiveException;
import parking.domain.model.Car;
import parking.domain.model.ParkingSpace;
import parking.domain.model.ParkingTicket;
import parking.domain.model.SpaceStatus;
import parking.domain.model.SpaceType;
import parking.domain.model.TicketStatus;

/** Tests for {@link CheckOutVehicleUseCase}. */
class CheckOutVehicleUseCaseTest {

    private final UseCaseFixture f = new UseCaseFixture();
    private ParkingSpace space;
    private ParkingTicket ticket;

    @BeforeEach
    void setUp() {
        space = new ParkingSpace("A-1", SpaceType.CAR);
        f.registerSpace.execute(space);
        ticket = f.checkIn.execute(new Car("ABC123", "b", "m", "Red"), UseCaseFixture.ENTRY);
    }

    @Test
    void closesTheTicketAndComputesTheAmount() {
        ParkingTicket closed = f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusMinutes(90));
        assertEquals(TicketStatus.CLOSED, closed.getStatus());
        assertEquals(1800, closed.getAmount());
    }

    @Test
    void releasesTheSpace() {
        f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(1));
        assertEquals(SpaceStatus.AVAILABLE, space.getStatus());
    }

    @Test
    void appliesTheDailyCapOnLongStays() {
        assertEquals(7000, f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(11)).getAmount());
    }

    @Test
    void plateLookupIgnoresCase() {
        f.checkOut.execute("abc123", UseCaseFixture.ENTRY.plusHours(1));
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
    }

    @Test
    void cannotExitWithoutAnActiveTicket() {
        assertThrows(TicketNotActiveException.class,
                () -> f.checkOut.execute("ZZZ999", UseCaseFixture.ENTRY.plusHours(1)));
    }

    @Test
    void cannotExitTwice() {
        f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(1));
        assertThrows(TicketNotActiveException.class,
                () -> f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(2)));
    }

    @Test
    void vehicleCanReenterAfterLeaving() {
        f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(1));
        ParkingTicket again = f.checkIn.execute(new Car("ABC123", "b", "m", "Red"),
                UseCaseFixture.ENTRY.plusHours(2));
        assertEquals(TicketStatus.ACTIVE, again.getStatus());
    }
}
