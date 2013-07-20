package network;

import java.util.Date;

import org.jgrapht.graph.DefaultEdge;


public class VertexTalkEdge<V> extends DefaultEdge  {
	private static int nrID = 0;
	private int ID, weight;
	private V v;
	private String label;
	private Vertex source, target;
	private Date date;
	
	
	
	public VertexTalkEdge (String label) {
		weight = 1;
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

	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}

	public Vertex getTarget() {
		return target;
	}

	public void setTarget(Vertex target) {
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

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	
	
}
