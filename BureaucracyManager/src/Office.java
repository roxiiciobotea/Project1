import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Office implements Runnable {
	private static int officeCounter = 0;
	private int officeNr;
	private final List<Document> issuedDocs;
	private final BlockingQueue<Client> queue = new LinkedBlockingQueue<Client>();

	// TODO make this a Runnable & let it handle clients

	public Office(List<Document> issuedDocs) {
		this.officeNr = ++officeCounter;
		this.issuedDocs = issuedDocs;
	}

	public synchronized void addClient(Client c) {
		queue.add(c);
	}

	public boolean issues(Document d) {
		if (issuedDocs.contains(d))
			return true;
		return false;
	}

	private void issueDoc(Client c, Document d) {
		if (c.hasPrerequisiteDocs(d.getDependencies())) {
			if (issuedDocs.contains(d)) {
				c.acquireDoc(d);
				Main.threadMessage(this + " issued doc " + d + " for client " + c);
			} else
				Main.threadMessage("WRONG OFFICE !!!"); // impossible
		} else
			Main.threadMessage("Client does not have necessary documents!");
	}

	@Override
	public String toString() {
		return "Office " + officeNr;
	}

	@Override
	public void run() {
		Main.threadMessage(this + " running");

		while (true) {
			System.out.println(this + "queue: " + queue);

			Client c = queue.poll();
			// for (Client c : queue) {
			if (c != null) {
				Document doc = c.requireDoc();
				synchronized (doc) {
					issueDoc(c, doc);
					doc.notifyAll();
				}
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
