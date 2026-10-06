package com.pending.model;

import org.json.simple.JSONObject;

import java.util.UUID;

public abstract class Serializable {
    protected UUID id;

    public Serializable() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public abstract JSONObject serialize();
    public abstract void deserialize(JSONObject object);
}
