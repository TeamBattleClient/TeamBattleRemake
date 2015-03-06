package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S13PacketDestroyEntities extends Packet {
	private int[] field_149100_a;

	public S13PacketDestroyEntities() {
	}

	public S13PacketDestroyEntities(int... p_i45211_1_) {
		field_149100_a = p_i45211_1_;
	}

	public int[] func_149098_c() {
		return field_149100_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleDestroyEntities(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149100_a = new int[p_148837_1_.readByte()];

		for (int var2 = 0; var2 < field_149100_a.length; ++var2) {
			field_149100_a[var2] = p_148837_1_.readInt();
		}
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		final StringBuilder var1 = new StringBuilder();

		for (int var2 = 0; var2 < field_149100_a.length; ++var2) {
			if (var2 > 0) {
				var1.append(", ");
			}

			var1.append(field_149100_a[var2]);
		}

		return String.format("entities=%d[%s]",
				new Object[] { Integer.valueOf(field_149100_a.length), var1 });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149100_a.length);

		for (final int element : field_149100_a) {
			p_148840_1_.writeInt(element);
		}
	}
}
