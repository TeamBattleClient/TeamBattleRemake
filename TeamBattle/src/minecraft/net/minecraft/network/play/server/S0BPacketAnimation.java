package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0BPacketAnimation extends Packet {
	private int field_148980_b;
	private int field_148981_a;

	public S0BPacketAnimation() {
	}

	public S0BPacketAnimation(Entity p_i45172_1_, int p_i45172_2_) {
		field_148981_a = p_i45172_1_.getEntityId();
		field_148980_b = p_i45172_2_;
	}

	public int func_148977_d() {
		return field_148980_b;
	}

	public int func_148978_c() {
		return field_148981_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleAnimation(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148981_a = p_148837_1_.readVarIntFromBuffer();
		field_148980_b = p_148837_1_.readUnsignedByte();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format(
				"id=%d, type=%d",
				new Object[] { Integer.valueOf(field_148981_a),
						Integer.valueOf(field_148980_b) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148981_a);
		p_148840_1_.writeByte(field_148980_b);
	}
}
