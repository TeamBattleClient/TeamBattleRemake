package net.minecraft.world;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess {
	/** Reference to the MinecraftServer object. */
	private final MinecraftServer mcServer;

	/** The WorldServer object. */
	private final WorldServer theWorldServer;

	public WorldManager(MinecraftServer p_i1517_1_, WorldServer p_i1517_2_) {
		mcServer = p_i1517_1_;
		theWorldServer = p_i1517_2_;
	}

	@Override
	public void broadcastSound(int p_82746_1_, int p_82746_2_, int p_82746_3_,
			int p_82746_4_, int p_82746_5_) {
		mcServer.getConfigurationManager().func_148540_a(
				new S28PacketEffect(p_82746_1_, p_82746_2_, p_82746_3_,
						p_82746_4_, p_82746_5_, true));
	}

	/**
	 * Starts (or continues) destroying a block with given ID at the given
	 * coordinates for the given partially destroyed value
	 */
	@Override
	public void destroyBlockPartially(int p_147587_1_, int p_147587_2_,
			int p_147587_3_, int p_147587_4_, int p_147587_5_) {
		final Iterator var6 = mcServer.getConfigurationManager().playerEntityList
				.iterator();

		while (var6.hasNext()) {
			final EntityPlayerMP var7 = (EntityPlayerMP) var6.next();

			if (var7 != null && var7.worldObj == theWorldServer
					&& var7.getEntityId() != p_147587_1_) {
				final double var8 = p_147587_2_ - var7.posX;
				final double var10 = p_147587_3_ - var7.posY;
				final double var12 = p_147587_4_ - var7.posZ;

				if (var8 * var8 + var10 * var10 + var12 * var12 < 1024.0D) {
					var7.playerNetServerHandler
							.sendPacket(new S25PacketBlockBreakAnim(
									p_147587_1_, p_147587_2_, p_147587_3_,
									p_147587_4_, p_147587_5_));
				}
			}
		}
	}

	/**
	 * On the client, re-renders this block. On the server, does nothing. Used
	 * for lighting updates.
	 */
	@Override
	public void markBlockForRenderUpdate(int p_147588_1_, int p_147588_2_,
			int p_147588_3_) {
	}

	/**
	 * On the client, re-renders the block. On the server, sends the block to
	 * the client (which will re-render it), including the tile entity
	 * description packet if applicable. Args: x, y, z
	 */
	@Override
	public void markBlockForUpdate(int p_147586_1_, int p_147586_2_,
			int p_147586_3_) {
		theWorldServer.getPlayerManager().func_151250_a(p_147586_1_,
				p_147586_2_, p_147586_3_);
	}

	/**
	 * On the client, re-renders all blocks in this range, inclusive. On the
	 * server, does nothing. Args: min x, min y, min z, max x, max y, max z
	 */
	@Override
	public void markBlockRangeForRenderUpdate(int p_147585_1_, int p_147585_2_,
			int p_147585_3_, int p_147585_4_, int p_147585_5_, int p_147585_6_) {
	}

	/**
	 * Called on all IWorldAccesses when an entity is created or loaded. On
	 * client worlds, starts downloading any necessary textures. On server
	 * worlds, adds the entity to the entity tracker.
	 */
	@Override
	public void onEntityCreate(Entity p_72703_1_) {
		theWorldServer.getEntityTracker().addEntityToTracker(p_72703_1_);
	}

	/**
	 * Called on all IWorldAccesses when an entity is unloaded or destroyed. On
	 * client worlds, releases any downloaded textures. On server worlds,
	 * removes the entity from the entity tracker.
	 */
	@Override
	public void onEntityDestroy(Entity p_72709_1_) {
		theWorldServer.getEntityTracker().removeEntityFromAllTrackingPlayers(
				p_72709_1_);
	}

	@Override
	public void onStaticEntitiesChanged() {
	}

	/**
	 * Plays a pre-canned sound effect along with potentially auxiliary
	 * data-driven one-shot behaviour (particles, etc).
	 */
	@Override
	public void playAuxSFX(EntityPlayer p_72706_1_, int p_72706_2_,
			int p_72706_3_, int p_72706_4_, int p_72706_5_, int p_72706_6_) {
		mcServer.getConfigurationManager().func_148543_a(
				p_72706_1_,
				p_72706_3_,
				p_72706_4_,
				p_72706_5_,
				64.0D,
				theWorldServer.provider.dimensionId,
				new S28PacketEffect(p_72706_2_, p_72706_3_, p_72706_4_,
						p_72706_5_, p_72706_6_, false));
	}

	/**
	 * Plays the specified record. Arg: recordName, x, y, z
	 */
	@Override
	public void playRecord(String p_72702_1_, int p_72702_2_, int p_72702_3_,
			int p_72702_4_) {
	}

	/**
	 * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
	 */
	@Override
	public void playSound(String p_72704_1_, double p_72704_2_,
			double p_72704_4_, double p_72704_6_, float p_72704_8_,
			float p_72704_9_) {
		mcServer.getConfigurationManager().func_148541_a(
				p_72704_2_,
				p_72704_4_,
				p_72704_6_,
				p_72704_8_ > 1.0F ? (double) (16.0F * p_72704_8_) : 16.0D,
				theWorldServer.provider.dimensionId,
				new S29PacketSoundEffect(p_72704_1_, p_72704_2_, p_72704_4_,
						p_72704_6_, p_72704_8_, p_72704_9_));
	}

	/**
	 * Plays sound to all near players except the player reference given
	 */
	@Override
	public void playSoundToNearExcept(EntityPlayer p_85102_1_,
			String p_85102_2_, double p_85102_3_, double p_85102_5_,
			double p_85102_7_, float p_85102_9_, float p_85102_10_) {
		mcServer.getConfigurationManager().func_148543_a(
				p_85102_1_,
				p_85102_3_,
				p_85102_5_,
				p_85102_7_,
				p_85102_9_ > 1.0F ? (double) (16.0F * p_85102_9_) : 16.0D,
				theWorldServer.provider.dimensionId,
				new S29PacketSoundEffect(p_85102_2_, p_85102_3_, p_85102_5_,
						p_85102_7_, p_85102_9_, p_85102_10_));
	}

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	@Override
	public void spawnParticle(String p_72708_1_, double p_72708_2_,
			double p_72708_4_, double p_72708_6_, double p_72708_8_,
			double p_72708_10_, double p_72708_12_) {
	}
}
