package net.minecraft.stats;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class StatisticsFile extends StatFileWriter {
	private static final Logger logger = LogManager.getLogger();

	public static String func_150880_a(Map p_150880_0_) {
		final JsonObject var1 = new JsonObject();
		final Iterator var2 = p_150880_0_.entrySet().iterator();

		while (var2.hasNext()) {
			final Entry var3 = (Entry) var2.next();

			if (((TupleIntJsonSerializable) var3.getValue())
					.getJsonSerializableValue() != null) {
				final JsonObject var4 = new JsonObject();
				var4.addProperty("value", Integer
						.valueOf(((TupleIntJsonSerializable) var3.getValue())
								.getIntegerValue()));

				try {
					var4.add("progress", ((TupleIntJsonSerializable) var3
							.getValue()).getJsonSerializableValue()
							.getSerializableElement());
				} catch (final Throwable var6) {
					logger.warn(
							"Couldn\'t save statistic "
									+ ((StatBase) var3.getKey())
											.func_150951_e()
									+ ": error serializing progress", var6);
				}

				var1.add(((StatBase) var3.getKey()).statId, var4);
			} else {
				var1.addProperty(((StatBase) var3.getKey()).statId, Integer
						.valueOf(((TupleIntJsonSerializable) var3.getValue())
								.getIntegerValue()));
			}
		}

		return var1.toString();
	}

	private int field_150885_f = -300;
	private boolean field_150886_g = false;
	private final File field_150887_d;
	private final Set field_150888_e = Sets.newHashSet();

	private final MinecraftServer field_150890_c;

	public StatisticsFile(MinecraftServer p_i45306_1_, File p_i45306_2_) {
		field_150890_c = p_i45306_1_;
		field_150887_d = p_i45306_2_;
	}

	@Override
	public void func_150873_a(EntityPlayer p_150873_1_, StatBase p_150873_2_,
			int p_150873_3_) {
		final int var4 = p_150873_2_.isAchievement() ? writeStat(p_150873_2_)
				: 0;
		super.func_150873_a(p_150873_1_, p_150873_2_, p_150873_3_);
		field_150888_e.add(p_150873_2_);

		if (p_150873_2_.isAchievement() && var4 == 0 && p_150873_3_ > 0) {
			field_150886_g = true;

			if (field_150890_c.func_147136_ar()) {
				field_150890_c.getConfigurationManager().func_148539_a(
						new ChatComponentTranslation("chat.type.achievement",
								new Object[] { p_150873_1_.func_145748_c_(),
										p_150873_2_.func_150955_j() }));
			}
		}
	}

	public void func_150876_a(EntityPlayerMP p_150876_1_) {
		final int var2 = field_150890_c.getTickCounter();
		final HashMap var3 = Maps.newHashMap();

		if (field_150886_g || var2 - field_150885_f > 300) {
			field_150885_f = var2;
			final Iterator var4 = func_150878_c().iterator();

			while (var4.hasNext()) {
				final StatBase var5 = (StatBase) var4.next();
				var3.put(var5, Integer.valueOf(writeStat(var5)));
			}
		}

		p_150876_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(
				var3));
	}

	public void func_150877_d() {
		final Iterator var1 = field_150875_a.keySet().iterator();

		while (var1.hasNext()) {
			final StatBase var2 = (StatBase) var1.next();
			field_150888_e.add(var2);
		}
	}

	public Set func_150878_c() {
		final HashSet var1 = Sets.newHashSet(field_150888_e);
		field_150888_e.clear();
		field_150886_g = false;
		return var1;
	}

	public boolean func_150879_e() {
		return field_150886_g;
	}

	public Map func_150881_a(String p_150881_1_) {
		final JsonElement var2 = new JsonParser().parse(p_150881_1_);

		if (!var2.isJsonObject())
			return Maps.newHashMap();
		else {
			final JsonObject var3 = var2.getAsJsonObject();
			final HashMap var4 = Maps.newHashMap();
			final Iterator var5 = var3.entrySet().iterator();

			while (var5.hasNext()) {
				final Entry var6 = (Entry) var5.next();
				final StatBase var7 = StatList.func_151177_a((String) var6
						.getKey());

				if (var7 != null) {
					final TupleIntJsonSerializable var8 = new TupleIntJsonSerializable();

					if (((JsonElement) var6.getValue()).isJsonPrimitive()
							&& ((JsonElement) var6.getValue())
									.getAsJsonPrimitive().isNumber()) {
						var8.setIntegerValue(((JsonElement) var6.getValue())
								.getAsInt());
					} else if (((JsonElement) var6.getValue()).isJsonObject()) {
						final JsonObject var9 = ((JsonElement) var6.getValue())
								.getAsJsonObject();

						if (var9.has("value")
								&& var9.get("value").isJsonPrimitive()
								&& var9.get("value").getAsJsonPrimitive()
										.isNumber()) {
							var8.setIntegerValue(var9.getAsJsonPrimitive(
									"value").getAsInt());
						}

						if (var9.has("progress")
								&& var7.func_150954_l() != null) {
							try {
								final Constructor var10 = var7.func_150954_l()
										.getConstructor(new Class[0]);
								final IJsonSerializable var11 = (IJsonSerializable) var10
										.newInstance(new Object[0]);
								var11.func_152753_a(var9.get("progress"));
								var8.setJsonSerializableValue(var11);
							} catch (final Throwable var12) {
								logger.warn("Invalid statistic progress in "
										+ field_150887_d, var12);
							}
						}
					}

					var4.put(var7, var8);
				} else {
					logger.warn("Invalid statistic in " + field_150887_d
							+ ": Don\'t know what " + (String) var6.getKey()
							+ " is");
				}
			}

			return var4;
		}
	}

	public void func_150882_a() {
		if (field_150887_d.isFile()) {
			try {
				field_150875_a.clear();
				field_150875_a.putAll(func_150881_a(FileUtils
						.readFileToString(field_150887_d)));
			} catch (final IOException var2) {
				logger.error(
						"Couldn\'t read statistics file " + field_150887_d,
						var2);
			} catch (final JsonParseException var3) {
				logger.error("Couldn\'t parse statistics file "
						+ field_150887_d, var3);
			}
		}
	}

	public void func_150883_b() {
		try {
			FileUtils.writeStringToFile(field_150887_d,
					func_150880_a(field_150875_a));
		} catch (final IOException var2) {
			logger.error("Couldn\'t save stats", var2);
		}
	}

	public void func_150884_b(EntityPlayerMP p_150884_1_) {
		final HashMap var2 = Maps.newHashMap();
		final Iterator var3 = AchievementList.achievementList.iterator();

		while (var3.hasNext()) {
			final Achievement var4 = (Achievement) var3.next();

			if (hasAchievementUnlocked(var4)) {
				var2.put(var4, Integer.valueOf(writeStat(var4)));
				field_150888_e.remove(var4);
			}
		}

		p_150884_1_.playerNetServerHandler.sendPacket(new S37PacketStatistics(
				var2));
	}
}
