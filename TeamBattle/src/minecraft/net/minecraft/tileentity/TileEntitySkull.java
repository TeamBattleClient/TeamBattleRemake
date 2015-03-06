package net.minecraft.tileentity;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class TileEntitySkull extends TileEntity {
	private int field_145908_a;
	private int field_145910_i;
	private GameProfile field_152110_j = null;

	public void func_145903_a(int p_145903_1_) {
		field_145910_i = p_145903_1_;
	}

	public int func_145904_a() {
		return field_145908_a;
	}

	public int func_145906_b() {
		return field_145910_i;
	}

	public void func_152106_a(GameProfile p_152106_1_) {
		field_145908_a = 3;
		field_152110_j = p_152106_1_;
		func_152109_d();
	}

	public void func_152107_a(int p_152107_1_) {
		field_145908_a = p_152107_1_;
		field_152110_j = null;
	}

	public GameProfile func_152108_a() {
		return field_152110_j;
	}

	private void func_152109_d() {
		if (field_152110_j != null
				&& !StringUtils.isNullOrEmpty(field_152110_j.getName())) {
			if (!field_152110_j.isComplete()
					|| !field_152110_j.getProperties().containsKey("textures")) {
				GameProfile var1 = MinecraftServer.getServer().func_152358_ax()
						.func_152655_a(field_152110_j.getName());

				if (var1 != null) {
					final Property var2 = (Property) Iterables.getFirst(var1
							.getProperties().get("textures"), (Object) null);

					if (var2 == null) {
						var1 = MinecraftServer.getServer().func_147130_as()
								.fillProfileProperties(var1, true);
					}

					field_152110_j = var1;
					onInventoryChanged();
				}
			}
		}
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new S35PacketUpdateTileEntity(field_145851_c, field_145848_d,
				field_145849_e, 4, var1);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145908_a = p_145839_1_.getByte("SkullType");
		field_145910_i = p_145839_1_.getByte("Rot");

		if (field_145908_a == 3) {
			if (p_145839_1_.func_150297_b("Owner", 10)) {
				field_152110_j = NBTUtil.func_152459_a(p_145839_1_
						.getCompoundTag("Owner"));
			} else if (p_145839_1_.func_150297_b("ExtraType", 8)
					&& !StringUtils.isNullOrEmpty(p_145839_1_
							.getString("ExtraType"))) {
				field_152110_j = new GameProfile((UUID) null,
						p_145839_1_.getString("ExtraType"));
				func_152109_d();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setByte("SkullType", (byte) (field_145908_a & 255));
		p_145841_1_.setByte("Rot", (byte) (field_145910_i & 255));

		if (field_152110_j != null) {
			final NBTTagCompound var2 = new NBTTagCompound();
			NBTUtil.func_152460_a(var2, field_152110_j);
			p_145841_1_.setTag("Owner", var2);
		}
	}
}
