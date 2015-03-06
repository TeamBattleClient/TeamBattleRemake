package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S33PacketUpdateSign extends Packet {
	private String[] field_149349_d;
	private int field_149350_b;
	private int field_149351_c;
	private int field_149352_a;

	public S33PacketUpdateSign() {
	}

	public S33PacketUpdateSign(int p_i45231_1_, int p_i45231_2_,
			int p_i45231_3_, String[] p_i45231_4_) {
		field_149352_a = p_i45231_1_;
		field_149350_b = p_i45231_2_;
		field_149351_c = p_i45231_3_;
		field_149349_d = new String[] { p_i45231_4_[0], p_i45231_4_[1],
				p_i45231_4_[2], p_i45231_4_[3] };
	}

	public int func_149344_e() {
		return field_149351_c;
	}

	public int func_149345_d() {
		return field_149350_b;
	}

	public int func_149346_c() {
		return field_149352_a;
	}

	public String[] func_149347_f() {
		return field_149349_d;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleUpdateSign(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149352_a = p_148837_1_.readInt();
		field_149350_b = p_148837_1_.readShort();
		field_149351_c = p_148837_1_.readInt();
		field_149349_d = new String[4];

		for (int var2 = 0; var2 < 4; ++var2) {
			field_149349_d[var2] = p_148837_1_.readStringFromBuffer(15);
		}
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149352_a);
		p_148840_1_.writeShort(field_149350_b);
		p_148840_1_.writeInt(field_149351_c);

		for (int var2 = 0; var2 < 4; ++var2) {
			p_148840_1_.writeStringToBuffer(field_149349_d[var2]);
		}
	}
}
