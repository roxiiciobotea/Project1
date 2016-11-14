package BureaucracyManager;

import java.util.ArrayList;
import java.util.List;

public class Document {
	private final String name;
	private final List<Document> dependencies = new ArrayList<>();

	/*
	 * for documents without dependencies
	 */
	public Document(String docName) {
		this.name = docName;
	}

	/*
	 * for documents with dependencies
	 */
	public Document(String docName, List<Document> dependencies) {
		this.name = docName;
		this.dependencies.addAll(dependencies);
	}

	/*
	 * returns the document name
	 */
	public String getDocName() {
		return this.name;
	}

	/*
	 * returns the entire dependency tree for this document
	 */
	public List<Document> getDependencies() {
		List<Document> allDep = new ArrayList<>();

		for (Document d : dependencies) {
			allDep.addAll(d.getDependencies());
			allDep.add(d);
		}

		return allDep;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
