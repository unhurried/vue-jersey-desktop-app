package backend.resource;

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

import backend.config.PfConfigService;
import backend.helper.BeanHelper;
import backend.repository.entity.PfConfig;
import backend.resource.bean.PfConfigBean;

/** A JAX-RS (Jersey) resource class to manage port forwarding config. */
@Component
@Path("pf_config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class PfConfigResource {

	@Autowired private PfConfigService pfConfigService;
	@Autowired private BeanHelper beanHelper;

	@GET
	public Response get() {
		return Response.ok().entity(pfConfigService.get()).build();
	}

	@PUT
	public Response update(PfConfigBean reqBean) {
		PfConfig entity = beanHelper.createAndCopy(reqBean, PfConfig.class);
		entity = pfConfigService.put(entity);
		PfConfigBean resBean = beanHelper.createAndCopy(entity, PfConfigBean.class);
		return Response.ok().entity(resBean).build();
	}
}
