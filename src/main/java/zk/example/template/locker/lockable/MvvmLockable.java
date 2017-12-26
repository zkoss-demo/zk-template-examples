package zk.example.template.locker.lockable;

import org.zkoss.bind.BindUtils;
import zk.example.template.locker.lockservice.LockEvent;

public class MvvmLockable<T> extends UiLockable<T> {

	public MvvmLockable(String self, T resource) {
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
		return "MvvmLockable{" + getOwner() + " on " + getResourceKey() + '}';
	}

}
