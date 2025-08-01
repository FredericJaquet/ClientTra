package com.frederic.clienttra.projections;

/**
 * Projection interface providing minimal information about a document.
 * Typically used when only the ID and document number are needed.
 */
public interface DocumentMinimalProjection {
    Integer getIdDocument();
    String getDocNumber();
}
