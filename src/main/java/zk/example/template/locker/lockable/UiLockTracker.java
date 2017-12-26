package zk.example.template.locker.lockable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import zk.example.template.locker.rx.operators.ZkObservable;
import zk.example.template.locker.lockservice.LockService;
import zk.example.template.locker.lockservice.LockStatus;

import java.util.concurrent.TimeUnit;

public class UiLockTracker<T> {
	private final int aliveCheckInterval;
	private final int aliveCheckTimeout;

	private UiLockable<T> lockable;
	private Disposable lockEventSubscription;
	private Disposable aliveCheckSubscription;

	public UiLockTracker(int aliveCheckInterval, int aliveCheckTimeout) {
		this.aliveCheckInterval = aliveCheckInterval;
		this.aliveCheckTimeout = aliveCheckTimeout;
	}

	public void observe(UiLockable<T> lockable) {
		if(this.lockable != null) {
			throw new IllegalStateException("still watching: " + this.lockable);
		}
		this.lockable = lockable;
		lockEventSubscription = LockService.observeResource(lockable.getResourceKey())
				.doOnNext(event -> System.out.println("locked: " + event.owner + " on " + event.getResourceKey()))
				.compose(ZkObservable.activated())
				.subscribe(lockable::onLockEvent, this::resetOnError);
	}

	public boolean lock() {
		boolean locked = LockService.lock(lockable.getResourceKey(), lockable.getSelf());
		if(locked) {
			startAliveCheck();
		}
		return locked;
	}

	public void unlock() {
		LockService.unlock(lockable.getResourceKey(), lockable.getSelf());
		stopAliveCheck();
	}

	public void reset() {
		if(lockEventSubscription != null) {
			lockEventSubscription.dispose();
		}
		if(lockable != null && lockable.getStatus() == LockStatus.OWNED) {
			unlock();
		}
		this.lockable = null;
		stopAliveCheck();
	}

	public UiLockable<T> getLockable() {
		return lockable;
	}

	private void resetOnError(Throwable err) {
		System.err.println("Dispose unavailable UiLockTracker: " + lockable + " - " + err);
		reset();
	}

	private void startAliveCheck() {
		aliveCheckSubscription = Observable.interval(aliveCheckInterval, TimeUnit.SECONDS)
				.compose(ZkObservable.activated())
				.timeout(aliveCheckTimeout + aliveCheckInterval, TimeUnit.SECONDS)
				.subscribe(
						aliveCount -> {
							System.out.println("alive: " + (aliveCount + 1) * aliveCheckInterval + "sec, " + lockable);
						},
						err -> {
							System.err.println("Unlocking unresponsive lock owner: " + lockable + " - " + err);
							unlock();
						});
	}

	private void stopAliveCheck() {
		if(aliveCheckSubscription != null) {
			aliveCheckSubscription.dispose();
			aliveCheckSubscription = null;
		}
	}
}
