package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C11PacketEnchantItem extends Packet {
	private int field_149540_b;
	private int field_149541_a;

	public C11PacketEnchantItem() {
	}

	public C11PacketEnchantItem(int p_i45245_1_, int p_i45245_2_) {
		field_149541_a = p_i45245_1_;
		field_149540_b = p_i45245_2_;
	}

	public int func_149537_d() {
		return field_149540_b;
	}

	public int func_149539_c() {
		return field_149541_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processEnchantItem(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149541_a = p_148837_1_.readByte();
		field_149540_b = p_148837_1_.readByte();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format(
				"id=%d, button=%d",
				new Object[] { Integer.valueOf(field_149541_a),
						Integer.valueOf(field_149540_b) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149541_a);
		p_148840_1_.writeByte(field_149540_b);
	}
}
