package net.minecraft.client.entity;

import me.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import event.events.EventChatSent;
import event.events.EventPostSendMotionUpdates;
import event.events.EventPreSendMotionUpdates;

public class EntityClientPlayerMP extends EntityPlayerSP {
	private String field_142022_ce;
	private final StatFileWriter field_146108_bO;
	/** has the client player's health been set? */
	private boolean hasSetHealth;

	/** Old Minimum Y of the bounding box */
	private double oldMinY;
	private double oldPosX;
	private double oldPosZ;
	private float oldRotationPitch;

	private float oldRotationYaw;
	public final NetHandlerPlayClient sendQueue;

	/** should the player stop sneaking? */
	private boolean shouldStopSneaking;

	/**
	 * Counter used to ensure that the server sends a move packet (Packet11, 12
	 * or 13) to the client at least once a second.
	 */
	private int ticksSinceMovePacket;
	private boolean wasSneaking;

	public EntityClientPlayerMP(Minecraft p_i45064_1_, World p_i45064_2_,
			Session p_i45064_3_, NetHandlerPlayClient p_i45064_4_,
			StatFileWriter p_i45064_5_) {
		super(p_i45064_1_, p_i45064_2_, p_i45064_3_, 0);
		sendQueue = p_i45064_4_;
		field_146108_bO = p_i45064_5_;
	}

	/**
	 * Adds a value to a statistic field.
	 */
	@Override
	public void addStat(StatBase p_71064_1_, int p_71064_2_) {
		if (p_71064_1_ != null) {
			if (p_71064_1_.isIndependent) {
				super.addStat(p_71064_1_, p_71064_2_);
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return false;
	}

	/**
	 * set current crafting inventory back to the 2x2 square
	 */
	@Override
	public void closeScreen() {
		sendQueue.addToSendQueue(new C0DPacketCloseWindow(
				openContainer.windowId));
		closeScreenNoPacket();
	}

	/**
	 * Closes the GUI screen without sending a packet to the server
	 */
	public void closeScreenNoPacket() {
		inventory.setItemStack((ItemStack) null);
		super.closeScreen();
	}

	/**
	 * Deals damage to the entity. If its a EntityPlayer then will take damage
	 * from the armor first and then health second with the reduced value. Args:
	 * damageAmount
	 */
	@Override
	protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
		if (!isEntityInvulnerable()) {
			setHealth(getHealth() - p_70665_2_);
		}
	}

	/**
	 * Called when player presses the drop item key
	 */
	@Override
	public EntityItem dropOneItem(boolean p_71040_1_) {
		final int var2 = p_71040_1_ ? 3 : 4;
		sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, 0, 0, 0, 0));
		return null;
	}

	@Override
	protected void func_110318_g() {
		sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 6,
				(int) (getHorseJumpPower() * 100.0F)));
	}

	public void func_110322_i() {
		sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 7));
	}

	public void func_142020_c(String p_142020_1_) {
		field_142022_ce = p_142020_1_;
	}

	public String func_142021_k() {
		return field_142022_ce;
	}

	public StatFileWriter func_146107_m() {
		return field_146108_bO;
	}

	/**
	 * Heal living entity (param: amount of half-hearts)
	 */
	@Override
	public void heal(float p_70691_1_) {
	}

	/**
	 * Joins the passed in entity item with the world. Args: entityItem
	 */
	@Override
	protected void joinEntityItemWithWorld(EntityItem p_71012_1_) {
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	@Override
	public void mountEntity(Entity p_70078_1_) {
		super.mountEntity(p_70078_1_);

		if (p_70078_1_ instanceof EntityMinecart) {
			mc.getSoundHandler().playSound(
					new MovingSoundMinecartRiding(this,
							(EntityMinecart) p_70078_1_));
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (worldObj.blockExists(MathHelper.floor_double(posX), 0,
				MathHelper.floor_double(posZ))) {
			super.onUpdate();

			if (isRiding()) {
				final EventPreSendMotionUpdates pre = new EventPreSendMotionUpdates(
						rotationYaw, rotationPitch);
				Client.getEventManager().call(pre);
				if (pre.isCancelled())
					return;
				sendQueue
						.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
								pre.getYaw(), pre.getPitch(), onGround));
				sendQueue.addToSendQueue(new C0CPacketInput(moveStrafing,
						moveForward, movementInput.jump, movementInput.sneak));
				Client.getEventManager()
						.call(new EventPostSendMotionUpdates());
			} else {
				sendMotionUpdates();
			}
		}
	}

	@Override
	public void respawnPlayer() {
		sendQueue.addToSendQueue(new C16PacketClientStatus(
				C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
	}

	/**
	 * Sends a chat message from the player. Args: chatMessage
	 */
	public void sendChatMessage(String p_71165_1_) {
		final EventChatSent event = new EventChatSent(p_71165_1_);
		Client.getEventManager().call(event);
		event.checkForCommands();
		if (event.isCancelled())
			return;
		p_71165_1_ = event.getMessage();
		sendQueue.addToSendQueue(new C01PacketChatMessage(p_71165_1_));
	}

	/**
	 * Send updated motion and position information to the server
	 */
	public void sendMotionUpdates() {
		final EventPreSendMotionUpdates pre = new EventPreSendMotionUpdates(
				rotationYaw, rotationPitch);
		Client.getEventManager().call(pre);
		if (pre.isCancelled())
			return;
		if (mc.thePlayer.isBlocking()) {
			mc.getNetHandler().addToSendQueue(
					new C08PacketPlayerBlockPlacement(-1, -1, -1, 255,
							mc.thePlayer.inventory.getCurrentItem(), 0.0F,
							0.0F, 0.0F));
			mc.getNetHandler().addToSendQueue(
					new C07PacketPlayerDigging(5, 0, 0, 0, -255));
		}
		
		final boolean var1 = isSprinting();

		if (var1 != wasSneaking) {
			if (var1) {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 4));
			} else {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 5));
			}

			wasSneaking = var1;
		}

		final boolean var2 = isSneaking();

		if (var2 != shouldStopSneaking) {
			if (var2) {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 1));
			} else {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 2));
			}

			shouldStopSneaking = var2;
		}

		final double var3 = posX - oldPosX;
		final double var5 = boundingBox.minY - oldMinY;
		final double var7 = posZ - oldPosZ;
		final double var9 = pre.getYaw() - oldRotationYaw;
		final double var11 = pre.getPitch() - oldRotationPitch;
		boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4D
				|| ticksSinceMovePacket >= 20;
		final boolean var14 = var9 != 0.0D || var11 != 0.0D;

		if (ridingEntity != null) {
			sendQueue
					.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(motionX, -999.0D, -999.0D, motionZ, pre.getYaw(),pre.getPitch(), onGround));
			var13 = false;
		} else if (var13 && var14) {
			sendQueue
					.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
							posX, boundingBox.minY, posY, posZ, pre.getYaw(),
							pre.getPitch(), onGround));
		} else if (var13) {
			sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							posX, boundingBox.minY, posY, posZ, onGround));
		} else if (var14) {
			sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
					pre.getYaw(), pre.getPitch(), onGround));
		} else {
			sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
		}

		++ticksSinceMovePacket;
		if (var13) {
			oldPosX = posX;
			oldMinY = boundingBox.minY;
			oldPosZ = posZ;
			ticksSinceMovePacket = 0;
		}

		if (var14) {
			oldRotationYaw = pre.getYaw();
			oldRotationPitch = pre.getPitch();
		}
		
		Client.getEventManager().call(new EventPostSendMotionUpdates());
		if (mc.thePlayer.isBlocking()) {
			mc.getNetHandler().addToSendQueue(
					new C07PacketPlayerDigging(5, 0, 0, 0, -255));
			mc.getNetHandler().addToSendQueue(
					new C08PacketPlayerBlockPlacement(-1, -1, -1, 255,
							mc.thePlayer.inventory.getCurrentItem(), 0.0F,
							0.0F, 0.0F));
		}
	}

	/**
	 * Sends the player's abilities to the server (if there is one).
	 */
	@Override
	public void sendPlayerAbilities() {
		sendQueue.addToSendQueue(new C13PacketPlayerAbilities(capabilities));
	}

	/**
	 * Updates health locally.
	 */
	@Override
	public void setPlayerSPHealth(float p_71150_1_) {
		if (hasSetHealth) {
			super.setPlayerSPHealth(p_71150_1_);
		} else {
			setHealth(p_71150_1_);
			hasSetHealth = true;
		}
	}

	/**
	 * Swings the item the player is holding.
	 */
	@Override
	public void swingItem() {
		super.swingItem();
		sendQueue.addToSendQueue(new C0APacketAnimation(this, 1));
	}
}
