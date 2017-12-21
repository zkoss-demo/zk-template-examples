package zk.example.template.locker.service;

public class LockEvent {
	public String resourceKey;
	public String owner;

	public LockEvent(String resourceKey, String owner) {
		this.resourceKey = resourceKey;
		this.owner = owner;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public String getOwner() {
		return owner;
	}
}
