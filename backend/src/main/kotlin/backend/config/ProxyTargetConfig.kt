package backend.config

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import kotlin.annotation.Target

/** configuration for API proxy (loaded from application.yaml)  */
@Component
@ConfigurationProperties(prefix = "proxy")
data class ProxyTargetConfig (
    var targetList: List<Target>? = null
) {
    data class Target (
        var name: String? = null,
        var scheme: String = "http",
        var host: String? = null,
        var port: Int = 80,
        var portForwarding: Boolean = false,
        var client: Client? = null
    )

    data class Client (
        var id: String? = null,
        var secret: String? = null
    )
}