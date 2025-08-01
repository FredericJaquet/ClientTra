package com.frederic.clienttra.projections;

/**
 * Projection interface providing minimal customer company data.
 * Used to fetch essential fields for lightweight operations or references.
 */
public interface CustomerMinimalProjection {
    Integer getIdCompany();
    String getComName();
    String getVatNumber();
}
