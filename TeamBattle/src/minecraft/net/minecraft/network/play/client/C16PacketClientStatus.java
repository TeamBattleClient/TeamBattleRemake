package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus extends Packet {
	public static enum EnumState {
		OPEN_INVENTORY_ACHIEVEMENT("OPEN_INVENTORY_ACHIEVEMENT", 2, 2), PERFORM_RESPAWN(
				"PERFORM_RESPAWN", 0, 0), REQUEST_STATS("REQUEST_STATS", 1, 1);
		private static final C16PacketClientStatus.EnumState[] field_151404_e = new C16PacketClientStatus.EnumState[values().length];
		static {
			final C16PacketClientStatus.EnumState[] var0 = values();
			final int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				final C16PacketClientStatus.EnumState var3 = var0[var2];
				field_151404_e[var3.field_151403_d] = var3;
			}
		}

		private final int field_151403_d;

		private EnumState(String p_i45241_1_, int p_i45241_2_, int p_i45241_3_) {
			field_151403_d = p_i45241_3_;
		}
	}

	private C16PacketClientStatus.EnumState field_149437_a;

	public C16PacketClientStatus() {
	}

	public C16PacketClientStatus(C16PacketClientStatus.EnumState p_i45242_1_) {
		field_149437_a = p_i45242_1_;
	}

	public C16PacketClientStatus.EnumState func_149435_c() {
		return field_149437_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processClientStatus(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149437_a = C16PacketClientStatus.EnumState.field_151404_e[p_148837_1_
				.readByte()
				% C16PacketClientStatus.EnumState.field_151404_e.length];
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149437_a.field_151403_d);
	}
}
