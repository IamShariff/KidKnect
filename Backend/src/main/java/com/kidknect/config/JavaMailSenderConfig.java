package com.kidknect.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration class for setting up JavaMailSender to send emails.
 */
@Configuration
public class JavaMailSenderConfig {

	// Injecting properties from application.properties or application.yml
	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private int port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String auth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String startTls;

	/**
	 * Bean to create an instance of JavaMailSender.
	 *
	 * @return JavaMailSender instance configured with the specified properties.
	 */
	@Bean
	JavaMailSender mailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(host);
		javaMailSender.setPort(port);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);

		// Setting additional properties for the mail sender
		Properties props = new Properties();
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.starttls.enable", startTls);

		javaMailSender.setJavaMailProperties(props);

		return javaMailSender;
	}

}
