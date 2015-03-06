package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntityDamageSource extends DamageSource {
	protected Entity damageSourceEntity;

	public EntityDamageSource(String p_i1567_1_, Entity p_i1567_2_) {
		super(p_i1567_1_);
		damageSourceEntity = p_i1567_2_;
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
		final ItemStack var2 = damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase) damageSourceEntity)
				.getHeldItem() : null;
		final String var3 = "death.attack." + damageType;
		final String var4 = var3 + ".item";
		return var2 != null && var2.hasDisplayName()
				&& StatCollector.canTranslate(var4) ? new ChatComponentTranslation(
				var4, new Object[] { p_151519_1_.func_145748_c_(),
						damageSourceEntity.func_145748_c_(),
						var2.func_151000_E() }) : new ChatComponentTranslation(
				var3, new Object[] { p_151519_1_.func_145748_c_(),
						damageSourceEntity.func_145748_c_() });
	}

	@Override
	public Entity getEntity() {
		return damageSourceEntity;
	}

	/**
	 * Return whether this damage source will have its damage amount scaled
	 * based on the current difficulty.
	 */
	@Override
	public boolean isDifficultyScaled() {
		return damageSourceEntity != null
				&& damageSourceEntity instanceof EntityLivingBase
				&& !(damageSourceEntity instanceof EntityPlayer);
	}
}
