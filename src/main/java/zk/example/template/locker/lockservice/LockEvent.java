package zk.example.template.locker.lockservice;

public class LockEvent {
	public Object resourceKey;
	public String owner;

	public LockEvent(Object resourceKey, String owner) {
		this.resourceKey = resourceKey;
		this.owner = owner;
	}

	public Object getResourceKey() {
		return resourceKey;
	}

	public String getOwner() {
		return owner;
	}
}
