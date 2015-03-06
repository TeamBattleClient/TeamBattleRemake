package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase {

	/**
	 * Returns true if the given command sender is allowed to use this command.
	 */
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return MinecraftServer.getServer().isSinglePlayer()
				|| super.canCommandSenderUseCommand(p_71519_1_);
	}

	@Override
	public String getCommandName() {
		return "seed";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.seed.usage";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		final Object var3 = p_71515_1_ instanceof EntityPlayer ? ((EntityPlayer) p_71515_1_).worldObj
				: MinecraftServer.getServer().worldServerForDimension(0);
		p_71515_1_.addChatMessage(new ChatComponentTranslation(
				"commands.seed.success", new Object[] { Long
						.valueOf(((World) var3).getSeed()) }));
	}
}
