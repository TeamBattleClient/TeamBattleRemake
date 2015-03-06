package net.minecraft.entity.item;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging {
	public static enum EnumArt {
		Alban("Alban", 2, "Alban", 16, 16, 32, 0), Aztec("Aztec", 1, "Aztec",
				16, 16, 16, 0), Aztec2("Aztec2", 3, "Aztec2", 16, 16, 48, 0), Bomb(
				"Bomb", 4, "Bomb", 16, 16, 64, 0), BurningSkull("BurningSkull",
				23, "BurningSkull", 64, 64, 128, 192), Bust("Bust", 15, "Bust",
				32, 32, 32, 128), Courbet("Courbet", 8, "Courbet", 32, 16, 32,
				32), Creebet("Creebet", 11, "Creebet", 32, 16, 128, 32), DonkeyKong(
				"DonkeyKong", 25, "DonkeyKong", 64, 48, 192, 112), Fighters(
				"Fighters", 20, "Fighters", 64, 32, 0, 96), Graham("Graham",
				13, "Graham", 16, 32, 16, 64), Kebab("Kebab", 0, "Kebab", 16,
				16, 0, 0), Match("Match", 14, "Match", 32, 32, 0, 128), Pigscene(
				"Pigscene", 22, "Pigscene", 64, 64, 64, 192), Plant("Plant", 5,
				"Plant", 16, 16, 80, 0), Pointer("Pointer", 21, "Pointer", 64,
				64, 0, 192), Pool("Pool", 7, "Pool", 32, 16, 0, 32), Sea("Sea",
				9, "Sea", 32, 16, 64, 32), Skeleton("Skeleton", 24, "Skeleton",
				64, 48, 192, 64), SkullAndRoses("SkullAndRoses", 18,
				"SkullAndRoses", 32, 32, 128, 128), Stage("Stage", 16, "Stage",
				32, 32, 64, 128), Sunset("Sunset", 10, "Sunset", 32, 16, 96, 32), Void(
				"Void", 17, "Void", 32, 32, 96, 128), Wanderer("Wanderer", 12,
				"Wanderer", 16, 32, 0, 64), Wasteland("Wasteland", 6,
				"Wasteland", 16, 16, 96, 0), Wither("Wither", 19, "Wither", 32,
				32, 160, 128);
		public static final int maxArtTitleLength = "SkullAndRoses".length();
		public final int offsetX;
		public final int offsetY;
		public final int sizeX;
		public final int sizeY;
		public final String title;

		private EnumArt(String p_i1598_1_, int p_i1598_2_, String p_i1598_3_,
				int p_i1598_4_, int p_i1598_5_, int p_i1598_6_, int p_i1598_7_) {
			title = p_i1598_3_;
			sizeX = p_i1598_4_;
			sizeY = p_i1598_5_;
			offsetX = p_i1598_6_;
			offsetY = p_i1598_7_;
		}
	}

	public EntityPainting.EnumArt art;

	public EntityPainting(World p_i1599_1_) {
		super(p_i1599_1_);
	}

	public EntityPainting(World p_i1600_1_, int p_i1600_2_, int p_i1600_3_,
			int p_i1600_4_, int p_i1600_5_) {
		super(p_i1600_1_, p_i1600_2_, p_i1600_3_, p_i1600_4_, p_i1600_5_);
		final ArrayList var6 = new ArrayList();
		final EntityPainting.EnumArt[] var7 = EntityPainting.EnumArt.values();
		final int var8 = var7.length;

		for (int var9 = 0; var9 < var8; ++var9) {
			final EntityPainting.EnumArt var10 = var7[var9];
			art = var10;
			setDirection(p_i1600_5_);

			if (onValidSurface()) {
				var6.add(var10);
			}
		}

		if (!var6.isEmpty()) {
			art = (EntityPainting.EnumArt) var6.get(rand.nextInt(var6.size()));
		}

		setDirection(p_i1600_5_);
	}

	public EntityPainting(World p_i1601_1_, int p_i1601_2_, int p_i1601_3_,
			int p_i1601_4_, int p_i1601_5_, String p_i1601_6_) {
		this(p_i1601_1_, p_i1601_2_, p_i1601_3_, p_i1601_4_, p_i1601_5_);
		final EntityPainting.EnumArt[] var7 = EntityPainting.EnumArt.values();
		final int var8 = var7.length;

		for (int var9 = 0; var9 < var8; ++var9) {
			final EntityPainting.EnumArt var10 = var7[var9];

			if (var10.title.equals(p_i1601_6_)) {
				art = var10;
				break;
			}
		}

		setDirection(p_i1601_5_);
	}

	@Override
	public int getHeightPixels() {
		return art.sizeY;
	}

	@Override
	public int getWidthPixels() {
		return art.sizeX;
	}

	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	@Override
	public void onBroken(Entity p_110128_1_) {
		if (p_110128_1_ instanceof EntityPlayer) {
			final EntityPlayer var2 = (EntityPlayer) p_110128_1_;

			if (var2.capabilities.isCreativeMode)
				return;
		}

		entityDropItem(new ItemStack(Items.painting), 0.0F);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		final String var2 = p_70037_1_.getString("Motive");
		final EntityPainting.EnumArt[] var3 = EntityPainting.EnumArt.values();
		final int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			final EntityPainting.EnumArt var6 = var3[var5];

			if (var6.title.equals(var2)) {
				art = var6;
			}
		}

		if (art == null) {
			art = EntityPainting.EnumArt.Kebab;
		}

		super.readEntityFromNBT(p_70037_1_);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setString("Motive", art.title);
		super.writeEntityToNBT(p_70014_1_);
	}
}
