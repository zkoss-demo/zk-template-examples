package zk.example.template.locker;

import org.zkoss.bind.BindUtils;
import zk.example.template.locker.service.LockEvent;
import zk.example.template.locker.service.Lockable;

public class UiLockable<T> extends Lockable<T> {

	public UiLockable(String self, T resource) {
		super(self, resource);
	}

	@Override
	public void onLockEvent(LockEvent event) {
		super.onLockEvent(event);
		BindUtils.postNotifyChange(null, null, this, "owner");
		BindUtils.postNotifyChange(null, null, this, "status");
	}

	@Override
	public String toString() {
		return "UiLockable{" + getOwner() + " on " + getResourceKey() + '}';
	}

}
