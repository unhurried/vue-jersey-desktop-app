package backend.gui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.awt.Desktop
import java.io.IOException

/** A command line component that opens the application URL in default browser.  */
@Component
class Browser : CommandLineRunner {
    @Autowired
    private lateinit var context: ServletWebServerApplicationContext

    @Autowired
    private lateinit var environment: Environment

    @Throws(IOException::class)
    override fun run(vararg args: String) {
        if (!listOf(*environment.activeProfiles).contains("development")) {
            val desktop = Desktop.getDesktop()
            val uri = UriComponentsBuilder
                    .fromUriString("http://localhost/")
                    .port(context.webServer.port)
                    .build().toUri()
            desktop.browse(uri)
        }
    }
}