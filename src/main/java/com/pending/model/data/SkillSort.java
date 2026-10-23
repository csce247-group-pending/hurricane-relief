package com.pending.model.data;

import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;
import com.pending.model.enums.Skill;

public class SkillSort implements PriorityFilter<ReliefRequest> {
    private Volunteer volunteer;

    public SkillSort(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    @Override
    public int filter(ReliefRequest object) {
        for (ReliefType volunteerSkill: volunteer.getSkills()) {
            for (requestedSkill: object.getTypes())
        }
        return 0;
    }
}
