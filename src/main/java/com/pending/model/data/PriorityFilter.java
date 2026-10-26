package com.pending.model.data;

/**
 * Represents a function that assigns a numeric priority score to an object.
 * Higher scores can be used to place objects earlier in a sorted result.
 *
 * @param <T> the type of object evaluated by this priority filter
 */
@FunctionalInterface
public interface PriorityFilter<T> {

    /**
     * Calculates the priority score for an object.
     *
     * @param object the object to evaluate
     * @return the calculated priority score
     */
    double filter(T object);
}