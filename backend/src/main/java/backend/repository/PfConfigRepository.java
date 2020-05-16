package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.repository.entity.PfConfig;

/** Spring Data JPA repository for PfConifigEntity */
public interface PfConfigRepository extends JpaRepository<PfConfig, Long> {}