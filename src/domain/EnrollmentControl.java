package domain;

import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollmentControl {
    private final int MAXIMUM_ALLOWED_UNITS_FOR_PROBATION_STUDENTS = 14;
    private final int MAXIMUM_GPA_OF_PROBATION_STUDENTS = 12;
    private final int MAXIMUM_ALLOWED_UNITS_FOR_ORDINARY_STUDENTS = 16;
    private final int MAXIMUM_GPA_OF_ORDINARY_STUDENTS = 16;
    private final int MAXIMUM_ALLOWED_UNITS = 20;

	public void enroll(Student student, List<CSE> offerings) throws EnrollmentRulesViolationException {
        checkForAlreadyPassedCourses(student, offerings);
        checkForPrerequisiteRequirements(student, offerings);
        checkForDuplicateEnrollRequest(offerings);
        checkForConflictingExamTimes(offerings);
        checkForGPALimit(student, offerings);
        for (CSE offering : offerings)
            student.takeCourse(offering.getCourse(), offering.getSection());
    }

    private void checkForPrerequisiteRequirements(Student student, List<CSE> offerings)
            throws EnrollmentRulesViolationException {
        for (CSE offering : offerings) {
            List<Course> prerequisites = offering.getCourse().getPrerequisites();
            for (Course prerequisite : prerequisites) {
                if (!student.hasPassed(prerequisite))
                    throw new EnrollmentRulesViolationException(
                            String.format("The student has not passed %s as a prerequisite of %s",
                                    prerequisite.getName(),offering.getCourse().getName()));
            }
        }
    }

    private void checkForGPALimit(Student student, List<CSE> offerings) throws EnrollmentRulesViolationException {
        int unitsRequested = offerings.stream().mapToInt(offering -> offering.getCourse().getUnits()).sum();
        if ((student.getGpa() < MAXIMUM_GPA_OF_PROBATION_STUDENTS &&
                        unitsRequested > MAXIMUM_ALLOWED_UNITS_FOR_PROBATION_STUDENTS) ||
                (student.getGpa() < MAXIMUM_GPA_OF_ORDINARY_STUDENTS &&
                        unitsRequested > MAXIMUM_ALLOWED_UNITS_FOR_ORDINARY_STUDENTS) ||
                (unitsRequested > MAXIMUM_ALLOWED_UNITS))
            throw new EnrollmentRulesViolationException(
                    String.format("Number of units (%d) requested does not match GPA of %f",
                            unitsRequested, student.getGpa()));
    }

    private void checkForDuplicateEnrollRequest(List<CSE> offerings) throws EnrollmentRulesViolationException {
        for (CSE firstOffering : offerings) {
            for (CSE secondOffering : offerings) {
                if (firstOffering == secondOffering)
                    continue;
                if (firstOffering.hasExamTimeConflict(secondOffering.getExamTime()))
                    throw new EnrollmentRulesViolationException(
                            String.format("Two offerings %s and %s have the same exam time",
                                    firstOffering, secondOffering));
            }
        }
    }

    private void checkForConflictingExamTimes(List<CSE> offerings) throws EnrollmentRulesViolationException {
        for (CSE firstOffering : offerings) {
            for (CSE secondOffering : offerings) {
                if (firstOffering == secondOffering)
                    continue;
                if (firstOffering.getCourse().equals(secondOffering.getCourse()))
                    throw new EnrollmentRulesViolationException(
                            String.format("%s is requested to be taken twice", firstOffering.getCourse().getName()));
            }
        }
    }

    private void checkForAlreadyPassedCourses(Student student, List<CSE> offerings)
            throws EnrollmentRulesViolationException {
        for (CSE offering : offerings) {
            if (student.hasPassed(offering.getCourse()))
                throw new EnrollmentRulesViolationException(
                        String.format("The student has already passed %s", offering.getCourse().getName()));
        }
    }
}
