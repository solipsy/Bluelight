package network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.transform.TransformerConfigurationException;

import org.jgrapht.ext.GmlExporter;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleGraph;
import org.xml.sax.SAXException;


public class Net {
	private SimpleGraph<String, TalkEdge> net;
	private GmlExporter exporter; 
	private GraphMLExporter exporter1;

	
	public Net () {
		net = new SimpleGraph<String, TalkEdge>(new ClassBasedEdgeFactory<String, TalkEdge>(TalkEdge.class));
		exporter = new GmlExporter();
		exporter1 = new GraphMLExporter<>();
	}
	
	public void export (String siteID) {
		String finalURL;
		
		Writer w = null;
		try {
			w = new FileWriter(new File ("C:\\Users\\solipsy\\Documents\\!Data\\Gephi\\" + siteID + "_Network.gml"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		exporter.setPrintLabels(exporter.PRINT_EDGE_VERTEX_LABELS);
//		exporter.setPrintLabels(exporter.PRINT_EDGE_LABELS);
		exporter.export(w, net);
	}

	
	public void exportGx () {
		String finalURL;
		
		Writer w = null;
		try {
			w = new FileWriter(new File ("C:\\Users\\solipsy\\Documents\\!Data\\Komentatorji\\results\\" + "userNetwork.graphml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
//		exporter.setPrintLabels(exporter.PRINT_VERTEX_LABELS);
		try {
			exporter1.export(w, net);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SimpleGraph<String, TalkEdge> getNet() {
		return net;
	}
}
