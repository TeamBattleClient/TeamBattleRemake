package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;

public class PingResponseHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LogManager.getLogger();
	private final NetworkSystem field_151257_b;

	public PingResponseHandler(NetworkSystem p_i45286_1_) {
		field_151257_b = p_i45286_1_;
	}

	@Override
	public void channelRead(ChannelHandlerContext p_channelRead_1_,
			Object p_channelRead_2_) {
		final ByteBuf var3 = (ByteBuf) p_channelRead_2_;
		var3.markReaderIndex();
		boolean var4 = true;

		try {
			if (var3.readUnsignedByte() == 254) {
				final InetSocketAddress var5 = (InetSocketAddress) p_channelRead_1_
						.channel().remoteAddress();
				final MinecraftServer var6 = field_151257_b.func_151267_d();
				final int var7 = var3.readableBytes();
				String var8;

				switch (var7) {
				case 0:
					logger.debug(
							"Ping: (<1.3.x) from {}:{}",
							new Object[] { var5.getAddress(),
									Integer.valueOf(var5.getPort()) });
					var8 = String.format(
							"%s\u00a7%d\u00a7%d",
							new Object[] {
									var6.getMOTD(),
									Integer.valueOf(var6
											.getCurrentPlayerCount()),
									Integer.valueOf(var6.getMaxPlayers()) });
					func_151256_a(p_channelRead_1_, func_151255_a(var8));
					break;

				case 1:
					if (var3.readUnsignedByte() != 1)
						return;

					logger.debug(
							"Ping: (1.4-1.5.x) from {}:{}",
							new Object[] { var5.getAddress(),
									Integer.valueOf(var5.getPort()) });
					var8 = String.format(
							"\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d",
							new Object[] {
									Integer.valueOf(127),
									var6.getMinecraftVersion(),
									var6.getMOTD(),
									Integer.valueOf(var6
											.getCurrentPlayerCount()),
									Integer.valueOf(var6.getMaxPlayers()) });
					func_151256_a(p_channelRead_1_, func_151255_a(var8));
					break;

				default:
					boolean var23 = var3.readUnsignedByte() == 1;
					var23 &= var3.readUnsignedByte() == 250;
					var23 &= "MC|PingHost".equals(new String(var3.readBytes(
							var3.readShort() * 2).array(), Charsets.UTF_16BE));
					final int var9 = var3.readUnsignedShort();
					var23 &= var3.readUnsignedByte() >= 73;
					var23 &= 3 + var3.readBytes(var3.readShort() * 2).array().length + 4 == var9;
					var23 &= var3.readInt() <= 65535;
					var23 &= var3.readableBytes() == 0;

					if (!var23)
						return;

					logger.debug(
							"Ping: (1.6) from {}:{}",
							new Object[] { var5.getAddress(),
									Integer.valueOf(var5.getPort()) });
					final String var10 = String.format(
							"\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d",
							new Object[] {
									Integer.valueOf(127),
									var6.getMinecraftVersion(),
									var6.getMOTD(),
									Integer.valueOf(var6
											.getCurrentPlayerCount()),
									Integer.valueOf(var6.getMaxPlayers()) });
					final ByteBuf var11 = func_151255_a(var10);

					try {
						func_151256_a(p_channelRead_1_, var11);
					} finally {
						var11.release();
					}
				}

				var3.release();
				var4 = false;
				return;
			}
		} catch (final RuntimeException var21) {
			return;
		} finally {
			if (var4) {
				var3.resetReaderIndex();
				p_channelRead_1_.channel().pipeline().remove("legacy_query");
				p_channelRead_1_.fireChannelRead(p_channelRead_2_);
			}
		}
	}

	private ByteBuf func_151255_a(String p_151255_1_) {
		final ByteBuf var2 = Unpooled.buffer();
		var2.writeByte(255);
		final char[] var3 = p_151255_1_.toCharArray();
		var2.writeShort(var3.length);
		final char[] var4 = var3;
		final int var5 = var3.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			final char var7 = var4[var6];
			var2.writeChar(var7);
		}

		return var2;
	}

	private void func_151256_a(ChannelHandlerContext p_151256_1_,
			ByteBuf p_151256_2_) {
		p_151256_1_.pipeline().firstContext().writeAndFlush(p_151256_2_)
				.addListener(ChannelFutureListener.CLOSE);
	}
}
