package down.TeamBattle.Modules.Modules.addons;

public final class Point {
	protected String name;
	protected final String server;
	protected final int x, y, z;

	public Point(String name, String server, int x, int y, int z) {
		this.name = name;
		this.server = server;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getName() {
		return name;
	}

	public String getServer() {
		return server;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setName(String name) {
		this.name = name;
	}
}