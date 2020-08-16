package backend.resource

import backend.config.PfConfigService
import backend.helper.BeanHelper
import backend.repository.entity.PfConfig
import backend.resource.bean.PfConfigBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/** A JAX-RS (Jersey) resource class to manage port forwarding config.  */
@Component
@Path("pf_config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
class PfConfigResource {
    @Autowired
    private lateinit var pfConfigService: PfConfigService

    @Autowired
    private lateinit var beanHelper: BeanHelper

    @GET
    fun get(): Response {
        return Response.ok().entity(pfConfigService.get()).build()
    }

    @PUT
    fun update(reqBean: PfConfigBean): Response {
        var entity = beanHelper.createAndCopy(reqBean, PfConfig::class.java)
        entity = pfConfigService.put(entity)
        val resBean = beanHelper.createAndCopy(entity, PfConfigBean::class.java)
        return Response.ok().entity(resBean).build()
    }
}