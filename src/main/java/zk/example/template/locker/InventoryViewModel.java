package zk.example.template.locker;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.ListModelList;
import zk.example.template.locker.domain.InventoryItem;
import zk.example.template.locker.domain.InventoryService;
import zk.example.template.locker.lockable.Lockable;
import zk.example.template.locker.lockable.MvvmLockable;
import zk.example.template.locker.lockable.UiLockTracker;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryViewModel {
	private static final AtomicInteger userCounter = new AtomicInteger(0);
	private final String username = "user-" + userCounter.incrementAndGet();

	private ListModelList<InventoryItem> inventory;
	private final UiLockTracker<InventoryItem> lockTracker = new UiLockTracker<>(10, 5);

	@Init
	public void init(@ContextParam(ContextType.DESKTOP) Desktop desktop) {
		desktop.enableServerPush(true);
		inventory = new ListModelList<>(InventoryService.getInventory());
	}

	@Command
	public void view(@BindingParam("item") InventoryItem item) {
		lockTracker.reset();
		lockTracker.observe(new MvvmLockable(username, item));
		BindUtils.postNotifyChange(null, null, this, "currentItem");
	}

	@Command
	public void edit(@BindingParam("item") InventoryItem item) {
		view(item);
		lockTracker.lock();
	}

	@Command
	public void save(@BindingParam("item") InventoryItem item) {
		lockTracker.unlock();
		inventory.notifyChange(item);
	}

	@Command
	public void cancel(@BindingParam("item") InventoryItem item) {
		lockTracker.unlock();
	}

	public ListModelList<InventoryItem> getInventory() {
		return inventory;
	}

	public Lockable<InventoryItem> getCurrentItem() {
		return lockTracker.getLockable();
	}

	public String getUsername() {
		return username;
	}
}
