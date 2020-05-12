package backend.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/** A command line component that opens the application URL in default browser. */
@Component
public class Browser implements CommandLineRunner {

	@Autowired private ServletWebServerApplicationContext context;
	@Autowired private Environment environment;

	@Override
	public void run(String... args) throws IOException {
		if (!Arrays.asList(environment.getActiveProfiles()).contains("development")) {
			Desktop desktop = Desktop.getDesktop();
			URI uri = UriComponentsBuilder
					.fromUriString("http://localhost/")
					.port(context.getWebServer().getPort())
					.build().toUri();
			desktop.browse(uri);
		}
	}
}
