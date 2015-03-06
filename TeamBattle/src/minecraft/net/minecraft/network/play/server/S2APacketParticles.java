package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2APacketParticles extends Packet {
	private float field_149230_f;
	private float field_149231_g;
	private float field_149232_d;
	private float field_149233_e;
	private float field_149234_b;
	private float field_149235_c;
	private String field_149236_a;
	private float field_149237_h;
	private int field_149238_i;

	public S2APacketParticles() {
	}

	public S2APacketParticles(String p_i45199_1_, float p_i45199_2_,
			float p_i45199_3_, float p_i45199_4_, float p_i45199_5_,
			float p_i45199_6_, float p_i45199_7_, float p_i45199_8_,
			int p_i45199_9_) {
		field_149236_a = p_i45199_1_;
		field_149234_b = p_i45199_2_;
		field_149235_c = p_i45199_3_;
		field_149232_d = p_i45199_4_;
		field_149233_e = p_i45199_5_;
		field_149230_f = p_i45199_6_;
		field_149231_g = p_i45199_7_;
		field_149237_h = p_i45199_8_;
		field_149238_i = p_i45199_9_;
	}

	public double func_149220_d() {
		return field_149234_b;
	}

	public float func_149221_g() {
		return field_149233_e;
	}

	public int func_149222_k() {
		return field_149238_i;
	}

	public float func_149223_i() {
		return field_149231_g;
	}

	public float func_149224_h() {
		return field_149230_f;
	}

	public double func_149225_f() {
		return field_149232_d;
	}

	public double func_149226_e() {
		return field_149235_c;
	}

	public float func_149227_j() {
		return field_149237_h;
	}

	public String func_149228_c() {
		return field_149236_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleParticles(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149236_a = p_148837_1_.readStringFromBuffer(64);
		field_149234_b = p_148837_1_.readFloat();
		field_149235_c = p_148837_1_.readFloat();
		field_149232_d = p_148837_1_.readFloat();
		field_149233_e = p_148837_1_.readFloat();
		field_149230_f = p_148837_1_.readFloat();
		field_149231_g = p_148837_1_.readFloat();
		field_149237_h = p_148837_1_.readFloat();
		field_149238_i = p_148837_1_.readInt();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149236_a);
		p_148840_1_.writeFloat(field_149234_b);
		p_148840_1_.writeFloat(field_149235_c);
		p_148840_1_.writeFloat(field_149232_d);
		p_148840_1_.writeFloat(field_149233_e);
		p_148840_1_.writeFloat(field_149230_f);
		p_148840_1_.writeFloat(field_149231_g);
		p_148840_1_.writeFloat(field_149237_h);
		p_148840_1_.writeInt(field_149238_i);
	}
}
