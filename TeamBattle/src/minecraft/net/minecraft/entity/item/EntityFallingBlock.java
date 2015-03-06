package net.minecraft.entity.item;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity {
	private boolean field_145808_f;
	private boolean field_145809_g;
	public NBTTagCompound field_145810_d;
	private Block field_145811_e;
	public int field_145812_b;
	public boolean field_145813_c;
	public int field_145814_a;
	private int field_145815_h;
	private float field_145816_i;

	public EntityFallingBlock(World p_i1706_1_) {
		super(p_i1706_1_);
		field_145813_c = true;
		field_145815_h = 40;
		field_145816_i = 2.0F;
	}

	public EntityFallingBlock(World p_i45318_1_, double p_i45318_2_,
			double p_i45318_4_, double p_i45318_6_, Block p_i45318_8_) {
		this(p_i45318_1_, p_i45318_2_, p_i45318_4_, p_i45318_6_, p_i45318_8_, 0);
	}

	public EntityFallingBlock(World p_i45319_1_, double p_i45319_2_,
			double p_i45319_4_, double p_i45319_6_, Block p_i45319_8_,
			int p_i45319_9_) {
		super(p_i45319_1_);
		field_145813_c = true;
		field_145815_h = 40;
		field_145816_i = 2.0F;
		field_145811_e = p_i45319_8_;
		field_145814_a = p_i45319_9_;
		preventEntitySpawning = true;
		setSize(0.98F, 0.98F);
		yOffset = height / 2.0F;
		setPosition(p_i45319_2_, p_i45319_4_, p_i45319_6_);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = p_i45319_2_;
		prevPosY = p_i45319_4_;
		prevPosZ = p_i45319_6_;
	}

	@Override
	public void addEntityCrashInfo(CrashReportCategory p_85029_1_) {
		super.addEntityCrashInfo(p_85029_1_);
		p_85029_1_.addCrashSection("Immitating block ID",
				Integer.valueOf(Block.getIdFromBlock(field_145811_e)));
		p_85029_1_.addCrashSection("Immitating block data",
				Integer.valueOf(field_145814_a));
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	/**
	 * Return whether this entity should be rendered as on fire.
	 */
	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
		if (field_145809_g) {
			final int var2 = MathHelper.ceiling_float_int(p_70069_1_ - 1.0F);

			if (var2 > 0) {
				final ArrayList var3 = new ArrayList(
						worldObj.getEntitiesWithinAABBExcludingEntity(this,
								boundingBox));
				final boolean var4 = field_145811_e == Blocks.anvil;
				final DamageSource var5 = var4 ? DamageSource.anvil
						: DamageSource.fallingBlock;
				final Iterator var6 = var3.iterator();

				while (var6.hasNext()) {
					final Entity var7 = (Entity) var6.next();
					var7.attackEntityFrom(
							var5,
							Math.min(
									MathHelper.floor_float(var2
											* field_145816_i), field_145815_h));
				}

				if (var4
						&& rand.nextFloat() < 0.05000000074505806D + var2 * 0.05D) {
					int var8 = field_145814_a >> 2;
					final int var9 = field_145814_a & 3;
					++var8;

					if (var8 > 2) {
						field_145808_f = true;
					} else {
						field_145814_a = var9 | var8 << 2;
					}
				}
			}
		}
	}

	public Block func_145805_f() {
		return field_145811_e;
	}

	public void func_145806_a(boolean p_145806_1_) {
		field_145809_g = p_145806_1_;
	}

	public World func_145807_e() {
		return worldObj;
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (field_145811_e.getMaterial() == Material.air) {
			setDead();
		} else {
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			++field_145812_b;
			motionY -= 0.03999999910593033D;
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.9800000190734863D;
			motionY *= 0.9800000190734863D;
			motionZ *= 0.9800000190734863D;

			if (!worldObj.isClient) {
				final int var1 = MathHelper.floor_double(posX);
				final int var2 = MathHelper.floor_double(posY);
				final int var3 = MathHelper.floor_double(posZ);

				if (field_145812_b == 1) {
					if (worldObj.getBlock(var1, var2, var3) != field_145811_e) {
						setDead();
						return;
					}

					worldObj.setBlockToAir(var1, var2, var3);
				}

				if (onGround) {
					motionX *= 0.699999988079071D;
					motionZ *= 0.699999988079071D;
					motionY *= -0.5D;

					if (worldObj.getBlock(var1, var2, var3) != Blocks.piston_extension) {
						setDead();

						if (!field_145808_f
								&& worldObj.canPlaceEntityOnSide(
										field_145811_e, var1, var2, var3, true,
										1, (Entity) null, (ItemStack) null)
								&& !BlockFalling.func_149831_e(worldObj, var1,
										var2 - 1, var3)
								&& worldObj.setBlock(var1, var2, var3,
										field_145811_e, field_145814_a, 3)) {
							if (field_145811_e instanceof BlockFalling) {
								((BlockFalling) field_145811_e).func_149828_a(
										worldObj, var1, var2, var3,
										field_145814_a);
							}

							if (field_145810_d != null
									&& field_145811_e instanceof ITileEntityProvider) {
								final TileEntity var4 = worldObj.getTileEntity(
										var1, var2, var3);

								if (var4 != null) {
									final NBTTagCompound var5 = new NBTTagCompound();
									var4.writeToNBT(var5);
									final Iterator var6 = field_145810_d
											.func_150296_c().iterator();

									while (var6.hasNext()) {
										final String var7 = (String) var6
												.next();
										final NBTBase var8 = field_145810_d
												.getTag(var7);

										if (!var7.equals("x")
												&& !var7.equals("y")
												&& !var7.equals("z")) {
											var5.setTag(var7, var8.copy());
										}
									}

									var4.readFromNBT(var5);
									var4.onInventoryChanged();
								}
							}
						} else if (field_145813_c && !field_145808_f) {
							entityDropItem(
									new ItemStack(
											field_145811_e,
											1,
											field_145811_e
													.damageDropped(field_145814_a)),
									0.0F);
						}
					}
				} else if (field_145812_b > 100 && !worldObj.isClient
						&& (var2 < 1 || var2 > 256) || field_145812_b > 600) {
					if (field_145813_c) {
						entityDropItem(new ItemStack(field_145811_e, 1,
								field_145811_e.damageDropped(field_145814_a)),
								0.0F);
					}

					setDead();
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		if (p_70037_1_.func_150297_b("TileID", 99)) {
			field_145811_e = Block
					.getBlockById(p_70037_1_.getInteger("TileID"));
		} else {
			field_145811_e = Block
					.getBlockById(p_70037_1_.getByte("Tile") & 255);
		}

		field_145814_a = p_70037_1_.getByte("Data") & 255;
		field_145812_b = p_70037_1_.getByte("Time") & 255;

		if (p_70037_1_.func_150297_b("HurtEntities", 99)) {
			field_145809_g = p_70037_1_.getBoolean("HurtEntities");
			field_145816_i = p_70037_1_.getFloat("FallHurtAmount");
			field_145815_h = p_70037_1_.getInteger("FallHurtMax");
		} else if (field_145811_e == Blocks.anvil) {
			field_145809_g = true;
		}

		if (p_70037_1_.func_150297_b("DropItem", 99)) {
			field_145813_c = p_70037_1_.getBoolean("DropItem");
		}

		if (p_70037_1_.func_150297_b("TileEntityData", 10)) {
			field_145810_d = p_70037_1_.getCompoundTag("TileEntityData");
		}

		if (field_145811_e.getMaterial() == Material.air) {
			field_145811_e = Blocks.sand;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setByte("Tile", (byte) Block.getIdFromBlock(field_145811_e));
		p_70014_1_.setInteger("TileID", Block.getIdFromBlock(field_145811_e));
		p_70014_1_.setByte("Data", (byte) field_145814_a);
		p_70014_1_.setByte("Time", (byte) field_145812_b);
		p_70014_1_.setBoolean("DropItem", field_145813_c);
		p_70014_1_.setBoolean("HurtEntities", field_145809_g);
		p_70014_1_.setFloat("FallHurtAmount", field_145816_i);
		p_70014_1_.setInteger("FallHurtMax", field_145815_h);

		if (field_145810_d != null) {
			p_70014_1_.setTag("TileEntityData", field_145810_d);
		}
	}
}
