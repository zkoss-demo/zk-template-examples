package zk.example.template.locker.lockservice;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.Observable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LockService {
	private static Map<Object, BehaviorSubject<LockEvent>> resourceSubjects = new ConcurrentHashMap<>();

	static {
		Observable.interval(20, TimeUnit.SECONDS).subscribe(count -> cleanIdleResourceQueues());
	}

	public static Observable<LockEvent> observeResource(Object resourceKey) {
		return resourceSubject(resourceKey);
	}

	public static void lock(Object resourceKey, String requester) {
		resourceSubject(resourceKey).onNext(new LockEvent(resourceKey, requester));
	}

	public static void unlock(Object resourceKey, String requester) {
		resourceSubject(resourceKey).onNext(new LockEvent(resourceKey, null));
	}

	private static BehaviorSubject<LockEvent> resourceSubject(Object key) {
		return resourceSubjects.computeIfAbsent(key, o -> {
			System.out.println("create resourceSubject for: " + key);
			return BehaviorSubject.createDefault(new LockEvent(key, null));
		});
	}

	private static void cleanIdleResourceQueues() {
		resourceSubjects.entrySet().removeIf(entry -> {
			boolean hasNoObservers = !entry.getValue().hasObservers();
			if (hasNoObservers) {
				entry.getValue().onComplete();
				System.out.println("clean idle resourceSubject for: " + entry.getKey());
			}
			return hasNoObservers;
		});
	}
}
