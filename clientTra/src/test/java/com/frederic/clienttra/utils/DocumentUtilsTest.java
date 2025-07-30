package com.frederic.clienttra.utils;

import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentUtilsTest {

    @InjectMocks
    private DocumentUtils documentUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Order> baseOrders(){
        Order order1 = Order.builder()
                .total(25.0)
                .build();
        Order order2 = Order.builder()
                .total(50.0)
                .build();
        Order order3 = Order.builder()
                .total(30.0)
                .build();

        return Arrays.asList(order1, order2, order3);
    }

    private Document baseDocument(){
        return Document.builder()
                .vatRate(0.21)
                .withholding(0.15)
                .totalNet(0.0)
                .totalVat(0.0)
                .totalWithholding(0.0)
                .totalGross(0.0)
                .totalToPay(0.0)
                .orders(baseOrders())
                .build();
    }

    @Test
    void calculateTotals_ShouldCalculateCorrectly(){
        Document document = baseDocument();
        double totalNetExpected = 105.0;
        double totalVatExpected = 22.05;
        double totalWithholdingExpected = 15.75;
        double totalGrossExpected = 127.05;
        double totalToPayExpected = 111.30;

        documentUtils.calculateTotals(document);

        assertThat(document.getTotalNet()).isEqualTo(totalNetExpected);
        assertThat(document.getTotalVat()).isEqualTo(totalVatExpected);
        assertThat(document.getTotalWithholding()).isEqualTo(totalWithholdingExpected);
        assertThat(document.getTotalGross()).isEqualTo(totalGrossExpected);
        assertThat(document.getTotalToPay()).isEqualTo(totalToPayExpected);
    }
}
