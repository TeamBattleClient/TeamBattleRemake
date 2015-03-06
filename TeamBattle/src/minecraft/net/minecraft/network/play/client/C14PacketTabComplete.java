package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import org.apache.commons.lang3.StringUtils;

public class C14PacketTabComplete extends Packet {
	private String field_149420_a;

	public C14PacketTabComplete() {
	}

	public C14PacketTabComplete(String p_i45239_1_) {
		field_149420_a = p_i45239_1_;
	}

	public String func_149419_c() {
		return field_149420_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processTabComplete(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149420_a = p_148837_1_.readStringFromBuffer(32767);
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format("message=\'%s\'", new Object[] { field_149420_a });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(StringUtils.substring(field_149420_a,
				0, 32767));
	}
}
