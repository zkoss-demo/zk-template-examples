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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LockEvent lockEvent = (LockEvent) o;

		if (!resourceKey.equals(lockEvent.resourceKey)) return false;
		return owner != null ? owner.equals(lockEvent.owner) : lockEvent.owner == null;
	}

	@Override
	public int hashCode() {
		int result = resourceKey.hashCode();
		result = 31 * result + (owner != null ? owner.hashCode() : 0);
		return result;
	}
}
