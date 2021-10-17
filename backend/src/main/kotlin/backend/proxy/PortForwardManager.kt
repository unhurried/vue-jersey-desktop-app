package backend.proxy

import backend.config.PfConfigService
import backend.config.ProxyTargetConfig
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.SocketUtils
import java.util.*

/** A component that creates and caches a SSH connection and port forwarding settings.  */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class PortForwardManager (private val pfConfigService: PfConfigService) {

    // Return the existing session when it is still alive.
    // Otherwise, create a new session.

    /** Get an existing SSH session or create a new one.  */
    // cache for SSH session (connection)
    private var session: Session? = null
        get() // Return the existing session when it is still alive.
        {
            return if (field != null && field!!.isConnected) {
                field
            } else try {
                val jsch = JSch()
                val (_, host, port, username, password) = pfConfigService.get()
                val session = jsch.getSession(username, host, port)
                session.setPassword(password)
                val props = Properties()
                props["StrictHostKeyChecking"] = "no"
                session.setConfig(props)
                session.connect()
                field = session
                session
            } catch (je: JSchException) {
                throw RuntimeException(je)
            }
        }

    // Otherwise, create a new session.

    data class ProxiedTarget (
        var scheme: String? = null,
        var host: String? = null,
        var port: Int = 0
    )

    @Synchronized
    fun getTarget(target: ProxyTargetConfig.Target): ProxiedTarget {
        // Return original target when port forwarding is not required.
        if (!target.portForwarding) {
            return ProxiedTarget(target.scheme, target.host, target.port)
        }

        // Get an existing SSH session or create a new one.
        val session = session

        // Search for local port forwarding to the target.
        val result = searchForwardedTarget(session, target)

        // Return an existing port forwarding setting if it was created before.
        return if (result.isPresent) {
            result.get()

            // Otherwise, add a port forwarding setting.
        } else {
            val localPort = SocketUtils.findAvailableTcpPort()
            try {
                session!!.setPortForwardingL(localPort, target.host, target.port)
            } catch (e: JSchException) {
                throw RuntimeException(e)
            }
            ProxiedTarget(SCHEME_HTTP, LOCAL_HOST, localPort)
        }
    }

    private fun searchForwardedTarget(session: Session?, target: ProxyTargetConfig.Target): Optional<ProxiedTarget> {
        Assert.notNull(session, "A SSH session must be created in advance.")
        Assert.notNull(target, "Target must not be null.")
        try {
            for (pfl in session!!.portForwardingL) {
                val pflArray = pfl.split(":").toTypedArray()
                val localPort = Integer.valueOf(pflArray[0])
                val remoteHost = pflArray[1]
                val remotePort = Integer.valueOf(pflArray[2])
                if (remoteHost == target.host && remotePort == target.port) {
                    return Optional.of(ProxiedTarget(SCHEME_HTTP, LOCAL_HOST, localPort))
                }
            }
        } catch (e: NumberFormatException) {
            throw RuntimeException(e)
        } catch (e: JSchException) {
            throw RuntimeException(e)
        }
        return Optional.empty()
    }

    companion object {
        private const val SCHEME_HTTP = "http"
        private const val LOCAL_HOST = "localhost"
    }
}