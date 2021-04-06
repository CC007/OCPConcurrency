package nl.cjib.training.ocp.concurrency.presentatie1.deel7_atomic_classes;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class AtomicClasses
{
	/**
	 * De Concurrency API bevat Atomic classes.
	 * Deze bieden de mogelijkheid om bepaalde operaties op een thread-safe manier te doen.
	 */
	public static void main(String[] args)
	{
		// Atomic classes hebben een no arg constructor.
		// Hierbij wordt de waarde van de atomic elementen op de default waarde van het type gezet.
		System.out.println("boolean: " + new AtomicBoolean()); // default false
		System.out.println("int: " + new AtomicInteger()); // default 0
		System.out.println("long: " + new AtomicLong()); // default 0
		System.out.println("Reference: " + new AtomicReference<>());  // default null

		// Behalve voor booleans is voor alle Atomic classes ook een array variant.
		// Atomic array classes hebben geen no-arg constructor maar een int arg constructor.
		// Deze geeft de lengte van de array aan.
		// Ook hierbij worden de elementen in de array op de default waarde van het type gezet.
		System.out.println("int array: " + new AtomicIntegerArray(3)); // default [0, 0, 0]
		System.out.println("long array: " + new AtomicLongArray(3)); // default [0, 0, 0]
		System.out.println("Reference: " + new AtomicReferenceArray<>(3));  // default [null, null, null]

		// Je kan aan de constructor ook de initiele waarde van het object meegeven.
		AtomicBoolean atomicBoolean = new AtomicBoolean(true);
		AtomicInteger atomicInteger = new AtomicInteger(5);
		AtomicLong atomicLong = new AtomicLong(42L);
		AtomicReference<String> atomicStringReference = new AtomicReference<>("Hello World");

		AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[]{1, 2, 3});
		AtomicLongArray atomicLongArray = new AtomicLongArray(new long[]{42L, 420, 1337});
		AtomicReferenceArray<String> atomicStringAtomicReferenceArray = new AtomicReferenceArray<>(
			new String[]{"Hello World", "Greetings", "Goedemorgen"});

		// Alle Atomic classes hebben een get, set, compareAndSet en getAndSet methode
		boolean boolVal = atomicBoolean.get();
		System.out.println(boolVal);
		atomicInteger.set(10);
		long longVal = atomicLong.getAndSet(69L);
		System.out.println(longVal);
		System.out.println(atomicStringReference.compareAndSet("Hello People", "Hallo Wereld"));
		System.out.println(atomicStringReference.compareAndSet("Hello World", "Hallo Wereld"));

		// De nummer classes hebben een incrementAndGet/getAndIncrement, decrementAndGet/getAndDecrement
		// en de addAndGet/getAndAdd methods
		int getBeforeIncrementing = atomicInteger.getAndIncrement(); // Hierbij haal je de oude waarde op voor de increment
		long getAfterDecrementing = atomicLong.decrementAndGet(); // Hierbij decrement je voordat je de nieuwe waarde ophaalt
		int getBeforeAdd = atomicInteger.getAndAdd(4); // Hierbij haal je de oude waarde op voordat je het getal er bij optelt

		// Verder hebben alle Atomic classes behalve AtomicBoolean de updateAndGet/getAndUpdate
		// en accumulateAndGet/getAndAccumulate methods
		atomicInteger.updateAndGet(oldVal -> oldVal * 5); // Hierbij geef je een unary operator mee om de waarde te veranderen
		atomicStringReference.getAndAccumulate("!", (oldVal, x) -> oldVal + x); // Hierbij geef je een binary operator en de 2e parameter van de operator mee
																				  // om de waarde te veranderen

		// Bij alle hierboven genoemde methods hebben de array classes een extra parameter voor de index van het element
		atomicIntegerArray.get(0);
		atomicLongArray.addAndGet(1, 5);
		atomicStringAtomicReferenceArray.compareAndSet(2, "Goedemorgen", "Goedemiddag");
	}
}
