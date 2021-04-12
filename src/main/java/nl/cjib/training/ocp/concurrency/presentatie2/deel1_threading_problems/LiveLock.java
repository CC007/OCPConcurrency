package nl.cjib.training.ocp.concurrency.presentatie2.deel1_threading_problems;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Een deadlock is wanneer 2 of meer threads op een cyclische manier op elkaars locks of resources wachten.
 * In dit geval zijn deze threads nog wel bezig tussen de pogingen om de lock of resource te krijgen,
 * maar zal deze lock of resource nooit beschikbaar komen door de cyclische afhankelijkheid.
 * <p>
 * Dit kan bijvoorbeeld voorkomen als threads een poging om een mogelijke deadlock te voorkomen,
 * maar blijkt dat deze poging niet uitgebreid genoeg is..
 * <p>
 * Hieronder is weer het voorbeeld met de metafoor van een smalle steeg.
 * Je kan de steeg van beide kanten doorlopen, maar er is geen ruimte om elkaar te passeren.
 */
public class LiveLock
{
	/**
	 * Deze class symboliseert een steeg met 5 posities waar men kan staan in de steeg.
	 * <p>
	 * In dit geval bij het bewegen van een thread zal deze thread proberen om een stap terug te doen,
	 * als ze voor een bepaalde tijd niet naar voren kan lopen.
	 * <p>
	 * Dit is echter niet genoeg om het locking probleem op te lossen
	 */
	private static class Alley
	{
		// Voor de posities in de steeg zijn hier 5 locks gebruikt. Net als bij het deadlock voorbeeld.
		private static final Lock[] locationLocks = new Lock[]{
			new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()
		};

		public static void printAlleyStartLocation(int startLocationIndex, int threadNumber)
		{
			locationLocks[startLocationIndex].lock();
			System.out.println("Thread " + threadNumber + " starts at location " + startLocationIndex);
		}

		public static void printAlleyEndLocation(int endLocationIndex, int threadNumber)
		{
			System.out.println("Thread " + threadNumber + " ends at location " + endLocationIndex);
			locationLocks[endLocationIndex].unlock();
		}

		public static int printAlleyLocation(int oldLocationIndex, int newLocationIndex, int backupLocationIndex, int threadNumber) throws InterruptedException
		{
			// de thread probeert vooruit te lopen voor een halve seconde.
			if (locationLocks[newLocationIndex].tryLock(500, TimeUnit.MILLISECONDS)) {
				System.out.println("Thread " + threadNumber + " moves from location " + oldLocationIndex + " to location " + newLocationIndex);
				locationLocks[oldLocationIndex].unlock();
				return newLocationIndex;
			}
			// Als dit niet lukt, probeert hij terug te lopen
			if (backupLocationIndex >= 0 && backupLocationIndex <= 4) {
				locationLocks[backupLocationIndex].lock();
				System.out.println("Thread " + threadNumber + " moves back from location " + oldLocationIndex + " to location " + backupLocationIndex);
				locationLocks[oldLocationIndex].unlock();
				return backupLocationIndex;
			}
			// Als dat ook niet lukt, blijft hij staan.
			System.out.println("Thread " + threadNumber + " was unable to move");
			return oldLocationIndex;

		}
	}

	/**
	 * Hieronder worden 2 threads gedefinieerd. Beide proberen door de steeg te lopen, vanaf verschillende kanten.
	 */
	public static void main(String[] args)
	{
		new Thread(() -> {
			try {
				Alley.printAlleyStartLocation(0, 1);
				int i = 0;
				while (i < 4) {
					i = Alley.printAlleyLocation(i, i + 1, i - 1, 1);
					Thread.sleep(1000);
				}
				Alley.printAlleyEndLocation(4, 1);
			}
			catch (InterruptedException e) {
				System.exit(1);
			}
		}).start();

		new Thread(() -> {
			try {
				Alley.printAlleyStartLocation(4, 2);
				int i = 4;
				while (i > 0) {
					i = Alley.printAlleyLocation(i, i - 1, i + 1, 2);
					Thread.sleep(1000);
				}
				Alley.printAlleyEndLocation(0, 2);
			}
			catch (InterruptedException e) {
				System.exit(1);
			}
		}).start();

	}
}
