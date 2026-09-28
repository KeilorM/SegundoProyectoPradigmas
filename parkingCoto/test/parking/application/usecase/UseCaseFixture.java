package parking.application.usecase;

import parking.domain.repository.ParkingSpaceRepository;
import parking.domain.repository.ParkingTicketRepository;
import parking.domain.repository.PaymentRepository;
import parking.domain.repository.VehicleRepository;
import parking.infrastructure.repository.InMemoryParkingSpaceRepository;
import parking.infrastructure.repository.InMemoryParkingTicketRepository;
import parking.infrastructure.repository.InMemoryPaymentRepository;
import parking.infrastructure.repository.InMemoryVehicleRepository;

import java.time.LocalDateTime;

/**
 * Shared test fixture: fresh in-memory repositories and the use cases
 * wired to them, so every test starts from a clean state.
 */
class UseCaseFixture {

    static final LocalDateTime ENTRY = LocalDateTime.of(2026, 9, 16, 8, 0);

    final VehicleRepository vehicles = new InMemoryVehicleRepository();
    final ParkingSpaceRepository spaces = new InMemoryParkingSpaceRepository();
    final ParkingTicketRepository tickets = new InMemoryParkingTicketRepository();
    final PaymentRepository payments = new InMemoryPaymentRepository();

    final RegisterVehicleUseCase registerVehicle = new RegisterVehicleUseCase(vehicles);
    final RegisterParkingSpaceUseCase registerSpace = new RegisterParkingSpaceUseCase(spaces);
    final CheckInVehicleUseCase checkIn = new CheckInVehicleUseCase(spaces, tickets);
    final CheckOutVehicleUseCase checkOut = new CheckOutVehicleUseCase(tickets);
    final RegisterPaymentUseCase registerPayment = new RegisterPaymentUseCase(payments);
    final ValidateSpaceAssignmentUseCase validateAssignment = new ValidateSpaceAssignmentUseCase();
    final ListAvailableSpacesUseCase listAvailableSpaces = new ListAvailableSpacesUseCase(spaces);
    final ListActiveTicketsUseCase listActiveTickets = new ListActiveTicketsUseCase(tickets);
    final ListVehiclesInsideUseCase listVehiclesInside = new ListVehiclesInsideUseCase(tickets);
    final GetOccupancyByTypeUseCase getOccupancy = new GetOccupancyByTypeUseCase(spaces);
    final GetTotalRevenueUseCase getRevenue = new GetTotalRevenueUseCase(payments);
}
