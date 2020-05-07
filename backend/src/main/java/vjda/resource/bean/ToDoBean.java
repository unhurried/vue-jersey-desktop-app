package vjda.resource.bean;

import java.io.Serializable;

import lombok.Data;

/** Data Transfer Object for request and response bodies of ToDoResource */
@Data public class ToDoBean implements Serializable {
	private Long id;
	private String title;
	private Category category;
	private String content;

	public enum Category { one, two, three }
}