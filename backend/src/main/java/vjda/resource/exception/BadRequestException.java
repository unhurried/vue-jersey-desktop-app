package vjda.resource.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import vjda.resource.bean.ErrorBean;

/** An exception to return a bad request (401) error */
public class BadRequestException extends WebApplicationException {
	public BadRequestException(String errorCode) {
		super(Response.status(Status.BAD_REQUEST).entity(createErrorBean(errorCode)).build());
	}

	private static ErrorBean createErrorBean(String errorCode) {
		ErrorBean bean = new ErrorBean();
		bean.setErrorCode(errorCode);
		return bean;
	}
}
