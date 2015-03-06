package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S08PacketPlayerPosLook extends Packet {
	private boolean field_148935_f;
	public float field_148936_d;
	public float field_148937_e;
	private double field_148938_b;
	private double field_148939_c;
	private double field_148940_a;

	public S08PacketPlayerPosLook() {
	}

	public S08PacketPlayerPosLook(double p_i45164_1_, double p_i45164_3_,
			double p_i45164_5_, float p_i45164_7_, float p_i45164_8_,
			boolean p_i45164_9_) {
		field_148940_a = p_i45164_1_;
		field_148938_b = p_i45164_3_;
		field_148939_c = p_i45164_5_;
		field_148936_d = p_i45164_7_;
		field_148937_e = p_i45164_8_;
		field_148935_f = p_i45164_9_;
	}

	public double func_148928_d() {
		return field_148938_b;
	}

	public boolean func_148929_h() {
		return field_148935_f;
	}

	public float func_148930_g() {
		return field_148937_e;
	}

	public float func_148931_f() {
		return field_148936_d;
	}

	public double func_148932_c() {
		return field_148940_a;
	}

	public double func_148933_e() {
		return field_148939_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handlePlayerPosLook(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148940_a = p_148837_1_.readDouble();
		field_148938_b = p_148837_1_.readDouble();
		field_148939_c = p_148837_1_.readDouble();
		field_148936_d = p_148837_1_.readFloat();
		field_148937_e = p_148837_1_.readFloat();
		field_148935_f = p_148837_1_.readBoolean();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeDouble(field_148940_a);
		p_148840_1_.writeDouble(field_148938_b);
		p_148840_1_.writeDouble(field_148939_c);
		p_148840_1_.writeFloat(field_148936_d);
		p_148840_1_.writeFloat(field_148937_e);
		p_148840_1_.writeBoolean(field_148935_f);
	}
}
