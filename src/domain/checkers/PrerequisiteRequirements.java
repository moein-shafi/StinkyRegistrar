package domain.checkers;

import domain.violations.CheckerViolation;
import domain.Offering;
import domain.Course;
import domain.Student;

import java.util.List;


public class PrerequisiteRequirements extends BaseChecker{
    Student student;
    List<Offering> offerings;

    public PrerequisiteRequirements(Student student, List<Offering> offerings){
        this.student = student;
        this.offerings = offerings;
    }

    public CheckerViolation doCheck() {
        for (Offering offering : offerings) {
            List<Course> prerequisites = offering.getPrerequisites();
            for (Course prerequisite : prerequisites) {
                if (!student.hasPassed(prerequisite.getId()))
                    return new CheckerViolation(
                        String.format("The student has not passed %s as a prerequisite of %s",
                                    prerequisite.getName(),offering.getName()));
            }
        }
        return null;
    }
}
