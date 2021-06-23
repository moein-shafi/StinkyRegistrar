package domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
	private String id;
	private String name;

	private Map<Term, Map<Course, Double>> transcript;
	private List<Offering> currentTerm;

	public Student(String id, String name) {
		this.id = id;
		this.name = name;
		this.transcript = new HashMap<>();
		this.currentTerm = new ArrayList<>();
	}
	
	public void takeOffering(Offering offering) {
		currentTerm.add(offering);
	}

	public Map<Term, Map<Course, Double>> getTranscript() {
		return transcript;
	}

	public void addTranscriptRecord(Course course, Term term, double grade) {
	    if (!transcript.containsKey(term))
	        transcript.put(term, new HashMap<>());
	    transcript.get(term).put(course, grade);
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

	public boolean hasPassed(Course course)
	{
		for (Map<Course, Double> courses : transcript.values()) {
			if (courses.containsKey(course)) {
				if (courses.get(course) >= 10)
					return true;
			}
		}
		return false;
	}

	public boolean hasTaken(String courseId)
	{
		for (Offering offering : currentTerm) {
			if(offering.getCourse().getId().equals(courseId))
				return true;
		}
		return false;
	}

	public double getGpa() {
		double points = 0;
		int totalUnits = 0;
		for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
			for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
				points += r.getValue() * r.getKey().getUnits();
				totalUnits += r.getKey().getUnits();
			}
		}
		return points / totalUnits;
	}
}
