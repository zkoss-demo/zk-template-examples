package zk.example.template.orgchart;

import java.util.ArrayList;
import java.util.List;

public class OrgNode<T> {
	private T data;
	private List<OrgNode<T>> children;

	public OrgNode(T data) {
		this.data = data;
	}

	public OrgNode(T data, List<OrgNode<T>> children) {
		this.data = data;
		this.children = new ArrayList<>(children);
	}

	public boolean isLeaf() {
		return children == null;
	}

	public boolean isVertical() {
		return children != null && children.stream().allMatch(OrgNode::isLeaf);
	}

	public T getData() {
		return data;
	}

	public List<OrgNode<T>> getChildren() {
		return children;
	}
}
