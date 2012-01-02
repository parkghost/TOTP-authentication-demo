package me.brandonc.security.totp.core;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.Tomcat;

public class Main {
	public static void main(String[] args) throws Exception {

		File appBasePath = new File(args[0]);
		String appBase = appBasePath.getAbsolutePath();
		Tomcat tomcat = new Tomcat();

		// The port that we should run on can be set into an environment
		// variable
		// Look for that variable and default to 8080 if it isn't there.
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}

		tomcat.setPort(Integer.parseInt(webPort));
		tomcat.setBaseDir("./target/");
		tomcat.getHost().setAppBase(appBase);

		String contextPath = "/";

		final Context context = tomcat.addWebapp(contextPath, appBase);

		// override default webroot
		LifecycleListener afterStartListener = new LifecycleListener() {
			@Override
			public void lifecycleEvent(LifecycleEvent event) {
				if (Lifecycle.AFTER_START_EVENT.equals(event.getType())) {
					context.addServletMapping("/", "dispatcher");
				}
			}
		};

		context.addLifecycleListener(afterStartListener);

		tomcat.start();
		tomcat.getServer().await();

	}
}
