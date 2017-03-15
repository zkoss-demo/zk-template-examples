package zk.gradle.test;

import org.apache.commons.codec.language.bm.Rule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DefaultZatsEnvironment;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.ZatsEnvironment;

/**
 * A {@link TestRule} implementing {@link ExternalResource} creating and destroying a {@link ZatsEnvironment}.</br>
 * Used with {@link ClassRule} it provides a pluggable alternative to separate static methods annotated with {@link BeforeClass} and {@link AfterClass}</br>
 * </br>
 * Also see: <a href="https://dzone.com/articles/junit-49-class-and-suite-level-rules" target="_blank">dzone.com/articles/junit-49-class-and-suite-level-rules</a>
 */
public class ZatsEnv extends ExternalResource {
	private ZatsEnvironment env;
	
	private String resourceRoot;
	private String webInfPath;
	
	/**
	 * Creates a default {@link DefaultZatsEnvironment#DefaultZatsEnvironment()}
	 * @param resourceRoot - will be passed to {@link ZatsEnvironment#init(String)}
	 */
	public ZatsEnv(String resourceRoot) {
		this.resourceRoot = resourceRoot;
	}

	/**
	 * Creates a {@link DefaultZatsEnvironment#DefaultZatsEnvironment()} with a specified WEB-INF folder
	 * @param webInfPath - will be used as the constructor argument in {@link DefaultZatsEnvironment#DefaultZatsEnvironment(String)}
	 * @param resourceRoot - will be passed to {@link ZatsEnvironment#init(String)}
	 */
	public ZatsEnv(String webInfPath, String resourceRoot) {
		this.webInfPath = webInfPath;
		this.resourceRoot = resourceRoot;
	}

	@Override
	protected void before() throws Throwable {
		if(webInfPath != null) {
			env = new DefaultZatsEnvironment(webInfPath);
		} else {
			env = new DefaultZatsEnvironment();
		}
		env.init(resourceRoot); 
	}
	
	@Override
	protected void after() {
		env.destroy();
	}

	/**
	 * A {@link TestRule} implementing {@link ExternalResource} automatically creating and destroying a new Zats {@link Client} instance.</br>
	 * Used with {@link Rule} this will provide a pluggable alternative to separate methods annotated with {@link Before} and {@link After}
	 */
	public class AutoClient extends ExternalResource {
		private org.zkoss.zats.mimic.Client client;

		@Override
		protected void before() throws Throwable {
			client = env.newClient();
		}
		
		@Override
		protected void after() {
			client.destroy();
		}
		
		/**
		 * convenience method to load a zul page directly (calls: {@link Client#connect(String)})
		 * @param zulPath
		 * @return {@link DesktopAgent}
		 */
		public DesktopAgent connect(String zulPath) {
			return client.connect(zulPath);
		}
		
		/**
		 * allows access to the automatically created Zats {@link Client} instance. To access all functionality.  
		 * @return the current Zats {@link Client}
		 */
		public Client getClient() {
			return client;
		}
	}

	/**
	 * creates an {@link AutoClient} rule from the current {@link ZatsEnv}
	 * @return an {@link AutoClient} rule
	 */
	public AutoClient autoClient() {
		return new AutoClient();
	}
}
