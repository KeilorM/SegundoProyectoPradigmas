/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parking.domain.exception;

/**
 *
 * @author Laboratorio_M
 */

/**
 * Thrown when attempting to assign a space whose {@code SpaceType} does
 * not match the vehicle's required type.
 */
public class IncompatibleSpaceException extends SpaceNotAvailableException {

    private static final long serialVersionUID = 1L;

    /**
     * @param number the number of the incompatible space
     * @param plate  the plate of the vehicle that was rejected
     */
    public IncompatibleSpaceException(String number, String plate) {
        super("Space " + number + " is not compatible with vehicle " + plate);
    }
}

