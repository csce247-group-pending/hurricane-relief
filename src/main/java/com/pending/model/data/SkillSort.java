package com.pending.model.data;

import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;

/**
 * Calculates the priority of relief requests for a specific volunteer.
 * Requests receive a higher score when their needed relief types match
 * skills the volunteer can provide.
 */
public class SkillSort implements PriorityFilter<ReliefRequest> {
    private Volunteer volunteer;

    /**
     * Creates a request-priority sorter for one volunteer.
     *
     * @param volunteer the volunteer whose skills are used for matching
     */
    public SkillSort(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    /**
     * Calculates a relevance score for a relief request.
     * Each matching volunteer skill adds ten points to the score.
     *
     * @param object the relief request to evaluate
     * @return the request's relevance score for this volunteer
     */
    @Override
    public double filter(ReliefRequest object) {
        double relevance = 0;

        for (ReliefType volunteerReliefType
                : volunteer.getReliefTypes()) {

            if (object.getTypes().contains(volunteerReliefType)) {
                relevance += 10;
            }
        }

        return relevance;
    }
}