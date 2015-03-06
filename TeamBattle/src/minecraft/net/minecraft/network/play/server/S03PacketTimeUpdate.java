package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S03PacketTimeUpdate extends Packet {
	private long field_149368_b;
	private long field_149369_a;

	public S03PacketTimeUpdate() {
	}

	public S03PacketTimeUpdate(long p_i45230_1_, long p_i45230_3_,
			boolean p_i45230_5_) {
		field_149369_a = p_i45230_1_;
		field_149368_b = p_i45230_3_;

		if (!p_i45230_5_) {
			field_149368_b = -field_149368_b;

			if (field_149368_b == 0L) {
				field_149368_b = -1L;
			}
		}
	}

	public long func_149365_d() {
		return field_149368_b;
	}

	public long func_149366_c() {
		return field_149369_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleTimeUpdate(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149369_a = p_148837_1_.readLong();
		field_149368_b = p_148837_1_.readLong();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format(
				"time=%d,dtime=%d",
				new Object[] { Long.valueOf(field_149369_a),
						Long.valueOf(field_149368_b) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeLong(field_149369_a);
		p_148840_1_.writeLong(field_149368_b);
	}
}
