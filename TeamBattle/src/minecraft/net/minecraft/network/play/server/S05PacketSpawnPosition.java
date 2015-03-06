package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S05PacketSpawnPosition extends Packet {
	private int field_149362_b;
	private int field_149363_c;
	private int field_149364_a;

	public S05PacketSpawnPosition() {
	}

	public S05PacketSpawnPosition(int p_i45229_1_, int p_i45229_2_,
			int p_i45229_3_) {
		field_149364_a = p_i45229_1_;
		field_149362_b = p_i45229_2_;
		field_149363_c = p_i45229_3_;
	}

	public int func_149358_e() {
		return field_149363_c;
	}

	public int func_149359_d() {
		return field_149362_b;
	}

	public int func_149360_c() {
		return field_149364_a;
	}

	/**
	 * If true, the network manager will process the packet immediately when
	 * received, otherwise it will queue it for processing. Currently true for:
	 * Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
	 */
	@Override
	public boolean hasPriority() {
		return false;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnPosition(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149364_a = p_148837_1_.readInt();
		field_149362_b = p_148837_1_.readInt();
		field_149363_c = p_148837_1_.readInt();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format(
				"x=%d, y=%d, z=%d",
				new Object[] { Integer.valueOf(field_149364_a),
						Integer.valueOf(field_149362_b),
						Integer.valueOf(field_149363_c) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149364_a);
		p_148840_1_.writeInt(field_149362_b);
		p_148840_1_.writeInt(field_149363_c);
	}
}
