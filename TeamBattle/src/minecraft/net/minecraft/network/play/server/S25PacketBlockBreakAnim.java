package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S25PacketBlockBreakAnim extends Packet {
	private int field_148848_d;
	private int field_148849_e;
	private int field_148850_b;
	private int field_148851_c;
	private int field_148852_a;

	public S25PacketBlockBreakAnim() {
	}

	public S25PacketBlockBreakAnim(int p_i45174_1_, int p_i45174_2_,
			int p_i45174_3_, int p_i45174_4_, int p_i45174_5_) {
		field_148852_a = p_i45174_1_;
		field_148850_b = p_i45174_2_;
		field_148851_c = p_i45174_3_;
		field_148848_d = p_i45174_4_;
		field_148849_e = p_i45174_5_;
	}

	public int func_148842_f() {
		return field_148848_d;
	}

	public int func_148843_e() {
		return field_148851_c;
	}

	public int func_148844_d() {
		return field_148850_b;
	}

	public int func_148845_c() {
		return field_148852_a;
	}

	public int func_148846_g() {
		return field_148849_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleBlockBreakAnim(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148852_a = p_148837_1_.readVarIntFromBuffer();
		field_148850_b = p_148837_1_.readInt();
		field_148851_c = p_148837_1_.readInt();
		field_148848_d = p_148837_1_.readInt();
		field_148849_e = p_148837_1_.readUnsignedByte();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148852_a);
		p_148840_1_.writeInt(field_148850_b);
		p_148840_1_.writeInt(field_148851_c);
		p_148840_1_.writeInt(field_148848_d);
		p_148840_1_.writeByte(field_148849_e);
	}
}
