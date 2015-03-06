package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S28PacketEffect extends Packet {
	private boolean field_149246_f;
	private int field_149247_d;
	private int field_149248_e;
	private int field_149249_b;
	private int field_149250_c;
	private int field_149251_a;

	public S28PacketEffect() {
	}

	public S28PacketEffect(int p_i45198_1_, int p_i45198_2_, int p_i45198_3_,
			int p_i45198_4_, int p_i45198_5_, boolean p_i45198_6_) {
		field_149251_a = p_i45198_1_;
		field_149250_c = p_i45198_2_;
		field_149247_d = p_i45198_3_;
		field_149248_e = p_i45198_4_;
		field_149249_b = p_i45198_5_;
		field_149246_f = p_i45198_6_;
	}

	public int func_149239_h() {
		return field_149248_e;
	}

	public int func_149240_f() {
		return field_149250_c;
	}

	public int func_149241_e() {
		return field_149249_b;
	}

	public int func_149242_d() {
		return field_149251_a;
	}

	public int func_149243_g() {
		return field_149247_d;
	}

	public boolean func_149244_c() {
		return field_149246_f;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEffect(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149251_a = p_148837_1_.readInt();
		field_149250_c = p_148837_1_.readInt();
		field_149247_d = p_148837_1_.readByte() & 255;
		field_149248_e = p_148837_1_.readInt();
		field_149249_b = p_148837_1_.readInt();
		field_149246_f = p_148837_1_.readBoolean();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149251_a);
		p_148840_1_.writeInt(field_149250_c);
		p_148840_1_.writeByte(field_149247_d & 255);
		p_148840_1_.writeInt(field_149248_e);
		p_148840_1_.writeInt(field_149249_b);
		p_148840_1_.writeBoolean(field_149246_f);
	}
}
