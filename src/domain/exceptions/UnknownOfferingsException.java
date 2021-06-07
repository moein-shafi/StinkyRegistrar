package domain.exceptions;

import java.util.ArrayList;

public class UnknownOfferingsException extends Exception {
	private ArrayList<String> missingIds;
	
	public UnknownOfferingsException(ArrayList<String> missingIds) {
		this.missingIds = missingIds;
	}

	public ArrayList<String> getMissingIds() {
		return missingIds;
	}
	
}
