package com.pending.model;

@FunctionalInterface
public interface Filter<T> {
    public boolean filter(T object);
}
