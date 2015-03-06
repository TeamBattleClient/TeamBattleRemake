package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S32PacketConfirmTransaction extends Packet {
	private short field_148892_b;
	private boolean field_148893_c;
	private int field_148894_a;

	public S32PacketConfirmTransaction() {
	}

	public S32PacketConfirmTransaction(int p_i45182_1_, short p_i45182_2_,
			boolean p_i45182_3_) {
		field_148894_a = p_i45182_1_;
		field_148892_b = p_i45182_2_;
		field_148893_c = p_i45182_3_;
	}

	public boolean func_148888_e() {
		return field_148893_c;
	}

	public int func_148889_c() {
		return field_148894_a;
	}

	public short func_148890_d() {
		return field_148892_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleConfirmTransaction(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148894_a = p_148837_1_.readUnsignedByte();
		field_148892_b = p_148837_1_.readShort();
		field_148893_c = p_148837_1_.readBoolean();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format("id=%d, uid=%d, accepted=%b", new Object[] {
				Integer.valueOf(field_148894_a), Short.valueOf(field_148892_b),
				Boolean.valueOf(field_148893_c) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_148894_a);
		p_148840_1_.writeShort(field_148892_b);
		p_148840_1_.writeBoolean(field_148893_c);
	}
}
