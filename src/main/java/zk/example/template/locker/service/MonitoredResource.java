package zk.example.template.locker.service;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.DependsOn;
import zk.example.template.locker.domain.WithId;
import zk.example.template.locker.rx.operators.ZkObservable;

import java.util.concurrent.TimeUnit;

public class MonitoredResource<T extends WithId> implements Disposable{

	private final String self;
	private final T resource;
	private String owner;
	private final Disposable disposable;
	private Disposable aliveCheck;

	public MonitoredResource(String self, T resource) {
		this.self = self;
		this.resource = resource;

		disposable = LockService.observeResource(getResourceKey())
				.compose(ZkObservable.activated())
				.subscribe(this::handleLockEvent, this::cleanupOnError);
	}

	public String getResourceKey() {
		return getResource().getClass().getName() + "-" + resource.getId();
	}

	private void cleanupOnError(Throwable throwable) {
		throwable.printStackTrace();
		disposable.dispose();
		clearAliveCheck();
	}

	private void handleLockEvent(LockEvent event) {
		this.updateOwner(event.getOwner());
	}

	public boolean lock() {
		boolean locked = LockService.lock(getResourceKey(), self);
		if(locked) {
			aliveCheck = Observable.interval(5, TimeUnit.SECONDS)
					.compose(ZkObservable.activated())
					.timeout(10, TimeUnit.SECONDS)
					.subscribe(
							aliveCount -> {
								System.out.println("alive since: " + (aliveCount + 1) * 5 + "sec, " + self + " on " + getResourceKey());
							},
							err -> {
								System.out.println("unlocking dead lock for: " + self + " on " + getResourceKey());
								err.printStackTrace();
								unlock();
							});
		}
		return locked;
	}

	public void unlock() {
		LockService.unlock(getResourceKey(), self);
		clearAliveCheck();
	}

	private void clearAliveCheck() {
		if(aliveCheck != null) {
			aliveCheck.dispose();
			aliveCheck = null;
		}
	}

	public T getResource() {
		return resource;
	}

	public void updateOwner(String owner) {
		this.owner = owner;
		BindUtils.postNotifyChange(null, null, this, "owner");
		BindUtils.postNotifyChange(null, null, this, "status");
	}

	public String getOwner() {
		return owner;
	}

//	@DependsOn("owner") //TODO reproduce problem
	public LockStatus getStatus() {
		return owner == null ? LockStatus.AVAILABLE :
				self.equals(owner) ? LockStatus.OWNED : LockStatus.UNAVAILABLE;
	}

	@Override
	public void dispose() {
		disposable.dispose();
		if(getStatus() == LockStatus.OWNED) {
			unlock();
		}
	}

	@Override
	public boolean isDisposed() {
		return disposable.isDisposed();
	}
}
