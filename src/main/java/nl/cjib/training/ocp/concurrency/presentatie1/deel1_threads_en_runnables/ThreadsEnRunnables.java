package nl.cjib.training.ocp.concurrency.presentatie1.deel1_threads_en_runnables;

/**
 * {@link Runnable} is een functional interface dat ontworpen is om te worden geimplementeerd door
 * een andere class waarvan de instances bedoeld zijn om uitgevoerd te worden in een andere thread.
 */
public class ThreadsEnRunnables
{
	/**
	 * Hieronder zie je de {@link MyRunnable} class die {@link Runnable} implementeerd.
	 */
	static class MyRunnable implements Runnable
	{
		@Override
		public void run()
		{
			System.out.println("Hello World!");
		}
	}

	public static void main(String[] args)
	{
		// Hier zie je het gebruik van een MyRunnable instance in een thread.
		new Thread(new MyRunnable()).start();

		// Runnable is ook een functional interface,
		// dus kan je de runnable ook meegeven als lambda.
		Thread thread = new Thread(
			() -> System.out.println("Hello Lambda")
		);
		// Je kan de priority van een thread ook veranderen.
		// Normaal heeft een thread een priority van 5 (Thread.NORM_PRIORITY)
		// In dit geval heeft het zetten van een priority niet veel nut,
		// want dit zijn kortdurende threads.
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

		// De main methode zelf draait ook altijd in een eigen thread (de main thread).
		// Als je zelf geen nieuwe threads maken, dan is het programma single-threaded*.
		System.out.println("Hello from main thread");
	}
}

// * Er zijn nog wel enkele systeem threads, waaronder de garbage collector,
//   maar als er maar 1 user thread is wordt de applicatie vaak alsnog single-threaded genoemd.