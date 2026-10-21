package com.pending.model.data.serializables;

import com.pending.model.enums.ResourceType;

public class Resource extends Serializable {
    private ResourceType type;
    private int quantity;
    private String description;

    public Resource(ResourceType type, int quantity, String description) {
        this.type = type;
        this.quantity = quantity;
        this.description = description;
        serializeBehavior = new SerializeJSONResource(this);
    }

    public Resource() {
        this(null, 0, "");
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
