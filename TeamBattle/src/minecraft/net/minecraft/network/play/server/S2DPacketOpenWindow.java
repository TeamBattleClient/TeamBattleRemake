package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2DPacketOpenWindow extends Packet {
	private int field_148904_f;
	private int field_148905_d;
	private boolean field_148906_e;
	private int field_148907_b;
	private String field_148908_c;
	private int field_148909_a;

	public S2DPacketOpenWindow() {
	}

	public S2DPacketOpenWindow(int p_i45184_1_, int p_i45184_2_,
			String p_i45184_3_, int p_i45184_4_, boolean p_i45184_5_) {
		field_148909_a = p_i45184_1_;
		field_148907_b = p_i45184_2_;
		field_148908_c = p_i45184_3_;
		field_148905_d = p_i45184_4_;
		field_148906_e = p_i45184_5_;
	}

	public S2DPacketOpenWindow(int p_i45185_1_, int p_i45185_2_,
			String p_i45185_3_, int p_i45185_4_, boolean p_i45185_5_,
			int p_i45185_6_) {
		this(p_i45185_1_, p_i45185_2_, p_i45185_3_, p_i45185_4_, p_i45185_5_);
		field_148904_f = p_i45185_6_;
	}

	public int func_148897_h() {
		return field_148904_f;
	}

	public int func_148898_f() {
		return field_148905_d;
	}

	public int func_148899_d() {
		return field_148907_b;
	}

	public boolean func_148900_g() {
		return field_148906_e;
	}

	public int func_148901_c() {
		return field_148909_a;
	}

	public String func_148902_e() {
		return field_148908_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleOpenWindow(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148909_a = p_148837_1_.readUnsignedByte();
		field_148907_b = p_148837_1_.readUnsignedByte();
		field_148908_c = p_148837_1_.readStringFromBuffer(32);
		field_148905_d = p_148837_1_.readUnsignedByte();
		field_148906_e = p_148837_1_.readBoolean();

		if (field_148907_b == 11) {
			field_148904_f = p_148837_1_.readInt();
		}
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_148909_a);
		p_148840_1_.writeByte(field_148907_b);
		p_148840_1_.writeStringToBuffer(field_148908_c);
		p_148840_1_.writeByte(field_148905_d);
		p_148840_1_.writeBoolean(field_148906_e);

		if (field_148907_b == 11) {
			p_148840_1_.writeInt(field_148904_f);
		}
	}
}
