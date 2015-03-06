package net.minecraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBoat extends Item {

	public ItemBoat() {
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabTransport);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_,
			EntityPlayer p_77659_3_) {
		final float var4 = 1.0F;
		final float var5 = p_77659_3_.prevRotationPitch
				+ (p_77659_3_.rotationPitch - p_77659_3_.prevRotationPitch)
				* var4;
		final float var6 = p_77659_3_.prevRotationYaw
				+ (p_77659_3_.rotationYaw - p_77659_3_.prevRotationYaw) * var4;
		final double var7 = p_77659_3_.prevPosX
				+ (p_77659_3_.posX - p_77659_3_.prevPosX) * var4;
		final double var9 = p_77659_3_.prevPosY
				+ (p_77659_3_.posY - p_77659_3_.prevPosY) * var4 + 1.62D
				- p_77659_3_.yOffset;
		final double var11 = p_77659_3_.prevPosZ
				+ (p_77659_3_.posZ - p_77659_3_.prevPosZ) * var4;
		final Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
		final float var14 = MathHelper.cos(-var6 * 0.017453292F
				- (float) Math.PI);
		final float var15 = MathHelper.sin(-var6 * 0.017453292F
				- (float) Math.PI);
		final float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		final float var17 = MathHelper.sin(-var5 * 0.017453292F);
		final float var18 = var15 * var16;
		final float var20 = var14 * var16;
		final double var21 = 5.0D;
		final Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20
				* var21);
		final MovingObjectPosition var24 = p_77659_2_.rayTraceBlocks(var13,
				var23, true);

		if (var24 == null)
			return p_77659_1_;
		else {
			final Vec3 var25 = p_77659_3_.getLook(var4);
			boolean var26 = false;
			final float var27 = 1.0F;
			final List var28 = p_77659_2_.getEntitiesWithinAABBExcludingEntity(
					p_77659_3_,
					p_77659_3_.boundingBox.addCoord(var25.xCoord * var21,
							var25.yCoord * var21, var25.zCoord * var21).expand(
							var27, var27, var27));
			int var29;

			for (var29 = 0; var29 < var28.size(); ++var29) {
				final Entity var30 = (Entity) var28.get(var29);

				if (var30.canBeCollidedWith()) {
					final float var31 = var30.getCollisionBorderSize();
					final AxisAlignedBB var32 = var30.boundingBox.expand(var31,
							var31, var31);

					if (var32.isVecInside(var13)) {
						var26 = true;
					}
				}
			}

			if (var26)
				return p_77659_1_;
			else {
				if (var24.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					var29 = var24.blockX;
					int var33 = var24.blockY;
					final int var34 = var24.blockZ;

					if (p_77659_2_.getBlock(var29, var33, var34) == Blocks.snow_layer) {
						--var33;
					}

					final EntityBoat var35 = new EntityBoat(p_77659_2_,
							var29 + 0.5F, var33 + 1.0F, var34 + 0.5F);
					var35.rotationYaw = ((MathHelper
							.floor_double(p_77659_3_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) - 1) * 90;

					if (!p_77659_2_.getCollidingBoundingBoxes(var35,
							var35.boundingBox.expand(-0.1D, -0.1D, -0.1D))
							.isEmpty())
						return p_77659_1_;

					if (!p_77659_2_.isClient) {
						p_77659_2_.spawnEntityInWorld(var35);
					}

					if (!p_77659_3_.capabilities.isCreativeMode) {
						--p_77659_1_.stackSize;
					}
				}

				return p_77659_1_;
			}
		}
	}
}
