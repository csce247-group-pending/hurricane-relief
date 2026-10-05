package com.pending.model;

import java.time.LocalDateTime;

public class Resource {
    private ResourceType type;
    private int quantity;
    private String description;
    private LocalDateTime restockDate;

    public Resource(ResourceType type, int quantity, String description) {

    }
    
}
