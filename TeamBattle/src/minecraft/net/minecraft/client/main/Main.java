package net.minecraft.client.main;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;

public class Main {
	private static final java.lang.reflect.Type field_152370_a = new ParameterizedType() {

		@Override
		public java.lang.reflect.Type[] getActualTypeArguments() {
			return new java.lang.reflect.Type[] { String.class,
					new ParameterizedType() {

						@Override
						public java.lang.reflect.Type[] getActualTypeArguments() {
							return new java.lang.reflect.Type[] { String.class };
						}

						@Override
						public java.lang.reflect.Type getOwnerType() {
							return null;
						}

						@Override
						public java.lang.reflect.Type getRawType() {
							return Collection.class;
						}
					} };
		}

		@Override
		public java.lang.reflect.Type getOwnerType() {
			return null;
		}

		@Override
		public java.lang.reflect.Type getRawType() {
			return Map.class;
		}
	};

	private static boolean func_110121_a(String p_110121_0_) {
		return p_110121_0_ != null && !p_110121_0_.isEmpty();
	}

	public static void main(String[] p_main_0_) {
		otherMain(p_main_0_);
	}
	
	public static void otherMain(String[] p_main_0_) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		final OptionParser var1 = new OptionParser();
		var1.allowsUnrecognizedOptions();
		var1.accepts("demo");
		var1.accepts("fullscreen");
		final ArgumentAcceptingOptionSpec var2 = var1.accepts("server")
				.withRequiredArg();
		final ArgumentAcceptingOptionSpec var3 = var1.accepts("port")
				.withRequiredArg().ofType(Integer.class)
				.defaultsTo(Integer.valueOf(25565), new Integer[0]);
		final ArgumentAcceptingOptionSpec var4 = var1.accepts("gameDir")
				.withRequiredArg().ofType(File.class)
				.defaultsTo(new File("."), new File[0]);
		final ArgumentAcceptingOptionSpec var5 = var1.accepts("assetsDir")
				.withRequiredArg().ofType(File.class);
		final ArgumentAcceptingOptionSpec var6 = var1
				.accepts("resourcePackDir").withRequiredArg()
				.ofType(File.class);
		final ArgumentAcceptingOptionSpec var7 = var1.accepts("proxyHost")
				.withRequiredArg();
		final ArgumentAcceptingOptionSpec var8 = var1.accepts("proxyPort")
				.withRequiredArg().defaultsTo("8080", new String[0])
				.ofType(Integer.class);
		final ArgumentAcceptingOptionSpec var9 = var1.accepts("proxyUser")
				.withRequiredArg();
		final ArgumentAcceptingOptionSpec var10 = var1.accepts("proxyPass")
				.withRequiredArg();
		final ArgumentAcceptingOptionSpec var11 = var1
				.accepts("username")
				.withRequiredArg()
				.defaultsTo("Player" + Minecraft.getSystemTime() % 1000L,
						new String[0]);
		final ArgumentAcceptingOptionSpec var12 = var1.accepts("uuid")
				.withRequiredArg();
		final ArgumentAcceptingOptionSpec var13 = var1.accepts("accessToken")
				.withRequiredArg().required();
		final ArgumentAcceptingOptionSpec var14 = var1.accepts("version")
				.withRequiredArg().required();
		final ArgumentAcceptingOptionSpec var15 = var1.accepts("width")
				.withRequiredArg().ofType(Integer.class)
				.defaultsTo(Integer.valueOf(854), new Integer[0]);
		final ArgumentAcceptingOptionSpec var16 = var1.accepts("height")
				.withRequiredArg().ofType(Integer.class)
				.defaultsTo(Integer.valueOf(480), new Integer[0]);
		final ArgumentAcceptingOptionSpec var17 = var1
				.accepts("userProperties").withRequiredArg().required();
		final ArgumentAcceptingOptionSpec var18 = var1.accepts("assetIndex")
				.withRequiredArg();
		final ArgumentAcceptingOptionSpec var19 = var1.accepts("userType")
				.withRequiredArg().defaultsTo("legacy", new String[0]);
		final NonOptionArgumentSpec var20 = var1.nonOptions();
		final OptionSet var21 = var1.parse(p_main_0_);
		final List var22 = var21.valuesOf(var20);
		final String var23 = (String) var21.valueOf(var7);
		Proxy var24 = Proxy.NO_PROXY;

		if (var23 != null) {
			try {
				var24 = new Proxy(Type.SOCKS, new InetSocketAddress(var23,
						((Integer) var21.valueOf(var8)).intValue()));
			} catch (final Exception var41) {
				;
			}
		}

		final String var25 = (String) var21.valueOf(var9);
		final String var26 = (String) var21.valueOf(var10);

		if (!var24.equals(Proxy.NO_PROXY) && func_110121_a(var25)
				&& func_110121_a(var26)) {
			Authenticator.setDefault(new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(var25, var26
							.toCharArray());
				}
			});
		}

		final int var27 = ((Integer) var21.valueOf(var15)).intValue();
		final int var28 = ((Integer) var21.valueOf(var16)).intValue();
		final boolean var29 = var21.has("fullscreen");
		final boolean var30 = var21.has("demo");
		final String var31 = (String) var21.valueOf(var14);
		final HashMultimap var32 = HashMultimap.create();
		final Iterator var33 = ((Map) new Gson().fromJson(
				(String) var21.valueOf(var17), field_152370_a)).entrySet()
				.iterator();

		while (var33.hasNext()) {
			final Entry var34 = (Entry) var33.next();
			var32.putAll(var34.getKey(), (Iterable) var34.getValue());
		}

		final File var42 = (File) var21.valueOf(var4);
		final File var43 = var21.has(var5) ? (File) var21.valueOf(var5)
				: new File(var42, "assets/");
		final File var35 = var21.has(var6) ? (File) var21.valueOf(var6)
				: new File(var42, "resourcepacks/");
		final String var36 = var21.has(var12) ? (String) var12.value(var21)
				: (String) var11.value(var21);
		final String var37 = var21.has(var18) ? (String) var18.value(var21)
				: null;
		final Session var38 = new Session((String) var11.value(var21), var36,
				(String) var13.value(var21), (String) var19.value(var21));
		final Minecraft var39 = new Minecraft(var38, var27, var28, var29,
				var30, var42, var43, var35, var24, var31, var32, var37);
		final String var40 = (String) var21.valueOf(var2);

		if (var40 != null) {
			var39.setServer(var40, ((Integer) var21.valueOf(var3)).intValue());
		}

		Runtime.getRuntime().addShutdownHook(
				new Thread("Client Shutdown Thread") {

					@Override
					public void run() {
						Minecraft.stopIntegratedServer();
					}
				});

		if (!var22.isEmpty()) {
			System.out.println("Completely ignored arguments: " + var22);
		}

		Thread.currentThread().setName("Client thread");
		var39.run();
	}
}
