package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class S01PacketJoinGame extends Packet {
	private int field_149200_f;
	private WorldType field_149201_g;
	private int field_149202_d;
	private EnumDifficulty field_149203_e;
	private boolean field_149204_b;
	private WorldSettings.GameType field_149205_c;
	private int field_149206_a;

	public S01PacketJoinGame() {
	}

	public S01PacketJoinGame(int p_i45201_1_,
			WorldSettings.GameType p_i45201_2_, boolean p_i45201_3_,
			int p_i45201_4_, EnumDifficulty p_i45201_5_, int p_i45201_6_,
			WorldType p_i45201_7_) {
		field_149206_a = p_i45201_1_;
		field_149202_d = p_i45201_4_;
		field_149203_e = p_i45201_5_;
		field_149205_c = p_i45201_2_;
		field_149200_f = p_i45201_6_;
		field_149204_b = p_i45201_3_;
		field_149201_g = p_i45201_7_;
	}

	public EnumDifficulty func_149192_g() {
		return field_149203_e;
	}

	public int func_149193_h() {
		return field_149200_f;
	}

	public int func_149194_f() {
		return field_149202_d;
	}

	public boolean func_149195_d() {
		return field_149204_b;
	}

	public WorldType func_149196_i() {
		return field_149201_g;
	}

	public int func_149197_c() {
		return field_149206_a;
	}

	public WorldSettings.GameType func_149198_e() {
		return field_149205_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleJoinGame(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149206_a = p_148837_1_.readInt();
		final short var2 = p_148837_1_.readUnsignedByte();
		field_149204_b = (var2 & 8) == 8;
		final int var3 = var2 & -9;
		field_149205_c = WorldSettings.GameType.getByID(var3);
		field_149202_d = p_148837_1_.readByte();
		field_149203_e = EnumDifficulty.getDifficultyEnum(p_148837_1_
				.readUnsignedByte());
		field_149200_f = p_148837_1_.readUnsignedByte();
		field_149201_g = WorldType.parseWorldType(p_148837_1_
				.readStringFromBuffer(16));

		if (field_149201_g == null) {
			field_149201_g = WorldType.DEFAULT;
		}
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String
				.format("eid=%d, gameType=%d, hardcore=%b, dimension=%d, difficulty=%s, maxplayers=%d",
						new Object[] { Integer.valueOf(field_149206_a),
								Integer.valueOf(field_149205_c.getID()),
								Boolean.valueOf(field_149204_b),
								Integer.valueOf(field_149202_d),
								field_149203_e, Integer.valueOf(field_149200_f) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149206_a);
		int var2 = field_149205_c.getID();

		if (field_149204_b) {
			var2 |= 8;
		}

		p_148840_1_.writeByte(var2);
		p_148840_1_.writeByte(field_149202_d);
		p_148840_1_.writeByte(field_149203_e.getDifficultyId());
		p_148840_1_.writeByte(field_149200_f);
		p_148840_1_.writeStringToBuffer(field_149201_g.getWorldTypeName());
	}
}
