package net.minecraft.network.play.server;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3FPacketCustomPayload extends Packet {
	private byte[] field_149171_b;
	private String field_149172_a;

	public S3FPacketCustomPayload() {
	}

	public S3FPacketCustomPayload(String p_i45190_1_, byte[] p_i45190_2_) {
		field_149172_a = p_i45190_1_;
		field_149171_b = p_i45190_2_;

		if (p_i45190_2_.length >= 1048576)
			throw new IllegalArgumentException(
					"Payload may not be larger than 1048576 bytes");
	}

	public S3FPacketCustomPayload(String p_i45189_1_, ByteBuf p_i45189_2_) {
		this(p_i45189_1_, p_i45189_2_.array());
	}

	public byte[] func_149168_d() {
		return field_149171_b;
	}

	public String func_149169_c() {
		return field_149172_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleCustomPayload(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149172_a = p_148837_1_.readStringFromBuffer(20);
		field_149171_b = new byte[p_148837_1_.readUnsignedShort()];
		p_148837_1_.readBytes(field_149171_b);
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149172_a);
		p_148840_1_.writeShort(field_149171_b.length);
		p_148840_1_.writeBytes(field_149171_b);
	}
}
