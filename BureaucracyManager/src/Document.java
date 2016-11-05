import java.util.ArrayList;
import java.util.List;

public class Document {
	private static int docsCounter = 0;
	private int docNr;
	private final List<Document> dependencies = new ArrayList<>();

	public Document() {
		this.docNr = ++docsCounter;
	}

	public Document(List<Document> dependencies) {
		this.docNr = ++docsCounter;
		this.dependencies.addAll(dependencies);
	}

	public List<Document> getDependencies() {
		return this.dependencies;
	}

	@Override
	public String toString() {
		return "Document " + docNr;
	}
}
