package parking.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import parking.domain.model.Car;
import parking.domain.model.ParkingSpace;
import parking.domain.model.ParkingTicket;
import parking.domain.model.SpaceType;

/** Tests for the in-memory repository adapters. */
class InMemoryRepositoriesTest {

    @Test
    void ticketNumbersIncreaseFromOne() {
        InMemoryParkingTicketRepository repo = new InMemoryParkingTicketRepository();
        assertEquals(1, repo.nextTicketNumber());
        assertEquals(2, repo.nextTicketNumber());
    }

    @Test
    void savingTheSameTicketTwiceStoresItOnce() {
        InMemoryParkingTicketRepository repo = new InMemoryParkingTicketRepository();
        ParkingTicket t = new ParkingTicket(1, new Car("ABC123", "b", "m", "Red"),
                new ParkingSpace("A-1", SpaceType.CAR), LocalDateTime.now());
        repo.save(t);
        repo.save(t);
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void findsTicketsByNumberAndActiveTicketsByPlate() {
        InMemoryParkingTicketRepository repo = new InMemoryParkingTicketRepository();
        ParkingTicket t = new ParkingTicket(7, new Car("ABC123", "b", "m", "Red"),
                new ParkingSpace("A-1", SpaceType.CAR), LocalDateTime.now());
        repo.save(t);
        assertTrue(repo.findByNumber(7).isPresent());
        assertFalse(repo.findByNumber(8).isPresent());
        assertTrue(repo.findActiveByPlate("abc123").isPresent());
        t.close(LocalDateTime.now().plusHours(1));
        assertFalse(repo.findActiveByPlate("ABC123").isPresent());
    }

    @Test
    void paymentIdsIncreaseFromOne() {
        InMemoryPaymentRepository repo = new InMemoryPaymentRepository();
        assertEquals(1, repo.nextPaymentId());
        assertEquals(2, repo.nextPaymentId());
    }

    @Test
    void vehicleLookupByPlateIsCaseInsensitive() {
        InMemoryVehicleRepository repo = new InMemoryVehicleRepository();
        repo.save(new Car("ABC123", "b", "m", "Red"));
        assertTrue(repo.existsByPlate("abc123"));
        assertFalse(repo.existsByPlate("ZZZ999"));
    }

    @Test
    void availableSpacesAreFilteredByTypeAndStatus() {
        InMemoryParkingSpaceRepository repo = new InMemoryParkingSpaceRepository();
        ParkingSpace free = new ParkingSpace("A-1", SpaceType.CAR);
        ParkingSpace taken = new ParkingSpace("A-2", SpaceType.CAR);
        taken.occupy();
        repo.save(free);
        repo.save(taken);
        repo.save(new ParkingSpace("M-1", SpaceType.MOTORCYCLE));
        assertEquals(1, repo.findAvailableByType(SpaceType.CAR).size());
    }
}
