package backend.resource;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.helper.BeanHelper;
import backend.repository.ConfigRepository;
import backend.repository.entity.Config;
import backend.resource.bean.ConfigBean;

/** A JAX-RS (Jersey) resource class to manage application config. */
@Component
@Path("config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class ConfigResource {

	@Autowired private ConfigRepository repository;
	@Autowired private BeanHelper beanHelper;

	/** GET /api/config: Get config resource. */
	@GET
	public Response get() {
		Optional<Config> result = repository.findById(new Config().getId());
		if (result.isPresent()) {
			ConfigBean resBean = beanHelper.createAndCopy(result.get(), ConfigBean.class);
			return Response.ok().entity(resBean).build();
		} else {
			return Response.ok().entity(new ConfigBean()).build();
		}
	}

	/** PUT /api/config: Update config resource. */
	@PUT
	public Response update(ConfigBean reqBean) {
		Config entity = beanHelper.createAndCopy(reqBean, Config.class);
		entity = repository.save(entity);
		ConfigBean resBean = beanHelper.createAndCopy(entity, ConfigBean.class);
		return Response.ok().entity(resBean).build();
	}
}