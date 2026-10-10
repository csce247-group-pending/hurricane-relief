package com.pending.model;

import com.pending.model.data.serializables.Hurricane;

public interface Observer {
    public void update(Hurricane hurricane);
}
