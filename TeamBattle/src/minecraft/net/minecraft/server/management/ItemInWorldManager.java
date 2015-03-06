package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;

public class ItemInWorldManager {
	private int curblockDamage;

	private int durabilityRemainingOnBlock;
	private WorldSettings.GameType gameType;

	private int initialBlockDamage;
	private int initialDamage;
	/** True if the player is destroying a block */
	private boolean isDestroyingBlock;
	private int partiallyDestroyedBlockX;
	private int partiallyDestroyedBlockY;
	private int partiallyDestroyedBlockZ;

	private int posX;
	private int posY;
	private int posZ;
	/**
	 * Set to true when the "finished destroying block" packet is received but
	 * the block wasn't fully damaged yet. The block will not be destroyed while
	 * this is false.
	 */
	private boolean receivedFinishDiggingPacket;
	/** The world object that this object is connected to. */
	public World theWorld;
	/** The EntityPlayerMP object that this object is connected to. */
	public EntityPlayerMP thisPlayerMP;

	public ItemInWorldManager(World p_i1524_1_) {
		gameType = WorldSettings.GameType.NOT_SET;
		durabilityRemainingOnBlock = -1;
		theWorld = p_i1524_1_;
	}

	/**
	 * Activate the clicked on block, otherwise use the held item. Args: player,
	 * world, itemStack, x, y, z, side, xOffset, yOffset, zOffset
	 */
	public boolean activateBlockOrUseItem(EntityPlayer p_73078_1_,
			World p_73078_2_, ItemStack p_73078_3_, int p_73078_4_,
			int p_73078_5_, int p_73078_6_, int p_73078_7_, float p_73078_8_,
			float p_73078_9_, float p_73078_10_) {
		if ((!p_73078_1_.isSneaking() || p_73078_1_.getHeldItem() == null)
				&& p_73078_2_.getBlock(p_73078_4_, p_73078_5_, p_73078_6_)
						.onBlockActivated(p_73078_2_, p_73078_4_, p_73078_5_,
								p_73078_6_, p_73078_1_, p_73078_7_, p_73078_8_,
								p_73078_9_, p_73078_10_))
			return true;
		else if (p_73078_3_ == null)
			return false;
		else if (isCreative()) {
			final int var11 = p_73078_3_.getItemDamage();
			final int var12 = p_73078_3_.stackSize;
			final boolean var13 = p_73078_3_.tryPlaceItemIntoWorld(p_73078_1_,
					p_73078_2_, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_7_,
					p_73078_8_, p_73078_9_, p_73078_10_);
			p_73078_3_.setItemDamage(var11);
			p_73078_3_.stackSize = var12;
			return var13;
		} else
			return p_73078_3_.tryPlaceItemIntoWorld(p_73078_1_, p_73078_2_,
					p_73078_4_, p_73078_5_, p_73078_6_, p_73078_7_, p_73078_8_,
					p_73078_9_, p_73078_10_);
	}

	/**
	 * note: this ignores the pars passed in and continues to destroy the
	 * onClickedBlock
	 */
	public void cancelDestroyingBlock(int p_73073_1_, int p_73073_2_,
			int p_73073_3_) {
		isDestroyingBlock = false;
		theWorld.destroyBlockInWorldPartially(thisPlayerMP.getEntityId(),
				partiallyDestroyedBlockX, partiallyDestroyedBlockY,
				partiallyDestroyedBlockZ, -1);
	}

	public WorldSettings.GameType getGameType() {
		return gameType;
	}

	/**
	 * if the gameType is currently NOT_SET then change it to par1
	 */
	public void initializeGameType(WorldSettings.GameType p_73077_1_) {
		if (gameType == WorldSettings.GameType.NOT_SET) {
			gameType = p_73077_1_;
		}

		setGameType(gameType);
	}

	/**
	 * Get if we are in creative game mode.
	 */
	public boolean isCreative() {
		return gameType.isCreative();
	}

	/**
	 * if not creative, it calls destroyBlockInWorldPartially untill the block
	 * is broken first. par4 is the specific side. tryHarvestBlock can also be
	 * the result of this call
	 */
	public void onBlockClicked(int p_73074_1_, int p_73074_2_, int p_73074_3_,
			int p_73074_4_) {
		if (!gameType.isAdventure()
				|| thisPlayerMP.isCurrentToolAdventureModeExempt(p_73074_1_,
						p_73074_2_, p_73074_3_)) {
			if (isCreative()) {
				if (!theWorld.extinguishFire((EntityPlayer) null, p_73074_1_,
						p_73074_2_, p_73074_3_, p_73074_4_)) {
					tryHarvestBlock(p_73074_1_, p_73074_2_, p_73074_3_);
				}
			} else {
				theWorld.extinguishFire((EntityPlayer) null, p_73074_1_,
						p_73074_2_, p_73074_3_, p_73074_4_);
				initialDamage = curblockDamage;
				float var5 = 1.0F;
				final Block var6 = theWorld.getBlock(p_73074_1_, p_73074_2_,
						p_73074_3_);

				if (var6.getMaterial() != Material.air) {
					var6.onBlockClicked(theWorld, p_73074_1_, p_73074_2_,
							p_73074_3_, thisPlayerMP);
					var5 = var6.getPlayerRelativeBlockHardness(thisPlayerMP,
							thisPlayerMP.worldObj, p_73074_1_, p_73074_2_,
							p_73074_3_);
				}

				if (var6.getMaterial() != Material.air && var5 >= 1.0F) {
					tryHarvestBlock(p_73074_1_, p_73074_2_, p_73074_3_);
				} else {
					isDestroyingBlock = true;
					partiallyDestroyedBlockX = p_73074_1_;
					partiallyDestroyedBlockY = p_73074_2_;
					partiallyDestroyedBlockZ = p_73074_3_;
					final int var7 = (int) (var5 * 10.0F);
					theWorld.destroyBlockInWorldPartially(
							thisPlayerMP.getEntityId(), p_73074_1_, p_73074_2_,
							p_73074_3_, var7);
					durabilityRemainingOnBlock = var7;
				}
			}
		}
	}

	/**
	 * Removes a block and triggers the appropriate events
	 */
	private boolean removeBlock(int p_73079_1_, int p_73079_2_, int p_73079_3_) {
		final Block var4 = theWorld
				.getBlock(p_73079_1_, p_73079_2_, p_73079_3_);
		final int var5 = theWorld.getBlockMetadata(p_73079_1_, p_73079_2_,
				p_73079_3_);
		var4.onBlockHarvested(theWorld, p_73079_1_, p_73079_2_, p_73079_3_,
				var5, thisPlayerMP);
		final boolean var6 = theWorld.setBlockToAir(p_73079_1_, p_73079_2_,
				p_73079_3_);

		if (var6) {
			var4.onBlockDestroyedByPlayer(theWorld, p_73079_1_, p_73079_2_,
					p_73079_3_, var5);
		}

		return var6;
	}

	public void setGameType(WorldSettings.GameType p_73076_1_) {
		gameType = p_73076_1_;
		p_73076_1_.configurePlayerCapabilities(thisPlayerMP.capabilities);
		thisPlayerMP.sendPlayerAbilities();
	}

	/**
	 * Sets the world instance.
	 */
	public void setWorld(WorldServer p_73080_1_) {
		theWorld = p_73080_1_;
	}

	/**
	 * Attempts to harvest a block at the given coordinate
	 */
	public boolean tryHarvestBlock(int p_73084_1_, int p_73084_2_,
			int p_73084_3_) {
		if (gameType.isAdventure()
				&& !thisPlayerMP.isCurrentToolAdventureModeExempt(p_73084_1_,
						p_73084_2_, p_73084_3_))
			return false;
		else if (gameType.isCreative() && thisPlayerMP.getHeldItem() != null
				&& thisPlayerMP.getHeldItem().getItem() instanceof ItemSword)
			return false;
		else {
			final Block var4 = theWorld.getBlock(p_73084_1_, p_73084_2_,
					p_73084_3_);
			final int var5 = theWorld.getBlockMetadata(p_73084_1_, p_73084_2_,
					p_73084_3_);
			theWorld.playAuxSFXAtEntity(
					thisPlayerMP,
					2001,
					p_73084_1_,
					p_73084_2_,
					p_73084_3_,
					Block.getIdFromBlock(var4)
							+ (theWorld.getBlockMetadata(p_73084_1_,
									p_73084_2_, p_73084_3_) << 12));
			final boolean var6 = removeBlock(p_73084_1_, p_73084_2_, p_73084_3_);

			if (isCreative()) {
				thisPlayerMP.playerNetServerHandler
						.sendPacket(new S23PacketBlockChange(p_73084_1_,
								p_73084_2_, p_73084_3_, theWorld));
			} else {
				final ItemStack var7 = thisPlayerMP.getCurrentEquippedItem();
				final boolean var8 = thisPlayerMP.canHarvestBlock(var4);

				if (var7 != null) {
					var7.func_150999_a(theWorld, var4, p_73084_1_, p_73084_2_,
							p_73084_3_, thisPlayerMP);

					if (var7.stackSize == 0) {
						thisPlayerMP.destroyCurrentEquippedItem();
					}
				}

				if (var6 && var8) {
					var4.harvestBlock(theWorld, thisPlayerMP, p_73084_1_,
							p_73084_2_, p_73084_3_, var5);
				}
			}

			return var6;
		}
	}

	/**
	 * Attempts to right-click use an item by the given EntityPlayer in the
	 * given World
	 */
	public boolean tryUseItem(EntityPlayer p_73085_1_, World p_73085_2_,
			ItemStack p_73085_3_) {
		final int var4 = p_73085_3_.stackSize;
		final int var5 = p_73085_3_.getItemDamage();
		final ItemStack var6 = p_73085_3_.useItemRightClick(p_73085_2_,
				p_73085_1_);

		if (var6 == p_73085_3_
				&& (var6 == null || var6.stackSize == var4
						&& var6.getMaxItemUseDuration() <= 0
						&& var6.getItemDamage() == var5))
			return false;
		else {
			p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = var6;

			if (isCreative()) {
				var6.stackSize = var4;

				if (var6.isItemStackDamageable()) {
					var6.setItemDamage(var5);
				}
			}

			if (var6.stackSize == 0) {
				p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = null;
			}

			if (!p_73085_1_.isUsingItem()) {
				((EntityPlayerMP) p_73085_1_)
						.sendContainerToPlayer(p_73085_1_.inventoryContainer);
			}

			return true;
		}
	}

	public void uncheckedTryHarvestBlock(int p_73082_1_, int p_73082_2_,
			int p_73082_3_) {
		if (p_73082_1_ == partiallyDestroyedBlockX
				&& p_73082_2_ == partiallyDestroyedBlockY
				&& p_73082_3_ == partiallyDestroyedBlockZ) {
			final int var4 = curblockDamage - initialDamage;
			final Block var5 = theWorld.getBlock(p_73082_1_, p_73082_2_,
					p_73082_3_);

			if (var5.getMaterial() != Material.air) {
				final float var6 = var5.getPlayerRelativeBlockHardness(
						thisPlayerMP, thisPlayerMP.worldObj, p_73082_1_,
						p_73082_2_, p_73082_3_)
						* (var4 + 1);

				if (var6 >= 0.7F) {
					isDestroyingBlock = false;
					theWorld.destroyBlockInWorldPartially(
							thisPlayerMP.getEntityId(), p_73082_1_, p_73082_2_,
							p_73082_3_, -1);
					tryHarvestBlock(p_73082_1_, p_73082_2_, p_73082_3_);
				} else if (!receivedFinishDiggingPacket) {
					isDestroyingBlock = false;
					receivedFinishDiggingPacket = true;
					posX = p_73082_1_;
					posY = p_73082_2_;
					posZ = p_73082_3_;
					initialBlockDamage = initialDamage;
				}
			}
		}
	}

	public void updateBlockRemoving() {
		++curblockDamage;
		float var3;
		int var4;

		if (receivedFinishDiggingPacket) {
			final int var1 = curblockDamage - initialBlockDamage;
			final Block var2 = theWorld.getBlock(posX, posY, posZ);

			if (var2.getMaterial() == Material.air) {
				receivedFinishDiggingPacket = false;
			} else {
				var3 = var2.getPlayerRelativeBlockHardness(thisPlayerMP,
						thisPlayerMP.worldObj, posX, posY, posZ) * (var1 + 1);
				var4 = (int) (var3 * 10.0F);

				if (var4 != durabilityRemainingOnBlock) {
					theWorld.destroyBlockInWorldPartially(
							thisPlayerMP.getEntityId(), posX, posY, posZ, var4);
					durabilityRemainingOnBlock = var4;
				}

				if (var3 >= 1.0F) {
					receivedFinishDiggingPacket = false;
					tryHarvestBlock(posX, posY, posZ);
				}
			}
		} else if (isDestroyingBlock) {
			final Block var5 = theWorld.getBlock(partiallyDestroyedBlockX,
					partiallyDestroyedBlockY, partiallyDestroyedBlockZ);

			if (var5.getMaterial() == Material.air) {
				theWorld.destroyBlockInWorldPartially(
						thisPlayerMP.getEntityId(), partiallyDestroyedBlockX,
						partiallyDestroyedBlockY, partiallyDestroyedBlockZ, -1);
				durabilityRemainingOnBlock = -1;
				isDestroyingBlock = false;
			} else {
				final int var6 = curblockDamage - initialDamage;
				var3 = var5.getPlayerRelativeBlockHardness(thisPlayerMP,
						thisPlayerMP.worldObj, partiallyDestroyedBlockX,
						partiallyDestroyedBlockY, partiallyDestroyedBlockZ)
						* (var6 + 1);
				var4 = (int) (var3 * 10.0F);

				if (var4 != durabilityRemainingOnBlock) {
					theWorld.destroyBlockInWorldPartially(
							thisPlayerMP.getEntityId(),
							partiallyDestroyedBlockX, partiallyDestroyedBlockY,
							partiallyDestroyedBlockZ, var4);
					durabilityRemainingOnBlock = var4;
				}
			}
		}
	}
}
