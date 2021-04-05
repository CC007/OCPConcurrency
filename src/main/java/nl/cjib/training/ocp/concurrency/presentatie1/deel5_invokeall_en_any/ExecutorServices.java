package nl.cjib.training.ocp.concurrency.presentatie1.deel5_invokeall_en_any;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Je kan ook meerdere {@link Callable} tasks tegelijk submitten.
 * Dit kan op meerdere manieren, afhankelijk van wat je bedoeling is.
 */
public class ExecutorServices
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
		System.out.println(executorServiceSingle.invokeAny(Arrays.asList(task1, task2)));



		// Zoals altijd tenminste een shutdown.
		executorServiceSingle.shutdown();

	}
}
