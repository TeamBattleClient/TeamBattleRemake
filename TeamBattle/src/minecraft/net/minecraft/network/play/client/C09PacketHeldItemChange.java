package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C09PacketHeldItemChange extends Packet {
	private int field_149615_a;

	public C09PacketHeldItemChange() {
	}

	public C09PacketHeldItemChange(int p_i45262_1_) {
		field_149615_a = p_i45262_1_;
	}

	public int func_149614_c() {
		return field_149615_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processHeldItemChange(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149615_a = p_148837_1_.readShort();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeShort(field_149615_a);
	}
}
