package com.frederic.clienttra.projections;

public interface CustomerListProjection {
    Integer getIdCustomer();
    String getComName();
    String getVatNumber();
    String getEmail();
    String getWeb();
    Boolean getEnabled();
}