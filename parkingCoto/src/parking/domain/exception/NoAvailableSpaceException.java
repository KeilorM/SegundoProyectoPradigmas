/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parking.domain.exception;
import parking.domain.model.SpaceType;

/**
 *
 * @author Laboratorio_M
 */

/**
 * Thrown when there is no available, compatible space to assign to an
 * entering vehicle.
 */
public class NoAvailableSpaceException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * @param type the space type that has no available spaces
     */
    public NoAvailableSpaceException(SpaceType type) {
        super("No available spaces for type " + type);
    }
}

