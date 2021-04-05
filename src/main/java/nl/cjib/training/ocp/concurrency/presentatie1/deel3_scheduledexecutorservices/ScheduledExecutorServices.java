package nl.cjib.training.ocp.concurrency.presentatie1.deel3_scheduledexecutorservices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Een {@link ScheduledExecutorService} is een speciale vorm van de {@link ExecutorService}.
 * De {@link ScheduledExecutorService} maakt gebruik ook van 1 of meer threads,
 * maar in plaats van de gesubmitte task direct uit te voeren kan deze voor een bepaalde tijd uitgesteld worden.
 */
public class ScheduledExecutorServices
{

	/**
	 * Het aanmaken van een {@link ExecutorService} kan gedaan worden met de {@link Executors} class.
	 */
	public static void main(String[] args) throws InterruptedException
	{
		// Hier is een voorbeeld van een ScheduledExecutorService met 1 thread.
		ScheduledExecutorService executorServiceSingle = Executors.newSingleThreadScheduledExecutor();

		// Hier is een voorbeeld van een ScheduledExecutorService met 5 threads.
		ScheduledExecutorService executorServiceMultiple = Executors.newScheduledThreadPool(5);

		// Hier zie hoe je een task kan schedulen in de ScheduledExecutorService.
		// Hier wordt het uitvoeren van de task met 2 seconden uitgesteld.
		executorServiceSingle.schedule(
			() -> System.out.println("Hello ExecutorService"),
			2,
			TimeUnit.SECONDS);

		// Je kan een task ook schedulen om herhaaldelijk uitgevoerd te worden
		// Hier wordt de task gescheduled met een initiele wachttijd van 2 seconden.
		// Daarna wordt de task elke seconde uitgevoerd.
		executorServiceSingle.scheduleAtFixedRate(
			() -> System.out.println("Hello ExecutorService at fixed rate"),
			2,
			1,
			TimeUnit.SECONDS);

		// Als alternatief kan je ook een herhaaldelijk uitgevoerde task definieren,
		// door de wachttijd tussen het uitvoeren van de task te specificeren
		// Hier wordt de task gescheduled met een initiele wachttijd van 2 seconden.
		// Daarna wordt tussen de tasks 1 seconde gewacht totdat de task opnieuw uitgevoerd wordt.
		executorServiceSingle.scheduleWithFixedDelay(
			() -> System.out.println("Hello ExecutorService with fixed delay"),
			2,
			1,
			TimeUnit.SECONDS);

		// Met deze static method kan je een thread (in dit geval het main thread) laten wachten voor een bepaalde tijd
		// Vergeet hierbij niet de InterruptedException af te vangen of door te gooien in de method definitie
		Thread.sleep(5000);

		// Zoals altijd tenminste een shutdown.
		// De shutdown zorgt er ook voor dat een herhaaldelijk uitgevoerde task niet opnieuw gesubmit wordt.
		executorServiceSingle.shutdown();

	}
}
