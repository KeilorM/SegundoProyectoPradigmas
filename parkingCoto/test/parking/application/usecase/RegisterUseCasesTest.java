package parking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import parking.domain.exception.BusinessException;
import parking.domain.model.Car;
import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceType;

/** Tests for registering vehicles and spaces. */
class RegisterUseCasesTest {

    private final UseCaseFixture f = new UseCaseFixture();

    @Test
    void registersAVehicle() {
        f.registerVehicle.execute(new Car("ABC123", "Toyota", "Corolla", "Red"));
        assertEquals(1, f.vehicles.findAll().size());
        assertTrue(f.vehicles.existsByPlate("ABC123"));
    }

    @Test
    void rejectsADuplicatePlate() {
        f.registerVehicle.execute(new Car("ABC123", "Toyota", "Corolla", "Red"));
        assertThrows(BusinessException.class,
                () -> f.registerVehicle.execute(new Car("abc123", "Mazda", "3", "Blue")));
        assertEquals(1, f.vehicles.findAll().size());
    }

    @Test
    void registersASpace() {
        f.registerSpace.execute(new ParkingSpace("A-1", SpaceType.CAR));
        assertEquals(1, f.spaces.findAll().size());
    }
}
