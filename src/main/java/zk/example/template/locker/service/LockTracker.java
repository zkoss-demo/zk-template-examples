package zk.example.template.locker.service;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import zk.example.template.locker.rx.operators.ZkObservable;

import java.util.concurrent.TimeUnit;

public class LockTracker<T> implements Disposable{

	private final Disposable disposable;
	private final Lockable<T> lockable;
	private Disposable aliveCheck;

	public LockTracker(Lockable<T> lockable) {
		this.lockable = lockable;
		disposable = LockService.observeResource(lockable.getResourceKey())
				.doOnNext(event -> System.out.println("locked: " + event.owner + " on " + event.getResourceKey()))
				.compose(ZkObservable.activated())
				.subscribe(lockable::onLockEvent, this::cleanupOnError);
	}

	public Lockable<T> getLockable() {
		return lockable;
	}

	private void cleanupOnError(Throwable err) {
		System.err.println("Dispose unavailable LockTracker: " + lockable + " - " + err);
		disposable.dispose();
		clearAliveCheck();
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
		clearAliveCheck();
	}

	@Override
	public void dispose() {
		disposable.dispose();
		if(lockable.getStatus() == LockStatus.OWNED) {
			unlock();
		}
		clearAliveCheck();
	}

	@Override
	public boolean isDisposed() {
		return disposable.isDisposed();
	}

	private void startAliveCheck() {
		aliveCheck = Observable.interval(5, TimeUnit.SECONDS)
				.compose(ZkObservable.activated())
				.timeout(10, TimeUnit.SECONDS)
				.subscribe(
						aliveCount -> {
							System.out.println("alive: " + (aliveCount + 1) * 5 + "sec, " + lockable);
						},
						err -> {
							System.err.println("Unlocking unresponsive lock owner: " + lockable + " - " + err);
							unlock();
						});
	}

	private void clearAliveCheck() {
		if(aliveCheck != null) {
			aliveCheck.dispose();
			aliveCheck = null;
		}
	}
}
