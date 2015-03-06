package net.minecraft.server.network;

import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {
	static final class SwitchEnumConnectionState {
		static final int[] field_151291_a = new int[EnumConnectionState
				.values().length];

		static {
			try {
				field_151291_a[EnumConnectionState.LOGIN.ordinal()] = 1;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				field_151291_a[EnumConnectionState.STATUS.ordinal()] = 2;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	private final NetworkManager field_147386_b;

	private final MinecraftServer field_147387_a;

	public NetHandlerHandshakeTCP(MinecraftServer p_i45295_1_,
			NetworkManager p_i45295_2_) {
		field_147387_a = p_i45295_1_;
		field_147386_b = p_i45295_2_;
	}

	/**
	 * Allows validation of the connection state transition. Parameters: from,
	 * to (connection state). Typically throws IllegalStateException or
	 * UnsupportedOperationException if validation fails
	 */
	@Override
	public void onConnectionStateTransition(EnumConnectionState p_147232_1_,
			EnumConnectionState p_147232_2_) {
		if (p_147232_2_ != EnumConnectionState.LOGIN
				&& p_147232_2_ != EnumConnectionState.STATUS)
			throw new UnsupportedOperationException("Invalid state "
					+ p_147232_2_);
	}

	/**
	 * Invoked when disconnecting, the parameter is a ChatComponent describing
	 * the reason for termination
	 */
	@Override
	public void onDisconnect(IChatComponent p_147231_1_) {
	}

	/**
	 * For scheduled network tasks. Used in NetHandlerPlayServer to send
	 * keep-alive packets and in NetHandlerLoginServer for a login-timeout
	 */
	@Override
	public void onNetworkTick() {
	}

	/**
	 * There are two recognized intentions for initiating a handshake: logging
	 * in and acquiring server status. The NetworkManager's protocol will be
	 * reconfigured according to the specified intention, although a
	 * login-intention must pass a versioncheck or receive a disconnect
	 * otherwise
	 */
	@Override
	public void processHandshake(C00Handshake p_147383_1_) {
		switch (NetHandlerHandshakeTCP.SwitchEnumConnectionState.field_151291_a[p_147383_1_
				.func_149594_c().ordinal()]) {
		case 1:
			field_147386_b.setConnectionState(EnumConnectionState.LOGIN);
			ChatComponentText var2;

			if (p_147383_1_.func_149595_d() > 5) {
				var2 = new ChatComponentText(
						"Outdated server! I\'m still on 1.7.10");
				field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(
						var2), new GenericFutureListener[0]);
				field_147386_b.closeChannel(var2);
			} else if (p_147383_1_.func_149595_d() < 5) {
				var2 = new ChatComponentText(
						"Outdated client! Please use 1.7.10");
				field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(
						var2), new GenericFutureListener[0]);
				field_147386_b.closeChannel(var2);
			} else {
				field_147386_b.setNetHandler(new NetHandlerLoginServer(
						field_147387_a, field_147386_b));
			}

			break;

		case 2:
			field_147386_b.setConnectionState(EnumConnectionState.STATUS);
			field_147386_b.setNetHandler(new NetHandlerStatusServer(
					field_147387_a, field_147386_b));
			break;

		default:
			throw new UnsupportedOperationException("Invalid intention "
					+ p_147383_1_.func_149594_c());
		}
	}
}
