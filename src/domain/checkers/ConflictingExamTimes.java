package domain.checkers;

import domain.violations.CheckerViolation;
import domain.Offering;

import java.util.List;

public class ConflictingExamTimes extends BaseChecker{
    List<Offering> offerings;

    public ConflictingExamTimes(List<Offering> offerings){
        this.offerings = offerings;
    }

    public CheckerViolation doCheck() {
        for (Offering firstOffering : offerings) {
            for (Offering secondOffering : offerings) {
                if (firstOffering == secondOffering)
                    continue;
                if (firstOffering.hasExamTimeConflict(secondOffering.getExamTime()))
                    return new CheckerViolation(
                        String.format("Two offerings %s and %s have the same exam time",
                        firstOffering, secondOffering));
            }
        }
        return null;
    }
}
