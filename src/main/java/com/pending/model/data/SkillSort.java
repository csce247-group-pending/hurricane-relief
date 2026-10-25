package com.pending.model.data;

import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;

import java.time.LocalDateTime;

public class SkillSort implements PriorityFilter<ReliefRequest> {
    private Volunteer volunteer;

    public SkillSort(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    @Override
    public double filter(ReliefRequest object) {
        double relevance = 0;
        for (ReliefType volunteerReliefType: volunteer.getReliefTypes()) {
            if (object.getTypes().contains(volunteerReliefType)) {
                relevance += 10;
            }
        }
        return relevance;
    }
}
