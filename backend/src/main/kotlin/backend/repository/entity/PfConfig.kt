package backend.repository.entity

import lombok.Data
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/** A JPA entity corresponding to pf_config table  */
@Entity
@Table(name = "pf_config")
data class PfConfig (
    @Id var id: Long = 1L,
    var host: String = "127.0.0.1",
    var port: Int = 22,
    var username: String  = "username",
    var password: String = "password"
)
