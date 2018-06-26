package zk.example.template.orgchart;

import org.zkoss.bind.annotation.Init;

import java.util.Arrays;

import static zk.example.template.orgchart.OrgNodeData.Type.*;

public class OrgChartViewModel {
	private static final String NO_IMAGE = null;
	private OrgNode<OrgNodeData> orgChartRoot;

	@Init
	public void init() {
		orgChartRoot = createOrgNode(PRESIDENT, "President", null, "icon/icon1.svg",
				createOrgNode(VICE_PRESIDENT, "Vice President", "Account Services", "icon/icon2.svg",
						createOrgNode(SUPERVISOR, "Account Supervisor", null, NO_IMAGE,
								createOrgNode(EMPLOYEE, "Account Executive", null, NO_IMAGE),
								createOrgNode(EMPLOYEE, "Account Executive", null, NO_IMAGE)
						),
						createOrgNode(SUPERVISOR, "Account Supervisor", null, NO_IMAGE)
				),
				createOrgNode(VICE_PRESIDENT, "Vice President", "Creative Services", "icon/icon3.svg",
						createOrgNode(SUPERVISOR, "Art / Copy", null, NO_IMAGE),
						createOrgNode(SUPERVISOR, "Production", null, NO_IMAGE)
				),
				createOrgNode(VICE_PRESIDENT, "Vice President", "Marketing Services", "icon/icon4.svg",
						createOrgNode(SUPERVISOR, "Media", null, NO_IMAGE),
						createOrgNode(SUPERVISOR, "Research", null, NO_IMAGE)
				),
				createOrgNode(VICE_PRESIDENT, "Vice President", "Management Services", "icon/icon5.svg",
						createOrgNode(SUPERVISOR, "Accounting", null, NO_IMAGE),
						createOrgNode(SUPERVISOR, "Personnel", null, NO_IMAGE),
						createOrgNode(SUPERVISOR, "Purchasing", null, NO_IMAGE)
				)
		);
	}

	public OrgNode<OrgNodeData> getOrgChartRoot() {
		return orgChartRoot;
	}

	public static OrgNode<OrgNodeData> createOrgNode(OrgNodeData.Type type, String title, String name, String icon, OrgNode<OrgNodeData>... children) {
		if (children.length > 0) {
			return new OrgNode<OrgNodeData>(new OrgNodeData(type, title, name, icon), Arrays.asList(children));
		} else {
			return new OrgNode<OrgNodeData>(new OrgNodeData(type, title, name, icon));
		}
	}
}
