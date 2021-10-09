package backend.resource

import backend.repository.ToDoRepository
import backend.repository.entity.ToDo
import backend.resource.bean.ToDoBean
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import javax.ws.rs.core.Response

// @SpringBootTest: Required to enable Spring features in the test class.
// webEnvironment specifies how the servlet environment is provided.
// (Provide a real or mock servlet environment, or not using a servlet.)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ToDoResourceTest (@Autowired private val resource: ToDoResource) {

    // Use @MockBean (Spring Boot annotation) instead of @Mock (Mockito annotation).
    @MockBean lateinit var repository: ToDoRepository

    @Test
    fun testCreate() {
        val t = ToDo()
        t.id = 1L
        t.title = "Test ToDo Item"
        t.category = ToDo.Category.one
        t.content = "This is a test ToDo item."
        given(repository.save(any(ToDo::class.java))).willReturn(t)
        val bean = ToDoBean()
        bean.id = 1L
        bean.title = "Test ToDo Item"
        bean.category = ToDoBean.Category.one
        bean.content = "This is a test ToDo item."
        val res: Response = resource.create(bean)
        assertThat(res.entity).isEqualToComparingFieldByField(bean)
    }
}
