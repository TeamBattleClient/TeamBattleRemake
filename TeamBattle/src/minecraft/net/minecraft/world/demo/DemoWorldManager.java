package net.minecraft.world.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class DemoWorldManager extends ItemInWorldManager {
	private boolean demoTimeExpired;
	private int field_73102_f;
	private int field_73104_e;
	private boolean field_73105_c;

	public DemoWorldManager(World p_i1513_1_) {
		super(p_i1513_1_);
	}

	/**
	 * Activate the clicked on block, otherwise use the held item. Args: player,
	 * world, itemStack, x, y, z, side, xOffset, yOffset, zOffset
	 */
	@Override
	public boolean activateBlockOrUseItem(EntityPlayer p_73078_1_,
			World p_73078_2_, ItemStack p_73078_3_, int p_73078_4_,
			int p_73078_5_, int p_73078_6_, int p_73078_7_, float p_73078_8_,
			float p_73078_9_, float p_73078_10_) {
		if (demoTimeExpired) {
			sendDemoReminder();
			return false;
		} else
			return super.activateBlockOrUseItem(p_73078_1_, p_73078_2_,
					p_73078_3_, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_7_,
					p_73078_8_, p_73078_9_, p_73078_10_);
	}

	/**
	 * if not creative, it calls destroyBlockInWorldPartially untill the block
	 * is broken first. par4 is the specific side. tryHarvestBlock can also be
	 * the result of this call
	 */
	@Override
	public void onBlockClicked(int p_73074_1_, int p_73074_2_, int p_73074_3_,
			int p_73074_4_) {
		if (demoTimeExpired) {
			sendDemoReminder();
		} else {
			super.onBlockClicked(p_73074_1_, p_73074_2_, p_73074_3_, p_73074_4_);
		}
	}

	/**
	 * Sends a message to the player reminding them that this is the demo
	 * version
	 */
	private void sendDemoReminder() {
		if (field_73104_e > 100) {
			thisPlayerMP.addChatMessage(new ChatComponentTranslation(
					"demo.reminder", new Object[0]));
			field_73104_e = 0;
		}
	}

	/**
	 * Attempts to harvest a block at the given coordinate
	 */
	@Override
	public boolean tryHarvestBlock(int p_73084_1_, int p_73084_2_,
			int p_73084_3_) {
		return demoTimeExpired ? false : super.tryHarvestBlock(p_73084_1_,
				p_73084_2_, p_73084_3_);
	}

	/**
	 * Attempts to right-click use an item by the given EntityPlayer in the
	 * given World
	 */
	@Override
	public boolean tryUseItem(EntityPlayer p_73085_1_, World p_73085_2_,
			ItemStack p_73085_3_) {
		if (demoTimeExpired) {
			sendDemoReminder();
			return false;
		} else
			return super.tryUseItem(p_73085_1_, p_73085_2_, p_73085_3_);
	}

	@Override
	public void uncheckedTryHarvestBlock(int p_73082_1_, int p_73082_2_,
			int p_73082_3_) {
		if (!demoTimeExpired) {
			super.uncheckedTryHarvestBlock(p_73082_1_, p_73082_2_, p_73082_3_);
		}
	}

	@Override
	public void updateBlockRemoving() {
		super.updateBlockRemoving();
		++field_73102_f;
		final long var1 = theWorld.getTotalWorldTime();
		final long var3 = var1 / 24000L + 1L;

		if (!field_73105_c && field_73102_f > 20) {
			field_73105_c = true;
			thisPlayerMP.playerNetServerHandler
					.sendPacket(new S2BPacketChangeGameState(5, 0.0F));
		}

		demoTimeExpired = var1 > 120500L;

		if (demoTimeExpired) {
			++field_73104_e;
		}

		if (var1 % 24000L == 500L) {
			if (var3 <= 6L) {
				thisPlayerMP.addChatMessage(new ChatComponentTranslation(
						"demo.day." + var3, new Object[0]));
			}
		} else if (var3 == 1L) {
			if (var1 == 100L) {
				thisPlayerMP.playerNetServerHandler
						.sendPacket(new S2BPacketChangeGameState(5, 101.0F));
			} else if (var1 == 175L) {
				thisPlayerMP.playerNetServerHandler
						.sendPacket(new S2BPacketChangeGameState(5, 102.0F));
			} else if (var1 == 250L) {
				thisPlayerMP.playerNetServerHandler
						.sendPacket(new S2BPacketChangeGameState(5, 103.0F));
			}
		} else if (var3 == 5L && var1 % 24000L == 22000L) {
			thisPlayerMP.addChatMessage(new ChatComponentTranslation(
					"demo.day.warning", new Object[0]));
		}
	}
}
