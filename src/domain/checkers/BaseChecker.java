package domain.checkers;

import domain.violations.CheckerViolation;


public abstract class  BaseChecker {
    public abstract CheckerViolation doCheck() ;
}
