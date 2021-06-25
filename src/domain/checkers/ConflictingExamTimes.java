package domain.checkers;

import domain.exceptions.EnrollmentRulesViolationException;
import domain.Offering;

import java.util.List;

public class ConflictingExamTimes {
    List<Offering> offerings;

    public ConflictingExamTimes(List<Offering> offerings){
        this.offerings = offerings;
    }

    public void doCheck() throws EnrollmentRulesViolationException {
        for (Offering firstOffering : offerings) {
            for (Offering secondOffering : offerings) {
                if (firstOffering == secondOffering)
                    continue;
                if (firstOffering.hasExamTimeConflict(secondOffering.getExamTime()))
                    throw new EnrollmentRulesViolationException(
                            String.format("Two offerings %s and %s have the same exam time",
                                    firstOffering, secondOffering));
            }
        }
    }
}
