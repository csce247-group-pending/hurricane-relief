package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class SerializeJSONLivingBeing<T extends LivingBeing> extends SerializeJSON<T> {
    public SerializeJSONLivingBeing(T serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date_of_birth", serializable.getDateOfBirth().toString());
        jsonObject.put("type", serializable.getType().name());
        JSONArray reliefNeeds = new JSONArray();
        for (ReliefType reliefType: serializable.getReliefNeeds()) {
            reliefNeeds.add(reliefType.name());
        }
        jsonObject.put("relief_needs", reliefNeeds);
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setDateOfBirth(LocalDate.parse((String) jsonObject.get("date_of_birth")));
        serializable.setType(LivingBeingType.valueOf((String) jsonObject.get("type")));
        serializable.getReliefNeeds().clear();
        JSONArray reliefNeeds = (JSONArray) jsonObject.get("relief_needs");
        for (Object reliefType: reliefNeeds) {
            serializable.addReliefNeed(ReliefType.valueOf((String) reliefType));
        }
    }
}
