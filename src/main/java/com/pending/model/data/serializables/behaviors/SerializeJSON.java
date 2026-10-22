package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Serializable;
import org.json.simple.JSONObject;

public abstract class SerializeJSON<T extends Serializable> extends SerializeBehavior<T> {
    public SerializeJSON(T serializable) {
        super(serializable);
    }

    public abstract JSONObject serialize();

    public abstract void deserialize(JSONObject jsonObject);

    public void deserialize(Object object) {
        deserialize((JSONObject) object);
    }
}