package backend.resource.bean

import java.io.Serializable

/** Data Transfer Object for request and response bodies in error responses  */
data class ErrorBean (
    var errorCode: String? = null
)
