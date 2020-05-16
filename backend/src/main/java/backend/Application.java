package backend;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import backend.gui.ConsoleFrame;

/** Main application class for Spring Boot */
@SpringBootApplication
@EntityScan
@EnableJpaRepositories
public class Application extends SpringBootServletInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.out.println(servletContext.getContextPath());
	}

	public static void main(String[] args) throws IOException {
		// Open a console window built with Swing to show stdout messages.
		ConsoleFrame.show();

		// Disable colored output, that is not supported in ConsoleFrame.
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--spring.output.ansi.enabled")) {
				args[i] = "--spring.output.ansi.enabled=never";
			}
		}

		// Prevent Spring application from running in headless mode.
		new SpringApplicationBuilder(Application.class).headless(false).run(args);
	}
}