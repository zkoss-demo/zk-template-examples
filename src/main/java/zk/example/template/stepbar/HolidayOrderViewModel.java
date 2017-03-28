package zk.example.template.stepbar;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import zk.example.template.stepbar.StepBarModel.Step;

public class HolidayOrderViewModel {
	private StepBarModel stepBarModel;
	private boolean carAdded = false;
	
	@Init
	public void init() {
		stepBarModel = new StepBarModel();
		addStep("Destination", "z-icon-globe", "steps/destination.zul");
		addStep("Accommodation", "z-icon-hotel", "steps/accommodation.zul");
		addStep("Personal Details", "z-icon-user", "steps/personal-details.zul");
		addStep("Payment", "z-icon-credit-card", "steps/payment.zul");
		addStep("Enjoy Holiday", "z-icon-smile-o", "steps/finish.zul");
		stepBarModel.getItems().addToSelection(stepBarModel.getItems().get(0));
	}
	
	@Command
	public void gotoStep(@BindingParam("step") StepBarModel.Step step) {
		stepBarModel.navigateTo(step);
	}
	
	@Command
	public void next() {
		stepBarModel.next();
	}
	
	@Command
	public void back() {
		stepBarModel.back();
	}
	
	@Command
	@NotifyChange("carAdded")
	public void addCar() {
		addStep(2, "Rent Car", "z-icon-car", "steps/rent-car.zul");
		this.carAdded = true;
		
	}

	@Command
	@NotifyChange("carAdded")
	public void removeCar() {
		Step carStep = stepBarModel.getCurrent();
		stepBarModel.next();
		stepBarModel.getItems().remove(carStep);
		this.carAdded = false;
	}
	
	public StepBarModel getStepBarModel() {
		return stepBarModel;
	}

	public boolean isCarAdded() {
		return carAdded;
	}
	
	public void addStep(String label, String icon, String uri) {
		addStep(stepBarModel.getItems().size(), label, icon, uri);
	}
	
	public void addStep(int index, String label, String icon, String uri) {
		stepBarModel.getItems().add(index, stepBarModel.new Step(label, icon, uri));
	}
}
