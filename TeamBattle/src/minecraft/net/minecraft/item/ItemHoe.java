package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ItemHoe extends Item {
	protected Item.ToolMaterial theToolMaterial;

	public ItemHoe(Item.ToolMaterial p_i45343_1_) {
		theToolMaterial = p_i45343_1_;
		maxStackSize = 1;
		setMaxDamage(p_i45343_1_.getMaxUses());
		setCreativeTab(CreativeTabs.tabTools);
	}

	/**
	 * Returns the name of the material this tool is made from as it is declared
	 * in EnumToolMaterial (meaning diamond would return "EMERALD")
	 */
	public String getMaterialName() {
		return theToolMaterial.toString();
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@Override
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_,
			World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_,
			int p_77648_7_, float p_77648_8_, float p_77648_9_,
			float p_77648_10_) {
		if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_,
				p_77648_7_, p_77648_1_))
			return false;
		else {
			final Block var11 = p_77648_3_.getBlock(p_77648_4_, p_77648_5_,
					p_77648_6_);

			if (p_77648_7_ != 0
					&& p_77648_3_.getBlock(p_77648_4_, p_77648_5_ + 1,
							p_77648_6_).getMaterial() == Material.air
					&& (var11 == Blocks.grass || var11 == Blocks.dirt)) {
				final Block var12 = Blocks.farmland;
				p_77648_3_.playSoundEffect(p_77648_4_ + 0.5F,
						p_77648_5_ + 0.5F, p_77648_6_ + 0.5F,
						var12.stepSound.func_150498_e(),
						(var12.stepSound.func_150497_c() + 1.0F) / 2.0F,
						var12.stepSound.func_150494_d() * 0.8F);

				if (p_77648_3_.isClient)
					return true;
				else {
					p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_,
							var12);
					p_77648_1_.damageItem(1, p_77648_2_);
					return true;
				}
			} else
				return false;
		}
	}
}
