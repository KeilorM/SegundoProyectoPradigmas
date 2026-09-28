package parking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.domain.model.Car;
import parking.domain.model.Motorcycle;
import parking.domain.model.ParkingSpace;
import parking.domain.model.ParkingTicket;
import parking.domain.model.PaymentType;
import parking.domain.model.SpaceType;
import parking.domain.model.Vehicle;

/** Tests for the read-only query use cases (spaces, tickets, occupancy, revenue). */
class QueryUseCasesTest {

    private final UseCaseFixture f = new UseCaseFixture();

    @BeforeEach
    void setUp() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        f.registerSpace.execute(new ParkingSpace("A-2", SpaceType.CAR));
        f.registerSpace.execute(new ParkingSpace("M-1", SpaceType.MOTORCYCLE));
        ParkingSpace broken = new ParkingSpace("M-2", SpaceType.MOTORCYCLE);
        broken.markOutOfService();
        f.registerSpace.execute(broken);
    }

    @Test
    void listsOnlyAvailableSpacesOfTheRequestedType() {
        assertEquals(2, f.listAvailableSpaces.execute(SpaceType.CAR).size());
        assertEquals(1, f.listAvailableSpaces.execute(SpaceType.MOTORCYCLE).size()); // M-2 is out of service
        assertTrue(f.listAvailableSpaces.execute(SpaceType.CARGO).isEmpty());
    }

    @Test
    void availableSpacesShrinkAfterCheckIn() {
        f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertEquals(1, f.listAvailableSpaces.execute(SpaceType.CAR).size());
    }

    @Test
    void listsActiveTicketsAndVehiclesInside() {
        Vehicle car = new Car("AAA111", "b", "m", "Red");
        Vehicle moto = new Motorcycle("MOT111", "b", "m", "Red");
        f.checkIn.execute(car, UseCaseFixture.ENTRY);
        f.checkIn.execute(moto, UseCaseFixture.ENTRY);
        assertEquals(2, f.listActiveTickets.execute().size());
        List<Vehicle> inside = f.listVehiclesInside.execute();
        assertTrue(inside.contains(car) && inside.contains(moto));

        f.checkOut.execute("AAA111", UseCaseFixture.ENTRY.plusHours(1));
        assertEquals(1, f.listActiveTickets.execute().size());
        assertEquals(List.of(moto), f.listVehiclesInside.execute());
    }

    @Test
    void occupancyCountsByTypeAndStatus() {
        f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        Map<SpaceType, long[]> occupancy = f.getOccupancy.execute();
        assertArrayEquals(new long[]{1, 1, 0}, occupancy.get(SpaceType.CAR));          // available, occupied, out
        assertArrayEquals(new long[]{1, 0, 1}, occupancy.get(SpaceType.MOTORCYCLE));
        assertArrayEquals(new long[]{0, 0, 0}, occupancy.get(SpaceType.CARGO));
    }

    @Test
    void revenueIsZeroWithoutPayments() {
        assertEquals(0, f.getRevenue.execute());
    }

    @Test
    void revenueSumsAllPayments() {
        ParkingTicket t1 = f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        ParkingTicket t2 = f.checkIn.execute(new Motorcycle("MOT111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        f.checkOut.execute("AAA111", UseCaseFixture.ENTRY.plusHours(1));   // 900
        f.checkOut.execute("MOT111", UseCaseFixture.ENTRY.plusHours(1));   // 500
        f.registerPayment.execute(t1, PaymentType.CARD, UseCaseFixture.ENTRY);
        f.registerPayment.execute(t2, PaymentType.CASH, UseCaseFixture.ENTRY);
        assertEquals(1400, f.getRevenue.execute());
    }

    @Test
    void unpaidClosedTicketsDoNotCountAsRevenue() {
        f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        f.checkOut.execute("AAA111", UseCaseFixture.ENTRY.plusHours(1));
        assertEquals(0, f.getRevenue.execute());
    }
}
