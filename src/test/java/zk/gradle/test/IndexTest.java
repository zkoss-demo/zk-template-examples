package zk.gradle.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zul.Textbox;

public class IndexTest {

	private Client zatsClient;

	@BeforeClass
	public static void init() {
		Zats.init("src/main/webapp");
	}
	
	@AfterClass
	public static void destroy() {
		Zats.end();
	}
	
	@Before
	public void setup() {
		zatsClient = Zats.newClient();
	}
	
	@Test
	public void testIndex() {
		DesktopAgent desktopAgent = zatsClient.connect("/index.zul");

		ComponentAgent button = desktopAgent.query("#testButton");
		ComponentAgent textbox = desktopAgent.query("#testInput");
		
		Assert.assertEquals("", textbox.as(Textbox.class).getValue());
		button.click();
		Assert.assertEquals("Test Successful", textbox.as(Textbox.class).getValue());
	}
	
}
