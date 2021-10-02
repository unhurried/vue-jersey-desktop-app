package backend.aspect

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

/** An aspect (Spring AOP) that logs HTTP method and path of API calls. */
// @Aspect and @Component are required to register an aspect.
@Aspect
@Component
class LoggerAspect (val request: HttpServletRequest) {
    private val logger = LoggerFactory.getLogger(LoggerAspect::class.java)

    // @Before: Execute an advice before a join point.
    @Before("within(backend.resource.ToDoResource)")
    public fun before() {
        logger.info("API call: " + request.method + " " + request.pathInfo);
    }
}