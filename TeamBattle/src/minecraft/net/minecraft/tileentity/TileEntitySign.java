package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;

public class TileEntitySign extends TileEntity {
	public String[] field_145915_a = new String[] { "", "", "", "" };
	private boolean field_145916_j = true;
	private EntityPlayer field_145917_k;
	public int field_145918_i = -1;

	public EntityPlayer func_145911_b() {
		return field_145917_k;
	}

	public void func_145912_a(EntityPlayer p_145912_1_) {
		field_145917_k = p_145912_1_;
	}

	public void func_145913_a(boolean p_145913_1_) {
		field_145916_j = p_145913_1_;

		if (!p_145913_1_) {
			field_145917_k = null;
		}
	}

	public boolean func_145914_a() {
		return field_145916_j;
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	@Override
	public Packet getDescriptionPacket() {
		final String[] var1 = new String[4];
		System.arraycopy(field_145915_a, 0, var1, 0, 4);
		return new S33PacketUpdateSign(field_145851_c, field_145848_d,
				field_145849_e, var1);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		field_145916_j = false;
		super.readFromNBT(p_145839_1_);

		for (int var2 = 0; var2 < 4; ++var2) {
			field_145915_a[var2] = p_145839_1_.getString("Text" + (var2 + 1));

			if (field_145915_a[var2].length() > 15) {
				field_145915_a[var2] = field_145915_a[var2].substring(0, 15);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setString("Text1", field_145915_a[0]);
		p_145841_1_.setString("Text2", field_145915_a[1]);
		p_145841_1_.setString("Text3", field_145915_a[2]);
		p_145841_1_.setString("Text4", field_145915_a[3]);
	}
}
