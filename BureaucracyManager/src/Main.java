import java.util.Arrays;

public class Main {

	public static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}

	public static void main(String[] args) {
		threadMessage("Setting up the system...");

		//create documents with their dependencies
		Document testDependency = new Document();
		Document testDoc = new Document(Arrays.asList(testDependency));
		
		//create all offices
		Office testOffice = new Office(Arrays.asList(testDoc));
		Office test2Offices = new Office(Arrays.asList(testDependency));
		
		//add offices to the system
		BureaucraticSystem testSyst = new BureaucraticSystem(Arrays.asList(testOffice,test2Offices));
		
		//create clients
		Client testClient = new Client(testDoc);
		Client test2Clients = new Client(testDependency);
		
		new Thread(testOffice).start();
		new Thread(test2Offices).start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread(testClient).start();
		new Thread(test2Clients).start();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
