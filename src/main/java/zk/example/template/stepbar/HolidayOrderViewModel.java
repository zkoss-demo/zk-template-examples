package zk.example.template.stepbar;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class HolidayOrderViewModel {
	public static final String SUBMIT_COMMAND = "submit";

	private StepModel stepModel;

	private boolean carAdded = false;
	
	@Init
	public void init() {
		stepModel = new StepModel();
		appendStep("Destination", "z-icon-globe");
		appendStep("Accomodation", "z-icon-hotel");
		appendStep("Personal Details", "z-icon-user");
		appendStep("Payment", "z-icon-credit-card");
		appendStep("Enjoy Holiday", "z-icon-smile-o");
		stepModel.getSteps().addToSelection(stepModel.getSteps().get(0));
	}
	
	@Command
	@NotifyChange("carAdded")
	public void addCar() {
		insertStep(2, "Rent Car", "z-icon-car");
		this.carAdded = true;
		
	}

	@Command
	public void gotoStep(@BindingParam("step") StepModel.Step step) {
		stepModel.gotoStep(step);
	}

	@Command
	public void next() {
		stepModel.next();
	}
	
	@Command
	public void back() {
		stepModel.back();
	}
	
	public StepModel getStepModel() {
		return stepModel;
	}

	public boolean isCarAdded() {
		return carAdded;
	}
	
	public void appendStep(String label, String icon) {
		stepModel.getSteps().add(stepModel.new Step(label, icon));
	}
	
	public void insertStep(int index, String label, String icon) {
		stepModel.getSteps().add(index, stepModel.new Step(label, icon));
	}
}
