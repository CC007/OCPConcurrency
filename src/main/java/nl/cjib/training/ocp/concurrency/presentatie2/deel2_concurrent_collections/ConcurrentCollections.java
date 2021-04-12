package nl.cjib.training.ocp.concurrency.presentatie2.deel2_concurrent_collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Concurrent collections zijn bedoelt om gebruikt te worden in een multi-threaded situatie
 */
public class ConcurrentCollections
{
	public static void main(String[] args)
	{
		// Map.of(...) zit niet in JDK 8. Deze is hier puur voor het gemak gebruikt.
		Map<String, Integer> map = Map.of("One", 1, "Three", 3);
		Collection<Integer> collection = Arrays.asList(1, 2, 5, 3);
		List<Integer> list = List.copyOf(collection);
		Set<Integer> set = Set.copyOf(collection);

		// Blocking queues zijn de concurrent versies van queues.
		BlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>(collection);
		BlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>(collection);
		try {
			// Ze bieden een versie van offer en poll met een timeout
			// er wordt bij offer gewacht totdat er ruimte is in de queue/deque
			linkedBlockingQueue.offer(6, 1, TimeUnit.SECONDS);
			linkedBlockingDeque.offer(6, 1, TimeUnit.SECONDS);

			// er wordt bij poll gewacht totdat er een item beschikbaar is om op te halen uit de queue/deque
			linkedBlockingQueue.poll(1, TimeUnit.SECONDS);
			linkedBlockingDeque.poll(1, TimeUnit.SECONDS);

			// Bij deques heb je ook de offer/poll first/last met timeout
			linkedBlockingDeque.offerFirst(7, 1, TimeUnit.SECONDS);
			linkedBlockingDeque.offerLast(8, 1, TimeUnit.SECONDS); // Zelfde als offer(...)

			linkedBlockingDeque.pollFirst(1, TimeUnit.SECONDS);
			linkedBlockingDeque.pollLast(1, TimeUnit.SECONDS); // Zelfde als poll(...)
		}
		catch (InterruptedException e) {
			System.exit(1);
		}

		// SkipList set en map zijn de concurrent versies van TreeSet en TreeMap en zijn dus sorted en navigable
		SortedSet<Integer> sortedSet = new ConcurrentSkipListSet<>(collection);
		SortedMap<String, Integer> sortedMap = new ConcurrentSkipListMap<>(map);
		sortedSet.first();
		sortedMap.lastKey();

		NavigableSet<Integer> navigableSet = new ConcurrentSkipListSet<>(collection);
		NavigableMap<String, Integer> navigableMap = new ConcurrentSkipListMap<>(map);
		navigableSet.floor(4);
		navigableMap.descendingMap();

		// CopyOnWrite collections doen wat de naam impliceert:
		// bij een write actie zoals add/remove/put/etc. wordt er onder de motorkap een nieuwe collectie gemaakt
		// Dit zorgt ervoor dat iterators geen concurrent modification exceptions krijgen
		// als er tijdens het itereren een element wordt toegevoegd of verwijderd
		// CopyOnWrite collections zijn erg memory intensief
		// Ze worden vooral gebruikt bij multi-threaded applicaties als er meer reads gedaan worden dan writes
		List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>(collection);
		Set<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet<>(collection);

		System.out.println("Elements");
		new Thread(() -> {
			sleep();
			copyOnWriteArrayList.add(6);
		});

		for (Integer elem : copyOnWriteArrayList) {
			sleep();
			// hier wordt 6 niet geprint, omdat de forloop de iterator al had opgehaald voordat 6 was toegevoegd
			// 6 staat daarom in een kopie van de originele onderliggende lijst
			// die lijst heeft zijn eigen iterator
			System.out.println(elem);
		}

		// overige concurrent collections:
		ConcurrentMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>(map);
		Queue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>(collection);
		Deque<Integer> concurrentLinkedDeque = new ConcurrentLinkedDeque<>(collection);

		// Synchronized collection methods zijn bedoelt om al bestaande collecties te wrappen
		// als bij het aanmaken van de collection nog niet bekend was dat deze multi-threaded gebruikt moest gaan worden
		Collection<Integer> synchronizedCollection = Collections.synchronizedCollection(collection);
		List<Integer> synchronizedList = Collections.synchronizedList(list);
		Map<String, Integer> synchronizedMap = Collections.synchronizedMap(map);
		Set<Integer> synchronizedSet = Collections.synchronizedSet(set);

		NavigableMap<String, Integer> synchronizedNavigableMap = Collections.synchronizedNavigableMap(new TreeMap<>(map));
		NavigableSet<Integer> synchronizedNavigableSet = Collections.synchronizedNavigableSet(new TreeSet<>(set));

		SortedMap<String, Integer> synchronizedSortedMap = Collections.synchronizedSortedMap(new TreeMap<>(map));
		SortedSet<Integer> synchronizedSortedSet = Collections.synchronizedSortedSet(new TreeSet<>(set));
	}

	private static void sleep()
	{
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
