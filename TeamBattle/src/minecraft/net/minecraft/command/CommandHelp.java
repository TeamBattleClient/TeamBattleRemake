package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

public class CommandHelp extends CommandBase {

	@Override
	public List getCommandAliases() {
		return Arrays.asList(new String[] { "?" });
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	protected Map getCommands() {
		return MinecraftServer.getServer().getCommandManager().getCommands();
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.help.usage";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	/**
	 * Returns a sorted list of all possible commands for the given
	 * ICommandSender.
	 */
	protected List getSortedPossibleCommands(ICommandSender p_71534_1_) {
		final List var2 = MinecraftServer.getServer().getCommandManager()
				.getPossibleCommands(p_71534_1_);
		Collections.sort(var2);
		return var2;
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		final List var3 = getSortedPossibleCommands(p_71515_1_);
		final byte var4 = 7;
		final int var5 = (var3.size() - 1) / var4;
		int var13;

		try {
			var13 = p_71515_2_.length == 0 ? 0 : parseIntBounded(p_71515_1_,
					p_71515_2_[0], 1, var5 + 1) - 1;
		} catch (final NumberInvalidException var12) {
			final Map var8 = getCommands();
			final ICommand var9 = (ICommand) var8.get(p_71515_2_[0]);

			if (var9 != null)
				throw new WrongUsageException(var9.getCommandUsage(p_71515_1_),
						new Object[0]);

			if (MathHelper.parseIntWithDefault(p_71515_2_[0], -1) != -1)
				throw var12;

			throw new CommandNotFoundException();
		}

		final int var7 = Math.min((var13 + 1) * var4, var3.size());
		final ChatComponentTranslation var14 = new ChatComponentTranslation(
				"commands.help.header", new Object[] {
						Integer.valueOf(var13 + 1), Integer.valueOf(var5 + 1) });
		var14.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
		p_71515_1_.addChatMessage(var14);

		for (int var15 = var13 * var4; var15 < var7; ++var15) {
			final ICommand var10 = (ICommand) var3.get(var15);
			final ChatComponentTranslation var11 = new ChatComponentTranslation(
					var10.getCommandUsage(p_71515_1_), new Object[0]);
			var11.getChatStyle().setChatClickEvent(
					new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/"
							+ var10.getCommandName() + " "));
			p_71515_1_.addChatMessage(var11);
		}

		if (var13 == 0 && p_71515_1_ instanceof EntityPlayer) {
			final ChatComponentTranslation var16 = new ChatComponentTranslation(
					"commands.help.footer", new Object[0]);
			var16.getChatStyle().setColor(EnumChatFormatting.GREEN);
			p_71515_1_.addChatMessage(var16);
		}
	}
}
