package domain;

import static org.junit.Assert.*;

import java.util.*;

import domain.exceptions.EnrollmentRulesViolationException;
import org.junit.Before;
import org.junit.Test;

public class EnrollCtrlTest {
	private Student bebe;
	private Offering prog;
	private Offering ap;
	private Offering dm;
	private Offering math1;
	private Offering math2;
	private Offering phys1;
	private Offering phys2;
	private Offering maaref;
	private Offering farsi;
	private Offering english;
	private Offering akhlagh;
	private Offering economy;
	private Offering karafarini;

	@Before
	public void setup() {
		math1 = new Offering("4", "MATH1", 3);
		phys1 = new Offering("8", "PHYS1", 3);
		prog = new Offering("7", "PROG", 4);
		math2 = (Offering) new Offering("6", "MATH2", 3).withPrerequisites(math1);
		phys2 = (Offering) new Offering("9", "PHYS2", 3).withPrerequisites(math1, phys1);
		ap = (Offering) new Offering("2", "AP", 3).withPrerequisites(prog);
		dm = (Offering) new Offering("3", "DM", 3).withPrerequisites(prog);
		economy = new Offering("1", "ECO", 3);
		maaref = new Offering("5", "MAAREF", 2);
		farsi = new Offering("12", "FA", 2);
		english = new Offering("10", "EN", 2);
		akhlagh = new Offering("11", "AKHLAGH", 2);
		karafarini = new Offering("13", "KAR", 3);

		bebe = new Student("1", "Bebe");
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
	    Set<Course> coursesTaken = new HashSet<>();
		for (Course course : courses) {
			if (!s.hasTaken(course.getId()))
				return false;
		}
		return true;
	}

	@Test
	public void canTakeBasicCoursesInFirstTerm() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(bebe, requestedOfferings(math1, phys1, prog));
		assertTrue(hasTaken(bebe, math1, phys1, prog));
	}

	@Test
	public void canTakeNoOfferings() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(bebe, new ArrayList<>());
		assertTrue(hasTaken(bebe));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeWithoutPreTaken() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(bebe, requestedOfferings(math2, phys1, prog));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeWithoutPrePassed() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
		bebe.addTranscriptRecord(prog, new Term("t1"), 12);
		bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);
		new EnrollmentControl().enroll(bebe, requestedOfferings(math2, ap));
	}

	@Test
	public void canTakeWithPreFinallyPassed() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
		bebe.addTranscriptRecord(prog, new Term("t1"), 12);
		bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);

		bebe.addTranscriptRecord(phys2, new Term("t2"), 10);
		bebe.addTranscriptRecord(ap, new Term("t2"), 16);
		bebe.addTranscriptRecord(math1, new Term("t2"), 10.5);

		new EnrollmentControl().enroll(bebe, requestedOfferings(math2, dm));
		assertTrue(hasTaken(bebe, math2, dm));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeAlreadyPassed1() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
		bebe.addTranscriptRecord(prog, new Term("t1"), 12);
		bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);

		bebe.addTranscriptRecord(phys2, new Term("t2"), 10);
		bebe.addTranscriptRecord(ap, new Term("t2"), 16);
		bebe.addTranscriptRecord(math1, new Term("t2"), 10.5);

		new EnrollmentControl().enroll(bebe, requestedOfferings(math1, dm));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeAlreadyPassed2() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
		bebe.addTranscriptRecord(prog, new Term("t1"), 12);
		bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);

		bebe.addTranscriptRecord(phys2, new Term("t2"), 10);
		bebe.addTranscriptRecord(ap, new Term("t2"), 16);
		bebe.addTranscriptRecord(math1, new Term("t2"), 10.5);

		new EnrollmentControl().enroll(bebe, requestedOfferings(phys1, dm));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeOfferingsWithSameExamTime() throws EnrollmentRulesViolationException {
		Calendar cal = Calendar.getInstance();
		List<Offering> offerings = requestedOfferings(phys1, math1, phys1);
		for (Offering offering : offerings)
			offering.setExamDate(cal.getTime());
		new EnrollmentControl().enroll(bebe, offerings);
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTakeACourseTwice() throws EnrollmentRulesViolationException {
		new EnrollmentControl().enroll(bebe, requestedOfferings(phys1, dm, phys1));
	}

	@Test
	public void canTake14WithGPA11() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 13);
		bebe.addTranscriptRecord(prog, new Term("t1"), 11);
		bebe.addTranscriptRecord(math1, new Term("t1"), 9);

		new EnrollmentControl().enroll(bebe, requestedOfferings(dm, math1, farsi, akhlagh, english, maaref));
		assertTrue(hasTaken(bebe, dm, math1, farsi, akhlagh, english, maaref));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTake15WithGPA11() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 13);
		bebe.addTranscriptRecord(prog, new Term("t1"), 11);
		bebe.addTranscriptRecord(math1, new Term("t1"), 9);

		new EnrollmentControl().enroll(bebe, requestedOfferings(dm, math1, farsi, akhlagh, english, ap));
		assertTrue(hasTaken(bebe, dm, math1, farsi, akhlagh, english, ap));
	}

	@Test
	public void canTake15WithGPA12() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 15);
		bebe.addTranscriptRecord(prog, new Term("t1"), 12);
		bebe.addTranscriptRecord(math1, new Term("t1"), 9);

		new EnrollmentControl().enroll(bebe, requestedOfferings(dm, math1, farsi, akhlagh, english, maaref));
		assertTrue(hasTaken(bebe, dm, math1, farsi, akhlagh, english, maaref));
	}

	@Test
	public void canTake15WithGPA15() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 15);
		bebe.addTranscriptRecord(prog, new Term("t1"), 15);
		bebe.addTranscriptRecord(math1, new Term("t1"), 15);

		new EnrollmentControl().enroll(bebe, requestedOfferings(dm, math2, farsi, akhlagh, english, maaref));
		assertTrue(hasTaken(bebe, dm, math2, farsi, akhlagh, english, maaref));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTake18WithGPA15() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 15);
		bebe.addTranscriptRecord(prog, new Term("t1"), 15);
		bebe.addTranscriptRecord(math1, new Term("t1"), 15);

		new EnrollmentControl().enroll(bebe, requestedOfferings(ap, dm, math2, farsi, akhlagh, english, ap));
		assertTrue(hasTaken(bebe, ap, dm, math2, farsi, akhlagh, english, ap));
	}

	@Test
	public void canTake20WithGPA16() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 16);
		bebe.addTranscriptRecord(prog, new Term("t1"), 16);
		bebe.addTranscriptRecord(math1, new Term("t1"), 16);

		new EnrollmentControl().enroll(bebe, requestedOfferings(
				ap, dm, math2, phys2, economy, karafarini, farsi));
		assertTrue(hasTaken(bebe, ap, dm, math2, phys2, economy, karafarini, farsi));
	}

	@Test(expected = EnrollmentRulesViolationException.class)
	public void cannotTake24() throws EnrollmentRulesViolationException {
		bebe.addTranscriptRecord(phys1, new Term("t1"), 16);
		bebe.addTranscriptRecord(prog, new Term("t1"), 16);
		bebe.addTranscriptRecord(math1, new Term("t1"), 16);

		new EnrollmentControl().enroll(bebe, requestedOfferings(
				ap, dm, math2, phys2, economy, karafarini, farsi, akhlagh, english));
		assertTrue(hasTaken(bebe, ap, dm, math2, phys2, economy, karafarini, farsi, akhlagh, english));
	}


}