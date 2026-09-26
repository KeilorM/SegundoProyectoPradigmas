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
 * Thrown when attempting to register an exit for a vehicle that has no
 * currently active ticket.
 */
public class TicketNotActiveException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param plate the plate of the vehicle with no active ticket
     */
    public TicketNotActiveException(String plate) {
        super("There is no active ticket for vehicle " + plate);
    }
}

