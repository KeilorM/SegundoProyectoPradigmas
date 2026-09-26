/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parking.application.usecase;
import parking.domain.model.Payment;
import parking.domain.repository.PaymentRepository;
/**
 *
 * @author Laboratorio_M
 */


/**
 * Computes the total revenue collected across every registered payment.
 */
public class GetTotalRevenueUseCase {

    private final PaymentRepository paymentRepository;

    /**
     * @param paymentRepository port used to query every registered payment
     */
    public GetTotalRevenueUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * @return the sum of the amounts of every registered payment
     */
    public long execute() {
        return paymentRepository.findAll().stream()
                .mapToLong(Payment::getAmount)
                .sum();
    }
}

