package backend.resource.bean;

import java.io.Serializable;

import lombok.Data;

/** Data Transfer Object for request and response bodies of ConfigResource */
@Data public class ConfigBean implements Serializable {
	private String username;
	private String password;
}