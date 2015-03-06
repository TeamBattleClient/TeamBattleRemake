package net.minecraft.client.multiplayer;

import me.client.Client;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import event.events.EventBlockBreaking;
import event.events.EventBlockClicked;

public class PlayerControllerMP {
	/**
	 * Block dig operation in creative mode (instantly digs the block).
	 */
	public static void clickBlockCreative(Minecraft p_78744_0_,
			PlayerControllerMP p_78744_1_, int p_78744_2_, int p_78744_3_,
			int p_78744_4_, int p_78744_5_) {
		if (!p_78744_0_.theWorld.extinguishFire(p_78744_0_.thePlayer,
				p_78744_2_, p_78744_3_, p_78744_4_, p_78744_5_)) {
			p_78744_1_.onPlayerDestroyBlock(p_78744_2_, p_78744_3_, p_78744_4_,
					p_78744_5_);
		}
	}

	/**
	 * Delays the first damage on the block after the first click on the block
	 */
	private int blockHitDelay;

	/** Current block damage (MP) */
	private float curBlockDamageMP;

	/** PosX of the current block being destroyed */
	private int currentBlockX = -1;

	/** PosY of the current block being destroyed */
	private int currentBlockY = -1;

	/** PosZ of the current block being destroyed */
	private int currentblockZ = -1;

	/** Current game type for the player */
	private WorldSettings.GameType currentGameType;

	/** The Item currently being used to destroy a block */
	private ItemStack currentItemHittingBlock;

	/** Index of the current item held by the player in the inventory hotbar */
	private int currentPlayerItem;

	/** Tells if the player is hitting a block */
	private boolean isHittingBlock;

	/** The Minecraft instance. */
	private final Minecraft mc;

	private final NetHandlerPlayClient netClientHandler;

	/**
	 * Tick counter, when it hits 4 it resets back to 0 and plays the step sound
	 */
	private float stepSoundTickCounter;

	public PlayerControllerMP(Minecraft p_i45062_1_,
			NetHandlerPlayClient p_i45062_2_) {
		currentGameType = WorldSettings.GameType.SURVIVAL;
		mc = p_i45062_1_;
		netClientHandler = p_i45062_2_;
	}

	/**
	 * Attacks an entity
	 */
	public void attackEntity(EntityPlayer p_78764_1_, Entity p_78764_2_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C02PacketUseEntity(p_78764_2_,
				C02PacketUseEntity.Action.ATTACK));
		p_78764_1_.attackTargetEntityWithCurrentItem(p_78764_2_);
	}

	/**
	 * Called by Minecraft class when the player is hitting a block with an
	 * item. Args: x, y, z, side
	 */
	public void clickBlock(int p_78743_1_, int p_78743_2_, int p_78743_3_,
			int p_78743_4_) {
		final EventBlockClicked clicked = new EventBlockClicked(p_78743_1_,
				p_78743_2_, p_78743_3_, p_78743_4_);
		Client.getEventManager().call(clicked);
		if (!currentGameType.isAdventure()
				|| mc.thePlayer.isCurrentToolAdventureModeExempt(p_78743_1_,
						p_78743_2_, p_78743_3_)) {
			if (currentGameType.isCreative()) {
				netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0,
						p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_));
				clickBlockCreative(mc, this, p_78743_1_, p_78743_2_,
						p_78743_3_, p_78743_4_);
				blockHitDelay = 5;
			} else if (!isHittingBlock
					|| !sameToolAndBlock(p_78743_1_, p_78743_2_, p_78743_3_)) {
				if (isHittingBlock) {
					netClientHandler.addToSendQueue(new C07PacketPlayerDigging(
							1, currentBlockX, currentBlockY, currentblockZ,
							p_78743_4_));
				}

				netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0,
						p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_));
				final Block var5 = mc.theWorld.getBlock(p_78743_1_, p_78743_2_,
						p_78743_3_);
				final boolean var6 = var5.getMaterial() != Material.air;

				if (var6 && curBlockDamageMP == 0.0F) {
					var5.onBlockClicked(mc.theWorld, p_78743_1_, p_78743_2_,
							p_78743_3_, mc.thePlayer);
				}

				if (var6
						&& var5.getPlayerRelativeBlockHardness(mc.thePlayer,
								mc.thePlayer.worldObj, p_78743_1_, p_78743_2_,
								p_78743_3_) >= 1.0F) {
					onPlayerDestroyBlock(p_78743_1_, p_78743_2_, p_78743_3_,
							p_78743_4_);
				} else {
					isHittingBlock = true;
					currentBlockX = p_78743_1_;
					currentBlockY = p_78743_2_;
					currentblockZ = p_78743_3_;
					currentItemHittingBlock = mc.thePlayer.getHeldItem();
					curBlockDamageMP = 0.0F;
					stepSoundTickCounter = 0.0F;
					mc.theWorld.destroyBlockInWorldPartially(
							mc.thePlayer.getEntityId(), currentBlockX,
							currentBlockY, currentblockZ,
							(int) (curBlockDamageMP * 10.0F) - 1);
				}
			}
		}
	}

	/**
	 * If modified to return true, the player spins around slowly around (0,
	 * 68.5, 0). The GUI is disabled, the view is set to first person, and both
	 * chat and menu are disabled. Unless the server is modified to ignore
	 * illegal stances, attempting to enter a world at all will result in an
	 * immediate kick due to an illegal stance. Appears to be left-over debug,
	 * or demo code.
	 */
	public boolean enableEverythingIsScrewedUpMode() {
		return false;
	}

	/**
	 * true for hitting entities far away.
	 */
	public boolean extendedReach() {
		return currentGameType.isCreative();
	}

	/**
	 * Flips the player around. Args: player
	 */
	public void flipPlayer(EntityPlayer p_78745_1_) {
		p_78745_1_.rotationYaw = -180.0F;
	}

	public boolean func_110738_j() {
		return mc.thePlayer.isRiding()
				&& mc.thePlayer.ridingEntity instanceof EntityHorse;
	}

	public EntityClientPlayerMP func_147493_a(World p_147493_1_,
			StatFileWriter p_147493_2_) {
		return new EntityClientPlayerMP(mc, p_147493_1_, mc.getSession(),
				netClientHandler, p_147493_2_);
	}

	public boolean gameIsSurvivalOrAdventure() {
		return currentGameType.isSurvivalOrAdventure();
	}

	/**
	 * player reach distance = 4F
	 */
	public float getBlockReachDistance() {
		return currentGameType.isCreative() ? 5.0F : 4.5F;
	}

	/**
	 * Send packet to server - player is interacting with another entity (left
	 * click)
	 */
	public boolean interactWithEntitySendPacket(EntityPlayer p_78768_1_,
			Entity p_78768_2_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C02PacketUseEntity(p_78768_2_,
				C02PacketUseEntity.Action.INTERACT));
		return p_78768_1_.interactWith(p_78768_2_);
	}

	/**
	 * returns true if player is in creative mode
	 */
	public boolean isInCreativeMode() {
		return currentGameType.isCreative();
	}

	/**
	 * Checks if the player is not creative, used for checking if it should
	 * break a block instantly
	 */
	public boolean isNotCreative() {
		return !currentGameType.isCreative();
	}

	/**
	 * Called when a player damages a block and updates damage counters
	 */
	public void onPlayerDamageBlock(int p_78759_1_, int p_78759_2_,
			int p_78759_3_, int p_78759_4_) {
		syncCurrentPlayItem();
		final EventBlockBreaking event = new EventBlockBreaking(p_78759_1_,
				p_78759_2_, p_78759_3_, p_78759_4_, 5, 1);
		Client.getEventManager().call(event);

		if (blockHitDelay > 0) {
			--blockHitDelay;
		} else if (currentGameType.isCreative()) {
			blockHitDelay = event.getDelay();
			netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0,
					p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_));
			clickBlockCreative(mc, this, p_78759_1_, p_78759_2_, p_78759_3_,
					p_78759_4_);
		} else {
			if (sameToolAndBlock(p_78759_1_, p_78759_2_, p_78759_3_)) {
				final Block var5 = mc.theWorld.getBlock(p_78759_1_, p_78759_2_,
						p_78759_3_);

				if (var5.getMaterial() == Material.air) {
					isHittingBlock = false;
					return;
				}

				curBlockDamageMP += var5.getPlayerRelativeBlockHardness(
						mc.thePlayer, mc.theWorld, p_78759_1_, p_78759_2_,
						p_78759_3_)
						* event.getDamage();

				if (stepSoundTickCounter % 4.0F == 0.0F) {
					mc.getSoundHandler()
							.playSound(
									new PositionedSoundRecord(
											new ResourceLocation(var5.stepSound
													.func_150498_e()),
											(var5.stepSound.func_150497_c() + 1.0F) / 8.0F,
											var5.stepSound.func_150494_d() * 0.5F,
											p_78759_1_ + 0.5F,
											p_78759_2_ + 0.5F,
											p_78759_3_ + 0.5F));
				}

				++stepSoundTickCounter;

				if (curBlockDamageMP >= 1.0F) {
					isHittingBlock = false;
					netClientHandler.addToSendQueue(new C07PacketPlayerDigging(
							2, p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_));
					onPlayerDestroyBlock(p_78759_1_, p_78759_2_, p_78759_3_,
							p_78759_4_);
					curBlockDamageMP = 0.0F;
					stepSoundTickCounter = 0.0F;
					blockHitDelay = event.getDelay();
				}

				mc.theWorld.destroyBlockInWorldPartially(
						mc.thePlayer.getEntityId(), currentBlockX,
						currentBlockY, currentblockZ,
						(int) (curBlockDamageMP * 10.0F) - 1);
			} else {
				clickBlock(p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_);
			}
		}
	}

	/**
	 * Called when a player completes the destruction of a block
	 */
	public boolean onPlayerDestroyBlock(int p_78751_1_, int p_78751_2_,
			int p_78751_3_, int p_78751_4_) {
		if (currentGameType.isAdventure()
				&& !mc.thePlayer.isCurrentToolAdventureModeExempt(p_78751_1_,
						p_78751_2_, p_78751_3_))
			return false;
		else if (currentGameType.isCreative()
				&& mc.thePlayer.getHeldItem() != null
				&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
			return false;
		else {
			final WorldClient var5 = mc.theWorld;
			final Block var6 = var5
					.getBlock(p_78751_1_, p_78751_2_, p_78751_3_);

			if (var6.getMaterial() == Material.air)
				return false;
			else {
				var5.playAuxSFX(
						2001,
						p_78751_1_,
						p_78751_2_,
						p_78751_3_,
						Block.getIdFromBlock(var6)
								+ (var5.getBlockMetadata(p_78751_1_,
										p_78751_2_, p_78751_3_) << 12));
				final int var7 = var5.getBlockMetadata(p_78751_1_, p_78751_2_,
						p_78751_3_);
				final boolean var8 = var5.setBlockToAir(p_78751_1_, p_78751_2_,
						p_78751_3_);

				if (var8) {
					var6.onBlockDestroyedByPlayer(var5, p_78751_1_, p_78751_2_,
							p_78751_3_, var7);
				}

				currentBlockY = -1;

				if (!currentGameType.isCreative()) {
					final ItemStack var9 = mc.thePlayer
							.getCurrentEquippedItem();

					if (var9 != null) {
						var9.func_150999_a(var5, var6, p_78751_1_, p_78751_2_,
								p_78751_3_, mc.thePlayer);

						if (var9.stackSize == 0) {
							mc.thePlayer.destroyCurrentEquippedItem();
						}
					}
				}

				return var8;
			}
		}
	}

	/**
	 * Handles a players right click. Args: player, world, x, y, z, side, hitVec
	 */
	public boolean onPlayerRightClick(EntityPlayer p_78760_1_,
			World p_78760_2_, ItemStack p_78760_3_, int p_78760_4_,
			int p_78760_5_, int p_78760_6_, int p_78760_7_, Vec3 p_78760_8_) {
		syncCurrentPlayItem();
		final float var9 = (float) p_78760_8_.xCoord - p_78760_4_;
		final float var10 = (float) p_78760_8_.yCoord - p_78760_5_;
		final float var11 = (float) p_78760_8_.zCoord - p_78760_6_;
		boolean var12 = false;

		if ((!p_78760_1_.isSneaking() || p_78760_1_.getHeldItem() == null)
				&& p_78760_2_.getBlock(p_78760_4_, p_78760_5_, p_78760_6_)
						.onBlockActivated(p_78760_2_, p_78760_4_, p_78760_5_,
								p_78760_6_, p_78760_1_, p_78760_7_, var9,
								var10, var11)) {
			var12 = true;
		}

		if (!var12 && p_78760_3_ != null
				&& p_78760_3_.getItem() instanceof ItemBlock) {
			final ItemBlock var13 = (ItemBlock) p_78760_3_.getItem();

			if (!var13.func_150936_a(p_78760_2_, p_78760_4_, p_78760_5_,
					p_78760_6_, p_78760_7_, p_78760_1_, p_78760_3_))
				return false;
		}

		netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(
				p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_,
				p_78760_1_.inventory.getCurrentItem(), var9, var10, var11));

		if (var12)
			return true;
		else if (p_78760_3_ == null)
			return false;
		else if (currentGameType.isCreative()) {
			final int var16 = p_78760_3_.getItemDamage();
			final int var14 = p_78760_3_.stackSize;
			final boolean var15 = p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_,
					p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_,
					var9, var10, var11);
			p_78760_3_.setItemDamage(var16);
			p_78760_3_.stackSize = var14;
			return var15;
		} else
			return p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_, p_78760_2_,
					p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, var9,
					var10, var11);
	}

	public void onStoppedUsingItem(EntityPlayer p_78766_1_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C07PacketPlayerDigging(5, 0, 0, 0,
				255));
		p_78766_1_.stopUsingItem();
	}

	/**
	 * Resets current block damage and isHittingBlock
	 */
	public void resetBlockRemoving() {
		if (isHittingBlock) {
			netClientHandler.addToSendQueue(new C07PacketPlayerDigging(1,
					currentBlockX, currentBlockY, currentblockZ, -1));
		}

		isHittingBlock = false;
		curBlockDamageMP = 0.0F;
		mc.theWorld.destroyBlockInWorldPartially(mc.thePlayer.getEntityId(),
				currentBlockX, currentBlockY, currentblockZ, -1);
	}

	private boolean sameToolAndBlock(int p_85182_1_, int p_85182_2_,
			int p_85182_3_) {
		final ItemStack var4 = mc.thePlayer.getHeldItem();
		boolean var5 = currentItemHittingBlock == null && var4 == null;

		if (currentItemHittingBlock != null && var4 != null) {
			var5 = var4.getItem() == currentItemHittingBlock.getItem()
					&& ItemStack.areItemStackTagsEqual(var4,
							currentItemHittingBlock)
					&& (var4.isItemStackDamageable() || var4.getItemDamage() == currentItemHittingBlock
							.getItemDamage());
		}

		return p_85182_1_ == currentBlockX && p_85182_2_ == currentBlockY
				&& p_85182_3_ == currentblockZ && var5;
	}

	/**
	 * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to
	 * send a packet indicating the enchantment action the player has taken.
	 */
	public void sendEnchantPacket(int p_78756_1_, int p_78756_2_) {
		netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_,
				p_78756_2_));
	}

	/**
	 * Sends a Packet107 to the server to drop the item on the ground
	 */
	public void sendPacketDropItem(ItemStack p_78752_1_) {
		if (currentGameType.isCreative() && p_78752_1_ != null) {
			netClientHandler
					.addToSendQueue(new C10PacketCreativeInventoryAction(-1,
							p_78752_1_));
		}
	}

	/**
	 * Used in PlayerControllerMP to update the server with an ItemStack in a
	 * slot.
	 */
	public void sendSlotPacket(ItemStack p_78761_1_, int p_78761_2_) {
		if (currentGameType.isCreative()) {
			netClientHandler
					.addToSendQueue(new C10PacketCreativeInventoryAction(
							p_78761_2_, p_78761_1_));
		}
	}

	/**
	 * Notifies the server of things like consuming food, etc...
	 */
	public boolean sendUseItem(EntityPlayer p_78769_1_, World p_78769_2_,
			ItemStack p_78769_3_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(-1,
				-1, -1, 255, p_78769_1_.inventory.getCurrentItem(), 0.0F, 0.0F,
				0.0F));
		final int var4 = p_78769_3_.stackSize;
		final ItemStack var5 = p_78769_3_.useItemRightClick(p_78769_2_,
				p_78769_1_);

		if (var5 == p_78769_3_ && (var5 == null || var5.stackSize == var4))
			return false;
		else {
			p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = var5;

			if (var5.stackSize == 0) {
				p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = null;
			}

			return true;
		}
	}

	/**
	 * Sets the game type for the player.
	 */
	public void setGameType(WorldSettings.GameType p_78746_1_) {
		currentGameType = p_78746_1_;
		currentGameType.configurePlayerCapabilities(mc.thePlayer.capabilities);
	}

	/**
	 * Sets player capabilities depending on current gametype. params: player
	 */
	public void setPlayerCapabilities(EntityPlayer p_78748_1_) {
		currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
	}

	public boolean shouldDrawHUD() {
		return currentGameType.isSurvivalOrAdventure();
	}

	/**
	 * Syncs the current player item with the server
	 */
	public void syncCurrentPlayItem() {
		final int var1 = mc.thePlayer.inventory.currentItem;

		if (var1 != currentPlayerItem) {
			currentPlayerItem = var1;
			netClientHandler.addToSendQueue(new C09PacketHeldItemChange(
					currentPlayerItem));
		}
	}

	public void updateController() {
		syncCurrentPlayItem();

		if (netClientHandler.getNetworkManager().isChannelOpen()) {
			netClientHandler.getNetworkManager().processReceivedPackets();
		} else if (netClientHandler.getNetworkManager().getExitMessage() != null) {
			netClientHandler
					.getNetworkManager()
					.getNetHandler()
					.onDisconnect(
							netClientHandler.getNetworkManager()
									.getExitMessage());
		} else {
			netClientHandler
					.getNetworkManager()
					.getNetHandler()
					.onDisconnect(
							new ChatComponentText("Disconnected from server"));
		}
	}

	public ItemStack windowClick(int p_78753_1_, int p_78753_2_,
			int p_78753_3_, int p_78753_4_, EntityPlayer p_78753_5_) {
		final short var6 = p_78753_5_.openContainer
				.getNextTransactionID(p_78753_5_.inventory);
		final ItemStack var7 = p_78753_5_.openContainer.slotClick(p_78753_2_,
				p_78753_3_, p_78753_4_, p_78753_5_);
		netClientHandler.addToSendQueue(new C0EPacketClickWindow(p_78753_1_,
				p_78753_2_, p_78753_3_, p_78753_4_, var7, var6));
		return var7;
	}
}
