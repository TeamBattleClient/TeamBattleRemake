package net.minecraft.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

public class EntityOtherPlayerMP extends AbstractClientPlayer {
	private boolean isItemInUse;
	private double otherPlayerMPPitch;
	private int otherPlayerMPPosRotationIncrements;
	private double otherPlayerMPX;
	private double otherPlayerMPY;
	private double otherPlayerMPYaw;
	private double otherPlayerMPZ;

	public EntityOtherPlayerMP(World p_i45075_1_, GameProfile p_i45075_2_) {
		super(p_i45075_1_, p_i45075_2_);
		yOffset = 0.0F;
		stepHeight = 0.0F;
		noClip = true;
		field_71082_cx = 0.25F;
		renderDistanceWeight = 10.0D;
	}

	/**
	 * Notifies this sender of some sort of information. This is for messages
	 * intended to display to the user. Used for typical output (like
	 * "you asked for whether or not this game rule is set, so here's your answer"
	 * ), warnings (like "I fetched this block for you by ID, but I'd like you
	 * to know that every time you do this, I die a little
	 * inside"), and errors (like "it's not called iron_pixacke, silly").
	 */
	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().func_146227_a(
				p_145747_1_);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return true;
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 */
	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return false;
	}

	@Override
	public float getEyeHeight() {
		return 1.82F;
	}

	/**
	 * Return the position for this command sender.
	 */
	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(posX + 0.5D),
				MathHelper.floor_double(posY + 0.5D),
				MathHelper.floor_double(posZ + 0.5D));
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.updateEntityActionState();

		if (otherPlayerMPPosRotationIncrements > 0) {
			final double var1 = posX + (otherPlayerMPX - posX)
					/ otherPlayerMPPosRotationIncrements;
			final double var3 = posY + (otherPlayerMPY - posY)
					/ otherPlayerMPPosRotationIncrements;
			final double var5 = posZ + (otherPlayerMPZ - posZ)
					/ otherPlayerMPPosRotationIncrements;
			double var7;

			for (var7 = otherPlayerMPYaw - rotationYaw; var7 < -180.0D; var7 += 360.0D) {
				;
			}

			while (var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			rotationYaw = (float) (rotationYaw + var7
					/ otherPlayerMPPosRotationIncrements);
			rotationPitch = (float) (rotationPitch + (otherPlayerMPPitch - rotationPitch)
					/ otherPlayerMPPosRotationIncrements);
			--otherPlayerMPPosRotationIncrements;
			setPosition(var1, var3, var5);
			setRotation(rotationYaw, rotationPitch);
		}

		prevCameraYaw = cameraYaw;
		float var9 = MathHelper.sqrt_double(motionX * motionX + motionZ
				* motionZ);
		float var2 = (float) Math.atan(-motionY * 0.20000000298023224D) * 15.0F;

		if (var9 > 0.1F) {
			var9 = 0.1F;
		}

		if (!onGround || getHealth() <= 0.0F) {
			var9 = 0.0F;
		}

		if (onGround || getHealth() <= 0.0F) {
			var2 = 0.0F;
		}

		cameraYaw += (var9 - cameraYaw) * 0.4F;
		cameraPitch += (var2 - cameraPitch) * 0.8F;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		field_71082_cx = 0.0F;
		super.onUpdate();
		prevLimbSwingAmount = limbSwingAmount;
		final double var1 = posX - prevPosX;
		final double var3 = posZ - prevPosZ;
		float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3) * 4.0F;

		if (var5 > 1.0F) {
			var5 = 1.0F;
		}

		limbSwingAmount += (var5 - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;

		if (!isItemInUse && isEating()
				&& inventory.mainInventory[inventory.currentItem] != null) {
			final ItemStack var6 = inventory.mainInventory[inventory.currentItem];
			setItemInUse(inventory.mainInventory[inventory.currentItem], var6
					.getItem().getMaxItemUseDuration(var6));
			isItemInUse = true;
		} else if (isItemInUse && !isEating()) {
			clearItemInUse();
			isItemInUse = false;
		}
	}

	/**
	 * sets the players height back to normal after doing things like sleeping
	 * and dieing
	 */
	@Override
	protected void resetHeight() {
		yOffset = 0.0F;
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		if (p_70062_1_ == 0) {
			inventory.mainInventory[inventory.currentItem] = p_70062_2_;
		} else {
			inventory.armorInventory[p_70062_1_ - 1] = p_70062_2_;
		}
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no
	 * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	@Override
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_,
			double p_70056_5_, float p_70056_7_, float p_70056_8_,
			int p_70056_9_) {
		otherPlayerMPX = p_70056_1_;
		otherPlayerMPY = p_70056_3_;
		otherPlayerMPZ = p_70056_5_;
		otherPlayerMPYaw = p_70056_7_;
		otherPlayerMPPitch = p_70056_8_;
		otherPlayerMPPosRotationIncrements = p_70056_9_;
	}
}
