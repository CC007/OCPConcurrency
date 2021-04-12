package nl.cjib.training.ocp.concurrency.presentatie2.deel1_threading_problems;

import java.util.Random;

/**
 * Een race condition is als meerdere threads dezelfde resource proberen te veranderen,
 * waarbij de volgorde en het al dan niet gebruik maken van locks de uiteindelijke state van de resource zal bepalen.
 */
public class RaceCondition
{
	private static final Random r = new Random(System.nanoTime());

	private static class Task {
		private int number = 1;

		public void plusTwo() {
			int number = this.number + 2;
			// De random sleep wordt hier gebruikt om de tijd tussen de read en de write van het nummer wat te verhogen
			randomSleep(r.nextInt(10));
			System.out.println("Addition");
			this.number = number;
		}
		public void triple() {
			int number = this.number * 3;
			randomSleep(r.nextInt(10));
			System.out.println("Multiplication");
			this.number = number;
		}
		public int getNumber(){
			return number;
		}
	}


	/**
	 * In dit voorbeeld wordt een object gemaakt met de waarde 1.
	 * Hierbij wordt parallel 1 keer de waarde verdrievoudigd en 1 keer 2 bij de waarde opgeteld.
	 */
	private static void printTaskResult() throws InterruptedException
	{
		Task task = new Task();

		// De random sleep wordt hier gebruikt om te kunnen laten zien dat beide threads in willekeurige volgorde
		// of gelijktijdig de resource proberen te veranderen, wat zorgt voor een race condition.
		new Thread(() -> {
			randomSleep(r.nextInt(100));
			task.plusTwo();
		}).start();

		new Thread(() -> {
			randomSleep(r.nextInt(100));
			task.triple();
		}).start();

		Thread.sleep(1000);

		// Dit kan 3 mogelijke waarden printen:
		// * 5, als eerst vermenigvuldigd wordt en daarna opgeteld
		// * 9, als eerst wordt opgeteld en daarna vermenigvuldigd
		// * 3, als het vermenigvuldigen en optellen tegelijkertijd gebeurt
		System.out.println(task.getNumber());
	}
	private static void randomSleep(int millis)
	{
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			System.exit(1);
		}
	}

	/**
	 * We voeren printTaskResult hier meerdere keren uit om te laten zien
	 * dat je daadwerkelijk verschillende antwoorden kan krijgen
	 */
	public static void main(String[] args) throws InterruptedException
	{
		while (true) {
			printTaskResult();
		}
	}
}
