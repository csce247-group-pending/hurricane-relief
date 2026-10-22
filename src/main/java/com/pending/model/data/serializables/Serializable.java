package com.pending.model.data.serializables;

import com.pending.model.data.serializables.behaviors.SerializeBehavior;

import java.util.UUID;

public abstract class Serializable {
    protected UUID id;
    protected SerializeBehavior<? extends Serializable> serializeBehavior;
    public static String file;

    public Serializable() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Object serialize() {
        return serializeBehavior.serialize();
    }

    public void deserialize(Object object) {
        if (serializeBehavior == null) {
            System.out.println(this.getClass().getName() + " has no serialize behavior!");
        } else {
            serializeBehavior.deserialize(object);
        }
    }

    public void resolveReferences() {
        serializeBehavior.resolveReferences();
    }
}
