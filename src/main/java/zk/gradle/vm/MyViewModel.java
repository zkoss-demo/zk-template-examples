package zk.gradle.vm;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class MyViewModel {
	public static final String SUBMIT_COMMAND = "submit";
	private String name;
	private String response;
	
	@Command(SUBMIT_COMMAND)
	@NotifyChange("response")
	public void submit() {
		response = String.format("Hello %s!", name);
	} 
	
	public String getResponse() {
		return response;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
