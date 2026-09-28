package parking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import parking.domain.exception.NoAvailableSpaceException;
import parking.domain.exception.VehicleWithActiveTicketException;
import parking.domain.model.Car;
import parking.domain.model.CargoVehicle;
import parking.domain.model.Motorcycle;
import parking.domain.model.ParkingSpace;
import parking.domain.model.ParkingTicket;
import parking.domain.model.SpaceStatus;
import parking.domain.model.SpaceType;
import parking.domain.model.TicketStatus;
import parking.domain.model.Vehicle;

/** Tests for {@link CheckInVehicleUseCase}. */
class CheckInVehicleUseCaseTest {

    private final UseCaseFixture f = new UseCaseFixture();

    @Test
    void carEntersAndGetsAnActiveTicketAndAnOccupiedSpace() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        ParkingTicket t = f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertEquals(TicketStatus.ACTIVE, t.getStatus());
        assertEquals(SpaceStatus.OCCUPIED, t.getSpace().getStatus());
        assertEquals(UseCaseFixture.ENTRY, t.getEntryTime());
    }

    @Test
    void motorcycleEnters() {
        f.registerSpace.execute(new ParkingSpace("M-1", SpaceType.MOTORCYCLE));
        ParkingTicket t = f.checkIn.execute(new Motorcycle("MOT111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertEquals("M-1", t.getSpace().getNumber());
    }

    @Test
    void cargoVehicleEnters() {
        f.registerSpace.execute(new ParkingSpace("C-1", SpaceType.CARGO));
        ParkingTicket t = f.checkIn.execute(new CargoVehicle("CAR111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertEquals("C-1", t.getSpace().getNumber());
    }

    @Test
    void automaticallyPicksTheCompatibleSpace() {
        f.registerSpace.execute(new ParkingSpace("M-1", SpaceType.MOTORCYCLE));
        f.registerSpace.execute(new ParkingSpace("C-1", SpaceType.CARGO));
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        ParkingTicket t = f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertEquals("A-1", t.getSpace().getNumber());
    }

    @Test
    void secondVehicleGetsADifferentSpace() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        f.registerSpace.execute(new ParkingSpace("A-2", SpaceType.CAR));
        ParkingTicket t1 = f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        ParkingTicket t2 = f.checkIn.execute(new Car("BBB222", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertNotEquals(t1.getSpace().getNumber(), t2.getSpace().getNumber());
    }

    @Test
    void ticketNumbersAreUnique() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        f.registerSpace.execute(new ParkingSpace("A-2", SpaceType.CAR));
        ParkingTicket t1 = f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        ParkingTicket t2 = f.checkIn.execute(new Car("BBB222", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertNotEquals(t1.getNumber(), t2.getNumber());
    }

    @Test
    void failsWhenThereAreNoSpacesAtAll() {
        assertThrows(NoAvailableSpaceException.class,
                () -> f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY));
    }

    @Test
    void failsWhenOnlyIncompatibleSpacesExist() {
        f.registerSpace.execute(new ParkingSpace("M-1", SpaceType.MOTORCYCLE));
        assertThrows(NoAvailableSpaceException.class,
                () -> f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY));
    }

    @Test
    void failsWhenTheOnlyCompatibleSpaceIsOccupied() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        assertThrows(NoAvailableSpaceException.class,
                () -> f.checkIn.execute(new Car("BBB222", "b", "m", "Red"), UseCaseFixture.ENTRY));
    }

    @Test
    void failsWhenTheOnlyCompatibleSpaceIsOutOfService() {
        ParkingSpace broken = new ParkingSpace("A-1", SpaceType.CAR);
        broken.markOutOfService();
        f.registerSpace.execute(broken);
        assertThrows(NoAvailableSpaceException.class,
                () -> f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY));
    }

    @Test
    void aVehicleCannotHaveTwoActiveTickets() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        f.registerSpace.execute(new ParkingSpace("A-2", SpaceType.CAR));
        Vehicle car = new Car("AAA111", "b", "m", "Red");
        f.checkIn.execute(car, UseCaseFixture.ENTRY);
        assertThrows(VehicleWithActiveTicketException.class,
                () -> f.checkIn.execute(car, UseCaseFixture.ENTRY));
    }

    @Test
    void aFailedCheckInDoesNotOccupyAnySpace() {
        f.registerSpace.execute(new ParkingSpace("M-1", SpaceType.MOTORCYCLE));
        try {
            f.checkIn.execute(new Car("AAA111", "b", "m", "Red"), UseCaseFixture.ENTRY);
        } catch (NoAvailableSpaceException expected) {
            // expected
        }
        assertEquals(SpaceStatus.AVAILABLE, f.spaces.findAll().get(0).getStatus());
        assertEquals(0, f.tickets.findAll().size());
    }
}
