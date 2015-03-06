package event.events;

import event.Cancellable;
import net.minecraft.network.Packet;
import event.Event;
public final class EventPacketSent extends Event implements Cancellable {
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
