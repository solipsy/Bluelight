package csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CSVExport {
	private static String ROOT = "C:\\Users\\solipsy\\Documents\\!Data\\Drugs\\";

	public static void main(String[] args) {
		export();

	}
	
	private static void export () {
		List<String[]> rows = new ArrayList<String[]> ();
		   try {
			CSVReader reader = new CSVReader(new FileReader(ROOT + "effectsbyMonth.csv"));
			rows = reader.readAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String[] row : rows) {
			String[] rrr = row[0].split(";");
			System.out.println (rrr[0] + " " + rrr[1]  );
		}
		
	}

}
