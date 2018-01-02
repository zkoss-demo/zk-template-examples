package zk.example.template.locker.lockable;

import zk.example.template.locker.domain.WithId;
import zk.example.template.locker.lockservice.LockEvent;
import zk.example.template.locker.lockservice.LockStatus;

public class Lockable<T> {
	private final String self;
	private final T resource;
	private String owner;

	public Lockable(String self, T resource) {
		this.self = self;
		this.resource = resource;
	}

	protected void onLockEvent(LockEvent event) {
		this.owner = event.getOwner();
	}

	public String getSelf() {
		return self;
	}

	public T getResource() {
		return resource;
	}

	public String getOwner() {
		return owner;
	}

	public LockStatus getStatus() {
		return owner == null ? LockStatus.AVAILABLE :
				self.equals(owner) ? LockStatus.OWNED : LockStatus.UNAVAILABLE;
	}

	protected Object getResourceKey() {
		if(resource instanceof WithId) {
			return resource.getClass().getName() + "-" + ((WithId) resource).getId();
		} else {
			return resource;
		}
	}
}
