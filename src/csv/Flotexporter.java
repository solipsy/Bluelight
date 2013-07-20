package csv;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Flotexporter {
	private static final String ROOT = "C:\\Users\\solipsy\\Documents\\!Data\\Drugs\\";

	public static void main(String[] args) {
		try {
			export();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		xAxis();
	}
	
	private static void xAxis () {
		for (int year = 2010; year < 2014; year ++) {
			for (int month = 0; month < 12; month ++) {
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, 1);
				System.out.println(cal.getTime().getTime()); //.getTime()
			}
		}
	}
	
	private static void export () throws IOException {
		List<String[]> rows = new ArrayList<String[]> ();
		try {
			CSVReader reader = new CSVReader(new FileReader(ROOT + "drugsbyMonth.csv"));
			rows = reader.readAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FileWriter fstream = new FileWriter(ROOT + "flotdrugsMonth.txt");
		BufferedWriter out = new BufferedWriter(fstream);
		
		int length = rows.get(0)[0].split(";").length;
		
		for (int len = 1; len < length; len ++ ) {
			int c = 0;
			String [] header = rows.get(0)[0].split(";");
			out.write ("\"" + header[len] + "\": {\n");
			out.write ("\tlabel: \"" + header[len] + "\",\n");
			out.write ("\tdata: ");
			out.write("[");
			int rrr = 0;
			for (String [] row : rows) {
				
				String [] nr = row[0].split(";");
				System.out.println ( nr[0]);
				if (c > 0) out.write("[" + nr[0] + ", " + nr[len] + "]");
				if (rrr < 39 && rrr > 0) out.write(", " );
//				if (c > 0) System.out.print("[" + rrr + ", " + nr[len] + "], ");
				c++;
				rrr++;
			}
			out.write("]\n},\n");

//			System.out.println (length);
		}
		out.flush();
		out.close();

	}
	
	

}
