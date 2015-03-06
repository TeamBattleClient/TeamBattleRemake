package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.World;

public class C02PacketUseEntity extends Packet {
	public static enum Action {
		ATTACK("ATTACK", 1, 1), INTERACT("INTERACT", 0, 0);
		private static final C02PacketUseEntity.Action[] field_151421_c = new C02PacketUseEntity.Action[values().length];
		static {
			final C02PacketUseEntity.Action[] var0 = values();
			final int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				final C02PacketUseEntity.Action var3 = var0[var2];
				field_151421_c[var3.field_151418_d] = var3;
			}
		}

		private final int field_151418_d;

		private Action(String p_i45250_1_, int p_i45250_2_, int p_i45250_3_) {
			field_151418_d = p_i45250_3_;
		}
	}

	private C02PacketUseEntity.Action field_149566_b;

	private int field_149567_a;

	public C02PacketUseEntity() {
	}

	public C02PacketUseEntity(Entity p_i45251_1_,
			C02PacketUseEntity.Action p_i45251_2_) {
		field_149567_a = p_i45251_1_.getEntityId();
		field_149566_b = p_i45251_2_;
	}

	public Entity func_149564_a(World p_149564_1_) {
		return p_149564_1_.getEntityByID(field_149567_a);
	}

	public C02PacketUseEntity.Action func_149565_c() {
		return field_149566_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processUseEntity(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149567_a = p_148837_1_.readInt();
		field_149566_b = C02PacketUseEntity.Action.field_151421_c[p_148837_1_
				.readByte() % C02PacketUseEntity.Action.field_151421_c.length];
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149567_a);
		p_148840_1_.writeByte(field_149566_b.field_151418_d);
	}
}
