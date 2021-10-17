package backend.resource.exception

import backend.resource.bean.ErrorBean
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response.*
import javax.ws.rs.core.Response.Status.*

/** An exception to return a bad request (401) error  */
class BadRequestException(errorCode: String) : WebApplicationException (
    status(BAD_REQUEST).entity(ErrorBean(errorCode)).build()
)