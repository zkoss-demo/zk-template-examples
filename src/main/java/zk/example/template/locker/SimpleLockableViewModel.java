package zk.example.template.locker;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Desktop;
import zk.example.template.locker.domain.SimpleResource;
import zk.example.template.locker.lockable.MvvmLockable;
import zk.example.template.locker.lockable.UiLockTracker;
import zk.example.template.locker.lockable.Lockable;
import zk.example.template.locker.lockservice.LockEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleLockableViewModel {
	private static final AtomicInteger userCounter = new AtomicInteger(0);
	private final String username = "User-" + userCounter.incrementAndGet();

	private final UiLockTracker<SimpleResource> lockTracker = new UiLockTracker<>(10, 5);
	private static SimpleResource sharedResource = new SimpleResource("the Resource Value");

	@Init
	public void init(@ContextParam(ContextType.DESKTOP) Desktop desktop) {
		desktop.enableServerPush(true);
		lockTracker.observe(new MvvmLockable<SimpleResource>(getUsername(), sharedResource) {
			@Override
			public void onLockEvent(LockEvent event) {
				super.onLockEvent(event);
				BindUtils.postNotifyChange(null, null, sharedResource, "value");
			}
		});
	}

	@Command
	public void lock() {
		lockTracker.lock();
	}

	@Command
	public void unlock() {
		lockTracker.unlock();
	}

	public Lockable<SimpleResource> getLockable() {
		return lockTracker.getLockable();
	}

	public String getUsername() {
		return username;
	}
}
