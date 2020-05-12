package backend.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/** configuration for API proxy (loaded from application.yaml) */
@Component
@ConfigurationProperties(prefix="proxy")
@Data
public class ProxyTargetConfig {
	List<Target> targetList;

	@Data public static class Target {
		private String name;
		private String scheme = "http";
		private String host;
		private int port = 80;
		private boolean portForwarding = false;
		private Client client;

	}

	@Data public static class Client {
		private String id;
		private String secret;
	}
}
