import java.util.List;

public class Office {
	private static int officeCounter = 0;
	private int officeNr;
	private final List<Document> issuedDocs;

	public Office(List<Document> issuedDocs) {
		this.officeNr = ++officeCounter;
		this.issuedDocs = issuedDocs;
	}

	public boolean issues(Document d) {
		if (issuedDocs.contains(d))
			return true;
		return false;
	}

	public void issueDoc(Client c, Document d) {
		if (c.hasDocs(d.getDependencies())) {
			if (issuedDocs.contains(d)) {
				c.acquiredDoc(d);
				Main.threadMessage(this + " issued doc " + d + " for client " + c);
			} else
				Main.threadMessage("CLIENT DID NOT ACQUIRE DOC !!!");
		} else
			Main.threadMessage("Client does not have necessary documents!");
	}

	@Override
	public String toString() {
		return "Office " + officeNr;
	}
}
