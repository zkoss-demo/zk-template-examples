package zk.example.template.orgchart;

import org.zkoss.bind.annotation.Init;

import java.util.Arrays;

import static zk.example.template.orgchart.OrgChartViewModel.OrgNodeData.Type.*;

public class OrgChartViewModel {

	private static String NO_IMAGE = null;
	private ModelNode<OrgNodeData> orgChartRoot;

	@Init
	public void init() {
		orgChartRoot = orgNode(PRESIDENT, "President", null, "icon/icon1.svg",
				orgNode(VICE_PRESIDENT, "Vice President", "Account Services", "icon/icon2.svg",
						orgNode(SUPERVISOR, "Account Supervisor", null, NO_IMAGE,
								orgNode(EMPLOYEE, "Account Executive", null, NO_IMAGE),
								orgNode(EMPLOYEE, "Account Executive", null, NO_IMAGE)
						),
						orgNode(SUPERVISOR, "Account Supervisor", null, NO_IMAGE)
				),
				orgNode(VICE_PRESIDENT, "Vice President", "Creative Services", "icon/icon3.svg",
						orgNode(SUPERVISOR, "Art / Copy", null, NO_IMAGE),
						orgNode(SUPERVISOR, "Production", null, NO_IMAGE)
				),
				orgNode(VICE_PRESIDENT, "Vice President", "Marketing Services", "icon/icon4.svg",
						orgNode(SUPERVISOR, "Media", null, NO_IMAGE),
						orgNode(SUPERVISOR, "Research", null, NO_IMAGE)
				),
				orgNode(VICE_PRESIDENT, "Vice President", "Management Services", "icon/icon5.svg",
						orgNode(SUPERVISOR, "Accounting", null, NO_IMAGE),
						orgNode(SUPERVISOR, "Personnel", null, NO_IMAGE),
						orgNode(SUPERVISOR, "Purchasing", null, NO_IMAGE)
				)
		);
	}

	public ModelNode<OrgNodeData> getOrgChartRoot() {
		return orgChartRoot;
	}

	private ModelNode<OrgNodeData> orgNode(OrgNodeData.Type president, String title, String name, String icon, ModelNode<OrgNodeData>... children) {
		if(children.length > 0) {
			return new ModelNode<OrgNodeData>(new OrgNodeData(president, title, name, icon), Arrays.asList(children));
		} else {
			return new ModelNode<OrgNodeData>(new OrgNodeData(president, title, name, icon));
		}
	}

	public static class OrgNodeData {
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
}
