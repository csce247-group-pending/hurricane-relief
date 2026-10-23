package com.pending.model.data;

@FunctionalInterface
public interface PriorityFilter<T> {
    public double filter(T object);
}
