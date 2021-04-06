package nl.cjib.training.ocp.concurrency.presentatie1.deel6_synchronized_blocks;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Als je werkt met het synchronized keyword, dan maak je gebruik van een intrinsic lock (ook wel een monitor genoemd).
 * <p>
 * Elk object heeft precies 1 intrinsic lock,
 * waardoor deze gebruikt kan worden om exclusieve toegang te geven tot het object.
 * <p>
 * Als thread toegang nodig heeft een synchronized code block,
 * dan moet deze thread de gerelateerde intrinsic lock zien te krijgen.
 * Na het synchronized code block geeft de thread de intrinsic lock weer terug.
 * <p>
 * Zolang een thread in het bezit is van de intrinsic lock,
 * kunnen de andere threads deze lock niet in bezit krijgen.
 * Deze threads zullen dan dus wachten totdat de intrinsic lock weer beschikbaar is.
 */
public class SynchronizedBlocks
{
	private static int counter;
	private static final Object lock = new Object();

	/**
	 * Hier zie je een method waarin geen synchronized block gedefinieerd is.
	 *
	 * Meerdere threads kunnen tegelijkertijd proberen om de waarde van de counter op te hogen
	 * Dit kan ervoor zorgen dat de waarde niet altijd goed opgehoogd wordt.
	 */
	private static boolean inc()
	{
		counter++;
		return true;
	}

	/**
	 * Hier zie je een method waarin een synchronized block gedefinieerd is.
	 * Hiervoor is de "lock" variabele als lock gebruikt, maar je kan theoretisch elk object gebruiken.
	 *
	 * Er kan maar 1 thread tegelijk de waarde ophogen
	 * Dit garandeert dat de waarde goed opgehoogd wordt.
	 */
	private static boolean syncedInc()
	{
		synchronized (lock) {
			counter++;
		}
		return true;
	}

	/**
	 * Je kan ook synchronized gebruiken op de method zelf.
	 * In dat geval wordt de het object van de method gebruikt als lock
	 * (of in dit geval de class van de method, omdat de method static is)
	 */
	private static synchronized boolean methodSyncedInc()
	{
		counter++;
		return true;
	}

	/**
	 * Als voorbeeld simuleren we een groot aantal tasks,
	 * die door een substantiele hoeveelheid threads uitgevoerd wordt.
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		List<Callable<Boolean>> tasks = Collections.nCopies(50000, SynchronizedBlocks::inc);
		List<Callable<Boolean>> syncedTasks = Collections.nCopies(50000, SynchronizedBlocks::syncedInc);
		List<Callable<Boolean>> methodSyncedTasks = Collections.nCopies(50000, SynchronizedBlocks::methodSyncedInc);
		List<Future<Boolean>> futures;

		// Voorbeeld voor tasks waarbij geen synchronized gebruikt wordt.
		counter = 0;
		futures = executorService.invokeAll(tasks);
		for (Future<Boolean> future : futures) {
			future.get();
		}
		System.out.println(counter);

		// Voorbeeld voor tasks waarbij een synchronized block gebruikt wordt.
		counter = 0;
		futures = executorService.invokeAll(syncedTasks);
		for (Future<Boolean> future : futures) {
			future.get();
		}
		System.out.println(counter);

		// Voorbeeld voor tasks waarbij een synchronized method gebruikt wordt.
		counter = 0;
		futures = executorService.invokeAll(methodSyncedTasks);
		for (Future<Boolean> future : futures) {
			future.get();
		}
		System.out.println(counter);

		executorService.shutdown();
	}
}
