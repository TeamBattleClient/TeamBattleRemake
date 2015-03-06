package net.minecraft.server.network;

import io.netty.util.concurrent.GenericFutureListener;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.SecretKey;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;

public class NetHandlerLoginServer implements INetHandlerLoginServer {
	static enum LoginState {
		ACCEPTED("ACCEPTED", 4), AUTHENTICATING("AUTHENTICATING", 2), HELLO(
				"HELLO", 0), KEY("KEY", 1), READY_TO_ACCEPT("READY_TO_ACCEPT",
				3);

		private LoginState(String p_i45297_1_, int p_i45297_2_) {
		}
	}

	private static final Random field_147329_d = new Random();
	private static final AtomicInteger field_147331_b = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private final MinecraftServer field_147327_f;
	private NetHandlerLoginServer.LoginState field_147328_g;
	private final byte[] field_147330_e = new byte[4];
	public final NetworkManager field_147333_a;
	private final String field_147334_j;
	private SecretKey field_147335_k;
	private int field_147336_h;

	private GameProfile field_147337_i;

	public NetHandlerLoginServer(MinecraftServer p_i45298_1_,
			NetworkManager p_i45298_2_) {
		field_147328_g = NetHandlerLoginServer.LoginState.HELLO;
		field_147334_j = "";
		field_147327_f = p_i45298_1_;
		field_147333_a = p_i45298_2_;
		field_147329_d.nextBytes(field_147330_e);
	}

	public String func_147317_d() {
		return field_147337_i != null ? field_147337_i.toString() + " ("
				+ field_147333_a.getSocketAddress().toString() + ")" : String
				.valueOf(field_147333_a.getSocketAddress());
	}

	public void func_147322_a(String p_147322_1_) {
		try {
			logger.info("Disconnecting " + func_147317_d() + ": " + p_147322_1_);
			final ChatComponentText var2 = new ChatComponentText(p_147322_1_);
			field_147333_a
					.scheduleOutboundPacket(new S00PacketDisconnect(var2),
							new GenericFutureListener[0]);
			field_147333_a.closeChannel(var2);
		} catch (final Exception var3) {
			logger.error("Error whilst disconnecting player", var3);
		}
	}

	public void func_147326_c() {
		if (!field_147337_i.isComplete()) {
			field_147337_i = func_152506_a(field_147337_i);
		}

		final String var1 = field_147327_f.getConfigurationManager()
				.func_148542_a(field_147333_a.getSocketAddress(),
						field_147337_i);

		if (var1 != null) {
			func_147322_a(var1);
		} else {
			field_147328_g = NetHandlerLoginServer.LoginState.ACCEPTED;
			field_147333_a.scheduleOutboundPacket(new S02PacketLoginSuccess(
					field_147337_i), new GenericFutureListener[0]);
			field_147327_f.getConfigurationManager()
					.initializeConnectionToPlayer(
							field_147333_a,
							field_147327_f.getConfigurationManager()
									.func_148545_a(field_147337_i));
		}
	}

	protected GameProfile func_152506_a(GameProfile p_152506_1_) {
		final UUID var2 = UUID
				.nameUUIDFromBytes(("OfflinePlayer:" + p_152506_1_.getName())
						.getBytes(Charsets.UTF_8));
		return new GameProfile(var2, p_152506_1_.getName());
	}

	/**
	 * Allows validation of the connection state transition. Parameters: from,
	 * to (connection state). Typically throws IllegalStateException or
	 * UnsupportedOperationException if validation fails
	 */
	@Override
	public void onConnectionStateTransition(EnumConnectionState p_147232_1_,
			EnumConnectionState p_147232_2_) {
		Validate.validState(
				field_147328_g == NetHandlerLoginServer.LoginState.ACCEPTED
						|| field_147328_g == NetHandlerLoginServer.LoginState.HELLO,
				"Unexpected change in protocol", new Object[0]);
		Validate.validState(p_147232_2_ == EnumConnectionState.PLAY
				|| p_147232_2_ == EnumConnectionState.LOGIN,
				"Unexpected protocol " + p_147232_2_, new Object[0]);
	}

	/**
	 * Invoked when disconnecting, the parameter is a ChatComponent describing
	 * the reason for termination
	 */
	@Override
	public void onDisconnect(IChatComponent p_147231_1_) {
		logger.info(func_147317_d() + " lost connection: "
				+ p_147231_1_.getUnformattedText());
	}

	/**
	 * For scheduled network tasks. Used in NetHandlerPlayServer to send
	 * keep-alive packets and in NetHandlerLoginServer for a login-timeout
	 */
	@Override
	public void onNetworkTick() {
		if (field_147328_g == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT) {
			func_147326_c();
		}

		if (field_147336_h++ == 600) {
			func_147322_a("Took too long to log in");
		}
	}

	@Override
	public void processEncryptionResponse(
			C01PacketEncryptionResponse p_147315_1_) {
		Validate.validState(
				field_147328_g == NetHandlerLoginServer.LoginState.KEY,
				"Unexpected key packet", new Object[0]);
		final PrivateKey var2 = field_147327_f.getKeyPair().getPrivate();

		if (!Arrays.equals(field_147330_e, p_147315_1_.func_149299_b(var2)))
			throw new IllegalStateException("Invalid nonce!");
		else {
			field_147335_k = p_147315_1_.func_149300_a(var2);
			field_147328_g = NetHandlerLoginServer.LoginState.AUTHENTICATING;
			field_147333_a.enableEncryption(field_147335_k);
			new Thread("User Authenticator #"
					+ field_147331_b.incrementAndGet()) {

				@Override
				public void run() {
					final GameProfile var1 = field_147337_i;

					try {
						final String var2 = new BigInteger(
								CryptManager
										.getServerIdHash(field_147334_j,
												field_147327_f.getKeyPair()
														.getPublic(),
												field_147335_k)).toString(16);
						field_147337_i = field_147327_f.func_147130_as()
								.hasJoinedServer(
										new GameProfile((UUID) null,
												var1.getName()), var2);

						if (field_147337_i != null) {
							NetHandlerLoginServer.logger.info("UUID of player "
									+ field_147337_i.getName() + " is "
									+ field_147337_i.getId());
							field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
						} else if (field_147327_f.isSinglePlayer()) {
							NetHandlerLoginServer.logger
									.warn("Failed to verify username but will let them in anyway!");
							field_147337_i = NetHandlerLoginServer.this
									.func_152506_a(var1);
							field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
						} else {
							NetHandlerLoginServer.this
									.func_147322_a("Failed to verify username!");
							NetHandlerLoginServer.logger
									.error("Username \'"
											+ field_147337_i.getName()
											+ "\' tried to join with an invalid session");
						}
					} catch (final AuthenticationUnavailableException var3) {
						if (field_147327_f.isSinglePlayer()) {
							NetHandlerLoginServer.logger
									.warn("Authentication servers are down but will let them in anyway!");
							field_147337_i = NetHandlerLoginServer.this
									.func_152506_a(var1);
							field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
						} else {
							NetHandlerLoginServer.this
									.func_147322_a("Authentication servers are down. Please try again later, sorry!");
							NetHandlerLoginServer.logger
									.error("Couldn\'t verify username because servers are unavailable");
						}
					}
				}
			}.start();
		}
	}

	@Override
	public void processLoginStart(C00PacketLoginStart p_147316_1_) {
		Validate.validState(
				field_147328_g == NetHandlerLoginServer.LoginState.HELLO,
				"Unexpected hello packet", new Object[0]);
		field_147337_i = p_147316_1_.func_149304_c();

		if (field_147327_f.isServerInOnlineMode()
				&& !field_147333_a.isLocalChannel()) {
			field_147328_g = NetHandlerLoginServer.LoginState.KEY;
			field_147333_a.scheduleOutboundPacket(
					new S01PacketEncryptionRequest(field_147334_j,
							field_147327_f.getKeyPair().getPublic(),
							field_147330_e), new GenericFutureListener[0]);
		} else {
			field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
		}
	}
}
