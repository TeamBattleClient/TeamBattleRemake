package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;

public class S27PacketExplosion extends Packet {
	private float field_149152_f;
	private float field_149153_g;
	private float field_149154_d;
	private List field_149155_e;
	private double field_149156_b;
	private double field_149157_c;
	private double field_149158_a;
	private float field_149159_h;

	public S27PacketExplosion() {
	}

	public S27PacketExplosion(double p_i45193_1_, double p_i45193_3_,
			double p_i45193_5_, float p_i45193_7_, List p_i45193_8_,
			Vec3 p_i45193_9_) {
		field_149158_a = p_i45193_1_;
		field_149156_b = p_i45193_3_;
		field_149157_c = p_i45193_5_;
		field_149154_d = p_i45193_7_;
		field_149155_e = new ArrayList(p_i45193_8_);

		if (p_i45193_9_ != null) {
			field_149152_f = (float) p_i45193_9_.xCoord;
			field_149153_g = (float) p_i45193_9_.yCoord;
			field_149159_h = (float) p_i45193_9_.zCoord;
		}
	}

	public double func_149143_g() {
		return field_149156_b;
	}

	public float func_149144_d() {
		return field_149153_g;
	}

	public double func_149145_h() {
		return field_149157_c;
	}

	public float func_149146_i() {
		return field_149154_d;
	}

	public float func_149147_e() {
		return field_149159_h;
	}

	public double func_149148_f() {
		return field_149158_a;
	}

	public float func_149149_c() {
		return field_149152_f;
	}

	public List func_149150_j() {
		return field_149155_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleExplosion(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149158_a = p_148837_1_.readFloat();
		field_149156_b = p_148837_1_.readFloat();
		field_149157_c = p_148837_1_.readFloat();
		field_149154_d = p_148837_1_.readFloat();
		final int var2 = p_148837_1_.readInt();
		field_149155_e = new ArrayList(var2);
		final int var3 = (int) field_149158_a;
		final int var4 = (int) field_149156_b;
		final int var5 = (int) field_149157_c;

		for (int var6 = 0; var6 < var2; ++var6) {
			final int var7 = p_148837_1_.readByte() + var3;
			final int var8 = p_148837_1_.readByte() + var4;
			final int var9 = p_148837_1_.readByte() + var5;
			field_149155_e.add(new ChunkPosition(var7, var8, var9));
		}

		field_149152_f = p_148837_1_.readFloat();
		field_149153_g = p_148837_1_.readFloat();
		field_149159_h = p_148837_1_.readFloat();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeFloat((float) field_149158_a);
		p_148840_1_.writeFloat((float) field_149156_b);
		p_148840_1_.writeFloat((float) field_149157_c);
		p_148840_1_.writeFloat(field_149154_d);
		p_148840_1_.writeInt(field_149155_e.size());
		final int var2 = (int) field_149158_a;
		final int var3 = (int) field_149156_b;
		final int var4 = (int) field_149157_c;
		final Iterator var5 = field_149155_e.iterator();

		while (var5.hasNext()) {
			final ChunkPosition var6 = (ChunkPosition) var5.next();
			final int var7 = var6.field_151329_a - var2;
			final int var8 = var6.field_151327_b - var3;
			final int var9 = var6.field_151328_c - var4;
			p_148840_1_.writeByte(var7);
			p_148840_1_.writeByte(var8);
			p_148840_1_.writeByte(var9);
		}

		p_148840_1_.writeFloat(field_149152_f);
		p_148840_1_.writeFloat(field_149153_g);
		p_148840_1_.writeFloat(field_149159_h);
	}
}
