package ann001;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author catalin.podariu[at]gmail.com
 *
 */
public class Normalizer {

	private ArrayList<Double[]> csvFile;

	private String rawTextLine = "";
	private String delimiter = ",";
	private String[] rawLineValues;

	public static void main(String[] args) {
		new Normalizer().run("D:\\gap\\ann\\res\\data.csv", -100.0, 100.0);
	}

	public void run(String filePath, double min, double max) {
		csvFile = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			while ((rawTextLine = reader.readLine()) != null) {
				normalizeValue(min, max);
			}
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}

		writeToFile(csvFile, filePath);
	}

	private void normalizeValue(double min, double max) {
		Double[] csvLine = new Double[3];
		rawLineValues = rawTextLine.split(delimiter);

		for (int i = 0; i < rawLineValues.length; i++) {
			String stringValue = rawLineValues[i];
			Double value = Double.valueOf(stringValue);
			csvLine[i] = (value - (min)) / (max - (min));
		}
		csvFile.add(csvLine);
	}

	private void writeToFile(ArrayList<Double[]> csvFile, String filePath) {
		String filePathMod = filePath + "_modified.csv";

		try (FileWriter writer = new FileWriter(filePathMod)) {
			appendCSVLinesToWriter(csvFile, writer);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	private void appendCSVLinesToWriter(ArrayList<Double[]> csvFile, //
			FileWriter writer) throws IOException {

		Iterator<Double[]> csvLines = csvFile.iterator();

		while (csvLines.hasNext()) {
			Double[] temp = (Double[]) csvLines.next();

			for (int i = 0; i < temp.length; i++) {
				Double value = temp[i];
				if (i != temp.length) {
					writer.append(value.toString() + ", ");
				} else {
					writer.append(value.toString());
				}
			}
			writer.append("\n");
		}
	}

}
