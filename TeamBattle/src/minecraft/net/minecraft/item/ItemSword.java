package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class ItemSword extends Item {
	private final Item.ToolMaterial field_150933_b;
	private final float field_150934_a;

	public ItemSword(Item.ToolMaterial p_i45356_1_) {
		field_150933_b = p_i45356_1_;
		maxStackSize = 1;
		setMaxDamage(p_i45356_1_.getMaxUses());
		setCreativeTab(CreativeTabs.tabCombat);
		field_150934_a = 4.0F + p_i45356_1_.getDamageVsEntity();
	}

	@Override
	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_) {
		if (p_150893_2_ == Blocks.web)
			return 15.0F;
		else {
			final Material var3 = p_150893_2_.getMaterial();
			return var3 != Material.plants && var3 != Material.vine
					&& var3 != Material.coral && var3 != Material.leaves
					&& var3 != Material.field_151572_C ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean func_150897_b(Block p_150897_1_) {
		return p_150897_1_ == Blocks.web;
	}

	public float func_150931_i() {
		return field_150933_b.getDamageVsEntity();
	}

	public String func_150932_j() {
		return field_150933_b.toString();
	}

	@Override
	public float getDamageVsEntity() {
		return field_150934_a;
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return field_150933_b.func_150995_f() == p_82789_2_.getItem() ? true
				: super.getIsRepairable(p_82789_1_, p_82789_2_);
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit
	 * damage.
	 */
	@Override
	public Multimap getItemAttributeModifiers() {
		final Multimap var1 = super.getItemAttributeModifiers();
		var1.put(SharedMonsterAttributes.attackDamage
				.getAttributeUnlocalizedName(), new AttributeModifier(
				field_111210_e, "Weapon modifier", field_150934_a, 0));
		return var1;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability() {
		return field_150933_b.getEnchantability();
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.block;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 72000;
	}

	/**
	 * Current implementations of this method in child classes do not use the
	 * entry argument beside ev. They just raise the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_,
			EntityLivingBase p_77644_3_) {
		p_77644_1_.damageItem(1, p_77644_3_);
		return true;
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_,
			Block p_150894_3_, int p_150894_4_, int p_150894_5_,
			int p_150894_6_, EntityLivingBase p_150894_7_) {
		if (p_150894_3_.getBlockHardness(p_150894_2_, p_150894_4_, p_150894_5_,
				p_150894_6_) != 0.0D) {
			p_150894_1_.damageItem(2, p_150894_7_);
		}

		return true;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_,
			EntityPlayer p_77659_3_) {
		p_77659_3_.setItemInUse(p_77659_1_, getMaxItemUseDuration(p_77659_1_));
		return p_77659_1_;
	}
}
