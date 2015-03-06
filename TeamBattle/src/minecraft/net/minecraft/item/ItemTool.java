package net.minecraft.item;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class ItemTool extends Item {
	/** Damage versus entities. */
	private final float damageVsEntity;
	protected float efficiencyOnProperMaterial = 4.0F;

	private final Set field_150914_c;

	/** The material this tool is made from. */
	protected Item.ToolMaterial toolMaterial;

	protected ItemTool(float p_i45333_1_, Item.ToolMaterial p_i45333_2_,
			Set p_i45333_3_) {
		toolMaterial = p_i45333_2_;
		field_150914_c = p_i45333_3_;
		maxStackSize = 1;
		setMaxDamage(p_i45333_2_.getMaxUses());
		efficiencyOnProperMaterial = p_i45333_2_
				.getEfficiencyOnProperMaterial();
		damageVsEntity = p_i45333_1_ + p_i45333_2_.getDamageVsEntity();
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_) {
		return field_150914_c.contains(p_150893_2_) ? efficiencyOnProperMaterial
				: 1.0F;
	}

	public Item.ToolMaterial func_150913_i() {
		return toolMaterial;
	}

	@Override
	public float getDamageVsEntity() {
		return damageVsEntity;
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return toolMaterial.func_150995_f() == p_82789_2_.getItem() ? true
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
				field_111210_e, "Tool modifier", damageVsEntity, 0));
		return var1;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability() {
		return toolMaterial.getEnchantability();
	}

	/**
	 * Return the name for this tool's material.
	 */
	public String getToolMaterialName() {
		return toolMaterial.toString();
	}

	/**
	 * Current implementations of this method in child classes do not use the
	 * entry argument beside ev. They just raise the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_,
			EntityLivingBase p_77644_3_) {
		p_77644_1_.damageItem(2, p_77644_3_);
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
			p_150894_1_.damageItem(1, p_150894_7_);
		}

		return true;
	}
}
