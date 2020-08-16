package backend.proxy

import backend.config.ProxyTargetConfig
import org.apache.commons.codec.binary.Base64
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URI
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriBuilder

/** A service class that proxies API requests and responses.  */
@Component
class ApiProxy {
    @Autowired
    private lateinit var proxyTargetConfig: ProxyTargetConfig

    @Autowired
    private lateinit var pfManager: PortForwardManager

    /** Proxies API requests to targets specified in configuration (application.yaml).  */
    @Throws(InvalidTargetException::class)
    fun proxy(targetName: String, uri: URI?, method: String?, entity: String?): Response {
        val target = searchEnvByName(targetName)
        val proxiedTarget = pfManager.getTarget(target)
        try {
            HttpClients.createDefault().use { httpClient ->
                val requestUri = UriBuilder.fromUri(uri)
                        .scheme(proxiedTarget.scheme).host(proxiedTarget.host).port(proxiedTarget.port).build()
                val requestBuilder = RequestBuilder.create(method)
                        .setUri(requestUri).addHeader("Host", target.host)
                if (target.client != null) {
                    val clientId = target.client!!.id
                    val clientSecret = target.client!!.secret
                    val headerValue = Base64.encodeBase64String("$clientId:$clientSecret".toByteArray())
                    requestBuilder.addHeader("Authorization", "Basic $headerValue")
                }
                if (entity != null) {
                    requestBuilder.entity = StringEntity(entity)
                }
                val request = requestBuilder.build()
                httpClient.execute(request).use { response ->
                    val responseBuilder = Response.status(response.statusLine.statusCode)
                    for (h in response.allHeaders) {
                        responseBuilder.header(h.name, h.value)
                    }
                    return responseBuilder.entity(EntityUtils.toString(response.entity)).build()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    /** Retrieve target info identified by name from applicaiton.yaml.  */
    @Throws(InvalidTargetException::class)
    private fun searchEnvByName(name: String): ProxyTargetConfig.Target {
        for (env in proxyTargetConfig.targetList!!) {
            if (env.name == name) {
                return env
            }
        }
        throw InvalidTargetException()
    }
}