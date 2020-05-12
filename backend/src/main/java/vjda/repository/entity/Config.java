package vjda.repository.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/** A JPA entity corresponding to config table */
@Entity
@Table(name="config")
@Data
public class Config {
    @Id
    private Long id = 1L;
	private String username;
	private String password;
}