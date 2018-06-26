package zk.example.template.orgchart;

public class OrgNodeData {
	public enum Type {PRESIDENT, VICE_PRESIDENT, SUPERVISOR, EMPLOYEE}

	private Type type;
	private String title;
	private String name;
	private String icon;

	public OrgNodeData(Type type, String title, String name, String icon) {
		this.type = type;
		this.title = title;
		this.name = name;
		this.icon = icon;
	}

	public Type getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}
}
