package vjda.proxy;

import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.SocketUtils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.Value;
import vjda.config.PortForwardingConfig;
import vjda.config.ProxyTargetConfig.Target;

/** A component that creates and caches a SSH connection and port forwarding settings. */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PortForwardManager {

	@Autowired private PortForwardingConfig portFwConfig;

	// cache for SSH session (connection)
	private Session session;

	private static final String SCHEME_HTTP = "http";
	private static final String LOCAL_HOST = "localhost";

	@Value public static class ProxiedTarget {
		private String scheme;
		private String host;
		private int port;
	}

	public synchronized ProxiedTarget getTarget(Target target) {
		// Return original target when port forwarding is not required.
		if (target.isPortForwarding() == false) {
			return new ProxiedTarget(target.getScheme(), target.getHost(), target.getPort());
		}

		// Get an existing SSH session or create a new one.
		final Session session = getSession();

		// Search for local port forwarding to the target.
		Optional<ProxiedTarget> result = searchForwardedTarget(session, target);

		// Return an existing port forwarding setting if it was created before.
		if (result.isPresent()) {
			return result.get();

		// Otherwise, add a port forwarding setting.
		} else {
			int localPort = SocketUtils.findAvailableTcpPort();
			try {
				session.setPortForwardingL(localPort, target.getHost(), target.getPort());
			} catch (JSchException e) {
				throw new RuntimeException(e);
			}
			return new ProxiedTarget(SCHEME_HTTP, LOCAL_HOST, localPort);
		}
	}

	/** Get an existing SSH session or create a new one. */
	private Session getSession() {
		// Return the existing session when it is still alive.
		if (this.session != null && this.session.isConnected()) {
			return this.session;
		}

		// Otherwise, create a new session.
		try {
			final JSch jsch = new JSch();
			final Session session = jsch.getSession(portFwConfig.getUsername(), portFwConfig.getHost(),
					portFwConfig.getPort());
			session.setPassword(portFwConfig.getPassword());

			final Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.connect();
			this.session = session;
			return session;

		} catch (JSchException je) {
			throw new RuntimeException(je);
		}
	}

	private Optional<ProxiedTarget> searchForwardedTarget(Session session, Target target) {
		Assert.notNull(session, "A SSH session must be created in advance.");
		Assert.notNull(target, "Target must not be null.");

		try {
			for (String pfl : session.getPortForwardingL()) {
				String[] pflArray = pfl.split(":");
				int localPort = Integer.valueOf(pflArray[0]);
				String remoteHost = pflArray[1];
				int remotePort = Integer.valueOf(pflArray[2]);

				if (remoteHost.equals(target.getHost()) && remotePort == target.getPort()) {
					return Optional.of(new ProxiedTarget(SCHEME_HTTP, LOCAL_HOST, localPort));
				}
			}
		} catch (NumberFormatException | JSchException e) {
			throw new RuntimeException(e);
		}

		return Optional.empty();
	}
}
