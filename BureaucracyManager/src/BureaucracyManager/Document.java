package BureaucracyManager;
import java.util.ArrayList;
import java.util.List;

public class Document {
	private final String name;
	private final List<Document> dependencies = new ArrayList<>();

	//for documents without dependencies
	public Document(String docName) {
		this.name=docName;
	}

	//for documents with dependencies
	public Document(String docName, List<Document> dependencies) {
		this.name = docName;
		this.dependencies.addAll(dependencies);
	}
	
	public String getDocName() {
		return this.name;
	}
	
	private synchronized List<Document> getAllDependencies() {
		List<Document> allDep = new ArrayList<>();
		
		for(Document d : dependencies) {
			allDep.addAll(d.getDependencies());
			allDep.add(d);
		}
		
		return allDep;
	}

	public synchronized List<Document> getDependencies() {
		return this.getAllDependencies();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
