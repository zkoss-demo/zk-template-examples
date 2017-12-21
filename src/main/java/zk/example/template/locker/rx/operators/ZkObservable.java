package zk.example.template.locker.rx.operators;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ZkObservable {

	public static <T> ObservableTransformer<T, T> activated() {
		return activated(Executions.getCurrent().getDesktop());
	}

	public static <T> ObservableTransformer<T, T> activated(Desktop desktop) {
		ZkDesktopOps desktopOps = new ZkDesktopOps(desktop);
		//use IO Scheduler - potentially blocking operation to wait for desktop activation
		return upstream -> upstream
				.observeOn(Schedulers.io())
				.doOnNext(toConsumer(desktopOps.activate())) //potentially blocking
				.doAfterNext(toConsumer(desktopOps.deactivate()))
				.doOnTerminate(desktopOps.deactivate());
	}

	private static <T> Consumer<T> toConsumer(Action action) {
		return ignored -> action.run();
	}
}
