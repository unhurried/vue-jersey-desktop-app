package backend.repository.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/** A JPA entity corresponding to pf_config table */
@Entity
@Table(name="pf_config")
@Data
public class PfConfig {
	@Id private final Long id = 1L;
	private String host = "127.0.0.1";
	private Integer port = 22;
	private String username = "username";
	private String password = "password";
}