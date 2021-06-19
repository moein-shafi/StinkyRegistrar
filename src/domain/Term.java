package domain;

import java.sql.Date;
import java.util.List;

public class Term {
	private String name;
	private Date startDate;
		private List<CSE> offeringList;

	public Term(String name) {
		this.name = name;
		this.startDate = null;
	}

	public Term(String name, Date startDate) {
		this.name = name;
		this.startDate = startDate;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	
}

