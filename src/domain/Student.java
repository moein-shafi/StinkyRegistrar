package domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public List<Term> getTerms() {
		return terms;
	}

	public void addTranscriptRecord(Offering offering, Term term, double grade) {
		if(!terms.contains(term))
			terms.add(term);
		term.addOffering(offering, grade);
	}

    public List<Offering> getCurrentTerm() {
        return currentTerm;
    }

    public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}

	public boolean hasPassed(String courseId)
	{
		for (Term term : terms) {
			if(term.hasPassed(courseId))
				return true;
		}
		return false;
	}

	public boolean hasTaken(String courseId)
	{
		for (Offering offering : currentTerm) {
			if(offering.getId().equals(courseId))
				return true;
		}
		return false;
	}

	public double getGpa() {
		double points = 0;
		int totalUnits = 0;
		for (Term term : terms) {
			for (Offering offering : term.getOfferings().keySet()) {
				points += term.getOfferings().get(offering) * offering.getUnits();
				totalUnits += offering.getUnits();
			}
		}
		return points / totalUnits;
	}
}
