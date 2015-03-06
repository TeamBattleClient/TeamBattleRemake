package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C07PacketPlayerDigging extends Packet {
	private int field_149507_d;
	private int field_149508_e;
	private int field_149509_b;
	private int field_149510_c;
	private int field_149511_a;

	public C07PacketPlayerDigging() {
	}

	public C07PacketPlayerDigging(int p_i45258_1_, int p_i45258_2_,
			int p_i45258_3_, int p_i45258_4_, int p_i45258_5_) {
		field_149508_e = p_i45258_1_;
		field_149511_a = p_i45258_2_;
		field_149509_b = p_i45258_3_;
		field_149510_c = p_i45258_4_;
		field_149507_d = p_i45258_5_;
	}

	public int func_149501_f() {
		return field_149507_d;
	}

	public int func_149502_e() {
		return field_149510_c;
	}

	public int func_149503_d() {
		return field_149509_b;
	}

	public int func_149505_c() {
		return field_149511_a;
	}

	public int func_149506_g() {
		return field_149508_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processPlayerDigging(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149508_e = p_148837_1_.readUnsignedByte();
		field_149511_a = p_148837_1_.readInt();
		field_149509_b = p_148837_1_.readUnsignedByte();
		field_149510_c = p_148837_1_.readInt();
		field_149507_d = p_148837_1_.readUnsignedByte();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149508_e);
		p_148840_1_.writeInt(field_149511_a);
		p_148840_1_.writeByte(field_149509_b);
		p_148840_1_.writeInt(field_149510_c);
		p_148840_1_.writeByte(field_149507_d);
	}
}
