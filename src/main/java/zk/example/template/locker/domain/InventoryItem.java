package zk.example.template.locker.domain;

import java.math.BigDecimal;

public class InventoryItem implements WithId {
	private final long id;
	private String name;
	private BigDecimal value;
	private int quantity;

	public InventoryItem(long id) {
		this.id = id;
	}

	public InventoryItem(long id, String name, BigDecimal value, int quantity) {
		this(id);
		this.name = name;
		this.value = value;
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "InventoryItem{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
