package zk.example.template.locker.rx.operators;

import io.reactivex.functions.Action;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;

public class ZkDesktopOps {
	private Desktop desktop;
	public ZkDesktopOps(Desktop desktop) {
		this.desktop = desktop;
	}
	public Action activate() {
		return () -> {
			Executions.activate(desktop);
		};
	}
	public Action deactivate() {
		return () -> {
			if(Executions.getCurrent() != null) {
				Executions.deactivate(desktop);
			}
		};
	}
}