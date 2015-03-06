package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

public class LayeredTexture extends AbstractTexture {
	private static final Logger logger = LogManager.getLogger();
	public final List layeredTextureNames;

	public LayeredTexture(String... p_i1274_1_) {
		layeredTextureNames = Lists.newArrayList(p_i1274_1_);
	}

	@Override
	public void loadTexture(IResourceManager p_110551_1_) throws IOException {
		func_147631_c();
		BufferedImage var2 = null;

		try {
			final Iterator var3 = layeredTextureNames.iterator();

			while (var3.hasNext()) {
				final String var4 = (String) var3.next();

				if (var4 != null) {
					final InputStream var5 = p_110551_1_.getResource(
							new ResourceLocation(var4)).getInputStream();
					final BufferedImage var6 = ImageIO.read(var5);

					if (var2 == null) {
						var2 = new BufferedImage(var6.getWidth(),
								var6.getHeight(), 2);
					}

					var2.getGraphics().drawImage(var6, 0, 0,
							(ImageObserver) null);
				}
			}
		} catch (final IOException var7) {
			logger.error("Couldn\'t load layered image", var7);
			return;
		}

		TextureUtil.uploadTextureImage(getGlTextureId(), var2);
	}
}
