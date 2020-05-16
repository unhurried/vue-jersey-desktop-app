package backend.resource;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import backend.helper.BeanHelper;
import backend.repository.ToDoRepository;
import backend.repository.entity.ToDo;
import backend.resource.bean.ListParam;
import backend.resource.bean.ToDoBean;
import backend.resource.bean.ToDoListBean;
import backend.resource.bean.ToDoBean.Category;
import backend.resource.exception.NotFoundException;

/** A JAX-RS (Jersey) resource class for ToDo items */
@Component
@Path("todos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class ToDoResource {

	@Autowired private ToDoRepository repository;
	@Autowired private BeanHelper beanHelper;

	/** GET /api/todos: Get a list of todo items. */
	@GET
	public Response getList(@BeanParam ListParam listParam) {
		Pageable pageable = PageRequest.of(listParam.getPage(), listParam.getSize());
		Page<ToDo> entityList = repository.findAll(pageable);
		ToDoListBean list = new ToDoListBean();
		list.setItems(beanHelper.createAndCopyIterable(entityList, ToDoBean.class, (ToDo src, ToDoBean target) -> {
			target.setCategory(Category.valueOf(src.getCategory().toString()));
		}));
		list.setTotal(entityList.getTotalElements());
		return Response.ok().entity(list).build();
	}

	/** POST /api/todos: Create a new todo item. */
	@POST
	public Response create(ToDoBean reqBean) {
		ToDo entity = beanHelper.createAndCopy(reqBean, ToDo.class, (ToDoBean src, ToDo target) -> {
			target.setId(null);
			target.setCategory(ToDo.Category.valueOf(src.getCategory().toString()));
		});
		entity = repository.save(entity);
		ToDoBean resBean = beanHelper.createAndCopy(entity, ToDoBean.class, (ToDo src, ToDoBean target) -> {
			target.setCategory(Category.valueOf(src.getCategory().toString()));
		});
		return Response.ok().entity(resBean).build();
	}

	/** GET /api/todos/{id}: Get a todo item. */
	@GET @Path("{id}")
	public Response get(@PathParam("id") Long id) {
		Optional<ToDo> result = repository.findById(id);
		if(result.isPresent()) {
			ToDoBean bean = beanHelper.createAndCopy(result.get(), ToDoBean.class, (ToDo src, ToDoBean target) -> {
				target.setCategory(Category.valueOf(src.getCategory().toString()));
			});
			return Response.ok().entity(bean).build();
		} else {
			throw new NotFoundException();
		}
	}

	/** PUT /api/todos/{id}: Update a todo item. */
	@PUT @Path("{id}")
	public Response update(@PathParam("id") Long id, ToDoBean reqBean) {
		ToDo entity = beanHelper.createAndCopy(reqBean, ToDo.class, (ToDoBean src, ToDo target) -> {
			target.setId(id);
			target.setCategory(ToDo.Category.valueOf(src.getCategory().toString()));
		});
		repository.save(entity);
		return Response.ok().entity(reqBean).build();
	}

	/** DELETE /api/todos/{id}: Delete a todo item. */
	@DELETE @Path("{id}")
	public Response remove(@PathParam("id") Long id) {
		Optional<ToDo> result = repository.findById(id);
		if(result.isPresent()) {
			repository.deleteById(id);
			return Response.ok().build();
		} else {
			throw new NotFoundException();
		}
	}
}