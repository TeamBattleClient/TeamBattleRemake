package net.minecraft.src;

import java.awt.Dimension;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.minecraft.client.renderer.GLAllocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Mipmaps {
	public static void allocateMipmapTextures(int width, int height, String name) {
		final Dimension[] dims = makeMipmapDimensions(width, height, name);

		for (int i = 0; i < dims.length; ++i) {
			final Dimension dim = dims[i];
			final int mipWidth = dim.width;
			final int mipHeight = dim.height;
			final int level = i + 1;
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, level, GL11.GL_RGBA,
					mipWidth, mipHeight, 0, GL12.GL_BGRA,
					GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer) null);
		}
	}

	private static int alphaBlend(int c1, int c2) {
		int a1 = (c1 & -16777216) >> 24 & 255;
		int a2 = (c2 & -16777216) >> 24 & 255;
		int ax = (a1 + a2) / 2;

		if (a1 == 0 && a2 == 0) {
			a1 = 1;
			a2 = 1;
		} else {
			if (a1 == 0) {
				c1 = c2;
				ax /= 2;
			}

			if (a2 == 0) {
				c2 = c1;
				ax /= 2;
			}
		}

		final int r1 = (c1 >> 16 & 255) * a1;
		final int g1 = (c1 >> 8 & 255) * a1;
		final int b1 = (c1 & 255) * a1;
		final int r2 = (c2 >> 16 & 255) * a2;
		final int g2 = (c2 >> 8 & 255) * a2;
		final int b2 = (c2 & 255) * a2;
		final int rx = (r1 + r2) / (a1 + a2);
		final int gx = (g1 + g2) / (a1 + a2);
		final int bx = (b1 + b2) / (a1 + a2);
		return ax << 24 | rx << 16 | gx << 8 | bx;
	}

	public static int alphaBlend(int c1, int c2, int c3, int c4) {
		final int cx1 = alphaBlend(c1, c2);
		final int cx2 = alphaBlend(c3, c4);
		final int cx = alphaBlend(cx1, cx2);
		return cx;
	}

	public static int[][] generateMipMapData(int[] data, int width, int height,
			Dimension[] mipmapDimensions) {
		int[] parMipData = data;
		int parWidth = width;
		boolean scale = true;
		final int[][] mipmapDatas = new int[mipmapDimensions.length][];

		for (int i = 0; i < mipmapDimensions.length; ++i) {
			final Dimension dim = mipmapDimensions[i];
			final int mipWidth = dim.width;
			final int mipHeight = dim.height;
			final int[] mipData = new int[mipWidth * mipHeight];
			mipmapDatas[i] = mipData;
			if (scale) {
				for (int mipX = 0; mipX < mipWidth; ++mipX) {
					for (int mipY = 0; mipY < mipHeight; ++mipY) {
						final int p1 = parMipData[mipX * 2 + 0 + (mipY * 2 + 0)
								* parWidth];
						final int p2 = parMipData[mipX * 2 + 1 + (mipY * 2 + 0)
								* parWidth];
						final int p3 = parMipData[mipX * 2 + 1 + (mipY * 2 + 1)
								* parWidth];
						final int p4 = parMipData[mipX * 2 + 0 + (mipY * 2 + 1)
								* parWidth];
						final int pixel = alphaBlend(p1, p2, p3, p4);
						mipData[mipX + mipY * mipWidth] = pixel;
					}
				}
			}

			parMipData = mipData;
			parWidth = mipWidth;

			if (mipWidth <= 1 || mipHeight <= 1) {
				scale = false;
			}
		}

		return mipmapDatas;
	}

	public static IntBuffer[] makeMipmapBuffers(Dimension[] mipmapDimensions,
			int[][] mipmapDatas) {
		if (mipmapDimensions == null)
			return null;
		else {
			final IntBuffer[] mipmapBuffers = new IntBuffer[mipmapDimensions.length];

			for (int i = 0; i < mipmapDimensions.length; ++i) {
				final Dimension dim = mipmapDimensions[i];
				final int bufLen = dim.width * dim.height;
				final IntBuffer buf = GLAllocation
						.createDirectIntBuffer(bufLen);
				final int[] data = mipmapDatas[i];
				buf.clear();
				buf.put(data);
				buf.clear();
				mipmapBuffers[i] = buf;
			}

			return mipmapBuffers;
		}
	}

	public static Dimension[] makeMipmapDimensions(int width, int height,
			String iconName) {
		final int texWidth = TextureUtils.ceilPowerOfTwo(width);
		final int texHeight = TextureUtils.ceilPowerOfTwo(height);

		if (texWidth == width && texHeight == height) {
			final ArrayList listDims = new ArrayList();
			int mipWidth = texWidth;
			int mipHeight = texHeight;

			while (true) {
				mipWidth /= 2;
				mipHeight /= 2;

				if (mipWidth <= 0 && mipHeight <= 0) {
					final Dimension[] mipmapDimensions1 = (Dimension[]) listDims
							.toArray(new Dimension[listDims.size()]);
					return mipmapDimensions1;
				}

				if (mipWidth <= 0) {
					mipWidth = 1;
				}

				if (mipHeight <= 0) {
					mipHeight = 1;
				}

				final Dimension dim = new Dimension(mipWidth, mipHeight);
				listDims.add(dim);
			}
		} else {
			Config.warn("Mipmaps not possible (power of 2 dimensions needed), texture: "
					+ iconName + ", dim: " + width + "x" + height);
			return new Dimension[0];
		}
	}

	private IntBuffer[] mipmapBuffers;

	private final int[][] mipmapDatas;

	private final Dimension[] mipmapDimensions;

	public Mipmaps(String iconName, int width, int height, int[] data,
			boolean direct) {
		mipmapDimensions = makeMipmapDimensions(width, height, iconName);
		mipmapDatas = generateMipMapData(data, width, height, mipmapDimensions);

		if (direct) {
			mipmapBuffers = makeMipmapBuffers(mipmapDimensions, mipmapDatas);
		}
	}
}
