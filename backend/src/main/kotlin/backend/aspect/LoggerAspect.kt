package backend.aspect

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Aspect
@Component
class LoggerAspect (val request: HttpServletRequest) {
    private val logger = LoggerFactory.getLogger(LoggerAspect::class.java)

    @Before("within(backend.resource.ToDoResource)")
    public fun before() {
        logger.info("API call: " + request.method + " " + request.pathInfo);
    }
}