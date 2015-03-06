package net.minecraft.client.renderer.texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.MathHelper;

import com.google.common.collect.Lists;

public class Stitcher {
	public static class Holder implements Comparable {
		private final int height;
		private final int mipmapLevelHolder;
		private boolean rotated;
		private float scaleFactor = 1.0F;
		private final TextureAtlasSprite theTexture;
		private final int width;

		public Holder(TextureAtlasSprite p_i45094_1_, int p_i45094_2_) {
			theTexture = p_i45094_1_;
			width = p_i45094_1_.getIconWidth();
			height = p_i45094_1_.getIconHeight();
			mipmapLevelHolder = p_i45094_2_;
			rotated = Stitcher.func_147969_b(height, p_i45094_2_) > Stitcher
					.func_147969_b(width, p_i45094_2_);
		}

		@Override
		public int compareTo(Object p_compareTo_1_) {
			return this.compareTo((Stitcher.Holder) p_compareTo_1_);
		}

		public int compareTo(Stitcher.Holder p_compareTo_1_) {
			int var2;

			if (getHeight() == p_compareTo_1_.getHeight()) {
				if (getWidth() == p_compareTo_1_.getWidth()) {
					if (theTexture.getIconName() == null)
						return p_compareTo_1_.theTexture.getIconName() == null ? 0
								: -1;

					return theTexture.getIconName().compareTo(
							p_compareTo_1_.theTexture.getIconName());
				}

				var2 = getWidth() < p_compareTo_1_.getWidth() ? 1 : -1;
			} else {
				var2 = getHeight() < p_compareTo_1_.getHeight() ? 1 : -1;
			}

			return var2;
		}

		public TextureAtlasSprite getAtlasSprite() {
			return theTexture;
		}

		public int getHeight() {
			return rotated ? Stitcher.func_147969_b(
					(int) (width * scaleFactor), mipmapLevelHolder) : Stitcher
					.func_147969_b((int) (height * scaleFactor),
							mipmapLevelHolder);
		}

		public int getWidth() {
			return rotated ? Stitcher.func_147969_b(
					(int) (height * scaleFactor), mipmapLevelHolder) : Stitcher
					.func_147969_b((int) (width * scaleFactor),
							mipmapLevelHolder);
		}

		public boolean isRotated() {
			return rotated;
		}

		public void rotate() {
			rotated = !rotated;
		}

		public void setNewDimension(int p_94196_1_) {
			if (width > p_94196_1_ && height > p_94196_1_) {
				scaleFactor = (float) p_94196_1_
						/ (float) Math.min(width, height);
			}
		}

		@Override
		public String toString() {
			return "Holder{width=" + width + ", height=" + height + '}';
		}
	}

	public static class Slot {
		private final int height;
		private Stitcher.Holder holder;
		private final int originX;
		private final int originY;
		private List subSlots;
		private final int width;

		public Slot(int p_i1277_1_, int p_i1277_2_, int p_i1277_3_,
				int p_i1277_4_) {
			originX = p_i1277_1_;
			originY = p_i1277_2_;
			width = p_i1277_3_;
			height = p_i1277_4_;
		}

		public boolean addSlot(Stitcher.Holder p_94182_1_) {
			if (holder != null)
				return false;
			else {
				final int var2 = p_94182_1_.getWidth();
				final int var3 = p_94182_1_.getHeight();

				if (var2 <= width && var3 <= height) {
					if (var2 == width && var3 == height) {
						holder = p_94182_1_;
						return true;
					} else {
						if (subSlots == null) {
							subSlots = new ArrayList(1);
							subSlots.add(new Stitcher.Slot(originX, originY,
									var2, var3));
							final int var4 = width - var2;
							final int var5 = height - var3;

							if (var5 > 0 && var4 > 0) {
								final int var6 = Math.max(height, var4);
								final int var7 = Math.max(width, var5);

								if (var6 >= var7) {
									subSlots.add(new Stitcher.Slot(originX,
											originY + var3, var2, var5));
									subSlots.add(new Stitcher.Slot(originX
											+ var2, originY, var4, height));
								} else {
									subSlots.add(new Stitcher.Slot(originX
											+ var2, originY, var4, var3));
									subSlots.add(new Stitcher.Slot(originX,
											originY + var3, width, var5));
								}
							} else if (var4 == 0) {
								subSlots.add(new Stitcher.Slot(originX, originY
										+ var3, var2, var5));
							} else if (var5 == 0) {
								subSlots.add(new Stitcher.Slot(originX + var2,
										originY, var4, var3));
							}
						}

						final Iterator var8 = subSlots.iterator();
						Stitcher.Slot var9;

						do {
							if (!var8.hasNext())
								return false;

							var9 = (Stitcher.Slot) var8.next();
						} while (!var9.addSlot(p_94182_1_));

						return true;
					}
				} else
					return false;
			}
		}

		public void getAllStitchSlots(List p_94184_1_) {
			if (holder != null) {
				p_94184_1_.add(this);
			} else if (subSlots != null) {
				final Iterator var2 = subSlots.iterator();

				while (var2.hasNext()) {
					final Stitcher.Slot var3 = (Stitcher.Slot) var2.next();
					var3.getAllStitchSlots(p_94184_1_);
				}
			}
		}

		public int getOriginX() {
			return originX;
		}

		public int getOriginY() {
			return originY;
		}

		public Stitcher.Holder getStitchHolder() {
			return holder;
		}

		@Override
		public String toString() {
			return "Slot{originX=" + originX + ", originY=" + originY
					+ ", width=" + width + ", height=" + height + ", texture="
					+ holder + ", subSlots=" + subSlots + '}';
		}
	}

	private static int func_147969_b(int p_147969_0_, int p_147969_1_) {
		return (p_147969_0_ >> p_147969_1_)
				+ ((p_147969_0_ & (1 << p_147969_1_) - 1) == 0 ? 0 : 1) << p_147969_1_;
	}

	private int currentHeight;
	private int currentWidth;
	private final boolean forcePowerOf2;
	private final int maxHeight;
	/** Max size (width or height) of a single tile */
	private final int maxTileDimension;

	private final int maxWidth;

	private final int mipmapLevelStitcher;

	private final Set setStitchHolders = new HashSet(256);

	private final List stitchSlots = new ArrayList(256);

	public Stitcher(int p_i45095_1_, int p_i45095_2_, boolean p_i45095_3_,
			int p_i45095_4_, int p_i45095_5_) {
		mipmapLevelStitcher = p_i45095_5_;
		maxWidth = p_i45095_1_;
		maxHeight = p_i45095_2_;
		forcePowerOf2 = p_i45095_3_;
		maxTileDimension = p_i45095_4_;
	}

	public void addSprite(TextureAtlasSprite p_110934_1_) {
		final Stitcher.Holder var2 = new Stitcher.Holder(p_110934_1_,
				mipmapLevelStitcher);

		if (maxTileDimension > 0) {
			var2.setNewDimension(maxTileDimension);
		}

		setStitchHolders.add(var2);
	}

	/**
	 * Attempts to find space for specified tile
	 */
	private boolean allocateSlot(Stitcher.Holder p_94310_1_) {
		for (int var2 = 0; var2 < stitchSlots.size(); ++var2) {
			if (((Stitcher.Slot) stitchSlots.get(var2)).addSlot(p_94310_1_))
				return true;

			p_94310_1_.rotate();

			if (((Stitcher.Slot) stitchSlots.get(var2)).addSlot(p_94310_1_))
				return true;

			p_94310_1_.rotate();
		}

		return expandAndAllocateSlot(p_94310_1_);
	}

	public void doStitch() {
		final Stitcher.Holder[] var1 = (Stitcher.Holder[]) setStitchHolders
				.toArray(new Stitcher.Holder[setStitchHolders.size()]);
		Arrays.sort(var1);
		final Stitcher.Holder[] var2 = var1;
		final int var3 = var1.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final Stitcher.Holder var5 = var2[var4];

			if (!allocateSlot(var5)) {
				final String var6 = String
						.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?",
								new Object[] {
										var5.getAtlasSprite().getIconName(),
										Integer.valueOf(var5.getAtlasSprite()
												.getIconWidth()),
										Integer.valueOf(var5.getAtlasSprite()
												.getIconHeight()) });
				throw new StitcherException(var5, var6);
			}
		}

		if (forcePowerOf2) {
			currentWidth = MathHelper.roundUpToPowerOfTwo(currentWidth);
			currentHeight = MathHelper.roundUpToPowerOfTwo(currentHeight);
		}
	}

	/**
	 * Expand stitched texture in order to make space for specified tile
	 */
	private boolean expandAndAllocateSlot(Stitcher.Holder p_94311_1_) {
		final int var2 = Math
				.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
		final boolean var3 = currentWidth == 0 && currentHeight == 0;
		boolean var4;
		int var5;

		if (forcePowerOf2) {
			var5 = MathHelper.roundUpToPowerOfTwo(currentWidth);
			final int var6 = MathHelper.roundUpToPowerOfTwo(currentHeight);
			final int var7 = MathHelper
					.roundUpToPowerOfTwo(currentWidth + var2);
			final int var8 = MathHelper.roundUpToPowerOfTwo(currentHeight
					+ var2);
			final boolean var9 = var7 <= maxWidth;
			final boolean var10 = var8 <= maxHeight;

			if (!var9 && !var10)
				return false;

			final boolean var11 = var5 != var7;
			final boolean var12 = var6 != var8;

			if (var11 ^ var12) {
				var4 = !var11;
			} else {
				var4 = var9 && var5 <= var6;
			}
		} else {
			final boolean var13 = currentWidth + var2 <= maxWidth;
			final boolean var14 = currentHeight + var2 <= maxHeight;

			if (!var13 && !var14)
				return false;

			var4 = var13 && (var3 || currentWidth <= currentHeight);
		}

		var5 = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());

		if (MathHelper
				.roundUpToPowerOfTwo((var4 ? currentHeight : currentWidth)
						+ var5) > (var4 ? maxHeight : maxWidth))
			return false;
		else {
			Stitcher.Slot var15;

			if (var4) {
				if (p_94311_1_.getWidth() > p_94311_1_.getHeight()) {
					p_94311_1_.rotate();
				}

				if (currentHeight == 0) {
					currentHeight = p_94311_1_.getHeight();
				}

				var15 = new Stitcher.Slot(currentWidth, 0,
						p_94311_1_.getWidth(), currentHeight);
				currentWidth += p_94311_1_.getWidth();
			} else {
				var15 = new Stitcher.Slot(0, currentHeight, currentWidth,
						p_94311_1_.getHeight());
				currentHeight += p_94311_1_.getHeight();
			}

			var15.addSlot(p_94311_1_);
			stitchSlots.add(var15);
			return true;
		}
	}

	public int getCurrentHeight() {
		return currentHeight;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}

	public List getStichSlots() {
		final ArrayList var1 = Lists.newArrayList();
		final Iterator var2 = stitchSlots.iterator();

		while (var2.hasNext()) {
			final Stitcher.Slot var3 = (Stitcher.Slot) var2.next();
			var3.getAllStitchSlots(var1);
		}

		final ArrayList var7 = Lists.newArrayList();
		final Iterator var8 = var1.iterator();

		while (var8.hasNext()) {
			final Stitcher.Slot var4 = (Stitcher.Slot) var8.next();
			final Stitcher.Holder var5 = var4.getStitchHolder();
			final TextureAtlasSprite var6 = var5.getAtlasSprite();
			var6.initSprite(currentWidth, currentHeight, var4.getOriginX(),
					var4.getOriginY(), var5.isRotated());
			var7.add(var6);
		}

		return var7;
	}
}
