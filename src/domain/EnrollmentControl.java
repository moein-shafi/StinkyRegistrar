package domain;

import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollmentControl {
    static private final int MAXIMUM_ALLOWED_UNITS_FOR_PROBATION_STUDENTS = 14;
    static private final int MAXIMUM_GPA_OF_PROBATION_STUDENTS = 12;
    static private final int MAXIMUM_ALLOWED_UNITS_FOR_ORDINARY_STUDENTS = 16;
    static private final int MAXIMUM_GPA_OF_ORDINARY_STUDENTS = 16;
    static private final int MAXIMUM_ALLOWED_UNITS = 20;

	public void enroll(Student student, List<Offering> offerings) throws EnrollmentRulesViolationException {
        checkForAlreadyPassedCourses(student, offerings);
        checkForPrerequisiteRequirements(student, offerings);
        checkForDuplicateEnrollRequest(offerings);
        checkForConflictingExamTimes(offerings);
        checkForGPALimit(student, offerings);
        for (Offering offering : offerings)
            student.takeOffering(offering);
    }

    private void checkForPrerequisiteRequirements(Student student, List<Offering> offerings)
            throws EnrollmentRulesViolationException {
        for (Offering offering : offerings) {
            List<Course> prerequisites = offering.getPrerequisites();
            for (Course prerequisite : prerequisites) {
                if (!student.hasPassed(prerequisite.getId()))
                    throw new EnrollmentRulesViolationException(
                            String.format("The student has not passed %s as a prerequisite of %s",
                                    prerequisite.getName(),offering.getName()));
            }
        }
    }

    private void checkForGPALimit(Student student, List<Offering> offerings) throws EnrollmentRulesViolationException {
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

    private void checkForConflictingExamTimes(List<Offering> offerings) throws EnrollmentRulesViolationException {
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

    private void checkForDuplicateEnrollRequest(List<Offering> offerings) throws EnrollmentRulesViolationException {
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

    private void checkForAlreadyPassedCourses(Student student, List<Offering> offerings)
            throws EnrollmentRulesViolationException {
        for (Offering offering : offerings) {
            if (student.hasPassed(offering.getId()))
                throw new EnrollmentRulesViolationException(
                        String.format("The student has already passed %s", offering.getName()));
        }
    }
}
