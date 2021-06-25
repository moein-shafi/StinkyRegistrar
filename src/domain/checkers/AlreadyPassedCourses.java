public class AlreadyPassedCourses {
    Student student;
    List<Offering> offerings;

    public AlreadyPassedCourses(Student student, List<Offering> offerings){
        this.student = student;
        this.offerings = offerings;
    }

    public void doCheck() throws EnrollmentRulesViolationException {
        for (Offering offering : offerings) {
            if (student.hasPassed(offering.getId()))
                throw new EnrollmentRulesViolationException(
                        String.format("The student has already passed %s", offering.getName()));
        }
    }
}
