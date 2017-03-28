package zk.example.template.stepbar;

import org.zkoss.bind.BindUtils;

import zk.example.template.stepbar.StepBarModel.Step;

public class StepBarModel extends NavModel<Step> {
	@Override
	public void navigateTo(Step step) {
		//notify change all steps affected by random access navigation (steps between old and new index inclusive)
		int oldIndex = getItems().indexOf(getCurrent());
		int newIndex = getItems().indexOf(step);
		super.navigateTo(step);
		getItems().subList(Math.min(newIndex, oldIndex), Math.max(newIndex, oldIndex) + 1)
				  .forEach(affectedStep -> BindUtils.postNotifyChange(null, null, affectedStep, "*"));
	}

	public class Step {
		private String label;
		private String icon;
		private String uri;

		public Step(String label, String icon, String uri) {
			super();
			this.label = label;
			this.icon = icon;
			this.uri = uri;
		}

		public String getStatus() {
			return isDone() ? "previous" : (getCurrent() == this ? "current" : "following"); 
		}
		
		public boolean isDone() {
			return getItems().indexOf(this) < getItems().indexOf(getCurrent()); 
		}
		
		public String getLabel() {
			return label;
		}
		
		public String getIcon() {
			return icon;
		}
		
		public String getUri() {
			return uri;
		}
	}
}
