package net.minecraft.server.management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;

public class PlayerProfileCache {
	class ProfileEntry {
		private final GameProfile field_152672_b;
		private final Date field_152673_c;

		private ProfileEntry(GameProfile p_i1165_2_, Date p_i1165_3_) {
			field_152672_b = p_i1165_2_;
			field_152673_c = p_i1165_3_;
		}

		ProfileEntry(GameProfile p_i1166_2_, Date p_i1166_3_, Object p_i1166_4_) {
			this(p_i1166_2_, p_i1166_3_);
		}

		public GameProfile func_152668_a() {
			return field_152672_b;
		}

		public Date func_152670_b() {
			return field_152673_c;
		}
	}

	class Serializer implements JsonDeserializer, JsonSerializer {

		private Serializer() {
		}

		Serializer(Object p_i1163_2_) {
			this();
		}

		@Override
		public Object deserialize(JsonElement p_deserialize_1_,
				Type p_deserialize_2_,
				JsonDeserializationContext p_deserialize_3_) {
			return func_152675_a(p_deserialize_1_, p_deserialize_2_,
					p_deserialize_3_);
		}

		public PlayerProfileCache.ProfileEntry func_152675_a(
				JsonElement p_152675_1_, Type p_152675_2_,
				JsonDeserializationContext p_152675_3_) {
			if (p_152675_1_.isJsonObject()) {
				final JsonObject var4 = p_152675_1_.getAsJsonObject();
				final JsonElement var5 = var4.get("name");
				final JsonElement var6 = var4.get("uuid");
				final JsonElement var7 = var4.get("expiresOn");

				if (var5 != null && var6 != null) {
					final String var8 = var6.getAsString();
					final String var9 = var5.getAsString();
					Date var10 = null;

					if (var7 != null) {
						try {
							var10 = PlayerProfileCache.field_152659_a
									.parse(var7.getAsString());
						} catch (final ParseException var14) {
							var10 = null;
						}
					}

					if (var9 != null && var8 != null) {
						UUID var11;

						try {
							var11 = UUID.fromString(var8);
						} catch (final Throwable var13) {
							return null;
						}

						final PlayerProfileCache.ProfileEntry var12 = PlayerProfileCache.this.new ProfileEntry(
								new GameProfile(var11, var9), var10, null);
						return var12;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		}

		public JsonElement func_152676_a(
				PlayerProfileCache.ProfileEntry p_152676_1_, Type p_152676_2_,
				JsonSerializationContext p_152676_3_) {
			final JsonObject var4 = new JsonObject();
			var4.addProperty("name", p_152676_1_.func_152668_a().getName());
			final UUID var5 = p_152676_1_.func_152668_a().getId();
			var4.addProperty("uuid", var5 == null ? "" : var5.toString());
			var4.addProperty("expiresOn", PlayerProfileCache.field_152659_a
					.format(p_152676_1_.func_152670_b()));
			return var4;
		}

		@Override
		public JsonElement serialize(Object p_serialize_1_,
				Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
			return func_152676_a(
					(PlayerProfileCache.ProfileEntry) p_serialize_1_,
					p_serialize_2_, p_serialize_3_);
		}
	}

	public static final SimpleDateFormat field_152659_a = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss Z");

	private static final ParameterizedType field_152666_h = new ParameterizedType() {

		@Override
		public Type[] getActualTypeArguments() {
			return new Type[] { PlayerProfileCache.ProfileEntry.class };
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

		@Override
		public Type getRawType() {
			return List.class;
		}
	};

	private static GameProfile func_152650_a(MinecraftServer p_152650_0_,
			String p_152650_1_) {
		final GameProfile[] var2 = new GameProfile[1];
		final ProfileLookupCallback var3 = new ProfileLookupCallback() {

			@Override
			public void onProfileLookupFailed(
					GameProfile p_onProfileLookupFailed_1_,
					Exception p_onProfileLookupFailed_2_) {
				var2[0] = null;
			}

			@Override
			public void onProfileLookupSucceeded(
					GameProfile p_onProfileLookupSucceeded_1_) {
				var2[0] = p_onProfileLookupSucceeded_1_;
			}
		};
		p_152650_0_.func_152359_aw().findProfilesByNames(
				new String[] { p_152650_1_ }, Agent.MINECRAFT, var3);

		if (!p_152650_0_.isServerInOnlineMode() && var2[0] == null) {
			final UUID var4 = EntityPlayer.func_146094_a(new GameProfile(
					(UUID) null, p_152650_1_));
			final GameProfile var5 = new GameProfile(var4, p_152650_1_);
			var3.onProfileLookupSucceeded(var5);
		}

		return var2[0];
	}

	protected final Gson field_152660_b;
	private final Map field_152661_c = Maps.newHashMap();
	private final Map field_152662_d = Maps.newHashMap();

	private final LinkedList field_152663_e = Lists.newLinkedList();

	private final MinecraftServer field_152664_f;

	private final File field_152665_g;

	public PlayerProfileCache(MinecraftServer p_i1171_1_, File p_i1171_2_) {
		field_152664_f = p_i1171_1_;
		field_152665_g = p_i1171_2_;
		final GsonBuilder var3 = new GsonBuilder();
		var3.registerTypeHierarchyAdapter(
				PlayerProfileCache.ProfileEntry.class,
				new PlayerProfileCache.Serializer(null));
		field_152660_b = var3.create();
		func_152657_b();
	}

	public void func_152649_a(GameProfile p_152649_1_) {
		func_152651_a(p_152649_1_, (Date) null);
	}

	private void func_152651_a(GameProfile p_152651_1_, Date p_152651_2_) {
		final UUID var3 = p_152651_1_.getId();

		if (p_152651_2_ == null) {
			final Calendar var4 = Calendar.getInstance();
			var4.setTime(new Date());
			var4.add(2, 1);
			p_152651_2_ = var4.getTime();
		}

		final String var10 = p_152651_1_.getName().toLowerCase(Locale.ROOT);
		final PlayerProfileCache.ProfileEntry var5 = new PlayerProfileCache.ProfileEntry(
				p_152651_1_, p_152651_2_, null);
		synchronized (field_152663_e) {
			if (field_152662_d.containsKey(var3)) {
				final PlayerProfileCache.ProfileEntry var7 = (PlayerProfileCache.ProfileEntry) field_152662_d
						.get(var3);
				field_152661_c.remove(var7.func_152668_a().getName()
						.toLowerCase(Locale.ROOT));
				field_152661_c.put(
						p_152651_1_.getName().toLowerCase(Locale.ROOT), var5);
				field_152663_e.remove(p_152651_1_);
			} else {
				field_152662_d.put(var3, var5);
				field_152661_c.put(var10, var5);
			}

			field_152663_e.addFirst(p_152651_1_);
		}
	}

	public GameProfile func_152652_a(UUID p_152652_1_) {
		final PlayerProfileCache.ProfileEntry var2 = (PlayerProfileCache.ProfileEntry) field_152662_d
				.get(p_152652_1_);
		return var2 == null ? null : var2.func_152668_a();
	}

	private PlayerProfileCache.ProfileEntry func_152653_b(UUID p_152653_1_) {
		final PlayerProfileCache.ProfileEntry var2 = (PlayerProfileCache.ProfileEntry) field_152662_d
				.get(p_152653_1_);

		if (var2 != null) {
			final GameProfile var3 = var2.func_152668_a();
			synchronized (field_152663_e) {
				field_152663_e.remove(var3);
				field_152663_e.addFirst(var3);
			}
		}

		return var2;
	}

	public String[] func_152654_a() {
		final ArrayList var1 = Lists.newArrayList(field_152661_c.keySet());
		return (String[]) var1.toArray(new String[var1.size()]);
	}

	public GameProfile func_152655_a(String p_152655_1_) {
		final String var2 = p_152655_1_.toLowerCase(Locale.ROOT);
		PlayerProfileCache.ProfileEntry var3 = (PlayerProfileCache.ProfileEntry) field_152661_c
				.get(var2);

		if (var3 != null
				&& new Date().getTime() >= var3.field_152673_c.getTime()) {
			field_152662_d.remove(var3.func_152668_a().getId());
			field_152661_c.remove(var3.func_152668_a().getName()
					.toLowerCase(Locale.ROOT));
			synchronized (field_152663_e) {
				field_152663_e.remove(var3.func_152668_a());
			}

			var3 = null;
		}

		GameProfile var9;

		if (var3 != null) {
			var9 = var3.func_152668_a();
			synchronized (field_152663_e) {
				field_152663_e.remove(var9);
				field_152663_e.addFirst(var9);
			}
		} else {
			var9 = func_152650_a(field_152664_f, var2);

			if (var9 != null) {
				func_152649_a(var9);
				var3 = (PlayerProfileCache.ProfileEntry) field_152661_c
						.get(var2);
			}
		}

		func_152658_c();
		return var3 == null ? null : var3.func_152668_a();
	}

	private List func_152656_a(int p_152656_1_) {
		final ArrayList var2 = Lists.newArrayList();
		ArrayList var3;

		synchronized (field_152663_e) {
			var3 = Lists.newArrayList(Iterators.limit(
					field_152663_e.iterator(), p_152656_1_));
		}

		final Iterator var8 = var3.iterator();

		while (var8.hasNext()) {
			final GameProfile var5 = (GameProfile) var8.next();
			final PlayerProfileCache.ProfileEntry var6 = func_152653_b(var5
					.getId());

			if (var6 != null) {
				var2.add(var6);
			}
		}

		return var2;
	}

	public void func_152657_b() {
		List var1 = null;
		BufferedReader var2 = null;
		label81: {
			try {
				var2 = Files.newReader(field_152665_g, Charsets.UTF_8);
				var1 = (List) field_152660_b.fromJson(var2, field_152666_h);
				break label81;
			} catch (final FileNotFoundException var10) {
				;
			} finally {
				IOUtils.closeQuietly(var2);
			}

			return;
		}

		if (var1 != null) {
			field_152661_c.clear();
			field_152662_d.clear();
			synchronized (field_152663_e) {
				field_152663_e.clear();
			}

			var1 = Lists.reverse(var1);
			final Iterator var12 = var1.iterator();

			while (var12.hasNext()) {
				final PlayerProfileCache.ProfileEntry var4 = (PlayerProfileCache.ProfileEntry) var12
						.next();

				if (var4 != null) {
					func_152651_a(var4.func_152668_a(), var4.func_152670_b());
				}
			}
		}
	}

	public void func_152658_c() {
		final String var1 = field_152660_b.toJson(func_152656_a(1000));
		BufferedWriter var2 = null;

		try {
			var2 = Files.newWriter(field_152665_g, Charsets.UTF_8);
			var2.write(var1);
			return;
		} catch (final FileNotFoundException var8) {
			;
		} catch (final IOException var9) {
			return;
		} finally {
			IOUtils.closeQuietly(var2);
		}
	}
}
