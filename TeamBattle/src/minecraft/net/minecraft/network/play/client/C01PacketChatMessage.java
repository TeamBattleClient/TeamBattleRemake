package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C01PacketChatMessage extends Packet {
	private String field_149440_a;

	public C01PacketChatMessage() {
	}

	public C01PacketChatMessage(String p_i45240_1_) {
		if (p_i45240_1_.length() > 100) {
			p_i45240_1_ = p_i45240_1_.substring(0, 100);
		}

		field_149440_a = p_i45240_1_;
	}

	public String func_149439_c() {
		return field_149440_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processChatMessage(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149440_a = p_148837_1_.readStringFromBuffer(100);
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format("message=\'%s\'", new Object[] { field_149440_a });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149440_a);
	}
}
