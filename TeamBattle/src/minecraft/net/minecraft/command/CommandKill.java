package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;

public class CommandKill extends CommandBase {

	@Override
	public String getCommandName() {
		return "kill";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.kill.usage";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		final EntityPlayerMP var3 = getCommandSenderAsPlayer(p_71515_1_);
		var3.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
		p_71515_1_.addChatMessage(new ChatComponentTranslation(
				"commands.kill.success", new Object[0]));
	}
}
