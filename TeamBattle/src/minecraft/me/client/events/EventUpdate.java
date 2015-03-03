package me.client.events;

import net.minecraft.client.entity.EntityClientPlayerMP;

import com.darkmagician6.eventapi.events.ClientEvent;

public class EventUpdate extends ClientEvent{

	public EntityClientPlayerMP player;
	
	public EventUpdate(EntityClientPlayerMP player) {
		this.player = player;
	}
}
