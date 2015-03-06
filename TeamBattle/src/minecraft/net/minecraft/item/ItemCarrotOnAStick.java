package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item {

	public ItemCarrotOnAStick() {
		setCreativeTab(CreativeTabs.tabTransport);
		setMaxStackSize(1);
		setMaxDamage(25);
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@Override
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_,
			EntityPlayer p_77659_3_) {
		if (p_77659_3_.isRiding()
				&& p_77659_3_.ridingEntity instanceof EntityPig) {
			final EntityPig var4 = (EntityPig) p_77659_3_.ridingEntity;

			if (var4.getAIControlledByPlayer().isControlledByPlayer()
					&& p_77659_1_.getMaxDamage() - p_77659_1_.getItemDamage() >= 7) {
				var4.getAIControlledByPlayer().boostSpeed();
				p_77659_1_.damageItem(7, p_77659_3_);

				if (p_77659_1_.stackSize == 0) {
					final ItemStack var5 = new ItemStack(Items.fishing_rod);
					var5.setTagCompound(p_77659_1_.stackTagCompound);
					return var5;
				}
			}
		}

		return p_77659_1_;
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
