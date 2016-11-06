import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client implements Runnable {
	private static int clientCounter = 0;
	private int clientNr;
	private final Document doc;
	private Document crtReqDoc;
	private Map<Document, Boolean> intermediaryDocs = new HashMap<Document, Boolean>();

	public Client(Document doc) {
		this.clientNr = ++clientCounter;
		this.doc = doc;
		insertIntermediaryDocs(doc);
	}

	private void insertIntermediaryDocs(Document doc) {

		List<Document> dependencies = this.doc.getDependencies();

		for (Document d : dependencies) {
			intermediaryDocs.put(d, false);
		}
	}

	public boolean hasPrerequisiteDocs(List<Document> docs) {
		return intermediaryDocs.keySet().containsAll(docs);
	}

	public Document requireDoc() {
		return crtReqDoc;
	}

	public synchronized void acquireDoc(Document d) {
		intermediaryDocs.replace(d, true);
	}

	@Override
	public String toString() {
		return "Client " + clientNr;
	}

	@Override
	public void run() {
		Main.threadMessage("I need document " + doc);
		Main.threadMessage("Document prerequisites: " + intermediaryDocs.keySet());
	}

}
