/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parking.application.usecase;
import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceType;
import parking.domain.repository.ParkingSpaceRepository;
import java.util.List;

/**
 *
 * @author Laboratorio_M
 */


/**
 * Lists every currently available space of a given type.
 */
public class ListAvailableSpacesUseCase {

    private final ParkingSpaceRepository spaceRepository;

    /**
     * @param spaceRepository port used to query spaces
     */
    public ListAvailableSpacesUseCase(ParkingSpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    /**
     * @param type the space type to filter by
     * @return the available spaces of that type
     */
    public List<ParkingSpace> execute(SpaceType type) {
        return spaceRepository.findAvailableByType(type);
    }
}

