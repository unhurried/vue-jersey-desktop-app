package backend.gui

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
class Browser(
    val context: ApplicationContext,
    val environment: Environment) : CommandLineRunner {

    @Throws(IOException::class)
    override fun run(vararg args: String) {
        // Skip opening the default browser when servlet is not running
        // or the application is running in the development mode.
        if (context !is ServletWebServerApplicationContext ||
            listOf(*environment.activeProfiles).contains("development") ) {
            return
        }

        val desktop = Desktop.getDesktop()
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost/")
            .port((context as ServletWebServerApplicationContext).webServer.port)
            .build().toUri()
        desktop.browse(uri)
    }
}
