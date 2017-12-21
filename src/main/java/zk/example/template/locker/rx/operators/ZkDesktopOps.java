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
//			Logger.log("activate:" + desktop);
			Executions.activate(desktop);
//			Logger.log("activated:" + desktop);
		};
	}
	public Action deactivate() {
		return () -> {
			if(Executions.getCurrent() != null) {
				Executions.deactivate(desktop);
//				Logger.log("deactivated:" + desktop);
			}
		};
	}
}