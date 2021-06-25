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

public class EnrollmentControl {
    private List<BaseChecker> checkers;

	public void enroll(Student student, List<Offering> offerings) throws EnrollmentRulesViolationException {
        checkers = new ArrayList<>();
        checkers.add(new AlreadyPassedCourses(student, offerings));
        checkers.add(new PrerequisiteRequirements(student, offerings));
        checkers.add(new GPALimit(student, offerings));
        checkers.add(new DuplicateEnrollRequest(offerings));
        checkers.add(new ConflictingExamTimes(offerings));

        for (BaseChecker checker : checkers)
            checker.doCheck();
        for (Offering offering : offerings)
            student.takeOffering(offering);
    }
}
