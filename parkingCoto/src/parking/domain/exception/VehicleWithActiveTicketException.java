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
 * Thrown when a vehicle attempts to check in while it already has an
 * {@code ACTIVE} ticket open (assignment rule: "a vehicle cannot have
 * two active tickets").
 */
public class VehicleWithActiveTicketException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param plate the plate of the vehicle that already has an active ticket
     */
    public VehicleWithActiveTicketException(String plate) {
        super("Vehicle " + plate + " already has an active ticket");
    }
}

