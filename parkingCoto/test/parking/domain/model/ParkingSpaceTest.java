package parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link ParkingSpace}. */
class ParkingSpaceTest {

    private final ParkingSpace space = new ParkingSpace("A-1", SpaceType.CAR);

    @Test
    void startsAvailable() {
        assertEquals(SpaceStatus.AVAILABLE, space.getStatus());
        assertTrue(space.isAvailable());
    }

    @Test
    void occupyMarksItOccupied() {
        space.occupy();
        assertEquals(SpaceStatus.OCCUPIED, space.getStatus());
        assertFalse(space.isAvailable());
    }

    @Test
    void cannotOccupyAnOccupiedSpace() {
        space.occupy();
        assertThrows(IllegalStateException.class, space::occupy);
    }

    @Test
    void cannotOccupyAnOutOfServiceSpace() {
        space.markOutOfService();
        assertThrows(IllegalStateException.class, space::occupy);
    }

    @Test
    void releaseMakesItAvailableAgain() {
        space.occupy();
        space.release();
        assertEquals(SpaceStatus.AVAILABLE, space.getStatus());
    }

    @Test
    void cannotReleaseAnOutOfServiceSpace() {
        space.markOutOfService();
        assertThrows(IllegalStateException.class, space::release);
    }

    @Test
    void isCompatibleOnlyWithItsOwnType() {
        assertTrue(space.isCompatibleWith(new Car("A1", "b", "m", "Red")));
        assertFalse(space.isCompatibleWith(new Motorcycle("A2", "b", "m", "Red")));
        assertFalse(space.isCompatibleWith(new CargoVehicle("A3", "b", "m", "Red")));
    }
}
