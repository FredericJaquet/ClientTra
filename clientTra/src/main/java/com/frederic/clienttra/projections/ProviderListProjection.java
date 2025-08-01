package com.frederic.clienttra.projections;

/**
 * Projection interface representing a summarized view of providers.
 * Used to fetch essential provider information for listing purposes.
 */
public interface ProviderListProjection {
    Integer getIdProvider();
    String getComName();
    String getVatNumber();
    String getEmail();
    String getWeb();
    Boolean getEnabled();
}
