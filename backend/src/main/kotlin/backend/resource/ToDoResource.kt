package backend.resource

import backend.helper.BeanHelper
import backend.repository.ToDoRepository
import backend.repository.entity.ToDo
import backend.resource.bean.ListParam
import backend.resource.bean.ToDoBean
import backend.resource.bean.ToDoListBean
import backend.resource.exception.NotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/** A JAX-RS (Jersey) resource class for ToDo items  */
// Resource classes must be annotated with one of @Component, @Service, @Controller, @Repository.
// (Note: Spring AOP doesn't work if @Named is used instead.)
@Component
@Path("todos") // Base path for all methods in the class
@Consumes(MediaType.APPLICATION_JSON) // Content-Type of request body
@Produces(MediaType.APPLICATION_JSON) // Content-Type of response body
@Transactional // Make all the methods in the class transactional.
class ToDoResource (private val repository: ToDoRepository, val beanHelper: BeanHelper) {
    val beanToEntity = { src: ToDoBean, target: ToDo ->
        target.category = ToDo.Category.valueOf(src.category.toString())
    }

    val entityToBean = { src: ToDo, target: ToDoBean ->
        target.category = ToDoBean.Category.valueOf(src.category.toString()) }

    /** GET /api/todos: Get a list of todo items.  */
    @GET
    fun getList(@BeanParam listParam: ListParam): Response {
        val pageable: Pageable = PageRequest.of(listParam.page, listParam.size)
        val entityList = repository.findAll(pageable)
        var list = ToDoListBean()
        list.items = beanHelper.createAndCopyIterable(entityList, ToDoBean::class.java, entityToBean)
        list.total = entityList.totalElements
        return Response.ok().entity(list).build()
    }

    /** POST /api/todos: Create a new todo item.  */
    @POST
    fun create(reqBean: ToDoBean): Response {
        var entity = beanHelper.createAndCopy(reqBean, ToDo::class.java, beanToEntity)
        entity = repository.save(entity)
        val resBean = beanHelper.createAndCopy(entity, ToDoBean::class.java, entityToBean)
        return Response.ok().entity(resBean).build()
    }

    /** GET /api/todos/{id}: Get a todo item.  */
    @GET
    @Path("{id}")
    operator fun get(@PathParam("id") id: Long): Response {
        val result = repository.findById(id)
        return if (result.isPresent) {
            val bean = beanHelper.createAndCopy(result.get(), ToDoBean::class.java, entityToBean)
            Response.ok().entity(bean).build()
        } else {
            throw NotFoundException()
        }
    }

    /** PUT /api/todos/{id}: Update a todo item.  */
    @PUT
    @Path("{id}")
    fun update(@PathParam("id") id: Long?, reqBean: ToDoBean): Response {
        reqBean.id = id
        val entity = beanHelper.createAndCopy(reqBean, ToDo::class.java, beanToEntity)
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