package net.minecraft.init;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Bootstrap {
	private static boolean field_151355_a = false;

	static void func_151353_a() {
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow,
				new BehaviorProjectileDispense() {

					@Override
					protected IProjectile getProjectileEntity(World p_82499_1_,
							IPosition p_82499_2_) {
						final EntityArrow var3 = new EntityArrow(p_82499_1_,
								p_82499_2_.getX(), p_82499_2_.getY(),
								p_82499_2_.getZ());
						var3.canBePickedUp = 1;
						return var3;
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg,
				new BehaviorProjectileDispense() {

					@Override
					protected IProjectile getProjectileEntity(World p_82499_1_,
							IPosition p_82499_2_) {
						return new EntityEgg(p_82499_1_, p_82499_2_.getX(),
								p_82499_2_.getY(), p_82499_2_.getZ());
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball,
				new BehaviorProjectileDispense() {

					@Override
					protected IProjectile getProjectileEntity(World p_82499_1_,
							IPosition p_82499_2_) {
						return new EntitySnowball(p_82499_1_,
								p_82499_2_.getX(), p_82499_2_.getY(),
								p_82499_2_.getZ());
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(
				Items.experience_bottle, new BehaviorProjectileDispense() {

					@Override
					protected float func_82498_a() {
						return super.func_82498_a() * 0.5F;
					}

					@Override
					protected float func_82500_b() {
						return super.func_82500_b() * 1.25F;
					}

					@Override
					protected IProjectile getProjectileEntity(World p_82499_1_,
							IPosition p_82499_2_) {
						return new EntityExpBottle(p_82499_1_, p_82499_2_
								.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem,
				new IBehaviorDispenseItem() {
					private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();

					@Override
					public ItemStack dispense(IBlockSource p_82482_1_,
							final ItemStack p_82482_2_) {
						return ItemPotion.isSplash(p_82482_2_.getItemDamage()) ? new BehaviorProjectileDispense() {

							@Override
							protected float func_82498_a() {
								return super.func_82498_a() * 0.5F;
							}

							@Override
							protected float func_82500_b() {
								return super.func_82500_b() * 1.25F;
							}

							@Override
							protected IProjectile getProjectileEntity(
									World p_82499_1_, IPosition p_82499_2_) {
								return new EntityPotion(p_82499_1_, p_82499_2_
										.getX(), p_82499_2_.getY(), p_82499_2_
										.getZ(), p_82482_2_.copy());
							}
						}.dispense(p_82482_1_, p_82482_2_) : field_150843_b
								.dispense(p_82482_1_, p_82482_2_);
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg,
				new BehaviorDefaultDispenseItem() {

					@Override
					public ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						final EnumFacing var3 = BlockDispenser
								.func_149937_b(p_82487_1_.getBlockMetadata());
						final double var4 = p_82487_1_.getX()
								+ var3.getFrontOffsetX();
						final double var6 = p_82487_1_.getYInt() + 0.2F;
						final double var8 = p_82487_1_.getZ()
								+ var3.getFrontOffsetZ();
						final Entity var10 = ItemMonsterPlacer.spawnCreature(
								p_82487_1_.getWorld(),
								p_82487_2_.getItemDamage(), var4, var6, var8);

						if (var10 instanceof EntityLivingBase
								&& p_82487_2_.hasDisplayName()) {
							((EntityLiving) var10).setCustomNameTag(p_82487_2_
									.getDisplayName());
						}

						p_82487_2_.splitStack(1);
						return p_82487_2_;
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks,
				new BehaviorDefaultDispenseItem() {

					@Override
					public ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						final EnumFacing var3 = BlockDispenser
								.func_149937_b(p_82487_1_.getBlockMetadata());
						final double var4 = p_82487_1_.getX()
								+ var3.getFrontOffsetX();
						final double var6 = p_82487_1_.getYInt() + 0.2F;
						final double var8 = p_82487_1_.getZ()
								+ var3.getFrontOffsetZ();
						final EntityFireworkRocket var10 = new EntityFireworkRocket(
								p_82487_1_.getWorld(), var4, var6, var8,
								p_82487_2_);
						p_82487_1_.getWorld().spawnEntityInWorld(var10);
						p_82487_2_.splitStack(1);
						return p_82487_2_;
					}

					@Override
					protected void playDispenseSound(IBlockSource p_82485_1_) {
						p_82485_1_.getWorld().playAuxSFX(1002,
								p_82485_1_.getXInt(), p_82485_1_.getYInt(),
								p_82485_1_.getZInt(), 0);
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge,
				new BehaviorDefaultDispenseItem() {

					@Override
					public ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						final EnumFacing var3 = BlockDispenser
								.func_149937_b(p_82487_1_.getBlockMetadata());
						final IPosition var4 = BlockDispenser
								.func_149939_a(p_82487_1_);
						final double var5 = var4.getX()
								+ var3.getFrontOffsetX() * 0.3F;
						final double var7 = var4.getY()
								+ var3.getFrontOffsetX() * 0.3F;
						final double var9 = var4.getZ()
								+ var3.getFrontOffsetZ() * 0.3F;
						final World var11 = p_82487_1_.getWorld();
						final Random var12 = var11.rand;
						final double var13 = var12.nextGaussian() * 0.05D
								+ var3.getFrontOffsetX();
						final double var15 = var12.nextGaussian() * 0.05D
								+ var3.getFrontOffsetY();
						final double var17 = var12.nextGaussian() * 0.05D
								+ var3.getFrontOffsetZ();
						var11.spawnEntityInWorld(new EntitySmallFireball(var11,
								var5, var7, var9, var13, var15, var17));
						p_82487_2_.splitStack(1);
						return p_82487_2_;
					}

					@Override
					protected void playDispenseSound(IBlockSource p_82485_1_) {
						p_82485_1_.getWorld().playAuxSFX(1009,
								p_82485_1_.getXInt(), p_82485_1_.getYInt(),
								p_82485_1_.getZInt(), 0);
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat,
				new BehaviorDefaultDispenseItem() {
					private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();

					@Override
					public ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						final EnumFacing var3 = BlockDispenser
								.func_149937_b(p_82487_1_.getBlockMetadata());
						final World var4 = p_82487_1_.getWorld();
						final double var5 = p_82487_1_.getX()
								+ var3.getFrontOffsetX() * 1.125F;
						final double var7 = p_82487_1_.getY()
								+ var3.getFrontOffsetY() * 1.125F;
						final double var9 = p_82487_1_.getZ()
								+ var3.getFrontOffsetZ() * 1.125F;
						final int var11 = p_82487_1_.getXInt()
								+ var3.getFrontOffsetX();
						final int var12 = p_82487_1_.getYInt()
								+ var3.getFrontOffsetY();
						final int var13 = p_82487_1_.getZInt()
								+ var3.getFrontOffsetZ();
						final Material var14 = var4.getBlock(var11, var12,
								var13).getMaterial();
						double var15;

						if (Material.water.equals(var14)) {
							var15 = 1.0D;
						} else {
							if (!Material.air.equals(var14)
									|| !Material.water.equals(var4.getBlock(
											var11, var12 - 1, var13)
											.getMaterial()))
								return field_150842_b.dispense(p_82487_1_,
										p_82487_2_);

							var15 = 0.0D;
						}

						final EntityBoat var17 = new EntityBoat(var4, var5,
								var7 + var15, var9);
						var4.spawnEntityInWorld(var17);
						p_82487_2_.splitStack(1);
						return p_82487_2_;
					}

					@Override
					protected void playDispenseSound(IBlockSource p_82485_1_) {
						p_82485_1_.getWorld().playAuxSFX(1000,
								p_82485_1_.getXInt(), p_82485_1_.getYInt(),
								p_82485_1_.getZInt(), 0);
					}
				});
		final BehaviorDefaultDispenseItem var0 = new BehaviorDefaultDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();

			@Override
			public ItemStack dispenseStack(IBlockSource p_82487_1_,
					ItemStack p_82487_2_) {
				final ItemBucket var3 = (ItemBucket) p_82487_2_.getItem();
				final int var4 = p_82487_1_.getXInt();
				final int var5 = p_82487_1_.getYInt();
				final int var6 = p_82487_1_.getZInt();
				final EnumFacing var7 = BlockDispenser.func_149937_b(p_82487_1_
						.getBlockMetadata());

				if (var3.tryPlaceContainedLiquid(p_82487_1_.getWorld(), var4
						+ var7.getFrontOffsetX(),
						var5 + var7.getFrontOffsetY(),
						var6 + var7.getFrontOffsetZ())) {
					p_82487_2_.func_150996_a(Items.bucket);
					p_82487_2_.stackSize = 1;
					return p_82487_2_;
				} else
					return field_150841_b.dispense(p_82487_1_, p_82487_2_);
			}
		};
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket,
				var0);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket,
				var0);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket,
				new BehaviorDefaultDispenseItem() {
					private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();

					@Override
					public ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						final EnumFacing var3 = BlockDispenser
								.func_149937_b(p_82487_1_.getBlockMetadata());
						final World var4 = p_82487_1_.getWorld();
						final int var5 = p_82487_1_.getXInt()
								+ var3.getFrontOffsetX();
						final int var6 = p_82487_1_.getYInt()
								+ var3.getFrontOffsetY();
						final int var7 = p_82487_1_.getZInt()
								+ var3.getFrontOffsetZ();
						final Material var8 = var4.getBlock(var5, var6, var7)
								.getMaterial();
						final int var9 = var4
								.getBlockMetadata(var5, var6, var7);
						Item var10;

						if (Material.water.equals(var8) && var9 == 0) {
							var10 = Items.water_bucket;
						} else {
							if (!Material.lava.equals(var8) || var9 != 0)
								return super.dispenseStack(p_82487_1_,
										p_82487_2_);

							var10 = Items.lava_bucket;
						}

						var4.setBlockToAir(var5, var6, var7);

						if (--p_82487_2_.stackSize == 0) {
							p_82487_2_.func_150996_a(var10);
							p_82487_2_.stackSize = 1;
						} else if (((TileEntityDispenser) p_82487_1_
								.getBlockTileEntity())
								.func_146019_a(new ItemStack(var10)) < 0) {
							field_150840_b.dispense(p_82487_1_, new ItemStack(
									var10));
						}

						return p_82487_2_;
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(
				Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
					private boolean field_150839_b = true;

					@Override
					protected ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						final EnumFacing var3 = BlockDispenser
								.func_149937_b(p_82487_1_.getBlockMetadata());
						final World var4 = p_82487_1_.getWorld();
						final int var5 = p_82487_1_.getXInt()
								+ var3.getFrontOffsetX();
						final int var6 = p_82487_1_.getYInt()
								+ var3.getFrontOffsetY();
						final int var7 = p_82487_1_.getZInt()
								+ var3.getFrontOffsetZ();

						if (var4.isAirBlock(var5, var6, var7)) {
							var4.setBlock(var5, var6, var7, Blocks.fire);

							if (p_82487_2_.attemptDamageItem(1, var4.rand)) {
								p_82487_2_.stackSize = 0;
							}
						} else if (var4.getBlock(var5, var6, var7) == Blocks.tnt) {
							Blocks.tnt.onBlockDestroyedByPlayer(var4, var5,
									var6, var7, 1);
							var4.setBlockToAir(var5, var6, var7);
						} else {
							field_150839_b = false;
						}

						return p_82487_2_;
					}

					@Override
					protected void playDispenseSound(IBlockSource p_82485_1_) {
						if (field_150839_b) {
							p_82485_1_.getWorld().playAuxSFX(1000,
									p_82485_1_.getXInt(), p_82485_1_.getYInt(),
									p_82485_1_.getZInt(), 0);
						} else {
							p_82485_1_.getWorld().playAuxSFX(1001,
									p_82485_1_.getXInt(), p_82485_1_.getYInt(),
									p_82485_1_.getZInt(), 0);
						}
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye,
				new BehaviorDefaultDispenseItem() {
					private boolean field_150838_b = true;

					@Override
					protected ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						if (p_82487_2_.getItemDamage() == 15) {
							final EnumFacing var3 = BlockDispenser
									.func_149937_b(p_82487_1_
											.getBlockMetadata());
							final World var4 = p_82487_1_.getWorld();
							final int var5 = p_82487_1_.getXInt()
									+ var3.getFrontOffsetX();
							final int var6 = p_82487_1_.getYInt()
									+ var3.getFrontOffsetY();
							final int var7 = p_82487_1_.getZInt()
									+ var3.getFrontOffsetZ();

							if (ItemDye.func_150919_a(p_82487_2_, var4, var5,
									var6, var7)) {
								if (!var4.isClient) {
									var4.playAuxSFX(2005, var5, var6, var7, 0);
								}
							} else {
								field_150838_b = false;
							}

							return p_82487_2_;
						} else
							return super.dispenseStack(p_82487_1_, p_82487_2_);
					}

					@Override
					protected void playDispenseSound(IBlockSource p_82485_1_) {
						if (field_150838_b) {
							p_82485_1_.getWorld().playAuxSFX(1000,
									p_82485_1_.getXInt(), p_82485_1_.getYInt(),
									p_82485_1_.getZInt(), 0);
						} else {
							p_82485_1_.getWorld().playAuxSFX(1001,
									p_82485_1_.getXInt(), p_82485_1_.getYInt(),
									p_82485_1_.getZInt(), 0);
						}
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(
				Item.getItemFromBlock(Blocks.tnt),
				new BehaviorDefaultDispenseItem() {

					@Override
					protected ItemStack dispenseStack(IBlockSource p_82487_1_,
							ItemStack p_82487_2_) {
						final EnumFacing var3 = BlockDispenser
								.func_149937_b(p_82487_1_.getBlockMetadata());
						final World var4 = p_82487_1_.getWorld();
						final int var5 = p_82487_1_.getXInt()
								+ var3.getFrontOffsetX();
						final int var6 = p_82487_1_.getYInt()
								+ var3.getFrontOffsetY();
						final int var7 = p_82487_1_.getZInt()
								+ var3.getFrontOffsetZ();
						final EntityTNTPrimed var8 = new EntityTNTPrimed(var4,
								var5 + 0.5F, var6 + 0.5F, var7 + 0.5F,
								(EntityLivingBase) null);
						var4.spawnEntityInWorld(var8);
						--p_82487_2_.stackSize;
						return p_82487_2_;
					}
				});
	}

	public static void func_151354_b() {
		if (!field_151355_a) {
			field_151355_a = true;
			Block.registerBlocks();
			BlockFire.func_149843_e();
			Item.registerItems();
			StatList.func_151178_a();
			func_151353_a();
		}
	}
}
