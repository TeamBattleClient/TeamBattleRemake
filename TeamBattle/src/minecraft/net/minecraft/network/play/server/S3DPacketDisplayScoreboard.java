package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class S3DPacketDisplayScoreboard extends Packet {
	private String field_149373_b;
	private int field_149374_a;

	public S3DPacketDisplayScoreboard() {
	}

	public S3DPacketDisplayScoreboard(int p_i45216_1_,
			ScoreObjective p_i45216_2_) {
		field_149374_a = p_i45216_1_;

		if (p_i45216_2_ == null) {
			field_149373_b = "";
		} else {
			field_149373_b = p_i45216_2_.getName();
		}
	}

	public String func_149370_d() {
		return field_149373_b;
	}

	public int func_149371_c() {
		return field_149374_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleDisplayScoreboard(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149374_a = p_148837_1_.readByte();
		field_149373_b = p_148837_1_.readStringFromBuffer(16);
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149374_a);
		p_148840_1_.writeStringToBuffer(field_149373_b);
	}
}
