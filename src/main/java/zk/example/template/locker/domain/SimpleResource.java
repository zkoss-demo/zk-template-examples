package zk.example.template.locker.domain;

public class SimpleResource {
	private String value;

	public SimpleResource(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
