package net.minecraft.command.server;

import io.netty.buffer.ByteBuf;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public abstract class CommandBlockLogic implements ICommandSender {
	private static final SimpleDateFormat field_145766_a = new SimpleDateFormat(
			"HH:mm:ss");
	private String field_145761_f = "@";
	private IChatComponent field_145762_d = null;
	private String field_145763_e = "";
	private int field_145764_b;
	private boolean field_145765_c = true;

	/**
	 * Notifies this sender of some sort of information. This is for messages
	 * intended to display to the user. Used for typical output (like
	 * "you asked for whether or not this game rule is set, so here's your answer"
	 * ), warnings (like "I fetched this block for you by ID, but I'd like you
	 * to know that every time you do this, I die a little
	 * inside"), and errors (like "it's not called iron_pixacke, silly").
	 */
	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		if (field_145765_c && getEntityWorld() != null
				&& !getEntityWorld().isClient) {
			field_145762_d = new ChatComponentText("["
					+ field_145766_a.format(new Date()) + "] ")
					.appendSibling(p_145747_1_);
			func_145756_e();
		}
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 */
	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return p_70003_1_ <= 2;
	}

	@Override
	public IChatComponent func_145748_c_() {
		return new ChatComponentText(getCommandSenderName());
	}

	public IChatComponent func_145749_h() {
		return field_145762_d;
	}

	public void func_145750_b(IChatComponent p_145750_1_) {
		field_145762_d = p_145750_1_;
	}

	public abstract int func_145751_f();

	public void func_145752_a(String p_145752_1_) {
		field_145763_e = p_145752_1_;
	}

	public String func_145753_i() {
		return field_145763_e;
	}

	public void func_145754_b(String p_145754_1_) {
		field_145761_f = p_145754_1_;
	}

	public void func_145755_a(World p_145755_1_) {
		if (p_145755_1_.isClient) {
			field_145764_b = 0;
		}

		final MinecraftServer var2 = MinecraftServer.getServer();

		if (var2 != null && var2.isCommandBlockEnabled()) {
			final ICommandManager var3 = var2.getCommandManager();
			field_145764_b = var3.executeCommand(this, field_145763_e);
		} else {
			field_145764_b = 0;
		}
	}

	public abstract void func_145756_e();

	public abstract void func_145757_a(ByteBuf p_145757_1_);

	public void func_145758_a(NBTTagCompound p_145758_1_) {
		p_145758_1_.setString("Command", field_145763_e);
		p_145758_1_.setInteger("SuccessCount", field_145764_b);
		p_145758_1_.setString("CustomName", field_145761_f);

		if (field_145762_d != null) {
			p_145758_1_.setString("LastOutput",
					IChatComponent.Serializer.func_150696_a(field_145762_d));
		}

		p_145758_1_.setBoolean("TrackOutput", field_145765_c);
	}

	public void func_145759_b(NBTTagCompound p_145759_1_) {
		field_145763_e = p_145759_1_.getString("Command");
		field_145764_b = p_145759_1_.getInteger("SuccessCount");

		if (p_145759_1_.func_150297_b("CustomName", 8)) {
			field_145761_f = p_145759_1_.getString("CustomName");
		}

		if (p_145759_1_.func_150297_b("LastOutput", 8)) {
			field_145762_d = IChatComponent.Serializer
					.func_150699_a(p_145759_1_.getString("LastOutput"));
		}

		if (p_145759_1_.func_150297_b("TrackOutput", 1)) {
			field_145765_c = p_145759_1_.getBoolean("TrackOutput");
		}
	}

	public int func_145760_g() {
		return field_145764_b;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return field_145761_f;
	}
}
