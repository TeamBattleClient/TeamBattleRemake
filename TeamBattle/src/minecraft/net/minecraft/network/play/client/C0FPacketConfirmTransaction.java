package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0FPacketConfirmTransaction extends Packet {
	private short field_149534_b;
	private boolean field_149535_c;
	private int field_149536_a;

	public C0FPacketConfirmTransaction() {
	}

	public C0FPacketConfirmTransaction(int p_i45244_1_, short p_i45244_2_,
			boolean p_i45244_3_) {
		field_149536_a = p_i45244_1_;
		field_149534_b = p_i45244_2_;
		field_149535_c = p_i45244_3_;
	}

	public int func_149532_c() {
		return field_149536_a;
	}

	public short func_149533_d() {
		return field_149534_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processConfirmTransaction(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149536_a = p_148837_1_.readByte();
		field_149534_b = p_148837_1_.readShort();
		field_149535_c = p_148837_1_.readByte() != 0;
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format("id=%d, uid=%d, accepted=%b", new Object[] {
				Integer.valueOf(field_149536_a), Short.valueOf(field_149534_b),
				Boolean.valueOf(field_149535_c) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149536_a);
		p_148840_1_.writeShort(field_149534_b);
		p_148840_1_.writeByte(field_149535_c ? 1 : 0);
	}
}
