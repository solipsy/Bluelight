package analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import network.Net;
import network.TalkEdge;
import network.Vertex;
import network.VertexNet;
import network.VertexTalkEdge;
import index.SolrSrvr;
import it.uniroma1.dis.wiserver.gexf4j.core.Edge;
import it.uniroma1.dis.wiserver.gexf4j.core.EdgeType;
import it.uniroma1.dis.wiserver.gexf4j.core.Gexf;
import it.uniroma1.dis.wiserver.gexf4j.core.Graph;
import it.uniroma1.dis.wiserver.gexf4j.core.Mode;
import it.uniroma1.dis.wiserver.gexf4j.core.Node;
import it.uniroma1.dis.wiserver.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wiserver.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wiserver.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wiserver.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wiserver.gexf4j.core.dynamic.Spell;
import it.uniroma1.dis.wiserver.gexf4j.core.dynamic.TimeFormat;
import it.uniroma1.dis.wiserver.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wiserver.gexf4j.core.impl.SpellImpl;
import it.uniroma1.dis.wiserver.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wiserver.gexf4j.core.impl.data.AttributeListImpl;
import parser.Detector;
import parser.Post;

public class NetworkAnalysis {
	private static SolrSrvr solr;
	private static Detector detector;
	private static Net net;
	private static VertexNet vnet;
	private static final String [] CATEGORIES = {"psychedelic", "basic", "other", "cannabis", "ecstasy", "culture", "media", "darkside", "advanced", "tripreports"};


	public static void main(String[] args) {
		Detector detector = new Detector();
		solr = new SolrSrvr();
//		getDrugsByCategory ("psychedelic", detector);
//		getDrugsAllCategories (detector);
//		getEffects ("darkside", detector);
		getEffectsAllPosts(detector);
	}
	
	
	
	private static void getDrugsByCategory (String category, Detector detector) {
		HashMap<String, Vertex> vertices = new HashMap<String, Vertex> ();
		vnet = new VertexNet();
		HashMap<String, String> drugsTitle = null;
		ArrayList<Post> posts = solr.getPosts(category);
		int postCounter = 0;

		for (Post post : posts) {
			HashMap<String, String> drugsPost = detect(post.getBody(), detector);
			if (drugsPost != null) {
				for (Iterator<String> drug1 = drugsPost.keySet().iterator(); drug1.hasNext();) {
					String d1 = drug1.next();

					for (Iterator<String> drug2 = drugsPost.keySet().iterator(); drug2.hasNext();) {
						String d2 = drug2.next();
						if (!d1.equals(d2)) {
							Vertex v1 = null;
							Vertex v2 = null;
							if (!vertices.containsKey(d1)) {
								v1 = new Vertex ("drug", d1);
								v1.setStartDate(post.getPostDate());
								vertices.put(d1,  v1);
								vnet.getNet().addVertex(v1);
							}
							else {
								v1 = vertices.get(d1);
								v1.setFreq(v1.getFreq() + 1);
							}
							
							if (!vertices.containsKey(d2)) {
								v2 = new Vertex ("drug", d2);
								v2.setStartDate(post.getPostDate());
								vertices.put(d2,  v2);
								vnet.getNet().addVertex(v2);
							}
							else {
								v2 = vertices.get(d2);
								v2.setFreq(v2.getFreq() + 1);
							}
							
							VertexTalkEdge edge = null;
							if (vnet.getNet().getEdge(v1,  v2) == null) {
								edge = new VertexTalkEdge("");
								edge.setSource(v1);
								edge.setTarget(v2);
								vnet.getNet().addEdge(v1, v2, edge);
							}
							else {
								edge = vnet.getNet().getEdge(v1,  v2);
								edge.setWeight(edge.getWeight() + 1);
							}
						}
					}
				}
			}
		}
		exportGexfDrugs(category, vnet);
		
//		vnet.export("drugs_" + category);
	}

	private static void getDrugsAllCategories(Detector detector) {
		vnet = new VertexNet();
		HashMap<String, Vertex> vertices = new HashMap<String, Vertex> ();
		HashMap<String, String> drugsTitle = null;
		for (String category1 : CATEGORIES) {
			System.out.println ("Category: " + category1);
			ArrayList<Post> posts = solr.getPosts(category1);
			for (Post post : posts) {
				HashMap<String, String> drugsPost = detect(post.getBody(), detector);

				if (drugsPost != null) {
					for (Iterator<String> drug1 = drugsPost.keySet().iterator(); drug1.hasNext();) {
						String d1 = drug1.next();
						for (Iterator<String> drug2 = drugsPost.keySet().iterator(); drug2.hasNext();) {
							String d2 = drug2.next();
							if (!d1.equals(d2)) {
								Vertex v1 = null;
								Vertex v2 = null;
								if (!vertices.containsKey(d1)) {
									v1 = new Vertex ("drug", d1);
									v1.setStartDate(post.getPostDate());
									vertices.put(d1,  v1);
									vnet.getNet().addVertex(v1);
								}
								else {
									v1 = vertices.get(d1);
									v1.setFreq(v1.getFreq() + 1);
								}
								
								if (!vertices.containsKey(d2)) {
									v2 = new Vertex ("drug", d2);
									v2.setStartDate(post.getPostDate());
									vertices.put(d2,  v2);
									vnet.getNet().addVertex(v2);
								}
								else {
									v2 = vertices.get(d2);
									v2.setFreq(v2.getFreq() + 1);
								}
								
								
								VertexTalkEdge edge = null;
								if (vnet.getNet().getEdge(v1,  v2) == null) {
									edge = new VertexTalkEdge("");
									edge.setSource(v1);
									edge.setTarget(v2);
									vnet.getNet().addEdge(v1, v2, edge);
								}
								else {
									edge = vnet.getNet().getEdge(v1,  v2);
									edge.setWeight(edge.getWeight() + 1);
								}
							}
						}
					}
				}
			}
		}
		exportGexfDrugs("all", vnet);
//		net.export("drugs_" + category);
	}
	
	private static void getEffects(String category, Detector detector) {
		vnet = new VertexNet();
		HashMap<String, Vertex> vertices = new HashMap<String, Vertex> ();
		HashMap<String, String> drugsTitle = null;
		HashMap<String, String> effectTitle = null;
		ArrayList<Post> posts = solr.getPosts(category);
		int postCounter = 0;

		for (Post post : posts) {

			drugsTitle = detect(post.getThreadTitle(), detector);
			effectTitle = detectEffects(post.getThreadTitle(),detector);
			HashMap<String, String> drugsPost = detect(post.getBody(), detector);
			HashMap<String, String> effectsPost = detectEffects(post.getBody(),detector);

			if (drugsTitle != null && drugsTitle.size() > 0 && drugsPost != null) drugsPost.putAll(drugsTitle); // add terms from title
			if (effectTitle != null && effectTitle.size() > 0  && effectsPost != null) effectsPost.putAll(effectTitle); // add terms from title
			if (effectTitle != null && effectTitle.size() > 0  && effectsPost != null && drugsPost != null) drugsPost.putAll(effectsPost);

			if (drugsPost != null) {
				if (effectsPost != null) drugsPost.putAll(effectsPost);
				for (Iterator<String> drug1 = drugsPost.keySet().iterator(); drug1.hasNext();) {
					String d1 = drug1.next();
					for (Iterator<String> drug2 = drugsPost.keySet().iterator(); drug2.hasNext();) {
						String d2 = drug2.next();
						if (!d1.equals(d2)) {
							Vertex v1 = null;
							Vertex v2 = null;
							if (!vertices.containsKey(d1)) {
								if (detector.isEffect(d1)) v1 = new Vertex ("effect", d1);
								else v1 = new Vertex ("drug", d1);
								v1.setStartDate(post.getPostDate());
								vertices.put(d1,  v1);
								vnet.getNet().addVertex(v1);
							}
							else {
								v1 = vertices.get(d1);
								v1.setFreq(v1.getFreq() + 1);
							}
							
							if (!vertices.containsKey(d2)) {
								if (detector.isEffect(d2)) v2 = new Vertex ("effect", d2);
								else v2 = new Vertex ("drug", d2);
								v2.setStartDate(post.getPostDate());
								vertices.put(d2,  v2);
								vnet.getNet().addVertex(v2);
							}
							else {
								v2 = vertices.get(d2);
								v2.setFreq(v2.getFreq() + 1);
							}
							
							
							VertexTalkEdge edge = null;
							if (vnet.getNet().getEdge(v1,  v2) == null) {
								edge = new VertexTalkEdge("");
								edge.setSource(v1);
								edge.setTarget(v2);
								vnet.getNet().addEdge(v1, v2, edge);
							}
							else {
								edge = vnet.getNet().getEdge(v1,  v2);
								edge.setWeight(edge.getWeight() + 1);
							}
						}
					}
				}
			}
		}
		exportGexfDrugs("effects_" + category, vnet);

	}
	
	private static void getEffectsAllPosts (Detector detector) {
		vnet = new VertexNet();
		HashMap<String, Vertex> vertices = new HashMap<String, Vertex> ();
		HashMap<String, String> drugsTitle = null;
		HashMap<String, String> effectTitle = null;
		for (String category : CATEGORIES) {
			System.out.println ("Category: " + category);
			ArrayList<Post> posts = solr.getPosts(category);
			int postCounter = 0;

			for (Post post : posts) {

				drugsTitle = detect(post.getThreadTitle(), detector);
				effectTitle = detectEffects(post.getThreadTitle(),detector);
				HashMap<String, String> drugsPost = detect(post.getBody(), detector);
				HashMap<String, String> effectsPost = detectEffects(post.getBody(),detector);

				if (drugsTitle != null && drugsTitle.size() > 0 && drugsPost != null) drugsPost.putAll(drugsTitle); // add terms from title
				if (effectTitle != null && effectTitle.size() > 0  && effectsPost != null) effectsPost.putAll(effectTitle); // add terms from title
				if (effectTitle != null && effectTitle.size() > 0  && effectsPost != null && drugsPost != null) drugsPost.putAll(effectsPost);

				if (drugsPost != null) {
					if (effectsPost != null) drugsPost.putAll(effectsPost);
					for (Iterator<String> drug1 = drugsPost.keySet().iterator(); drug1.hasNext();) {
						String d1 = drug1.next();
						for (Iterator<String> drug2 = drugsPost.keySet().iterator(); drug2.hasNext();) {
							String d2 = drug2.next();
							if (!d1.equals(d2)) {
								Vertex v1 = null;
								Vertex v2 = null;
								if (!vertices.containsKey(d1)) {
									if (detector.isEffect(d1)) v1 = new Vertex ("effect", d1);
									else v1 = new Vertex ("drug", d1);
									v1.setStartDate(post.getPostDate());
									vertices.put(d1,  v1);
									vnet.getNet().addVertex(v1);
								}
								else {
									v1 = vertices.get(d1);
									v1.setFreq(v1.getFreq() + 1);
								}
								
								if (!vertices.containsKey(d2)) {
									if (detector.isEffect(d2)) v2 = new Vertex ("effect", d2);
									else v2 = new Vertex ("drug", d2);
									v2.setStartDate(post.getPostDate());
									vertices.put(d2,  v2);
									vnet.getNet().addVertex(v2);
								}
								else {
									v2 = vertices.get(d2);
									v2.setFreq(v2.getFreq() + 1);
								}
								
								
								VertexTalkEdge edge = null;
								if (vnet.getNet().getEdge(v1,  v2) == null) {
									edge = new VertexTalkEdge("");
									edge.setSource(v1);
									edge.setTarget(v2);
									vnet.getNet().addEdge(v1, v2, edge);
								}
								else {
									edge = vnet.getNet().getEdge(v1,  v2);
									edge.setWeight(edge.getWeight() + 1);
								}
							}
						}
					}
				}
			}
		}
		
		exportGexfDrugs("effects_all" , vnet);

	}

	private static HashMap<String, String> detect(String body, Detector detector) {
		HashMap<String, String> resultDrugs = null;
		// drugs
		if (detector.detectDrugs(body) != null && detector.detectDrugs(body).size() > 0) {
			resultDrugs = detector.detectDrugs(body);
		}

		/*
		 * //foods if (syn.detectFoods(body) != null &&
		 * syn.detectFoods(body).size() > 0) { HashMap<String, String> result =
		 * syn.detectFoods(body); }
		 * 
		 * //effects if (syn.detectEffects(body) != null &&
		 * syn.detectEffects(body).size() > 0) { HashMap<String, String> result
		 * = syn.detectEffects(body); }
		 * 
		 * //activities if (syn.detectActivities(body) != null &&
		 * syn.detectActivities(body).size() > 0) { HashMap<String, String>
		 * result = syn.detectActivities(body); }
		 */

		return resultDrugs;
	}

	private static HashMap<String, String> detectEffects(String body, Detector detector) {
		HashMap<String, String> resultEffects = null;

		// effects
		if (detector.detectEffects(body) != null && detector.detectEffects(body).size() > 0) {
			resultEffects = detector.detectEffects(body);
		}

		return resultEffects;
	}
	
	public static void exportGexfDrugs (String name, VertexNet net) {
		Gexf gexf = new GexfImpl();
		Calendar date = Calendar.getInstance();
		gexf.getMetadata().setLastModified(date.getTime()).setCreator("Virostatiq").setDescription("bluelight drugs");
		Graph graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC).setTimeType(TimeFormat.DATE);

		AttributeList nodeattrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(nodeattrList);
		Attribute frequency = nodeattrList.createAttribute("0", AttributeType.INTEGER,"Frequency");
		Attribute type = nodeattrList.createAttribute("1", AttributeType.STRING,"Type");

		HashMap<String, Node> usedNodes = new HashMap<String, Node>();
		HashMap<String, Edge> usedEdges = new HashMap<String, Edge>();
		int vertexCount = 0;

		for (Vertex vertex : net.getNet().vertexSet()) {
			Node n = graph.createNode(vertexCount + "");
			n.setLabel(vertex.getLabel());
			n.getAttributeValues().addValue(frequency, vertex.getFreq() + "");
			n.getAttributeValues().addValue(type, vertex.getType());
			usedNodes.put(n.getLabel(), n);
			vertexCount++;
		}
		
		int edgeCount = 0;
		for (VertexTalkEdge edge : net.getNet().edgeSet()) {
			Vertex source = edge.getSource();
			Vertex target = edge.getTarget();
			Node sourceNode = usedNodes.get(source.getLabel());
			Node targetNode = usedNodes.get(target.getLabel());
			if (!usedEdges.containsKey(source.getLabel() + "-#-" + target.getLabel())) {
				if (sourceNode != null && targetNode != null) {
					Edge e = sourceNode.connectTo(edgeCount + "", targetNode);
					e.setEdgeType(EdgeType.UNDIRECTED);
					e.setWeight(edge.getWeight());
					usedEdges.put(source + "-#-" + target, e); 
				}
			}
			else {
				Edge e = usedEdges.get(source + "-#-" + target);
				e.setWeight(e.getWeight() + 1);
				System.out.println (source + "-#-" + target + " = " + e.getWeight());
				
			}
			edgeCount++;
		}
		System.out.println("used edges: " + usedEdges.size());
		System.out.println();
		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File("C:\\Users\\solipsy\\Documents\\!Data\\Gephi\\"
				+ "drugs_" + name + ".gexf");
		Writer out;
		try {
			out = new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");
			System.out.println(f.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
