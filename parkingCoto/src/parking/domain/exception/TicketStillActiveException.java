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
 * Thrown when attempting to pay a ticket that has not been closed yet
 * (business rule: "an active ticket cannot be paid").
 */
public class TicketStillActiveException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param ticketNumber the number of the still-active ticket
     */
    public TicketStillActiveException(int ticketNumber) {
        super("Ticket #" + ticketNumber + " is still active, it cannot be paid");
    }
}

