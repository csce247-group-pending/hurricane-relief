package com.pending.model.data;

@FunctionalInterface
public interface PriorityFilter<T> {
    public int filter(T object);
}
