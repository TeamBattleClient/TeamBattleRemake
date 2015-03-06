package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.src.RandomMobs;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TextureManager implements ITickable,
		IResourceManagerReloadListener {
	private static final Logger logger = LogManager.getLogger();
	private final List listTickables = Lists.newArrayList();
	private final Map mapResourceLocations = Maps.newHashMap();
	private final Map mapTextureCounters = Maps.newHashMap();
	private final Map mapTextureObjects = Maps.newHashMap();
	private final IResourceManager theResourceManager;

	public TextureManager(IResourceManager par1ResourceManager) {
		theResourceManager = par1ResourceManager;
	}

	public void bindTexture(ResourceLocation par1ResourceLocation) {
		if (Config.isRandomMobs()) {
			par1ResourceLocation = RandomMobs
					.getTextureLocation(par1ResourceLocation);
		}

		Object var2 = mapTextureObjects.get(par1ResourceLocation);

		if (var2 == null) {
			var2 = new SimpleTexture(par1ResourceLocation);
			loadTexture(par1ResourceLocation, (ITextureObject) var2);
		}

		TextureUtil.bindTexture(((ITextureObject) var2).getGlTextureId());
	}

	public void func_147645_c(ResourceLocation p_147645_1_) {
		final ITextureObject var2 = getTexture(p_147645_1_);

		if (var2 != null) {
			TextureUtil.deleteTexture(var2.getGlTextureId());
		}
	}

	public ResourceLocation getDynamicTextureLocation(String par1Str,
			DynamicTexture par2DynamicTexture) {
		Integer var3 = (Integer) mapTextureCounters.get(par1Str);

		if (var3 == null) {
			var3 = Integer.valueOf(1);
		} else {
			var3 = Integer.valueOf(var3.intValue() + 1);
		}

		mapTextureCounters.put(par1Str, var3);
		final ResourceLocation var4 = new ResourceLocation(String.format(
				"dynamic/%s_%d", new Object[] { par1Str, var3 }));
		loadTexture(var4, par2DynamicTexture);
		return var4;
	}

	public ResourceLocation getResourceLocation(int par1) {
		return (ResourceLocation) mapResourceLocations.get(Integer
				.valueOf(par1));
	}

	public ITextureObject getTexture(ResourceLocation par1ResourceLocation) {
		return (ITextureObject) mapTextureObjects.get(par1ResourceLocation);
	}

	public boolean loadTexture(ResourceLocation par1ResourceLocation,
			final ITextureObject par2TextureObject) {
		boolean var3 = true;
		Object par2TextureObject2 = par2TextureObject;

		try {
			par2TextureObject.loadTexture(theResourceManager);
		} catch (final IOException var8) {
			logger.warn("Failed to load texture: " + par1ResourceLocation, var8);
			par2TextureObject2 = TextureUtil.missingTexture;
			mapTextureObjects.put(par1ResourceLocation, par2TextureObject2);
			var3 = false;
		} catch (final Throwable var9) {
			final CrashReport var5 = CrashReport.makeCrashReport(var9,
					"Registering texture");
			final CrashReportCategory var6 = var5
					.makeCategory("Resource location being registered");
			var6.addCrashSection("Resource location", par1ResourceLocation);
			var6.addCrashSectionCallable("Texture object class",
					new Callable() {

						@Override
						public String call() {
							return par2TextureObject.getClass().getName();
						}
					});
			throw new ReportedException(var5);
		}

		mapTextureObjects.put(par1ResourceLocation, par2TextureObject2);
		return var3;
	}

	public boolean loadTextureMap(ResourceLocation par1ResourceLocation,
			TextureMap par2TextureMap) {
		if (loadTickableTexture(par1ResourceLocation, par2TextureMap)) {
			mapResourceLocations.put(
					Integer.valueOf(par2TextureMap.getTextureType()),
					par1ResourceLocation);
			return true;
		} else
			return false;
	}

	public boolean loadTickableTexture(ResourceLocation par1ResourceLocation,
			ITickableTextureObject par2TickableTextureObject) {
		if (loadTexture(par1ResourceLocation, par2TickableTextureObject)) {
			listTickables.add(par2TickableTextureObject);
			return true;
		} else
			return false;
	}

	@Override
	public void onResourceManagerReload(IResourceManager par1ResourceManager) {
		Config.dbg("*** Reloading textures ***");
		Config.log("Resource packs: " + Config.getResourcePackNames());
		final Iterator it = mapTextureObjects.keySet().iterator();

		while (it.hasNext()) {
			final ResourceLocation var2 = (ResourceLocation) it.next();

			if (var2.getResourcePath().startsWith("mcpatcher/")) {
				final ITextureObject var3 = (ITextureObject) mapTextureObjects
						.get(var2);
				final int glTexId = var3.getGlTextureId();

				if (glTexId > 0) {
					GL11.glDeleteTextures(glTexId);
				}

				it.remove();
			}
		}

		final Iterator var21 = mapTextureObjects.entrySet().iterator();

		while (var21.hasNext()) {
			final Entry var31 = (Entry) var21.next();
			loadTexture((ResourceLocation) var31.getKey(),
					(ITextureObject) var31.getValue());
		}
	}

	@Override
	public void tick() {
		final Iterator var1 = listTickables.iterator();

		while (var1.hasNext()) {
			final ITickable var2 = (ITickable) var1.next();
			var2.tick();
		}
	}
}
