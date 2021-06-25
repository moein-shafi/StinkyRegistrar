package domain.checkers;

import domain.exceptions.EnrollmentRulesViolationException;


public abstract class  BaseChecker {
    public abstract void doCheck() throws EnrollmentRulesViolationException;
}
