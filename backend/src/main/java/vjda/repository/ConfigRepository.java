package vjda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vjda.repository.entity.Config;

/** Spring Data JPA repository for ConifigEntity */
public interface ConfigRepository extends JpaRepository<Config, Long> {}