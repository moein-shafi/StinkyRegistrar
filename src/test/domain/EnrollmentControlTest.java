package test.domain;

import static org.junit.Assert.*;

import java.util.*;

import domain.exceptions.EnrollmentRulesViolationException;
import org.junit.Before;
import org.junit.Test;
import domain.*;


public class EnrollmentControlTest {
	private Student student;
	private Offering programming;
	private Offering advancedProgramming;
	private Offering discreteMathematics;
	private Offering mathematics1;
	private Offering mathematics2;
	private Offering physics1;
	private Offering physics2;
	private Offering islamicStudies;
	private Offering persianLiterature;
	private Offering english;
	private Offering islamicEthics;
	private Offering engineeringEconomics;
	private Offering entrepreneurship;

	@Before
	public void setup() {
		mathematics1 = new Offering("4", "MATHEMATICS1", 3);
		physics1 = new Offering("8", "PHYSICS1", 3);
		programming = new Offering("7", "PROGRAMMING", 4);
		mathematics2 = (Offering) new Offering("6", "MATHEMATICS2", 3).withPrerequisites(mathematics1);
		physics2 = (Offering) new Offering("9", "PHYSICS2", 3).withPrerequisites(mathematics1, physics1);
		advancedProgramming = (Offering) new Offering("2", "AP", 3).withPrerequisites(programming);
		discreteMathematics = (Offering) new Offering("3", "DM", 3).withPrerequisites(programming);
		engineeringEconomics = new Offering("1", "ENGINEERINGECONOMICS", 3);
		islamicStudies = new Offering("5", "ISLAMICSTUDIES", 2);
		persianLiterature = new Offering("12", "PERSIANLITERATURE", 2);
		english = new Offering("10", "ENGLISH", 2);
		islamicEthics = new Offering("11", "ISLAMICETHICS", 2);
		entrepreneurship = new Offering("13", "ENTREPRENEURSHIP", 3);

		student = new Student("1", "Bebe");
	}

	private ArrayList<Offering> requestedOfferings(Offering...offerings) {
		Calendar cal = Calendar.getInstance();
		ArrayList<Offering> result = new ArrayList<>();
		for (Offering offering : offerings) {
			cal.add(Calendar.DATE, 1);
			offering.setExamDate(cal.getTime());
			result.add(offering);
		}
		return result;
	}

	private boolean hasTaken(Student s, Course...courses) {
		for (Course course : courses) {
			if (!s.hasTaken(course.getId()))
				return false;
		}
		return true;
	}

	@Test
	public void canTakeBasicCoursesInFirstTerm() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(student, requestedOfferings(mathematics1, physics1, programming));
		assertTrue(hasTaken(student, mathematics1, physics1, programming));
	}

	@Test
	public void canTakeNoOfferings() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(student, new ArrayList<>());
		assertTrue(hasTaken(student));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeWithoutPreTaken() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(student, requestedOfferings(mathematics2, physics1, programming));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeWithoutPrePassed() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 18);
		student.addTranscriptRecord(programming, new Term("t1"), 12);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 8.4);
		new EnrollmentControl().enroll(student, requestedOfferings(mathematics2, advancedProgramming));
	}

	@Test
	public void canTakeWithPreFinallyPassed() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 18);
		student.addTranscriptRecord(programming, new Term("t1"), 12);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 8.4);

		student.addTranscriptRecord(physics2, new Term("t2"), 10);
		student.addTranscriptRecord(advancedProgramming, new Term("t2"), 16);
		student.addTranscriptRecord(mathematics1, new Term("t2"), 10.5);

		new EnrollmentControl().enroll(student, requestedOfferings(mathematics2, discreteMathematics));
		assertTrue(hasTaken(student, mathematics2, discreteMathematics));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeAlreadyPassed1() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 18);
		student.addTranscriptRecord(programming, new Term("t1"), 12);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 8.4);

		student.addTranscriptRecord(physics2, new Term("t2"), 10);
		student.addTranscriptRecord(advancedProgramming, new Term("t2"), 16);
		student.addTranscriptRecord(mathematics1, new Term("t2"), 10.5);

		new EnrollmentControl().enroll(student, requestedOfferings(mathematics1, discreteMathematics));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeAlreadyPassed2() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 18);
		student.addTranscriptRecord(programming, new Term("t1"), 12);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 8.4);

		student.addTranscriptRecord(physics2, new Term("t2"), 10);
		student.addTranscriptRecord(advancedProgramming, new Term("t2"), 16);
		student.addTranscriptRecord(mathematics1, new Term("t2"), 10.5);

		new EnrollmentControl().enroll(student, requestedOfferings(physics1, discreteMathematics));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeOfferingsWithSameExamTime() throws EnrollmentRulesViolationException {
		Calendar cal = Calendar.getInstance();
		List<Offering> offerings = requestedOfferings(physics1, mathematics1, physics1);
		for (Offering offering : offerings)
			offering.setExamDate(cal.getTime());
		new EnrollmentControl().enroll(student, offerings);
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeACourseTwice() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(student, requestedOfferings(physics1, discreteMathematics, physics1));
	}

	@Test
	public void canTake14WithGPA11() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 13);
		student.addTranscriptRecord(programming, new Term("t1"), 11);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 9);

		new EnrollmentControl().enroll(student, requestedOfferings(discreteMathematics, mathematics1, persianLiterature,
				islamicEthics, english, islamicStudies));
		assertTrue(hasTaken(student, discreteMathematics, mathematics1, persianLiterature, islamicEthics, english,
				islamicStudies));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTake15WithGPA11() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 13);
		student.addTranscriptRecord(programming, new Term("t1"), 11);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 9);

		new EnrollmentControl().enroll(student, requestedOfferings(discreteMathematics, mathematics1, persianLiterature,
				islamicEthics, english, advancedProgramming));
		assertTrue(hasTaken(student, discreteMathematics, mathematics1, persianLiterature, islamicEthics, english,
				advancedProgramming));
	}

	@Test
	public void canTake15WithGPA12() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 15);
		student.addTranscriptRecord(programming, new Term("t1"), 12);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 9);

		new EnrollmentControl().enroll(student, requestedOfferings(discreteMathematics, mathematics1, persianLiterature,
				islamicEthics, english, islamicStudies));
		assertTrue(hasTaken(student, discreteMathematics, mathematics1, persianLiterature, islamicEthics, english,
				islamicStudies));
	}

	@Test
	public void canTake15WithGPA15() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 15);
		student.addTranscriptRecord(programming, new Term("t1"), 15);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 15);

		new EnrollmentControl().enroll(student, requestedOfferings(discreteMathematics, mathematics2, persianLiterature,
				islamicEthics, english, islamicStudies));
		assertTrue(hasTaken(student, discreteMathematics, mathematics2, persianLiterature, islamicEthics, english,
				islamicStudies));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTake18WithGPA15() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 15);
		student.addTranscriptRecord(programming, new Term("t1"), 15);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 15);

		new EnrollmentControl().enroll(student, requestedOfferings(advancedProgramming, discreteMathematics,
				mathematics2, persianLiterature, islamicEthics, english, advancedProgramming));
		assertTrue(hasTaken(student, advancedProgramming, discreteMathematics, mathematics2, persianLiterature,
				islamicEthics, english, advancedProgramming));
	}

	@Test
	public void canTake20WithGPA16() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 16);
		student.addTranscriptRecord(programming, new Term("t1"), 16);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 16);

		new EnrollmentControl().enroll(student, requestedOfferings(
				advancedProgramming, discreteMathematics, mathematics2, physics2, engineeringEconomics,
				entrepreneurship, persianLiterature));
		assertTrue(hasTaken(student, advancedProgramming, discreteMathematics, mathematics2, physics2,
				engineeringEconomics, entrepreneurship, persianLiterature));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTake24() throws EnrollmentRulesViolationException {
		student.addTranscriptRecord(physics1, new Term("t1"), 16);
		student.addTranscriptRecord(programming, new Term("t1"), 16);
		student.addTranscriptRecord(mathematics1, new Term("t1"), 16);

		new EnrollmentControl().enroll(student, requestedOfferings(
				advancedProgramming, discreteMathematics, mathematics2, physics2, engineeringEconomics,
				entrepreneurship, persianLiterature, islamicEthics, english));
		assertTrue(hasTaken(student, advancedProgramming, discreteMathematics, mathematics2, physics2,
				engineeringEconomics, entrepreneurship, persianLiterature, islamicEthics, english));
	}
}