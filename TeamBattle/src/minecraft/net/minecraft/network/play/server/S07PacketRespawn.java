package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class S07PacketRespawn extends Packet {
	private WorldType field_149085_d;
	private EnumDifficulty field_149086_b;
	private WorldSettings.GameType field_149087_c;
	private int field_149088_a;

	public S07PacketRespawn() {
	}

	public S07PacketRespawn(int p_i45213_1_, EnumDifficulty p_i45213_2_,
			WorldType p_i45213_3_, WorldSettings.GameType p_i45213_4_) {
		field_149088_a = p_i45213_1_;
		field_149086_b = p_i45213_2_;
		field_149087_c = p_i45213_4_;
		field_149085_d = p_i45213_3_;
	}

	public WorldType func_149080_f() {
		return field_149085_d;
	}

	public EnumDifficulty func_149081_d() {
		return field_149086_b;
	}

	public int func_149082_c() {
		return field_149088_a;
	}

	public WorldSettings.GameType func_149083_e() {
		return field_149087_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleRespawn(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149088_a = p_148837_1_.readInt();
		field_149086_b = EnumDifficulty.getDifficultyEnum(p_148837_1_
				.readUnsignedByte());
		field_149087_c = WorldSettings.GameType.getByID(p_148837_1_
				.readUnsignedByte());
		field_149085_d = WorldType.parseWorldType(p_148837_1_
				.readStringFromBuffer(16));

		if (field_149085_d == null) {
			field_149085_d = WorldType.DEFAULT;
		}
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149088_a);
		p_148840_1_.writeByte(field_149086_b.getDifficultyId());
		p_148840_1_.writeByte(field_149087_c.getID());
		p_148840_1_.writeStringToBuffer(field_149085_d.getWorldTypeName());
	}
}
