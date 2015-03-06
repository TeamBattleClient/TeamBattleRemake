package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityBeacon extends TileEntity implements IInventory {
	public static final Potion[][] field_146009_a = new Potion[][] {
			{ Potion.moveSpeed, Potion.digSpeed },
			{ Potion.resistance, Potion.jump }, { Potion.damageBoost },
			{ Potion.regeneration } };
	private String field_146008_p;
	private int field_146010_n;
	private ItemStack field_146011_o;
	private int field_146012_l = -1;
	private int field_146013_m;
	private float field_146014_j;
	private boolean field_146015_k;
	private long field_146016_i;

	@Override
	public void closeInventory() {
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (p_70298_1_ == 0 && field_146011_o != null) {
			if (p_70298_2_ >= field_146011_o.stackSize) {
				final ItemStack var3 = field_146011_o;
				field_146011_o = null;
				return var3;
			} else {
				field_146011_o.stackSize -= p_70298_2_;
				return new ItemStack(field_146011_o.getItem(), p_70298_2_,
						field_146011_o.getItemDamage());
			}
		} else
			return null;
	}

	public int func_145998_l() {
		return field_146012_l;
	}

	public void func_145999_a(String p_145999_1_) {
		field_146008_p = p_145999_1_;
	}

	private void func_146000_x() {
		if (field_146015_k && field_146012_l > 0 && !worldObj.isClient
				&& field_146013_m > 0) {
			final double var1 = field_146012_l * 10 + 10;
			byte var3 = 0;

			if (field_146012_l >= 4 && field_146013_m == field_146010_n) {
				var3 = 1;
			}

			final AxisAlignedBB var4 = AxisAlignedBB.getBoundingBox(
					field_145851_c, field_145848_d, field_145849_e,
					field_145851_c + 1, field_145848_d + 1, field_145849_e + 1)
					.expand(var1, var1, var1);
			var4.maxY = worldObj.getHeight();
			final List var5 = worldObj.getEntitiesWithinAABB(
					EntityPlayer.class, var4);
			Iterator var6 = var5.iterator();
			EntityPlayer var7;

			while (var6.hasNext()) {
				var7 = (EntityPlayer) var6.next();
				var7.addPotionEffect(new PotionEffect(field_146013_m, 180,
						var3, true));
			}

			if (field_146012_l >= 4 && field_146013_m != field_146010_n
					&& field_146010_n > 0) {
				var6 = var5.iterator();

				while (var6.hasNext()) {
					var7 = (EntityPlayer) var6.next();
					var7.addPotionEffect(new PotionEffect(field_146010_n, 180,
							0, true));
				}
			}
		}
	}

	public void func_146001_d(int p_146001_1_) {
		field_146013_m = 0;

		for (int var2 = 0; var2 < field_146012_l && var2 < 3; ++var2) {
			final Potion[] var3 = field_146009_a[var2];
			final int var4 = var3.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				final Potion var6 = var3[var5];

				if (var6.id == p_146001_1_) {
					field_146013_m = p_146001_1_;
					return;
				}
			}
		}
	}

	public float func_146002_i() {
		if (!field_146015_k)
			return 0.0F;
		else {
			final int var1 = (int) (worldObj.getTotalWorldTime() - field_146016_i);
			field_146016_i = worldObj.getTotalWorldTime();

			if (var1 > 1) {
				field_146014_j -= var1 / 40.0F;

				if (field_146014_j < 0.0F) {
					field_146014_j = 0.0F;
				}
			}

			field_146014_j += 0.025F;

			if (field_146014_j > 1.0F) {
				field_146014_j = 1.0F;
			}

			return field_146014_j;
		}
	}

	private void func_146003_y() {
		final int var1 = field_146012_l;

		if (!worldObj.canBlockSeeTheSky(field_145851_c, field_145848_d + 1,
				field_145849_e)) {
			field_146015_k = false;
			field_146012_l = 0;
		} else {
			field_146015_k = true;
			field_146012_l = 0;

			for (int var2 = 1; var2 <= 4; field_146012_l = var2++) {
				final int var3 = field_145848_d - var2;

				if (var3 < 0) {
					break;
				}

				boolean var4 = true;

				for (int var5 = field_145851_c - var2; var5 <= field_145851_c
						+ var2
						&& var4; ++var5) {
					for (int var6 = field_145849_e - var2; var6 <= field_145849_e
							+ var2; ++var6) {
						final Block var7 = worldObj.getBlock(var5, var3, var6);

						if (var7 != Blocks.emerald_block
								&& var7 != Blocks.gold_block
								&& var7 != Blocks.diamond_block
								&& var7 != Blocks.iron_block) {
							var4 = false;
							break;
						}
					}
				}

				if (!var4) {
					break;
				}
			}

			if (field_146012_l == 0) {
				field_146015_k = false;
			}
		}

		if (!worldObj.isClient && field_146012_l == 4 && var1 < field_146012_l) {
			final Iterator var8 = worldObj.getEntitiesWithinAABB(
					EntityPlayer.class,
					AxisAlignedBB.getBoundingBox(field_145851_c,
							field_145848_d, field_145849_e, field_145851_c,
							field_145848_d - 4, field_145849_e).expand(10.0D,
							5.0D, 10.0D)).iterator();

			while (var8.hasNext()) {
				final EntityPlayer var9 = (EntityPlayer) var8.next();
				var9.triggerAchievement(AchievementList.field_150965_K);
			}
		}
	}

	public void func_146004_e(int p_146004_1_) {
		field_146010_n = 0;

		if (field_146012_l >= 4) {
			for (int var2 = 0; var2 < 4; ++var2) {
				final Potion[] var3 = field_146009_a[var2];
				final int var4 = var3.length;

				for (int var5 = 0; var5 < var4; ++var5) {
					final Potion var6 = var3[var5];

					if (var6.id == p_146004_1_) {
						field_146010_n = p_146004_1_;
						return;
					}
				}
			}
		}
	}

	public void func_146005_c(int p_146005_1_) {
		field_146012_l = p_146005_1_;
	}

	public int func_146006_k() {
		return field_146010_n;
	}

	public int func_146007_j() {
		return field_146013_m;
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new S35PacketUpdateTileEntity(field_145851_c, field_145848_d,
				field_145849_e, 3, var1);
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return isInventoryNameLocalized() ? field_146008_p : "container.beacon";
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return 1;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ == 0 ? field_146011_o : null;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (p_70304_1_ == 0 && field_146011_o != null) {
			final ItemStack var2 = field_146011_o;
			field_146011_o = null;
			return var2;
		} else
			return null;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return field_146008_p != null && field_146008_p.length() > 0;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_2_.getItem() == Items.emerald
				|| p_94041_2_.getItem() == Items.diamond
				|| p_94041_2_.getItem() == Items.gold_ingot
				|| p_94041_2_.getItem() == Items.iron_ingot;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return worldObj.getTileEntity(field_145851_c, field_145848_d,
				field_145849_e) != this ? false : p_70300_1_.getDistanceSq(
				field_145851_c + 0.5D, field_145848_d + 0.5D,
				field_145849_e + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_146013_m = p_145839_1_.getInteger("Primary");
		field_146010_n = p_145839_1_.getInteger("Secondary");
		field_146012_l = p_145839_1_.getInteger("Levels");
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (p_70299_1_ == 0) {
			field_146011_o = p_70299_2_;
		}
	}

	@Override
	public void updateEntity() {
		if (worldObj.getTotalWorldTime() % 80L == 0L) {
			func_146003_y();
			func_146000_x();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("Primary", field_146013_m);
		p_145841_1_.setInteger("Secondary", field_146010_n);
		p_145841_1_.setInteger("Levels", field_146012_l);
	}
}
