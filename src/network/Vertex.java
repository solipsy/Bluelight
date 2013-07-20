package network;

import java.util.Date;

public class Vertex {
	private String type;
	private String label;
	private int freq, ID;
	private static int nrID = 0;
	private Date startDate;
	

	public Vertex(String type, String label) {
		this.type = type;
		this.label = label;
		freq = 1;
		ID = ++nrID;
	}
	

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}


	
}
