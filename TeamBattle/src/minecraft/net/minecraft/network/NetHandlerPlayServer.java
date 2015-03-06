package net.minecraft.network;

import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class NetHandlerPlayServer implements INetHandlerPlayServer {
	static final class SwitchEnumState {
		static final int[] field_151290_a = new int[C16PacketClientStatus.EnumState
				.values().length];

		static {
			try {
				field_151290_a[C16PacketClientStatus.EnumState.PERFORM_RESPAWN
						.ordinal()] = 1;
			} catch (final NoSuchFieldError var3) {
				;
			}

			try {
				field_151290_a[C16PacketClientStatus.EnumState.REQUEST_STATS
						.ordinal()] = 2;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				field_151290_a[C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT
						.ordinal()] = 3;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	private static Random field_147376_j = new Random();
	private static final Logger logger = LogManager.getLogger();
	/**
	 * Incremented by 20 each time a user sends a chat message, decreased by one
	 * every tick. Non-ops kicked when over 200
	 */
	private int chatSpamThresholdCount;
	private final IntHashMap field_147372_n = new IntHashMap();

	private int field_147375_m;
	private long field_147377_k;
	private int field_147378_h;
	private long field_147379_i;
	/**
	 * Used to keep track of how the player is floating while gamerules should
	 * prevent that. Surpassing 80 ticks means kick
	 */
	private int floatingTickCount;

	private boolean hasMoved = true;
	private double lastPosX;
	private double lastPosY;
	private double lastPosZ;
	public final NetworkManager netManager;
	private int networkTickCount;
	public EntityPlayerMP playerEntity;

	private final MinecraftServer serverController;

	public NetHandlerPlayServer(MinecraftServer p_i1530_1_,
			NetworkManager p_i1530_2_, EntityPlayerMP p_i1530_3_) {
		serverController = p_i1530_1_;
		netManager = p_i1530_2_;
		p_i1530_2_.setNetHandler(this);
		playerEntity = p_i1530_3_;
		p_i1530_3_.playerNetServerHandler = this;
	}

	public NetworkManager func_147362_b() {
		return netManager;
	}

	private long func_147363_d() {
		return System.nanoTime() / 1000000L;
	}

	/**
	 * Handle commands that start with a /
	 */
	private void handleSlashCommand(String p_147361_1_) {
		serverController.getCommandManager().executeCommand(playerEntity,
				p_147361_1_);
	}

	/**
	 * Kick a player from the server with a reason
	 */
	public void kickPlayerFromServer(String p_147360_1_) {
		final ChatComponentText var2 = new ChatComponentText(p_147360_1_);
		netManager.scheduleOutboundPacket(new S40PacketDisconnect(var2),
				new GenericFutureListener[] { new GenericFutureListener() {

					@Override
					public void operationComplete(Future p_operationComplete_1_) {
						netManager.closeChannel(var2);
					}
				} });
		netManager.disableAutoRead();
	}

	/**
	 * Allows validation of the connection state transition. Parameters: from,
	 * to (connection state). Typically throws IllegalStateException or
	 * UnsupportedOperationException if validation fails
	 */
	@Override
	public void onConnectionStateTransition(EnumConnectionState p_147232_1_,
			EnumConnectionState p_147232_2_) {
		if (p_147232_2_ != EnumConnectionState.PLAY)
			throw new IllegalStateException("Unexpected change in protocol!");
	}

	/**
	 * Invoked when disconnecting, the parameter is a ChatComponent describing
	 * the reason for termination
	 */
	@Override
	public void onDisconnect(IChatComponent p_147231_1_) {
		logger.info(playerEntity.getCommandSenderName() + " lost connection: "
				+ p_147231_1_);
		serverController.func_147132_au();
		final ChatComponentTranslation var2 = new ChatComponentTranslation(
				"multiplayer.player.left",
				new Object[] { playerEntity.func_145748_c_() });
		var2.getChatStyle().setColor(EnumChatFormatting.YELLOW);
		serverController.getConfigurationManager().func_148539_a(var2);
		playerEntity.mountEntityAndWakeUp();
		serverController.getConfigurationManager()
				.playerLoggedOut(playerEntity);

		if (serverController.isSinglePlayer()
				&& playerEntity.getCommandSenderName().equals(
						serverController.getServerOwner())) {
			logger.info("Stopping singleplayer server as player logged out");
			serverController.initiateShutdown();
		}
	}

	/**
	 * For scheduled network tasks. Used in NetHandlerPlayServer to send
	 * keep-alive packets and in NetHandlerLoginServer for a login-timeout
	 */
	@Override
	public void onNetworkTick() {
		++networkTickCount;
		serverController.theProfiler.startSection("keepAlive");

		if (networkTickCount - field_147377_k > 40L) {
			field_147377_k = networkTickCount;
			field_147379_i = func_147363_d();
			field_147378_h = (int) field_147379_i;
			sendPacket(new S00PacketKeepAlive(field_147378_h));
		}

		if (chatSpamThresholdCount > 0) {
			--chatSpamThresholdCount;
		}

		if (field_147375_m > 0) {
			--field_147375_m;
		}

		if (playerEntity.func_154331_x() > 0L
				&& serverController.func_143007_ar() > 0
				&& MinecraftServer.getSystemTimeMillis()
						- playerEntity.func_154331_x() > serverController
						.func_143007_ar() * 1000 * 60) {
			kickPlayerFromServer("You have been idle for too long!");
		}
	}

	/**
	 * Processes the player swinging its held item
	 */
	@Override
	public void processAnimation(C0APacketAnimation p_147350_1_) {
		playerEntity.func_143004_u();

		if (p_147350_1_.func_149421_d() == 1) {
			playerEntity.swingItem();
		}
	}

	/**
	 * Process chat messages (broadcast back to clients) and commands (executes)
	 */
	@Override
	public void processChatMessage(C01PacketChatMessage p_147354_1_) {
		if (playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN) {
			final ChatComponentTranslation var4 = new ChatComponentTranslation(
					"chat.cannotSend", new Object[0]);
			var4.getChatStyle().setColor(EnumChatFormatting.RED);
			sendPacket(new S02PacketChat(var4));
		} else {
			playerEntity.func_143004_u();
			String var2 = p_147354_1_.func_149439_c();
			var2 = StringUtils.normalizeSpace(var2);

			for (int var3 = 0; var3 < var2.length(); ++var3) {
				if (!ChatAllowedCharacters
						.isAllowedCharacter(var2.charAt(var3))) {
					kickPlayerFromServer("Illegal characters in chat");
					return;
				}
			}

			if (var2.startsWith("/")) {
				handleSlashCommand(var2);
			} else {
				final ChatComponentTranslation var5 = new ChatComponentTranslation(
						"chat.type.text", new Object[] {
								playerEntity.func_145748_c_(), var2 });
				serverController.getConfigurationManager().func_148544_a(var5,
						false);
			}

			chatSpamThresholdCount += 20;

			if (chatSpamThresholdCount > 200
					&& !serverController.getConfigurationManager()
							.func_152596_g(playerEntity.getGameProfile())) {
				kickPlayerFromServer("disconnect.spam");
			}
		}
	}

	/**
	 * Executes a container/inventory slot manipulation as indicated by the
	 * packet. Sends the serverside result if they didn't match the indicated
	 * result and prevents further manipulation by the player until he confirms
	 * that it has the same open container/inventory
	 */
	@Override
	public void processClickWindow(C0EPacketClickWindow p_147351_1_) {
		playerEntity.func_143004_u();

		if (playerEntity.openContainer.windowId == p_147351_1_.func_149548_c()
				&& playerEntity.openContainer
						.isPlayerNotUsingContainer(playerEntity)) {
			final ItemStack var2 = playerEntity.openContainer.slotClick(
					p_147351_1_.func_149544_d(), p_147351_1_.func_149543_e(),
					p_147351_1_.func_149542_h(), playerEntity);

			if (ItemStack.areItemStacksEqual(p_147351_1_.func_149546_g(), var2)) {
				playerEntity.playerNetServerHandler
						.sendPacket(new S32PacketConfirmTransaction(p_147351_1_
								.func_149548_c(), p_147351_1_.func_149547_f(),
								true));
				playerEntity.isChangingQuantityOnly = true;
				playerEntity.openContainer.detectAndSendChanges();
				playerEntity.updateHeldItem();
				playerEntity.isChangingQuantityOnly = false;
			} else {
				field_147372_n.addKey(playerEntity.openContainer.windowId,
						Short.valueOf(p_147351_1_.func_149547_f()));
				playerEntity.playerNetServerHandler
						.sendPacket(new S32PacketConfirmTransaction(p_147351_1_
								.func_149548_c(), p_147351_1_.func_149547_f(),
								false));
				playerEntity.openContainer.setPlayerIsPresent(playerEntity,
						false);
				final ArrayList var3 = new ArrayList();

				for (int var4 = 0; var4 < playerEntity.openContainer.inventorySlots
						.size(); ++var4) {
					var3.add(((Slot) playerEntity.openContainer.inventorySlots
							.get(var4)).getStack());
				}

				playerEntity.sendContainerAndContentsToPlayer(
						playerEntity.openContainer, var3);
			}
		}
	}

	/**
	 * Updates serverside copy of client settings: language, render distance,
	 * chat visibility, chat colours, difficulty, and whether to show the cape
	 */
	@Override
	public void processClientSettings(C15PacketClientSettings p_147352_1_) {
		playerEntity.func_147100_a(p_147352_1_);
	}

	/**
	 * Processes the client status updates: respawn attempt from player, opening
	 * statistics or achievements, or acquiring 'open inventory' achievement
	 */
	@Override
	public void processClientStatus(C16PacketClientStatus p_147342_1_) {
		playerEntity.func_143004_u();
		final C16PacketClientStatus.EnumState var2 = p_147342_1_
				.func_149435_c();

		switch (NetHandlerPlayServer.SwitchEnumState.field_151290_a[var2
				.ordinal()]) {
		case 1:
			if (playerEntity.playerConqueredTheEnd) {
				playerEntity = serverController.getConfigurationManager()
						.respawnPlayer(playerEntity, 0, true);
			} else if (playerEntity.getServerForPlayer().getWorldInfo()
					.isHardcoreModeEnabled()) {
				if (serverController.isSinglePlayer()
						&& playerEntity.getCommandSenderName().equals(
								serverController.getServerOwner())) {
					playerEntity.playerNetServerHandler
							.kickPlayerFromServer("You have died. Game over, man, it\'s game over!");
					serverController.deleteWorldAndStopServer();
				} else {
					final UserListBansEntry var3 = new UserListBansEntry(
							playerEntity.getGameProfile(), (Date) null,
							"(You just lost the game)", (Date) null,
							"Death in Hardcore");
					serverController.getConfigurationManager().func_152608_h()
							.func_152687_a(var3);
					playerEntity.playerNetServerHandler
							.kickPlayerFromServer("You have died. Game over, man, it\'s game over!");
				}
			} else {
				if (playerEntity.getHealth() > 0.0F)
					return;

				playerEntity = serverController.getConfigurationManager()
						.respawnPlayer(playerEntity, 0, false);
			}

			break;

		case 2:
			playerEntity.func_147099_x().func_150876_a(playerEntity);
			break;

		case 3:
			playerEntity.triggerAchievement(AchievementList.openInventory);
		}
	}

	/**
	 * Processes the client closing windows (container)
	 */
	@Override
	public void processCloseWindow(C0DPacketCloseWindow p_147356_1_) {
		playerEntity.closeContainer();
	}

	/**
	 * Received in response to the server requesting to confirm that the
	 * client-side open container matches the servers' after a mismatched
	 * container-slot manipulation. It will unlock the player's ability to
	 * manipulate the container contents
	 */
	@Override
	public void processConfirmTransaction(
			C0FPacketConfirmTransaction p_147339_1_) {
		final Short var2 = (Short) field_147372_n
				.lookup(playerEntity.openContainer.windowId);

		if (var2 != null
				&& p_147339_1_.func_149533_d() == var2.shortValue()
				&& playerEntity.openContainer.windowId == p_147339_1_
						.func_149532_c()
				&& !playerEntity.openContainer
						.isPlayerNotUsingContainer(playerEntity)) {
			playerEntity.openContainer.setPlayerIsPresent(playerEntity, true);
		}
	}

	/**
	 * Update the server with an ItemStack in a slot.
	 */
	@Override
	public void processCreativeInventoryAction(
			C10PacketCreativeInventoryAction p_147344_1_) {
		if (playerEntity.theItemInWorldManager.isCreative()) {
			final boolean var2 = p_147344_1_.func_149627_c() < 0;
			final ItemStack var3 = p_147344_1_.func_149625_d();
			final boolean var4 = p_147344_1_.func_149627_c() >= 1
					&& p_147344_1_.func_149627_c() < 36 + InventoryPlayer
							.getHotbarSize();
			final boolean var5 = var3 == null || var3.getItem() != null;
			final boolean var6 = var3 == null || var3.getItemDamage() >= 0
					&& var3.stackSize <= 64 && var3.stackSize > 0;

			if (var4 && var5 && var6) {
				if (var3 == null) {
					playerEntity.inventoryContainer.putStackInSlot(
							p_147344_1_.func_149627_c(), (ItemStack) null);
				} else {
					playerEntity.inventoryContainer.putStackInSlot(
							p_147344_1_.func_149627_c(), var3);
				}

				playerEntity.inventoryContainer.setPlayerIsPresent(
						playerEntity, true);
			} else if (var2 && var5 && var6 && field_147375_m < 200) {
				field_147375_m += 20;
				final EntityItem var7 = playerEntity
						.dropPlayerItemWithRandomChoice(var3, true);

				if (var7 != null) {
					var7.setAgeToCreativeDespawnTime();
				}
			}
		}
	}

	/**
	 * Enchants the item identified by the packet given some convoluted
	 * conditions (matching window, which should/shouldn't be in use?)
	 */
	@Override
	public void processEnchantItem(C11PacketEnchantItem p_147338_1_) {
		playerEntity.func_143004_u();

		if (playerEntity.openContainer.windowId == p_147338_1_.func_149539_c()
				&& playerEntity.openContainer
						.isPlayerNotUsingContainer(playerEntity)) {
			playerEntity.openContainer.enchantItem(playerEntity,
					p_147338_1_.func_149537_d());
			playerEntity.openContainer.detectAndSendChanges();
		}
	}

	/**
	 * Processes a range of action-types: sneaking, sprinting, waking from
	 * sleep, opening the inventory or setting jump height of the horse the
	 * player is riding
	 */
	@Override
	public void processEntityAction(C0BPacketEntityAction p_147357_1_) {
		playerEntity.func_143004_u();

		if (p_147357_1_.func_149513_d() == 1) {
			playerEntity.setSneaking(true);
		} else if (p_147357_1_.func_149513_d() == 2) {
			playerEntity.setSneaking(false);
		} else if (p_147357_1_.func_149513_d() == 4) {
			playerEntity.setSprinting(true);
		} else if (p_147357_1_.func_149513_d() == 5) {
			playerEntity.setSprinting(false);
		} else if (p_147357_1_.func_149513_d() == 3) {
			playerEntity.wakeUpPlayer(false, true, true);
			hasMoved = false;
		} else if (p_147357_1_.func_149513_d() == 6) {
			if (playerEntity.ridingEntity != null
					&& playerEntity.ridingEntity instanceof EntityHorse) {
				((EntityHorse) playerEntity.ridingEntity)
						.setJumpPower(p_147357_1_.func_149512_e());
			}
		} else if (p_147357_1_.func_149513_d() == 7
				&& playerEntity.ridingEntity != null
				&& playerEntity.ridingEntity instanceof EntityHorse) {
			((EntityHorse) playerEntity.ridingEntity).openGUI(playerEntity);
		}
	}

	/**
	 * Updates which quickbar slot is selected
	 */
	@Override
	public void processHeldItemChange(C09PacketHeldItemChange p_147355_1_) {
		if (p_147355_1_.func_149614_c() >= 0
				&& p_147355_1_.func_149614_c() < InventoryPlayer
						.getHotbarSize()) {
			playerEntity.inventory.currentItem = p_147355_1_.func_149614_c();
			playerEntity.func_143004_u();
		} else {
			logger.warn(playerEntity.getCommandSenderName()
					+ " tried to set an invalid carried item");
		}
	}

	/**
	 * Processes player movement input. Includes walking, strafing, jumping,
	 * sneaking; excludes riding and toggling flying/sprinting
	 */
	@Override
	public void processInput(C0CPacketInput p_147358_1_) {
		playerEntity.setEntityActionState(p_147358_1_.func_149620_c(),
				p_147358_1_.func_149616_d(), p_147358_1_.func_149618_e(),
				p_147358_1_.func_149617_f());
	}

	/**
	 * Updates a players' ping statistics
	 */
	@Override
	public void processKeepAlive(C00PacketKeepAlive p_147353_1_) {
		if (p_147353_1_.func_149460_c() == field_147378_h) {
			final int var2 = (int) (func_147363_d() - field_147379_i);
			playerEntity.ping = (playerEntity.ping * 3 + var2) / 4;
		}
	}

	/**
	 * Processes clients perspective on player positioning and/or orientation
	 */
	@Override
	public void processPlayer(C03PacketPlayer p_147347_1_) {
		final WorldServer var2 = serverController
				.worldServerForDimension(playerEntity.dimension);
		if (!playerEntity.playerConqueredTheEnd) {
			double var3;

			if (!hasMoved) {
				var3 = p_147347_1_.func_149467_d() - lastPosY;

				if (p_147347_1_.func_149464_c() == lastPosX
						&& var3 * var3 < 0.01D
						&& p_147347_1_.func_149472_e() == lastPosZ) {
					hasMoved = true;
				}
			}

			if (hasMoved) {
				double var5;
				double var7;
				double var9;

				if (playerEntity.ridingEntity != null) {
					float var34 = playerEntity.rotationYaw;
					float var4 = playerEntity.rotationPitch;
					playerEntity.ridingEntity.updateRiderPosition();
					var5 = playerEntity.posX;
					var7 = playerEntity.posY;
					var9 = playerEntity.posZ;

					if (p_147347_1_.func_149463_k()) {
						var34 = p_147347_1_.func_149462_g();
						var4 = p_147347_1_.func_149470_h();
					}

					playerEntity.onGround = p_147347_1_.func_149465_i();
					playerEntity.onUpdateEntity();
					playerEntity.ySize = 0.0F;
					playerEntity.setPositionAndRotation(var5, var7, var9,
							var34, var4);

					if (playerEntity.ridingEntity != null) {
						playerEntity.ridingEntity.updateRiderPosition();
					}

					serverController.getConfigurationManager()
							.serverUpdateMountedMovingPlayer(playerEntity);

					if (hasMoved) {
						lastPosX = playerEntity.posX;
						lastPosY = playerEntity.posY;
						lastPosZ = playerEntity.posZ;
					}

					var2.updateEntity(playerEntity);
					return;
				}

				if (playerEntity.isPlayerSleeping()) {
					playerEntity.onUpdateEntity();
					playerEntity.setPositionAndRotation(lastPosX, lastPosY,
							lastPosZ, playerEntity.rotationYaw,
							playerEntity.rotationPitch);
					var2.updateEntity(playerEntity);
					return;
				}

				var3 = playerEntity.posY;
				lastPosX = playerEntity.posX;
				lastPosY = playerEntity.posY;
				lastPosZ = playerEntity.posZ;
				var5 = playerEntity.posX;
				var7 = playerEntity.posY;
				var9 = playerEntity.posZ;
				float var11 = playerEntity.rotationYaw;
				float var12 = playerEntity.rotationPitch;

				if (p_147347_1_.func_149466_j()
						&& p_147347_1_.func_149467_d() == -999.0D
						&& p_147347_1_.func_149471_f() == -999.0D) {
					p_147347_1_.func_149469_a(false);
				}

				double var13;

				if (p_147347_1_.func_149466_j()) {
					var5 = p_147347_1_.func_149464_c();
					var7 = p_147347_1_.func_149467_d();
					var9 = p_147347_1_.func_149472_e();
					var13 = p_147347_1_.func_149471_f()
							- p_147347_1_.func_149467_d();

					if (!playerEntity.isPlayerSleeping()
							&& (var13 > 1.65D || var13 < 0.1D)) {
						kickPlayerFromServer("Illegal stance");
						logger.warn(playerEntity.getCommandSenderName()
								+ " had an illegal stance: " + var13);
						return;
					}

					if (Math.abs(p_147347_1_.func_149464_c()) > 3.2E7D
							|| Math.abs(p_147347_1_.func_149472_e()) > 3.2E7D) {
						kickPlayerFromServer("Illegal position");
						return;
					}
				}

				if (p_147347_1_.func_149463_k()) {
					var11 = p_147347_1_.func_149462_g();
					var12 = p_147347_1_.func_149470_h();
				}

				playerEntity.onUpdateEntity();
				playerEntity.ySize = 0.0F;
				playerEntity.setPositionAndRotation(lastPosX, lastPosY,
						lastPosZ, var11, var12);

				if (!hasMoved)
					return;

				var13 = var5 - playerEntity.posX;
				double var15 = var7 - playerEntity.posY;
				double var17 = var9 - playerEntity.posZ;
				final double var19 = Math.min(Math.abs(var13),
						Math.abs(playerEntity.motionX));
				final double var21 = Math.min(Math.abs(var15),
						Math.abs(playerEntity.motionY));
				final double var23 = Math.min(Math.abs(var17),
						Math.abs(playerEntity.motionZ));
				double var25 = var19 * var19 + var21 * var21 + var23 * var23;

				if (var25 > 100.0D
						&& (!serverController.isSinglePlayer() || !serverController
								.getServerOwner().equals(
										playerEntity.getCommandSenderName()))) {
					logger.warn(playerEntity.getCommandSenderName()
							+ " moved too quickly! " + var13 + "," + var15
							+ "," + var17 + " (" + var19 + ", " + var21 + ", "
							+ var23 + ")");
					setPlayerLocation(lastPosX, lastPosY, lastPosZ,
							playerEntity.rotationYaw,
							playerEntity.rotationPitch);
					return;
				}

				final float var27 = 0.0625F;
				final boolean var28 = var2.getCollidingBoundingBoxes(
						playerEntity,
						playerEntity.boundingBox.copy().contract(var27, var27,
								var27)).isEmpty();

				if (playerEntity.onGround && !p_147347_1_.func_149465_i()
						&& var15 > 0.0D) {
					playerEntity.jump();
				}

				playerEntity.moveEntity(var13, var15, var17);
				playerEntity.onGround = p_147347_1_.func_149465_i();
				playerEntity.addMovementStat(var13, var15, var17);
				final double var29 = var15;
				var13 = var5 - playerEntity.posX;
				var15 = var7 - playerEntity.posY;

				if (var15 > -0.5D || var15 < 0.5D) {
					var15 = 0.0D;
				}

				var17 = var9 - playerEntity.posZ;
				var25 = var13 * var13 + var15 * var15 + var17 * var17;
				boolean var31 = false;

				if (var25 > 0.0625D && !playerEntity.isPlayerSleeping()
						&& !playerEntity.theItemInWorldManager.isCreative()) {
					var31 = true;
					logger.warn(playerEntity.getCommandSenderName()
							+ " moved wrongly!");
				}

				playerEntity.setPositionAndRotation(var5, var7, var9, var11,
						var12);
				final boolean var32 = var2.getCollidingBoundingBoxes(
						playerEntity,
						playerEntity.boundingBox.copy().contract(var27, var27,
								var27)).isEmpty();

				if (var28 && (var31 || !var32)
						&& !playerEntity.isPlayerSleeping()) {
					setPlayerLocation(lastPosX, lastPosY, lastPosZ, var11,
							var12);
					return;
				}

				final AxisAlignedBB var33 = playerEntity.boundingBox.copy()
						.expand(var27, var27, var27)
						.addCoord(0.0D, -0.55D, 0.0D);

				if (!serverController.isFlightAllowed()
						&& !playerEntity.theItemInWorldManager.isCreative()
						&& !var2.checkBlockCollision(var33)) {
					if (var29 >= -0.03125D) {
						++floatingTickCount;

						if (floatingTickCount > 80) {
							logger.warn(playerEntity.getCommandSenderName()
									+ " was kicked for floating too long!");
							kickPlayerFromServer("Flying is not enabled on this server");
							return;
						}
					}
				} else {
					floatingTickCount = 0;
				}

				playerEntity.onGround = p_147347_1_.func_149465_i();
				serverController.getConfigurationManager()
						.serverUpdateMountedMovingPlayer(playerEntity);
				playerEntity.handleFalling(playerEntity.posY - var3,
						p_147347_1_.func_149465_i());
			} else if (networkTickCount % 20 == 0) {
				setPlayerLocation(lastPosX, lastPosY, lastPosZ,
						playerEntity.rotationYaw, playerEntity.rotationPitch);
			}
		}
	}

	/**
	 * Processes a player starting/stopping flying
	 */
	@Override
	public void processPlayerAbilities(C13PacketPlayerAbilities p_147348_1_) {
		playerEntity.capabilities.isFlying = p_147348_1_.func_149488_d()
				&& playerEntity.capabilities.allowFlying;
	}

	/**
	 * Processes block placement and block activation (anvil, furnace, etc.)
	 */
	@Override
	public void processPlayerBlockPlacement(
			C08PacketPlayerBlockPlacement p_147346_1_) {
		final WorldServer var2 = serverController
				.worldServerForDimension(playerEntity.dimension);
		ItemStack var3 = playerEntity.inventory.getCurrentItem();
		boolean var4 = false;
		int var5 = p_147346_1_.func_149576_c();
		int var6 = p_147346_1_.func_149571_d();
		int var7 = p_147346_1_.func_149570_e();
		final int var8 = p_147346_1_.func_149568_f();
		playerEntity.func_143004_u();

		if (p_147346_1_.func_149568_f() == 255) {
			if (var3 == null)
				return;

			playerEntity.theItemInWorldManager.tryUseItem(playerEntity, var2,
					var3);
		} else if (p_147346_1_.func_149571_d() >= serverController
				.getBuildLimit() - 1
				&& (p_147346_1_.func_149568_f() == 1 || p_147346_1_
						.func_149571_d() >= serverController.getBuildLimit())) {
			final ChatComponentTranslation var9 = new ChatComponentTranslation(
					"build.tooHigh",
					new Object[] { Integer.valueOf(serverController
							.getBuildLimit()) });
			var9.getChatStyle().setColor(EnumChatFormatting.RED);
			playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(
					var9));
			var4 = true;
		} else {
			if (hasMoved
					&& playerEntity.getDistanceSq(var5 + 0.5D, var6 + 0.5D,
							var7 + 0.5D) < 64.0D
					&& !serverController.isBlockProtected(var2, var5, var6,
							var7, playerEntity)) {
				playerEntity.theItemInWorldManager.activateBlockOrUseItem(
						playerEntity, var2, var3, var5, var6, var7, var8,
						p_147346_1_.func_149573_h(),
						p_147346_1_.func_149569_i(),
						p_147346_1_.func_149575_j());
			}

			var4 = true;
		}

		if (var4) {
			playerEntity.playerNetServerHandler
					.sendPacket(new S23PacketBlockChange(var5, var6, var7, var2));

			if (var8 == 0) {
				--var6;
			}

			if (var8 == 1) {
				++var6;
			}

			if (var8 == 2) {
				--var7;
			}

			if (var8 == 3) {
				++var7;
			}

			if (var8 == 4) {
				--var5;
			}

			if (var8 == 5) {
				++var5;
			}

			playerEntity.playerNetServerHandler
					.sendPacket(new S23PacketBlockChange(var5, var6, var7, var2));
		}

		var3 = playerEntity.inventory.getCurrentItem();

		if (var3 != null && var3.stackSize == 0) {
			playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = null;
			var3 = null;
		}

		if (var3 == null || var3.getMaxItemUseDuration() == 0) {
			playerEntity.isChangingQuantityOnly = true;
			playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = ItemStack
					.copyItemStack(playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem]);
			final Slot var10 = playerEntity.openContainer.getSlotFromInventory(
					playerEntity.inventory, playerEntity.inventory.currentItem);
			playerEntity.openContainer.detectAndSendChanges();
			playerEntity.isChangingQuantityOnly = false;

			if (!ItemStack.areItemStacksEqual(
					playerEntity.inventory.getCurrentItem(),
					p_147346_1_.func_149574_g())) {
				sendPacket(new S2FPacketSetSlot(
						playerEntity.openContainer.windowId, var10.slotNumber,
						playerEntity.inventory.getCurrentItem()));
			}
		}
	}

	/**
	 * Processes the player initiating/stopping digging on a particular spot, as
	 * well as a player dropping items?. (0: initiated, 1: reinitiated, 2? , 3-4
	 * drop item (respectively without or with player control), 5: stopped;
	 * x,y,z, side clicked on;)
	 */
	@Override
	public void processPlayerDigging(C07PacketPlayerDigging p_147345_1_) {
		final WorldServer var2 = serverController
				.worldServerForDimension(playerEntity.dimension);
		playerEntity.func_143004_u();

		if (p_147345_1_.func_149506_g() == 4) {
			playerEntity.dropOneItem(false);
		} else if (p_147345_1_.func_149506_g() == 3) {
			playerEntity.dropOneItem(true);
		} else if (p_147345_1_.func_149506_g() == 5) {
			playerEntity.stopUsingItem();
		} else {
			boolean var3 = false;

			if (p_147345_1_.func_149506_g() == 0) {
				var3 = true;
			}

			if (p_147345_1_.func_149506_g() == 1) {
				var3 = true;
			}

			if (p_147345_1_.func_149506_g() == 2) {
				var3 = true;
			}

			final int var4 = p_147345_1_.func_149505_c();
			final int var5 = p_147345_1_.func_149503_d();
			final int var6 = p_147345_1_.func_149502_e();

			if (var3) {
				final double var7 = playerEntity.posX - (var4 + 0.5D);
				final double var9 = playerEntity.posY - (var5 + 0.5D) + 1.5D;
				final double var11 = playerEntity.posZ - (var6 + 0.5D);
				final double var13 = var7 * var7 + var9 * var9 + var11 * var11;

				if (var13 > 36.0D)
					return;

				if (var5 >= serverController.getBuildLimit())
					return;
			}

			if (p_147345_1_.func_149506_g() == 0) {
				if (!serverController.isBlockProtected(var2, var4, var5, var6,
						playerEntity)) {
					playerEntity.theItemInWorldManager.onBlockClicked(var4,
							var5, var6, p_147345_1_.func_149501_f());
				} else {
					playerEntity.playerNetServerHandler
							.sendPacket(new S23PacketBlockChange(var4, var5,
									var6, var2));
				}
			} else if (p_147345_1_.func_149506_g() == 2) {
				playerEntity.theItemInWorldManager.uncheckedTryHarvestBlock(
						var4, var5, var6);

				if (var2.getBlock(var4, var5, var6).getMaterial() != Material.air) {
					playerEntity.playerNetServerHandler
							.sendPacket(new S23PacketBlockChange(var4, var5,
									var6, var2));
				}
			} else if (p_147345_1_.func_149506_g() == 1) {
				playerEntity.theItemInWorldManager.cancelDestroyingBlock(var4,
						var5, var6);

				if (var2.getBlock(var4, var5, var6).getMaterial() != Material.air) {
					playerEntity.playerNetServerHandler
							.sendPacket(new S23PacketBlockChange(var4, var5,
									var6, var2));
				}
			}
		}
	}

	/**
	 * Retrieves possible tab completions for the requested command string and
	 * sends them to the client
	 */
	@Override
	public void processTabComplete(C14PacketTabComplete p_147341_1_) {
		final ArrayList var2 = Lists.newArrayList();
		final Iterator var3 = serverController.getPossibleCompletions(
				playerEntity, p_147341_1_.func_149419_c()).iterator();

		while (var3.hasNext()) {
			final String var4 = (String) var3.next();
			var2.add(var4);
		}

		playerEntity.playerNetServerHandler
				.sendPacket(new S3APacketTabComplete((String[]) var2
						.toArray(new String[var2.size()])));
	}

	@Override
	public void processUpdateSign(C12PacketUpdateSign p_147343_1_) {
		playerEntity.func_143004_u();
		final WorldServer var2 = serverController
				.worldServerForDimension(playerEntity.dimension);

		if (var2.blockExists(p_147343_1_.func_149588_c(),
				p_147343_1_.func_149586_d(), p_147343_1_.func_149585_e())) {
			final TileEntity var3 = var2.getTileEntity(
					p_147343_1_.func_149588_c(), p_147343_1_.func_149586_d(),
					p_147343_1_.func_149585_e());

			if (var3 instanceof TileEntitySign) {
				final TileEntitySign var4 = (TileEntitySign) var3;

				if (!var4.func_145914_a()
						|| var4.func_145911_b() != playerEntity) {
					serverController.logWarning("Player "
							+ playerEntity.getCommandSenderName()
							+ " just tried to change non-editable sign");
					return;
				}
			}

			int var6;
			int var8;

			for (var8 = 0; var8 < 4; ++var8) {
				boolean var5 = true;

				if (p_147343_1_.func_149589_f()[var8].length() > 15) {
					var5 = false;
				} else {
					for (var6 = 0; var6 < p_147343_1_.func_149589_f()[var8]
							.length(); ++var6) {
						if (!ChatAllowedCharacters
								.isAllowedCharacter(p_147343_1_.func_149589_f()[var8]
										.charAt(var6))) {
							var5 = false;
						}
					}
				}

				if (!var5) {
					p_147343_1_.func_149589_f()[var8] = "!?";
				}
			}

			if (var3 instanceof TileEntitySign) {
				var8 = p_147343_1_.func_149588_c();
				final int var9 = p_147343_1_.func_149586_d();
				var6 = p_147343_1_.func_149585_e();
				final TileEntitySign var7 = (TileEntitySign) var3;
				System.arraycopy(p_147343_1_.func_149589_f(), 0,
						var7.field_145915_a, 0, 4);
				var7.onInventoryChanged();
				var2.func_147471_g(var8, var9, var6);
			}
		}
	}

	/**
	 * Processes interactions ((un)leashing, opening command block GUI) and
	 * attacks on an entity with players currently equipped item
	 */
	@Override
	public void processUseEntity(C02PacketUseEntity p_147340_1_) {
		final WorldServer var2 = serverController
				.worldServerForDimension(playerEntity.dimension);
		final Entity var3 = p_147340_1_.func_149564_a(var2);
		playerEntity.func_143004_u();

		if (var3 != null) {
			final boolean var4 = playerEntity.canEntityBeSeen(var3);
			double var5 = 36.0D;

			if (!var4) {
				var5 = 9.0D;
			}

			if (playerEntity.getDistanceSqToEntity(var3) < var5) {
				if (p_147340_1_.func_149565_c() == C02PacketUseEntity.Action.INTERACT) {
					playerEntity.interactWith(var3);
				} else if (p_147340_1_.func_149565_c() == C02PacketUseEntity.Action.ATTACK) {
					if (var3 instanceof EntityItem
							|| var3 instanceof EntityXPOrb
							|| var3 instanceof EntityArrow
							|| var3 == playerEntity) {
						kickPlayerFromServer("Attempting to attack an invalid entity");
						serverController.logWarning("Player "
								+ playerEntity.getCommandSenderName()
								+ " tried to attack an invalid entity");
						return;
					}

					playerEntity.attackTargetEntityWithCurrentItem(var3);
				}
			}
		}
	}

	/**
	 * Synchronizes serverside and clientside book contents and signing
	 */
	@Override
	public void processVanilla250Packet(C17PacketCustomPayload p_147349_1_) {
		PacketBuffer var2;
		ItemStack var3;
		ItemStack var4;

		if ("MC|BEdit".equals(p_147349_1_.func_149559_c())) {
			var2 = new PacketBuffer(Unpooled.wrappedBuffer(p_147349_1_
					.func_149558_e()));

			try {
				var3 = var2.readItemStackFromBuffer();

				if (var3 == null)
					return;

				if (!ItemWritableBook.func_150930_a(var3.getTagCompound()))
					throw new IOException("Invalid book tag!");

				var4 = playerEntity.inventory.getCurrentItem();

				if (var4 != null) {
					if (var3.getItem() == Items.writable_book
							&& var3.getItem() == var4.getItem()) {
						var4.setTagInfo("pages", var3.getTagCompound()
								.getTagList("pages", 8));
					}

					return;
				}
			} catch (final Exception var38) {
				logger.error("Couldn\'t handle book info", var38);
				return;
			} finally {
				var2.release();
			}

			return;
		} else if ("MC|BSign".equals(p_147349_1_.func_149559_c())) {
			var2 = new PacketBuffer(Unpooled.wrappedBuffer(p_147349_1_
					.func_149558_e()));

			try {
				var3 = var2.readItemStackFromBuffer();

				if (var3 == null)
					return;

				if (!ItemEditableBook.validBookTagContents(var3
						.getTagCompound()))
					throw new IOException("Invalid book tag!");

				var4 = playerEntity.inventory.getCurrentItem();

				if (var4 != null) {
					if (var3.getItem() == Items.written_book
							&& var4.getItem() == Items.writable_book) {
						var4.setTagInfo(
								"author",
								new NBTTagString(playerEntity
										.getCommandSenderName()));
						var4.setTagInfo("title", new NBTTagString(var3
								.getTagCompound().getString("title")));
						var4.setTagInfo("pages", var3.getTagCompound()
								.getTagList("pages", 8));
						var4.func_150996_a(Items.written_book);
					}

					return;
				}
			} catch (final Exception var36) {
				logger.error("Couldn\'t sign book", var36);
				return;
			} finally {
				var2.release();
			}

			return;
		} else {
			DataInputStream var40;
			int var42;

			if ("MC|TrSel".equals(p_147349_1_.func_149559_c())) {
				try {
					var40 = new DataInputStream(new ByteArrayInputStream(
							p_147349_1_.func_149558_e()));
					var42 = var40.readInt();
					final Container var45 = playerEntity.openContainer;

					if (var45 instanceof ContainerMerchant) {
						((ContainerMerchant) var45)
								.setCurrentRecipeIndex(var42);
					}
				} catch (final Exception var35) {
					logger.error("Couldn\'t select trade", var35);
				}
			} else if ("MC|AdvCdm".equals(p_147349_1_.func_149559_c())) {
				if (!serverController.isCommandBlockEnabled()) {
					playerEntity.addChatMessage(new ChatComponentTranslation(
							"advMode.notEnabled", new Object[0]));
				} else if (playerEntity.canCommandSenderUseCommand(2, "")
						&& playerEntity.capabilities.isCreativeMode) {
					var2 = new PacketBuffer(Unpooled.wrappedBuffer(p_147349_1_
							.func_149558_e()));

					try {
						final byte var43 = var2.readByte();
						CommandBlockLogic var46 = null;

						if (var43 == 0) {
							final TileEntity var5 = playerEntity.worldObj
									.getTileEntity(var2.readInt(),
											var2.readInt(), var2.readInt());

							if (var5 instanceof TileEntityCommandBlock) {
								var46 = ((TileEntityCommandBlock) var5)
										.func_145993_a();
							}
						} else if (var43 == 1) {
							final Entity var48 = playerEntity.worldObj
									.getEntityByID(var2.readInt());

							if (var48 instanceof EntityMinecartCommandBlock) {
								var46 = ((EntityMinecartCommandBlock) var48)
										.func_145822_e();
							}
						}

						final String var49 = var2.readStringFromBuffer(var2
								.readableBytes());

						if (var46 != null) {
							var46.func_145752_a(var49);
							var46.func_145756_e();
							playerEntity
									.addChatMessage(new ChatComponentTranslation(
											"advMode.setCommand.success",
											new Object[] { var49 }));
						}
					} catch (final Exception var33) {
						logger.error("Couldn\'t set command block", var33);
					} finally {
						var2.release();
					}
				} else {
					playerEntity.addChatMessage(new ChatComponentTranslation(
							"advMode.notAllowed", new Object[0]));
				}
			} else if ("MC|Beacon".equals(p_147349_1_.func_149559_c())) {
				if (playerEntity.openContainer instanceof ContainerBeacon) {
					try {
						var40 = new DataInputStream(new ByteArrayInputStream(
								p_147349_1_.func_149558_e()));
						var42 = var40.readInt();
						final int var47 = var40.readInt();
						final ContainerBeacon var50 = (ContainerBeacon) playerEntity.openContainer;
						final Slot var6 = var50.getSlot(0);

						if (var6.getHasStack()) {
							var6.decrStackSize(1);
							final TileEntityBeacon var7 = var50.func_148327_e();
							var7.func_146001_d(var42);
							var7.func_146004_e(var47);
							var7.onInventoryChanged();
						}
					} catch (final Exception var32) {
						logger.error("Couldn\'t set beacon", var32);
					}
				}
			} else if ("MC|ItemName".equals(p_147349_1_.func_149559_c())
					&& playerEntity.openContainer instanceof ContainerRepair) {
				final ContainerRepair var41 = (ContainerRepair) playerEntity.openContainer;

				if (p_147349_1_.func_149558_e() != null
						&& p_147349_1_.func_149558_e().length >= 1) {
					final String var44 = ChatAllowedCharacters
							.filerAllowedCharacters(new String(p_147349_1_
									.func_149558_e(), Charsets.UTF_8));

					if (var44.length() <= 30) {
						var41.updateItemName(var44);
					}
				} else {
					var41.updateItemName("");
				}
			}
		}
	}

	public void sendPacket(final Packet p_147359_1_) {
		if (p_147359_1_ instanceof S02PacketChat) {
			final S02PacketChat var2 = (S02PacketChat) p_147359_1_;
			final EntityPlayer.EnumChatVisibility var3 = playerEntity
					.func_147096_v();

			if (var3 == EntityPlayer.EnumChatVisibility.HIDDEN)
				return;

			if (var3 == EntityPlayer.EnumChatVisibility.SYSTEM
					&& !var2.func_148916_d())
				return;
		}

		try {
			netManager.scheduleOutboundPacket(p_147359_1_,
					new GenericFutureListener[0]);
		} catch (final Throwable var5) {
			final CrashReport var6 = CrashReport.makeCrashReport(var5,
					"Sending packet");
			final CrashReportCategory var4 = var6
					.makeCategory("Packet being sent");
			var4.addCrashSectionCallable("Packet class", new Callable() {

				@Override
				public String call() {
					return p_147359_1_.getClass().getCanonicalName();
				}
			});
			throw new ReportedException(var6);
		}
	}

	public void setPlayerLocation(double p_147364_1_, double p_147364_3_,
			double p_147364_5_, float p_147364_7_, float p_147364_8_) {
		hasMoved = false;
		lastPosX = p_147364_1_;
		lastPosY = p_147364_3_;
		lastPosZ = p_147364_5_;
		playerEntity.setPositionAndRotation(p_147364_1_, p_147364_3_,
				p_147364_5_, p_147364_7_, p_147364_8_);
		playerEntity.playerNetServerHandler
				.sendPacket(new S08PacketPlayerPosLook(p_147364_1_,
						p_147364_3_ + 1.6200000047683716D, p_147364_5_,
						p_147364_7_, p_147364_8_, false));
	}
}
