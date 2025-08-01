package com.frederic.clienttra.utils;

import com.frederic.clienttra.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility service for calculations and formatting related to documents,
 * such as generating payment notes, calculating totals, and deadlines.
 */
@Service
@RequiredArgsConstructor
public class DocumentUtils {

    /**
     * Generates a payment note string indicating the expected payment date,
     * payment method, and bank account details if applicable.
     *
     * @param docDate     The date of the document.
     * @param customer    The customer entity containing payment terms and method.
     * @param bankAccount The bank account entity, used if payment method is transfer.
     * @return A string with the payment note, or null if docDate or customer is null.
     */
    public String generateNotePayment(LocalDate docDate, Customer customer, BankAccount bankAccount) {
        if (customer == null || docDate == null) {
            return null;
        }

        LocalDate paymentDate = docDate.plusDays(customer.getDuedate() != null ? customer.getDuedate() : 0);
        StringBuilder note = new StringBuilder("Pago previsto el ").append(paymentDate);
        // TODO: Soportar traducción del texto

        String payMethod = customer.getPayMethod();
        if (payMethod != null) {
            note.append(" por ").append(payMethod);
            if ("TRANSFERENCIA".equalsIgnoreCase(payMethod) && bankAccount != null) {
                note.append(" (")
                        .append("IBAN: ").append(bankAccount.getIban() != null ? bankAccount.getIban() : "N/A").append(", ")
                        .append("Titular: ").append(bankAccount.getHolder() != null ? bankAccount.getHolder() : "N/A").append(", ")
                        .append("Sucursal: ").append(bankAccount.getBranch() != null ? bankAccount.getBranch() : "N/A")
                        .append(")");
            }
        }

        return note.toString();
    }

    /**
     * Calculates and sets the total amounts (net, VAT, withholding, gross, and to pay)
     * on the given document based on its associated orders and tax rates.
     *
     * @param document The document entity whose totals are to be calculated.
     */
    public void calculateTotals(Document document){
        double totalNet = 0.0;
        double totalVat = 0.0;
        double totalWithholding = 0.0;
        double totalGross = 0.0;
        double totalToPay = 0.0;
        List<Order> orders = document.getOrders();

        for (Order order : orders) {
            totalNet += order.getTotal();
        }

        totalVat = totalNet * document.getVatRate();
        totalWithholding = totalNet * document.getWithholding();
        totalGross = totalNet + totalVat;
        totalToPay = totalGross - totalWithholding;

        document.setTotalNet(totalNet);
        document.setTotalVat(totalVat);
        document.setTotalWithholding(totalWithholding);
        document.setTotalGross(totalGross);
        document.setTotalToPay(totalToPay);
    }

    /**
     * Calculates the total gross amount converted to the secondary currency,
     * using the document's change rate.
     *
     * @param document The document entity.
     * @return The total gross amount in the second currency, or null if no conversion needed.
     */
    public Double calculateTotalGrossInCurrency2(Document document) {
        // TODO: Evaluar uso y lugar de esta conversión
        if (document.getChangeRate() == null || document.getChangeRate().getIdChangeRate() == 1) {
            return null;
        }
        Double rate = document.getChangeRate().getRate();
        return rate != null ? document.getTotalGross() * rate : null;
    }

    /**
     * Calculates the total amount to pay converted to the secondary currency,
     * using the document's change rate.
     *
     * @param document The document entity.
     * @return The total to pay amount in the second currency, or null if no conversion needed.
     */
    public Double calculateTotalToPayInCurrency2(Document document) {
        if (document.getChangeRate() == null || document.getChangeRate().getIdChangeRate() == 1) {
            return null;
        }
        Double rate = document.getChangeRate().getRate();
        return rate != null ? document.getTotalToPay() * rate : null;
    }

    /**
     * Calculates the payment deadline date by adding a delay in days to the document date.
     *
     * @param docDate The document date.
     * @param delay   The delay in days to add; if null, returns docDate unchanged.
     * @return The calculated deadline date.
     */
    public LocalDate calculateDeadline(LocalDate docDate, Integer delay){
        LocalDate deadline;
        if(delay != null) {
            deadline = docDate.plusDays(delay);
        } else {
            deadline = docDate;
        }

        return deadline;
    }
}
