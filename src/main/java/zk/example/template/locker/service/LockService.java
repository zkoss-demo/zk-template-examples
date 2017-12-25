package zk.example.template.locker.service;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.Observable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LockService {
	private static Map<Object, String> lockedResources = new ConcurrentHashMap<>();
	private static Map<Object, PublishSubject<LockEvent>> lockedResourceQueues = new ConcurrentHashMap<>();

	static {
		Observable.interval(20, TimeUnit.SECONDS)
				.subscribe(count -> cleanIdleResourceQueues());
	}

	public static Observable<LockEvent> observeResource(Object resourceKey) {
		return lockedResourceQueue(resourceKey).startWith(new LockEvent(resourceKey, lockedResources.get(resourceKey)));
	}

	public static boolean lock(Object resourceKey, String requester) {
		if (lockedResources.putIfAbsent(resourceKey, requester) == null) {
			lockedResourceQueue(resourceKey).onNext(new LockEvent(resourceKey, requester));
			return true;
		} else {
			return false;
		}
	}

	public static void unlock(Object resourceKey, String requester) {
		if (requester.equals(lockedResources.get(resourceKey))) {
			lockedResources.remove(resourceKey);
			lockedResourceQueue(resourceKey).onNext(new LockEvent(resourceKey, null));
		}
	}

	private static PublishSubject<LockEvent> lockedResourceQueue(Object key) {
		return (PublishSubject<LockEvent>) lockedResourceQueues.computeIfAbsent(key, o -> {
			System.out.println("create resourceQueue for: " + key);
			return PublishSubject.create();
		});
	}

	private static void cleanIdleResourceQueues() {
		lockedResourceQueues.entrySet().removeIf(entry -> {
			boolean hasNoObservers = !entry.getValue().hasObservers();
			if (hasNoObservers) {
				entry.getValue().onComplete();
				System.out.println("clean idle resourceQueue for: " + entry.getKey());
			}
			return hasNoObservers;
		});
	}
}
