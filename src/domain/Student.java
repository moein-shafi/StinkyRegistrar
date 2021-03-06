package domain;
import java.util.ArrayList;
import java.util.List;

public class Student {
	private String id;
	private String name;

	private List<Term> terms;
	private List<Offering> currentTerm;

	public Student(String id, String name) {
		this.id = id;
		this.name = name;
		this.terms = new ArrayList<>();
		this.currentTerm = new ArrayList<>();
	}
	
	public void takeOffering(Offering offering) {
		currentTerm.add(offering);
	}

	public void addTranscriptRecord(Offering offering, Term term, double grade) {
		if(!terms.contains(term))
			terms.add(term);
		term.addOffering(offering, grade);
	}
	
	public String toString() {
		return name;
	}

	public boolean hasPassed(String courseId) {
		for (Term term : terms) {
			if(term.hasPassed(courseId))
				return true;
		}
		return false;
	}

	public boolean hasTaken(String courseId) {
		for (Offering offering : currentTerm) {
			if(offering.getId().equals(courseId))
				return true;
		}
		return false;
	}

	public double getGpa() {
		if (terms.isEmpty()) {
			return 0;
		}
		return terms.stream().mapToDouble(Term::getGPA).average().orElseThrow();
	}
}
