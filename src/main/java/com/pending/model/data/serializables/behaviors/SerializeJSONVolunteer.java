package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.User;
import com.pending.model.data.serializables.Volunteer;

public class SerializeJSONVolunteer<T extends Volunteer> extends SerializeJSONUser<T> {
    public SerializeJSONVolunteer(T serializable) {
        super(serializable);
    }
}
