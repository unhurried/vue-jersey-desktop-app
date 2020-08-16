package backend.resource.bean

import lombok.Data
import java.io.Serializable

/** Data Transfer Object for request and response bodies of ToDoResource  */
data class ToDoListBean (
    var total: Long? = null,
    var items: Iterable<ToDoBean>? = null
)