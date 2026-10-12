package com.pending.model.data.serializables;

import com.pending.model.ResourceType;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;

public class Resource extends Serializable {
    private ResourceType type;
    private int quantity;
    private String description;
    private LocalDateTime restockDate;

    public Resource(ResourceType type, int quantity, String description) {
        this.type = type;
        this.quantity = quantity;
        this.description = description;
        this.restockDate = null;
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

    public LocalDateTime getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(LocalDateTime restockDate) {
        this.restockDate = restockDate;
    }
}
