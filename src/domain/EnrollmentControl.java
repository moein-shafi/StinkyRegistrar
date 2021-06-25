package domain;

import java.util.List;
import java.util.ArrayList;


import domain.exceptions.EnrollmentRulesViolationException;
import domain.checkers.BaseChecker;
import domain.checkers.PrerequisiteRequirements;
import domain.checkers.AlreadyPassedCourses;
import domain.checkers.GPALimit;
import domain.checkers.DuplicateEnrollRequest;
import domain.checkers.ConflictingExamTimes;
import domain.violations.BaseViolation;

public class EnrollmentControl {
    private String violationMsg;

	public void enroll(Student student, List<Offering> offerings) throws EnrollmentRulesViolationException {
        List<BaseChecker> checkers;
        List<BaseViolation> violations;
        checkers = new ArrayList<>();
        violations = new ArrayList<>();

        checkers.add(new AlreadyPassedCourses(student, offerings));
        checkers.add(new PrerequisiteRequirements(student, offerings));
        checkers.add(new GPALimit(student, offerings));
        checkers.add(new DuplicateEnrollRequest(offerings));
        checkers.add(new ConflictingExamTimes(offerings));

        for (BaseChecker checker : checkers){
            var violation = checker.doCheck();
            if (violation != null){
                violations.add(violation);
                violationMsg += violation.getViolationMessage();
            }
        }
        if (!violations.isEmpty()){
            throw new EnrollmentRulesViolationException(violationMsg);
        }
        for (Offering offering : offerings)
            student.takeOffering(offering);
    }
}
