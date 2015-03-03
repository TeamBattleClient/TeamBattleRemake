package me.client.events;

import net.minecraft.client.entity.EntityClientPlayerMP;

import com.darkmagician6.eventapi.events.ClientEvent;

public class EventPreMotion extends ClientEvent{

	public EntityClientPlayerMP player;
	
	public EventPreMotion(EntityClientPlayerMP player) {
		this.player = player;
	}
	
}
