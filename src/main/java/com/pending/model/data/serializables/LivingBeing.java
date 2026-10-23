package com.pending.model.data.serializables;

import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

import java.time.LocalDate;
import java.util.ArrayList;

public class LivingBeing extends Serializable {
    private LocalDate dateOfBirth;
    private LivingBeingType type;
    private ArrayList<ReliefType> reliefNeeds;

    public LivingBeing(LocalDate dateOfBirth, LivingBeingType type) {
        this.dateOfBirth = dateOfBirth;
        this.type = type;
        this.reliefNeeds = new ArrayList<>();
    }

    public LivingBeing() {
        this(LocalDate.now(), LivingBeingType.PET);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LivingBeingType getType() {
        return type;
    }

    public void setType(LivingBeingType type) {
        this.type = type;
    }

    public ArrayList<ReliefType> getReliefNeeds() {
        return reliefNeeds;
    }

    public boolean addReliefNeed(ReliefType need) {
        return reliefNeeds.add(need);
    }
}
