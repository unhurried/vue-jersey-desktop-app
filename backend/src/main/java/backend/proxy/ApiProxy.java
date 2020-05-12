package backend.proxy;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.config.ProxyTargetConfig;
import backend.config.ProxyTargetConfig.Target;
import backend.proxy.PortForwardManager.ProxiedTarget;

/** A service class that proxies API requests and responses. */
@Component
public class ApiProxy {

	@Autowired private ProxyTargetConfig proxyTargetConfig;
	@Autowired private PortForwardManager pfManager;

	/** Proxies API requests to targets specified in configuration (application.yaml). */
	public Response proxy(String targetName, URI uri, String method, String entity) throws InvalidTargetException {
		Target target = searchEnvByName(targetName);
		ProxiedTarget proxiedTarget = pfManager.getTarget(target);

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			URI requestUri = UriBuilder.fromUri(uri)
					.scheme(proxiedTarget.getScheme()).host(proxiedTarget.getHost()).port(proxiedTarget.getPort()).build();
			RequestBuilder requestBuilder = RequestBuilder.create(method)
					.setUri(requestUri).addHeader("Host", target.getHost());
			if (target.getClient() != null) {
				final String clientId = target.getClient().getId();
				final String clientSecret = target.getClient().getSecret();
				final String headerValue = Base64.encodeBase64String((clientId + ":" + clientSecret).getBytes());
				requestBuilder.addHeader("Authorization", "Basic " + headerValue);
			}
			if (entity != null) {
				requestBuilder.setEntity(new StringEntity(entity));
			}
			HttpUriRequest request = requestBuilder.build();

			try (CloseableHttpResponse response = httpClient.execute(request)) {
				ResponseBuilder responseBuilder = Response.status(response.getStatusLine().getStatusCode());
				for (Header h : response.getAllHeaders()) {
					responseBuilder.header(h.getName(), h.getValue());
				}
				return responseBuilder.entity(EntityUtils.toString(response.getEntity())).build();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** Retrieve target info identified by name from applicaiton.yaml. */
	private Target searchEnvByName(String name) throws InvalidTargetException {
		for (Target env : this.proxyTargetConfig.getTargetList()) {
			if (env.getName().equals(name)) {
				return env;
			}
		}
		throw new InvalidTargetException();
	}
}
