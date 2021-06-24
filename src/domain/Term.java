package domain;

import java.sql.Date;
import java.util.*;

public class Term {
	private String name;
	private Date startDate;
	private Map<Offering, Double> offerings = new HashMap<>();

	public Term(String name) {
		this.name = name;
		this.startDate = null;
	}
	
	public String toString() {
		return name;
	}

	public Map<Offering, Double> getOfferings() {
		return offerings;
	}

	public void addOffering(Offering offering, Double grade) {
		if(!offerings.containsKey(offering))
			offerings.put(offering, grade);
	}

	public boolean hasPassed(String courseId) {
		for (Offering offering : offerings.keySet()) {
			if (offering.getId().equals(courseId)) {
				if (offerings.get(offering) >= 10)
					return true;
			}
		}
		return false;
	}
}
