package com.frederic.clienttra.utils;

import com.frederic.clienttra.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public void calculateTotals(Document document){
        double totalNet=0.0;
        double totalVat=0.0;
        double totalWithholding=0.0;
        double totalGross=0.0;
        double totalToPay=0.0;
        List<Order>orders=document.getOrders();

        for(Order order : orders){
            totalNet=totalNet+order.getTotal();
        }

        totalVat=totalNet*document.getVatRate();
        totalWithholding=totalNet*document.getWithholding();
        totalGross=totalNet+totalVat;
        totalToPay=totalGross-totalWithholding;
        document.setTotalNet(totalNet);
        document.setTotalVat(totalVat);
        document.setTotalWithholding(totalWithholding);
        document.setTotalGross(totalGross);
        document.setTotalToPay(totalToPay);
    }

    public Double caculateTotalNet(List<Order> orders){
        double totalNet=0.0;
        for(Order order : orders){
            totalNet=totalNet+ order.getTotal();
        }
        return totalNet;
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

    public LocalDate calculateDeadline(LocalDate docDate, Integer delay){//TODO implementar método
        LocalDate deadline;
        if(delay != null) {
            deadline = docDate.plusDays(delay);
        }
        else{
            deadline=docDate;
        }

        return deadline;
    }
}

