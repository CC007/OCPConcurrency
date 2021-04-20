package nl.cjib.training.ocp.concurrency.presentatie3.deel2_parallel_streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * In contrast met de standaard seriële streams, bied java ook een manier om parallele streams te maken.
 * Hierbij worden meerdere threads benut om streams te verwerken.
 */
public class ParallelStreams
{

	/**
	 * Je hebt 2 manieren om een parallele stream te maken
	 */
	public static void main(String[] args)
	{
		List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

		// Maak een parallele stream van een sequential stream:
		Stream<Integer> sequentialStream = integers.stream();
		Stream<Integer> parallelStream1 = sequentialStream.parallel();

		// Maak een parallele stream obv een collection:
		Stream<Integer> parallelStream2 = integers.parallelStream();

		// Met isParallel() kan je checken of een stream parallel is.
		// Let op! De (voorheen) sequential stream geeft ook aan dat deze parallel is,
		// omdat we hiervan hierboven een parallele stream gemaakt hebben.
		System.out.println(sequentialStream.isParallel());
		System.out.println(parallelStream1.isParallel());
		System.out.println(parallelStream2.isParallel());

		// Seriële streams gedragen zich voorspelbaar mbt de volgorde in de forEach.
		System.out.println();
		integers.stream().forEach(x -> System.out.print(x + " "));
		System.out.println();

		// Parallele streams doen dat niet.
		System.out.println();
		integers.parallelStream().forEach(x -> System.out.print(x + " "));
		System.out.println();
		integers.parallelStream().forEach(x -> System.out.print(x + " "));
		System.out.println();
		integers.parallelStream().forEach(x -> System.out.print(x + " "));
		System.out.println();

		// Voor een gegarandeerde volgorde bij parallele streams kan je gebruik maken van forEachOrdered.
		System.out.println();
		integers.parallelStream().forEachOrdered(x -> System.out.print(x + " "));
		System.out.println();

		// Het gebruik van unordered streams kan bij parallele streams de performance verbeteren
		// Vanwege bepaalde optimalisaties voor unordered streams,
		// omdat hierbij geen rekening gehouden hoeft te worden met het behouden van de order.
		System.out.println();
		integers.stream().unordered().parallel().forEach(x -> System.out.print(x + " "));
		System.out.println();

		// PROBEER SIDE EFFECTS TE VOORKOMEN!
		// Hieronder zie je dat er geen garanties zijn in welke volgorde de map() wordt uitgevoerd
		// Zelfs als er aan het eind forEachOrdered gebruikt wordt.
		System.out.println();
		integers.parallelStream().map(integer -> {
			System.out.println(" " + integer);
			return integer + 1;
		}).forEachOrdered(System.out::println);

		// Voorbeeld 2
		List<String> integerList = Collections.synchronizedList(new ArrayList<>());
		System.out.println();
		integers.parallelStream().map(integer -> {
			integerList.add(" " + integer);
			return integer + 1;
		}).forEachOrdered(System.out::println);

		// Ook hier is de volgorde niet gegarandeerd
		integerList.forEach(System.out::println);

		// Vanwege de natuur van parallele processen zal findAny niet altijd hetzelfde resultaat geven.
		System.out.println();
		System.out.println(integers.parallelStream().findAny().get());
		System.out.println(integers.parallelStream().findAny().get());
		System.out.println(integers.parallelStream().findAny().get());
		System.out.println(integers.parallelStream().findAny().get());

		// voor zowel de reduce als de collect methode van een stream is er een variant beschikbaar
		// met 3 parameters: de identity, de accumulator en de combiner.
		// Parallele streams kunnen gebruik maken van de combiner door het resultaat van meerdere threads te combinen.
		System.out.println();
		System.out.println("Sum: " + integers.parallelStream()
			.reduce(0, (acc, integer) -> acc + integer, (acc1, acc2) -> acc1 + acc2));
		ArrayList<Integer> listCopy = integers.parallelStream()
			.collect(ArrayList::new, (acc, integer) -> acc.add(integer), (acc, otherAcc) -> acc.addAll(otherAcc));


	}
}
