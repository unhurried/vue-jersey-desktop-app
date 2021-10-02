package backend.repository.entity

import javax.persistence.*

/** A JPA entity corresponding to todo table  */
@Entity
@Table(name = "todo")
data class ToDo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String? = null,
    var category: Category? = null,
    var content: String? = null
) {
    enum class Category {
        one, two, three
    }
}