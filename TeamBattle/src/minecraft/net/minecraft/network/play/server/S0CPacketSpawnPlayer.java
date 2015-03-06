package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class S0CPacketSpawnPlayer extends Packet {
	private byte field_148951_f;
	private byte field_148952_g;
	private int field_148953_d;
	private int field_148954_e;
	private GameProfile field_148955_b;
	private int field_148956_c;
	private int field_148957_a;
	private List field_148958_j;
	private int field_148959_h;
	private DataWatcher field_148960_i;

	public S0CPacketSpawnPlayer() {
	}

	public S0CPacketSpawnPlayer(EntityPlayer p_i45171_1_) {
		field_148957_a = p_i45171_1_.getEntityId();
		field_148955_b = p_i45171_1_.getGameProfile();
		field_148956_c = MathHelper.floor_double(p_i45171_1_.posX * 32.0D);
		field_148953_d = MathHelper.floor_double(p_i45171_1_.posY * 32.0D);
		field_148954_e = MathHelper.floor_double(p_i45171_1_.posZ * 32.0D);
		field_148951_f = (byte) (int) (p_i45171_1_.rotationYaw * 256.0F / 360.0F);
		field_148952_g = (byte) (int) (p_i45171_1_.rotationPitch * 256.0F / 360.0F);
		final ItemStack var2 = p_i45171_1_.inventory.getCurrentItem();
		field_148959_h = var2 == null ? 0 : Item.getIdFromItem(var2.getItem());
		field_148960_i = p_i45171_1_.getDataWatcher();
	}

	public byte func_148941_i() {
		return field_148951_f;
	}

	public int func_148942_f() {
		return field_148956_c;
	}

	public int func_148943_d() {
		return field_148957_a;
	}

	public List func_148944_c() {
		if (field_148958_j == null) {
			field_148958_j = field_148960_i.getAllWatched();
		}

		return field_148958_j;
	}

	public byte func_148945_j() {
		return field_148952_g;
	}

	public int func_148946_h() {
		return field_148954_e;
	}

	public int func_148947_k() {
		return field_148959_h;
	}

	public GameProfile func_148948_e() {
		return field_148955_b;
	}

	public int func_148949_g() {
		return field_148953_d;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnPlayer(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148957_a = p_148837_1_.readVarIntFromBuffer();
		final UUID var2 = UUID.fromString(p_148837_1_.readStringFromBuffer(36));
		field_148955_b = new GameProfile(var2,
				p_148837_1_.readStringFromBuffer(16));
		final int var3 = p_148837_1_.readVarIntFromBuffer();

		for (int var4 = 0; var4 < var3; ++var4) {
			final String var5 = p_148837_1_.readStringFromBuffer(32767);
			final String var6 = p_148837_1_.readStringFromBuffer(32767);
			final String var7 = p_148837_1_.readStringFromBuffer(32767);
			field_148955_b.getProperties().put(var5,
					new Property(var5, var6, var7));
		}

		field_148956_c = p_148837_1_.readInt();
		field_148953_d = p_148837_1_.readInt();
		field_148954_e = p_148837_1_.readInt();
		field_148951_f = p_148837_1_.readByte();
		field_148952_g = p_148837_1_.readByte();
		field_148959_h = p_148837_1_.readShort();
		field_148958_j = DataWatcher
				.readWatchedListFromPacketBuffer(p_148837_1_);
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String
				.format("id=%d, gameProfile=\'%s\', x=%.2f, y=%.2f, z=%.2f, carried=%d",
						new Object[] { Integer.valueOf(field_148957_a),
								field_148955_b,
								Float.valueOf(field_148956_c / 32.0F),
								Float.valueOf(field_148953_d / 32.0F),
								Float.valueOf(field_148954_e / 32.0F),
								Integer.valueOf(field_148959_h) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148957_a);
		final UUID var2 = field_148955_b.getId();
		p_148840_1_.writeStringToBuffer(var2 == null ? "" : var2.toString());
		p_148840_1_.writeStringToBuffer(field_148955_b.getName());
		p_148840_1_.writeVarIntToBuffer(field_148955_b.getProperties().size());
		final Iterator var3 = field_148955_b.getProperties().values()
				.iterator();

		while (var3.hasNext()) {
			final Property var4 = (Property) var3.next();
			p_148840_1_.writeStringToBuffer(var4.getName());
			p_148840_1_.writeStringToBuffer(var4.getValue());
			p_148840_1_.writeStringToBuffer(var4.getSignature());
		}

		p_148840_1_.writeInt(field_148956_c);
		p_148840_1_.writeInt(field_148953_d);
		p_148840_1_.writeInt(field_148954_e);
		p_148840_1_.writeByte(field_148951_f);
		p_148840_1_.writeByte(field_148952_g);
		p_148840_1_.writeShort(field_148959_h);
		field_148960_i.func_151509_a(p_148840_1_);
	}
}
