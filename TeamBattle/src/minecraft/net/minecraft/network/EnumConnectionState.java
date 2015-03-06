package net.minecraft.network;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
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
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public enum EnumConnectionState {
	HANDSHAKING("HANDSHAKING", 0, -1, null) {

		{
			func_150751_a(0, C00Handshake.class);
		}
	},
	LOGIN("LOGIN", 3, 2, null) {

		{
			func_150756_b(0, S00PacketDisconnect.class);
			func_150756_b(1, S01PacketEncryptionRequest.class);
			func_150756_b(2, S02PacketLoginSuccess.class);
			func_150751_a(0, C00PacketLoginStart.class);
			func_150751_a(1, C01PacketEncryptionResponse.class);
		}
	},
	PLAY("PLAY", 1, 0, null) {

		{
			func_150756_b(0, S00PacketKeepAlive.class);
			func_150756_b(1, S01PacketJoinGame.class);
			func_150756_b(2, S02PacketChat.class);
			func_150756_b(3, S03PacketTimeUpdate.class);
			func_150756_b(4, S04PacketEntityEquipment.class);
			func_150756_b(5, S05PacketSpawnPosition.class);
			func_150756_b(6, S06PacketUpdateHealth.class);
			func_150756_b(7, S07PacketRespawn.class);
			func_150756_b(8, S08PacketPlayerPosLook.class);
			func_150756_b(9, S09PacketHeldItemChange.class);
			func_150756_b(10, S0APacketUseBed.class);
			func_150756_b(11, S0BPacketAnimation.class);
			func_150756_b(12, S0CPacketSpawnPlayer.class);
			func_150756_b(13, S0DPacketCollectItem.class);
			func_150756_b(14, S0EPacketSpawnObject.class);
			func_150756_b(15, S0FPacketSpawnMob.class);
			func_150756_b(16, S10PacketSpawnPainting.class);
			func_150756_b(17, S11PacketSpawnExperienceOrb.class);
			func_150756_b(18, S12PacketEntityVelocity.class);
			func_150756_b(19, S13PacketDestroyEntities.class);
			func_150756_b(20, S14PacketEntity.class);
			func_150756_b(21, S14PacketEntity.S15PacketEntityRelMove.class);
			func_150756_b(22, S14PacketEntity.S16PacketEntityLook.class);
			func_150756_b(23, S14PacketEntity.S17PacketEntityLookMove.class);
			func_150756_b(24, S18PacketEntityTeleport.class);
			func_150756_b(25, S19PacketEntityHeadLook.class);
			func_150756_b(26, S19PacketEntityStatus.class);
			func_150756_b(27, S1BPacketEntityAttach.class);
			func_150756_b(28, S1CPacketEntityMetadata.class);
			func_150756_b(29, S1DPacketEntityEffect.class);
			func_150756_b(30, S1EPacketRemoveEntityEffect.class);
			func_150756_b(31, S1FPacketSetExperience.class);
			func_150756_b(32, S20PacketEntityProperties.class);
			func_150756_b(33, S21PacketChunkData.class);
			func_150756_b(34, S22PacketMultiBlockChange.class);
			func_150756_b(35, S23PacketBlockChange.class);
			func_150756_b(36, S24PacketBlockAction.class);
			func_150756_b(37, S25PacketBlockBreakAnim.class);
			func_150756_b(38, S26PacketMapChunkBulk.class);
			func_150756_b(39, S27PacketExplosion.class);
			func_150756_b(40, S28PacketEffect.class);
			func_150756_b(41, S29PacketSoundEffect.class);
			func_150756_b(42, S2APacketParticles.class);
			func_150756_b(43, S2BPacketChangeGameState.class);
			func_150756_b(44, S2CPacketSpawnGlobalEntity.class);
			func_150756_b(45, S2DPacketOpenWindow.class);
			func_150756_b(46, S2EPacketCloseWindow.class);
			func_150756_b(47, S2FPacketSetSlot.class);
			func_150756_b(48, S30PacketWindowItems.class);
			func_150756_b(49, S31PacketWindowProperty.class);
			func_150756_b(50, S32PacketConfirmTransaction.class);
			func_150756_b(51, S33PacketUpdateSign.class);
			func_150756_b(52, S34PacketMaps.class);
			func_150756_b(53, S35PacketUpdateTileEntity.class);
			func_150756_b(54, S36PacketSignEditorOpen.class);
			func_150756_b(55, S37PacketStatistics.class);
			func_150756_b(56, S38PacketPlayerListItem.class);
			func_150756_b(57, S39PacketPlayerAbilities.class);
			func_150756_b(58, S3APacketTabComplete.class);
			func_150756_b(59, S3BPacketScoreboardObjective.class);
			func_150756_b(60, S3CPacketUpdateScore.class);
			func_150756_b(61, S3DPacketDisplayScoreboard.class);
			func_150756_b(62, S3EPacketTeams.class);
			func_150756_b(63, S3FPacketCustomPayload.class);
			func_150756_b(64, S40PacketDisconnect.class);
			func_150751_a(0, C00PacketKeepAlive.class);
			func_150751_a(1, C01PacketChatMessage.class);
			func_150751_a(2, C02PacketUseEntity.class);
			func_150751_a(3, C03PacketPlayer.class);
			func_150751_a(4, C03PacketPlayer.C04PacketPlayerPosition.class);
			func_150751_a(5, C03PacketPlayer.C05PacketPlayerLook.class);
			func_150751_a(6, C03PacketPlayer.C06PacketPlayerPosLook.class);
			func_150751_a(7, C07PacketPlayerDigging.class);
			func_150751_a(8, C08PacketPlayerBlockPlacement.class);
			func_150751_a(9, C09PacketHeldItemChange.class);
			func_150751_a(10, C0APacketAnimation.class);
			func_150751_a(11, C0BPacketEntityAction.class);
			func_150751_a(12, C0CPacketInput.class);
			func_150751_a(13, C0DPacketCloseWindow.class);
			func_150751_a(14, C0EPacketClickWindow.class);
			func_150751_a(15, C0FPacketConfirmTransaction.class);
			func_150751_a(16, C10PacketCreativeInventoryAction.class);
			func_150751_a(17, C11PacketEnchantItem.class);
			func_150751_a(18, C12PacketUpdateSign.class);
			func_150751_a(19, C13PacketPlayerAbilities.class);
			func_150751_a(20, C14PacketTabComplete.class);
			func_150751_a(21, C15PacketClientSettings.class);
			func_150751_a(22, C16PacketClientStatus.class);
			func_150751_a(23, C17PacketCustomPayload.class);
		}
	},
	STATUS("STATUS", 2, 1, null) {

		{
			func_150751_a(0, C00PacketServerQuery.class);
			func_150756_b(0, S00PacketServerInfo.class);
			func_150751_a(1, C01PacketPing.class);
			func_150756_b(1, S01PacketPong.class);
		}
	};
	private static final Map field_150761_f = Maps.newHashMap();

	private static final TIntObjectMap field_150764_e = new TIntObjectHashMap();

	static {
		final EnumConnectionState[] var0 = values();
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			final EnumConnectionState var3 = var0[var2];
			field_150764_e.put(var3.func_150759_c(), var3);
			final Iterator var4 = Iterables.concat(
					var3.func_150755_b().values(),
					var3.func_150753_a().values()).iterator();

			while (var4.hasNext()) {
				final Class var5 = (Class) var4.next();

				if (field_150761_f.containsKey(var5)
						&& field_150761_f.get(var5) != var3)
					throw new Error("Packet " + var5
							+ " is already assigned to protocol "
							+ field_150761_f.get(var5)
							+ " - can\'t reassign to " + var3);

				field_150761_f.put(var5, var3);
			}
		}
	}

	public static EnumConnectionState func_150752_a(Packet p_150752_0_) {
		return (EnumConnectionState) field_150761_f.get(p_150752_0_.getClass());
	}

	public static EnumConnectionState func_150760_a(int p_150760_0_) {
		return (EnumConnectionState) field_150764_e.get(p_150760_0_);
	}

	private final int field_150762_g;

	private final BiMap field_150769_h;

	private final BiMap field_150770_i;

	private EnumConnectionState(int p_i45152_3_) {
		field_150769_h = HashBiMap.create();
		field_150770_i = HashBiMap.create();
		field_150762_g = p_i45152_3_;
	}

	EnumConnectionState(String ignore1, int ignore2, int p_i1197_3_,
			Object p_i1197_4_) {
		this(p_i1197_3_);
	}

	protected EnumConnectionState func_150751_a(int p_150751_1_,
			Class p_150751_2_) {
		String var3;

		if (field_150769_h.containsKey(Integer.valueOf(p_150751_1_))) {
			var3 = "Serverbound packet ID " + p_150751_1_
					+ " is already assigned to "
					+ field_150769_h.get(Integer.valueOf(p_150751_1_))
					+ "; cannot re-assign to " + p_150751_2_;
			LogManager.getLogger().fatal(var3);
			throw new IllegalArgumentException(var3);
		} else if (field_150769_h.containsValue(p_150751_2_)) {
			var3 = "Serverbound packet " + p_150751_2_
					+ " is already assigned to ID "
					+ field_150769_h.inverse().get(p_150751_2_)
					+ "; cannot re-assign to " + p_150751_1_;
			LogManager.getLogger().fatal(var3);
			throw new IllegalArgumentException(var3);
		} else {
			field_150769_h.put(Integer.valueOf(p_150751_1_), p_150751_2_);
			return this;
		}
	}

	public BiMap func_150753_a() {
		return field_150769_h;
	}

	public BiMap func_150754_b(boolean p_150754_1_) {
		return p_150754_1_ ? func_150753_a() : func_150755_b();
	}

	public BiMap func_150755_b() {
		return field_150770_i;
	}

	protected EnumConnectionState func_150756_b(int p_150756_1_,
			Class p_150756_2_) {
		String var3;

		if (field_150770_i.containsKey(Integer.valueOf(p_150756_1_))) {
			var3 = "Clientbound packet ID " + p_150756_1_
					+ " is already assigned to "
					+ field_150770_i.get(Integer.valueOf(p_150756_1_))
					+ "; cannot re-assign to " + p_150756_2_;
			LogManager.getLogger().fatal(var3);
			throw new IllegalArgumentException(var3);
		} else if (field_150770_i.containsValue(p_150756_2_)) {
			var3 = "Clientbound packet " + p_150756_2_
					+ " is already assigned to ID "
					+ field_150770_i.inverse().get(p_150756_2_)
					+ "; cannot re-assign to " + p_150756_1_;
			LogManager.getLogger().fatal(var3);
			throw new IllegalArgumentException(var3);
		} else {
			field_150770_i.put(Integer.valueOf(p_150756_1_), p_150756_2_);
			return this;
		}
	}

	public BiMap func_150757_a(boolean p_150757_1_) {
		return p_150757_1_ ? func_150755_b() : func_150753_a();
	}

	public int func_150759_c() {
		return field_150762_g;
	}
}
