package nl.cjib.training.ocp.concurrency.presentatie2.deel3_cyclic_barriers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Een {@link CyclicBarrier} kan worden gebruikt om threads met elkaar te synchroniseren.
 */
public class CyclicBarriers
{
	/**
	 * De {@link CyclicBarrier} heeft 2 constructors:
	 *  - Bij de ene geef je alleen het aantal threads op dat nodig is om de barrier te trippen;
	 */
	private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
	/**
	 *  - Bij de andere geef je naast dat aantal ook nog een {@link Runnable} mee die uitgevoerd wordt
	 *    als de barrier getript wordt.
	 */
	private static final CyclicBarrier cyclicBarrierMetRunnable = new CyclicBarrier(2, () -> System.out.println("Barrier tripped"));
	public static void main(String[] args) throws InterruptedException
	{

		// met getParties haal je het aantal parties op dat nodig is om de barrier te trippen.
		System.out.println("Parties needed to trip the barrier: " + cyclicBarrier.getParties());

		new Thread(() -> task(1)).start();

		Thread.sleep(50);

		// met getNumberWaiting haal je het aantal parties op dat al voor de barrier aan het wachten is.
		System.out.println("Number waiting: " + cyclicBarrierMetRunnable.getNumberWaiting());

		new Thread(() -> task(2)).start();

		otherFeatures();
	}

	private static void task(int threadNumber)
	{
		try {
			// Je kan await gebruiken om te wachten voor de barrier.
			System.out.println("Thread " + threadNumber + " waiting for barrier...");
			cyclicBarrierMetRunnable.await();
			System.out.println("Thread " + threadNumber + " done waiting for barrier");

			// De barrier is herbruikbaar (vandaar de naam CyclicBarrier)
			System.out.println("Thread " + threadNumber + " waiting for barrier...");
			cyclicBarrierMetRunnable.await();
			System.out.println("Thread " + threadNumber + " done waiting for barrier");
		}
		catch (InterruptedException e) {
			System.exit(1);
		}
		catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	private static void otherFeatures() throws InterruptedException
	{
		Thread.sleep(1000);

		try {
			// Je kan bij await ook een timeout opgeven.
			// Bij een timeout krijg je een TimeoutException
			cyclicBarrierMetRunnable.await(1, TimeUnit.SECONDS);
		}
		catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		catch (TimeoutException e) {
			System.out.println("The barrier wasn't tripped in the timeout period");
		}
		// Een timeout zorgt er ook voor dat de CyclicBarrier daarna broken is.
		System.out.println("isBroken: " + cyclicBarrierMetRunnable.isBroken());

		try {
			cyclicBarrierMetRunnable.await();
		}
		catch (BrokenBarrierException e) {
			// Dit resulteert in een BrokenBarrierException als je nog een keer probeert te wachten op de barrier
			System.out.println("The barrier is broken and needs to be reset");
		}

		// Je kan de CyclicBarrier weer gereed maken voor gebruik met een reset.
		cyclicBarrierMetRunnable.reset();
		System.out.println("isBroken na reset: " + cyclicBarrierMetRunnable.isBroken());

		// Als je een reset doet vanuit een andere thread terwijl er al een thread op de barrier wacht...
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.schedule(cyclicBarrierMetRunnable::reset, 2, TimeUnit.SECONDS);
		try {
			System.out.println("Waiting at the barrier after the first reset...");
			cyclicBarrierMetRunnable.await();
		}
		// ...dan wordt de barrier voor dit wachtende thread ook als gebroken beschouwd
		// en zal deze een BrokenBarrierException geven.
		catch (BrokenBarrierException e) {
			System.out.println("The barrier was broken by resetting it again, while the thread was waiting.");
		}
		scheduledExecutorService.shutdown();
	}
}
