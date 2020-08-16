package backend.resource

import backend.helper.BeanHelper
import backend.repository.ToDoRepository
import backend.repository.entity.ToDo
import backend.resource.bean.ListParam
import backend.resource.bean.ToDoBean
import backend.resource.bean.ToDoListBean
import backend.resource.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.function.BiConsumer
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/** A JAX-RS (Jersey) resource class for ToDo items  */
@Component
@Path("todos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class ToDoResource {
    @Autowired
    private lateinit var repository: ToDoRepository

    @Autowired
    private lateinit var beanHelper: BeanHelper

    /** GET /api/todos: Get a list of todo items.  */
    @GET
    fun getList(@BeanParam listParam: ListParam): Response {
        val pageable: Pageable = PageRequest.of(listParam.page, listParam.size)
        val entityList = repository.findAll(pageable)
        val list = ToDoListBean()
        list.items = beanHelper.createAndCopyIterable(entityList, ToDoBean::class.java, BiConsumer { src: ToDo, target: ToDoBean -> target.category = ToDoBean.Category.valueOf(src.category.toString()) })
        list.total = entityList.totalElements
        return Response.ok().entity(list).build()
    }

    /** POST /api/todos: Create a new todo item.  */
    @POST
    fun create(reqBean: ToDoBean): Response {
        var entity = beanHelper.createAndCopy(reqBean, ToDo::class.java, BiConsumer { src: ToDoBean, target: ToDo ->
            target.id = null
            target.category = ToDo.Category.valueOf(src.category.toString())
        })
        entity = repository.save(entity)
        val resBean = beanHelper.createAndCopy(entity, ToDoBean::class.java, BiConsumer { src: ToDo, target: ToDoBean -> target.category = ToDoBean.Category.valueOf(src.category.toString()) })
        return Response.ok().entity(resBean).build()
    }

    /** GET /api/todos/{id}: Get a todo item.  */
    @GET
    @Path("{id}")
    operator fun get(@PathParam("id") id: Long): Response {
        val result = repository.findById(id)
        return if (result.isPresent) {
            val bean = beanHelper.createAndCopy(result.get(), ToDoBean::class.java, BiConsumer { src: ToDo, target: ToDoBean -> target.category = ToDoBean.Category.valueOf(src.category.toString()) })
            Response.ok().entity(bean).build()
        } else {
            throw NotFoundException()
        }
    }

    /** PUT /api/todos/{id}: Update a todo item.  */
    @PUT
    @Path("{id}")
    fun update(@PathParam("id") id: Long?, reqBean: ToDoBean): Response {
        val entity = beanHelper.createAndCopy(reqBean, ToDo::class.java, BiConsumer { src: ToDoBean, target: ToDo ->
            target.id = id
            target.category = ToDo.Category.valueOf(src.category.toString())
        })
        repository.save(entity)
        return Response.ok().entity(reqBean).build()
    }

    /** DELETE /api/todos/{id}: Delete a todo item.  */
    @DELETE
    @Path("{id}")
    fun remove(@PathParam("id") id: Long): Response {
        val result = repository.findById(id)
        return if (result.isPresent) {
            repository.deleteById(id)
            Response.ok().build()
        } else {
            throw NotFoundException()
        }
    }
}