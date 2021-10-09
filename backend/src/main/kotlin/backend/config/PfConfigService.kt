package backend.config

import backend.repository.PfConfigRepository
import backend.repository.entity.PfConfig
import org.springframework.stereotype.Component

@Component
class PfConfigService (private val repository: PfConfigRepository) {

    fun get(): PfConfig {
        val result = repository.findById(PfConfig().id)
        return if (result.isPresent) result.get() else PfConfig()
    }

    fun put(entity: PfConfig): PfConfig {
        return repository.save(entity)
    }
}