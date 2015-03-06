package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0FPacketSpawnMob extends Packet {
	private int field_149036_f;
	private int field_149037_g;
	private int field_149038_d;
	private int field_149039_e;
	private int field_149040_b;
	private int field_149041_c;
	private int field_149042_a;
	private DataWatcher field_149043_l;
	private List field_149044_m;
	private byte field_149045_j;
	private byte field_149046_k;
	private int field_149047_h;
	private byte field_149048_i;

	public S0FPacketSpawnMob() {
	}

	public S0FPacketSpawnMob(EntityLivingBase p_i45192_1_) {
		field_149042_a = p_i45192_1_.getEntityId();
		field_149040_b = (byte) EntityList.getEntityID(p_i45192_1_);
		field_149041_c = p_i45192_1_.myEntitySize
				.multiplyBy32AndRound(p_i45192_1_.posX);
		field_149038_d = MathHelper.floor_double(p_i45192_1_.posY * 32.0D);
		field_149039_e = p_i45192_1_.myEntitySize
				.multiplyBy32AndRound(p_i45192_1_.posZ);
		field_149048_i = (byte) (int) (p_i45192_1_.rotationYaw * 256.0F / 360.0F);
		field_149045_j = (byte) (int) (p_i45192_1_.rotationPitch * 256.0F / 360.0F);
		field_149046_k = (byte) (int) (p_i45192_1_.rotationYawHead * 256.0F / 360.0F);
		final double var2 = 3.9D;
		double var4 = p_i45192_1_.motionX;
		double var6 = p_i45192_1_.motionY;
		double var8 = p_i45192_1_.motionZ;

		if (var4 < -var2) {
			var4 = -var2;
		}

		if (var6 < -var2) {
			var6 = -var2;
		}

		if (var8 < -var2) {
			var8 = -var2;
		}

		if (var4 > var2) {
			var4 = var2;
		}

		if (var6 > var2) {
			var6 = var2;
		}

		if (var8 > var2) {
			var8 = var2;
		}

		field_149036_f = (int) (var4 * 8000.0D);
		field_149037_g = (int) (var6 * 8000.0D);
		field_149047_h = (int) (var8 * 8000.0D);
		field_149043_l = p_i45192_1_.getDataWatcher();
	}

	public int func_149023_f() {
		return field_149041_c;
	}

	public int func_149024_d() {
		return field_149042_a;
	}

	public int func_149025_e() {
		return field_149040_b;
	}

	public int func_149026_i() {
		return field_149036_f;
	}

	public List func_149027_c() {
		if (field_149044_m == null) {
			field_149044_m = field_149043_l.getAllWatched();
		}

		return field_149044_m;
	}

	public byte func_149028_l() {
		return field_149048_i;
	}

	public int func_149029_h() {
		return field_149039_e;
	}

	public byte func_149030_m() {
		return field_149045_j;
	}

	public int func_149031_k() {
		return field_149047_h;
	}

	public byte func_149032_n() {
		return field_149046_k;
	}

	public int func_149033_j() {
		return field_149037_g;
	}

	public int func_149034_g() {
		return field_149038_d;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnMob(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149042_a = p_148837_1_.readVarIntFromBuffer();
		field_149040_b = p_148837_1_.readByte() & 255;
		field_149041_c = p_148837_1_.readInt();
		field_149038_d = p_148837_1_.readInt();
		field_149039_e = p_148837_1_.readInt();
		field_149048_i = p_148837_1_.readByte();
		field_149045_j = p_148837_1_.readByte();
		field_149046_k = p_148837_1_.readByte();
		field_149036_f = p_148837_1_.readShort();
		field_149037_g = p_148837_1_.readShort();
		field_149047_h = p_148837_1_.readShort();
		field_149044_m = DataWatcher
				.readWatchedListFromPacketBuffer(p_148837_1_);
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String
				.format("id=%d, type=%d, x=%.2f, y=%.2f, z=%.2f, xd=%.2f, yd=%.2f, zd=%.2f",
						new Object[] { Integer.valueOf(field_149042_a),
								Integer.valueOf(field_149040_b),
								Float.valueOf(field_149041_c / 32.0F),
								Float.valueOf(field_149038_d / 32.0F),
								Float.valueOf(field_149039_e / 32.0F),
								Float.valueOf(field_149036_f / 8000.0F),
								Float.valueOf(field_149037_g / 8000.0F),
								Float.valueOf(field_149047_h / 8000.0F) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_149042_a);
		p_148840_1_.writeByte(field_149040_b & 255);
		p_148840_1_.writeInt(field_149041_c);
		p_148840_1_.writeInt(field_149038_d);
		p_148840_1_.writeInt(field_149039_e);
		p_148840_1_.writeByte(field_149048_i);
		p_148840_1_.writeByte(field_149045_j);
		p_148840_1_.writeByte(field_149046_k);
		p_148840_1_.writeShort(field_149036_f);
		p_148840_1_.writeShort(field_149037_g);
		p_148840_1_.writeShort(field_149047_h);
		field_149043_l.func_151509_a(p_148840_1_);
	}
}
