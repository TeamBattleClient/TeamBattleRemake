package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S38PacketPlayerListItem extends Packet {
	private boolean field_149124_b;
	private int field_149125_c;
	private String field_149126_a;

	public S38PacketPlayerListItem() {
	}

	public S38PacketPlayerListItem(String p_i45209_1_, boolean p_i45209_2_,
			int p_i45209_3_) {
		field_149126_a = p_i45209_1_;
		field_149124_b = p_i45209_2_;
		field_149125_c = p_i45209_3_;
	}

	public int func_149120_e() {
		return field_149125_c;
	}

	public boolean func_149121_d() {
		return field_149124_b;
	}

	public String func_149122_c() {
		return field_149126_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handlePlayerListItem(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149126_a = p_148837_1_.readStringFromBuffer(16);
		field_149124_b = p_148837_1_.readBoolean();
		field_149125_c = p_148837_1_.readShort();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149126_a);
		p_148840_1_.writeBoolean(field_149124_b);
		p_148840_1_.writeShort(field_149125_c);
	}
}
