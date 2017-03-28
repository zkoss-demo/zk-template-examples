package zk.example.template.stepbar;

import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.zul.ListModelList;

public class StepModel {
	private ListModelList<Step> steps = new ListModelList<>();

	public Step getCurrentStep() {
		Set<Step> selection = steps.getSelection();
		return selection.iterator().next();
	}

	@DependsOn("currentStep")
	public boolean isFirst() {
		return getCurrentIndex() == 0;
	};
	
	@DependsOn("currentStep")
	public boolean isLast() {
		return getCurrentIndex() == steps.size() - 1;
	};

	private int getCurrentIndex() {
		return steps.indexOf(getCurrentStep());
	}
	
	public void back() {
		gotoStep(steps.get(getCurrentIndex() - 1));
	}

	public void next() {
		gotoStep(steps.get(getCurrentIndex() + 1));
	}

	public void gotoStep(Step step) {
		Step currentStep = getCurrentStep();
		steps.addToSelection(step);
		BindUtils.postNotifyChange(null, null, this, "currentStep");
		int newIndex = steps.indexOf(step);
		int oldIndex = steps.indexOf(currentStep);
		steps.subList(Math.min(newIndex, oldIndex), Math.max(newIndex, oldIndex) + 1)
			.forEach(affectedStep -> BindUtils.postNotifyChange(null, null, affectedStep, "*"));
		;
	}

	public ListModelList<Step> getSteps() {
		return steps;
	}

	public class Step {
		private String label;
		private String icon;

		public Step(String label, String icon) {
			super();
			this.label = label;
			this.icon = icon;
		}

		public String getLabel() {
			return label;
		}
		
		public String getIcon() {
			return icon;
		}

		public String getStatus() {
			return isDone() ? "previous" : (getCurrentStep() == this ? "current" : "following"); 
		}
		
		public boolean isDone() {
			return steps.indexOf(this) < steps.indexOf(getCurrentStep()); 
		}
	}
}