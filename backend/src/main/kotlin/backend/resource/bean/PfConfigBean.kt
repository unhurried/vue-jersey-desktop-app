package backend.resource.bean

import lombok.Data
import java.io.Serializable

/** Data Transfer Object for request and response bodies of ConfigResource  */
data class PfConfigBean(
    var host: String? = null,
    var port: Int? = null,
    var username: String? = null,
    var password: String? = null
)