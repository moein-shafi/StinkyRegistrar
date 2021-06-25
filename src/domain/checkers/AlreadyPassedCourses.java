package domain.checkers;

import domain.Offering;
import domain.Student;
import domain.violations.CheckerViolation;

import java.util.List;

public class AlreadyPassedCourses extends BaseChecker{
    Student student;
    List<Offering> offerings;

    public AlreadyPassedCourses(Student student, List<Offering> offerings){
        this.student = student;
        this.offerings = offerings;
    }

    public CheckerViolation doCheck() {
        for (Offering offering : offerings) {
            if (student.hasPassed(offering.getId()))
                return new CheckerViolation(
                    String.format("The student has already passed %s", offering.getName()));
        }
        return null;
    }
}
