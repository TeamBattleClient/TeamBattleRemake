package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData extends SimpleTexture {
	private static final AtomicInteger field_147643_d = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private BufferedImage bufferedImage;
	private final File field_152434_e;
	private final IImageBuffer imageBuffer;
	private Thread imageThread;
	private final String imageUrl;
	private boolean textureUploaded;

	public ThreadDownloadImageData(File p_i1049_1_, String p_i1049_2_,
			ResourceLocation p_i1049_3_, IImageBuffer p_i1049_4_) {
		super(p_i1049_3_);
		field_152434_e = p_i1049_1_;
		imageUrl = p_i1049_2_;
		imageBuffer = p_i1049_4_;
	}

	private void func_147640_e() {
		if (!textureUploaded) {
			if (bufferedImage != null) {
				if (textureLocation != null) {
					func_147631_c();
				}

				TextureUtil.uploadTextureImage(super.getGlTextureId(),
						bufferedImage);
				textureUploaded = true;
			}
		}
	}

	public void func_147641_a(BufferedImage p_147641_1_) {
		bufferedImage = p_147641_1_;

		if (imageBuffer != null) {
			imageBuffer.func_152634_a();
		}
	}

	protected void func_152433_a() {
		imageThread = new Thread("Texture Downloader #"
				+ field_147643_d.incrementAndGet()) {

			@Override
			public void run() {
				HttpURLConnection var1 = null;
				ThreadDownloadImageData.logger.debug(
						"Downloading http texture from {} to {}", new Object[] {
								imageUrl, field_152434_e });

				try {
					var1 = (HttpURLConnection) new URL(imageUrl)
							.openConnection(Minecraft.getMinecraft().getProxy());
					var1.setDoInput(true);
					var1.setDoOutput(false);
					var1.connect();

					if (var1.getResponseCode() / 100 != 2)
						return;

					BufferedImage var2;

					if (field_152434_e != null) {
						FileUtils.copyInputStreamToFile(var1.getInputStream(),
								field_152434_e);
						var2 = ImageIO.read(field_152434_e);
					} else {
						var2 = ImageIO.read(var1.getInputStream());
					}

					if (imageBuffer != null) {
						var2 = imageBuffer.parseUserSkin(var2);
					}

					ThreadDownloadImageData.this.func_147641_a(var2);
				} catch (final Exception var6) {
					ThreadDownloadImageData.logger.error(
							"Couldn\'t download http texture", var6);
				} finally {
					if (var1 != null) {
						var1.disconnect();
					}
				}
			}
		};
		imageThread.setDaemon(true);
		imageThread.start();
	}

	@Override
	public int getGlTextureId() {
		func_147640_e();
		return super.getGlTextureId();
	}

	@Override
	public void loadTexture(IResourceManager p_110551_1_) throws IOException {
		if (bufferedImage == null && textureLocation != null) {
			super.loadTexture(p_110551_1_);
		}

		if (imageThread == null) {
			if (field_152434_e != null && field_152434_e.isFile()) {
				logger.debug("Loading http texture from local cache ({})",
						new Object[] { field_152434_e });

				try {
					bufferedImage = ImageIO.read(field_152434_e);

					if (imageBuffer != null) {
						func_147641_a(imageBuffer.parseUserSkin(bufferedImage));
					}
				} catch (final IOException var3) {
					logger.error("Couldn\'t load skin " + field_152434_e, var3);
					func_152433_a();
				}
			} else {
				func_152433_a();
			}
		}
	}
}
