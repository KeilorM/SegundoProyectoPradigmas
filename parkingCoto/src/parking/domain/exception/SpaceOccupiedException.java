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
 * Thrown when attempting to assign a space that is already
 * {@code OCCUPIED} by another vehicle.
 */
public class SpaceOccupiedException extends SpaceNotAvailableException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param number the number of the already-occupied space
     */
    public SpaceOccupiedException(String number) {
        super("Space " + number + " is already occupied");
    }
}

