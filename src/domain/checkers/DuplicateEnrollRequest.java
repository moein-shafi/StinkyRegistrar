package domain.checkers;

import domain.exceptions.EnrollmentRulesViolationException;
import domain.Offering;

import java.util.List;

public class DuplicateEnrollRequest {
    List<Offering> offerings;

    public DuplicateEnrollRequest(List<Offering> offerings){
        this.offerings = offerings;
    }

    public void doCheck() throws EnrollmentRulesViolationException {
        for (Offering firstOffering : offerings) {
            for (Offering secondOffering : offerings) {
                if (firstOffering == secondOffering)
                    continue;
                if (firstOffering.equals(secondOffering))
                    throw new EnrollmentRulesViolationException(
                            String.format("%s is requested to be taken twice", firstOffering.getName()));
            }
        }
    }
}
