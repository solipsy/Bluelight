package network;

import java.util.Date;

import org.jgrapht.graph.DefaultEdge;


public class TalkEdge<V> extends DefaultEdge  {
	private static int nrID = 0;
	private int ID, weight;
	private V v;
	private String label, source, target;
	private Date date;
	
	
	
	public TalkEdge (String label) {
		weight = 0;
		ID = ++nrID;
		this.label = label;
		date = new Date();
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public void addToWeight(int w) {
		this.weight  += w;
	}
	
	@Override
	public String toString () {
		return label;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
