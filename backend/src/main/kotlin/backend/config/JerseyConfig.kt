package backend.config

import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Component
import java.util.function.Consumer
import javax.ws.rs.ApplicationPath
import javax.ws.rs.Path
import javax.ws.rs.ext.Provider

/** Configurations for Jersey (JAX-RS)  */
@Component
@ApplicationPath("api")
class JerseyConfig : ResourceConfig() {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
    init {
        // packages(...) doesn't work when running application with a fat jar because it assumes .class files of
        // resource and provider classes can be accessed via file system.
        // To register resource and provider classes dynamically, scan class path manually and look for class names.
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AnnotationTypeFilter(Path::class.java))
        provider.addIncludeFilter(AnnotationTypeFilter(Provider::class.java))
        provider.findCandidateComponents("backend").forEach(Consumer { beanDefinition: BeanDefinition ->
            try {
                log.info("Registering {} to jersey config.", beanDefinition.beanClassName)
                register(Class.forName(beanDefinition.beanClassName))
            } catch (e: ClassNotFoundException) {
                log.warn("Failed to register {} to jersey confing.", beanDefinition.beanClassName)
            }
        })
    }
}