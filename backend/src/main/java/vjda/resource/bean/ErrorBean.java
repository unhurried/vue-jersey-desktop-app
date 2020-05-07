package vjda.resource.bean;

import java.io.Serializable;

import lombok.Data;

/** Data Transfer Object for request and response bodies in error responses */
@Data public class ErrorBean implements Serializable {
	private String errorCode;
}