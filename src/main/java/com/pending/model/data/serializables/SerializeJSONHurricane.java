package com.pending.model.data.serializables;

import org.json.simple.JSONObject;

public class SerializeJSONHurricane extends SerializeJSON<Hurricane> {
    public SerializeJSONHurricane(Hurricane serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        return null;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {

    }
}
