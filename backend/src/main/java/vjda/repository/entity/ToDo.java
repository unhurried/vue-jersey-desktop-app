package vjda.repository.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/** A JPA entity corresponding to todo table */
@Entity
@Table(name="todo")
@Data
public class ToDo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	private String title;
	private Category category;
	private String content;

	public enum Category { one, two, three }
}