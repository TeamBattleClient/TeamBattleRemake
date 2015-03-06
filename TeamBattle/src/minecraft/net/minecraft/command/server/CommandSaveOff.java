package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOff extends CommandBase {

	@Override
	public String getCommandName() {
		return "save-off";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.save-off.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		final MinecraftServer var3 = MinecraftServer.getServer();
		boolean var4 = false;

		for (final WorldServer worldServer : var3.worldServers) {
			if (worldServer != null) {
				final WorldServer var6 = worldServer;

				if (!var6.levelSaving) {
					var6.levelSaving = true;
					var4 = true;
				}
			}
		}

		if (var4) {
			func_152373_a(p_71515_1_, this, "commands.save.disabled",
					new Object[0]);
		} else
			throw new CommandException("commands.save-off.alreadyOff",
					new Object[0]);
	}
}
