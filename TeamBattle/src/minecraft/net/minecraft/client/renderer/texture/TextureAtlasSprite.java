package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Mipmaps;
import net.minecraft.util.IIcon;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class TextureAtlasSprite implements IIcon {
	private static int[][] func_147962_a(int[][] p_147962_0_, int p_147962_1_,
			int p_147962_2_, int p_147962_3_) {
		final int[][] var4 = new int[p_147962_0_.length][];

		for (int var5 = 0; var5 < p_147962_0_.length; ++var5) {
			final int[] var6 = p_147962_0_[var5];

			if (var6 != null) {
				var4[var5] = new int[(p_147962_1_ >> var5)
						* (p_147962_2_ >> var5)];
				System.arraycopy(var6, p_147962_3_ * var4[var5].length,
						var4[var5], 0, var4[var5].length);
			}
		}

		return var4;
	}

	private AnimationMetadataSection animationMetadata;
	public float baseU;
	public float baseV;
	private boolean field_147966_k;
	public IntBuffer[] frameBuffers;
	protected int frameCounter;
	public Mipmaps[] frameMipmaps;
	protected List framesTextureData = Lists.newArrayList();
	public int glOwnTextureId = -1;
	protected int height;
	private final String iconName;
	private int indexInMap = -1;
	private float maxU;
	private float maxV;

	private float minU;
	private float minV;
	protected int originX;
	protected int originY;
	protected boolean rotated;
	public int sheetHeight;
	public int sheetWidth;
	protected int tickCounter;
	private int uploadedFrameIndex = -1;
	private int uploadedOwnFrameIndex = -1;

	protected int width;

	protected TextureAtlasSprite(String par1Str) {
		iconName = par1Str;
	}

	private void allocateFrameTextureData(int par1) {
		if (framesTextureData.size() <= par1) {
			for (int var2 = framesTextureData.size(); var2 <= par1; ++var2) {
				framesTextureData.add((Object) null);
			}
		}
	}

	public void bindOwnTexture() {
	}

	public void bindUploadOwnTexture() {
		bindOwnTexture();
		this.uploadFrameTexture(frameCounter, 0, 0);
	}

	public void clearFramesTextureData() {
		framesTextureData.clear();
	}

	public void copyFrom(TextureAtlasSprite par1TextureAtlasSprite) {
		originX = par1TextureAtlasSprite.originX;
		originY = par1TextureAtlasSprite.originY;
		width = par1TextureAtlasSprite.width;
		height = par1TextureAtlasSprite.height;
		rotated = par1TextureAtlasSprite.rotated;
		minU = par1TextureAtlasSprite.minU;
		maxU = par1TextureAtlasSprite.maxU;
		minV = par1TextureAtlasSprite.minV;
		maxV = par1TextureAtlasSprite.maxV;
		baseU = Math.min(minU, maxU);
		baseV = Math.min(minV, maxV);
	}

	public void deleteOwnTexture() {
		if (glOwnTextureId >= 0) {
			GL11.glDeleteTextures(glOwnTextureId);
			glOwnTextureId = -1;
		}
	}

	private int[][] func_147960_a(int[][] p_147960_1_, int p_147960_2_,
			int p_147960_3_) {
		if (!field_147966_k)
			return p_147960_1_;
		else {
			final int[][] var4 = new int[p_147960_1_.length][];

			for (int var5 = 0; var5 < p_147960_1_.length; ++var5) {
				final int[] var6 = p_147960_1_[var5];

				if (var6 != null) {
					final int[] var7 = new int[(p_147960_2_ + 16 >> var5)
							* (p_147960_3_ + 16 >> var5)];
					System.arraycopy(var6, 0, var7, 0, var6.length);
					var4[var5] = TextureUtil
							.func_147948_a(var7, p_147960_2_ >> var5,
									p_147960_3_ >> var5, 8 >> var5);
				}
			}

			return var4;
		}
	}

	private void func_147961_a(int[][] p_147961_1_) {
		final int[] var2 = p_147961_1_[0];
		int var3 = 0;
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;
		int var7;

		for (var7 = 0; var7 < var2.length; ++var7) {
			if ((var2[var7] & -16777216) != 0) {
				var4 += var2[var7] >> 16 & 255;
				var5 += var2[var7] >> 8 & 255;
				var6 += var2[var7] >> 0 & 255;
				++var3;
			}
		}

		if (var3 != 0) {
			var4 /= var3;
			var5 /= var3;
			var6 /= var3;

			for (var7 = 0; var7 < var2.length; ++var7) {
				if ((var2[var7] & -16777216) == 0) {
					var2[var7] = var4 << 16 | var5 << 8 | var6;
				}
			}
		}
	}

	public void func_147963_d(int p_147963_1_) {
		final ArrayList var2 = Lists.newArrayList();

		for (int var3 = 0; var3 < framesTextureData.size(); ++var3) {
			final int[][] var4 = (int[][]) framesTextureData.get(var3);

			if (var4 != null) {
				try {
					var2.add(TextureUtil
							.func_147949_a(p_147963_1_, width, var4));
				} catch (final Throwable var8) {
					final CrashReport var6 = CrashReport.makeCrashReport(var8,
							"Generating mipmaps for frame");
					final CrashReportCategory var7 = var6
							.makeCategory("Frame being iterated");
					var7.addCrashSection("Frame index", Integer.valueOf(var3));
					var7.addCrashSectionCallable("Frame sizes", new Callable() {
						@Override
						public String call() {
							final StringBuilder var1 = new StringBuilder();
							final int[][] var2 = var4;
							final int var3 = var2.length;

							for (int var4x = 0; var4x < var3; ++var4x) {
								final int[] var5 = var2[var4x];

								if (var1.length() > 0) {
									var1.append(", ");
								}

								var1.append(var5 == null ? "null" : Integer
										.valueOf(var5.length));
							}

							return var1.toString();
						}
					});
					throw new ReportedException(var6);
				}
			}
		}

		setFramesTextureData(var2);
	}

	public void func_147964_a(BufferedImage[] p_147964_1_,
			AnimationMetadataSection p_147964_2_, boolean p_147964_3_) {
		resetSprite();
		field_147966_k = p_147964_3_;
		final int var4 = p_147964_1_[0].getWidth();
		final int var5 = p_147964_1_[0].getHeight();
		width = var4;
		height = var5;

		if (p_147964_3_) {
			width += 16;
			height += 16;
		}

		final int[][] var6 = new int[p_147964_1_.length][];
		int var7;

		for (var7 = 0; var7 < p_147964_1_.length; ++var7) {
			final BufferedImage var12 = p_147964_1_[var7];

			if (var12 != null) {
				if (var7 > 0
						&& (var12.getWidth() != var4 >> var7 || var12
								.getHeight() != var5 >> var7))
					throw new RuntimeException(
							String.format(
									"Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d",
									new Object[] { Integer.valueOf(var7),
											Integer.valueOf(var12.getWidth()),
											Integer.valueOf(var12.getHeight()),
											Integer.valueOf(var4 >> var7),
											Integer.valueOf(var5 >> var7) }));

				var6[var7] = new int[var12.getWidth() * var12.getHeight()];
				var12.getRGB(0, 0, var12.getWidth(), var12.getHeight(),
						var6[var7], 0, var12.getWidth());
			}
		}

		if (p_147964_2_ == null) {
			if (var5 != var4)
				throw new RuntimeException(
						"broken aspect ratio and not an animation");

			func_147961_a(var6);
			framesTextureData.add(func_147960_a(var6, var4, var5));
		} else {
			var7 = var5 / var4;
			final int var121 = var4;
			final int var9 = var4;
			height = width;
			int var11;

			if (p_147964_2_.getFrameCount() > 0) {
				final Iterator var13 = p_147964_2_.getFrameIndexSet()
						.iterator();

				while (var13.hasNext()) {
					var11 = ((Integer) var13.next()).intValue();

					if (var11 >= var7)
						throw new RuntimeException("invalid frameindex "
								+ var11);

					allocateFrameTextureData(var11);
					framesTextureData.set(
							var11,
							func_147960_a(
									func_147962_a(var6, var121, var9, var11),
									var121, var9));
				}

				animationMetadata = p_147964_2_;
			} else {
				final ArrayList var131 = Lists.newArrayList();

				for (var11 = 0; var11 < var7; ++var11) {
					framesTextureData.add(func_147960_a(
							func_147962_a(var6, var121, var9, var11), var121,
							var9));
					var131.add(new AnimationFrame(var11, -1));
				}

				animationMetadata = new AnimationMetadataSection(var131, width,
						height, p_147964_2_.getFrameTime());
			}
		}
	}

	public int[][] func_147965_a(int p_147965_1_) {
		return (int[][]) framesTextureData.get(p_147965_1_);
	}

	public int getFrameCount() {
		return framesTextureData.size();
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Returns the height of the icon, in pixels.
	 */
	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	public String getIconName() {
		return iconName;
	}

	/**
	 * Returns the width of the icon, in pixels.
	 */
	@Override
	public int getIconWidth() {
		return width;
	}

	public int getIndexInMap() {
		return indexInMap;
	}

	/**
	 * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax.
	 * Other arguments return in-between values.
	 */
	@Override
	public float getInterpolatedU(double par1) {
		final float var3 = maxU - minU;
		return minU + var3 * (float) par1 / 16.0F;
	}

	/**
	 * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax.
	 * Other arguments return in-between values.
	 */
	@Override
	public float getInterpolatedV(double par1) {
		final float var3 = maxV - minV;
		return minV + var3 * ((float) par1 / 16.0F);
	}

	/**
	 * Returns the maximum U coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMaxU() {
		return maxU;
	}

	/**
	 * Returns the maximum V coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMaxV() {
		return maxV;
	}

	/**
	 * Returns the minimum U coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMinU() {
		return minU;
	}

	/**
	 * Returns the minimum V coordinate to use when rendering with this icon.
	 */
	@Override
	public float getMinV() {
		return minV;
	}

	/**
	 * Returns the X position of this icon on its texture sheet, in pixels.
	 */
	public int getOriginX() {
		return originX;
	}

	/**
	 * Returns the Y position of this icon on its texture sheet, in pixels.
	 */
	public int getOriginY() {
		return originY;
	}

	public int getWidth() {
		return width;
	}

	public boolean hasAnimationMetadata() {
		return animationMetadata != null;
	}

	public boolean hasCustomLoader(IResourceManager manager,
			ResourceLocation location) {
		return false;
	}

	public void initSprite(int par1, int par2, int par3, int par4, boolean par5) {
		originX = par3;
		originY = par4;
		rotated = par5;
		final float var6 = (float) (0.009999999776482582D / par1);
		final float var7 = (float) (0.009999999776482582D / par2);
		minU = par3 / (float) par1 + var6;
		maxU = (par3 + width) / (float) par1 - var6;
		minV = (float) par4 / (float) par2 + var7;
		maxV = (float) (par4 + height) / (float) par2 - var7;

		if (field_147966_k) {
			final float var8 = 8.0F / par1;
			final float var9 = 8.0F / par2;
			minU += var8;
			maxU -= var8;
			minV += var9;
			maxV -= var9;
		}

		baseU = Math.min(minU, maxU);
		baseV = Math.min(minV, maxV);
	}

	public boolean load(IResourceManager manager, ResourceLocation location) {
		return true;
	}

	private void resetSprite() {
		animationMetadata = null;
		setFramesTextureData(Lists.newArrayList());
		frameCounter = 0;
		tickCounter = 0;
		deleteOwnTexture();
		uploadedFrameIndex = -1;
		uploadedOwnFrameIndex = -1;
		frameBuffers = null;
		frameMipmaps = null;
	}

	public void setFramesTextureData(List par1List) {
		framesTextureData = par1List;

		for (int i = 0; i < framesTextureData.size(); ++i) {
			if (framesTextureData.get(i) == null) {
				framesTextureData.set(i, new int[width * height]);
			}
		}
	}

	public void setIconHeight(int par1) {
		height = par1;
	}

	public void setIconWidth(int par1) {
		width = par1;
	}

	public void setIndexInMap(int indexInMap) {
		this.indexInMap = indexInMap;
	}

	public void setMipmapActive(boolean mipmapActive) {
		frameMipmaps = null;
	}

	@Override
	public String toString() {
		return "TextureAtlasSprite{name=\'" + iconName + '\'' + ", frameCount="
				+ framesTextureData.size() + ", rotated=" + rotated + ", x="
				+ originX + ", y=" + originY + ", height=" + height
				+ ", width=" + width + ", u0=" + minU + ", u1=" + maxU
				+ ", v0=" + minV + ", v1=" + maxV + '}';
	}

	public void updateAnimation() {
		++tickCounter;

		if (tickCounter >= animationMetadata.getFrameTimeSingle(frameCounter)) {
			final int var1 = animationMetadata.getFrameIndex(frameCounter);
			final int var2 = animationMetadata.getFrameCount() == 0 ? framesTextureData
					.size() : animationMetadata.getFrameCount();
			frameCounter = (frameCounter + 1) % var2;
			tickCounter = 0;
			final int var3 = animationMetadata.getFrameIndex(frameCounter);

			if (var1 != var3 && var3 >= 0 && var3 < framesTextureData.size()) {
				TextureUtil.func_147955_a(
						(int[][]) framesTextureData.get(var3), width, height,
						originX, originY, false, false);
				uploadedFrameIndex = var3;
			}
		}
	}

	public void uploadFrameTexture() {
		this.uploadFrameTexture(frameCounter, originX, originY);
	}

	public void uploadFrameTexture(int frameIndex, int xPos, int yPos) {
	}

	public void uploadOwnAnimation() {
		if (uploadedFrameIndex != uploadedOwnFrameIndex) {
			TextureUtil.bindTexture(glOwnTextureId);
			this.uploadFrameTexture(uploadedFrameIndex, 0, 0);
			uploadedOwnFrameIndex = uploadedFrameIndex;
		}
	}
}
