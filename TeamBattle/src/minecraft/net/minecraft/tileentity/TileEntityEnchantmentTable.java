package net.minecraft.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityEnchantmentTable extends TileEntity {
	private static Random field_145923_r = new Random();
	private String field_145922_s;
	public float field_145924_q;
	public float field_145925_p;
	public int field_145926_a;
	public float field_145927_n;
	public float field_145928_o;
	public float field_145929_l;
	public float field_145930_m;
	public float field_145931_j;
	public float field_145932_k;
	public float field_145933_i;

	public String func_145919_a() {
		return func_145921_b() ? field_145922_s : "container.enchant";
	}

	public void func_145920_a(String p_145920_1_) {
		field_145922_s = p_145920_1_;
	}

	public boolean func_145921_b() {
		return field_145922_s != null && field_145922_s.length() > 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);

		if (p_145839_1_.func_150297_b("CustomName", 8)) {
			field_145922_s = p_145839_1_.getString("CustomName");
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		field_145927_n = field_145930_m;
		field_145925_p = field_145928_o;
		final EntityPlayer var1 = worldObj.getClosestPlayer(
				field_145851_c + 0.5F, field_145848_d + 0.5F,
				field_145849_e + 0.5F, 3.0D);

		if (var1 != null) {
			final double var2 = var1.posX - (field_145851_c + 0.5F);
			final double var4 = var1.posZ - (field_145849_e + 0.5F);
			field_145924_q = (float) Math.atan2(var4, var2);
			field_145930_m += 0.1F;

			if (field_145930_m < 0.5F || field_145923_r.nextInt(40) == 0) {
				final float var6 = field_145932_k;

				do {
					field_145932_k += field_145923_r.nextInt(4)
							- field_145923_r.nextInt(4);
				} while (var6 == field_145932_k);
			}
		} else {
			field_145924_q += 0.02F;
			field_145930_m -= 0.1F;
		}

		while (field_145928_o >= (float) Math.PI) {
			field_145928_o -= (float) Math.PI * 2F;
		}

		while (field_145928_o < -(float) Math.PI) {
			field_145928_o += (float) Math.PI * 2F;
		}

		while (field_145924_q >= (float) Math.PI) {
			field_145924_q -= (float) Math.PI * 2F;
		}

		while (field_145924_q < -(float) Math.PI) {
			field_145924_q += (float) Math.PI * 2F;
		}

		float var7;

		for (var7 = field_145924_q - field_145928_o; var7 >= (float) Math.PI; var7 -= (float) Math.PI * 2F) {
			;
		}

		while (var7 < -(float) Math.PI) {
			var7 += (float) Math.PI * 2F;
		}

		field_145928_o += var7 * 0.4F;

		if (field_145930_m < 0.0F) {
			field_145930_m = 0.0F;
		}

		if (field_145930_m > 1.0F) {
			field_145930_m = 1.0F;
		}

		++field_145926_a;
		field_145931_j = field_145933_i;
		float var3 = (field_145932_k - field_145933_i) * 0.4F;
		final float var8 = 0.2F;

		if (var3 < -var8) {
			var3 = -var8;
		}

		if (var3 > var8) {
			var3 = var8;
		}

		field_145929_l += (var3 - field_145929_l) * 0.9F;
		field_145933_i += field_145929_l;
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);

		if (func_145921_b()) {
			p_145841_1_.setString("CustomName", field_145922_s);
		}
	}
}
