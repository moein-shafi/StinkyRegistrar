package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(Student s, List<CSE> offerings) throws EnrollmentRulesViolationException {
        checkForAlreadyPassedCourses(s, offerings);
        checkForPrerequisiteRequirements(s, offerings);
        checkForDuplicateEnrollRequest(offerings);
        checkForConflictingExamTimes(offerings);
        checkForGPALimit(s, offerings);
        for (CSE o : offerings)
            s.takeCourse(o.getCourse(), o.getSection());
    }

    private void checkForPrerequisiteRequirements(Student s, List<CSE> offerings) throws EnrollmentRulesViolationException {
        for (CSE offering : offerings) {
            List<Course> prereqs = offering.getCourse().getPrerequisites();
            for (Course pre : prereqs) {
                if (!s.hasPassed(pre))
                    throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), offering.getCourse().getName()));
            }
        }
    }

    private void checkForGPALimit(Student s,List<CSE> courses) throws EnrollmentRulesViolationException {
        Map<Term, Map<Course, Double>> transcript = s.getTranscript();
        int unitsRequested = 0;
        for (CSE o : courses)
            unitsRequested += o.getCourse().getUnits();
        double points = 0;
        int totalUnits = 0;
        for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
            for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                points += r.getValue() * r.getKey().getUnits();
                totalUnits += r.getKey().getUnits();
            }
		}
        double gpa = points / totalUnits;
        if ((gpa < 12 && unitsRequested > 14) ||
                (gpa < 16 && unitsRequested > 16) ||
                (unitsRequested > 20))
            throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, gpa));
    }

    private void checkForDuplicateEnrollRequest(List<CSE> offerings) throws EnrollmentRulesViolationException {
        for (CSE o : offerings) {
            for (CSE o2 : offerings) {
                if (o == o2)
                    continue;
                if (o.getExamTime().equals(o2.getExamTime()))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
            }
        }
    }

    private void checkForConflictingExamTimes(List<CSE> offerings) throws EnrollmentRulesViolationException {
        for (CSE o : offerings) {
            for (CSE o2 : offerings) {
                if (o == o2)
                    continue;
                if (o.getCourse().equals(o2.getCourse()))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
        }
    }

    private void checkForAlreadyPassedCourses(Student s, List<CSE> offerings) throws EnrollmentRulesViolationException {
        for (CSE o : offerings) {
            if (s.hasPassed(o.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
        }
    }
}
