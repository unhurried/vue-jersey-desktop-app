package backend.resource

import backend.proxy.ApiProxy
import backend.proxy.InvalidTargetException
import backend.resource.exception.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("proxy/{target}/{path:.*}")
class ProxyResource {
    @Autowired
    lateinit var apiProxy: ApiProxy

    @Context
    lateinit var uriInfo: UriInfo

    @Context
    lateinit var headers: HttpHeaders

    @GET
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    operator fun get(@PathParam("target") target: String, @PathParam("path") path: String): Response {
        return proxy(target, path, HttpMethod.GET, null)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    fun post(@PathParam("target") target: String, @PathParam("path") path: String, entity: String?): Response {
        return proxy(target, path, HttpMethod.GET, entity)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    fun put(@PathParam("target") target: String, @PathParam("path") path: String, entity: String?): Response {
        return proxy(target, path, HttpMethod.GET, entity)
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    fun delete(@PathParam("target") target: String, @PathParam("path") path: String): Response {
        return proxy(target, path, HttpMethod.GET, null)
    }

    private fun proxy(target: String, path: String, method: String, entity: String?): Response {
        val uri = uriInfo.requestUriBuilder.replacePath(path).build()
        return try {
            apiProxy.proxy(target, uri, method, entity)
        } catch (e: InvalidTargetException) {
            throw BadRequestException("target is invalid.")
        }
    }
}