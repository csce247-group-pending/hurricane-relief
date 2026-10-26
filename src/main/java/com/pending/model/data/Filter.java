package com.pending.model.data;

/**
 * Represents a condition used to determine whether an object should be
 * included in a filtered result.
 *
 * @param <T> the type of object evaluated by this filter
 */
@FunctionalInterface
public interface Filter<T> {

    /**
     * Tests whether an object matches this filter's condition.
     *
     * @param object the object to evaluate
     * @return true if the object matches; otherwise false
     */
    boolean filter(T object);
}