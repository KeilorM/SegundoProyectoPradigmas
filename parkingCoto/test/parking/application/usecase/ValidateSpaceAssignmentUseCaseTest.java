package parking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import parking.domain.exception.IncompatibleSpaceException;
import parking.domain.exception.SpaceNotAvailableException;
import parking.domain.exception.SpaceOccupiedException;
import parking.domain.exception.SpaceOutOfServiceException;
import parking.domain.model.Car;
import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceType;
import parking.domain.model.Vehicle;

/** Tests for {@link ValidateSpaceAssignmentUseCase}: one test per assignment rule. */
class ValidateSpaceAssignmentUseCaseTest {

    private final ValidateSpaceAssignmentUseCase validate = new ValidateSpaceAssignmentUseCase();
    private final Vehicle car = new Car("ABC123", "b", "m", "Red");

    @Test
    void acceptsAnAvailableCompatibleSpace() {
        assertDoesNotThrow(() -> validate.execute(new ParkingSpace("A-1", SpaceType.CAR), car));
    }

    @Test
    void rejectsAnOccupiedSpace() {
        ParkingSpace space = new ParkingSpace("A-1", SpaceType.CAR);
        space.occupy();
        assertThrows(SpaceOccupiedException.class, () -> validate.execute(space, car));
    }

    @Test
    void rejectsAnOutOfServiceSpace() {
        ParkingSpace space = new ParkingSpace("A-1", SpaceType.CAR);
        space.markOutOfService();
        assertThrows(SpaceOutOfServiceException.class, () -> validate.execute(space, car));
    }

    @Test
    void rejectsAnIncompatibleSpace() {
        ParkingSpace space = new ParkingSpace("M-1", SpaceType.MOTORCYCLE);
        assertThrows(IncompatibleSpaceException.class, () -> validate.execute(space, car));
    }

    @Test
    void allSpecificExceptionsShareTheSpaceNotAvailableRoot() {
        ParkingSpace space = new ParkingSpace("M-1", SpaceType.MOTORCYCLE);
        assertThrows(SpaceNotAvailableException.class, () -> validate.execute(space, car));
    }
}
