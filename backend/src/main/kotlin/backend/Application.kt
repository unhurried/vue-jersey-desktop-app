package backend

import backend.gui.ConsoleFrame
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/** Main application class for Spring Boot  */
@SpringBootApplication
@EntityScan
@EnableJpaRepositories
class Application

fun main(args: Array<String>) {
    // Open a console window built with Swing to show stdout messages.
    if (System.getProperty("spring.profiles.active") != "development") {
        ConsoleFrame.show()
    }

    // Disable colored output, that is not supported in ConsoleFrame.
    for (i in args.indices) {
        if (args[i].startsWith("--spring.output.ansi.enabled")) {
            args[i] = "--spring.output.ansi.enabled=never"
        }
    }

    // Prevent Spring application from running in headless mode.
    runApplication<Application>(*args) {
        setHeadless(false)
        this.addListeners()
    }
}