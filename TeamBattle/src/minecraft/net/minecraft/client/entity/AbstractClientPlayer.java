package net.minecraft.client.entity;

import java.awt.image.BufferedImage;
import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

public abstract class AbstractClientPlayer extends EntityPlayer implements
		SkinManager.SkinAvailableCallback {
	static final class SwitchType {
		static final int[] field_152630_a = new int[Type.values().length];

		static {
			try {
				field_152630_a[Type.SKIN.ordinal()] = 1;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				field_152630_a[Type.CAPE.ordinal()] = 2;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	public static final ResourceLocation locationStevePng = new ResourceLocation(
			"textures/entity/steve.png");

	public static ThreadDownloadImageData getDownloadImageSkin(
			ResourceLocation par0ResourceLocation, String par1Str) {
		final TextureManager var2 = Minecraft.getMinecraft()
				.getTextureManager();
		Object var3 = var2.getTexture(par0ResourceLocation);

		if (var3 == null) {
			var3 = new ThreadDownloadImageData((File) null, String.format(
					"http://skins.minecraft.net/MinecraftSkins/%s.png",
					new Object[] { StringUtils.stripControlCodes(par1Str) }),
					locationStevePng, new ImageBufferDownload());
			var2.loadTexture(par0ResourceLocation, (ITextureObject) var3);
		}

		return (ThreadDownloadImageData) var3;
	}

	public static ResourceLocation getLocationSkin(String par0Str) {
		return new ResourceLocation("skins/"
				+ StringUtils.stripControlCodes(par0Str));
	}

	private ResourceLocation locationCape;

	private ResourceLocation locationSkin;

	public AbstractClientPlayer(World p_i45074_1_, GameProfile p_i45074_2_) {
		super(p_i45074_1_, p_i45074_2_);
		final String var3 = getCommandSenderName();

		if (!var3.isEmpty()) {
			final SkinManager username = Minecraft.getMinecraft()
					.func_152342_ad();
			username.func_152790_a(p_i45074_2_, this, true);
		}

		String username1 = getCommandSenderName();

		if (username1 != null && !username1.isEmpty()) {
			username1 = StringUtils.stripControlCodes(username1);
			final String ofCapeUrl = "http://s.optifine.net/capes/" + username1
					+ ".png";
			final MinecraftProfileTexture mpt = new MinecraftProfileTexture(
					ofCapeUrl);
			final ResourceLocation rl = new ResourceLocation("skins/"
					+ mpt.getHash());
			final IImageBuffer iib = new IImageBuffer() {
				ImageBufferDownload ibd = new ImageBufferDownload();

				@Override
				public void func_152634_a() {
					locationCape = rl;
				}

				@Override
				public BufferedImage parseUserSkin(BufferedImage var1) {
					return ibd.parseUserSkin(var1);
				}
			};
			final ThreadDownloadImageData textureCape = new ThreadDownloadImageData(
					(File) null, mpt.getUrl(), (ResourceLocation) null, iib);
			Minecraft.getMinecraft().getTextureManager()
					.loadTexture(rl, textureCape);
		}
	}

	@Override
	public void func_152121_a(Type p_152121_1_, ResourceLocation p_152121_2_) {
		switch (AbstractClientPlayer.SwitchType.field_152630_a[p_152121_1_
				.ordinal()]) {
		case 1:
			locationSkin = p_152121_2_;
			break;

		case 2:
			locationCape = p_152121_2_;
		}
	}

	public boolean func_152122_n() {
		return !Config.isShowCapes() ? false : locationCape != null;
	}

	public boolean func_152123_o() {
		return locationSkin != null;
	}

	public ResourceLocation getLocationCape() {
		return locationCape;
	}

	public ResourceLocation getLocationSkin() {
		return locationSkin == null ? locationStevePng : locationSkin;
	}
}
