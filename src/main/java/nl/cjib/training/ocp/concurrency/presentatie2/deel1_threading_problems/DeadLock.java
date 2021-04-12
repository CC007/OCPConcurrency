package nl.cjib.training.ocp.concurrency.presentatie2.deel1_threading_problems;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Een deadlock is wanneer 2 of meer threads op een cyclische manier op elkaars locks of resources wachten.
 * In dit geval doen deze threads niets anders dan wachten to de lock of resource beschikbaar is.
 *
 * Hieronder is een voorbeeld met de metafoor van een smalle steeg.
 * Je kan de steeg van beide kanten doorlopen, maar er is geen ruimte om elkaar te passeren.
 */
public class DeadLock
{
	/**
	 * Deze class symboliseert een steeg met 5 posities waar men kan staan in de steeg.
	 */
	private static class Alley
	{
		// Voor de posities in de steeg zijn hier 5 locks gebruikt. Elke lock symboliseert een locatie in de steeg.
		// Er is hier gebruik gemaakt van ReentrantLock objecten ipv het synchronized keyword, puur als voorbeeld,
		// omdat je daarmee meer controle hebt over wanneer gelockt en geunlockt wordt.
		// De preciese werking hiervan is verder niet belangrijk voor het OCP.
		private static final Lock[] locationLocks = new Lock[]{
			new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()
		};

		public static void printAlleyStartLocation(int startLocationIndex, int threadNumber) {
			locationLocks[startLocationIndex].lock();
			System.out.println("Thread " + threadNumber + " starts at location " + startLocationIndex);
		}

		public static void printAlleyEndLocation(int endLocationIndex, int threadNumber) {
			System.out.println("Thread " + threadNumber + " ends at location " + endLocationIndex);
			locationLocks[endLocationIndex].unlock();
		}

		public static void printAlleyLocation(int oldLocationIndex, int newLocationIndex, int threadNumber) {
			locationLocks[newLocationIndex].lock();
			System.out.println("Thread " + threadNumber + " moves from location " + oldLocationIndex + " to location " + newLocationIndex);
			locationLocks[oldLocationIndex].unlock();
		}
	}

	/**
	 * Hieronder worden 2 threads gedefinieerd. Beide proberen door de steeg te lopen, vanaf verschillende kanten.
	 *
	 * Als slechts het ene of het andere thread gestart wordt, dan kan deze zonder problemen door de steeg lopen.
	 * Echter als beide tegelijk door de steeg proberen te lopen kunnen ze op een gegeven moment niet verder,
	 * omdat ze elkaar in de weg zitten.
	 */
	public static void main(String[] args)
	{
		new Thread(() -> {
			Alley.printAlleyStartLocation(0, 1);
			for (int i = 0; i < 4; i++) {
				Alley.printAlleyLocation(i, i+1, 1);
				sleep();
			}
			Alley.printAlleyEndLocation(4, 1);
		}).start();

		new Thread(() -> {
			Alley.printAlleyStartLocation(4, 2);
			for (int i = 4; i > 0; i--) {
				Alley.printAlleyLocation(i, i-1, 2);
				sleep();
			}
			Alley.printAlleyEndLocation(0, 2);
		}).start();
	}

	/**
	 * We gebruiken een sleep om zeker te zijn dat beide threads hun startpositie in de steeg weten te vinden.
	 * Daarnaast geeft dit ook meer duidelijkheid in de output.
	 */
	private static void sleep()
	{
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			System.exit(1);
		}
	}
}
