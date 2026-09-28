package parking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.domain.exception.BusinessException;
import parking.domain.exception.TicketStillActiveException;
import parking.domain.model.Car;
import parking.domain.model.ParkingSpace;
import parking.domain.model.ParkingTicket;
import parking.domain.model.Payment;
import parking.domain.model.PaymentType;
import parking.domain.model.SpaceType;
import parking.domain.model.TicketStatus;

/** Tests for {@link RegisterPaymentUseCase}. */
class RegisterPaymentUseCaseTest {

    private final UseCaseFixture f = new UseCaseFixture();
    private ParkingTicket ticket;

    @BeforeEach
    void setUp() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        ticket = f.checkIn.execute(new Car("ABC123", "b", "m", "Red"), UseCaseFixture.ENTRY);
    }

    @Test
    void cannotPayAnActiveTicket() {
        assertThrows(TicketStillActiveException.class,
                () -> f.registerPayment.execute(ticket, PaymentType.CASH, UseCaseFixture.ENTRY));
        assertEquals(0, f.payments.findAll().size());
    }

    @Test
    void paysAClosedTicket() {
        f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(1));
        Payment p = f.registerPayment.execute(ticket, PaymentType.CARD, UseCaseFixture.ENTRY.plusHours(1));
        assertEquals(TicketStatus.PAID, ticket.getStatus());
        assertEquals(ticket.getAmount(), p.getAmount());
        assertEquals(PaymentType.CARD, p.getPaymentType());
        assertEquals(1, f.payments.findAll().size());
    }

    @Test
    void cannotPayTheSameTicketTwice() {
        f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(1));
        f.registerPayment.execute(ticket, PaymentType.CASH, UseCaseFixture.ENTRY.plusHours(1));
        assertThrows(BusinessException.class,
                () -> f.registerPayment.execute(ticket, PaymentType.CASH, UseCaseFixture.ENTRY.plusHours(2)));
        assertEquals(1, f.payments.findAll().size());
    }

    @Test
    void acceptsEveryPaymentType() {
        f.registerSpace.execute(new ParkingSpace("A-2", SpaceType.CAR));
        f.registerSpace.execute(new ParkingSpace("A-3", SpaceType.CAR));
        ParkingTicket t2 = f.checkIn.execute(new Car("BBB222", "b", "m", "Red"), UseCaseFixture.ENTRY);
        ParkingTicket t3 = f.checkIn.execute(new Car("CCC333", "b", "m", "Red"), UseCaseFixture.ENTRY);
        f.checkOut.execute("ABC123", UseCaseFixture.ENTRY.plusHours(1));
        f.checkOut.execute("BBB222", UseCaseFixture.ENTRY.plusHours(1));
        f.checkOut.execute("CCC333", UseCaseFixture.ENTRY.plusHours(1));
        Payment a = f.registerPayment.execute(ticket, PaymentType.CASH, UseCaseFixture.ENTRY);
        Payment b = f.registerPayment.execute(t2, PaymentType.CARD, UseCaseFixture.ENTRY);
        Payment c = f.registerPayment.execute(t3, PaymentType.SINPE_MOVIL, UseCaseFixture.ENTRY);
        assertNotEquals(a.getId(), b.getId());
        assertNotEquals(b.getId(), c.getId());
        assertEquals(PaymentType.SINPE_MOVIL, c.getPaymentType());
    }
}
