package domain;
import java.util.Date;

public class Offering extends Course{
	private int section;
	private Date examDate;

	public Offering(String id, String name, int units) {
		super(id, name, units);
		this.section = 1;
		this.examDate = null;
	}

	public Offering(String id, String name, int units, Date examDate) {
		super(id, name, units);
		this.section = 1;
		this.examDate = examDate;
	}

	public Offering(String id, String name, int units, Date examDate, int section) {
		super(id, name, units);
		this.section = section;
		this.examDate = examDate;
	}
	
	public String toString() {
		return super.getName() + " - " + section;
	}
	
	public Date getExamTime() {
		return examDate;
	}

	public int getSection() { return section; }

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public boolean hasExamTimeConflict(Date date) {
		if (examDate.equals(date))
			return true;
		return false;
	}
}
