package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S30PacketWindowItems extends Packet {
	private ItemStack[] field_148913_b;
	private int field_148914_a;

	public S30PacketWindowItems() {
	}

	public S30PacketWindowItems(int p_i45186_1_, List p_i45186_2_) {
		field_148914_a = p_i45186_1_;
		field_148913_b = new ItemStack[p_i45186_2_.size()];

		for (int var3 = 0; var3 < field_148913_b.length; ++var3) {
			final ItemStack var4 = (ItemStack) p_i45186_2_.get(var3);
			field_148913_b[var3] = var4 == null ? null : var4.copy();
		}
	}

	public ItemStack[] func_148910_d() {
		return field_148913_b;
	}

	public int func_148911_c() {
		return field_148914_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleWindowItems(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148914_a = p_148837_1_.readUnsignedByte();
		final short var2 = p_148837_1_.readShort();
		field_148913_b = new ItemStack[var2];

		for (int var3 = 0; var3 < var2; ++var3) {
			field_148913_b[var3] = p_148837_1_.readItemStackFromBuffer();
		}
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_148914_a);
		p_148840_1_.writeShort(field_148913_b.length);
		final ItemStack[] var2 = field_148913_b;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final ItemStack var5 = var2[var4];
			p_148840_1_.writeItemStackToBuffer(var5);
		}
	}
}
