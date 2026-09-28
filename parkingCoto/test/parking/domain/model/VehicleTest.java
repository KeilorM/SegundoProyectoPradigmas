package parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Unit tests for {@link Vehicle} and its subclasses (validation and polymorphism). */
class VehicleTest {

    // ---------- color validation ----------

    @ParameterizedTest
    @ValueSource(strings = {"Red", "red", "Azul Marino", "Café", "  Negro  "})
    void acceptsColorsMadeOfLettersOnly(String color) {
        assertDoesNotThrow(() -> new Car("ABC123", "Toyota", "Corolla", color));
    }

    @ParameterizedTest
    @ValueSource(strings = {"R3d", "123", "Red1", "1Red", "Red!", "Blue-Green"})
    void rejectsColorsWithNumbersOrSymbols(String color) {
        assertThrows(IllegalArgumentException.class,
                () -> new Car("ABC123", "Toyota", "Corolla", color));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void rejectsBlankOrMissingColor(String color) {
        assertThrows(IllegalArgumentException.class,
                () -> new Car("ABC123", "Toyota", "Corolla", color));
    }

    @Test
    void colorValidationAppliesToEverySubclass() {
        assertThrows(IllegalArgumentException.class, () -> new Motorcycle("M1", "Honda", "CB", "R3d"));
        assertThrows(IllegalArgumentException.class, () -> new CargoVehicle("C1", "Volvo", "FH", "R3d"));
    }

    @Test
    void trimsColor() {
        assertEquals("Red", new Car("ABC123", "Toyota", "Corolla", "  Red ").getColor());
    }

    // ---------- plate ----------

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void rejectsBlankPlate(String plate) {
        assertThrows(IllegalArgumentException.class, () -> new Car(plate, "Toyota", "Corolla", "Red"));
    }

    @Test
    void normalizesPlateToUpperCaseAndTrims() {
        assertEquals("ABC123", new Car("  abc123 ", "Toyota", "Corolla", "Red").getPlate());
    }

    @Test
    void vehiclesWithTheSamePlateAreEqual() {
        Vehicle a = new Car("ABC123", "Toyota", "Corolla", "Red");
        Vehicle b = new Car("abc123", "Mazda", "3", "Blue");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void vehiclesWithDifferentPlatesAreNotEqual() {
        assertNotEquals(new Car("AAA111", "T", "C", "Red"), new Car("BBB222", "T", "C", "Red"));
    }

    // ---------- polymorphism ----------

    @Test
    void eachSubclassRequiresItsOwnSpaceType() {
        Vehicle car = new Car("A1", "b", "m", "Red");
        Vehicle motorcycle = new Motorcycle("A2", "b", "m", "Red");
        Vehicle cargo = new CargoVehicle("A3", "b", "m", "Red");
        assertEquals(SpaceType.CAR, car.getCompatibleSpaceType());
        assertEquals(SpaceType.MOTORCYCLE, motorcycle.getCompatibleSpaceType());
        assertEquals(SpaceType.CARGO, cargo.getCompatibleSpaceType());
    }

    @Test
    void eachSubclassChargesItsOwnHourlyRate() {
        Vehicle car = new Car("A1", "b", "m", "Red");
        Vehicle motorcycle = new Motorcycle("A2", "b", "m", "Red");
        Vehicle cargo = new CargoVehicle("A3", "b", "m", "Red");
        assertEquals(900, car.getRate().calculateAmount(60));
        assertEquals(500, motorcycle.getRate().calculateAmount(60));
        assertEquals(1500, cargo.getRate().calculateAmount(60));
    }
}
