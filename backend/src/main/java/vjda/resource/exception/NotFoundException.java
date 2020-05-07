package vjda.resource.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import vjda.resource.bean.ErrorBean;

/** An exception to return a not found (404) error */
public class NotFoundException extends WebApplicationException {
	public NotFoundException() {
		super(Response.status(Status.NOT_FOUND).entity(createErrorBean()).build());
	}

	private static ErrorBean createErrorBean() {
		ErrorBean bean = new ErrorBean();
		bean.setErrorCode("not_found");
		return bean;
	}
}