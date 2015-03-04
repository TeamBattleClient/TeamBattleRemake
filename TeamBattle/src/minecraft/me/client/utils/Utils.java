package me.client.utils;

import me.client.helpers.MinecraftHelper;
import net.minecraft.network.Packet;

public class Utils implements MinecraftHelper{

	public static void sendPacket(Packet packet) {
		mc.getNetHandler().addToSendQueue(packet);
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void setCancelled(boolean cancel) {
		
	}
	
}
