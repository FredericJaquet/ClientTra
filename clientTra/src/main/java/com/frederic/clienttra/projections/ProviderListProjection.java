package com.frederic.clienttra.projections;

public interface ProviderListProjection {
    Integer getIdProvider();
    String getComName();
    String getVatNumber();
    String getEmail();
    String getWeb();
    Boolean getEnabled();
}
