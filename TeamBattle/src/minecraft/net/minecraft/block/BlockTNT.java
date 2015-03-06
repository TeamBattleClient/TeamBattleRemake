package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block {
	private IIcon field_150115_b;
	private IIcon field_150116_a;

	public BlockTNT() {
		super(Material.tnt);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * Return whether this block can drop from an explosion.
	 */
	@Override
	public boolean canDropFromExplosion(Explosion p_149659_1_) {
		return false;
	}

	public void func_150114_a(World p_150114_1_, int p_150114_2_,
			int p_150114_3_, int p_150114_4_, int p_150114_5_,
			EntityLivingBase p_150114_6_) {
		if (!p_150114_1_.isClient) {
			if ((p_150114_5_ & 1) == 1) {
				final EntityTNTPrimed var7 = new EntityTNTPrimed(p_150114_1_,
						p_150114_2_ + 0.5F, p_150114_3_ + 0.5F,
						p_150114_4_ + 0.5F, p_150114_6_);
				p_150114_1_.spawnEntityInWorld(var7);
				p_150114_1_.playSoundAtEntity(var7, "game.tnt.primed", 1.0F,
						1.0F);
			}
		}
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 0 ? field_150115_b
				: p_149691_1_ == 1 ? field_150116_a : blockIcon;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_,
			int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_,
			int p_149727_6_, float p_149727_7_, float p_149727_8_,
			float p_149727_9_) {
		if (p_149727_5_.getCurrentEquippedItem() != null
				&& p_149727_5_.getCurrentEquippedItem().getItem() == Items.flint_and_steel) {
			func_150114_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_,
					1, p_149727_5_);
			p_149727_1_.setBlockToAir(p_149727_2_, p_149727_3_, p_149727_4_);
			p_149727_5_.getCurrentEquippedItem().damageItem(1, p_149727_5_);
			return true;
		} else
			return super.onBlockActivated(p_149727_1_, p_149727_2_,
					p_149727_3_, p_149727_4_, p_149727_5_, p_149727_6_,
					p_149727_7_, p_149727_8_, p_149727_9_);
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);

		if (p_149726_1_.isBlockIndirectlyGettingPowered(p_149726_2_,
				p_149726_3_, p_149726_4_)) {
			onBlockDestroyedByPlayer(p_149726_1_, p_149726_2_, p_149726_3_,
					p_149726_4_, 1);
			p_149726_1_.setBlockToAir(p_149726_2_, p_149726_3_, p_149726_4_);
		}
	}

	/**
	 * Called upon the block being destroyed by an explosion
	 */
	@Override
	public void onBlockDestroyedByExplosion(World p_149723_1_, int p_149723_2_,
			int p_149723_3_, int p_149723_4_, Explosion p_149723_5_) {
		if (!p_149723_1_.isClient) {
			final EntityTNTPrimed var6 = new EntityTNTPrimed(p_149723_1_,
					p_149723_2_ + 0.5F, p_149723_3_ + 0.5F, p_149723_4_ + 0.5F,
					p_149723_5_.getExplosivePlacedBy());
			var6.fuse = p_149723_1_.rand.nextInt(var6.fuse / 4) + var6.fuse / 8;
			p_149723_1_.spawnEntityInWorld(var6);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_,
			int p_149664_3_, int p_149664_4_, int p_149664_5_) {
		func_150114_a(p_149664_1_, p_149664_2_, p_149664_3_, p_149664_4_,
				p_149664_5_, (EntityLivingBase) null);
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_,
			int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
		if (p_149670_5_ instanceof EntityArrow && !p_149670_1_.isClient) {
			final EntityArrow var6 = (EntityArrow) p_149670_5_;

			if (var6.isBurning()) {
				func_150114_a(
						p_149670_1_,
						p_149670_2_,
						p_149670_3_,
						p_149670_4_,
						1,
						var6.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) var6.shootingEntity
								: null);
				p_149670_1_
						.setBlockToAir(p_149670_2_, p_149670_3_, p_149670_4_);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		if (p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_,
				p_149695_3_, p_149695_4_)) {
			onBlockDestroyedByPlayer(p_149695_1_, p_149695_2_, p_149695_3_,
					p_149695_4_, 1);
			p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 1;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_150116_a = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_150115_b = p_149651_1_.registerIcon(getTextureName() + "_bottom");
	}
}
