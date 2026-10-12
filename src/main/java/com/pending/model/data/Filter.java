package com.pending.model.data;

@FunctionalInterface
public interface Filter<T> {
    public boolean filter(T object);
}
