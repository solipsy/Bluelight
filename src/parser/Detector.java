package parser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Detector {
	private HashMap<String, String> drugs, foods, effects, activities;

	public Detector() {
		drugs = new HashMap<String, String> ();
		foods = new HashMap<String, String> ();
		effects = new HashMap<String, String> ();
		activities = new HashMap<String, String> ();
		
		try {
			CSVReader reader = new CSVReader(new FileReader("C:\\Users\\solipsy\\Documents\\!Data\\Drugs\\drugs.txt"));
			List<String[]> rows = reader.readAll();
			for (String[] row : rows) {
				drugs.put(row[1], row[0]);
			}
			
			reader = new CSVReader(new FileReader("C:\\Users\\solipsy\\Documents\\!Data\\Drugs\\foods.txt"));
			rows = reader.readAll();
			for (String[] row : rows) {
				foods.put(row[1], row[0]);
			}
			
			reader = new CSVReader(new FileReader("C:\\Users\\solipsy\\Documents\\!Data\\Drugs\\effects.txt"));
			rows = reader.readAll();
			for (String[] row : rows) {
				effects.put(row[1], row[0]);
			}
			
			reader = new CSVReader(new FileReader("C:\\Users\\solipsy\\Documents\\!Data\\Drugs\\activities.txt"));
			rows = reader.readAll();
			for (String[] row : rows) {
				activities.put(row[1], row[0]);
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		


	}
	
	public boolean isEffect (String test) {
		if (effects.containsKey(test)) return true;
		else return false;
	}
	
	public HashMap<String, String> detectDrugs (String body) {
		HashMap<String, String> result = new HashMap<String, String> ();
		for (Iterator<String> i = drugs.keySet().iterator(); i.hasNext();) {
			String drug = i.next();
			if (body.contains(" " + drug + " ") || body.startsWith(drug + " ")) {
				result.put(drugs.get(drug), drugs.get(drug));
			}
		}
		return result;
	}
	
	public HashMap<String, Integer> detectDrugsFreq (String body) {
		HashMap<String, Integer> result = new HashMap<String, Integer> ();
		for (Iterator<String> i = drugs.keySet().iterator(); i.hasNext();) {
			String drug = i.next();
			if (body.contains(" " + drug + " ") || body.startsWith(drug + " ")) {
				if (!result.containsKey(drug)) result.put(drugs.get(drug), 1);
				else {
					result.put(drug, result.get(drug) + 1 );
				}
			}
		}
		return result;
	}
	
	public HashMap<String, Integer> detectEffectsFreq (String body) {
		HashMap<String, Integer> result = new HashMap<String, Integer> ();
		for (Iterator<String> i = effects.keySet().iterator(); i.hasNext();) {
			String drug = i.next();
			if (body.contains(" " + drug + " ") || body.startsWith(drug + " ")) {
				if (!result.containsKey(drug)) result.put(effects.get(drug), 1);
				else {
					result.put(drug, result.get(drug) + 1 );
				}
			}
		}
		return result;
	}
	
	public HashMap<String, String> detectFoods (String body) {
		HashMap<String, String> result = new HashMap<String, String> ();
		for (Iterator<String> i = foods.keySet().iterator(); i.hasNext();) {
			String drug = i.next();
			if (body.contains(" " + drug + " ") || body.startsWith(drug + " ")) {
				result.put(foods.get(drug), foods.get(drug));
			}
		}
		return result;
	}
	
	public HashMap<String, String> detectEffects (String body) {
		HashMap<String, String> result = new HashMap<String, String> ();
		for (Iterator<String> i = effects.keySet().iterator(); i.hasNext();) {
			String drug = i.next();
			if (body.contains(" " + drug + " ") || body.startsWith(drug + " ")) {
				result.put(effects.get(drug), effects.get(drug));
			}
		}
		return result;
	}
	
	public HashMap<String, String> detectActivities (String body) {
		HashMap<String, String> result = new HashMap<String, String> ();
		for (Iterator<String> i = activities.keySet().iterator(); i.hasNext();) {
			String drug = i.next();
			if (body.contains(" " + drug + " ") || body.startsWith(drug + " ")) {
				result.put(activities.get(drug), activities.get(drug));
			}
		}
		return result;
	}

}
