package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S11PacketSpawnExperienceOrb extends Packet {
	private int field_148988_d;
	private int field_148989_e;
	private int field_148990_b;
	private int field_148991_c;
	private int field_148992_a;

	public S11PacketSpawnExperienceOrb() {
	}

	public S11PacketSpawnExperienceOrb(EntityXPOrb p_i45167_1_) {
		field_148992_a = p_i45167_1_.getEntityId();
		field_148990_b = MathHelper.floor_double(p_i45167_1_.posX * 32.0D);
		field_148991_c = MathHelper.floor_double(p_i45167_1_.posY * 32.0D);
		field_148988_d = MathHelper.floor_double(p_i45167_1_.posZ * 32.0D);
		field_148989_e = p_i45167_1_.getXpValue();
	}

	public int func_148982_f() {
		return field_148988_d;
	}

	public int func_148983_e() {
		return field_148991_c;
	}

	public int func_148984_d() {
		return field_148990_b;
	}

	public int func_148985_c() {
		return field_148992_a;
	}

	public int func_148986_g() {
		return field_148989_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnExperienceOrb(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148992_a = p_148837_1_.readVarIntFromBuffer();
		field_148990_b = p_148837_1_.readInt();
		field_148991_c = p_148837_1_.readInt();
		field_148988_d = p_148837_1_.readInt();
		field_148989_e = p_148837_1_.readShort();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format(
				"id=%d, value=%d, x=%.2f, y=%.2f, z=%.2f",
				new Object[] { Integer.valueOf(field_148992_a),
						Integer.valueOf(field_148989_e),
						Float.valueOf(field_148990_b / 32.0F),
						Float.valueOf(field_148991_c / 32.0F),
						Float.valueOf(field_148988_d / 32.0F) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148992_a);
		p_148840_1_.writeInt(field_148990_b);
		p_148840_1_.writeInt(field_148991_c);
		p_148840_1_.writeInt(field_148988_d);
		p_148840_1_.writeShort(field_148989_e);
	}
}
