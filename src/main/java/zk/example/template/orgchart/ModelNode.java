package zk.example.template.orgchart;

import org.zkoss.zul.ListModelList;

import java.util.List;

public class ModelNode<T> {
	private T data;
	private ListModelList<ModelNode<T>> children;

	public ModelNode(T data) {
		this.data = data;
	}

	public ModelNode(T data, List<ModelNode<T>> children) {
		this.data = data;
		this.children = new ListModelList<>(children);
	}

	public boolean isLeaf() {
		return children == null;
	}

	public boolean isVertical() {
		return children != null && children.stream().allMatch(ModelNode::isLeaf);
	}

	public T getData() {
		return data;
	}

	public ListModelList<ModelNode<T>> getChildren() {
		return children;
	}
}
