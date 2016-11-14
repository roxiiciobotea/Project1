package BureaucracyManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public final class BureaucraticSystem {
	private static List<Document> allDocuments = new LinkedList<>();
	private static List<Document> allowedDocuments = new LinkedList<>();
	// documents that the clients can ask for (the docs which have dependencies)

	private static List<Office> allOffices = new LinkedList<>();

	/*
	 * returns the office that issues document d
	 */
	public static Office getOfficeForDoc(Document d) {
		for (Office o : allOffices) {
			if (o.issues(d))
				return o;
		}
		return null;
	}

	/*
	 * returns the document with the specified name
	 */
	private static Document getDocForName(String name) {
		for (Document d : allDocuments) {
			if (d.getDocName().equals(name))
				return d;
		}
		return null;
	}

	/*
	 * gets a random document from the list of available documents
	 */
	private static Document getRandomDocument() {
		Random ro = new Random();
		int rand = ro.nextInt(allowedDocuments.size() - 1);

		return allowedDocuments.get(rand);

	}

	/*
	 * creates a list of documents from an array of strings representing the
	 * document names
	 */
	private static List<Document> createDocsList(String[] list) {
		List<Document> docs = new ArrayList<>();
		for (String s : list) {
			docs.add(getDocForName(s));
		}
		return docs;
	}

	/*
	 * processes the string containing the documents (read from the config file)
	 */
	private static void processDocuments(String documents) {
		String[] docs = documents.split("; ");

		for (String s : docs) {
			if (s.matches(".*[(].*[)]")) {
				String docName = s.substring(0, s.indexOf('('));

				String[] docDependencies = s.substring(s.indexOf('(') + 1, s.indexOf(')')).split(", ");
				List<Document> dependencies = createDocsList(docDependencies);

				Document d = new Document(docName, dependencies);
				allDocuments.add(d);
				allowedDocuments.add(d);
			} else
				allDocuments.add(new Document(s));
		}
	}

	/*
	 * processes the string containing the offices (read from the config file)
	 */
	private static void processOffices(String offices) {
		String[] ofcs = offices.split("; ");

		for (String s : ofcs) {
			String ofcName = s.substring(0, s.indexOf('('));

			String[] ofcDocuments = s.substring(s.indexOf('(') + 1, s.indexOf(')')).split(", ");

			allOffices.add(new Office(ofcName, createDocsList(ofcDocuments)));
		}
	}

	public static void main(String[] args) {
		// read docs & offices from config file
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

		// create clients
		List<Client> clients = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			clients.add(new Client(getRandomDocument()));
		}

		// start office threads
		for (Office o : allOffices) {
			new Thread(o).start();
		}

		/*
		 * main thread does not move on until all office threads have been
		 * started
		 */
		while (true) {
			boolean ok = true;
			for (Office o : allOffices) {
				if (!o.isRunning())
					ok = false;
			}
			if (ok)
				break;
		}

		// start client threads
		for (Client c : clients) {
			new Thread(c).start();
		}
	}
}
