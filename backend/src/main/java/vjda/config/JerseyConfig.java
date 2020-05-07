package vjda.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/** Configurations for Jersey (JAX-RS) */
@Component
@ApplicationPath("api")
@Slf4j
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		// packages(...) doesn't work when running application with a fat jar because it assumes .class files of
		// resource and provider classes can be accessed via file system.
		// To regsiter resource and provider classes dynamically, scan class path manually and look for class names.
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(Path.class));
		provider.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
		provider.findCandidateComponents("vjda").forEach(beanDefinition -> {
			try {
				log.info("Registering {} to jersey config.", beanDefinition.getBeanClassName());
				register(Class.forName(beanDefinition.getBeanClassName()));
			} catch (ClassNotFoundException e) {
				log.warn("Failed to register {} to jersey confing.", beanDefinition.getBeanClassName());
			}
		});
	}
}

