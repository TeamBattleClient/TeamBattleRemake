package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2FPacketSetSlot extends Packet {
	private int field_149177_b;
	private ItemStack field_149178_c;
	private int field_149179_a;

	public S2FPacketSetSlot() {
	}

	public S2FPacketSetSlot(int p_i45188_1_, int p_i45188_2_,
			ItemStack p_i45188_3_) {
		field_149179_a = p_i45188_1_;
		field_149177_b = p_i45188_2_;
		field_149178_c = p_i45188_3_ == null ? null : p_i45188_3_.copy();
	}

	public int func_149173_d() {
		return field_149177_b;
	}

	public ItemStack func_149174_e() {
		return field_149178_c;
	}

	public int func_149175_c() {
		return field_149179_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSetSlot(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149179_a = p_148837_1_.readByte();
		field_149177_b = p_148837_1_.readShort();
		field_149178_c = p_148837_1_.readItemStackFromBuffer();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149179_a);
		p_148840_1_.writeShort(field_149177_b);
		p_148840_1_.writeItemStackToBuffer(field_149178_c);
	}
}
