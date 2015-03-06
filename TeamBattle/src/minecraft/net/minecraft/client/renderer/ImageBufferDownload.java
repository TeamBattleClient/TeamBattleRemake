package net.minecraft.client.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload implements IImageBuffer {
	private int[] imageData;
	private int imageHeight;
	private int imageWidth;

	@Override
	public void func_152634_a() {
	}

	/**
	 * Returns true if the given area of the image contains transparent pixels
	 */
	private boolean hasTransparency(int par1, int par2, int par3, int par4) {
		for (int var5 = par1; var5 < par3; ++var5) {
			for (int var6 = par2; var6 < par4; ++var6) {
				final int var7 = imageData[var5 + var6 * imageWidth];

				if ((var7 >> 24 & 255) < 128)
					return true;
			}
		}

		return false;
	}

	@Override
	public BufferedImage parseUserSkin(BufferedImage par1BufferedImage) {
		if (par1BufferedImage == null)
			return null;
		else {
			imageWidth = 64;
			imageHeight = 32;
			final int srcWidth = par1BufferedImage.getWidth();
			final int srcHeight = par1BufferedImage.getHeight();

			if (srcWidth != 64 || srcHeight != 32 && srcHeight != 64) {
				while (imageWidth < srcWidth || imageHeight < srcHeight) {
					imageWidth *= 2;
					imageHeight *= 2;
				}
			}

			final BufferedImage bufferedimage = new BufferedImage(imageWidth,
					imageHeight, 2);
			final Graphics g = bufferedimage.getGraphics();
			g.drawImage(par1BufferedImage, 0, 0, (ImageObserver) null);
			g.dispose();
			imageData = ((DataBufferInt) bufferedimage.getRaster()
					.getDataBuffer()).getData();
			final int w = imageWidth;
			final int h = imageHeight;
			setAreaOpaque(0, 0, w / 2, h / 2);
			setAreaTransparent(w / 2, 0, w, h);
			setAreaOpaque(0, h / 2, w, h);
			return bufferedimage;
		}
	}

	/**
	 * Makes the given area of the image opaque
	 */
	private void setAreaOpaque(int par1, int par2, int par3, int par4) {
		for (int var5 = par1; var5 < par3; ++var5) {
			for (int var6 = par2; var6 < par4; ++var6) {
				imageData[var5 + var6 * imageWidth] |= -16777216;
			}
		}
	}

	/**
	 * Makes the given area of the image transparent if it was previously
	 * completely opaque (used to remove the outer layer of a skin around the
	 * head if it was saved all opaque; this would be redundant so it's assumed
	 * that the skin maker is just using an image editor without an alpha
	 * channel)
	 */
	private void setAreaTransparent(int par1, int par2, int par3, int par4) {
		if (!hasTransparency(par1, par2, par3, par4)) {
			for (int var5 = par1; var5 < par3; ++var5) {
				for (int var6 = par2; var6 < par4; ++var6) {
					imageData[var5 + var6 * imageWidth] &= 16777215;
				}
			}
		}
	}
}
