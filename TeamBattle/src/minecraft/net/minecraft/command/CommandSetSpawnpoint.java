package net.minecraft.command;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

public class CommandSetSpawnpoint extends CommandBase {

	/**
	 * Adds the strings available in this command to the given list of tab
	 * completion options.
	 */
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return p_71516_2_.length != 1 && p_71516_2_.length != 2 ? null
				: getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer
						.getServer().getAllUsernames());
	}

	@Override
	public String getCommandName() {
		return "spawnpoint";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.spawnpoint.usage";
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
		final EntityPlayerMP var3 = p_71515_2_.length == 0 ? getCommandSenderAsPlayer(p_71515_1_)
				: getPlayer(p_71515_1_, p_71515_2_[0]);

		if (p_71515_2_.length == 4) {
			if (var3.worldObj != null) {
				final byte var4 = 1;
				final int var5 = 30000000;
				int var9 = var4 + 1;
				final int var6 = parseIntBounded(p_71515_1_, p_71515_2_[var4],
						-var5, var5);
				final int var7 = parseIntBounded(p_71515_1_,
						p_71515_2_[var9++], 0, 256);
				final int var8 = parseIntBounded(p_71515_1_,
						p_71515_2_[var9++], -var5, var5);
				var3.setSpawnChunk(new ChunkCoordinates(var6, var7, var8), true);
				func_152373_a(
						p_71515_1_,
						this,
						"commands.spawnpoint.success",
						new Object[] { var3.getCommandSenderName(),
								Integer.valueOf(var6), Integer.valueOf(var7),
								Integer.valueOf(var8) });
			}
		} else {
			if (p_71515_2_.length > 1)
				throw new WrongUsageException("commands.spawnpoint.usage",
						new Object[0]);

			final ChunkCoordinates var10 = var3.getPlayerCoordinates();
			var3.setSpawnChunk(var10, true);
			func_152373_a(
					p_71515_1_,
					this,
					"commands.spawnpoint.success",
					new Object[] { var3.getCommandSenderName(),
							Integer.valueOf(var10.posX),
							Integer.valueOf(var10.posY),
							Integer.valueOf(var10.posZ) });
		}
	}
}
