package backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/** A component to load a configuration file (application.yaml). */
@Component
@ConfigurationProperties(prefix="proxy.port-forwarding")
@Data
public class PortForwardingConfig {
	private String host;
	private int port;
	private String username;
	private String password;
}