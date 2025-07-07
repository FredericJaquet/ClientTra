package com.frederic.clienttra.utils;

import com.frederic.clienttra.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DocumentUtils {

    public String generateNotePayment(LocalDate docDate, Customer customer, BankAccount bankAccount) {
        if (customer == null || docDate == null) {
            return null;
        }

        LocalDate paymentDate = docDate.plusDays(customer.getDuedate() != null ? customer.getDuedate() : 0);
        StringBuilder note = new StringBuilder("Pago previsto el ").append(paymentDate);//TODO Poder traducir el text tipo Fecha de pago / Date de payement / Duedate...

        String payMethod = customer.getPayMethod();
           if (payMethod != null) {
            note.append(" por ").append(payMethod);
            if ("TRANSFERENCIA".equalsIgnoreCase(payMethod) && bankAccount != null) { //TODO Ver como enviar el método de pago (Transfer, platform, ...)
                note.append(" (")
                    .append("IBAN: ").append(bankAccount.getIban() != null ? bankAccount.getIban() : "N/A").append(", ")
                    .append("Titular: ").append(bankAccount.getHolder() != null ? bankAccount.getHolder() : "N/A").append(", ")
                    .append("Sucursal: ").append(bankAccount.getBranch() != null ? bankAccount.getBranch() : "N/A")
                    .append(")");
            }
        }

        return note.toString();
    }

    public Double calculateTotalGrossInCurrency2(Document document) {
        if (document.getChangeRate() == null || document.getChangeRate().getIdChangeRate() == 1) {
            return null;
        }
        Double rate = document.getChangeRate().getRate();
        return rate != null ? document.getTotalGross() * rate : null;
    }

    public Double calculateTotalToPayInCurrency2(Document document) {
        if (document.getChangeRate() == null || document.getChangeRate().getIdChangeRate() == 1) {
            return null;
        }
        Double rate = document.getChangeRate().getRate();
        return rate != null ? document.getTotalToPay() * rate : null;
    }
}

