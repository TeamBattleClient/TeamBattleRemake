package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public abstract class AbstractResourcePack implements IResourcePack {
	private static final Logger resourceLog = LogManager.getLogger();

	protected static String getRelativeName(File p_110595_0_, File p_110595_1_) {
		return p_110595_0_.toURI().relativize(p_110595_1_.toURI()).getPath();
	}

	private static String locationToName(ResourceLocation p_110592_0_) {
		return String.format("%s/%s/%s",
				new Object[] { "assets", p_110592_0_.getResourceDomain(),
						p_110592_0_.getResourcePath() });
	}

	static IMetadataSection readMetadata(IMetadataSerializer p_110596_0_,
			InputStream p_110596_1_, String p_110596_2_) {
		JsonObject var3 = null;
		BufferedReader var4 = null;

		try {
			var4 = new BufferedReader(new InputStreamReader(p_110596_1_,
					Charsets.UTF_8));
			var3 = new JsonParser().parse(var4).getAsJsonObject();
		} catch (final RuntimeException var9) {
			throw new JsonParseException(var9);
		} finally {
			IOUtils.closeQuietly(var4);
		}

		return p_110596_0_.parseMetadataSection(p_110596_2_, var3);
	}

	public final File resourcePackFile;

	public AbstractResourcePack(File p_i1287_1_) {
		resourcePackFile = p_i1287_1_;
	}

	@Override
	public InputStream getInputStream(ResourceLocation p_110590_1_)
			throws IOException {
		return getInputStreamByName(locationToName(p_110590_1_));
	}

	protected abstract InputStream getInputStreamByName(String p_110591_1_)
			throws IOException;

	@Override
	public BufferedImage getPackImage() throws IOException {
		return ImageIO.read(getInputStreamByName("pack.png"));
	}

	@Override
	public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_,
			String p_135058_2_) throws IOException {
		return readMetadata(p_135058_1_, getInputStreamByName("pack.mcmeta"),
				p_135058_2_);
	}

	@Override
	public String getPackName() {
		return resourcePackFile.getName();
	}

	protected abstract boolean hasResourceName(String p_110593_1_);

	protected void logNameNotLowercase(String p_110594_1_) {
		resourceLog.warn(
				"ResourcePack: ignored non-lowercase namespace: %s in %s",
				new Object[] { p_110594_1_, resourcePackFile });
	}

	@Override
	public boolean resourceExists(ResourceLocation p_110589_1_) {
		return hasResourceName(locationToName(p_110589_1_));
	}
}
