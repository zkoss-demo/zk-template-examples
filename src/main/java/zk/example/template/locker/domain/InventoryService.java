package zk.example.template.locker.domain;

import zk.example.template.locker.domain.InventoryItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {

	private static ArrayList<InventoryItem> inventory;

	static {
		inventory = new ArrayList<>();
		inventory.add(new InventoryItem(10001L, "Hammer", new BigDecimal("25.00"), 2));
		inventory.add(new InventoryItem(10002L, "Nail", new BigDecimal("0.50"), 200));
		inventory.add(new InventoryItem(10003L, "Fridge", new BigDecimal("425.00"), 1));
		inventory.add(new InventoryItem(10004L, "Plate", new BigDecimal("3.90"), 6));
		inventory.add(new InventoryItem(10005L, "Cup", new BigDecimal("2.50"), 12));
	}
	public static List<InventoryItem> getInventory() {
		return inventory;
	}

}
