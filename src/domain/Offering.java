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
	
	public String toString() {
		return super.getName() + " - " + section;
	}
	
	public Date getExamTime() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public boolean hasExamTimeConflict(Date date) {
		return examDate.equals(date);
	}
}
