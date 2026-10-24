package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;

import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

public class LivingBeing extends Serializable {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LivingBeingType type;
    private ArrayList<ReliefType> reliefNeeds;

    public LivingBeing(String firstName, String lastName, LocalDate dateOfBirth, LivingBeingType type, ArrayList<ReliefType> reliefNeeds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
        setReliefNeeds(reliefNeeds);
    }

    public LivingBeing() {
        this("", "", LocalDate.now(), LivingBeingType.PET, new ArrayList<>());
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean addReliefNeed(ReliefType need) {
        return reliefNeeds.add(need);
    }

    public boolean removeReliefNeed(ReliefType need) {
        return reliefNeeds.remove(need);
    }
}
