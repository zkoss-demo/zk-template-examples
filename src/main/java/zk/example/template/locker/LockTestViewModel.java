package zk.example.template.locker;

import io.reactivex.disposables.Disposable;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;
import zk.example.template.locker.domain.InventoryItem;
import zk.example.template.locker.service.MonitoredResource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTestViewModel {

	private static final AtomicInteger usercount = new AtomicInteger(0);

	private List<InventoryItem> inventory;
	private MonitoredResource<InventoryItem> currentItem = null;

	private String username = "user-" + usercount.incrementAndGet();
	private Disposable lockMonitor;

	@Init
	public void init(@ContextParam(ContextType.DESKTOP) Desktop desktop) {
		desktop.enableServerPush(true);
		List<InventoryItem> inventory = initInventory();
	}

	@Command
	public void view(@BindingParam("item") InventoryItem item) {
		clearCurrentItem();
		currentItem = new MonitoredResource(username, item);
		BindUtils.postNotifyChange(null, null, this, "currentItem");
	}

	private void clearCurrentItem() {
		if(currentItem != null) {
			currentItem.dispose();
			currentItem = null;
		}
	}

	@Command
	public void lock(@BindingParam("item") InventoryItem item) {
		currentItem.lock();
	}

	@Command
	public void unlock(@BindingParam("item") InventoryItem item) {
		currentItem.unlock();
	}

	private List<InventoryItem> initInventory() {
		inventory = new ArrayList<>();
		inventory.add(new InventoryItem(10001L, "Hammer", new BigDecimal("25.00"), 2));
		inventory.add(new InventoryItem(10002L, "Nail", new BigDecimal("0.50"), 200));
		inventory.add(new InventoryItem(10003L, "Fridge", new BigDecimal("425.00"), 1));
		inventory.add(new InventoryItem(10004L, "Plate", new BigDecimal("3.90"), 6));
		inventory.add(new InventoryItem(10005L, "Cup", new BigDecimal("2.50"), 12));
		return inventory;
	}

	public List<InventoryItem> getInventory() {
		return inventory;
	}

	public MonitoredResource<InventoryItem> getCurrentItem() {
		return currentItem;
	}

	public String getUsername() {
		return username;
	}
}
