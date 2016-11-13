package BureaucracyManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class BureaucraticSystem {
	private static List<Document> allDocuments = new LinkedList<>();
	private static List<Office> allOffices = new LinkedList<>();

	public static Office getOfficeForDoc(Document d) {
		for (Office o : allOffices) {
			if (o.issues(d))
				return o;
		}
		return null;
	}

	private static Document getDocForName(String name) {
		for (Document d : allDocuments) {
			if (d.getName().equals(name))
				return d;
		}
		return null;
	}

	private static List<Document> createDocsList(String[] list) {
		List<Document> docs = new ArrayList<>();
		for (String s : list) {
			docs.add(getDocForName(s));
		}
		return docs;
	}

	private static void processDocuments(String documents) {
		String[] docs = documents.split("; ");

		for (String s : docs) {
			if (s.matches(".*[(].*[)]")) {
				String docName = s.substring(0, s.indexOf('('));

				String[] docDependencies = s.substring(s.indexOf('(') + 1, s.indexOf(')')).split(", ");
				List<Document> dependencies = createDocsList(docDependencies);

				allDocuments.add(new Document(docName, dependencies));
			} else
				allDocuments.add(new Document(s));
		}
	}

	private static void processOffices(String offices) {
		String[] ofcs = offices.split("; ");

		for (String s : ofcs) {
			String ofcName = s.substring(0, s.indexOf('('));

			String[] ofcDocuments = s.substring(s.indexOf('(') + 1, s.indexOf(')')).split(", ");

			allOffices.add(new Office(ofcName, createDocsList(ofcDocuments)));
		}
	}

	public static void main(String[] args) {
		try {
			BufferedReader b = new BufferedReader(new FileReader("src/BureaucracyManagerSetup.txt"));

			processDocuments(b.readLine());
			
			processOffices(b.readLine());

			b.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
/*		for(Office o : allOffices) {
			System.out.println(o + " issues  :  " + o.getIssuedDocs());
		}
		
		for(Document d : allDocuments) {
			System.out.println(d + "  :  " + d.getDependencies());
		}*/
		
		Client c1 = new Client(getDocForName("End Document 1"));
		Client c2 = new Client(getDocForName("End Document 2"));
		Client c3 = new Client(getDocForName("End Document 3"));
		Client c4 = new Client(getDocForName("End Document 4"));
		Client c5 = new Client(getDocForName("End Document 5"));
		Client c6 = new Client(getDocForName("End Document 6"));
		Client c7 = new Client(getDocForName("End Document 7"));
		Client c8 = new Client(getDocForName("End Document 8"));
		Client c9 = new Client(getDocForName("End Document 9"));
		Client c10 = new Client(getDocForName("End Document 10"));

		for (Office o : allOffices) {
			new Thread(o).start();
		}

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Thread(c1).start();
	}
}
