package com.frederic.clienttra.projections;

/**
 * Projection interface for retrieving minimal scheme information,
 * typically used in lists or dropdowns.
 */
public interface SchemeListProjection {
    // Projection for lightweight scheme listings (e.g., ComboBoxes).
    // Currently unused, reserved for potential frontend optimizations.
    Integer getIdScheme();
    String getSchemeName();
}
