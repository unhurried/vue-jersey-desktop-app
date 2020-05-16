package backend.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.repository.PfConfigRepository;
import backend.repository.entity.PfConfig;

@Component
public class PfConfigService {

	@Autowired private PfConfigRepository repository;

	public PfConfig get() {
		Optional<PfConfig> result = repository.findById(new PfConfig().getId());
		return result.isPresent() ? result.get() : new PfConfig();
	}

	public PfConfig put(PfConfig entity) {
		return repository.save(entity);
	}
}
