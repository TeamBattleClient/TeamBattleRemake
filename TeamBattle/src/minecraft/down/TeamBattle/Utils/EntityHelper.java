package down.TeamBattle.Utils;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import com.google.common.collect.Multimap;

public final class EntityHelper {
	private static Minecraft mc = Minecraft.getMinecraft();

	public static int getBestWeapon(Entity target) {
		final int originalSlot = mc.thePlayer.inventory.currentItem;
		int weaponSlot = -1;
		float weaponDamage = 1.0F;
		for (byte slot = 0; slot < 9; slot = (byte) (slot + 1)) {
			mc.thePlayer.inventory.currentItem = slot;
			final ItemStack itemStack = mc.thePlayer.getHeldItem();
			if (itemStack != null) {
				float damage = getItemDamage(itemStack);
				damage += EnchantmentHelper.getEnchantmentModifierLiving(
						mc.thePlayer, (EntityLivingBase) target);
				if (damage > weaponDamage) {
					weaponDamage = damage;
					weaponSlot = slot;
				}
			}
		}
		if (weaponSlot != -1)
			return weaponSlot;
		return originalSlot;
	}

	public static float[] getEntityRotations(Entity target) {
		final double var4 = target.posX - mc.thePlayer.posX;
		final double var6 = target.posZ - mc.thePlayer.posZ;
		final double var8 = target.posY + target.getEyeHeight() / 1.3
				- (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
		final double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
		final float yaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
		final float pitch = (float) -(Math.atan2(var8, var14) * 180.0D / Math.PI);
		return new float[] { yaw, pitch };
	}

	private static float getItemDamage(ItemStack itemStack) {
		final Multimap multimap = itemStack.getAttributeModifiers();
		if (!multimap.isEmpty()) {
			final Iterator iterator = multimap.entries().iterator();
			if (iterator.hasNext()) {
				final Map.Entry entry = (Map.Entry) iterator.next();
				final AttributeModifier attributeModifier = (AttributeModifier) entry
						.getValue();
				double damage;
				if (attributeModifier.getOperation() != 1
						&& attributeModifier.getOperation() != 2) {
					damage = attributeModifier.getAmount();
				} else {
					damage = attributeModifier.getAmount() * 100.0D;
				}
				if (attributeModifier.getAmount() > 1.0D)
					return 1.0F + (float) damage;
				return 1.0F;
			}
		}
		return 1.0F;
	}

}
