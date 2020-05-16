package backend.resource.bean;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import lombok.Data;

/** Data Transfer Object for request parameters for list resources. */
@Data public class ListParam {
	@DefaultValue("3")
	@QueryParam("size")
	private Integer size;

	@DefaultValue("0")
	@QueryParam("page")
	private Integer page;
}
