package backend.resource.bean

/** Data Transfer Object for request and response bodies of ToDoResource  */
data class ToDoBean (
    var id: Long? = null,
    var title: String? = null,
    var category: Category? = null,
    var content: String? = null
) {
    enum class Category {
        one, two, three
    }
}