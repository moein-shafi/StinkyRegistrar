package domain.checkers;

import domain.exceptions.EnrollmentRulesViolationException;
import domain.Offering;
import domain.Course;
import domain.Student;

import java.util.List;

public class GPALimit {
    private static final int MAXIMUM_ALLOWED_UNITS_FOR_PROBATION_STUDENTS = 14;
    private static final int MAXIMUM_GPA_OF_PROBATION_STUDENTS = 12;
    private static final int MAXIMUM_ALLOWED_UNITS_FOR_ORDINARY_STUDENTS = 16;
    private static final int MAXIMUM_GPA_OF_ORDINARY_STUDENTS = 16;
    private static final int MAXIMUM_ALLOWED_UNITS = 20;

    Student student;
    List<Offering> offerings;

    public GPALimit(Student student, List<Offering> offerings){
        this.student = student;
        this.offerings = offerings;
    }

    public void doCheck() throws EnrollmentRulesViolationException {
        int unitsRequested = offerings.stream().mapToInt(Course::getUnits).sum();
        if ((student.getGpa() < MAXIMUM_GPA_OF_PROBATION_STUDENTS &&
                        unitsRequested > MAXIMUM_ALLOWED_UNITS_FOR_PROBATION_STUDENTS) ||
                (student.getGpa() < MAXIMUM_GPA_OF_ORDINARY_STUDENTS &&
                        unitsRequested > MAXIMUM_ALLOWED_UNITS_FOR_ORDINARY_STUDENTS) ||
                (unitsRequested > MAXIMUM_ALLOWED_UNITS))
            throw new EnrollmentRulesViolationException(
                    String.format("Number of units (%d) requested does not match GPA of %f",
                            unitsRequested, student.getGpa()));
    }
}
