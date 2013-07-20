package network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.transform.TransformerConfigurationException;

import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GmlExporter;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleGraph;
import org.xml.sax.SAXException;

public class VertexNet {
	private SimpleGraph<Vertex, VertexTalkEdge> net;
	private GmlExporter<Vertex, VertexTalkEdge> exporter;
	private GraphMLExporter exporter1;

	public VertexNet() {
		net = new SimpleGraph<Vertex, VertexTalkEdge>(
				new ClassBasedEdgeFactory<Vertex, VertexTalkEdge>(
						VertexTalkEdge.class));

		exporter = new GmlExporter<Vertex, VertexTalkEdge>(new VertexIdProvider(),
				new VertexLabelProvider(), new EdgeIdProvider(),
				new EdgeLabelProvider());
		exporter.setPrintLabels(exporter.PRINT_EDGE_VERTEX_LABELS);
		exporter1 = new GraphMLExporter<>(new VertexIdProvider(),
				new VertexLabelProvider(), new EdgeIdProvider(),
				new EdgeLabelProvider());

	}

	class VertexIdProvider implements VertexNameProvider<Vertex> {
		public String getVertexName(Vertex node) {
			return Long.toString(node.getID());
		}

	}

	class VertexLabelProvider implements VertexNameProvider<Vertex> {

		@Override
		public String getVertexName(Vertex v) {
//			System.out.println("THIS IS NEVER CALLED");
			return v.getLabel();
		}


	}

	class EdgeIdProvider implements EdgeNameProvider<VertexTalkEdge> {

		@Override
		public String getEdgeName(VertexTalkEdge edge) {
			return Long.toString(edge.getID());
		}

	}

	class EdgeLabelProvider implements EdgeNameProvider<VertexTalkEdge> {

		@Override
		public String getEdgeName(VertexTalkEdge edge) {
//			System.out.println("THIS IS NEVER CALLED");
			return edge.getLabel();
		}

	}

	public void export(String siteID) {
		String finalURL;

		Writer w = null;
		try {
			w = new FileWriter(new File(
					"C:\\Users\\solipsy\\Documents\\!Data\\Gephi\\" + siteID
							+ ".gml"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// exporter.setPrintLabels(exporter.PRINT_EDGE_LABELS);
		exporter.export(w, net);
	}

	public void exportGx() {
		String finalURL;

		Writer w = null;
		try {
			w = new FileWriter(new File(
					"C:\\Users\\solipsy\\Documents\\!Data\\Komentatorji\\results\\"
							+ "userNetwork.graphml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// exporter.setPrintLabels(exporter.PRINT_VERTEX_LABELS);
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

	public SimpleGraph<Vertex, VertexTalkEdge> getNet() {
		return net;
	}
}
