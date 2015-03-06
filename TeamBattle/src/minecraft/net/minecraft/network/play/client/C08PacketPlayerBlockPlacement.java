package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C08PacketPlayerBlockPlacement extends Packet {
	private float field_149577_f;
	private float field_149578_g;
	private int field_149579_d;
	private ItemStack field_149580_e;
	private int field_149581_b;
	private int field_149582_c;
	private int field_149583_a;
	private float field_149584_h;

	public C08PacketPlayerBlockPlacement() {
	}

	public C08PacketPlayerBlockPlacement(int p_i45265_1_, int p_i45265_2_,
			int p_i45265_3_, int p_i45265_4_, ItemStack p_i45265_5_,
			float p_i45265_6_, float p_i45265_7_, float p_i45265_8_) {
		field_149583_a = p_i45265_1_;
		field_149581_b = p_i45265_2_;
		field_149582_c = p_i45265_3_;
		field_149579_d = p_i45265_4_;
		field_149580_e = p_i45265_5_ != null ? p_i45265_5_.copy() : null;
		field_149577_f = p_i45265_6_;
		field_149578_g = p_i45265_7_;
		field_149584_h = p_i45265_8_;
	}

	public int func_149568_f() {
		return field_149579_d;
	}

	public float func_149569_i() {
		return field_149578_g;
	}

	public int func_149570_e() {
		return field_149582_c;
	}

	public int func_149571_d() {
		return field_149581_b;
	}

	public float func_149573_h() {
		return field_149577_f;
	}

	public ItemStack func_149574_g() {
		return field_149580_e;
	}

	public float func_149575_j() {
		return field_149584_h;
	}

	public int func_149576_c() {
		return field_149583_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processPlayerBlockPlacement(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149583_a = p_148837_1_.readInt();
		field_149581_b = p_148837_1_.readUnsignedByte();
		field_149582_c = p_148837_1_.readInt();
		field_149579_d = p_148837_1_.readUnsignedByte();
		field_149580_e = p_148837_1_.readItemStackFromBuffer();
		field_149577_f = p_148837_1_.readUnsignedByte() / 16.0F;
		field_149578_g = p_148837_1_.readUnsignedByte() / 16.0F;
		field_149584_h = p_148837_1_.readUnsignedByte() / 16.0F;
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149583_a);
		p_148840_1_.writeByte(field_149581_b);
		p_148840_1_.writeInt(field_149582_c);
		p_148840_1_.writeByte(field_149579_d);
		p_148840_1_.writeItemStackToBuffer(field_149580_e);
		p_148840_1_.writeByte((int) (field_149577_f * 16.0F));
		p_148840_1_.writeByte((int) (field_149578_g * 16.0F));
		p_148840_1_.writeByte((int) (field_149584_h * 16.0F));
	}
}
