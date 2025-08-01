package com.frederic.clienttra.projections;

/**
 * Projection interface providing a minimal set of fields
 * for a provider company, typically used for lightweight references.
 */
public interface ProviderMinimalProjection {
    Integer getIdCompany();
    String getComName();
    String getVatNumber();
}
