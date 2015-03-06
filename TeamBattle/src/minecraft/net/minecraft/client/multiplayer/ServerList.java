package net.minecraft.client.multiplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerList {
	private static final Logger logger = LogManager.getLogger();

	public static void func_147414_b(ServerData p_147414_0_) {
		final ServerList var1 = new ServerList(Minecraft.getMinecraft());
		var1.loadServerList();

		for (int var2 = 0; var2 < var1.countServers(); ++var2) {
			final ServerData var3 = var1.getServerData(var2);

			if (var3.serverName.equals(p_147414_0_.serverName)
					&& var3.serverIP.equals(p_147414_0_.serverIP)) {
				var1.func_147413_a(var2, p_147414_0_);
				break;
			}
		}

		var1.saveServerList();
	}

	/** The Minecraft instance. */
	private final Minecraft mc;

	/** List of ServerData instances. */
	private final List servers = new ArrayList();

	public ServerList(Minecraft p_i1194_1_) {
		mc = p_i1194_1_;
		loadServerList();
	}

	/**
	 * Adds the given ServerData instance to the list.
	 */
	public void addServerData(ServerData p_78849_1_) {
		servers.add(p_78849_1_);
	}

	/**
	 * Counts the number of ServerData instances in the list.
	 */
	public int countServers() {
		return servers.size();
	}

	public void func_147413_a(int p_147413_1_, ServerData p_147413_2_) {
		servers.set(p_147413_1_, p_147413_2_);
	}

	/**
	 * Gets the ServerData instance stored for the given index in the list.
	 */
	public ServerData getServerData(int p_78850_1_) {
		return (ServerData) servers.get(p_78850_1_);
	}

	/**
	 * Loads a list of servers from servers.dat, by running
	 * ServerData.getServerDataFromNBTCompound on each NBT compound found in the
	 * "servers" tag list.
	 */
	public void loadServerList() {
		try {
			servers.clear();
			final NBTTagCompound var1 = CompressedStreamTools.read(new File(
					mc.mcDataDir, "servers.dat"));

			if (var1 == null)
				return;

			final NBTTagList var2 = var1.getTagList("servers", 10);

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				servers.add(ServerData.getServerDataFromNBTCompound(var2
						.getCompoundTagAt(var3)));
			}
		} catch (final Exception var4) {
			logger.error("Couldn\'t load server list", var4);
		}
	}

	/**
	 * Removes the ServerData instance stored for the given index in the list.
	 */
	public void removeServerData(int p_78851_1_) {
		servers.remove(p_78851_1_);
	}

	/**
	 * Runs getNBTCompound on each ServerData instance, puts everything into a
	 * "servers" NBT list and writes it to servers.dat.
	 */
	public void saveServerList() {
		try {
			final NBTTagList var1 = new NBTTagList();
			final Iterator var2 = servers.iterator();

			while (var2.hasNext()) {
				final ServerData var3 = (ServerData) var2.next();
				var1.appendTag(var3.getNBTCompound());
			}

			final NBTTagCompound var5 = new NBTTagCompound();
			var5.setTag("servers", var1);
			CompressedStreamTools.safeWrite(var5, new File(mc.mcDataDir,
					"servers.dat"));
		} catch (final Exception var4) {
			logger.error("Couldn\'t save server list", var4);
		}
	}

	/**
	 * Takes two list indexes, and swaps their order around.
	 */
	public void swapServers(int p_78857_1_, int p_78857_2_) {
		final ServerData var3 = getServerData(p_78857_1_);
		servers.set(p_78857_1_, getServerData(p_78857_2_));
		servers.set(p_78857_2_, var3);
		saveServerList();
	}
}
