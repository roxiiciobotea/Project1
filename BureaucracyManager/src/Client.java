import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client implements Runnable {
	private static int clientCounter = 0;
	private int clientNr;
	private final Document doc;
	private Map<Document, Boolean> intermediaryDocs = new HashMap<Document, Boolean>();

	public Client(Document doc) {
		this.clientNr = ++clientCounter;
		this.doc = doc;
	}

	private void insertIntermediaryDocs(Document doc) {
		List<Document> dependencies = this.doc.getDependencies();

		for (Document d : dependencies) {
			intermediaryDocs.put(d, false);
		}
	}

	public void acquiredDoc(Document d) {
		intermediaryDocs.replace(d, true);
	}
	
	public boolean hasDocs(List<Document> docs){
		return intermediaryDocs.keySet().containsAll(docs);
	}

	@Override
	public String toString() {
		return "Client " + clientNr;
	}

	@Override
	public void run() {
		Office o = BureaucraticSystem.getOfficeForDoc(doc);

		Main.threadMessage("I need document " + doc);
		insertIntermediaryDocs(doc);

		Main.threadMessage("Document dependencies: " + intermediaryDocs.keySet());
		for (Document d : intermediaryDocs.keySet()) {
			Office io = BureaucraticSystem.getOfficeForDoc(d);
			io.issueDoc(this, d);
		}

		o.issueDoc(this, doc);

		Main.threadMessage("thx,bye");
	}

}
