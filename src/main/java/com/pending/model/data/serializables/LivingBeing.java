package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;

import com.pending.model.enums.ReliefType;

public class LivingBeing extends Serializable {
    private String primaryName;
    private LocalDate dateOfBirth;
    private String type;
    private ArrayList<ReliefType> reliefNeeds;

    public LivingBeing(String primaryName, LocalDate dateOfBirth, String type, ArrayList<ReliefType> reliefNeeds) {
        this.primaryName = primaryName;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
        setReliefNeeds(reliefNeeds);
    }

    public LivingBeing() {
        this("", LocalDate.now(), "", new ArrayList<>());
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

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public ArrayList<ReliefType> getReliefNeeds() {
        return reliefNeeds;
    }

    public void setReliefNeeds(ArrayList<ReliefType> reliefNeeds) {
        if(reliefNeeds == null) {
            this.reliefNeeds = new ArrayList<>();
        } else {
             this.reliefNeeds = reliefNeeds;
        }
    }
}
