package Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import BureaucracyManager.*;

public class ReadTest {
	static List<Document> allDocuments = new ArrayList<>();
	static List<Office> allOffices = new ArrayList<>();

	private static Document getDocForName(String name) {
		for (Document d : allDocuments) {
			if (d.getName() == name)
				return d;
		}
		return new Document(name);
	}

	private static List<Document> createDocsList(String[] list) {
		List<Document> docs = new ArrayList<>();
		for (String s : list) {
			docs.add(getDocForName(s));
		}
		return docs;
	}

	public static void processDocuments(String documents) {
		String[] docs = documents.split("; ");

		for (String s : docs) {
			if (s.matches(".*[(].*[)]")) {
				String docName = s.substring(0, s.indexOf('('));

				String[] docDependencies = s.substring(s.indexOf('(') + 1, s.indexOf(')')).split(", ");

				allDocuments.add(new Document(docName, createDocsList(docDependencies)));
			}
			else
				allDocuments.add(new Document(s));
		}
	}

	public static void processOffices(String offices) {
		String[] ofcs = offices.split("; ");
		
		for(String s : ofcs) {
			String ofcName = s.substring(0, s.indexOf('('));
			
			String[] ofcDocuments = s.substring(s.indexOf('(') + 1, s.indexOf(')')).split(", ");
			
			allOffices.add(new Office(ofcName, createDocsList(ofcDocuments)));
		}
	}

	public static void main(String[] args) {
		try {
			BufferedReader b = new BufferedReader(new FileReader("src/BureaucracyManagerSetup.txt"));

			String documents = b.readLine();
			processDocuments(documents);
			String offices = b.readLine();
			processOffices(offices);

			b.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(allDocuments);
		System.out.println(allOffices);

	}
}
