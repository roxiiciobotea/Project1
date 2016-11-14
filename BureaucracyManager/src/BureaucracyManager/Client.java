package BureaucracyManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Client implements Runnable {
	// used for printing
	private static int clientCounter = 0;
	private int clientNr;

	private final Document doc;
	private Map<Document, Boolean> allNeededDocs = new LinkedHashMap<Document, Boolean>();
	private Document crtReqDoc;

	public Client(Document doc) {
		this.clientNr = ++clientCounter;
		this.doc = doc;
		getIntermediaryDocs(doc);
	}

	/*
	 * adds to the allNeededDocs map all the prerequisite documents in order to
	 * get doc & also adds doc at the end
	 */
	private void getIntermediaryDocs(Document doc) {
		List<Document> dependencies = doc.getDependencies();

		for (Document d : dependencies) {
			allNeededDocs.put(d, false);
		}

		allNeededDocs.put(doc, false);
	}

	/*
	 * checks if this client has all prerequisite documents in otder to get
	 * document doc
	 */
	public boolean hasPrerequisiteDocs(List<Document> docs) {
		return allNeededDocs.keySet().containsAll(docs);
	}

	/*
	 * method will be called in Office class - this client asks the office for
	 * the currently required document
	 */
	public Document requireDoc() {
		return crtReqDoc;
	}

	/*
	 * method will be called in Office class - this client is provided with the
	 * document it asked for
	 */
	public void acquireDoc(Document d) {
		allNeededDocs.replace(d, true);
	}

	@Override
	public String toString() {
		return "Client " + clientNr;
	}

	/*
	 * prints client, the document it asked for & all the documents it needs
	 * 
	 * for each needed document, the client asks the BureaucrasticSystem what
	 * office issues the doc & sends itself to that office
	 * 
	 * in the synchronized block, the client waits for the office to issue the
	 * necessary document
	 */
	@Override
	public void run() {
		System.out.println(this + ": I want to get " + doc + ". All documents needed: " + allNeededDocs.keySet());

		for (Document d : allNeededDocs.keySet()) {
			crtReqDoc = d;
			Office o = BureaucraticSystem.getOfficeForDoc(crtReqDoc);
			o.addClient(this);
			synchronized (crtReqDoc) {
				try {
					crtReqDoc.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
