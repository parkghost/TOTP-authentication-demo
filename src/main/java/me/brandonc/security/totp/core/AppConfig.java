package me.brandonc.security.totp.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;

@Configuration
@ImportResource({ "classpath*:META-INF/spring/applicationContext.xml" })
@ComponentScan(basePackages = "me.brandonc.security.totp", excludeFilters = { @Filter({ Configuration.class, Controller.class }) })
public class AppConfig {

}