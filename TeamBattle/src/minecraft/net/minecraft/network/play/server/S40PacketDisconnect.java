package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S40PacketDisconnect extends Packet {
	private IChatComponent field_149167_a;

	public S40PacketDisconnect() {
	}

	public S40PacketDisconnect(IChatComponent p_i45191_1_) {
		field_149167_a = p_i45191_1_;
	}

	public IChatComponent func_149165_c() {
		return field_149167_a;
	}

	/**
	 * If true, the network manager will process the packet immediately when
	 * received, otherwise it will queue it for processing. Currently true for:
	 * Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
	 */
	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleDisconnect(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149167_a = IChatComponent.Serializer.func_150699_a(p_148837_1_
				.readStringFromBuffer(32767));
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(IChatComponent.Serializer
				.func_150696_a(field_149167_a));
	}
}
