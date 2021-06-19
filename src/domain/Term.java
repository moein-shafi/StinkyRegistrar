package domain;

import java.sql.Date;
import java.util.List;

public class Term {
	private String name;
	private Date startDate;
	private List<CSE> offeringList;

	public static class Builder {
		private String name;
		private Date startDate;
		private List<CSE> offeringList;

		public Builder(String name) {
			this.name = name;
		}

		public Builder withStartDate(Date startDate) {
			this.startDate = startDate;

			return this;
		}
		
		public Builder withOfferingList(List<CSE> offeringList) {
			this.offeringList = offeringList;

			return this;
		}

		public Term build(){
			Term term = new Term();
			term.name = this.name;
			term.offeringList = this.offeringList;
			term.startDate = this.startDate;
			return term;
		}
	}

	private Term() {
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

