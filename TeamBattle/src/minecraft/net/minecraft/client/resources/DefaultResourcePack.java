package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.ImmutableSet;

public class DefaultResourcePack implements IResourcePack {
	public static final Set defaultResourceDomains = ImmutableSet.of(
			"minecraft", "realms");
	private final Map field_152781_b;

	public DefaultResourcePack(Map p_i1046_1_) {
		field_152781_b = p_i1046_1_;
	}

	public InputStream func_152780_c(ResourceLocation p_152780_1_)
			throws IOException {
		final File var2 = (File) field_152781_b.get(p_152780_1_.toString());
		return var2 != null && var2.isFile() ? new FileInputStream(var2) : null;
	}

	@Override
	public InputStream getInputStream(ResourceLocation p_110590_1_)
			throws IOException {
		final InputStream var2 = getResourceStream(p_110590_1_);

		if (var2 != null)
			return var2;
		else {
			final InputStream var3 = func_152780_c(p_110590_1_);

			if (var3 != null)
				return var3;
			else
				throw new FileNotFoundException(p_110590_1_.getResourcePath());
		}
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return ImageIO.read(DefaultResourcePack.class.getResourceAsStream("/"
				+ new ResourceLocation("pack.png").getResourcePath()));
	}

	@Override
	public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_,
			String p_135058_2_) throws IOException {
		try {
			final FileInputStream var3 = new FileInputStream(
					(File) field_152781_b.get("pack.mcmeta"));
			return AbstractResourcePack.readMetadata(p_135058_1_, var3,
					p_135058_2_);
		} catch (final RuntimeException var4) {
			return null;
		} catch (final FileNotFoundException var5) {
			return null;
		}
	}

	@Override
	public String getPackName() {
		return "Default";
	}

	@Override
	public Set getResourceDomains() {
		return defaultResourceDomains;
	}

	private InputStream getResourceStream(ResourceLocation p_110605_1_) {
		return DefaultResourcePack.class.getResourceAsStream("/assets/"
				+ p_110605_1_.getResourceDomain() + "/"
				+ p_110605_1_.getResourcePath());
	}

	@Override
	public boolean resourceExists(ResourceLocation p_110589_1_) {
		return getResourceStream(p_110589_1_) != null
				|| field_152781_b.containsKey(p_110589_1_.toString());
	}
}
