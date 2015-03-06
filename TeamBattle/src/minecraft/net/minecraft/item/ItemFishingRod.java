package net.minecraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemFishingRod extends Item {
	private IIcon theIcon;

	public ItemFishingRod() {
		setMaxDamage(64);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
	}

	public IIcon func_94597_g() {
		return theIcon;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability() {
		return 1;
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@Override
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	@Override
	public boolean isItemTool(ItemStack p_77616_1_) {
		return super.isItemTool(p_77616_1_);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_,
			EntityPlayer p_77659_3_) {
		if (p_77659_3_.fishEntity != null) {
			final int var4 = p_77659_3_.fishEntity.func_146034_e();
			p_77659_1_.damageItem(var4, p_77659_3_);
			p_77659_3_.swingItem();
		} else {
			p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F,
					0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!p_77659_2_.isClient) {
				p_77659_2_.spawnEntityInWorld(new EntityFishHook(p_77659_2_,
						p_77659_3_));
			}

			p_77659_3_.swingItem();
		}

		return p_77659_1_;
	}

	@Override
	public void registerIcons(IIconRegister p_94581_1_) {
		itemIcon = p_94581_1_.registerIcon(getIconString() + "_uncast");
		theIcon = p_94581_1_.registerIcon(getIconString() + "_cast");
	}

	/**
	 * Returns true if this item should be rotated by 180 degrees around the Y
	 * axis when being held in an entities hands.
	 */
	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}
}
