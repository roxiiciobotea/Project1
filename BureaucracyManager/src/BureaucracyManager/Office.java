package BureaucracyManager;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Office implements Runnable {
	private final String name;
	private final List<Document> issuedDocs;
	private final BlockingQueue<Client> clientsQueue = new LinkedBlockingQueue<Client>();

	// for handling coffee breaks
	private final long coffeeBreakDuration = 5000; // ms
	private final Random ro = new Random();
	private int coffeeTime; // will store the random number generated
	private long startTime;
	private long crtTime;

	private boolean running = false;

	public Office(String officeName, List<Document> issuedDocs) {
		this.name = officeName;
		this.issuedDocs = issuedDocs;
	}

	/*
	 * checks whether the office's thread has been started of not
	 */
	public boolean isRunning() {
		return running;
	}

	/*
	 * returns a list with the documents issued by this office
	 */
	public List<Document> getIssuedDocs() {
		return this.issuedDocs;
	}

	/*
	 * adds a client to the queue
	 */
	public void addClient(Client c) {
		clientsQueue.add(c);
	}

	/*
	 * checks if this office issues document d
	 */
	public boolean issues(Document d) {
		if (issuedDocs.contains(d))
			return true;
		return false;
	}

	/*
	 * checks if client c has all the prerequisites in order to get document d
	 */
	private boolean checkDependencies(Client c, Document d) {
		return c.hasPrerequisiteDocs(d.getDependencies());
	}

	/*
	 * gives client c document d
	 */
	private void issueDoc(Client c, Document d) {
		if (!checkDependencies(c, d)) {
			System.out.println(this + ": " + c + " does not have the necessary documents to acquire " + d + "!");
			return;
			/*
			 * doesn't happen because the client gets the documents in a
			 * specific order
			 */
		} else {
			c.acquireDoc(d);
			System.out.println(this + " issued " + d + " for client " + c);
		}
	}

	@Override
	public String toString() {
		return this.name;
	}

	/*
	 * prints the fact that the office is up & running
	 * 
	 * checks if it's time for a coffee break
	 * 
	 * if there are clients in the queue, prints the queue
	 * 
	 * takes the first client in the queue, asks him what document does he need
	 * 
	 * in the synchronized block, issues the document for the client & notifies
	 * the client thread that the office did its job
	 */
	@Override
	public void run() {
		System.out.println(this + " is running!");
		running = true;

		startTime = System.currentTimeMillis();
		coffeeTime = ro.nextInt(60) * 1000;

		while (true) {
			crtTime = System.currentTimeMillis();
			if ((crtTime - startTime) > coffeeTime)
				try {
					System.out.println(this + ": COFFEE BREAK!");
					Thread.sleep(coffeeBreakDuration);
					System.out.println(this + ": BACK TO WORK...");

					startTime = System.currentTimeMillis();
					coffeeTime = ro.nextInt(60) * 1000;
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			if (!clientsQueue.isEmpty()) {
				System.out.println(this.name + " queue: " + clientsQueue);

				Client c = clientsQueue.poll();
				Document doc = c.requireDoc();
				synchronized (doc) {
					issueDoc(c, doc);
					doc.notify();
				}
			}

			/*
			 * waits 1 sec time between clients
			 */
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
