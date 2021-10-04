package backend.gui

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.awt.Desktop
import java.io.IOException

/** A command line component that opens the application URL in default browser.  */
@Component
class Browser : CommandLineRunner {
    @Autowired
    private lateinit var ctx: ApplicationContext;

    @Autowired
    private lateinit var environment: Environment

    @Throws(IOException::class)
    override fun run(vararg args: String) {
        // Skip opening the default browser when servlet is not running.
        var sctx: ServletWebServerApplicationContext
        try {
             sctx = ctx.getBean(ServletWebServerApplicationContext::class.java)
        } catch(e: NoSuchBeanDefinitionException) {
            return;
        }

        if (!listOf(*environment.activeProfiles).contains("development")) {
            val desktop = Desktop.getDesktop()
            val uri = UriComponentsBuilder
                    .fromUriString("http://localhost/")
                    .port(sctx.webServer.port)
                    .build().toUri()
            desktop.browse(uri)
        }
    }
}
