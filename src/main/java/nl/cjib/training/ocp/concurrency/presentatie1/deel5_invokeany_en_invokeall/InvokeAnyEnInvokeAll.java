package nl.cjib.training.ocp.concurrency.presentatie1.deel5_invokeany_en_invokeall;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Je kan ook meerdere {@link Callable} tasks tegelijk submitten.
 * Dit kan op meerdere manieren, afhankelijk van wat je bedoeling is.
 */
public class InvokeAnyEnInvokeAll
{

	/**
	 * Het aanmaken van een {@link ExecutorService} kan gedaan worden met de {@link Executors} class.
	 */
	public static void main(String[] args) throws ExecutionException, InterruptedException
	{
		ExecutorService executorServiceSingle = Executors.newSingleThreadExecutor();

		Callable<String> task1 = () -> "Task 1";
		Callable<String> task2 = () -> "Task 2";

		// Hier zie hoe je meerdere tasks kan submitten naar de ExecutorService.
		// Hierbij wordt gewacht tot het resultaat terugkomt van de task die als eerste compleet is.
		System.out.println(
			"Result: " + executorServiceSingle.invokeAny(Arrays.asList(task1, task2))
		);

		// Als je wilt dat alle tasks uitgevoerd moeten worden, dan kan je invokeAll gebruiken
		List<Future<String>> resultFutures = executorServiceSingle.invokeAll(Arrays.asList(task1, task2));

		// De futures die je terugkrijgt van invokeAll zijn in dezelfde volgorde als de tasks die je meegaf als param
		for (Future<String> x : resultFutures) {
			System.out.println(x.get());
		}

		// Zoals altijd tenminste een shutdown.
		executorServiceSingle.shutdown();

	}
}
