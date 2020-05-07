package vjda.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import vjda.proxy.ApiProxy;
import vjda.proxy.InvalidTargetException;
import vjda.resource.exception.BadRequestException;

@Path("proxy/{target}/{path:.*}")
public class ProxyResource {
	@Autowired private ApiProxy apiProxy;
	@Context UriInfo uriInfo;
	@Context HttpHeaders headers;

	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public Response get(@PathParam("target") String target, @PathParam("path") String path) {
		return proxy(target, path, HttpMethod.GET, null);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public Response post(@PathParam("target") String target, @PathParam("path") String path, String entity) {
		return proxy(target, path, HttpMethod.GET, entity);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public Response put(@PathParam("target") String target, @PathParam("path") String path, String entity) {
		return proxy(target, path, HttpMethod.GET, entity);
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public Response delete(@PathParam("target") String target, @PathParam("path") String path) {
		return proxy(target, path, HttpMethod.GET, null);
	}

	private Response proxy(String target, String path, String method, String entity) {
		URI uri = uriInfo.getRequestUriBuilder().replacePath(path).build();
		try {
			return apiProxy.proxy(target, uri, method, entity);
		} catch (InvalidTargetException e) {
			throw new BadRequestException("target is invalid.");
		}
	}
}
