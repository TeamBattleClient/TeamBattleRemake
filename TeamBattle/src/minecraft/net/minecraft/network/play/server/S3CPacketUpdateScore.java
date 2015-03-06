package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;

public class S3CPacketUpdateScore extends Packet {
	private int field_149326_d;
	private String field_149327_b = "";
	private int field_149328_c;
	private String field_149329_a = "";

	public S3CPacketUpdateScore() {
	}

	public S3CPacketUpdateScore(Score p_i45227_1_, int p_i45227_2_) {
		field_149329_a = p_i45227_1_.getPlayerName();
		field_149327_b = p_i45227_1_.func_96645_d().getName();
		field_149328_c = p_i45227_1_.getScorePoints();
		field_149326_d = p_i45227_2_;
	}

	public S3CPacketUpdateScore(String p_i45228_1_) {
		field_149329_a = p_i45228_1_;
		field_149327_b = "";
		field_149328_c = 0;
		field_149326_d = 1;
	}

	public String func_149321_d() {
		return field_149327_b;
	}

	public int func_149322_f() {
		return field_149326_d;
	}

	public int func_149323_e() {
		return field_149328_c;
	}

	public String func_149324_c() {
		return field_149329_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleUpdateScore(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149329_a = p_148837_1_.readStringFromBuffer(16);
		field_149326_d = p_148837_1_.readByte();

		if (field_149326_d != 1) {
			field_149327_b = p_148837_1_.readStringFromBuffer(16);
			field_149328_c = p_148837_1_.readInt();
		}
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149329_a);
		p_148840_1_.writeByte(field_149326_d);

		if (field_149326_d != 1) {
			p_148840_1_.writeStringToBuffer(field_149327_b);
			p_148840_1_.writeInt(field_149328_c);
		}
	}
}
