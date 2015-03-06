package net.minecraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class ItemCoal extends Item {
	private IIcon field_111220_a;

	public ItemCoal() {
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return p_77617_1_ == 1 ? field_111220_a : super
				.getIconFromDamage(p_77617_1_);
	}

	/**
	 * This returns the sub items
	 */
	@Override
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_,
			List p_150895_3_) {
		p_150895_3_.add(new ItemStack(p_150895_1_, 1, 0));
		p_150895_3_.add(new ItemStack(p_150895_1_, 1, 1));
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return p_77667_1_.getItemDamage() == 1 ? "item.charcoal" : "item.coal";
	}

	@Override
	public void registerIcons(IIconRegister p_94581_1_) {
		super.registerIcons(p_94581_1_);
		field_111220_a = p_94581_1_.registerIcon("charcoal");
	}
}
