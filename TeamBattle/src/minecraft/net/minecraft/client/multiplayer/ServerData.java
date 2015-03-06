package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ServerData {
	public static enum ServerResourceMode {
		DISABLED("DISABLED", 1, "disabled"), ENABLED("ENABLED", 0, "enabled"), PROMPT(
				"PROMPT", 2, "prompt");
		private final IChatComponent field_152594_d;

		private ServerResourceMode(String p_i1053_1_, int p_i1053_2_,
				String p_i1053_3_) {
			field_152594_d = new ChatComponentTranslation(
					"addServer.resourcePack." + p_i1053_3_, new Object[0]);
		}

		public IChatComponent func_152589_a() {
			return field_152594_d;
		}
	}

	/**
	 * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a ServerData
	 * instance.
	 */
	public static ServerData getServerDataFromNBTCompound(
			NBTTagCompound p_78837_0_) {
		final ServerData var1 = new ServerData(p_78837_0_.getString("name"),
				p_78837_0_.getString("ip"));

		if (p_78837_0_.func_150297_b("icon", 8)) {
			var1.func_147407_a(p_78837_0_.getString("icon"));
		}

		if (p_78837_0_.func_150297_b("acceptTextures", 1)) {
			if (p_78837_0_.getBoolean("acceptTextures")) {
				var1.func_152584_a(ServerData.ServerResourceMode.ENABLED);
			} else {
				var1.func_152584_a(ServerData.ServerResourceMode.DISABLED);
			}
		} else {
			var1.func_152584_a(ServerData.ServerResourceMode.PROMPT);
		}

		return var1;
	}

	private String field_147411_m;

	public String field_147412_i;

	private ServerData.ServerResourceMode field_152587_j;
	private boolean field_152588_l;

	public boolean field_78841_f;
	public int field_82821_f;
	/** Game version for this server. */
	public String gameVersion;
	/** last server ping that showed up in the server browser */
	public long pingToServer;
	/**
	 * the string indicating number of players on and capacity of the server
	 * that is shown on the server browser (i.e. "5/20" meaning 5 slots used out
	 * of 20 slots total)
	 */
	public String populationInfo;
	public String serverIP;

	/**
	 * (better variable name would be 'hostname') server name as displayed in
	 * the server browser's second line (grey text)
	 */
	public String serverMOTD;

	public String serverName;

	public ServerData(String p_i1193_1_, String p_i1193_2_) {
		field_82821_f = 5;
		gameVersion = "1.7.10";
		field_152587_j = ServerData.ServerResourceMode.PROMPT;
		serverName = p_i1193_1_;
		serverIP = p_i1193_2_;
	}

	public ServerData(String p_i1055_1_, String p_i1055_2_, boolean p_i1055_3_) {
		this(p_i1055_1_, p_i1055_2_);
		field_152588_l = p_i1055_3_;
	}

	public void func_147407_a(String p_147407_1_) {
		field_147411_m = p_147407_1_;
	}

	public String func_147409_e() {
		return field_147411_m;
	}

	public void func_152583_a(ServerData p_152583_1_) {
		serverIP = p_152583_1_.serverIP;
		serverName = p_152583_1_.serverName;
		func_152584_a(p_152583_1_.func_152586_b());
		field_147411_m = p_152583_1_.field_147411_m;
	}

	public void func_152584_a(ServerData.ServerResourceMode p_152584_1_) {
		field_152587_j = p_152584_1_;
	}

	public boolean func_152585_d() {
		return field_152588_l;
	}

	public ServerData.ServerResourceMode func_152586_b() {
		return field_152587_j;
	}

	/**
	 * Returns an NBTTagCompound with the server's name, IP and maybe
	 * acceptTextures.
	 */
	public NBTTagCompound getNBTCompound() {
		final NBTTagCompound var1 = new NBTTagCompound();
		var1.setString("name", serverName);
		var1.setString("ip", serverIP);

		if (field_147411_m != null) {
			var1.setString("icon", field_147411_m);
		}

		if (field_152587_j == ServerData.ServerResourceMode.ENABLED) {
			var1.setBoolean("acceptTextures", true);
		} else if (field_152587_j == ServerData.ServerResourceMode.DISABLED) {
			var1.setBoolean("acceptTextures", false);
		}

		return var1;
	}
}
