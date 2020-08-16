package backend.resource.exception

import backend.resource.bean.ErrorBean
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response.*
import javax.ws.rs.core.Response.Status.*

/** An exception to return a not found (404) error  */
class NotFoundException : WebApplicationException(
        status(NOT_FOUND).entity(ErrorBean("not_found")).build()
)
