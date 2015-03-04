package me.client.events;

import net.minecraft.network.Packet;
import me.client.helpers.MinecraftHelper;

public class EventPacketSent implements MinecraftHelper{
	
	private boolean cancel;
	private Packet packet;

	public EventPacketSent(Packet packet) {
		this.packet = packet;
	}

	public Packet getPacket() {
		return packet;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}
}
	

