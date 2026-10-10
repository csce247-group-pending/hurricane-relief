package com.pending.model.data.serializables;

public abstract class SerializeBehavior<T extends Serializable> {
    protected T serializable;

    public SerializeBehavior(T serializable) {
        this.serializable = serializable;
    }

    public abstract Object serialize();
    public abstract void deserialize(Object object);
}
