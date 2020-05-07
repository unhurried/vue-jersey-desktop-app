package vjda.provider;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/** A ContainerResponseFilter to add CORS response headers when running in "development" mode.
 *  (CORS headers are not required in "production" mode as frontend app is served from backend server.) */
@Provider
public class CorsFilter implements ContainerResponseFilter {

	@Autowired private Environment environment;

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		if (Arrays.asList(environment.getActiveProfiles()).contains("development")) {
			response.getHeaders().replace("Access-Control-Allow-Origin", Arrays.asList("*"));
			response.getHeaders().replace("Access-Control-Allow-Headers", Arrays.asList("origin, content-type, accept, authorization"));
			response.getHeaders().replace("Access-Control-Allow-Credentials", Arrays.asList("true"));
			response.getHeaders().replace("Access-Control-Allow-Methods", Arrays.asList("GET, POST, PUT, DELETE, OPTIONS, HEAD"));
		}
	}
}
