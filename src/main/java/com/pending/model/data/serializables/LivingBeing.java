package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

<<<<<<< HEAD
import com.pending.model.data.serializables.behaviors.SerializeJSONLivingBeing;
=======
import com.pending.model.enums.JSONFile;
>>>>>>> eeb4d9b (Continue implementing serialization for new data types)
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONArray;

public class LivingBeing extends Serializable {
<<<<<<< HEAD
    public static final String file = "json/livingbeings.json";

=======
    public static final String file = JSONFile.LIVING_BEINGS.getFilename();
>>>>>>> eeb4d9b (Continue implementing serialization for new data types)
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LivingBeingType type;
    private ArrayList<ReliefType> reliefNeeds;

    public LivingBeing(String firstName, String lastName,
            LocalDate dateOfBirth, LivingBeingType type,
            ArrayList<ReliefType> reliefNeeds) {

        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
        setReliefNeeds(reliefNeeds);

        serializeBehavior = new SerializeJSONLivingBeing<>(this);
    }

    public LivingBeing() {
        this("", "", LocalDate.now(), LivingBeingType.PET,
                new ArrayList<>());
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
        this.reliefNeeds = reliefNeeds == null
                ? new ArrayList<>()
                : reliefNeeds;
    }

    public boolean addReliefNeed(ReliefType need) {
        return reliefNeeds.add(need);
    }

    public boolean removeReliefNeed(ReliefType need) {
        return reliefNeeds.remove(need);
    }

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName
                + " is a " + type + " Age: " + getAge();
    }
}