package backend.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import java.io.IOException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.ext.Provider

/** A ContainerResponseFilter to add CORS response headers when running in "development" mode.
 * (CORS headers are not required in "production" mode as frontend app is served from backend server.)  */
@Provider
class CorsFilter : ContainerResponseFilter {
    @Autowired
    private lateinit var environment: Environment

    @Throws(IOException::class)
    override fun filter(request: ContainerRequestContext, response: ContainerResponseContext) {
        if (listOf(*environment.activeProfiles).contains("development")) {
            response.headers["Access-Control-Allow-Origin"] = listOf("*")
            response.headers["Access-Control-Allow-Headers"] = listOf("origin, content-type, accept, authorization")
            response.headers["Access-Control-Allow-Credentials"] = listOf("true")
            response.headers["Access-Control-Allow-Methods"] = listOf("GET, POST, PUT, DELETE, OPTIONS, HEAD")
        }
    }
}