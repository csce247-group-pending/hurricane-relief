package com.pending.model.data.serializables;

import com.pending.model.data.Database;

public abstract class SerializeBehavior<T extends Serializable> {
    protected T serializable;

    public SerializeBehavior(T serializable) {
        this.serializable = serializable;
    }

    public abstract Object serialize();
    public abstract void deserialize(Object object);
    public void resolveReferences() {
        // Do nothing most of the time, overridden by User and
    }
}
