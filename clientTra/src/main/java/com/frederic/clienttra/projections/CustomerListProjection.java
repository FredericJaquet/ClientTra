package com.frederic.clienttra.projections;

/**
 * Projection interface for listing customers with selected fields.
 * Utilized to retrieve lightweight customer data for list views.
 */
public interface CustomerListProjection {
    Integer getIdCustomer();
    String getComName();
    String getVatNumber();
    String getEmail();
    String getWeb();
    Boolean getEnabled();
}