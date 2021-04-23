package nl.cjib.training.ocp.concurrency.presentatie3.deel1_forkjoin_framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * Het fork/join framework is ontworpen voor tasks die in kleinere delen kan worden opgesplits,
 * waardoor deze kleinere delen parallel uitgevoerd kunnen worden.
 *
 * Hierbij wordt gebruik gemaakt van een work-stealing algoritme
 * Dit houdt in dat threads die geen taken meer hebben om uit te voeren,
 * taken kunnen afpakken die in de wacht staan bij andere threads.
 *
 * Het belangrijkste component in het framework is de {@link ForkJoinPool}.
 * Je kan deze op verschillende manieren instantieren:
 */
public class ForkJoinFramework
{
	// Default pool, waar threads worden opgeruimd als het rustig is en worden aangemaakt als ze weer nodig zijn.
	private static final ForkJoinPool commonPool = ForkJoinPool.commonPool();

	// Een pool met een aantal threads gebaseerd op het aantal beschikbare processoren
	private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

	// Een pool met een gespecificeerd aantal threads.
	private static final ForkJoinPool fiveThreadForkJoinPool = new ForkJoinPool(5);

	/**
	 * {@link ForkJoinPool}s kunnen {@link ForkJoinTask}s uitvoeren.
	 *
	 * De belangrijkste methoden voor een {@link ForkJoinTask} zijn:
	 *  - fork(): het asynchroon uitvoeren van de taak;
	 *  - join(): het wachten op een resultaat van de taak;
	 *  - invoke(): voer de taak uit en wacht op het resultaat (dus als het ware een fork en join in één);
	 *  - ForkJoinTask.invokeAll(...): voer meerdere taken uit en wacht tot deze klaar zijn met uitvoeren.
	 * <p>
	 * 2 subclasses van de {@link ForkJoinTask} zijn de {@link RecursiveAction} en de {@link RecursiveTask}
	 * <p>
	 * Deze twee classes hebben 1 abstract method: compute()
	 * Deze methode zou de volgende pseudocode moeten volgen:
	 * <p>
	 * if (meegegeven hoeveelheid werk is klein genoeg)
	 *    voer dit werk uit
	 * else
	 *    splits het werk in meerdere delen
	 *    roep dit gesplitste werk recursief aan en wacht op het resultaat
	 * <p>
	 * Hieronder zie je een voorbeeld van een {@link RecursiveAction}, waarbij strings worden uitgeprint.
	 */
	private static class PrintingAction extends RecursiveAction
	{
		// In deze task gaan we strings uitprinten
		private final List<String> toPrint;

		private PrintingAction(List<String> toPrint)
		{
			this.toPrint = toPrint;
		}

		/**
		 * De compute() methode van een {@link RecursiveAction} verwacht geen parameters en geen return type.
		 */
		@Override
		protected void compute()
		{
			// Als er maar 1 element is in de lijst
			if (toPrint.size() == 1) {
				// Dan printen we dit element uit
				System.out.println(toPrint.get(0) + " (" + Thread.currentThread().getName() + ")");
			}
			else {
				// Anders splitsen we het werk op in 2 delen, met elk de helft van de lijst.
				PrintingAction printingAction1 = new PrintingAction(toPrint.subList(0, toPrint.size() / 2));
				PrintingAction printingAction2 = new PrintingAction(toPrint.subList(toPrint.size() / 2, toPrint.size()));
				// En voeren we dit werk uit
				ForkJoinTask.invokeAll(printingAction1, printingAction2);
			}
		}
	}

	/**
	 * Hieronder zie je een voorbeeld van een {@link RecursiveTask}, waarbij een lijst van nummers gesorteerd wordt.
	 */
	private static class SortingTask extends RecursiveTask<List<Integer>>
	{
		// De nummers die gesorteerd moeten worden
		private final List<Integer> toSort;

		private SortingTask(List<Integer> toSort)
		{
			this.toSort = toSort;
		}

		/**
		 * De compute() methode van een {@link RecursiveTask} verwacht geen parameters
		 * en geeft een resultaat terug van het type dat opgegeven is in de generics parameter.
		 *
		 * Hieronder is een implementatie gemaakt van merge sort, mbv forks en joins.
		 */
		@Override
		protected List<Integer> compute()
		{
			// Als er maar 1 element is in de lijst
			if (toSort.size() == 1) {
				return Collections.unmodifiableList(toSort);
			}
			else {
				// Anders splitsen we het werk op in 2 delen, met elk de helft van de lijst.
				SortingTask sortingTask1 = new SortingTask(toSort.subList(0, toSort.size() / 2));
				SortingTask sortingTask2 = new SortingTask(toSort.subList(toSort.size() / 2, toSort.size()));
				// En voeren we dit werk uit
				sortingTask1.fork();
				sortingTask2.fork();
				// waarna we het resultaat samenvoegen
				synchronized (this) {
					List<Integer> left = sortingTask1.join();
					List<Integer> right = sortingTask2.join();
					System.out.println(left);
					System.out.println(right);
					System.out.println();
					return merge(left, right);
				}
			}
		}

		/**
		 * Voeg twee gesorteerde stapels samen naar één grote gesorteerde stapel.
		 */
		private List<Integer> merge(List<Integer> left, List<Integer> right)
		{
			// Bereid de resulterende lijst voor
			List<Integer> resultList = new ArrayList<>();

			// Pak de iterators van de gesorteerde stapels
			Iterator<Integer> leftIterator = left.iterator();
			Iterator<Integer> rightIterator = right.iterator();

			// Pak van beide stapels de bovenste
			Integer leftInteger = leftIterator.next();
			Integer rightInteger = rightIterator.next();

			// Loop tot een break gevonden wordt
			while (true) {
				// Pak de kleinste van de twee
				if (leftInteger.compareTo(rightInteger) < 0) {
					// Als links de kleinste is, voeg deze toe aan de samengevoegde stapel
					resultList.add(leftInteger);
					if (leftIterator.hasNext()) {
						// Open de volgende in de linker stapel als deze nog niet leeg is.
						leftInteger = leftIterator.next();
					} else {
						// Als de linker stapel wel leeg is, voeg dan de overige nummers van de rechter stapel toe.
						resultList.add(rightInteger);
						while (rightIterator.hasNext()) {
							resultList.add(rightIterator.next());
						}
						break;
					}
				} else {
					// Als rechts de kleinste is, voeg deze toe aan de samengevoegde stapel
					resultList.add(rightInteger);
					if (rightIterator.hasNext()) {
						// Open de volgende in de rechter stapel als deze nog niet leeg is.
						rightInteger = rightIterator.next();
					} else {
						// Als de rechter stapel wel leeg is, voeg dan de overige nummers van de linker stapel toe.
						resultList.add(leftInteger);
						while (leftIterator.hasNext()) {
							resultList.add(leftIterator.next());
						}
						break;
					}
				}
			}
			// Geef de samengevoegde stapel terug.
			return resultList;
		}
	}

	/**
	 * Voorbeeld hoe je de {@link ForkJoinTask}s kan aanroepen:
	 */
	public static void main(String[] args)
	{
		List<String> words = List.of("Ik", "Maan", "Roos", "Vis", "Sok", "Pen");
		PrintingAction printingAction = new PrintingAction(words);
		commonPool.invoke(printingAction);

		List<Integer> toSort = List.of(42, 1337, 69, 7, 420, 12);
		SortingTask sortingTask = new SortingTask(toSort);
		List<Integer> sorted = commonPool.invoke(sortingTask);
		System.out.println(sorted.stream().map(Object::toString).collect(Collectors.joining(", ")));
	}
}
