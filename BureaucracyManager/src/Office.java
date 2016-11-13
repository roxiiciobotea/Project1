import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Office implements Runnable {
	private final String name;
	private final List<Document> issuedDocs;
	private final BlockingQueue<Client> queue = new LinkedBlockingQueue<Client>();

	// TODO make this a Runnable & let it handle clients

	public Office(String officeName, List<Document> issuedDocs) {
		this.name = officeName;
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
				Main.threadMessage(this + " issued " + d + " for client " + c);
			} else
				Main.threadMessage("WRONG OFFICE !!!"); // technically impossible
		} else
			Main.threadMessage("Client does not have necessary documents!");
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public void run() {
		Main.threadMessage(this + " running");
		Random ro = new Random();
		int coffeeTime = ro.nextInt(10);
		long start = System.currentTimeMillis();
		long crt;

		while (true) {
			crt = System.currentTimeMillis();
			if((crt-start) > (coffeeTime*1000))
				try {
					System.out.println(this + ": COFFEE BREAK!");
					Thread.sleep(5000);
					System.out.println(this + ": BACK TO WORK...");
					start = System.currentTimeMillis();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			
			if (!queue.isEmpty())
				System.out.println(this.name + " queue: " + queue);

			Client c = queue.poll();
			if (c != null) {
				Document doc = c.requireDoc();
				synchronized (doc) {
					issueDoc(c, doc);
					doc.notify();
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
