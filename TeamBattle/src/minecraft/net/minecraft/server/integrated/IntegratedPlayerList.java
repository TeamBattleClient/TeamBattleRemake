package net.minecraft.server.integrated;

import java.net.SocketAddress;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.ServerConfigurationManager;

import com.mojang.authlib.GameProfile;

public class IntegratedPlayerList extends ServerConfigurationManager {
	/**
	 * Holds the NBT data for the host player's save file, so this can be
	 * written to level.dat.
	 */
	private NBTTagCompound hostPlayerData;

	public IntegratedPlayerList(IntegratedServer p_i1314_1_) {
		super(p_i1314_1_);
		func_152611_a(10);
	}

	@Override
	public String func_148542_a(SocketAddress p_148542_1_,
			GameProfile p_148542_2_) {
		return p_148542_2_.getName().equalsIgnoreCase(
				getServerInstance().getServerOwner())
				&& func_152612_a(p_148542_2_.getName()) != null ? "That name is already taken."
				: super.func_148542_a(p_148542_1_, p_148542_2_);
	}

	/**
	 * On integrated servers, returns the host's player data to be written to
	 * level.dat.
	 */
	@Override
	public NBTTagCompound getHostPlayerData() {
		return hostPlayerData;
	}

	@Override
	public IntegratedServer getServerInstance() {
		return (IntegratedServer) super.getServerInstance();
	}

	/**
	 * also stores the NBTTags if this is an intergratedPlayerList
	 */
	@Override
	protected void writePlayerData(EntityPlayerMP p_72391_1_) {
		if (p_72391_1_.getCommandSenderName().equals(
				getServerInstance().getServerOwner())) {
			hostPlayerData = new NBTTagCompound();
			p_72391_1_.writeToNBT(hostPlayerData);
		}

		super.writePlayerData(p_72391_1_);
	}
}
