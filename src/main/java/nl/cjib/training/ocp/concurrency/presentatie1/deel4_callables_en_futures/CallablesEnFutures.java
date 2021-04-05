package nl.cjib.training.ocp.concurrency.presentatie1.deel4_callables_en_futures;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * {@link Callable} is ook een functional interface dat ontworpen is om te worden geimplementeerd door
 * een andere class waarvan de instances bedoeld zijn om uitgevoerd te worden in een andere thread.
 *
 * Deze zullen moeten worden uitgevoerd door een {link {@link ExecutorService}}.
 *
 * {@link Callable} is een generiek interface,
 * waarbij het generieke argument het return type bepaalt van de call methode.
 */
public class CallablesEnFutures
{
	/**
	 * Hieronder zie je de {@link MyCallable} class die {@link Runnable} implementeert.
	 */
	static class MyCallable implements Callable<String> {

		@Override
		public String call() throws Exception // Een callable mag ook een checked exception gooien.
		{
			System.out.println("Hello from callable");
			return "my result";
		}
	}

	public static void main(String[] args)
	{
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		// De submit methode van de ExecutorService geeft een Future<T> terug,
		// waar T het return type is van de call method van de Callable<T>.
		Future<String> stringFuture = executorService.submit(new MyCallable());

		// Hieronder zie je hoe je het starten van een task kan voorkomen of een task voortijdig kan stoppen.
//		stringFuture.cancel(true);

		// Je kan met de get methode wachten op de waarde van de future.
		// Vergeet niet om hiervoor de InterruptedException en ExecutionException exceptions af te vangen.
		try {
			System.out.println(stringFuture.get());

			// Ook het submitten van een Runnable geeft een future terug.
			// Hierbij krijg je met de get methode null van de Future zodra de task afgerond is.
			Future<?> nullFuture = executorService.submit(()-> System.out.println("Hello from runnable"));
			System.out.println(nullFuture.get());

			// Je kan ook zelf aangeven welke waarde je terug wilt krijgen als de Runnable task afgerond is
			Future<Double> resultFuture = executorService.submit(()-> System.out.println("Hello from runnable"), 3.14);
			System.out.println(resultFuture.get());
		}
		catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		// Zoals altijd tenminste een shutdown.
		executorService.shutdown();
	}
}
