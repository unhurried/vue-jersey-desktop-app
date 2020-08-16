package backend.repository

import backend.repository.entity.PfConfig
import org.springframework.data.jpa.repository.JpaRepository

/** Spring Data JPA repository for PfConifigEntity  */
interface PfConfigRepository : JpaRepository<PfConfig, Long>