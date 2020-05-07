package vjda.resource.bean;

import java.io.Serializable;

import lombok.Data;

/** Data Transfer Object for request and response bodies of ToDoResource */
@Data public class ToDoListBean implements Serializable {
	private Long total;
	private Iterable<ToDoBean> items;
}