package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class EntityDamageSourceIndirect extends EntityDamageSource {
	private final Entity indirectEntity;

	public EntityDamageSourceIndirect(String p_i1568_1_, Entity p_i1568_2_,
			Entity p_i1568_3_) {
		super(p_i1568_1_, p_i1568_2_);
		indirectEntity = p_i1568_3_;
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
		final IChatComponent var2 = indirectEntity == null ? damageSourceEntity
				.func_145748_c_() : indirectEntity.func_145748_c_();
		final ItemStack var3 = indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase) indirectEntity)
				.getHeldItem() : null;
		final String var4 = "death.attack." + damageType;
		final String var5 = var4 + ".item";
		return var3 != null && var3.hasDisplayName()
				&& StatCollector.canTranslate(var5) ? new ChatComponentTranslation(
				var5, new Object[] { p_151519_1_.func_145748_c_(), var2,
						var3.func_151000_E() }) : new ChatComponentTranslation(
				var4, new Object[] { p_151519_1_.func_145748_c_(), var2 });
	}

	@Override
	public Entity getEntity() {
		return indirectEntity;
	}

	@Override
	public Entity getSourceOfDamage() {
		return damageSourceEntity;
	}
}
