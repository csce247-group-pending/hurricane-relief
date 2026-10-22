package com.pending.model.data.serializables;

import java.time.LocalDate;

public class LivingBeing extends Serializable {
    private LocalDate dateOfBirth;
    private String type;

    public LivingBeing(LocalDate dateOfBirth, String type) {
        this.dateOfBirth = dateOfBirth;
        this.type = type;
    }

    public LivingBeing() {
        this(LocalDate.now(), "");
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
