package com.pending.model.enums;

/**
 * Represents the current progress state of a relief request.
 */
public enum ReliefStatus {
    /** The request has been submitted and is waiting for a volunteer. */
    REQUESTED,

    /** A volunteer has accepted the request. */
    ASSIGNED,

    /** The requested assistance has been completed. */
    COMPLETED
}