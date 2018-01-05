package zk.example.template.locker.lockable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.DesktopCleanup;
import zk.example.template.locker.lockservice.LockEvent;
import zk.example.template.locker.rx.operators.ZkObservable;
import zk.example.template.locker.lockservice.LockService;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UiLockTracker<T> {
	private final int aliveCheckInterval;
	private final int aliveCheckTimeout;
	private Desktop desktop;
	private DesktopCleanup desktopCleanup;

	private Lockable<T> lockable;
	private Disposable lockEventSubscription;
	private Disposable aliveCheckSubscription;

	public UiLockTracker(int aliveCheckInterval, int aliveCheckTimeout) {
		this.aliveCheckInterval = aliveCheckInterval;
		this.aliveCheckTimeout = aliveCheckTimeout;
		this.desktop = Executions.getCurrent().getDesktop();
	}

	public void observe(Lockable<T> lockable) {
		if(this.lockable != null) {
			throw new IllegalStateException("still watching: " + this.lockable);
		}
		this.lockable = lockable;
		subscribeToLockEvents(lockable);
		addDesktopCleanup();
	}

	private void subscribeToLockEvents(Lockable<T> lockable) {
		lockEventSubscription = LockService.observeResource(lockable.getResourceKey())
				//experimental idea: finetuning to reduce bursts of redundant events (reduce UI flicker)
				//.throttleLast(50, TimeUnit.MILLISECONDS)
				//.distinctUntilChanged((e1,e2) -> Objects.equals(e1.getOwner(), e2.getOwner()))
				.doOnNext(event -> System.out.println("locked: " + event.owner + " on " + event.getResourceKey()))
				.doOnNext(event -> toggleAliveCheck(event, lockable))
				.compose(ZkObservable.activated())
				.subscribe(lockable::onLockEvent, this::resetOnError);
	}

	private void toggleAliveCheck(LockEvent event, Lockable lockable) {
		if(Objects.equals(event.owner, lockable.getSelf())) {
			startAliveCheck();
		} else {
			stopAliveCheck();
		}
	}

	public void lock() {
		if(this.lockable == null) {
			throw new IllegalStateException("not observing any lockable");
		}
		if(lockable.getStatus() == LockStatus.AVAILABLE) {
			LockService.lock(lockable.getResourceKey(), lockable.getSelf());
		} else {
			return;
		}
	}

	public void unlock() {
		if(this.lockable == null) {
			throw new IllegalStateException("not observing any lockable");
		}
		if(lockable.getStatus() == LockStatus.OWNED) {
			LockService.unlock(lockable.getResourceKey(), lockable.getSelf());
			stopAliveCheck();
		}
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
		removeDesktopCleanup();
	}

	public Lockable<T> getLockable() {
		return lockable;
	}

	private void resetOnError(Throwable err) {
		System.err.println("Dispose unavailable UiLockTracker: " + lockable + " - " + err);
		reset();
	}

	private void startAliveCheck() {
		System.out.println("alive check: started, " + lockable);
		aliveCheckSubscription = Observable.interval(aliveCheckInterval, TimeUnit.SECONDS)
				.compose(ZkObservable.activated())
				.timeout(aliveCheckTimeout + aliveCheckInterval, TimeUnit.SECONDS)
				.subscribe(
						aliveCount -> {
							System.out.println("alive check: " + (aliveCount + 1) * aliveCheckInterval + "sec, " + lockable);
						},
						err -> {
							System.err.println("Unlocking unresponsive lock owner: " + lockable + " - " + err);
							unlock();
						});
	}

	private void stopAliveCheck() {
		if(aliveCheckSubscription != null) {
			System.out.println("alive check: stopped, " + lockable);
			aliveCheckSubscription.dispose();
			aliveCheckSubscription = null;
		}
	}

	private void addDesktopCleanup() {
		removeDesktopCleanup();
		desktopCleanup = dt -> this.reset();
		desktop.addListener(desktopCleanup);
	}

	private void removeDesktopCleanup() {
		if(desktopCleanup != null) {
			desktop.removeListener(desktopCleanup);
			this.desktopCleanup = null;
		}
	}
}
