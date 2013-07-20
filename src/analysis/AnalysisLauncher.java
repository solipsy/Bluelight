package analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import index.SolrSrvr;
import network.Net;
import network.TalkEdge;
import parser.Detector;
import parser.Post;

public class AnalysisLauncher {
	private static SolrSrvr solr;
	private static Detector detector;
	private static Net net;
	private static final String [] DRUGS = {"lsd", "mdma", "methylone", "mephedrone", "3-mmc", "2c-b", "2c-i", "2c-c", "2c-e", "2c-d", "2c-p", "ayahuasca", "methadone", "cocaine",
		"amphetamine", "methamphetamine", "mdpv", "marijuana", "synthetic cannabinoids", "fentanyl", "vicodin", "ghb", "gbl", "alcohol", "pcp", "ketamine", "methoxetamine", "valium", "xanax", "viagra", "cialis", "25i-nbome", "25-c nbome", 
		"mushrooms", "dpt", "dmt", "syrian rue", "2c-t-7", "5-apb", "6-apb", "mdai", "5-meo-dalt", "oxycontin", "piracetam", "salvia", "tramadol", "zoloft", "fluoxetine", "fluoxetin", "bromo-dragonfly", "mda", "peyote", "datura", 
		"ambien", "tobacco", "nootropics", "jwh-015", "jwh-018", "jwh-073", "jwh-081", "poppers", "kratom", "14-butanediol", "zoloft", "aniracetam", "lorazepam", "alprazolam", "buprenorphine",
		"hydromorphone", "hydrocodone", "oxymorphone", "codeine", "morphine", "pcp", "dxm", "mescaline", "mipt", "dipt", "amt", "mda", "n2o", "ibogaine", "diazepam", "clonazepam", "modafinil", "ephedrine",
		"scopolamine", "mimosa hostilis", "4-aco-dmt", "4-ho-mipt", "eipt", "proscaline", "4-ho-dipt", "tma", "tma-4", "4-ho-met", "tma-5", "dom", "doi", "doc", "suboxone", "klonopin", "haloperidol", "methylphenidate",
		"dmt", "2-ai", "etizolam", "sts-135", "methiopropamine", "synthacaine"};
	
	private static final String [] EFFECTS = {"comedown", "withdrawal", "hallucinations", "illusions", "serotonin syndrome", "anti-addiction", "addiction", 
		"morish", "pain", "music enhancement", "creativity", "dry eyes", "red eyes", "bitter taste", "depression", "suicide", "allergic reaction", "k-hole", 
		"visuals", "good feeling", "euphoria", "dysphoria", "overwhelming", "fear", "party", "ego loss", "mystical", "gratitude", "control", "dissapointment", 
		"tolerance", "exhaustion", "empathy", "hearing loss", "vasoconstriction", "psychosis", "overdose", "epilepsy", "stress relief", "brain aneurysm", "aneurysm", 
		"liver damage", "kidney damage", "nerve damage", "nystagmus", "oxidative stress", "jail", "bad trip", "flashback", "synaesthesia", "bodily harm", "fever", 
		"glossolalia", "ptsd", "apathy", "mood swings", "dilated pupils", "high temperature", "appetite loss", "schizophrenia", "delirium", "spiritual", "tachycardia", 
		"palpitations", "blood pressure", "heart attack", "dry mouth", "unpleasant taste", "diarrhea", "constipation", "anorexia", "weight loss", "urticaria", 
		"impotence", "urination", "defecation", "erection", "anger", "dizziness", "enjoying", "discomfort", "emotional", "nausea", "body load", "sweating", "headache",
		"throwing up", "nervousness", "trouble sleeping", "appetite", "gangrene", "death", "sleep", "meditation", "anxiety", "visions", "promiscuity", "dieting", 
		"pleasure", "brain damage", "damage", "success", "relaxation", "recovery", "abuse", "craving", "being a junkie", "sickness", "adhd", "binge", "drug test", 
		"employment", "unemployment", "confusion", "crying", "sobbing", "fainting", "uncounsciousness", "clairvoyance", "murder", "theft", "crime", "therapy", "tracers", 
		"disassociation", "dancing", "insomnia", "transformation", "out of body experience", "telepathy", "laughing", "happiness", "dreaming", "nightmares", 
		"contracted pupils", "being high", "tiredness", "regret"};
	

	public static void main(String[] args) {
		Detector syn = new Detector();
		solr = new SolrSrvr();
		try {
			goCal("effects_all", syn, "drugs");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// getEffects("");
	}

	public static void goCal(String term, Detector syn, String type) throws ParseException {
		ArrayList<HashMap<String, Integer>> endResult = new ArrayList<HashMap<String, Integer>>();
		try {
			FileWriter fstream = new FileWriter(
					"C:\\Users\\solipsy\\Documents\\!Data\\Drugs\\freq_byMonth_drugs"
							+ term + ".txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int year = 2010; year < 2014; year++) {
				for (int month = 1; month < 13; month++) {
//					for (int day = 1; day < 31; day++) {
						System.out.println(month + " / " + year);
						Calendar cal = Calendar.getInstance();
						cal.set(year, month, 1, 0, 0);
						Date d1 = cal.getTime();
						cal.set(year, month + 1, 1, 0, 0);
						Date d2 = cal.getTime();

						ArrayList<Post> result = solr.getPostsByDate(
								solr.getIndexedDate(d1),
								solr.getIndexedDate(d2), "*");
						HashMap<String, Integer> dayResult = new HashMap<String, Integer>();
						for (Post post : result) {
							HashMap<String, Integer> drugs = new HashMap<String, Integer>();
							if (type.equals("drugs")) {
								drugs = detectDrugsWithFreq(post.getBody(), syn);
							}
							else if (type.equals("effects")) {
								drugs = detectEffectsFreq(post.getBody(), syn);
							}
							if (drugs != null) {
								for (Map.Entry<String, Integer> entry : drugs
										.entrySet()) {
									if (!dayResult.containsKey(entry.getKey()))
										dayResult.put(entry.getKey(), 1);
									else
										dayResult.put(entry.getKey(),dayResult.get(entry.getKey()) + 1);
								}
							}
						}
						endResult.add(dayResult);
					}
//				}
			}
			for (String drug : DRUGS) {
				out.write (drug + "\t");
			}
			out.write("\n");
			for (HashMap<String, Integer> oneDay : endResult) {
				for (String drug : DRUGS) {
					if (oneDay.containsKey(drug)) {
						out.write(oneDay.get(drug) + "\t");
					} else out.write("0\t");
				}
				out.write("\n");

			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static HashMap<String, Integer> detectDrugsWithFreq(String body,
			Detector syn) {
		HashMap<String, Integer> resultDrugs = null;
		// drugs
		if (syn.detectDrugs(body) != null && syn.detectDrugs(body).size() > 0) {
			resultDrugs = syn.detectDrugsFreq(body);
		}
		return resultDrugs;
	}

	private static HashMap<String, String> detect(String body, Detector syn) {
		HashMap<String, String> resultDrugs = null;
		// drugs
		if (syn.detectDrugs(body) != null && syn.detectDrugs(body).size() > 0) {
			resultDrugs = syn.detectDrugs(body);
		}
		return resultDrugs;
	}
	
	private static HashMap<String, Integer> detectEffectsFreq(String body, Detector syn) {
		HashMap<String, Integer> resultDrugs = null;

		// effects
		if (syn.detectEffects(body) != null && syn.detectEffectsFreq(body).size() > 0) {
			resultDrugs = syn.detectEffectsFreq(body);
		}

		return resultDrugs;
	}

	private static HashMap<String, String> detectEffects(String body, Detector syn) {
		HashMap<String, String> resultDrugs = null;

		// effects
		if (syn.detectEffects(body) != null && syn.detectEffects(body).size() > 0) {
			resultDrugs = syn.detectEffects(body);
		}

		return resultDrugs;
	}

}
