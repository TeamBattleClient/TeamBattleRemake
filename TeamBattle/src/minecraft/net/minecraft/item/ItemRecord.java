package net.minecraft.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.BlockJukebox;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemRecord extends Item {
	private static final Map field_150928_b = new HashMap();

	public static ItemRecord func_150926_b(String p_150926_0_) {
		return (ItemRecord) field_150928_b.get(p_150926_0_);
	}

	public final String field_150929_a;

	protected ItemRecord(String p_i45350_1_) {
		field_150929_a = p_i45350_1_;
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabMisc);
		field_150928_b.put(p_i45350_1_, this);
	}

	/**
	 * allows items to add custom lines of information to the mouseover
	 * description
	 */
	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_,
			List p_77624_3_, boolean p_77624_4_) {
		p_77624_3_.add(func_150927_i());
	}

	public String func_150927_i() {
		return StatCollector.translateToLocal("item.record." + field_150929_a
				+ ".desc");
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return itemIcon;
	}

	/**
	 * Return an item rarity from EnumRarity
	 */
	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return EnumRarity.rare;
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
		if (p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_) == Blocks.jukebox
				&& p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_,
						p_77648_6_) == 0) {
			if (p_77648_3_.isClient)
				return true;
			else {
				((BlockJukebox) Blocks.jukebox).func_149926_b(p_77648_3_,
						p_77648_4_, p_77648_5_, p_77648_6_, p_77648_1_);
				p_77648_3_.playAuxSFXAtEntity((EntityPlayer) null, 1005,
						p_77648_4_, p_77648_5_, p_77648_6_,
						Item.getIdFromItem(this));
				--p_77648_1_.stackSize;
				return true;
			}
		} else
			return false;
	}
}
