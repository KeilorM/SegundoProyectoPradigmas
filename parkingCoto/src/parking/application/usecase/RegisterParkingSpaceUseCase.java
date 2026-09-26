/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parking.application.usecase;
import parking.domain.model.ParkingSpace;
import parking.domain.repository.ParkingSpaceRepository;
/**
 *
 * @author Laboratorio_M
 */


/**
 * Registers a new physical space in the parking lot.
 */
public class RegisterParkingSpaceUseCase {

    private final ParkingSpaceRepository spaceRepository;

    /**
     * Creates the use case.
     *
     * @param spaceRepository port used to persist spaces
     */
    public RegisterParkingSpaceUseCase(ParkingSpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    /**
     * Registers the given space.
     *
     * @param space the space to register
     */
    public void execute(ParkingSpace space) {
        spaceRepository.save(space);
    }
}

