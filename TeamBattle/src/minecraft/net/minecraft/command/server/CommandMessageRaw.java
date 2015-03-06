package net.minecraft.command.server;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonParseException;

public class CommandMessageRaw extends CommandBase {

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(
				p_71516_2_, MinecraftServer.getServer().getAllUsernames())
				: null;
	}

	@Override
	public String getCommandName() {
		return "tellraw";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.tellraw.usage";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	/**
	 * Return whether the specified command parameter index is a username
	 * parameter.
	 */
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_2_ == 0;
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length < 2)
			throw new WrongUsageException("commands.tellraw.usage",
					new Object[0]);
		else {
			final EntityPlayerMP var3 = getPlayer(p_71515_1_, p_71515_2_[0]);
			final String var4 = func_82360_a(p_71515_1_, p_71515_2_, 1);

			try {
				final IChatComponent var5 = IChatComponent.Serializer
						.func_150699_a(var4);
				var3.addChatMessage(var5);
			} catch (final JsonParseException var7) {
				final Throwable var6 = ExceptionUtils.getRootCause(var7);
				throw new SyntaxErrorException(
						"commands.tellraw.jsonException",
						new Object[] { var6 == null ? "" : var6.getMessage() });
			}
		}
	}
}
