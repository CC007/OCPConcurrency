package nl.cjib.training.ocp.concurrency.presentatie1.deel2_executorservices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * In plaats van zelf een thread aan te maken, kan je ook gebruik maken van een {@link ExecutorService}.
 * De {@link ExecutorService} maakt gebruik van 1 of meer threads en bepaalt op welke thread taken uitgevoerd worden.
 */
public class ExecutorServices
{

	/**
	 * Het aanmaken van een {@link ExecutorService} kan gedaan worden met de {@link Executors} class.
	 */
	public static void main(String[] args)
	{
		// Hier is een voorbeeld van een ExecutorService met 1 thread.
		ExecutorService executorServiceSingle = Executors.newSingleThreadExecutor();

		// Hier is een voorbeeld van een ExecutorService met 5 threads.
		ExecutorService executorServiceMultiple = Executors.newFixedThreadPool(5);

		// Hier is een voorbeeld van een ExecutorService die nieuwe threads maakt wanneer nodig.
		// Hierbij worden threads die weer vrijkomen hergebruikt.
		ExecutorService executorServiceCached = Executors.newCachedThreadPool();

		// Hier zie hoe je een task kan submitten naar de ExecutorService.
		executorServiceSingle.submit(() -> System.out.println("Hello ExecutorService"));

		// Een ExecutorService moet afgesloten worden, om te voorkomen dat threads onnodig blijven draaien.
		// De shutdown methode zorgt ervoor dat geen nieuwe tasks meer gesubmit kunnen worden.
		// De nog draaiende tasks worden nog wel afgerond.
		executorServiceSingle.shutdown();

		// Als alternatief kan je shutdownNow aanroepen. Deze zorgt dat ook alle draaiende tasks worden afgesloten.
		executorServiceSingle.shutdownNow();
	}
}
