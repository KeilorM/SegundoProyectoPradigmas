/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parking.application.usecase;
import parking.domain.model.ParkingTicket;
import parking.domain.repository.ParkingTicketRepository;

import java.util.List;

import java.util.List;
/**
 *
 * @author Laboratorio_M
 */


/**
 * Lists every currently active ticket.
 */
public class ListActiveTicketsUseCase {

    private final ParkingTicketRepository ticketRepository;

    /**
     * @param ticketRepository port used to query active tickets
     */
    public ListActiveTicketsUseCase(ParkingTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * @return every ticket currently in {@code ACTIVE} status
     */
    public List<ParkingTicket> execute() {
        return ticketRepository.findActive();
    }
}

