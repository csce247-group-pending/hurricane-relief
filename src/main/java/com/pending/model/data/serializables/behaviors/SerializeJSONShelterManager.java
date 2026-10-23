package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.ShelterManager;

public class SerializeJSONShelterManager<T extends ShelterManager> extends SerializeJSONVolunteer<T> {
    public SerializeJSONShelterManager(T serializable) {
        super(serializable);
    }

    @Override
    public void resolveReferences() {
        super.resolveReferences();
        Database database = Database.getInstance();
        serializable.setShelter(database.getShelterByUUID(serializable.getShelterUUIDReference()));
    }
}
