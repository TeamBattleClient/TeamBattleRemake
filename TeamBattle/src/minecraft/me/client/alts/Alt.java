package me.client.alts;

public class Alt {
	private String mask = "";
	private final String username, password;

	public Alt(String username, String password) {
		this(username, password, "");
	}

	public Alt(String username, String password, String mask) {
		this.username = username;
		this.password = password;
		this.mask = mask;
	}

	public String getMask() {
		return mask;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
}