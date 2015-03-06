package net.minecraft.command.server;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import com.mojang.authlib.GameProfile;

public class CommandPardonPlayer extends CommandBase {

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(
				p_71516_2_, MinecraftServer.getServer()
						.getConfigurationManager().func_152608_h()
						.func_152685_a()) : null;
	}

	/**
	 * Returns true if the given command sender is allowed to use this command.
	 */
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return MinecraftServer.getServer().getConfigurationManager()
				.func_152608_h().func_152689_b()
				&& super.canCommandSenderUseCommand(p_71519_1_);
	}

	@Override
	public String getCommandName() {
		return "pardon";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.unban.usage";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length == 1 && p_71515_2_[0].length() > 0) {
			final MinecraftServer var3 = MinecraftServer.getServer();
			final GameProfile var4 = var3.getConfigurationManager()
					.func_152608_h().func_152703_a(p_71515_2_[0]);

			if (var4 == null)
				throw new CommandException("commands.unban.failed",
						new Object[] { p_71515_2_[0] });
			else {
				var3.getConfigurationManager().func_152608_h()
						.func_152684_c(var4);
				func_152373_a(p_71515_1_, this, "commands.unban.success",
						new Object[] { p_71515_2_[0] });
			}
		} else
			throw new WrongUsageException("commands.unban.usage", new Object[0]);
	}
}
