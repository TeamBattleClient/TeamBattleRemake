package me.client.helpers;

import net.minecraft.client.Minecraft;

public interface MinecraftHelper {

	public static final Minecraft mc = Minecraft.getMinecraft();

	public boolean isCancelled();

	public void setCancelled(boolean cancel);
}

