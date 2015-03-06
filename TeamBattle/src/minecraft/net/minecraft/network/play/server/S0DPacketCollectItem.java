package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0DPacketCollectItem extends Packet {
	private int field_149356_b;
	private int field_149357_a;

	public S0DPacketCollectItem() {
	}

	public S0DPacketCollectItem(int p_i45232_1_, int p_i45232_2_) {
		field_149357_a = p_i45232_1_;
		field_149356_b = p_i45232_2_;
	}

	public int func_149353_d() {
		return field_149356_b;
	}

	public int func_149354_c() {
		return field_149357_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleCollectItem(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149357_a = p_148837_1_.readInt();
		field_149356_b = p_148837_1_.readInt();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149357_a);
		p_148840_1_.writeInt(field_149356_b);
	}
}
