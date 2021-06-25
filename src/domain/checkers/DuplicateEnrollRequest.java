package domain.checkers;

import domain.violations.CheckerViolation;
import domain.Offering;

import java.util.List;

public class DuplicateEnrollRequest extends BaseChecker{
    List<Offering> offerings;

    public DuplicateEnrollRequest(List<Offering> offerings){
        this.offerings = offerings;
    }

    public CheckerViolation doCheck() {
        for (Offering firstOffering : offerings) {
            for (Offering secondOffering : offerings) {
                if (firstOffering == secondOffering)
                    continue;
                if (firstOffering.equals(secondOffering))
                    return new CheckerViolation(
                        String.format("%s is requested to be taken twice", firstOffering.getName()));
            }
        }
        return null;
    }
}
