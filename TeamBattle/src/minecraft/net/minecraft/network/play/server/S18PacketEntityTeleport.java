package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S18PacketEntityTeleport extends Packet {
	private byte field_149453_f;
	private int field_149454_d;
	private byte field_149455_e;
	private int field_149456_b;
	private int field_149457_c;
	private int field_149458_a;

	public S18PacketEntityTeleport() {
	}

	public S18PacketEntityTeleport(Entity p_i45233_1_) {
		field_149458_a = p_i45233_1_.getEntityId();
		field_149456_b = MathHelper.floor_double(p_i45233_1_.posX * 32.0D);
		field_149457_c = MathHelper.floor_double(p_i45233_1_.posY * 32.0D);
		field_149454_d = MathHelper.floor_double(p_i45233_1_.posZ * 32.0D);
		field_149455_e = (byte) (int) (p_i45233_1_.rotationYaw * 256.0F / 360.0F);
		field_149453_f = (byte) (int) (p_i45233_1_.rotationPitch * 256.0F / 360.0F);
	}

	public S18PacketEntityTeleport(int p_i45234_1_, int p_i45234_2_,
			int p_i45234_3_, int p_i45234_4_, byte p_i45234_5_, byte p_i45234_6_) {
		field_149458_a = p_i45234_1_;
		field_149456_b = p_i45234_2_;
		field_149457_c = p_i45234_3_;
		field_149454_d = p_i45234_4_;
		field_149455_e = p_i45234_5_;
		field_149453_f = p_i45234_6_;
	}

	public int func_149446_f() {
		return field_149454_d;
	}

	public byte func_149447_h() {
		return field_149453_f;
	}

	public int func_149448_e() {
		return field_149457_c;
	}

	public int func_149449_d() {
		return field_149456_b;
	}

	public byte func_149450_g() {
		return field_149455_e;
	}

	public int func_149451_c() {
		return field_149458_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityTeleport(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149458_a = p_148837_1_.readInt();
		field_149456_b = p_148837_1_.readInt();
		field_149457_c = p_148837_1_.readInt();
		field_149454_d = p_148837_1_.readInt();
		field_149455_e = p_148837_1_.readByte();
		field_149453_f = p_148837_1_.readByte();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149458_a);
		p_148840_1_.writeInt(field_149456_b);
		p_148840_1_.writeInt(field_149457_c);
		p_148840_1_.writeInt(field_149454_d);
		p_148840_1_.writeByte(field_149455_e);
		p_148840_1_.writeByte(field_149453_f);
	}
}
