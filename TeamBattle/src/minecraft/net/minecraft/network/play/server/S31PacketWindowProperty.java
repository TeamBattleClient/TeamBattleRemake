package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S31PacketWindowProperty extends Packet {
	private int field_149184_b;
	private int field_149185_c;
	private int field_149186_a;

	public S31PacketWindowProperty() {
	}

	public S31PacketWindowProperty(int p_i45187_1_, int p_i45187_2_,
			int p_i45187_3_) {
		field_149186_a = p_i45187_1_;
		field_149184_b = p_i45187_2_;
		field_149185_c = p_i45187_3_;
	}

	public int func_149180_e() {
		return field_149185_c;
	}

	public int func_149181_d() {
		return field_149184_b;
	}

	public int func_149182_c() {
		return field_149186_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleWindowProperty(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149186_a = p_148837_1_.readUnsignedByte();
		field_149184_b = p_148837_1_.readShort();
		field_149185_c = p_148837_1_.readShort();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149186_a);
		p_148840_1_.writeShort(field_149184_b);
		p_148840_1_.writeShort(field_149185_c);
	}
}
