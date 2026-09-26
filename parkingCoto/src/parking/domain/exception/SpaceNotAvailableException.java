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
 * Root exception for the cases where one specific, already-identified
 * space cannot be assigned to a vehicle. See the more specific
 * subclasses {@link SpaceOccupiedException},
 * {@link SpaceOutOfServiceException} and {@link IncompatibleSpaceException}.
 */
public class SpaceNotAvailableException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message human-readable description of why the space cannot be assigned
     */
    public SpaceNotAvailableException(String message) {
        super(message);
    }
}

