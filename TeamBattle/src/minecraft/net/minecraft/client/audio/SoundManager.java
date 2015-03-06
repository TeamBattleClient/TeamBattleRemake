package net.minecraft.client.audio;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class SoundManager {
	class SoundSystemStarterThread extends SoundSystem {

		private SoundSystemStarterThread() {
		}

		SoundSystemStarterThread(Object p_i45118_2_) {
			this();
		}

		@Override
		public boolean playing(String p_playing_1_) {
			synchronized (SoundSystemConfig.THREAD_SYNC) {
				if (soundLibrary == null)
					return false;
				else {
					final Source var3 = soundLibrary.getSources().get(
							p_playing_1_);
					return var3 == null ? false : var3.playing()
							|| var3.paused() || var3.preLoad;
				}
			}
		}
	}

	private static final Marker field_148623_a = MarkerManager
			.getMarker("SOUNDS");

	private static final Logger logger = LogManager.getLogger();

	private static URL func_148612_a(final ResourceLocation p_148612_0_) {
		final String var1 = String.format(
				"%s:%s:%s",
				new Object[] { "mcsounddomain",
						p_148612_0_.getResourceDomain(),
						p_148612_0_.getResourcePath() });
		final URLStreamHandler var2 = new URLStreamHandler() {

			@Override
			protected URLConnection openConnection(final URL p_openConnection_1_) {
				return new URLConnection(p_openConnection_1_) {

					@Override
					public void connect() {
					}

					@Override
					public InputStream getInputStream() throws IOException {
						return Minecraft.getMinecraft().getResourceManager()
								.getResource(p_148612_0_).getInputStream();
					}
				};
			}
		};

		try {
			return new URL((URL) null, var1, var2);
		} catch (final MalformedURLException var4) {
			throw new Error("TODO: Sanely handle url exception! :D");
		}
	}

	private boolean field_148617_f;
	private int field_148618_g = 0;
	private final GameSettings field_148619_d;
	private SoundManager.SoundSystemStarterThread field_148620_e;
	private final SoundHandler field_148622_c;
	private final Map field_148624_n;
	private final List field_148625_l;
	private final Map field_148626_m;
	private final Map field_148627_j;
	private final Multimap field_148628_k;

	private final Map field_148629_h = HashBiMap.create();

	private final Map field_148630_i;

	public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_) {
		field_148630_i = ((BiMap) field_148629_h).inverse();
		field_148627_j = Maps.newHashMap();
		field_148628_k = HashMultimap.create();
		field_148625_l = Lists.newArrayList();
		field_148626_m = Maps.newHashMap();
		field_148624_n = Maps.newHashMap();
		field_148622_c = p_i45119_1_;
		field_148619_d = p_i45119_2_;

		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		} catch (final SoundSystemException var4) {
			logger.error(field_148623_a,
					"Error linking with the LibraryJavaSound plug-in", var4);
		}
	}

	private float func_148594_a(ISound p_148594_1_, SoundPoolEntry p_148594_2_,
			SoundCategory p_148594_3_) {
		return (float) MathHelper.clamp_double(p_148594_1_.func_147653_e()
				* p_148594_2_.func_148649_c() * func_148595_a(p_148594_3_),
				0.0D, 1.0D);
	}

	private float func_148595_a(SoundCategory p_148595_1_) {
		return p_148595_1_ != null && p_148595_1_ != SoundCategory.MASTER ? field_148619_d
				.getSoundLevel(p_148595_1_) : 1.0F;
	}

	public void func_148596_a() {
		func_148613_b();
		func_148608_i();
	}

	public boolean func_148597_a(ISound p_148597_1_) {
		if (!field_148617_f)
			return false;
		else {
			final String var2 = (String) field_148630_i.get(p_148597_1_);
			return var2 == null ? false
					: field_148620_e.playing(var2)
							|| field_148624_n.containsKey(var2)
							&& ((Integer) field_148624_n.get(var2)).intValue() <= field_148618_g;
		}
	}

	public void func_148599_a(ISound p_148599_1_, int p_148599_2_) {
		field_148626_m.put(p_148599_1_,
				Integer.valueOf(field_148618_g + p_148599_2_));
	}

	public void func_148601_a(SoundCategory p_148601_1_, float p_148601_2_) {
		if (field_148617_f) {
			if (p_148601_1_ == SoundCategory.MASTER) {
				field_148620_e.setMasterVolume(p_148601_2_);
			} else {
				final Iterator var3 = field_148628_k.get(p_148601_1_)
						.iterator();

				while (var3.hasNext()) {
					final String var4 = (String) var3.next();
					final ISound var5 = (ISound) field_148629_h.get(var4);
					final float var6 = func_148594_a(var5,
							(SoundPoolEntry) field_148627_j.get(var5),
							p_148601_1_);

					if (var6 <= 0.0F) {
						func_148602_b(var5);
					} else {
						field_148620_e.setVolume(var4, var6);
					}
				}
			}
		}
	}

	public void func_148602_b(ISound p_148602_1_) {
		if (field_148617_f) {
			final String var2 = (String) field_148630_i.get(p_148602_1_);

			if (var2 != null) {
				field_148620_e.stop(var2);
			}
		}
	}

	public void func_148604_f() {
		final Iterator var1 = field_148629_h.keySet().iterator();

		while (var1.hasNext()) {
			final String var2 = (String) var1.next();
			logger.debug(field_148623_a, "Resuming channel {}",
					new Object[] { var2 });
			field_148620_e.play(var2);
		}
	}

	public void func_148605_d() {
		++field_148618_g;
		Iterator var1 = field_148625_l.iterator();
		String var3;

		while (var1.hasNext()) {
			final ITickableSound var2 = (ITickableSound) var1.next();
			var2.update();

			if (var2.func_147667_k()) {
				func_148602_b(var2);
			} else {
				var3 = (String) field_148630_i.get(var2);
				field_148620_e.setVolume(
						var3,
						func_148594_a(
								var2,
								(SoundPoolEntry) field_148627_j.get(var2),
								field_148622_c.func_147680_a(
										var2.func_147650_b()).func_148728_d()));
				field_148620_e.setPitch(
						var3,
						func_148606_a(var2,
								(SoundPoolEntry) field_148627_j.get(var2)));
				field_148620_e.setPosition(var3, var2.func_147649_g(),
						var2.func_147654_h(), var2.func_147651_i());
			}
		}

		var1 = field_148629_h.entrySet().iterator();
		ISound var4;

		while (var1.hasNext()) {
			final Entry var9 = (Entry) var1.next();
			var3 = (String) var9.getKey();
			var4 = (ISound) var9.getValue();

			if (!field_148620_e.playing(var3)) {
				final int var5 = ((Integer) field_148624_n.get(var3))
						.intValue();

				if (var5 <= field_148618_g) {
					final int var6 = var4.func_147652_d();

					if (var4.func_147657_c() && var6 > 0) {
						field_148626_m.put(var4,
								Integer.valueOf(field_148618_g + var6));
					}

					var1.remove();
					logger.debug(
							field_148623_a,
							"Removed channel {} because it\'s not playing anymore",
							new Object[] { var3 });
					field_148620_e.removeSource(var3);
					field_148624_n.remove(var3);
					field_148627_j.remove(var4);

					try {
						field_148628_k.remove(
								field_148622_c.func_147680_a(
										var4.func_147650_b()).func_148728_d(),
								var3);
					} catch (final RuntimeException var8) {
						;
					}

					if (var4 instanceof ITickableSound) {
						field_148625_l.remove(var4);
					}
				}
			}
		}

		final Iterator var10 = field_148626_m.entrySet().iterator();

		while (var10.hasNext()) {
			final Entry var11 = (Entry) var10.next();

			if (field_148618_g >= ((Integer) var11.getValue()).intValue()) {
				var4 = (ISound) var11.getKey();

				if (var4 instanceof ITickableSound) {
					((ITickableSound) var4).update();
				}

				func_148611_c(var4);
				var10.remove();
			}
		}
	}

	private float func_148606_a(ISound p_148606_1_, SoundPoolEntry p_148606_2_) {
		return (float) MathHelper.clamp_double(p_148606_1_.func_147655_f()
				* p_148606_2_.func_148650_b(), 0.5D, 2.0D);
	}

	private synchronized void func_148608_i() {
		if (!field_148617_f) {
			try {
				new Thread(new Runnable() {

					@Override
					public void run() {
						field_148620_e = SoundManager.this.new SoundSystemStarterThread(
								null);
						field_148617_f = true;
						field_148620_e.setMasterVolume(field_148619_d
								.getSoundLevel(SoundCategory.MASTER));
						SoundManager.logger.info(SoundManager.field_148623_a,
								"Sound engine started");
					}
				}, "Sound Library Loader").start();
			} catch (final RuntimeException var2) {
				logger.error(
						field_148623_a,
						"Error starting SoundSystem. Turning off sounds & music",
						var2);
				field_148619_d.setSoundLevel(SoundCategory.MASTER, 0.0F);
				field_148619_d.saveOptions();
			}
		}
	}

	public void func_148610_e() {
		final Iterator var1 = field_148629_h.keySet().iterator();

		while (var1.hasNext()) {
			final String var2 = (String) var1.next();
			logger.debug(field_148623_a, "Pausing channel {}",
					new Object[] { var2 });
			field_148620_e.pause(var2);
		}
	}

	public void func_148611_c(ISound p_148611_1_) {
		if (field_148617_f) {
			if (field_148620_e.getMasterVolume() <= 0.0F) {
				logger.debug(
						field_148623_a,
						"Skipped playing soundEvent: {}, master volume was zero",
						new Object[] { p_148611_1_.func_147650_b() });
			} else {
				final SoundEventAccessorComposite var2 = field_148622_c
						.func_147680_a(p_148611_1_.func_147650_b());

				if (var2 == null) {
					logger.warn(field_148623_a,
							"Unable to play unknown soundEvent: {}",
							new Object[] { p_148611_1_.func_147650_b() });
				} else {
					final SoundPoolEntry var3 = var2.func_148720_g();

					if (var3 == SoundHandler.field_147700_a) {
						logger.warn(field_148623_a,
								"Unable to play empty soundEvent: {}",
								new Object[] { var2.func_148729_c() });
					} else {
						final float var4 = p_148611_1_.func_147653_e();
						float var5 = 16.0F;

						if (var4 > 1.0F) {
							var5 *= var4;
						}

						final SoundCategory var6 = var2.func_148728_d();
						final float var7 = func_148594_a(p_148611_1_, var3,
								var6);
						final double var8 = func_148606_a(p_148611_1_, var3);
						final ResourceLocation var10 = var3.func_148652_a();

						if (var7 == 0.0F) {
							logger.debug(
									field_148623_a,
									"Skipped playing sound {}, volume was zero.",
									new Object[] { var10 });
						} else {
							final boolean var11 = p_148611_1_.func_147657_c()
									&& p_148611_1_.func_147652_d() == 0;
							final String var12 = UUID.randomUUID().toString();

							if (var3.func_148648_d()) {
								field_148620_e.newStreamingSource(false, var12,
										func_148612_a(var10), var10.toString(),
										var11, p_148611_1_.func_147649_g(),
										p_148611_1_.func_147654_h(),
										p_148611_1_.func_147651_i(),
										p_148611_1_.func_147656_j()
												.func_148586_a(), var5);
							} else {
								field_148620_e.newSource(false, var12,
										func_148612_a(var10), var10.toString(),
										var11, p_148611_1_.func_147649_g(),
										p_148611_1_.func_147654_h(),
										p_148611_1_.func_147651_i(),
										p_148611_1_.func_147656_j()
												.func_148586_a(), var5);
							}

							logger.debug(
									field_148623_a,
									"Playing sound {} for event {} as channel {}",
									new Object[] { var3.func_148652_a(),
											var2.func_148729_c(), var12 });
							field_148620_e.setPitch(var12, (float) var8);
							field_148620_e.setVolume(var12, var7);
							field_148620_e.play(var12);
							field_148624_n.put(var12,
									Integer.valueOf(field_148618_g + 20));
							field_148629_h.put(var12, p_148611_1_);
							field_148627_j.put(p_148611_1_, var3);

							if (var6 != SoundCategory.MASTER) {
								field_148628_k.put(var6, var12);
							}

							if (p_148611_1_ instanceof ITickableSound) {
								field_148625_l.add(p_148611_1_);
							}
						}
					}
				}
			}
		}
	}

	public void func_148613_b() {
		if (field_148617_f) {
			func_148614_c();
			field_148620_e.cleanup();
			field_148617_f = false;
		}
	}

	public void func_148614_c() {
		if (field_148617_f) {
			final Iterator var1 = field_148629_h.keySet().iterator();

			while (var1.hasNext()) {
				final String var2 = (String) var1.next();
				field_148620_e.stop(var2);
			}

			field_148629_h.clear();
			field_148626_m.clear();
			field_148625_l.clear();
			field_148628_k.clear();
			field_148627_j.clear();
			field_148624_n.clear();
		}
	}

	public void func_148615_a(EntityPlayer p_148615_1_, float p_148615_2_) {
		if (field_148617_f && p_148615_1_ != null) {
			final float var3 = p_148615_1_.prevRotationPitch
					+ (p_148615_1_.rotationPitch - p_148615_1_.prevRotationPitch)
					* p_148615_2_;
			final float var4 = p_148615_1_.prevRotationYaw
					+ (p_148615_1_.rotationYaw - p_148615_1_.prevRotationYaw)
					* p_148615_2_;
			final double var5 = p_148615_1_.prevPosX
					+ (p_148615_1_.posX - p_148615_1_.prevPosX) * p_148615_2_;
			final double var7 = p_148615_1_.prevPosY
					+ (p_148615_1_.posY - p_148615_1_.prevPosY) * p_148615_2_;
			final double var9 = p_148615_1_.prevPosZ
					+ (p_148615_1_.posZ - p_148615_1_.prevPosZ) * p_148615_2_;
			final float var11 = MathHelper.cos((var4 + 90.0F) * 0.017453292F);
			final float var12 = MathHelper.sin((var4 + 90.0F) * 0.017453292F);
			final float var13 = MathHelper.cos(-var3 * 0.017453292F);
			final float var14 = MathHelper.sin(-var3 * 0.017453292F);
			final float var15 = MathHelper.cos((-var3 + 90.0F) * 0.017453292F);
			final float var16 = MathHelper.sin((-var3 + 90.0F) * 0.017453292F);
			final float var17 = var11 * var13;
			final float var19 = var12 * var13;
			final float var20 = var11 * var15;
			final float var22 = var12 * var15;
			field_148620_e.setListenerPosition((float) var5, (float) var7,
					(float) var9);
			field_148620_e.setListenerOrientation(var17, var14, var19, var20,
					var16, var22);
		}
	}
}
