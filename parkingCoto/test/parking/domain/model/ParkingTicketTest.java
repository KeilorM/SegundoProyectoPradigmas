package parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ParkingTicket} and its ACTIVE -> CLOSED -> PAID lifecycle. */
class ParkingTicketTest {

    private static final LocalDateTime ENTRY = LocalDateTime.of(2026, 9, 16, 8, 0);

    private ParkingTicket ticket;

    @BeforeEach
    void setUp() {
        ticket = new ParkingTicket(1, new Car("ABC123", "Toyota", "Corolla", "Red"),
                new ParkingSpace("A-1", SpaceType.CAR), ENTRY);
    }

    @Test
    void newTicketIsActiveWithNoAmountAndNoExit() {
        assertEquals(TicketStatus.ACTIVE, ticket.getStatus());
        assertEquals(0, ticket.getAmount());
        assertNull(ticket.getExitTime());
    }

    @Test
    void closeComputesAmountFromTheVehiclesRate() {
        long amount = ticket.close(ENTRY.plusMinutes(90)); // 2 billable hours * 900
        assertEquals(1800, amount);
        assertEquals(1800, ticket.getAmount());
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
        assertEquals(ENTRY.plusMinutes(90), ticket.getExitTime());
    }

    @Test
    void closeAppliesTheDailyCap() {
        assertEquals(7000, ticket.close(ENTRY.plusHours(11)));
    }

    @Test
    void cannotCloseTwice() {
        ticket.close(ENTRY.plusHours(1));
        assertThrows(IllegalStateException.class, () -> ticket.close(ENTRY.plusHours(2)));
    }

    @Test
    void cannotPayAnActiveTicket() {
        assertThrows(IllegalStateException.class, ticket::markAsPaid);
    }

    @Test
    void closedTicketCanBePaidOnce() {
        ticket.close(ENTRY.plusHours(1));
        ticket.markAsPaid();
        assertEquals(TicketStatus.PAID, ticket.getStatus());
        assertThrows(IllegalStateException.class, ticket::markAsPaid);
    }

    @Test
    void stayInMinutesIsMeasuredUntilExit() {
        ticket.close(ENTRY.plusMinutes(75));
        assertEquals(75, ticket.getStayInMinutes());
    }
}
