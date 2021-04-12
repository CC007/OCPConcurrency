package nl.cjib.training.ocp.concurrency.presentatie2.deel1_threading_problems;

/**
 * Starvation is wanneer een bepaalde thread een lock onnodig lang vasthoudt,
 * waardoor andere threads niet (vaak) de kans krijgen om deze lock in handen te krijgen.
 */
public class Starvation
{
	public static synchronized void printThreadNumber(int threadNumber)
	{
		System.out.println("Thread " + threadNumber + " has the lock");

		try {
			Thread.sleep(500);
		}
		catch (InterruptedException e) {
			System.exit(1);
		}
	}

	/**
	 * Hier is een methode die een synchronized methode 10 keer aanroept.
	 * Deze methode zelf is niet synchronized,
	 * waardoor de lock tussen alle aanroepen van de synchronized methode afgegeven kan worden
	 */
	private static void cheapPrintThreadNumber(int threadNumber)
	{
		for (int i = 0; i < 10; i++) {
			printThreadNumber(threadNumber);
		}
	}

	/**
	 * Hier is een synchronized methode die 10 keer een bepaalde taak uitvoert.
	 * Al deze tijd heeft het thread dat deze methode uitvoert de lock in handen,
	 * zodat andere threads de lock niet in handen kunnen krijgen.
	 */
	private static synchronized void greedyPrintThreadNumber(int threadNumber)
	{
		for (int i = 0; i < 10; i++) {
			printThreadNumber(threadNumber);
		}
	}

	public static void main(String[] args)
	{
		new Thread(() -> {
			while (true) {
				cheapPrintThreadNumber(1);
			}
		}).start();

		new Thread(() -> {
			while (true) {
				greedyPrintThreadNumber(2);
			}
		}).start();
	}
}
