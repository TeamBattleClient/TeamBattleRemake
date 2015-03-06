package net.minecraft.client.renderer;

import me.client.Client;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.src.Config;
import net.minecraft.src.ConnectedTextures;
import net.minecraft.src.CustomColorizer;
import net.minecraft.src.NaturalProperties;
import net.minecraft.src.NaturalTextures;
import net.minecraft.src.Reflector;
import net.minecraft.src.TextureUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import event.events.EventBlockRender;

public class RenderBlocks {
	public static final String __OBFID = "CL_00000940";

	public static boolean cfgGrassFix = true;

	public static boolean fancyGrass = true;
	private static RenderBlocks instance;

	public static RenderBlocks getInstance() {
		if (instance == null) {
			instance = new RenderBlocks();
		}

		return instance;
	}

	public static boolean renderItemIn3d(int par0) {
		switch (par0) {
		case -1:
			return false;

		case 0:
		case 10:
		case 11:
		case 13:
		case 16:
		case 21:
		case 22:
		case 26:
		case 27:
		case 31:
		case 32:
		case 34:
		case 35:
		case 39:
			return true;

		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 12:
		case 14:
		case 15:
		case 17:
		case 18:
		case 19:
		case 20:
		case 23:
		case 24:
		case 25:
		case 28:
		case 29:
		case 30:
		case 33:
		case 36:
		case 37:
		case 38:
		default:
			return Reflector.ModLoader.exists() ? Reflector.callBoolean(
					Reflector.ModLoader_renderBlockIsItemFull3D,
					new Object[] { Integer.valueOf(par0) })
					: Reflector.FMLRenderAccessLibrary.exists() ? Reflector
							.callBoolean(
									Reflector.FMLRenderAccessLibrary_renderItemAsFull3DBlock,
									new Object[] { Integer.valueOf(par0) })
							: false;
		}
	}

	public int aoBrightnessXYNN;
	public int aoBrightnessXYNP;
	public int aoBrightnessXYPN;

	public int aoBrightnessXYPP;

	public int aoBrightnessXYZNNN;

	public int aoBrightnessXYZNNP;

	public int aoBrightnessXYZNPN;

	public int aoBrightnessXYZNPP;

	public int aoBrightnessXYZPNN;
	public int aoBrightnessXYZPNP;
	public int aoBrightnessXYZPPN;
	public int aoBrightnessXYZPPP;
	public int aoBrightnessXZNN;
	public int aoBrightnessXZNP;
	public int aoBrightnessXZPN;
	public int aoBrightnessXZPP;
	public int aoBrightnessYZNN;
	public int aoBrightnessYZNP;
	public int aoBrightnessYZPN;
	public int aoBrightnessYZPP;
	public float aoLightValueOpaque = 0.2F;
	public boolean aoLightValuesCalculated;
	public float aoLightValueScratchXYNN;
	public float aoLightValueScratchXYNP;
	public float aoLightValueScratchXYPN;
	public float aoLightValueScratchXYPP;
	public float aoLightValueScratchXYZNNN;
	public float aoLightValueScratchXYZNNP;
	public float aoLightValueScratchXYZNPN;
	public float aoLightValueScratchXYZNPP;
	public float aoLightValueScratchXYZPNN;
	public float aoLightValueScratchXYZPNP;
	public float aoLightValueScratchXYZPPN;
	public float aoLightValueScratchXYZPPP;
	public float aoLightValueScratchXZNN;
	public float aoLightValueScratchXZNP;
	public float aoLightValueScratchXZPN;
	public float aoLightValueScratchXZPP;
	public float aoLightValueScratchYZNN;
	public float aoLightValueScratchYZNP;
	public float aoLightValueScratchYZPN;
	public float aoLightValueScratchYZPP;
	public boolean betterSnowEnabled = true;
	/** The IBlockAccess used by this instance of RenderBlocks */
	public IBlockAccess blockAccess;
	public int brightnessBottomLeft;
	public int brightnessBottomRight;
	public int brightnessTopLeft;
	public int brightnessTopRight;
	public float colorBlueBottomLeft;
	public float colorBlueBottomRight;
	public float colorBlueTopLeft;
	public float colorBlueTopRight;
	public float colorGreenBottomLeft;
	public float colorGreenBottomRight;
	public float colorGreenTopLeft;
	public float colorGreenTopRight;
	public float colorRedBottomLeft;
	public float colorRedBottomRight;
	public float colorRedTopLeft;
	public float colorRedTopRight;
	public boolean enableAO;
	public boolean field_152631_f;
	/**
	 * Set to true if the texture should be flipped horizontally during
	 * render*Face
	 */
	public boolean flipTexture;
	public boolean lockBlockBounds;
	public final Minecraft minecraftRB;
	public IIcon overrideBlockTexture;
	public boolean partialRenderBounds;
	public boolean renderAllFaces;
	public boolean renderFromInside = false;
	/** The maximum X value for rendering (default 1.0). */
	public double renderMaxX;
	/** The maximum Y value for rendering (default 1.0). */
	public double renderMaxY;
	/** The maximum Z value for rendering (default 1.0). */
	public double renderMaxZ;
	/** The minimum X value for rendering (default 0.0). */
	public double renderMinX;
	/** The minimum Y value for rendering (default 0.0). */
	public double renderMinY;
	/** The minimum Z value for rendering (default 0.0). */
	public double renderMinZ;
	public boolean useInventoryTint = true;
	public int uvRotateBottom;
	public int uvRotateEast;
	public int uvRotateNorth;
	public int uvRotateSouth;

	public int uvRotateTop;

	public int uvRotateWest;

	public RenderBlocks() {
		minecraftRB = Minecraft.getMinecraft();
	}

	public RenderBlocks(IBlockAccess par1IBlockAccess) {
		blockAccess = par1IBlockAccess;
		field_152631_f = false;
		flipTexture = false;
		minecraftRB = Minecraft.getMinecraft();
		aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
	}

	/**
	 * Clear override block texture
	 */
	public void clearOverrideBlockTexture() {
		overrideBlockTexture = null;
	}

	public void drawCrossedSquares(IIcon p_147765_1_, double p_147765_2_,
			double p_147765_4_, double p_147765_6_, float p_147765_8_) {
		final Tessellator var9 = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147765_1_ = overrideBlockTexture;
		}

		final double var10 = p_147765_1_.getMinU();
		final double var12 = p_147765_1_.getMinV();
		final double var14 = p_147765_1_.getMaxU();
		final double var16 = p_147765_1_.getMaxV();
		final double var18 = 0.45D * p_147765_8_;
		final double var20 = p_147765_2_ + 0.5D - var18;
		final double var22 = p_147765_2_ + 0.5D + var18;
		final double var24 = p_147765_6_ + 0.5D - var18;
		final double var26 = p_147765_6_ + 0.5D + var18;
		var9.addVertexWithUV(var20, p_147765_4_ + p_147765_8_, var24, var10,
				var12);
		var9.addVertexWithUV(var20, p_147765_4_ + 0.0D, var24, var10, var16);
		var9.addVertexWithUV(var22, p_147765_4_ + 0.0D, var26, var14, var16);
		var9.addVertexWithUV(var22, p_147765_4_ + p_147765_8_, var26, var14,
				var12);
		var9.addVertexWithUV(var22, p_147765_4_ + p_147765_8_, var26, var10,
				var12);
		var9.addVertexWithUV(var22, p_147765_4_ + 0.0D, var26, var10, var16);
		var9.addVertexWithUV(var20, p_147765_4_ + 0.0D, var24, var14, var16);
		var9.addVertexWithUV(var20, p_147765_4_ + p_147765_8_, var24, var14,
				var12);
		var9.addVertexWithUV(var20, p_147765_4_ + p_147765_8_, var26, var10,
				var12);
		var9.addVertexWithUV(var20, p_147765_4_ + 0.0D, var26, var10, var16);
		var9.addVertexWithUV(var22, p_147765_4_ + 0.0D, var24, var14, var16);
		var9.addVertexWithUV(var22, p_147765_4_ + p_147765_8_, var24, var14,
				var12);
		var9.addVertexWithUV(var22, p_147765_4_ + p_147765_8_, var24, var10,
				var12);
		var9.addVertexWithUV(var22, p_147765_4_ + 0.0D, var24, var10, var16);
		var9.addVertexWithUV(var20, p_147765_4_ + 0.0D, var26, var14, var16);
		var9.addVertexWithUV(var20, p_147765_4_ + p_147765_8_, var26, var14,
				var12);
	}

	private IIcon fixAoSideGrassTexture(IIcon tex, int x, int y, int z,
			int side, float f, float f1, float f2) {
		if (tex == TextureUtils.iconGrassSide
				|| tex == TextureUtils.iconMyceliumSide) {
			tex = Config.getSideGrassTexture(blockAccess, x, y, z, side, tex);

			if (tex == TextureUtils.iconGrassTop) {
				colorRedTopLeft *= f;
				colorRedBottomLeft *= f;
				colorRedBottomRight *= f;
				colorRedTopRight *= f;
				colorGreenTopLeft *= f1;
				colorGreenBottomLeft *= f1;
				colorGreenBottomRight *= f1;
				colorGreenTopRight *= f1;
				colorBlueTopLeft *= f2;
				colorBlueBottomLeft *= f2;
				colorBlueBottomRight *= f2;
				colorBlueTopRight *= f2;
			}
		}

		if (tex == TextureUtils.iconGrassSideSnowed) {
			tex = Config.getSideSnowGrassTexture(blockAccess, x, y, z, side);
		}

		return tex;
	}

	private float getAmbientOcclusionLightValue(int i, int j, int k) {
		final Block block = blockAccess.getBlock(i, j, k);
		return block.isBlockNormalCube() ? aoLightValueOpaque : 1.0F;
	}

	public int getAoBrightness(int p_147778_1_, int p_147778_2_,
			int p_147778_3_, int p_147778_4_) {
		if (p_147778_1_ == 0) {
			p_147778_1_ = p_147778_4_;
		}

		if (p_147778_2_ == 0) {
			p_147778_2_ = p_147778_4_;
		}

		if (p_147778_3_ == 0) {
			p_147778_3_ = p_147778_4_;
		}

		return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 16711935;
	}

	public IIcon getBlockIcon(Block p_147745_1_) {
		return getIconSafe(p_147745_1_.getBlockTextureFromSide(1));
	}

	public IIcon getBlockIcon(Block p_147793_1_, IBlockAccess p_147793_2_,
			int p_147793_3_, int p_147793_4_, int p_147793_5_, int p_147793_6_) {
		return getIconSafe(p_147793_1_.getIcon(p_147793_2_, p_147793_3_,
				p_147793_4_, p_147793_5_, p_147793_6_));
	}

	public IIcon getBlockIconFromSide(Block p_147777_1_, int p_147777_2_) {
		return getIconSafe(p_147777_1_.getBlockTextureFromSide(p_147777_2_));
	}

	public IIcon getBlockIconFromSideAndMetadata(Block p_147787_1_,
			int p_147787_2_, int p_147787_3_) {
		return getIconSafe(p_147787_1_.getIcon(p_147787_2_, p_147787_3_));
	}

	public float getFluidHeight(int p_147729_1_, int p_147729_2_,
			int p_147729_3_, Material p_147729_4_) {
		int var5 = 0;
		float var6 = 0.0F;

		for (int var7 = 0; var7 < 4; ++var7) {
			final int var8 = p_147729_1_ - (var7 & 1);
			final int var10 = p_147729_3_ - (var7 >> 1 & 1);

			if (blockAccess.getBlock(var8, p_147729_2_ + 1, var10)
					.getMaterial() == p_147729_4_)
				return 1.0F;

			final Material var11 = blockAccess.getBlock(var8, p_147729_2_,
					var10).getMaterial();

			if (var11 == p_147729_4_) {
				final int var12 = blockAccess.getBlockMetadata(var8,
						p_147729_2_, var10);

				if (var12 >= 8 || var12 == 0) {
					var6 += BlockLiquid.func_149801_b(var12) * 10.0F;
					var5 += 10;
				}

				var6 += BlockLiquid.func_149801_b(var12);
				++var5;
			} else if (!var11.isSolid()) {
				++var6;
				++var5;
			}
		}

		return 1.0F - var6 / var5;
	}

	public IIcon getIconSafe(IIcon p_147758_1_) {
		if (p_147758_1_ == null) {
			p_147758_1_ = ((TextureMap) Minecraft.getMinecraft()
					.getTextureManager()
					.getTexture(TextureMap.locationBlocksTexture))
					.getAtlasSprite("missingno");
		}

		return p_147758_1_;
	}

	public boolean hasOverrideBlockTexture() {
		return overrideBlockTexture != null;
	}

	private boolean hasSnowNeighbours(int x, int y, int z) {
		final Block blockSnow = Blocks.snow_layer;
		return blockAccess.getBlock(x - 1, y, z) != blockSnow
				&& blockAccess.getBlock(x + 1, y, z) != blockSnow
				&& blockAccess.getBlock(x, y, z - 1) != blockSnow
				&& blockAccess.getBlock(x, y, z + 1) != blockSnow ? false
				: blockAccess.getBlock(x, y - 1, z).isOpaqueCube();
	}

	public int mixAoBrightness(int p_147727_1_, int p_147727_2_,
			int p_147727_3_, int p_147727_4_, double p_147727_5_,
			double p_147727_7_, double p_147727_9_, double p_147727_11_) {
		final int var13 = (int) ((p_147727_1_ >> 16 & 255) * p_147727_5_
				+ (p_147727_2_ >> 16 & 255) * p_147727_7_
				+ (p_147727_3_ >> 16 & 255) * p_147727_9_ + (p_147727_4_ >> 16 & 255)
				* p_147727_11_) & 255;
		final int var14 = (int) ((p_147727_1_ & 255) * p_147727_5_
				+ (p_147727_2_ & 255) * p_147727_7_ + (p_147727_3_ & 255)
				* p_147727_9_ + (p_147727_4_ & 255) * p_147727_11_) & 255;
		return var13 << 16 | var14;
	}

	public void overrideBlockBounds(double p_147770_1_, double p_147770_3_,
			double p_147770_5_, double p_147770_7_, double p_147770_9_,
			double p_147770_11_) {
		renderMinX = p_147770_1_;
		renderMaxX = p_147770_7_;
		renderMinY = p_147770_3_;
		renderMaxY = p_147770_9_;
		renderMinZ = p_147770_5_;
		renderMaxZ = p_147770_11_;
		lockBlockBounds = true;
		partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2
				&& (renderMinX > 0.0D || renderMaxX < 1.0D || renderMinY > 0.0D
						|| renderMaxY < 1.0D || renderMinZ > 0.0D || renderMaxZ < 1.0D);
	}

	/**
	 * Render all faces of a block
	 */
	public void renderBlockAllFaces(Block p_147769_1_, int p_147769_2_,
			int p_147769_3_, int p_147769_4_) {
		renderAllFaces = true;
		renderBlockByRenderType(p_147769_1_, p_147769_2_, p_147769_3_,
				p_147769_4_);
		renderAllFaces = false;
	}

	/**
	 * Renders anvil
	 */
	public boolean renderBlockAnvil(BlockAnvil p_147725_1_, int p_147725_2_,
			int p_147725_3_, int p_147725_4_) {
		return renderBlockAnvilMetadata(p_147725_1_, p_147725_2_, p_147725_3_,
				p_147725_4_, blockAccess.getBlockMetadata(p_147725_2_,
						p_147725_3_, p_147725_4_));
	}

	/**
	 * Renders anvil block with metadata
	 */
	public boolean renderBlockAnvilMetadata(BlockAnvil p_147780_1_,
			int p_147780_2_, int p_147780_3_, int p_147780_4_, int p_147780_5_) {
		final Tessellator var6 = Tessellator.instance;
		var6.setBrightness(p_147780_1_.getBlockBrightness(blockAccess,
				p_147780_2_, p_147780_3_, p_147780_4_));
		final int var7 = p_147780_1_.colorMultiplier(blockAccess, p_147780_2_,
				p_147780_3_, p_147780_4_);
		float var8 = (var7 >> 16 & 255) / 255.0F;
		float var9 = (var7 >> 8 & 255) / 255.0F;
		float var10 = (var7 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			final float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			final float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = var11;
			var9 = var12;
			var10 = var13;
		}

		var6.setColorOpaque_F(var8, var9, var10);
		return renderBlockAnvilOrient(p_147780_1_, p_147780_2_, p_147780_3_,
				p_147780_4_, p_147780_5_, false);
	}

	/**
	 * Renders anvil block with orientation
	 */
	public boolean renderBlockAnvilOrient(BlockAnvil p_147728_1_,
			int p_147728_2_, int p_147728_3_, int p_147728_4_, int p_147728_5_,
			boolean p_147728_6_) {
		final int var7 = p_147728_6_ ? 0 : p_147728_5_ & 3;
		boolean var8 = false;
		float var9 = 0.0F;

		switch (var7) {
		case 0:
			uvRotateSouth = 2;
			uvRotateNorth = 1;
			uvRotateTop = 3;
			uvRotateBottom = 3;
			break;

		case 1:
			uvRotateEast = 1;
			uvRotateWest = 2;
			uvRotateTop = 2;
			uvRotateBottom = 1;
			var8 = true;
			break;

		case 2:
			uvRotateSouth = 1;
			uvRotateNorth = 2;
			break;

		case 3:
			uvRotateEast = 2;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 2;
			var8 = true;
		}

		var9 = renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_,
				p_147728_4_, 0, var9, 0.75F, 0.25F, 0.75F, var8, p_147728_6_,
				p_147728_5_);
		var9 = renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_,
				p_147728_4_, 1, var9, 0.5F, 0.0625F, 0.625F, var8, p_147728_6_,
				p_147728_5_);
		var9 = renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_,
				p_147728_4_, 2, var9, 0.25F, 0.3125F, 0.5F, var8, p_147728_6_,
				p_147728_5_);
		renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_,
				p_147728_4_, 3, var9, 0.625F, 0.375F, 1.0F, var8, p_147728_6_,
				p_147728_5_);
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateSouth = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		return true;
	}

	/**
	 * Renders anvil block with rotation
	 */
	public float renderBlockAnvilRotate(BlockAnvil p_147737_1_,
			int p_147737_2_, int p_147737_3_, int p_147737_4_, int p_147737_5_,
			float p_147737_6_, float p_147737_7_, float p_147737_8_,
			float p_147737_9_, boolean p_147737_10_, boolean p_147737_11_,
			int p_147737_12_) {
		if (p_147737_10_) {
			final float var14 = p_147737_7_;
			p_147737_7_ = p_147737_9_;
			p_147737_9_ = var14;
		}

		p_147737_7_ /= 2.0F;
		p_147737_9_ /= 2.0F;
		p_147737_1_.field_149833_b = p_147737_5_;
		setRenderBounds(0.5F - p_147737_7_, p_147737_6_, 0.5F - p_147737_9_,
				0.5F + p_147737_7_, p_147737_6_ + p_147737_8_,
				0.5F + p_147737_9_);

		if (p_147737_11_) {
			final Tessellator var141 = Tessellator.instance;
			var141.startDrawingQuads();
			var141.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(
					p_147737_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 0,
							p_147737_12_));
			var141.draw();
			var141.startDrawingQuads();
			var141.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(
					p_147737_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 1,
							p_147737_12_));
			var141.draw();
			var141.startDrawingQuads();
			var141.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(
					p_147737_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 2,
							p_147737_12_));
			var141.draw();
			var141.startDrawingQuads();
			var141.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(
					p_147737_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 3,
							p_147737_12_));
			var141.draw();
			var141.startDrawingQuads();
			var141.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(
					p_147737_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 4,
							p_147737_12_));
			var141.draw();
			var141.startDrawingQuads();
			var141.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(
					p_147737_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 5,
							p_147737_12_));
			var141.draw();
		} else {
			renderStandardBlock(p_147737_1_, p_147737_2_, p_147737_3_,
					p_147737_4_);
		}

		return p_147737_6_ + p_147737_8_;
	}

	public void renderBlockAsItem(Block p_147800_1_, int p_147800_2_,
			float p_147800_3_) {
		final Tessellator var4 = Tessellator.instance;
		final boolean var5 = p_147800_1_ == Blocks.grass;

		if (p_147800_1_ == Blocks.dispenser || p_147800_1_ == Blocks.dropper
				|| p_147800_1_ == Blocks.furnace) {
			p_147800_2_ = 3;
		}

		int var6;
		float var7;
		float var8;
		float var9;

		if (useInventoryTint) {
			var6 = p_147800_1_.getRenderColor(p_147800_2_);

			if (var5) {
				var6 = 16777215;
			}

			var7 = (var6 >> 16 & 255) / 255.0F;
			var8 = (var6 >> 8 & 255) / 255.0F;
			var9 = (var6 & 255) / 255.0F;
			GL11.glColor4f(var7 * p_147800_3_, var8 * p_147800_3_, var9
					* p_147800_3_, 1.0F);
		}

		var6 = p_147800_1_.getRenderType();
		setRenderBoundsFromBlock(p_147800_1_);
		int var14;

		if (var6 != 0 && var6 != 31 && var6 != 39 && var6 != 16 && var6 != 26) {
			if (var6 == 1) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				final IIcon var171 = getBlockIconFromSideAndMetadata(
						p_147800_1_, 0, p_147800_2_);
				drawCrossedSquares(var171, -0.5D, -0.5D, -0.5D, 1.0F);
				var4.draw();
			} else if (var6 == 19) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				p_147800_1_.setBlockBoundsForItemRender();
				renderBlockStemSmall(p_147800_1_, p_147800_2_, renderMaxY,
						-0.5D, -0.5D, -0.5D);
				var4.draw();
			} else if (var6 == 23) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				p_147800_1_.setBlockBoundsForItemRender();
				var4.draw();
			} else if (var6 == 13) {
				p_147800_1_.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				var7 = 0.0625F;
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
						getBlockIconFromSide(p_147800_1_, 0));
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 1.0F, 0.0F);
				renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
						getBlockIconFromSide(p_147800_1_, 1));
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 0.0F, -1.0F);
				var4.addTranslation(0.0F, 0.0F, var7);
				renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
						getBlockIconFromSide(p_147800_1_, 2));
				var4.addTranslation(0.0F, 0.0F, -var7);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(0.0F, 0.0F, 1.0F);
				var4.addTranslation(0.0F, 0.0F, -var7);
				renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
						getBlockIconFromSide(p_147800_1_, 3));
				var4.addTranslation(0.0F, 0.0F, var7);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(-1.0F, 0.0F, 0.0F);
				var4.addTranslation(var7, 0.0F, 0.0F);
				renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
						getBlockIconFromSide(p_147800_1_, 4));
				var4.addTranslation(-var7, 0.0F, 0.0F);
				var4.draw();
				var4.startDrawingQuads();
				var4.setNormal(1.0F, 0.0F, 0.0F);
				var4.addTranslation(-var7, 0.0F, 0.0F);
				renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
						getBlockIconFromSide(p_147800_1_, 5));
				var4.addTranslation(var7, 0.0F, 0.0F);
				var4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (var6 == 22) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				TileEntityRendererChestHelper.instance.func_147715_a(
						p_147800_1_, p_147800_2_, p_147800_3_);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			} else if (var6 == 6) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				renderBlockCropsImpl(p_147800_1_, p_147800_2_, -0.5D, -0.5D,
						-0.5D);
				var4.draw();
			} else if (var6 == 2) {
				var4.startDrawingQuads();
				var4.setNormal(0.0F, -1.0F, 0.0F);
				renderTorchAtAngle(p_147800_1_, -0.5D, -0.5D, -0.5D, 0.0D,
						0.0D, 0);
				var4.draw();
			} else if (var6 == 10) {
				for (var14 = 0; var14 < 2; ++var14) {
					if (var14 == 0) {
						setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
					}

					if (var14 == 1) {
						setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 0));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 1));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 2));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 3));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 5));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else if (var6 == 27) {
				var14 = 0;
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				var4.startDrawingQuads();

				for (int var181 = 0; var181 < 8; ++var181) {
					byte var17 = 0;
					byte var18 = 1;

					if (var181 == 0) {
						var17 = 2;
					}

					if (var181 == 1) {
						var17 = 3;
					}

					if (var181 == 2) {
						var17 = 4;
					}

					if (var181 == 3) {
						var17 = 5;
						var18 = 2;
					}

					if (var181 == 4) {
						var17 = 6;
						var18 = 3;
					}

					if (var181 == 5) {
						var17 = 7;
						var18 = 5;
					}

					if (var181 == 6) {
						var17 = 6;
						var18 = 2;
					}

					if (var181 == 7) {
						var17 = 3;
					}

					final float var11 = var17 / 16.0F;
					final float var12 = 1.0F - var14 / 16.0F;
					final float var13 = 1.0F - (var14 + var18) / 16.0F;
					var14 += var18;
					setRenderBounds(0.5F - var11, var13, 0.5F - var11,
							0.5F + var11, var12, 0.5F + var11);
					var4.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 0));
					var4.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 1));
					var4.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 2));
					var4.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 3));
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 4));
					var4.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 5));
				}

				var4.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (var6 == 11) {
				for (var14 = 0; var14 < 4; ++var14) {
					var8 = 0.125F;

					if (var14 == 0) {
						setRenderBounds(0.5F - var8, 0.0D, 0.0D, 0.5F + var8,
								1.0D, var8 * 2.0F);
					}

					if (var14 == 1) {
						setRenderBounds(0.5F - var8, 0.0D, 1.0F - var8 * 2.0F,
								0.5F + var8, 1.0D, 1.0D);
					}

					var8 = 0.0625F;

					if (var14 == 2) {
						setRenderBounds(0.5F - var8, 1.0F - var8 * 3.0F,
								-var8 * 2.0F, 0.5F + var8, 1.0F - var8,
								1.0F + var8 * 2.0F);
					}

					if (var14 == 3) {
						setRenderBounds(0.5F - var8, 0.5F - var8 * 3.0F,
								-var8 * 2.0F, 0.5F + var8, 0.5F - var8,
								1.0F + var8 * 2.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 0));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 1));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 2));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 3));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 5));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (var6 == 21) {
				for (var14 = 0; var14 < 3; ++var14) {
					var8 = 0.0625F;

					if (var14 == 0) {
						setRenderBounds(0.5F - var8, 0.30000001192092896D,
								0.0D, 0.5F + var8, 1.0D, var8 * 2.0F);
					}

					if (var14 == 1) {
						setRenderBounds(0.5F - var8, 0.30000001192092896D,
								1.0F - var8 * 2.0F, 0.5F + var8, 1.0D, 1.0D);
					}

					var8 = 0.0625F;

					if (var14 == 2) {
						setRenderBounds(0.5F - var8, 0.5D, 0.0D, 0.5F + var8,
								1.0F - var8, 1.0D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 0));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 1));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 2));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 3));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 4));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSide(p_147800_1_, 5));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else if (var6 == 32) {
				for (var14 = 0; var14 < 2; ++var14) {
					if (var14 == 0) {
						setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D,
								0.6875D);
					}

					if (var14 == 1) {
						setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 0,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 1,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 2,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 3,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 4,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 5,
									p_147800_2_));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (var6 == 35) {
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				renderBlockAnvilOrient((BlockAnvil) p_147800_1_, 0, 0, 0,
						p_147800_2_ << 2, true);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (var6 == 34) {
				for (var14 = 0; var14 < 3; ++var14) {
					if (var14 == 0) {
						setRenderBounds(0.125D, 0.0D, 0.125D, 0.875D, 0.1875D,
								0.875D);
						setOverrideBlockTexture(this
								.getBlockIcon(Blocks.obsidian));
					} else if (var14 == 1) {
						setRenderBounds(0.1875D, 0.1875D, 0.1875D, 0.8125D,
								0.875D, 0.8125D);
						setOverrideBlockTexture(this
								.getBlockIcon(Blocks.beacon));
					} else if (var14 == 2) {
						setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
						setOverrideBlockTexture(this.getBlockIcon(Blocks.glass));
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					var4.startDrawingQuads();
					var4.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 0,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 1,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 2,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 3,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 4,
									p_147800_2_));
					var4.draw();
					var4.startDrawingQuads();
					var4.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(
							p_147800_1_,
							0.0D,
							0.0D,
							0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 5,
									p_147800_2_));
					var4.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
				clearOverrideBlockTexture();
			} else if (var6 == 38) {
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				renderBlockHopperMetadata((BlockHopper) p_147800_1_, 0, 0, 0,
						0, true);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (Reflector.ModLoader.exists()) {
				Reflector.callVoid(
						Reflector.ModLoader_renderInvBlock,
						new Object[] { this, p_147800_1_,
								Integer.valueOf(p_147800_2_),
								Integer.valueOf(var6) });
			} else if (Reflector.FMLRenderAccessLibrary.exists()) {
				Reflector.callVoid(
						Reflector.FMLRenderAccessLibrary_renderInventoryBlock,
						new Object[] { this, p_147800_1_,
								Integer.valueOf(p_147800_2_),
								Integer.valueOf(var6) });
			}
		} else {
			if (var6 == 16) {
				p_147800_2_ = 1;
			}

			p_147800_1_.setBlockBoundsForItemRender();
			setRenderBoundsFromBlock(p_147800_1_);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			var4.startDrawingQuads();
			var4.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(
					p_147800_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147800_1_, 0, p_147800_2_));
			var4.draw();

			if (var5 && useInventoryTint) {
				var14 = p_147800_1_.getRenderColor(p_147800_2_);
				var8 = (var14 >> 16 & 255) / 255.0F;
				var9 = (var14 >> 8 & 255) / 255.0F;
				final float var10 = (var14 & 255) / 255.0F;
				GL11.glColor4f(var8 * p_147800_3_, var9 * p_147800_3_, var10
						* p_147800_3_, 1.0F);
			}

			var4.startDrawingQuads();
			var4.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(
					p_147800_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147800_1_, 1, p_147800_2_));
			var4.draw();

			if (var5 && useInventoryTint) {
				GL11.glColor4f(p_147800_3_, p_147800_3_, p_147800_3_, 1.0F);
			}

			var4.startDrawingQuads();
			var4.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(
					p_147800_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147800_1_, 2, p_147800_2_));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(
					p_147800_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147800_1_, 3, p_147800_2_));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(
					p_147800_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147800_1_, 4, p_147800_2_));
			var4.draw();
			var4.startDrawingQuads();
			var4.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(
					p_147800_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147800_1_, 5, p_147800_2_));
			var4.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
	}

	public boolean renderBlockBeacon(BlockBeacon p_147797_1_, int p_147797_2_,
			int p_147797_3_, int p_147797_4_) {
		final float var5 = 0.1875F;
		setOverrideBlockTexture(this.getBlockIcon(Blocks.glass));
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		renderStandardBlock(p_147797_1_, p_147797_2_, p_147797_3_, p_147797_4_);
		renderAllFaces = true;
		setOverrideBlockTexture(this.getBlockIcon(Blocks.obsidian));
		setRenderBounds(0.125D, 0.0062500000931322575D, 0.125D, 0.875D, var5,
				0.875D);
		renderStandardBlock(p_147797_1_, p_147797_2_, p_147797_3_, p_147797_4_);
		IIcon iconBeacon = this.getBlockIcon(Blocks.beacon);

		if (Config.isConnectedTextures()) {
			iconBeacon = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147797_1_, p_147797_2_, p_147797_3_, p_147797_4_, -1,
					iconBeacon);
		}

		setOverrideBlockTexture(iconBeacon);
		setRenderBounds(0.1875D, var5, 0.1875D, 0.8125D, 0.875D, 0.8125D);
		renderStandardBlock(p_147797_1_, p_147797_2_, p_147797_3_, p_147797_4_);
		renderAllFaces = false;
		clearOverrideBlockTexture();
		return true;
	}

	/**
	 * render a bed at the given coordinates
	 */
	public boolean renderBlockBed(Block p_147773_1_, int p_147773_2_,
			int p_147773_3_, int p_147773_4_) {
		final Tessellator var5 = Tessellator.instance;
		final int var6 = blockAccess.getBlockMetadata(p_147773_2_, p_147773_3_,
				p_147773_4_);
		int var7 = BlockDirectional.func_149895_l(var6);
		boolean var8 = BlockBed.func_149975_b(var6);

		if (Reflector.ForgeBlock_getBedDirection.exists()) {
			var7 = Reflector.callInt(
					p_147773_1_,
					Reflector.ForgeBlock_getBedDirection,
					new Object[] { blockAccess, Integer.valueOf(p_147773_2_),
							Integer.valueOf(p_147773_3_),
							Integer.valueOf(p_147773_4_) });
		}

		if (Reflector.ForgeBlock_isBedFoot.exists()) {
			var8 = Reflector.callBoolean(
					p_147773_1_,
					Reflector.ForgeBlock_isBedFoot,
					new Object[] { blockAccess, Integer.valueOf(p_147773_2_),
							Integer.valueOf(p_147773_3_),
							Integer.valueOf(p_147773_4_) });
		}

		final float var9 = 0.5F;
		final float var10 = 1.0F;
		final float var11 = 0.8F;
		final float var12 = 0.6F;
		final int var25 = p_147773_1_.getBlockBrightness(blockAccess,
				p_147773_2_, p_147773_3_, p_147773_4_);
		var5.setBrightness(var25);
		var5.setColorOpaque_F(var9, var9, var9);
		IIcon var26 = this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_,
				p_147773_3_, p_147773_4_, 0);

		if (overrideBlockTexture != null) {
			var26 = overrideBlockTexture;
		}

		double var27 = var26.getMinU();
		double var29 = var26.getMaxU();
		double var31 = var26.getMinV();
		double var33 = var26.getMaxV();
		double var35 = p_147773_2_ + renderMinX;
		double var37 = p_147773_2_ + renderMaxX;
		double var39 = p_147773_3_ + renderMinY + 0.1875D;
		double var41 = p_147773_4_ + renderMinZ;
		double var43 = p_147773_4_ + renderMaxZ;
		var5.addVertexWithUV(var35, var39, var43, var27, var33);
		var5.addVertexWithUV(var35, var39, var41, var27, var31);
		var5.addVertexWithUV(var37, var39, var41, var29, var31);
		var5.addVertexWithUV(var37, var39, var43, var29, var33);
		var5.setBrightness(p_147773_1_.getBlockBrightness(blockAccess,
				p_147773_2_, p_147773_3_ + 1, p_147773_4_));
		var5.setColorOpaque_F(var10, var10, var10);
		var26 = this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_,
				p_147773_3_, p_147773_4_, 1);

		if (overrideBlockTexture != null) {
			var26 = overrideBlockTexture;
		}

		var27 = var26.getMinU();
		var29 = var26.getMaxU();
		var31 = var26.getMinV();
		var33 = var26.getMaxV();
		var35 = var27;
		var37 = var29;
		var39 = var31;
		var41 = var31;
		var43 = var27;
		double var45 = var29;
		double var47 = var33;
		double var49 = var33;

		if (var7 == 0) {
			var37 = var27;
			var39 = var33;
			var43 = var29;
			var49 = var31;
		} else if (var7 == 2) {
			var35 = var29;
			var41 = var33;
			var45 = var27;
			var47 = var31;
		} else if (var7 == 3) {
			var35 = var29;
			var41 = var33;
			var45 = var27;
			var47 = var31;
			var37 = var27;
			var39 = var33;
			var43 = var29;
			var49 = var31;
		}

		final double var51 = p_147773_2_ + renderMinX;
		final double var53 = p_147773_2_ + renderMaxX;
		final double var55 = p_147773_3_ + renderMaxY;
		final double var57 = p_147773_4_ + renderMinZ;
		final double var59 = p_147773_4_ + renderMaxZ;
		var5.addVertexWithUV(var53, var55, var59, var43, var47);
		var5.addVertexWithUV(var53, var55, var57, var35, var39);
		var5.addVertexWithUV(var51, var55, var57, var37, var41);
		var5.addVertexWithUV(var51, var55, var59, var45, var49);
		int var61 = Direction.directionToFacing[var7];

		if (var8) {
			var61 = Direction.directionToFacing[Direction.rotateOpposite[var7]];
		}

		byte var62 = 4;

		switch (var7) {
		case 0:
			var62 = 5;
			break;

		case 1:
			var62 = 3;

		case 2:
		default:
			break;

		case 3:
			var62 = 2;
		}

		if (var61 != 2
				&& (renderAllFaces || p_147773_1_.shouldSideBeRendered(
						blockAccess, p_147773_2_, p_147773_3_, p_147773_4_ - 1,
						2))) {
			var5.setBrightness(renderMinZ > 0.0D ? var25 : p_147773_1_
					.getBlockBrightness(blockAccess, p_147773_2_, p_147773_3_,
							p_147773_4_ - 1));
			var5.setColorOpaque_F(var11, var11, var11);
			flipTexture = var62 == 2;
			renderFaceZNeg(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_,
							p_147773_3_, p_147773_4_, 2));
		}

		if (var61 != 3
				&& (renderAllFaces || p_147773_1_.shouldSideBeRendered(
						blockAccess, p_147773_2_, p_147773_3_, p_147773_4_ + 1,
						3))) {
			var5.setBrightness(renderMaxZ < 1.0D ? var25 : p_147773_1_
					.getBlockBrightness(blockAccess, p_147773_2_, p_147773_3_,
							p_147773_4_ + 1));
			var5.setColorOpaque_F(var11, var11, var11);
			flipTexture = var62 == 3;
			renderFaceZPos(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_,
							p_147773_3_, p_147773_4_, 3));
		}

		if (var61 != 4
				&& (renderAllFaces || p_147773_1_.shouldSideBeRendered(
						blockAccess, p_147773_2_ - 1, p_147773_3_, p_147773_4_,
						4))) {
			var5.setBrightness(renderMinZ > 0.0D ? var25 : p_147773_1_
					.getBlockBrightness(blockAccess, p_147773_2_ - 1,
							p_147773_3_, p_147773_4_));
			var5.setColorOpaque_F(var12, var12, var12);
			flipTexture = var62 == 4;
			renderFaceXNeg(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_,
							p_147773_3_, p_147773_4_, 4));
		}

		if (var61 != 5
				&& (renderAllFaces || p_147773_1_.shouldSideBeRendered(
						blockAccess, p_147773_2_ + 1, p_147773_3_, p_147773_4_,
						5))) {
			var5.setBrightness(renderMaxZ < 1.0D ? var25 : p_147773_1_
					.getBlockBrightness(blockAccess, p_147773_2_ + 1,
							p_147773_3_, p_147773_4_));
			var5.setColorOpaque_F(var12, var12, var12);
			flipTexture = var62 == 5;
			renderFaceXPos(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_,
							p_147773_3_, p_147773_4_, 5));
		}

		flipTexture = false;
		return true;
	}

	/**
	 * Render BlockBrewingStand
	 */
	public boolean renderBlockBrewingStand(BlockBrewingStand p_147741_1_,
			int p_147741_2_, int p_147741_3_, int p_147741_4_) {
		setRenderBounds(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		setOverrideBlockTexture(p_147741_1_.func_149959_e());
		renderAllFaces = true;
		setRenderBounds(0.5625D, 0.0D, 0.3125D, 0.9375D, 0.125D, 0.6875D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		setRenderBounds(0.125D, 0.0D, 0.0625D, 0.5D, 0.125D, 0.4375D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		setRenderBounds(0.125D, 0.0D, 0.5625D, 0.5D, 0.125D, 0.9375D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		renderAllFaces = false;
		clearOverrideBlockTexture();
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147741_1_.getBlockBrightness(blockAccess,
				p_147741_2_, p_147741_3_, p_147741_4_));
		final int var6 = p_147741_1_.colorMultiplier(blockAccess, p_147741_2_,
				p_147741_3_, p_147741_4_);
		float var7 = (var6 >> 16 & 255) / 255.0F;
		float var8 = (var6 >> 8 & 255) / 255.0F;
		float var9 = (var6 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var31 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
			final float var32 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
			final float var12 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
			var7 = var31;
			var8 = var32;
			var9 = var12;
		}

		var5.setColorOpaque_F(var7, var8, var9);
		IIcon var311 = getBlockIconFromSideAndMetadata(p_147741_1_, 0, 0);

		if (hasOverrideBlockTexture()) {
			var311 = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			var311 = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_, -1,
					var311);
		}

		final double var321 = var311.getMinV();
		final double var13 = var311.getMaxV();
		final int var15 = blockAccess.getBlockMetadata(p_147741_2_,
				p_147741_3_, p_147741_4_);

		for (int var16 = 0; var16 < 3; ++var16) {
			final double var17 = var16 * Math.PI * 2.0D / 3.0D + Math.PI / 2D;
			final double var19 = var311.getInterpolatedU(8.0D);
			double var21 = var311.getMaxU();

			if ((var15 & 1 << var16) != 0) {
				var21 = var311.getMinU();
			}

			final double var23 = p_147741_2_ + 0.5D;
			final double var25 = p_147741_2_ + 0.5D + Math.sin(var17) * 8.0D
					/ 16.0D;
			final double var27 = p_147741_4_ + 0.5D;
			final double var29 = p_147741_4_ + 0.5D + Math.cos(var17) * 8.0D
					/ 16.0D;
			var5.addVertexWithUV(var23, p_147741_3_ + 1, var27, var19, var321);
			var5.addVertexWithUV(var23, p_147741_3_ + 0, var27, var19, var13);
			var5.addVertexWithUV(var25, p_147741_3_ + 0, var29, var21, var13);
			var5.addVertexWithUV(var25, p_147741_3_ + 1, var29, var21, var321);
			var5.addVertexWithUV(var25, p_147741_3_ + 1, var29, var21, var321);
			var5.addVertexWithUV(var25, p_147741_3_ + 0, var29, var21, var13);
			var5.addVertexWithUV(var23, p_147741_3_ + 0, var27, var19, var13);
			var5.addVertexWithUV(var23, p_147741_3_ + 1, var27, var19, var321);
		}

		p_147741_1_.setBlockBoundsForItemRender();
		return true;
	}

	public boolean renderBlockByRenderType(Block par1Block, int par2, int par3,
			int par4) {
		final EventBlockRender blockRender = new EventBlockRender(par1Block,
				par2, par3, par4);
		Client.getEventManager().call(blockRender);
		renderAllFaces = blockRender.shouldRenderAllFaces();
		final int i = par1Block.getRenderType();

		if (i == -1)
			return false;
		else {
			par1Block.setBlockBoundsBasedOnState(blockAccess, par2, par3, par4);

			if (Config.isBetterSnow() && par1Block == Blocks.standing_sign
					&& hasSnowNeighbours(par2, par3, par4)) {
				renderSnow(par2, par3, par4,
						Blocks.snow_layer.getBlockBoundsMaxY());
			}

			setRenderBoundsFromBlock(par1Block);

			switch (i) {
			case 0:
				return renderStandardBlock(par1Block, par2, par3, par4);

			case 1:
				return renderCrossedSquares(par1Block, par2, par3, par4);

			case 2:
				return renderBlockTorch(par1Block, par2, par3, par4);

			case 3:
				return renderBlockFire((BlockFire) par1Block, par2, par3, par4);

			case 4:
				return renderBlockFluids(par1Block, par2, par3, par4);

			case 5:
				return renderBlockRedstoneWire(par1Block, par2, par3, par4);

			case 6:
				return renderBlockCrops(par1Block, par2, par3, par4);

			case 7:
				return renderBlockDoor(par1Block, par2, par3, par4);

			case 8:
				return renderBlockLadder(par1Block, par2, par3, par4);

			case 9:
				return renderBlockMinecartTrack((BlockRailBase) par1Block,
						par2, par3, par4);

			case 10:
				return renderBlockStairs((BlockStairs) par1Block, par2, par3,
						par4);

			case 11:
				return renderBlockFence((BlockFence) par1Block, par2, par3,
						par4);

			case 12:
				return renderBlockLever(par1Block, par2, par3, par4);

			case 13:
				return renderBlockCactus(par1Block, par2, par3, par4);

			case 14:
				return renderBlockBed(par1Block, par2, par3, par4);

			case 15:
				return renderBlockRepeater((BlockRedstoneRepeater) par1Block,
						par2, par3, par4);

			case 16:
				return renderPistonBase(par1Block, par2, par3, par4, false);

			case 17:
				return renderPistonExtension(par1Block, par2, par3, par4, true);

			case 18:
				return renderBlockPane((BlockPane) par1Block, par2, par3, par4);

			case 19:
				return renderBlockStem(par1Block, par2, par3, par4);

			case 20:
				return renderBlockVine(par1Block, par2, par3, par4);

			case 21:
				return renderBlockFenceGate((BlockFenceGate) par1Block, par2,
						par3, par4);

			case 22:
			default:
				if (Reflector.ModLoader.exists())
					return Reflector.callBoolean(
							Reflector.ModLoader_renderWorldBlock,
							new Object[] { this, blockAccess,
									Integer.valueOf(par2),
									Integer.valueOf(par3),
									Integer.valueOf(par4), par1Block,
									Integer.valueOf(i) });
				else {
					if (Reflector.FMLRenderAccessLibrary.exists())
						return Reflector
								.callBoolean(
										Reflector.FMLRenderAccessLibrary_renderWorldBlock,
										new Object[] { this, blockAccess,
												Integer.valueOf(par2),
												Integer.valueOf(par3),
												Integer.valueOf(par4),
												par1Block, Integer.valueOf(i) });

					return false;
				}

			case 23:
				return renderBlockLilyPad(par1Block, par2, par3, par4);

			case 24:
				return renderBlockCauldron((BlockCauldron) par1Block, par2,
						par3, par4);

			case 25:
				return renderBlockBrewingStand((BlockBrewingStand) par1Block,
						par2, par3, par4);

			case 26:
				return renderBlockEndPortalFrame(
						(BlockEndPortalFrame) par1Block, par2, par3, par4);

			case 27:
				return renderBlockDragonEgg((BlockDragonEgg) par1Block, par2,
						par3, par4);

			case 28:
				return renderBlockCocoa((BlockCocoa) par1Block, par2, par3,
						par4);

			case 29:
				return renderBlockTripWireSource(par1Block, par2, par3, par4);

			case 30:
				return renderBlockTripWire(par1Block, par2, par3, par4);

			case 31:
				return renderBlockLog(par1Block, par2, par3, par4);

			case 32:
				return renderBlockWall((BlockWall) par1Block, par2, par3, par4);

			case 33:
				return renderBlockFlowerpot((BlockFlowerPot) par1Block, par2,
						par3, par4);

			case 34:
				return renderBlockBeacon((BlockBeacon) par1Block, par2, par3,
						par4);

			case 35:
				return renderBlockAnvil((BlockAnvil) par1Block, par2, par3,
						par4);

			case 36:
				return renderBlockRedstoneDiode((BlockRedstoneDiode) par1Block,
						par2, par3, par4);

			case 37:
				return renderBlockRedstoneComparator(
						(BlockRedstoneComparator) par1Block, par2, par3, par4);

			case 38:
				return renderBlockHopper((BlockHopper) par1Block, par2, par3,
						par4);

			case 39:
				return renderBlockQuartz(par1Block, par2, par3, par4);

			case 40:
				return renderBlockDoublePlant((BlockDoublePlant) par1Block,
						par2, par3, par4);

			case 41:
				return renderBlockStainedGlassPane(par1Block, par2, par3, par4);
			}
		}
	}

	public boolean renderBlockCactus(Block p_147755_1_, int p_147755_2_,
			int p_147755_3_, int p_147755_4_) {
		final int var5 = p_147755_1_.colorMultiplier(blockAccess, p_147755_2_,
				p_147755_3_, p_147755_4_);
		float var6 = (var5 >> 16 & 255) / 255.0F;
		float var7 = (var5 >> 8 & 255) / 255.0F;
		float var8 = (var5 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var9 = (var6 * 30.0F + var7 * 59.0F + var8 * 11.0F) / 100.0F;
			final float var10 = (var6 * 30.0F + var7 * 70.0F) / 100.0F;
			final float var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
			var6 = var9;
			var7 = var10;
			var8 = var11;
		}

		return renderBlockCactusImpl(p_147755_1_, p_147755_2_, p_147755_3_,
				p_147755_4_, var6, var7, var8);
	}

	public boolean renderBlockCactusImpl(Block p_147754_1_, int p_147754_2_,
			int p_147754_3_, int p_147754_4_, float p_147754_5_,
			float p_147754_6_, float p_147754_7_) {
		final Tessellator var8 = Tessellator.instance;
		final float var10 = 0.5F;
		final float var11 = 1.0F;
		final float var12 = 0.8F;
		final float var13 = 0.6F;
		final float var14 = var10 * p_147754_5_;
		final float var15 = var11 * p_147754_5_;
		final float var16 = var12 * p_147754_5_;
		final float var17 = var13 * p_147754_5_;
		final float var18 = var10 * p_147754_6_;
		final float var19 = var11 * p_147754_6_;
		final float var20 = var12 * p_147754_6_;
		final float var21 = var13 * p_147754_6_;
		final float var22 = var10 * p_147754_7_;
		final float var23 = var11 * p_147754_7_;
		final float var24 = var12 * p_147754_7_;
		final float var25 = var13 * p_147754_7_;
		final float var26 = 0.0625F;
		final int var27 = p_147754_1_.getBlockBrightness(blockAccess,
				p_147754_2_, p_147754_3_, p_147754_4_);

		if (renderAllFaces
				|| p_147754_1_.shouldSideBeRendered(blockAccess, p_147754_2_,
						p_147754_3_ - 1, p_147754_4_, 0)) {
			var8.setBrightness(renderMinY > 0.0D ? var27 : p_147754_1_
					.getBlockBrightness(blockAccess, p_147754_2_,
							p_147754_3_ - 1, p_147754_4_));
			var8.setColorOpaque_F(var14, var18, var22);
			renderFaceYNeg(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
					this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_,
							p_147754_3_, p_147754_4_, 0));
		}

		if (renderAllFaces
				|| p_147754_1_.shouldSideBeRendered(blockAccess, p_147754_2_,
						p_147754_3_ + 1, p_147754_4_, 1)) {
			var8.setBrightness(renderMaxY < 1.0D ? var27 : p_147754_1_
					.getBlockBrightness(blockAccess, p_147754_2_,
							p_147754_3_ + 1, p_147754_4_));
			var8.setColorOpaque_F(var15, var19, var23);
			renderFaceYPos(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
					this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_,
							p_147754_3_, p_147754_4_, 1));
		}

		var8.setBrightness(var27);
		var8.setColorOpaque_F(var16, var20, var24);
		var8.addTranslation(0.0F, 0.0F, var26);
		renderFaceZNeg(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_,
						p_147754_3_, p_147754_4_, 2));
		var8.addTranslation(0.0F, 0.0F, -var26);
		var8.addTranslation(0.0F, 0.0F, -var26);
		renderFaceZPos(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_,
						p_147754_3_, p_147754_4_, 3));
		var8.addTranslation(0.0F, 0.0F, var26);
		var8.setColorOpaque_F(var17, var21, var25);
		var8.addTranslation(var26, 0.0F, 0.0F);
		renderFaceXNeg(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_,
						p_147754_3_, p_147754_4_, 4));
		var8.addTranslation(-var26, 0.0F, 0.0F);
		var8.addTranslation(-var26, 0.0F, 0.0F);
		renderFaceXPos(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_,
						p_147754_3_, p_147754_4_, 5));
		var8.addTranslation(var26, 0.0F, 0.0F);
		return true;
	}

	/**
	 * Render block cauldron
	 */
	public boolean renderBlockCauldron(BlockCauldron p_147785_1_,
			int p_147785_2_, int p_147785_3_, int p_147785_4_) {
		renderStandardBlock(p_147785_1_, p_147785_2_, p_147785_3_, p_147785_4_);
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147785_1_.getBlockBrightness(blockAccess,
				p_147785_2_, p_147785_3_, p_147785_4_));
		final int var6 = p_147785_1_.colorMultiplier(blockAccess, p_147785_2_,
				p_147785_3_, p_147785_4_);
		float var7 = (var6 >> 16 & 255) / 255.0F;
		float var8 = (var6 >> 8 & 255) / 255.0F;
		float var9 = (var6 & 255) / 255.0F;
		float var11;

		if (EntityRenderer.anaglyphEnable) {
			final float var15 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
			var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
			final float var16 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
			var7 = var15;
			var8 = var11;
			var9 = var16;
		}

		var5.setColorOpaque_F(var7, var8, var9);
		final IIcon var151 = p_147785_1_.getBlockTextureFromSide(2);
		var11 = 0.125F;
		renderFaceXPos(p_147785_1_, p_147785_2_ - 1.0F + var11, p_147785_3_,
				p_147785_4_, var151);
		renderFaceXNeg(p_147785_1_, p_147785_2_ + 1.0F - var11, p_147785_3_,
				p_147785_4_, var151);
		renderFaceZPos(p_147785_1_, p_147785_2_, p_147785_3_, p_147785_4_
				- 1.0F + var11, var151);
		renderFaceZNeg(p_147785_1_, p_147785_2_, p_147785_3_, p_147785_4_
				+ 1.0F - var11, var151);
		final IIcon var161 = BlockCauldron.func_150026_e("inner");
		renderFaceYPos(p_147785_1_, p_147785_2_, p_147785_3_ - 1.0F + 0.25F,
				p_147785_4_, var161);
		renderFaceYNeg(p_147785_1_, p_147785_2_, p_147785_3_ + 1.0F - 0.75F,
				p_147785_4_, var161);
		final int var13 = blockAccess.getBlockMetadata(p_147785_2_,
				p_147785_3_, p_147785_4_);

		if (var13 > 0) {
			final IIcon var14 = BlockLiquid.func_149803_e("water_still");
			final int wc = CustomColorizer.getFluidColor(Blocks.water,
					blockAccess, p_147785_2_, p_147785_3_, p_147785_4_);
			final float wr = (wc >> 16 & 255) / 255.0F;
			final float wg = (wc >> 8 & 255) / 255.0F;
			final float wb = (wc & 255) / 255.0F;
			var5.setColorOpaque_F(wr, wg, wb);
			renderFaceYPos(p_147785_1_, p_147785_2_, p_147785_3_ - 1.0F
					+ BlockCauldron.func_150025_c(var13), p_147785_4_, var14);
		}

		return true;
	}

	public boolean renderBlockCocoa(BlockCocoa p_147772_1_, int p_147772_2_,
			int p_147772_3_, int p_147772_4_) {
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147772_1_.getBlockBrightness(blockAccess,
				p_147772_2_, p_147772_3_, p_147772_4_));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final int var6 = blockAccess.getBlockMetadata(p_147772_2_, p_147772_3_,
				p_147772_4_);
		final int var7 = BlockDirectional.func_149895_l(var6);
		final int var8 = BlockCocoa.func_149987_c(var6);
		final IIcon var9 = p_147772_1_.func_149988_b(var8);
		final int var10 = 4 + var8 * 2;
		final int var11 = 5 + var8 * 2;
		final double var12 = 15.0D - var10;
		final double var14 = 15.0D;
		final double var16 = 4.0D;
		final double var18 = 4.0D + var11;
		double var20 = var9.getInterpolatedU(var12);
		double var22 = var9.getInterpolatedU(var14);
		double var24 = var9.getInterpolatedV(var16);
		double var26 = var9.getInterpolatedV(var18);
		double var28 = 0.0D;
		double var30 = 0.0D;

		switch (var7) {
		case 0:
			var28 = 8.0D - var10 / 2;
			var30 = 15.0D - var10;
			break;

		case 1:
			var28 = 1.0D;
			var30 = 8.0D - var10 / 2;
			break;

		case 2:
			var28 = 8.0D - var10 / 2;
			var30 = 1.0D;
			break;

		case 3:
			var28 = 15.0D - var10;
			var30 = 8.0D - var10 / 2;
		}

		double var32 = p_147772_2_ + var28 / 16.0D;
		double var34 = p_147772_2_ + (var28 + var10) / 16.0D;
		double var36 = p_147772_3_ + (12.0D - var11) / 16.0D;
		double var38 = p_147772_3_ + 0.75D;
		double var40 = p_147772_4_ + var30 / 16.0D;
		double var42 = p_147772_4_ + (var30 + var10) / 16.0D;
		var5.addVertexWithUV(var32, var36, var40, var20, var26);
		var5.addVertexWithUV(var32, var36, var42, var22, var26);
		var5.addVertexWithUV(var32, var38, var42, var22, var24);
		var5.addVertexWithUV(var32, var38, var40, var20, var24);
		var5.addVertexWithUV(var34, var36, var42, var20, var26);
		var5.addVertexWithUV(var34, var36, var40, var22, var26);
		var5.addVertexWithUV(var34, var38, var40, var22, var24);
		var5.addVertexWithUV(var34, var38, var42, var20, var24);
		var5.addVertexWithUV(var34, var36, var40, var20, var26);
		var5.addVertexWithUV(var32, var36, var40, var22, var26);
		var5.addVertexWithUV(var32, var38, var40, var22, var24);
		var5.addVertexWithUV(var34, var38, var40, var20, var24);
		var5.addVertexWithUV(var32, var36, var42, var20, var26);
		var5.addVertexWithUV(var34, var36, var42, var22, var26);
		var5.addVertexWithUV(var34, var38, var42, var22, var24);
		var5.addVertexWithUV(var32, var38, var42, var20, var24);
		int var44 = var10;

		if (var8 >= 2) {
			var44 = var10 - 1;
		}

		var20 = var9.getMinU();
		var22 = var9.getInterpolatedU(var44);
		var24 = var9.getMinV();
		var26 = var9.getInterpolatedV(var44);
		var5.addVertexWithUV(var32, var38, var42, var20, var26);
		var5.addVertexWithUV(var34, var38, var42, var22, var26);
		var5.addVertexWithUV(var34, var38, var40, var22, var24);
		var5.addVertexWithUV(var32, var38, var40, var20, var24);
		var5.addVertexWithUV(var32, var36, var40, var20, var24);
		var5.addVertexWithUV(var34, var36, var40, var22, var24);
		var5.addVertexWithUV(var34, var36, var42, var22, var26);
		var5.addVertexWithUV(var32, var36, var42, var20, var26);
		var20 = var9.getInterpolatedU(12.0D);
		var22 = var9.getMaxU();
		var24 = var9.getMinV();
		var26 = var9.getInterpolatedV(4.0D);
		var28 = 8.0D;
		var30 = 0.0D;
		double var45;

		switch (var7) {
		case 0:
			var28 = 8.0D;
			var30 = 12.0D;
			var45 = var20;
			var20 = var22;
			var22 = var45;
			break;

		case 1:
			var28 = 0.0D;
			var30 = 8.0D;
			break;

		case 2:
			var28 = 8.0D;
			var30 = 0.0D;
			break;

		case 3:
			var28 = 12.0D;
			var30 = 8.0D;
			var45 = var20;
			var20 = var22;
			var22 = var45;
		}

		var32 = p_147772_2_ + var28 / 16.0D;
		var34 = p_147772_2_ + (var28 + 4.0D) / 16.0D;
		var36 = p_147772_3_ + 0.75D;
		var38 = p_147772_3_ + 1.0D;
		var40 = p_147772_4_ + var30 / 16.0D;
		var42 = p_147772_4_ + (var30 + 4.0D) / 16.0D;

		if (var7 != 2 && var7 != 0) {
			if (var7 == 1 || var7 == 3) {
				var5.addVertexWithUV(var34, var36, var40, var20, var26);
				var5.addVertexWithUV(var32, var36, var40, var22, var26);
				var5.addVertexWithUV(var32, var38, var40, var22, var24);
				var5.addVertexWithUV(var34, var38, var40, var20, var24);
				var5.addVertexWithUV(var32, var36, var40, var22, var26);
				var5.addVertexWithUV(var34, var36, var40, var20, var26);
				var5.addVertexWithUV(var34, var38, var40, var20, var24);
				var5.addVertexWithUV(var32, var38, var40, var22, var24);
			}
		} else {
			var5.addVertexWithUV(var32, var36, var40, var22, var26);
			var5.addVertexWithUV(var32, var36, var42, var20, var26);
			var5.addVertexWithUV(var32, var38, var42, var20, var24);
			var5.addVertexWithUV(var32, var38, var40, var22, var24);
			var5.addVertexWithUV(var32, var36, var42, var20, var26);
			var5.addVertexWithUV(var32, var36, var40, var22, var26);
			var5.addVertexWithUV(var32, var38, var40, var22, var24);
			var5.addVertexWithUV(var32, var38, var42, var20, var24);
		}

		return true;
	}

	public boolean renderBlockCrops(Block p_147796_1_, int p_147796_2_,
			int p_147796_3_, int p_147796_4_) {
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147796_1_.getBlockBrightness(blockAccess,
				p_147796_2_, p_147796_3_, p_147796_4_));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		renderBlockCropsImpl(p_147796_1_, blockAccess.getBlockMetadata(
				p_147796_2_, p_147796_3_, p_147796_4_), p_147796_2_,
				p_147796_3_ - 0.0625F, p_147796_4_);
		return true;
	}

	public void renderBlockCropsImpl(Block p_147795_1_, int p_147795_2_,
			double p_147795_3_, double p_147795_5_, double p_147795_7_) {
		final Tessellator var9 = Tessellator.instance;
		IIcon var10 = getBlockIconFromSideAndMetadata(p_147795_1_, 0,
				p_147795_2_);

		if (hasOverrideBlockTexture()) {
			var10 = overrideBlockTexture;
		}

		final double var11 = var10.getMinU();
		final double var13 = var10.getMinV();
		final double var15 = var10.getMaxU();
		final double var17 = var10.getMaxV();
		double var19 = p_147795_3_ + 0.5D - 0.25D;
		double var21 = p_147795_3_ + 0.5D + 0.25D;
		double var23 = p_147795_7_ + 0.5D - 0.5D;
		double var25 = p_147795_7_ + 0.5D + 0.5D;
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var25, var15, var13);
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var25, var15, var13);
		var19 = p_147795_3_ + 0.5D - 0.5D;
		var21 = p_147795_3_ + 0.5D + 0.5D;
		var23 = p_147795_7_ + 0.5D - 0.25D;
		var25 = p_147795_7_ + 0.5D + 0.25D;
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var23, var11, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var23, var11, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var23, var15, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var23, var15, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var25, var15, var13);
		var9.addVertexWithUV(var19, p_147795_5_ + 1.0D, var25, var11, var13);
		var9.addVertexWithUV(var19, p_147795_5_ + 0.0D, var25, var11, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 0.0D, var25, var15, var17);
		var9.addVertexWithUV(var21, p_147795_5_ + 1.0D, var25, var15, var13);
	}

	public boolean renderBlockDoor(Block p_147760_1_, int p_147760_2_,
			int p_147760_3_, int p_147760_4_) {
		final Tessellator var5 = Tessellator.instance;
		final int var6 = blockAccess.getBlockMetadata(p_147760_2_, p_147760_3_,
				p_147760_4_);

		if ((var6 & 8) != 0) {
			if (blockAccess.getBlock(p_147760_2_, p_147760_3_ - 1, p_147760_4_) != p_147760_1_)
				return false;
		} else if (blockAccess.getBlock(p_147760_2_, p_147760_3_ + 1,
				p_147760_4_) != p_147760_1_)
			return false;

		boolean var7 = false;
		final float var8 = 0.5F;
		final float var9 = 1.0F;
		final float var10 = 0.8F;
		final float var11 = 0.6F;
		final int var12 = p_147760_1_.getBlockBrightness(blockAccess,
				p_147760_2_, p_147760_3_, p_147760_4_);
		var5.setBrightness(renderMinY > 0.0D ? var12 : p_147760_1_
				.getBlockBrightness(blockAccess, p_147760_2_, p_147760_3_ - 1,
						p_147760_4_));
		var5.setColorOpaque_F(var8, var8, var8);
		renderFaceYNeg(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_,
						p_147760_3_, p_147760_4_, 0));
		var7 = true;
		var5.setBrightness(renderMaxY < 1.0D ? var12 : p_147760_1_
				.getBlockBrightness(blockAccess, p_147760_2_, p_147760_3_ + 1,
						p_147760_4_));
		var5.setColorOpaque_F(var9, var9, var9);
		renderFaceYPos(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_,
						p_147760_3_, p_147760_4_, 1));
		var7 = true;
		var5.setBrightness(renderMinZ > 0.0D ? var12 : p_147760_1_
				.getBlockBrightness(blockAccess, p_147760_2_, p_147760_3_,
						p_147760_4_ - 1));
		var5.setColorOpaque_F(var10, var10, var10);
		IIcon var13 = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_,
				p_147760_3_, p_147760_4_, 2);
		renderFaceZNeg(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				var13);
		var7 = true;
		flipTexture = false;
		var5.setBrightness(renderMaxZ < 1.0D ? var12 : p_147760_1_
				.getBlockBrightness(blockAccess, p_147760_2_, p_147760_3_,
						p_147760_4_ + 1));
		var5.setColorOpaque_F(var10, var10, var10);
		var13 = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_,
				p_147760_3_, p_147760_4_, 3);
		renderFaceZPos(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				var13);
		var7 = true;
		flipTexture = false;
		var5.setBrightness(renderMinX > 0.0D ? var12 : p_147760_1_
				.getBlockBrightness(blockAccess, p_147760_2_ - 1, p_147760_3_,
						p_147760_4_));
		var5.setColorOpaque_F(var11, var11, var11);
		var13 = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_,
				p_147760_3_, p_147760_4_, 4);
		renderFaceXNeg(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				var13);
		var7 = true;
		flipTexture = false;
		var5.setBrightness(renderMaxX < 1.0D ? var12 : p_147760_1_
				.getBlockBrightness(blockAccess, p_147760_2_ + 1, p_147760_3_,
						p_147760_4_));
		var5.setColorOpaque_F(var11, var11, var11);
		var13 = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_,
				p_147760_3_, p_147760_4_, 5);
		renderFaceXPos(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				var13);
		var7 = true;
		flipTexture = false;
		return var7;
	}

	public boolean renderBlockDoublePlant(BlockDoublePlant p_147774_1_,
			int p_147774_2_, int p_147774_3_, int p_147774_4_) {
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147774_1_.getBlockBrightness(blockAccess,
				p_147774_2_, p_147774_3_, p_147774_4_));
		final int var6 = CustomColorizer.getColorMultiplier(p_147774_1_,
				blockAccess, p_147774_2_, p_147774_3_, p_147774_4_);
		float var7 = (var6 >> 16 & 255) / 255.0F;
		float var8 = (var6 >> 8 & 255) / 255.0F;
		float var9 = (var6 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var58 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
			final float var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
			final float var59 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
			var7 = var58;
			var8 = var11;
			var9 = var59;
		}

		var5.setColorOpaque_F(var7, var8, var9);
		long var581 = p_147774_2_ * 3129871 ^ p_147774_4_ * 116129781L;
		var581 = var581 * var581 * 42317861L + var581 * 11L;
		double var591 = p_147774_2_;
		final double var14 = p_147774_3_;
		double var16 = p_147774_4_;
		var591 += ((var581 >> 16 & 15L) / 15.0F - 0.5D) * 0.3D;
		var16 += ((var581 >> 24 & 15L) / 15.0F - 0.5D) * 0.3D;
		final int var18 = blockAccess.getBlockMetadata(p_147774_2_,
				p_147774_3_, p_147774_4_);
		final boolean var20 = BlockDoublePlant.func_149887_c(var18);
		int var60;

		if (var20) {
			if (blockAccess.getBlock(p_147774_2_, p_147774_3_ - 1, p_147774_4_) != p_147774_1_)
				return false;

			var60 = BlockDoublePlant
					.func_149890_d(blockAccess.getBlockMetadata(p_147774_2_,
							p_147774_3_ - 1, p_147774_4_));
		} else {
			var60 = BlockDoublePlant.func_149890_d(var18);
		}

		final IIcon var21 = p_147774_1_.func_149888_a(var20, var60);
		drawCrossedSquares(var21, var591, var14, var16, 1.0F);

		if (var20 && var60 == 0) {
			final IIcon var22 = p_147774_1_.field_149891_b[0];
			final double var23 = Math.cos(var581 * 0.8D) * Math.PI * 0.1D;
			final double var25 = Math.cos(var23);
			final double var27 = Math.sin(var23);
			double var29 = var22.getMinU();
			double var31 = var22.getMinV();
			double var33 = var22.getMaxU();
			double var35 = var22.getMaxV();
			final double var41 = 0.5D + 0.3D * var25 - 0.5D * var27;
			final double var43 = 0.5D + 0.5D * var25 + 0.3D * var27;
			final double var45 = 0.5D + 0.3D * var25 + 0.5D * var27;
			final double var47 = 0.5D + -0.5D * var25 + 0.3D * var27;
			final double var49 = 0.5D + -0.05D * var25 + 0.5D * var27;
			final double var51 = 0.5D + -0.5D * var25 + -0.05D * var27;
			final double var53 = 0.5D + -0.05D * var25 - 0.5D * var27;
			final double var55 = 0.5D + 0.5D * var25 + -0.05D * var27;
			var5.addVertexWithUV(var591 + var49, var14 + 1.0D, var16 + var51,
					var29, var35);
			var5.addVertexWithUV(var591 + var53, var14 + 1.0D, var16 + var55,
					var33, var35);
			var5.addVertexWithUV(var591 + var41, var14 + 0.0D, var16 + var43,
					var33, var31);
			var5.addVertexWithUV(var591 + var45, var14 + 0.0D, var16 + var47,
					var29, var31);
			final IIcon var57 = p_147774_1_.field_149891_b[1];
			var29 = var57.getMinU();
			var31 = var57.getMinV();
			var33 = var57.getMaxU();
			var35 = var57.getMaxV();
			var5.addVertexWithUV(var591 + var53, var14 + 1.0D, var16 + var55,
					var29, var35);
			var5.addVertexWithUV(var591 + var49, var14 + 1.0D, var16 + var51,
					var33, var35);
			var5.addVertexWithUV(var591 + var45, var14 + 0.0D, var16 + var47,
					var33, var31);
			var5.addVertexWithUV(var591 + var41, var14 + 0.0D, var16 + var43,
					var29, var31);
		}

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147774_2_, p_147774_3_, p_147774_4_)) {
			renderSnow(p_147774_2_, p_147774_3_, p_147774_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		return true;
	}

	public boolean renderBlockDragonEgg(BlockDragonEgg p_147802_1_,
			int p_147802_2_, int p_147802_3_, int p_147802_4_) {
		boolean var5 = false;
		int var6 = 0;

		for (int var7 = 0; var7 < 8; ++var7) {
			byte var8 = 0;
			byte var9 = 1;

			if (var7 == 0) {
				var8 = 2;
			}

			if (var7 == 1) {
				var8 = 3;
			}

			if (var7 == 2) {
				var8 = 4;
			}

			if (var7 == 3) {
				var8 = 5;
				var9 = 2;
			}

			if (var7 == 4) {
				var8 = 6;
				var9 = 3;
			}

			if (var7 == 5) {
				var8 = 7;
				var9 = 5;
			}

			if (var7 == 6) {
				var8 = 6;
				var9 = 2;
			}

			if (var7 == 7) {
				var8 = 3;
			}

			final float var10 = var8 / 16.0F;
			final float var11 = 1.0F - var6 / 16.0F;
			final float var12 = 1.0F - (var6 + var9) / 16.0F;
			var6 += var9;
			setRenderBounds(0.5F - var10, var12, 0.5F - var10, 0.5F + var10,
					var11, 0.5F + var10);
			renderStandardBlock(p_147802_1_, p_147802_2_, p_147802_3_,
					p_147802_4_);
		}

		var5 = true;
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return var5;
	}

	/**
	 * Render BlockEndPortalFrame
	 */
	public boolean renderBlockEndPortalFrame(BlockEndPortalFrame p_147743_1_,
			int p_147743_2_, int p_147743_3_, int p_147743_4_) {
		final int var5 = blockAccess.getBlockMetadata(p_147743_2_, p_147743_3_,
				p_147743_4_);
		final int var6 = var5 & 3;

		if (var6 == 0) {
			uvRotateTop = 3;
		} else if (var6 == 3) {
			uvRotateTop = 1;
		} else if (var6 == 1) {
			uvRotateTop = 2;
		}

		if (!BlockEndPortalFrame.func_150020_b(var5)) {
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
			renderStandardBlock(p_147743_1_, p_147743_2_, p_147743_3_,
					p_147743_4_);
			uvRotateTop = 0;
			return true;
		} else {
			renderAllFaces = true;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
			renderStandardBlock(p_147743_1_, p_147743_2_, p_147743_3_,
					p_147743_4_);
			setOverrideBlockTexture(p_147743_1_.func_150021_e());
			setRenderBounds(0.25D, 0.8125D, 0.25D, 0.75D, 1.0D, 0.75D);
			renderStandardBlock(p_147743_1_, p_147743_2_, p_147743_3_,
					p_147743_4_);
			renderAllFaces = false;
			clearOverrideBlockTexture();
			uvRotateTop = 0;
			return true;
		}
	}

	public boolean renderBlockFence(BlockFence p_147735_1_, int p_147735_2_,
			int p_147735_3_, int p_147735_4_) {
		boolean var5 = false;
		float var6 = 0.375F;
		float var7 = 0.625F;
		setRenderBounds(var6, 0.0D, var6, var7, 1.0D, var7);
		renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_, p_147735_4_);
		var5 = true;
		boolean var8 = false;
		boolean var9 = false;

		if (p_147735_1_.func_149826_e(blockAccess, p_147735_2_ - 1,
				p_147735_3_, p_147735_4_)
				|| p_147735_1_.func_149826_e(blockAccess, p_147735_2_ + 1,
						p_147735_3_, p_147735_4_)) {
			var8 = true;
		}

		if (p_147735_1_.func_149826_e(blockAccess, p_147735_2_, p_147735_3_,
				p_147735_4_ - 1)
				|| p_147735_1_.func_149826_e(blockAccess, p_147735_2_,
						p_147735_3_, p_147735_4_ + 1)) {
			var9 = true;
		}

		final boolean var10 = p_147735_1_.func_149826_e(blockAccess,
				p_147735_2_ - 1, p_147735_3_, p_147735_4_);
		final boolean var11 = p_147735_1_.func_149826_e(blockAccess,
				p_147735_2_ + 1, p_147735_3_, p_147735_4_);
		final boolean var12 = p_147735_1_.func_149826_e(blockAccess,
				p_147735_2_, p_147735_3_, p_147735_4_ - 1);
		final boolean var13 = p_147735_1_.func_149826_e(blockAccess,
				p_147735_2_, p_147735_3_, p_147735_4_ + 1);

		if (!var8 && !var9) {
			var8 = true;
		}

		var6 = 0.4375F;
		var7 = 0.5625F;
		float var14 = 0.75F;
		float var15 = 0.9375F;
		final float var16 = var10 ? 0.0F : var6;
		final float var17 = var11 ? 1.0F : var7;
		final float var18 = var12 ? 0.0F : var6;
		final float var19 = var13 ? 1.0F : var7;
		field_152631_f = true;

		if (var8) {
			setRenderBounds(var16, var14, var6, var17, var15, var7);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_,
					p_147735_4_);
			var5 = true;
		}

		if (var9) {
			setRenderBounds(var6, var14, var18, var7, var15, var19);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_,
					p_147735_4_);
			var5 = true;
		}

		var14 = 0.375F;
		var15 = 0.5625F;

		if (var8) {
			setRenderBounds(var16, var14, var6, var17, var15, var7);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_,
					p_147735_4_);
			var5 = true;
		}

		if (var9) {
			setRenderBounds(var6, var14, var18, var7, var15, var19);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_,
					p_147735_4_);
			var5 = true;
		}

		field_152631_f = false;
		p_147735_1_.setBlockBoundsBasedOnState(blockAccess, p_147735_2_,
				p_147735_3_, p_147735_4_);

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147735_2_, p_147735_3_, p_147735_4_)) {
			renderSnow(p_147735_2_, p_147735_3_, p_147735_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		return var5;
	}

	public boolean renderBlockFenceGate(BlockFenceGate p_147776_1_,
			int p_147776_2_, int p_147776_3_, int p_147776_4_) {
		final boolean var5 = true;
		final int var6 = blockAccess.getBlockMetadata(p_147776_2_, p_147776_3_,
				p_147776_4_);
		final boolean var7 = BlockFenceGate.isFenceGateOpen(var6);
		final int var8 = BlockDirectional.func_149895_l(var6);
		float var9 = 0.375F;
		float var10 = 0.5625F;
		float var11 = 0.75F;
		float var12 = 0.9375F;
		float var13 = 0.3125F;
		float var14 = 1.0F;

		if ((var8 == 2 || var8 == 0)
				&& blockAccess.getBlock(p_147776_2_ - 1, p_147776_3_,
						p_147776_4_) == Blocks.cobblestone_wall
				&& blockAccess.getBlock(p_147776_2_ + 1, p_147776_3_,
						p_147776_4_) == Blocks.cobblestone_wall
				|| (var8 == 3 || var8 == 1)
				&& blockAccess.getBlock(p_147776_2_, p_147776_3_,
						p_147776_4_ - 1) == Blocks.cobblestone_wall
				&& blockAccess.getBlock(p_147776_2_, p_147776_3_,
						p_147776_4_ + 1) == Blocks.cobblestone_wall) {
			var9 -= 0.1875F;
			var10 -= 0.1875F;
			var11 -= 0.1875F;
			var12 -= 0.1875F;
			var13 -= 0.1875F;
			var14 -= 0.1875F;
		}

		renderAllFaces = true;
		float var15;
		float var16;
		float var17;
		float var18;

		if (var8 != 3 && var8 != 1) {
			var15 = 0.0F;
			var16 = 0.125F;
			var17 = 0.4375F;
			var18 = 0.5625F;
			setRenderBounds(var15, var13, var17, var16, var14, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var15 = 0.875F;
			var16 = 1.0F;
			setRenderBounds(var15, var13, var17, var16, var14, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
		} else {
			uvRotateTop = 1;
			var15 = 0.4375F;
			var16 = 0.5625F;
			var17 = 0.0F;
			var18 = 0.125F;
			setRenderBounds(var15, var13, var17, var16, var14, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var17 = 0.875F;
			var18 = 1.0F;
			setRenderBounds(var15, var13, var17, var16, var14, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			uvRotateTop = 0;
		}

		if (var7) {
			if (var8 == 2 || var8 == 0) {
				uvRotateTop = 1;
			}

			if (var8 == 3) {
				var15 = 0.0F;
				var16 = 0.125F;
				var17 = 0.875F;
				var18 = 1.0F;
				setRenderBounds(0.8125D, var9, 0.0D, 0.9375D, var12, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.8125D, var9, 0.875D, 0.9375D, var12, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.5625D, var9, 0.0D, 0.8125D, var10, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.5625D, var9, 0.875D, 0.8125D, var10, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.5625D, var11, 0.0D, 0.8125D, var12, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.5625D, var11, 0.875D, 0.8125D, var12, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
			} else if (var8 == 1) {
				var15 = 0.0F;
				var16 = 0.125F;
				var17 = 0.875F;
				var18 = 1.0F;
				setRenderBounds(0.0625D, var9, 0.0D, 0.1875D, var12, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.0625D, var9, 0.875D, 0.1875D, var12, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.1875D, var9, 0.0D, 0.4375D, var10, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.1875D, var9, 0.875D, 0.4375D, var10, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.1875D, var11, 0.0D, 0.4375D, var12, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.1875D, var11, 0.875D, 0.4375D, var12, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
			} else if (var8 == 0) {
				var15 = 0.0F;
				var16 = 0.125F;
				var17 = 0.875F;
				var18 = 1.0F;
				setRenderBounds(0.0D, var9, 0.8125D, 0.125D, var12, 0.9375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.875D, var9, 0.8125D, 1.0D, var12, 0.9375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.0D, var9, 0.5625D, 0.125D, var10, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.875D, var9, 0.5625D, 1.0D, var10, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.0D, var11, 0.5625D, 0.125D, var12, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.875D, var11, 0.5625D, 1.0D, var12, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
			} else if (var8 == 2) {
				var15 = 0.0F;
				var16 = 0.125F;
				var17 = 0.875F;
				var18 = 1.0F;
				setRenderBounds(0.0D, var9, 0.0625D, 0.125D, var12, 0.1875D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.875D, var9, 0.0625D, 1.0D, var12, 0.1875D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.0D, var9, 0.1875D, 0.125D, var10, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.875D, var9, 0.1875D, 1.0D, var10, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.0D, var11, 0.1875D, 0.125D, var12, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
				setRenderBounds(0.875D, var11, 0.1875D, 1.0D, var12, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
						p_147776_4_);
			}
		} else if (var8 != 3 && var8 != 1) {
			var15 = 0.375F;
			var16 = 0.5F;
			var17 = 0.4375F;
			var18 = 0.5625F;
			setRenderBounds(var15, var9, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var15 = 0.5F;
			var16 = 0.625F;
			setRenderBounds(var15, var9, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var15 = 0.625F;
			var16 = 0.875F;
			setRenderBounds(var15, var9, var17, var16, var10, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			setRenderBounds(var15, var11, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var15 = 0.125F;
			var16 = 0.375F;
			setRenderBounds(var15, var9, var17, var16, var10, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			setRenderBounds(var15, var11, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
		} else {
			uvRotateTop = 1;
			var15 = 0.4375F;
			var16 = 0.5625F;
			var17 = 0.375F;
			var18 = 0.5F;
			setRenderBounds(var15, var9, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var17 = 0.5F;
			var18 = 0.625F;
			setRenderBounds(var15, var9, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var17 = 0.625F;
			var18 = 0.875F;
			setRenderBounds(var15, var9, var17, var16, var10, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			setRenderBounds(var15, var11, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			var17 = 0.125F;
			var18 = 0.375F;
			setRenderBounds(var15, var9, var17, var16, var10, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
			setRenderBounds(var15, var11, var17, var16, var12, var18);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_,
					p_147776_4_);
		}

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147776_2_, p_147776_3_, p_147776_4_)) {
			renderSnow(p_147776_2_, p_147776_3_, p_147776_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		renderAllFaces = false;
		uvRotateTop = 0;
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return var5;
	}

	public boolean renderBlockFire(BlockFire p_147801_1_, int p_147801_2_,
			int p_147801_3_, int p_147801_4_) {
		final Tessellator var5 = Tessellator.instance;
		final IIcon var6 = p_147801_1_.func_149840_c(0);
		final IIcon var7 = p_147801_1_.func_149840_c(1);
		IIcon var8 = var6;

		if (hasOverrideBlockTexture()) {
			var8 = overrideBlockTexture;
		}

		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		var5.setBrightness(p_147801_1_.getBlockBrightness(blockAccess,
				p_147801_2_, p_147801_3_, p_147801_4_));
		double var9 = var8.getMinU();
		double var11 = var8.getMinV();
		double var13 = var8.getMaxU();
		double var15 = var8.getMaxV();
		float var17 = 1.4F;
		double var20;
		double var22;
		double var24;
		double var26;
		double var28;
		double var30;
		double var32;

		if (!World.doesBlockHaveSolidTopSurface(blockAccess, p_147801_2_,
				p_147801_3_ - 1, p_147801_4_)
				&& !Blocks.fire.func_149844_e(blockAccess, p_147801_2_,
						p_147801_3_ - 1, p_147801_4_)) {
			final float var36 = 0.2F;
			final float var19 = 0.0625F;

			if ((p_147801_2_ + p_147801_3_ + p_147801_4_ & 1) == 1) {
				var9 = var7.getMinU();
				var11 = var7.getMinV();
				var13 = var7.getMaxU();
				var15 = var7.getMaxV();
			}

			if ((p_147801_2_ / 2 + p_147801_3_ / 2 + p_147801_4_ / 2 & 1) == 1) {
				var20 = var13;
				var13 = var9;
				var9 = var20;
			}

			if (Blocks.fire.func_149844_e(blockAccess, p_147801_2_ - 1,
					p_147801_3_, p_147801_4_)) {
				var5.addVertexWithUV(p_147801_2_ + var36, p_147801_3_ + var17
						+ var19, p_147801_4_ + 1, var13, var11);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 1, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + var36, p_147801_3_ + var17
						+ var19, p_147801_4_ + 0, var9, var11);
				var5.addVertexWithUV(p_147801_2_ + var36, p_147801_3_ + var17
						+ var19, p_147801_4_ + 0, var9, var11);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 1, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + var36, p_147801_3_ + var17
						+ var19, p_147801_4_ + 1, var13, var11);
			}

			if (Blocks.fire.func_149844_e(blockAccess, p_147801_2_ + 1,
					p_147801_3_, p_147801_4_)) {
				var5.addVertexWithUV(p_147801_2_ + 1 - var36, p_147801_3_
						+ var17 + var19, p_147801_4_ + 0, var9, var11);
				var5.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0
						+ var19, p_147801_4_ + 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0
						+ var19, p_147801_4_ + 1, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + 1 - var36, p_147801_3_
						+ var17 + var19, p_147801_4_ + 1, var13, var11);
				var5.addVertexWithUV(p_147801_2_ + 1 - var36, p_147801_3_
						+ var17 + var19, p_147801_4_ + 1, var13, var11);
				var5.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0
						+ var19, p_147801_4_ + 1, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0
						+ var19, p_147801_4_ + 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + 1 - var36, p_147801_3_
						+ var17 + var19, p_147801_4_ + 0, var9, var11);
			}

			if (Blocks.fire.func_149844_e(blockAccess, p_147801_2_,
					p_147801_3_, p_147801_4_ - 1)) {
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17
						+ var19, p_147801_4_ + var36, var13, var11);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 0, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + var19,
						p_147801_4_ + 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17
						+ var19, p_147801_4_ + var36, var9, var11);
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17
						+ var19, p_147801_4_ + var36, var9, var11);
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + var19,
						p_147801_4_ + 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 0, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17
						+ var19, p_147801_4_ + var36, var13, var11);
			}

			if (Blocks.fire.func_149844_e(blockAccess, p_147801_2_,
					p_147801_3_, p_147801_4_ + 1)) {
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17
						+ var19, p_147801_4_ + 1 - var36, var9, var11);
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + var19,
						p_147801_4_ + 1 - 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 1 - 0, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17
						+ var19, p_147801_4_ + 1 - var36, var13, var11);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17
						+ var19, p_147801_4_ + 1 - var36, var13, var11);
				var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + var19,
						p_147801_4_ + 1 - 0, var13, var15);
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + var19,
						p_147801_4_ + 1 - 0, var9, var15);
				var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17
						+ var19, p_147801_4_ + 1 - var36, var9, var11);
			}

			if (Blocks.fire.func_149844_e(blockAccess, p_147801_2_,
					p_147801_3_ + 1, p_147801_4_)) {
				var20 = p_147801_2_ + 0.5D + 0.5D;
				var22 = p_147801_2_ + 0.5D - 0.5D;
				var24 = p_147801_4_ + 0.5D + 0.5D;
				var26 = p_147801_4_ + 0.5D - 0.5D;
				var28 = p_147801_2_ + 0.5D - 0.5D;
				var30 = p_147801_2_ + 0.5D + 0.5D;
				var32 = p_147801_4_ + 0.5D - 0.5D;
				final double var34 = p_147801_4_ + 0.5D + 0.5D;
				var9 = var6.getMinU();
				var11 = var6.getMinV();
				var13 = var6.getMaxU();
				var15 = var6.getMaxV();
				++p_147801_3_;
				var17 = -0.2F;

				if ((p_147801_2_ + p_147801_3_ + p_147801_4_ & 1) == 0) {
					var5.addVertexWithUV(var28, p_147801_3_ + var17,
							p_147801_4_ + 0, var13, var11);
					var5.addVertexWithUV(var20, p_147801_3_ + 0,
							p_147801_4_ + 0, var13, var15);
					var5.addVertexWithUV(var20, p_147801_3_ + 0,
							p_147801_4_ + 1, var9, var15);
					var5.addVertexWithUV(var28, p_147801_3_ + var17,
							p_147801_4_ + 1, var9, var11);
					var9 = var7.getMinU();
					var11 = var7.getMinV();
					var13 = var7.getMaxU();
					var15 = var7.getMaxV();
					var5.addVertexWithUV(var30, p_147801_3_ + var17,
							p_147801_4_ + 1, var13, var11);
					var5.addVertexWithUV(var22, p_147801_3_ + 0,
							p_147801_4_ + 1, var13, var15);
					var5.addVertexWithUV(var22, p_147801_3_ + 0,
							p_147801_4_ + 0, var9, var15);
					var5.addVertexWithUV(var30, p_147801_3_ + var17,
							p_147801_4_ + 0, var9, var11);
				} else {
					var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17,
							var34, var13, var11);
					var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0,
							var26, var13, var15);
					var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0,
							var26, var9, var15);
					var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17,
							var34, var9, var11);
					var9 = var7.getMinU();
					var11 = var7.getMinV();
					var13 = var7.getMaxU();
					var15 = var7.getMaxV();
					var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17,
							var32, var13, var11);
					var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0,
							var24, var13, var15);
					var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0,
							var24, var9, var15);
					var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17,
							var32, var9, var11);
				}
			}
		} else {
			double var18 = p_147801_2_ + 0.5D + 0.2D;
			var20 = p_147801_2_ + 0.5D - 0.2D;
			var22 = p_147801_4_ + 0.5D + 0.2D;
			var24 = p_147801_4_ + 0.5D - 0.2D;
			var26 = p_147801_2_ + 0.5D - 0.3D;
			var28 = p_147801_2_ + 0.5D + 0.3D;
			var30 = p_147801_4_ + 0.5D - 0.3D;
			var32 = p_147801_4_ + 0.5D + 0.3D;
			var5.addVertexWithUV(var26, p_147801_3_ + var17, p_147801_4_ + 1,
					var13, var11);
			var5.addVertexWithUV(var18, p_147801_3_ + 0, p_147801_4_ + 1,
					var13, var15);
			var5.addVertexWithUV(var18, p_147801_3_ + 0, p_147801_4_ + 0, var9,
					var15);
			var5.addVertexWithUV(var26, p_147801_3_ + var17, p_147801_4_ + 0,
					var9, var11);
			var5.addVertexWithUV(var28, p_147801_3_ + var17, p_147801_4_ + 0,
					var13, var11);
			var5.addVertexWithUV(var20, p_147801_3_ + 0, p_147801_4_ + 0,
					var13, var15);
			var5.addVertexWithUV(var20, p_147801_3_ + 0, p_147801_4_ + 1, var9,
					var15);
			var5.addVertexWithUV(var28, p_147801_3_ + var17, p_147801_4_ + 1,
					var9, var11);
			var9 = var7.getMinU();
			var11 = var7.getMinV();
			var13 = var7.getMaxU();
			var15 = var7.getMaxV();
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17, var32,
					var13, var11);
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, var24,
					var13, var15);
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, var24, var9,
					var15);
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17, var32,
					var9, var11);
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17, var30,
					var13, var11);
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, var22,
					var13, var15);
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, var22, var9,
					var15);
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17, var30,
					var9, var11);
			var18 = p_147801_2_ + 0.5D - 0.5D;
			var20 = p_147801_2_ + 0.5D + 0.5D;
			var22 = p_147801_4_ + 0.5D - 0.5D;
			var24 = p_147801_4_ + 0.5D + 0.5D;
			var26 = p_147801_2_ + 0.5D - 0.4D;
			var28 = p_147801_2_ + 0.5D + 0.4D;
			var30 = p_147801_4_ + 0.5D - 0.4D;
			var32 = p_147801_4_ + 0.5D + 0.4D;
			var5.addVertexWithUV(var26, p_147801_3_ + var17, p_147801_4_ + 0,
					var9, var11);
			var5.addVertexWithUV(var18, p_147801_3_ + 0, p_147801_4_ + 0, var9,
					var15);
			var5.addVertexWithUV(var18, p_147801_3_ + 0, p_147801_4_ + 1,
					var13, var15);
			var5.addVertexWithUV(var26, p_147801_3_ + var17, p_147801_4_ + 1,
					var13, var11);
			var5.addVertexWithUV(var28, p_147801_3_ + var17, p_147801_4_ + 1,
					var9, var11);
			var5.addVertexWithUV(var20, p_147801_3_ + 0, p_147801_4_ + 1, var9,
					var15);
			var5.addVertexWithUV(var20, p_147801_3_ + 0, p_147801_4_ + 0,
					var13, var15);
			var5.addVertexWithUV(var28, p_147801_3_ + var17, p_147801_4_ + 0,
					var13, var11);
			var9 = var6.getMinU();
			var11 = var6.getMinV();
			var13 = var6.getMaxU();
			var15 = var6.getMaxV();
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17, var32,
					var9, var11);
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, var24, var9,
					var15);
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, var24,
					var13, var15);
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17, var32,
					var13, var11);
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + var17, var30,
					var9, var11);
			var5.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, var22, var9,
					var15);
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, var22,
					var13, var15);
			var5.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + var17, var30,
					var13, var11);
		}

		return true;
	}

	/**
	 * Renders flower pot
	 */
	public boolean renderBlockFlowerpot(BlockFlowerPot p_147752_1_,
			int p_147752_2_, int p_147752_3_, int p_147752_4_) {
		renderStandardBlock(p_147752_1_, p_147752_2_, p_147752_3_, p_147752_4_);
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147752_1_.getBlockBrightness(blockAccess,
				p_147752_2_, p_147752_3_, p_147752_4_));
		int var6 = p_147752_1_.colorMultiplier(blockAccess, p_147752_2_,
				p_147752_3_, p_147752_4_);
		final IIcon var7 = getBlockIconFromSide(p_147752_1_, 0);
		float var8 = (var6 >> 16 & 255) / 255.0F;
		float var9 = (var6 >> 8 & 255) / 255.0F;
		float var10 = (var6 & 255) / 255.0F;
		float var11;

		if (EntityRenderer.anaglyphEnable) {
			var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			final float var21 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			final float var22 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = var11;
			var9 = var21;
			var10 = var22;
		}

		var5.setColorOpaque_F(var8, var9, var10);
		var11 = 0.1865F;
		renderFaceXPos(p_147752_1_, p_147752_2_ - 0.5F + var11, p_147752_3_,
				p_147752_4_, var7);
		renderFaceXNeg(p_147752_1_, p_147752_2_ + 0.5F - var11, p_147752_3_,
				p_147752_4_, var7);
		renderFaceZPos(p_147752_1_, p_147752_2_, p_147752_3_, p_147752_4_
				- 0.5F + var11, var7);
		renderFaceZNeg(p_147752_1_, p_147752_2_, p_147752_3_, p_147752_4_
				+ 0.5F - var11, var7);
		renderFaceYPos(p_147752_1_, p_147752_2_, p_147752_3_ - 0.5F + var11
				+ 0.1875F, p_147752_4_, this.getBlockIcon(Blocks.dirt));
		final TileEntity var211 = blockAccess.getTileEntity(p_147752_2_,
				p_147752_3_, p_147752_4_);

		if (var211 != null && var211 instanceof TileEntityFlowerPot) {
			final Item var221 = ((TileEntityFlowerPot) var211).func_145965_a();
			final int var14 = ((TileEntityFlowerPot) var211).func_145966_b();

			if (var221 instanceof ItemBlock) {
				final Block var15 = Block.getBlockFromItem(var221);
				final int var16 = var15.getRenderType();
				final float var17 = 0.0F;
				final float var18 = 4.0F;
				final float var19 = 0.0F;
				var5.addTranslation(var17 / 16.0F, var18 / 16.0F, var19 / 16.0F);
				var6 = var15.colorMultiplier(blockAccess, p_147752_2_,
						p_147752_3_, p_147752_4_);

				if (var6 != 16777215) {
					var8 = (var6 >> 16 & 255) / 255.0F;
					var9 = (var6 >> 8 & 255) / 255.0F;
					var10 = (var6 & 255) / 255.0F;
					var5.setColorOpaque_F(var8, var9, var10);
				}

				if (var16 == 1) {
					drawCrossedSquares(
							getBlockIconFromSideAndMetadata(var15, 0, var14),
							p_147752_2_, p_147752_3_, p_147752_4_, 0.75F);
				} else if (var16 == 13) {
					renderAllFaces = true;
					final float var20 = 0.125F;
					setRenderBounds(0.5F - var20, 0.0D, 0.5F - var20,
							0.5F + var20, 0.25D, 0.5F + var20);
					renderStandardBlock(var15, p_147752_2_, p_147752_3_,
							p_147752_4_);
					setRenderBounds(0.5F - var20, 0.25D, 0.5F - var20,
							0.5F + var20, 0.5D, 0.5F + var20);
					renderStandardBlock(var15, p_147752_2_, p_147752_3_,
							p_147752_4_);
					setRenderBounds(0.5F - var20, 0.5D, 0.5F - var20,
							0.5F + var20, 0.75D, 0.5F + var20);
					renderStandardBlock(var15, p_147752_2_, p_147752_3_,
							p_147752_4_);
					renderAllFaces = false;
					setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
				}

				var5.addTranslation(-var17 / 16.0F, -var18 / 16.0F,
						-var19 / 16.0F);
			}
		}

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147752_2_, p_147752_3_, p_147752_4_)) {
			renderSnow(p_147752_2_, p_147752_3_, p_147752_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		return true;
	}

	public boolean renderBlockFluids(Block p_147721_1_, int p_147721_2_,
			int p_147721_3_, int p_147721_4_) {
		final Tessellator var5 = Tessellator.instance;
		final int var6 = CustomColorizer.getFluidColor(p_147721_1_,
				blockAccess, p_147721_2_, p_147721_3_, p_147721_4_);
		final float var7 = (var6 >> 16 & 255) / 255.0F;
		final float var8 = (var6 >> 8 & 255) / 255.0F;
		final float var9 = (var6 & 255) / 255.0F;
		final boolean var10 = p_147721_1_.shouldSideBeRendered(blockAccess,
				p_147721_2_, p_147721_3_ + 1, p_147721_4_, 1);
		final boolean var11 = p_147721_1_.shouldSideBeRendered(blockAccess,
				p_147721_2_, p_147721_3_ - 1, p_147721_4_, 0);
		final boolean[] var12 = new boolean[] {
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_,
						p_147721_3_, p_147721_4_ - 1, 2),
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_,
						p_147721_3_, p_147721_4_ + 1, 3),
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_ - 1,
						p_147721_3_, p_147721_4_, 4),
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_ + 1,
						p_147721_3_, p_147721_4_, 5) };

		if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2]
				&& !var12[3])
			return false;
		else {
			boolean var13 = false;
			final float var14 = 0.5F;
			final float var15 = 1.0F;
			final float var16 = 0.8F;
			final float var17 = 0.6F;
			final double var18 = 0.0D;
			final double var20 = 1.0D;
			final Material var22 = p_147721_1_.getMaterial();
			final int var23 = blockAccess.getBlockMetadata(p_147721_2_,
					p_147721_3_, p_147721_4_);
			double var24 = getFluidHeight(p_147721_2_, p_147721_3_,
					p_147721_4_, var22);
			double var26 = getFluidHeight(p_147721_2_, p_147721_3_,
					p_147721_4_ + 1, var22);
			double var28 = getFluidHeight(p_147721_2_ + 1, p_147721_3_,
					p_147721_4_ + 1, var22);
			double var30 = getFluidHeight(p_147721_2_ + 1, p_147721_3_,
					p_147721_4_, var22);
			final double var32 = 0.0010000000474974513D;
			float var52;
			float var53;
			float var54;
			double var39;
			double var41;
			double var43;
			double var45;
			double var47;
			double var49;

			if (renderAllFaces || var10) {
				var13 = true;
				IIcon var57 = getBlockIconFromSideAndMetadata(p_147721_1_, 1,
						var23);
				final float var58 = (float) BlockLiquid.func_149802_a(
						blockAccess, p_147721_2_, p_147721_3_, p_147721_4_,
						var22);

				if (var58 > -999.0F) {
					var57 = getBlockIconFromSideAndMetadata(p_147721_1_, 2,
							var23);
				}

				var24 -= var32;
				var26 -= var32;
				var28 -= var32;
				var30 -= var32;
				double var37;
				double var51;

				if (var58 < -999.0F) {
					var37 = var57.getInterpolatedU(0.0D);
					var45 = var57.getInterpolatedV(0.0D);
					var39 = var37;
					var47 = var57.getInterpolatedV(16.0D);
					var41 = var57.getInterpolatedU(16.0D);
					var49 = var47;
					var43 = var41;
					var51 = var45;
				} else {
					var52 = MathHelper.sin(var58) * 0.25F;
					var53 = MathHelper.cos(var58) * 0.25F;
					var54 = 8.0F;
					var37 = var57
							.getInterpolatedU(8.0F + (-var53 - var52) * 16.0F);
					var45 = var57
							.getInterpolatedV(8.0F + (-var53 + var52) * 16.0F);
					var39 = var57
							.getInterpolatedU(8.0F + (-var53 + var52) * 16.0F);
					var47 = var57
							.getInterpolatedV(8.0F + (var53 + var52) * 16.0F);
					var41 = var57
							.getInterpolatedU(8.0F + (var53 + var52) * 16.0F);
					var49 = var57
							.getInterpolatedV(8.0F + (var53 - var52) * 16.0F);
					var43 = var57
							.getInterpolatedU(8.0F + (var53 - var52) * 16.0F);
					var51 = var57
							.getInterpolatedV(8.0F + (-var53 - var52) * 16.0F);
				}

				var5.setBrightness(p_147721_1_.getBlockBrightness(blockAccess,
						p_147721_2_, p_147721_3_, p_147721_4_));
				var5.setColorOpaque_F(var15 * var7, var15 * var8, var15 * var9);
				final double var56 = 3.90625E-5D;
				var5.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + var24,
						p_147721_4_ + 0, var37 + var56, var45 + var56);
				var5.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + var26,
						p_147721_4_ + 1, var39 + var56, var47 - var56);
				var5.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + var28,
						p_147721_4_ + 1, var41 - var56, var49 - var56);
				var5.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + var30,
						p_147721_4_ + 0, var43 - var56, var51 + var56);
				var5.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + var24,
						p_147721_4_ + 0, var37 + var56, var45 + var56);
				var5.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + var30,
						p_147721_4_ + 0, var43 - var56, var51 + var56);
				var5.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + var28,
						p_147721_4_ + 1, var41 - var56, var49 - var56);
				var5.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + var26,
						p_147721_4_ + 1, var39 + var56, var47 - var56);
			}

			if (renderAllFaces || var11) {
				var5.setBrightness(p_147721_1_.getBlockBrightness(blockAccess,
						p_147721_2_, p_147721_3_ - 1, p_147721_4_));
				var5.setColorOpaque_F(var14 * var7, var14 * var8, var14 * var9);
				renderFaceYNeg(p_147721_1_, p_147721_2_, p_147721_3_ + var32,
						p_147721_4_, getBlockIconFromSide(p_147721_1_, 0));
				var13 = true;
			}

			for (int var571 = 0; var571 < 4; ++var571) {
				int var581 = p_147721_2_;
				int var591 = p_147721_4_;

				if (var571 == 0) {
					var591 = p_147721_4_ - 1;
				}

				if (var571 == 1) {
					++var591;
				}

				if (var571 == 2) {
					var581 = p_147721_2_ - 1;
				}

				if (var571 == 3) {
					++var581;
				}

				final IIcon var59 = getBlockIconFromSideAndMetadata(
						p_147721_1_, var571 + 2, var23);

				if (renderAllFaces || var12[var571]) {
					if (var571 == 0) {
						var39 = var24;
						var41 = var30;
						var43 = p_147721_2_;
						var47 = p_147721_2_ + 1;
						var45 = p_147721_4_ + var32;
						var49 = p_147721_4_ + var32;
					} else if (var571 == 1) {
						var39 = var28;
						var41 = var26;
						var43 = p_147721_2_ + 1;
						var47 = p_147721_2_;
						var45 = p_147721_4_ + 1 - var32;
						var49 = p_147721_4_ + 1 - var32;
					} else if (var571 == 2) {
						var39 = var26;
						var41 = var24;
						var43 = p_147721_2_ + var32;
						var47 = p_147721_2_ + var32;
						var45 = p_147721_4_ + 1;
						var49 = p_147721_4_;
					} else {
						var39 = var30;
						var41 = var28;
						var43 = p_147721_2_ + 1 - var32;
						var47 = p_147721_2_ + 1 - var32;
						var45 = p_147721_4_;
						var49 = p_147721_4_ + 1;
					}

					var13 = true;
					final float var60 = var59.getInterpolatedU(0.0D);
					var52 = var59.getInterpolatedU(8.0D);
					var53 = var59
							.getInterpolatedV((1.0D - var39) * 16.0D * 0.5D);
					var54 = var59
							.getInterpolatedV((1.0D - var41) * 16.0D * 0.5D);
					final float var55 = var59.getInterpolatedV(8.0D);
					var5.setBrightness(p_147721_1_.getBlockBrightness(
							blockAccess, var581, p_147721_3_, var591));
					float var61 = 1.0F;
					var61 *= var571 < 2 ? var16 : var17;
					var5.setColorOpaque_F(var15 * var61 * var7, var15 * var61
							* var8, var15 * var61 * var9);
					var5.addVertexWithUV(var43, p_147721_3_ + var39, var45,
							var60, var53);
					var5.addVertexWithUV(var47, p_147721_3_ + var41, var49,
							var52, var54);
					var5.addVertexWithUV(var47, p_147721_3_ + 0, var49, var52,
							var55);
					var5.addVertexWithUV(var43, p_147721_3_ + 0, var45, var60,
							var55);
					var5.addVertexWithUV(var43, p_147721_3_ + 0, var45, var60,
							var55);
					var5.addVertexWithUV(var47, p_147721_3_ + 0, var49, var52,
							var55);
					var5.addVertexWithUV(var47, p_147721_3_ + var41, var49,
							var52, var54);
					var5.addVertexWithUV(var43, p_147721_3_ + var39, var45,
							var60, var53);
				}
			}

			renderMinY = var18;
			renderMaxY = var20;
			return var13;
		}
	}

	public boolean renderBlockHopper(BlockHopper p_147803_1_, int p_147803_2_,
			int p_147803_3_, int p_147803_4_) {
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147803_1_.getBlockBrightness(blockAccess,
				p_147803_2_, p_147803_3_, p_147803_4_));
		final int var6 = p_147803_1_.colorMultiplier(blockAccess, p_147803_2_,
				p_147803_3_, p_147803_4_);
		float var7 = (var6 >> 16 & 255) / 255.0F;
		float var8 = (var6 >> 8 & 255) / 255.0F;
		float var9 = (var6 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var10 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
			final float var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
			final float var12 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
			var7 = var10;
			var8 = var11;
			var9 = var12;
		}

		var5.setColorOpaque_F(var7, var8, var9);
		return renderBlockHopperMetadata(p_147803_1_, p_147803_2_, p_147803_3_,
				p_147803_4_, blockAccess.getBlockMetadata(p_147803_2_,
						p_147803_3_, p_147803_4_), false);
	}

	public boolean renderBlockHopperMetadata(BlockHopper p_147799_1_,
			int p_147799_2_, int p_147799_3_, int p_147799_4_, int p_147799_5_,
			boolean p_147799_6_) {
		final Tessellator var7 = Tessellator.instance;
		final int var8 = BlockHopper.func_149918_b(p_147799_5_);
		final double var9 = 0.625D;
		setRenderBounds(0.0D, var9, 0.0D, 1.0D, 1.0D, 1.0D);

		if (p_147799_6_) {
			var7.startDrawingQuads();
			var7.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(
					p_147799_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147799_1_, 0, p_147799_5_));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(
					p_147799_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147799_1_, 1, p_147799_5_));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(
					p_147799_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147799_1_, 2, p_147799_5_));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(
					p_147799_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147799_1_, 3, p_147799_5_));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(
					p_147799_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147799_1_, 4, p_147799_5_));
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(
					p_147799_1_,
					0.0D,
					0.0D,
					0.0D,
					getBlockIconFromSideAndMetadata(p_147799_1_, 5, p_147799_5_));
			var7.draw();
		} else {
			renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_,
					p_147799_4_);
		}

		float var13;

		if (!p_147799_6_) {
			var7.setBrightness(p_147799_1_.getBlockBrightness(blockAccess,
					p_147799_2_, p_147799_3_, p_147799_4_));
			final int var24 = p_147799_1_.colorMultiplier(blockAccess,
					p_147799_2_, p_147799_3_, p_147799_4_);
			float var25 = (var24 >> 16 & 255) / 255.0F;
			var13 = (var24 >> 8 & 255) / 255.0F;
			float var26 = (var24 & 255) / 255.0F;

			if (EntityRenderer.anaglyphEnable) {
				final float var15 = (var25 * 30.0F + var13 * 59.0F + var26 * 11.0F) / 100.0F;
				final float var27 = (var25 * 30.0F + var13 * 70.0F) / 100.0F;
				final float var17 = (var25 * 30.0F + var26 * 70.0F) / 100.0F;
				var25 = var15;
				var13 = var27;
				var26 = var17;
			}

			var7.setColorOpaque_F(var25, var13, var26);
		}

		final IIcon var241 = BlockHopper.func_149916_e("hopper_outside");
		final IIcon var251 = BlockHopper.func_149916_e("hopper_inside");
		var13 = 0.125F;

		if (p_147799_6_) {
			var7.startDrawingQuads();
			var7.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(p_147799_1_, -1.0F + var13, 0.0D, 0.0D, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(p_147799_1_, 1.0F - var13, 0.0D, 0.0D, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(p_147799_1_, 0.0D, 0.0D, -1.0F + var13, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, 1.0F - var13, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(p_147799_1_, 0.0D, -1.0D + var9, 0.0D, var251);
			var7.draw();
		} else {
			renderFaceXPos(p_147799_1_, p_147799_2_ - 1.0F + var13,
					p_147799_3_, p_147799_4_, var241);
			renderFaceXNeg(p_147799_1_, p_147799_2_ + 1.0F - var13,
					p_147799_3_, p_147799_4_, var241);
			renderFaceZPos(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_
					- 1.0F + var13, var241);
			renderFaceZNeg(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_
					+ 1.0F - var13, var241);
			renderFaceYPos(p_147799_1_, p_147799_2_, p_147799_3_ - 1.0F + var9,
					p_147799_4_, var251);
		}

		setOverrideBlockTexture(var241);
		final double var261 = 0.25D;
		final double var271 = 0.25D;
		setRenderBounds(var261, var271, var261, 1.0D - var261, var9 - 0.002D,
				1.0D - var261);

		if (p_147799_6_) {
			var7.startDrawingQuads();
			var7.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(p_147799_1_, 0.0D, 0.0D, 0.0D, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(p_147799_1_, 0.0D, 0.0D, 0.0D, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(p_147799_1_, 0.0D, 0.0D, 0.0D, var241);
			var7.draw();
			var7.startDrawingQuads();
			var7.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, var241);
			var7.draw();
		} else {
			renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_,
					p_147799_4_);
		}

		if (!p_147799_6_) {
			final double var20 = 0.375D;
			final double var22 = 0.25D;
			setOverrideBlockTexture(var241);

			if (var8 == 0) {
				setRenderBounds(var20, 0.0D, var20, 1.0D - var20, 0.25D,
						1.0D - var20);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_,
						p_147799_4_);
			}

			if (var8 == 2) {
				setRenderBounds(var20, var271, 0.0D, 1.0D - var20, var271
						+ var22, var261);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_,
						p_147799_4_);
			}

			if (var8 == 3) {
				setRenderBounds(var20, var271, 1.0D - var261, 1.0D - var20,
						var271 + var22, 1.0D);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_,
						p_147799_4_);
			}

			if (var8 == 4) {
				setRenderBounds(0.0D, var271, var20, var261, var271 + var22,
						1.0D - var20);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_,
						p_147799_4_);
			}

			if (var8 == 5) {
				setRenderBounds(1.0D - var261, var271, var20, 1.0D, var271
						+ var22, 1.0D - var20);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_,
						p_147799_4_);
			}
		}

		clearOverrideBlockTexture();
		return true;
	}

	public boolean renderBlockLadder(Block p_147794_1_, int p_147794_2_,
			int p_147794_3_, int p_147794_4_) {
		final Tessellator var5 = Tessellator.instance;
		IIcon var6 = getBlockIconFromSide(p_147794_1_, 0);

		if (hasOverrideBlockTexture()) {
			var6 = overrideBlockTexture;
		}

		final int var15 = blockAccess.getBlockMetadata(p_147794_2_,
				p_147794_3_, p_147794_4_);

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			var6 = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147794_1_, p_147794_2_, p_147794_3_, p_147794_4_, var15,
					var6);
		}

		var5.setBrightness(p_147794_1_.getBlockBrightness(blockAccess,
				p_147794_2_, p_147794_3_, p_147794_4_));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final double var7 = var6.getMinU();
		final double var9 = var6.getMinV();
		final double var11 = var6.getMaxU();
		final double var13 = var6.getMaxV();
		final double var16 = 0.0D;
		final double var18 = 0.05000000074505806D;

		if (var15 == 5) {
			var5.addVertexWithUV(p_147794_2_ + var18, p_147794_3_ + 1 + var16,
					p_147794_4_ + 1 + var16, var7, var9);
			var5.addVertexWithUV(p_147794_2_ + var18, p_147794_3_ + 0 - var16,
					p_147794_4_ + 1 + var16, var7, var13);
			var5.addVertexWithUV(p_147794_2_ + var18, p_147794_3_ + 0 - var16,
					p_147794_4_ + 0 - var16, var11, var13);
			var5.addVertexWithUV(p_147794_2_ + var18, p_147794_3_ + 1 + var16,
					p_147794_4_ + 0 - var16, var11, var9);
		}

		if (var15 == 4) {
			var5.addVertexWithUV(p_147794_2_ + 1 - var18, p_147794_3_ + 0
					- var16, p_147794_4_ + 1 + var16, var11, var13);
			var5.addVertexWithUV(p_147794_2_ + 1 - var18, p_147794_3_ + 1
					+ var16, p_147794_4_ + 1 + var16, var11, var9);
			var5.addVertexWithUV(p_147794_2_ + 1 - var18, p_147794_3_ + 1
					+ var16, p_147794_4_ + 0 - var16, var7, var9);
			var5.addVertexWithUV(p_147794_2_ + 1 - var18, p_147794_3_ + 0
					- var16, p_147794_4_ + 0 - var16, var7, var13);
		}

		if (var15 == 3) {
			var5.addVertexWithUV(p_147794_2_ + 1 + var16, p_147794_3_ + 0
					- var16, p_147794_4_ + var18, var11, var13);
			var5.addVertexWithUV(p_147794_2_ + 1 + var16, p_147794_3_ + 1
					+ var16, p_147794_4_ + var18, var11, var9);
			var5.addVertexWithUV(p_147794_2_ + 0 - var16, p_147794_3_ + 1
					+ var16, p_147794_4_ + var18, var7, var9);
			var5.addVertexWithUV(p_147794_2_ + 0 - var16, p_147794_3_ + 0
					- var16, p_147794_4_ + var18, var7, var13);
		}

		if (var15 == 2) {
			var5.addVertexWithUV(p_147794_2_ + 1 + var16, p_147794_3_ + 1
					+ var16, p_147794_4_ + 1 - var18, var7, var9);
			var5.addVertexWithUV(p_147794_2_ + 1 + var16, p_147794_3_ + 0
					- var16, p_147794_4_ + 1 - var18, var7, var13);
			var5.addVertexWithUV(p_147794_2_ + 0 - var16, p_147794_3_ + 0
					- var16, p_147794_4_ + 1 - var18, var11, var13);
			var5.addVertexWithUV(p_147794_2_ + 0 - var16, p_147794_3_ + 1
					+ var16, p_147794_4_ + 1 - var18, var11, var9);
		}

		return true;
	}

	public boolean renderBlockLever(Block p_147790_1_, int p_147790_2_,
			int p_147790_3_, int p_147790_4_) {
		final int var5 = blockAccess.getBlockMetadata(p_147790_2_, p_147790_3_,
				p_147790_4_);
		final int var6 = var5 & 7;
		final boolean var7 = (var5 & 8) > 0;
		final Tessellator var8 = Tessellator.instance;
		final boolean var9 = hasOverrideBlockTexture();

		if (!var9) {
			setOverrideBlockTexture(this.getBlockIcon(Blocks.cobblestone));
		}

		final float var10 = 0.25F;
		final float var11 = 0.1875F;
		final float var12 = 0.1875F;

		if (var6 == 5) {
			setRenderBounds(0.5F - var11, 0.0D, 0.5F - var10, 0.5F + var11,
					var12, 0.5F + var10);
		} else if (var6 == 6) {
			setRenderBounds(0.5F - var10, 0.0D, 0.5F - var11, 0.5F + var10,
					var12, 0.5F + var11);
		} else if (var6 == 4) {
			setRenderBounds(0.5F - var11, 0.5F - var10, 1.0F - var12,
					0.5F + var11, 0.5F + var10, 1.0D);
		} else if (var6 == 3) {
			setRenderBounds(0.5F - var11, 0.5F - var10, 0.0D, 0.5F + var11,
					0.5F + var10, var12);
		} else if (var6 == 2) {
			setRenderBounds(1.0F - var12, 0.5F - var10, 0.5F - var11, 1.0D,
					0.5F + var10, 0.5F + var11);
		} else if (var6 == 1) {
			setRenderBounds(0.0D, 0.5F - var10, 0.5F - var11, var12,
					0.5F + var10, 0.5F + var11);
		} else if (var6 == 0) {
			setRenderBounds(0.5F - var10, 1.0F - var12, 0.5F - var11,
					0.5F + var10, 1.0D, 0.5F + var11);
		} else if (var6 == 7) {
			setRenderBounds(0.5F - var11, 1.0F - var12, 0.5F - var10,
					0.5F + var11, 1.0D, 0.5F + var10);
		}

		renderStandardBlock(p_147790_1_, p_147790_2_, p_147790_3_, p_147790_4_);

		if (!var9) {
			clearOverrideBlockTexture();
		}

		var8.setBrightness(p_147790_1_.getBlockBrightness(blockAccess,
				p_147790_2_, p_147790_3_, p_147790_4_));
		var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		IIcon var13 = getBlockIconFromSide(p_147790_1_, 0);

		if (hasOverrideBlockTexture()) {
			var13 = overrideBlockTexture;
		}

		double var14 = var13.getMinU();
		double var16 = var13.getMinV();
		double var18 = var13.getMaxU();
		double var20 = var13.getMaxV();
		final Vec3[] var22 = new Vec3[8];
		final float var23 = 0.0625F;
		final float var24 = 0.0625F;
		final float var25 = 0.625F;
		var22[0] = Vec3.createVectorHelper(-var23, 0.0D, -var24);
		var22[1] = Vec3.createVectorHelper(var23, 0.0D, -var24);
		var22[2] = Vec3.createVectorHelper(var23, 0.0D, var24);
		var22[3] = Vec3.createVectorHelper(-var23, 0.0D, var24);
		var22[4] = Vec3.createVectorHelper(-var23, var25, -var24);
		var22[5] = Vec3.createVectorHelper(var23, var25, -var24);
		var22[6] = Vec3.createVectorHelper(var23, var25, var24);
		var22[7] = Vec3.createVectorHelper(-var23, var25, var24);

		for (int var31 = 0; var31 < 8; ++var31) {
			if (var7) {
				var22[var31].zCoord -= 0.0625D;
				var22[var31].rotateAroundX((float) Math.PI * 2F / 9F);
			} else {
				var22[var31].zCoord += 0.0625D;
				var22[var31].rotateAroundX(-((float) Math.PI * 2F / 9F));
			}

			if (var6 == 0 || var6 == 7) {
				var22[var31].rotateAroundZ((float) Math.PI);
			}

			if (var6 == 6 || var6 == 0) {
				var22[var31].rotateAroundY((float) Math.PI / 2F);
			}

			if (var6 > 0 && var6 < 5) {
				var22[var31].yCoord -= 0.375D;
				var22[var31].rotateAroundX((float) Math.PI / 2F);

				if (var6 == 4) {
					var22[var31].rotateAroundY(0.0F);
				}

				if (var6 == 3) {
					var22[var31].rotateAroundY((float) Math.PI);
				}

				if (var6 == 2) {
					var22[var31].rotateAroundY((float) Math.PI / 2F);
				}

				if (var6 == 1) {
					var22[var31].rotateAroundY(-((float) Math.PI / 2F));
				}

				var22[var31].xCoord += p_147790_2_ + 0.5D;
				var22[var31].yCoord += p_147790_3_ + 0.5F;
				var22[var31].zCoord += p_147790_4_ + 0.5D;
			} else if (var6 != 0 && var6 != 7) {
				var22[var31].xCoord += p_147790_2_ + 0.5D;
				var22[var31].yCoord += p_147790_3_ + 0.125F;
				var22[var31].zCoord += p_147790_4_ + 0.5D;
			} else {
				var22[var31].xCoord += p_147790_2_ + 0.5D;
				var22[var31].yCoord += p_147790_3_ + 0.875F;
				var22[var31].zCoord += p_147790_4_ + 0.5D;
			}
		}

		Vec3 var311 = null;
		Vec3 var27 = null;
		Vec3 var28 = null;
		Vec3 var29 = null;

		for (int var30 = 0; var30 < 6; ++var30) {
			if (var30 == 0) {
				var14 = var13.getInterpolatedU(7.0D);
				var16 = var13.getInterpolatedV(6.0D);
				var18 = var13.getInterpolatedU(9.0D);
				var20 = var13.getInterpolatedV(8.0D);
			} else if (var30 == 2) {
				var14 = var13.getInterpolatedU(7.0D);
				var16 = var13.getInterpolatedV(6.0D);
				var18 = var13.getInterpolatedU(9.0D);
				var20 = var13.getMaxV();
			}

			if (var30 == 0) {
				var311 = var22[0];
				var27 = var22[1];
				var28 = var22[2];
				var29 = var22[3];
			} else if (var30 == 1) {
				var311 = var22[7];
				var27 = var22[6];
				var28 = var22[5];
				var29 = var22[4];
			} else if (var30 == 2) {
				var311 = var22[1];
				var27 = var22[0];
				var28 = var22[4];
				var29 = var22[5];
			} else if (var30 == 3) {
				var311 = var22[2];
				var27 = var22[1];
				var28 = var22[5];
				var29 = var22[6];
			} else if (var30 == 4) {
				var311 = var22[3];
				var27 = var22[2];
				var28 = var22[6];
				var29 = var22[7];
			} else if (var30 == 5) {
				var311 = var22[0];
				var27 = var22[3];
				var28 = var22[7];
				var29 = var22[4];
			}

			var8.addVertexWithUV(var311.xCoord, var311.yCoord, var311.zCoord,
					var14, var20);
			var8.addVertexWithUV(var27.xCoord, var27.yCoord, var27.zCoord,
					var18, var20);
			var8.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord,
					var18, var16);
			var8.addVertexWithUV(var29.xCoord, var29.yCoord, var29.zCoord,
					var14, var16);
		}

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147790_2_, p_147790_3_, p_147790_4_)) {
			renderSnow(p_147790_2_, p_147790_3_, p_147790_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		return true;
	}

	public boolean renderBlockLilyPad(Block p_147783_1_, int p_147783_2_,
			int p_147783_3_, int p_147783_4_) {
		final Tessellator var5 = Tessellator.instance;
		IIcon var6 = getBlockIconFromSide(p_147783_1_, 1);

		if (hasOverrideBlockTexture()) {
			var6 = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			var6 = ConnectedTextures
					.getConnectedTexture(blockAccess, p_147783_1_, p_147783_2_,
							p_147783_3_, p_147783_4_, 1, var6);
		}

		final float var7 = 0.015625F;
		final double var8 = var6.getMinU();
		final double var10 = var6.getMinV();
		final double var12 = var6.getMaxU();
		final double var14 = var6.getMaxV();
		long var16 = p_147783_2_ * 3129871 ^ p_147783_4_ * 116129781L
				^ p_147783_3_;
		var16 = var16 * var16 * 42317861L + var16 * 11L;
		final int var18 = (int) (var16 >> 16 & 3L);
		var5.setBrightness(p_147783_1_.getBlockBrightness(blockAccess,
				p_147783_2_, p_147783_3_, p_147783_4_));
		final float var19 = p_147783_2_ + 0.5F;
		final float var20 = p_147783_4_ + 0.5F;
		final float var21 = (var18 & 1) * 0.5F * (1 - var18 / 2 % 2 * 2);
		final float var22 = (var18 + 1 & 1) * 0.5F
				* (1 - (var18 + 1) / 2 % 2 * 2);
		final int col = CustomColorizer.getLilypadColor();
		var5.setColorOpaque_I(col);
		var5.addVertexWithUV(var19 + var21 - var22, p_147783_3_ + var7, var20
				+ var21 + var22, var8, var10);
		var5.addVertexWithUV(var19 + var21 + var22, p_147783_3_ + var7, var20
				- var21 + var22, var12, var10);
		var5.addVertexWithUV(var19 - var21 + var22, p_147783_3_ + var7, var20
				- var21 - var22, var12, var14);
		var5.addVertexWithUV(var19 - var21 - var22, p_147783_3_ + var7, var20
				+ var21 - var22, var8, var14);
		var5.setColorOpaque_I((col & 16711422) >> 1);
		var5.addVertexWithUV(var19 - var21 - var22, p_147783_3_ + var7, var20
				+ var21 - var22, var8, var14);
		var5.addVertexWithUV(var19 - var21 + var22, p_147783_3_ + var7, var20
				- var21 - var22, var12, var14);
		var5.addVertexWithUV(var19 + var21 + var22, p_147783_3_ + var7, var20
				- var21 + var22, var12, var10);
		var5.addVertexWithUV(var19 + var21 - var22, p_147783_3_ + var7, var20
				+ var21 + var22, var8, var10);
		return true;
	}

	public boolean renderBlockLog(Block p_147742_1_, int p_147742_2_,
			int p_147742_3_, int p_147742_4_) {
		final int var5 = blockAccess.getBlockMetadata(p_147742_2_, p_147742_3_,
				p_147742_4_);
		final int var6 = var5 & 12;

		if (var6 == 4) {
			uvRotateEast = 2;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 2;
		} else if (var6 == 8) {
			uvRotateSouth = 2;
			uvRotateNorth = 1;
			uvRotateTop = 3;
			uvRotateBottom = 3;
		}

		final boolean var7 = renderStandardBlock(p_147742_1_, p_147742_2_,
				p_147742_3_, p_147742_4_);
		uvRotateSouth = 0;
		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		return var7;
	}

	public boolean renderBlockMinecartTrack(BlockRailBase p_147766_1_,
			int p_147766_2_, int p_147766_3_, int p_147766_4_) {
		final Tessellator var5 = Tessellator.instance;
		int var6 = blockAccess.getBlockMetadata(p_147766_2_, p_147766_3_,
				p_147766_4_);
		IIcon var7 = getBlockIconFromSideAndMetadata(p_147766_1_, 0, var6);

		if (hasOverrideBlockTexture()) {
			var7 = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			var7 = ConnectedTextures
					.getConnectedTexture(blockAccess, p_147766_1_, p_147766_2_,
							p_147766_3_, p_147766_4_, 1, var7);
		}

		if (p_147766_1_.func_150050_e()) {
			var6 &= 7;
		}

		var5.setBrightness(p_147766_1_.getBlockBrightness(blockAccess,
				p_147766_2_, p_147766_3_, p_147766_4_));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final double var8 = var7.getMinU();
		final double var10 = var7.getMinV();
		final double var12 = var7.getMaxU();
		final double var14 = var7.getMaxV();
		final double var16 = 0.0625D;
		double var18 = p_147766_2_ + 1;
		double var20 = p_147766_2_ + 1;
		double var22 = p_147766_2_ + 0;
		double var24 = p_147766_2_ + 0;
		double var26 = p_147766_4_ + 0;
		double var28 = p_147766_4_ + 1;
		double var30 = p_147766_4_ + 1;
		double var32 = p_147766_4_ + 0;
		double var34 = p_147766_3_ + var16;
		double var36 = p_147766_3_ + var16;
		double var38 = p_147766_3_ + var16;
		double var40 = p_147766_3_ + var16;

		if (var6 != 1 && var6 != 2 && var6 != 3 && var6 != 7) {
			if (var6 == 8) {
				var18 = var20 = p_147766_2_ + 0;
				var22 = var24 = p_147766_2_ + 1;
				var26 = var32 = p_147766_4_ + 1;
				var28 = var30 = p_147766_4_ + 0;
			} else if (var6 == 9) {
				var18 = var24 = p_147766_2_ + 0;
				var20 = var22 = p_147766_2_ + 1;
				var26 = var28 = p_147766_4_ + 0;
				var30 = var32 = p_147766_4_ + 1;
			}
		} else {
			var18 = var24 = p_147766_2_ + 1;
			var20 = var22 = p_147766_2_ + 0;
			var26 = var28 = p_147766_4_ + 1;
			var30 = var32 = p_147766_4_ + 0;
		}

		if (var6 != 2 && var6 != 4) {
			if (var6 == 3 || var6 == 5) {
				++var36;
				++var38;
			}
		} else {
			++var34;
			++var40;
		}

		var5.addVertexWithUV(var18, var34, var26, var12, var10);
		var5.addVertexWithUV(var20, var36, var28, var12, var14);
		var5.addVertexWithUV(var22, var38, var30, var8, var14);
		var5.addVertexWithUV(var24, var40, var32, var8, var10);
		var5.addVertexWithUV(var24, var40, var32, var8, var10);
		var5.addVertexWithUV(var22, var38, var30, var8, var14);
		var5.addVertexWithUV(var20, var36, var28, var12, var14);
		var5.addVertexWithUV(var18, var34, var26, var12, var10);

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147766_2_, p_147766_3_, p_147766_4_)) {
			renderSnow(p_147766_2_, p_147766_3_, p_147766_4_, 0.05D);
		}

		return true;
	}

	public boolean renderBlockPane(BlockPane p_147767_1_, int p_147767_2_,
			int p_147767_3_, int p_147767_4_) {
		final int var5 = blockAccess.getHeight();
		final Tessellator var6 = Tessellator.instance;
		var6.setBrightness(p_147767_1_.getBlockBrightness(blockAccess,
				p_147767_2_, p_147767_3_, p_147767_4_));
		final int var7 = p_147767_1_.colorMultiplier(blockAccess, p_147767_2_,
				p_147767_3_, p_147767_4_);
		float var8 = (var7 >> 16 & 255) / 255.0F;
		float var9 = (var7 >> 8 & 255) / 255.0F;
		float var10 = (var7 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var63 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			final float var64 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			final float kr = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = var63;
			var9 = var64;
			var10 = kr;
		}

		var6.setColorOpaque_F(var8, var9, var10);
		IIcon var631;
		IIcon var641;

		if (hasOverrideBlockTexture()) {
			var631 = overrideBlockTexture;
			var641 = overrideBlockTexture;
		} else {
			final int kr1 = blockAccess.getBlockMetadata(p_147767_2_,
					p_147767_3_, p_147767_4_);
			var631 = getBlockIconFromSideAndMetadata(p_147767_1_, 0, kr1);
			var641 = p_147767_1_.func_150097_e();
		}

		IIcon kr2 = var631;
		IIcon kz = var631;
		IIcon kzr = var631;

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			var631 = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147767_1_, p_147767_2_, p_147767_3_, p_147767_4_, 2,
					var631);
			kr2 = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147767_1_, p_147767_2_, p_147767_3_, p_147767_4_, 3, kr2);
			kz = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147767_1_, p_147767_2_, p_147767_3_, p_147767_4_, 4, kz);
			kzr = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147767_1_, p_147767_2_, p_147767_3_, p_147767_4_, 5, kzr);
		}

		final double var65 = var631.getMinU();
		final double var15 = var631.getInterpolatedU(8.0D);
		final double var17 = var631.getMaxU();
		final double var19 = var631.getMinV();
		final double var21 = var631.getMaxV();
		final double dr = kr2.getMinU();
		final double d1r = kr2.getInterpolatedU(8.0D);
		final double d2r = kr2.getMaxU();
		final double d3r = kr2.getMinV();
		final double d4r = kr2.getMaxV();
		final double dz = kz.getMinU();
		final double d1z = kz.getInterpolatedU(8.0D);
		final double d2z = kz.getMaxU();
		final double d3z = kz.getMinV();
		final double d4z = kz.getMaxV();
		final double dzr = kzr.getMinU();
		final double d1zr = kzr.getInterpolatedU(8.0D);
		final double d2zr = kzr.getMaxU();
		final double d3zr = kzr.getMinV();
		final double d4zr = kzr.getMaxV();
		final double var23 = var641.getInterpolatedU(7.0D);
		final double var25 = var641.getInterpolatedU(9.0D);
		final double var27 = var641.getMinV();
		final double var29 = var641.getInterpolatedV(8.0D);
		final double var31 = var641.getMaxV();
		final double var33 = p_147767_2_;
		final double var35 = p_147767_2_ + 0.5D;
		final double var37 = p_147767_2_ + 1;
		final double var39 = p_147767_4_;
		final double var41 = p_147767_4_ + 0.5D;
		final double var43 = p_147767_4_ + 1;
		final double var45 = p_147767_2_ + 0.5D - 0.0625D;
		final double var47 = p_147767_2_ + 0.5D + 0.0625D;
		final double var49 = p_147767_4_ + 0.5D - 0.0625D;
		final double var51 = p_147767_4_ + 0.5D + 0.0625D;
		final boolean var53 = p_147767_1_.func_150098_a(blockAccess.getBlock(
				p_147767_2_, p_147767_3_, p_147767_4_ - 1));
		final boolean var54 = p_147767_1_.func_150098_a(blockAccess.getBlock(
				p_147767_2_, p_147767_3_, p_147767_4_ + 1));
		final boolean var55 = p_147767_1_.func_150098_a(blockAccess.getBlock(
				p_147767_2_ - 1, p_147767_3_, p_147767_4_));
		final boolean var56 = p_147767_1_.func_150098_a(blockAccess.getBlock(
				p_147767_2_ + 1, p_147767_3_, p_147767_4_));
		final boolean var57 = p_147767_1_.shouldSideBeRendered(blockAccess,
				p_147767_2_, p_147767_3_ + 1, p_147767_4_, 1);
		final boolean var58 = p_147767_1_.shouldSideBeRendered(blockAccess,
				p_147767_2_, p_147767_3_ - 1, p_147767_4_, 0);
		if ((!var55 || !var56) && (var55 || var56 || var53 || var54)) {
			if (var55 && !var56) {
				var6.addVertexWithUV(var33, p_147767_3_ + 1, var41, var65,
						var19);
				var6.addVertexWithUV(var33, p_147767_3_ + 0, var41, var65,
						var21);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, var15,
						var21);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, var15,
						var19);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, d1r, d3r);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, d1r, d4r);
				var6.addVertexWithUV(var33, p_147767_3_ + 0, var41, d2r, d4r);
				var6.addVertexWithUV(var33, p_147767_3_ + 1, var41, d2r, d3r);

				if (!var54 && !var53) {
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var51, var23,
							var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var51, var23,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var49, var25,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var49, var25,
							var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var49, var23,
							var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var49, var23,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var51, var25,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var51, var25,
							var27);
				}

				if (var57
						|| p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_ - 1,
								p_147767_3_ + 1, p_147767_4_)) {
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
				}

				if (var58
						|| p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_ - 1,
								p_147767_3_ - 1, p_147767_4_)) {
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var29);
				}
			} else if (!var55 && var56) {
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, var15,
						var19);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, var15,
						var21);
				var6.addVertexWithUV(var37, p_147767_3_ + 0, var41, var17,
						var21);
				var6.addVertexWithUV(var37, p_147767_3_ + 1, var41, var17,
						var19);
				var6.addVertexWithUV(var37, p_147767_3_ + 1, var41, dr, d3r);
				var6.addVertexWithUV(var37, p_147767_3_ + 0, var41, dr, d4r);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, d1r, d4r);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, d1r, d3r);

				if (!var54 && !var53) {
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var49, var23,
							var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var49, var23,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var51, var25,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var51, var25,
							var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var51, var23,
							var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var51, var23,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 0, var49, var25,
							var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1, var49, var25,
							var27);
				}

				if (var57
						|| p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_ + 1,
								p_147767_3_ + 1, p_147767_4_)) {
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var27);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var49,
							var23, var27);
				}

				if (var58
						|| p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_ + 1,
								p_147767_3_ - 1, p_147767_4_)) {
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var27);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var49,
							var23, var27);
				}
			}
		} else {
			var6.addVertexWithUV(var33, p_147767_3_ + 1, var41, var65, var19);
			var6.addVertexWithUV(var33, p_147767_3_ + 0, var41, var65, var21);
			var6.addVertexWithUV(var37, p_147767_3_ + 0, var41, var17, var21);
			var6.addVertexWithUV(var37, p_147767_3_ + 1, var41, var17, var19);
			var6.addVertexWithUV(var37, p_147767_3_ + 1, var41, dr, d3r);
			var6.addVertexWithUV(var37, p_147767_3_ + 0, var41, dr, d4r);
			var6.addVertexWithUV(var33, p_147767_3_ + 0, var41, d2r, d4r);
			var6.addVertexWithUV(var33, p_147767_3_ + 1, var41, d2r, d3r);

			if (var57) {
				var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var51,
						var25, var31);
				var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var51,
						var25, var27);
				var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var49,
						var23, var27);
				var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var49,
						var23, var31);
				var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var51,
						var25, var31);
				var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var51,
						var25, var27);
				var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var49,
						var23, var27);
				var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var49,
						var23, var31);
			} else {
				if (p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_ - 1,
								p_147767_3_ + 1, p_147767_4_)) {
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var33, p_147767_3_ + 1 + 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
				}

				if (p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_ + 1,
								p_147767_3_ + 1, p_147767_4_)) {
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var27);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ + 1 + 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var37, p_147767_3_ + 1 + 0.01D, var49,
							var23, var27);
				}
			}

			if (var58) {
				var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var51, var25,
						var31);
				var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var51, var25,
						var27);
				var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var49, var23,
						var27);
				var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var49, var23,
						var31);
				var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var51, var25,
						var31);
				var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var51, var25,
						var27);
				var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var49, var23,
						var27);
				var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var49, var23,
						var31);
			} else {
				if (p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_ - 1,
								p_147767_3_ - 1, p_147767_4_)) {
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var51,
							var25, var31);
					var6.addVertexWithUV(var33, p_147767_3_ - 0.01D, var49,
							var23, var31);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var29);
				}

				if (p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_ + 1,
								p_147767_3_ - 1, p_147767_4_)) {
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var27);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var51,
							var25, var27);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var51,
							var25, var29);
					var6.addVertexWithUV(var35, p_147767_3_ - 0.01D, var49,
							var23, var29);
					var6.addVertexWithUV(var37, p_147767_3_ - 0.01D, var49,
							var23, var27);
				}
			}
		}

		if ((!var53 || !var54) && (var55 || var56 || var53 || var54)) {
			if (var53 && !var54) {
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var39, dz, d3z);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var39, dz, d4z);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, d1z, d4z);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, d1z, d3z);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, d1zr, d3zr);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, d1zr, d4zr);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var39, d2zr, d4zr);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var39, d2zr, d3zr);

				if (!var56 && !var55) {
					var6.addVertexWithUV(var45, p_147767_3_ + 1, var41, var23,
							var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 0, var41, var23,
							var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 0, var41, var25,
							var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1, var41, var25,
							var27);
					var6.addVertexWithUV(var47, p_147767_3_ + 1, var41, var23,
							var27);
					var6.addVertexWithUV(var47, p_147767_3_ + 0, var41, var23,
							var31);
					var6.addVertexWithUV(var45, p_147767_3_ + 0, var41, var25,
							var31);
					var6.addVertexWithUV(var45, p_147767_3_ + 1, var41, var25,
							var27);
				}

				if (var57
						|| p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1,
								p_147767_4_ - 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var39, var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var39, var23, var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var39, var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var39, var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var23, var27);
				}

				if (var58
						|| p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1,
								p_147767_4_ - 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var39,
							var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var39,
							var23, var27);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var39,
							var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var39,
							var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var23, var27);
				}
			} else if (!var53 && var54) {
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, d1z, d3z);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, d1z, d4z);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var43, d2z, d4z);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var43, d2z, d3z);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var43, dzr, d3zr);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var43, dzr, d4zr);
				var6.addVertexWithUV(var35, p_147767_3_ + 0, var41, d1zr, d4zr);
				var6.addVertexWithUV(var35, p_147767_3_ + 1, var41, d1zr, d3zr);

				if (!var56 && !var55) {
					var6.addVertexWithUV(var47, p_147767_3_ + 1, var41, var23,
							var27);
					var6.addVertexWithUV(var47, p_147767_3_ + 0, var41, var23,
							var31);
					var6.addVertexWithUV(var45, p_147767_3_ + 0, var41, var25,
							var31);
					var6.addVertexWithUV(var45, p_147767_3_ + 1, var41, var25,
							var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 1, var41, var23,
							var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 0, var41, var23,
							var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 0, var41, var25,
							var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1, var41, var25,
							var27);
				}

				if (var57
						|| p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1,
								p_147767_4_ + 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var43, var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var43, var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var25, var29);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var43, var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var43, var25, var29);
				}

				if (var58
						|| p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1,
								p_147767_4_ + 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var43,
							var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var43,
							var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var25, var29);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var43,
							var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var43,
							var25, var29);
				}
			}
		} else {
			var6.addVertexWithUV(var35, p_147767_3_ + 1, var43, dzr, d3zr);
			var6.addVertexWithUV(var35, p_147767_3_ + 0, var43, dzr, d4zr);
			var6.addVertexWithUV(var35, p_147767_3_ + 0, var39, d2zr, d4zr);
			var6.addVertexWithUV(var35, p_147767_3_ + 1, var39, d2zr, d3zr);
			var6.addVertexWithUV(var35, p_147767_3_ + 1, var39, dz, d3z);
			var6.addVertexWithUV(var35, p_147767_3_ + 0, var39, dz, d4z);
			var6.addVertexWithUV(var35, p_147767_3_ + 0, var43, d2z, d4z);
			var6.addVertexWithUV(var35, p_147767_3_ + 1, var43, d2z, d3z);

			if (var57) {
				var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D, var43,
						var25, var31);
				var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D, var39,
						var25, var27);
				var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D, var39,
						var23, var27);
				var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D, var43,
						var23, var31);
				var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D, var39,
						var25, var31);
				var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D, var43,
						var25, var27);
				var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D, var43,
						var23, var27);
				var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D, var39,
						var23, var31);
			} else {
				if (p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1,
								p_147767_4_ - 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var39, var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var39, var23, var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var39, var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var39, var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var23, var27);
				}

				if (p_147767_3_ < var5 - 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1,
								p_147767_4_ + 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var43, var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var43, var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var25, var29);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var43, var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ + 1 + 0.005D,
							var41, var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var41, var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ + 1 + 0.005D,
							var43, var25, var29);
				}
			}

			if (var58) {
				var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var43, var25,
						var31);
				var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var39, var25,
						var27);
				var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var39, var23,
						var27);
				var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var43, var23,
						var31);
				var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var39, var25,
						var31);
				var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var43, var25,
						var27);
				var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var43, var23,
						var27);
				var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var39, var23,
						var31);
			} else {
				if (p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1,
								p_147767_4_ - 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var39,
							var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var39,
							var23, var27);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var25, var27);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var39,
							var25, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var39,
							var23, var29);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var23, var27);
				}

				if (p_147767_3_ > 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1,
								p_147767_4_ + 1)) {
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var43,
							var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var43,
							var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var25, var29);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var43,
							var23, var29);
					var6.addVertexWithUV(var45, p_147767_3_ - 0.005D, var41,
							var23, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var41,
							var25, var31);
					var6.addVertexWithUV(var47, p_147767_3_ - 0.005D, var43,
							var25, var29);
				}
			}
		}

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147767_2_, p_147767_3_, p_147767_4_)) {
			renderSnow(p_147767_2_, p_147767_3_, p_147767_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		return true;
	}

	public boolean renderBlockQuartz(Block p_147779_1_, int p_147779_2_,
			int p_147779_3_, int p_147779_4_) {
		final int var5 = blockAccess.getBlockMetadata(p_147779_2_, p_147779_3_,
				p_147779_4_);

		if (var5 == 3) {
			uvRotateEast = 2;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 2;
		} else if (var5 == 4) {
			uvRotateSouth = 2;
			uvRotateNorth = 1;
			uvRotateTop = 3;
			uvRotateBottom = 3;
		}

		final boolean var6 = renderStandardBlock(p_147779_1_, p_147779_2_,
				p_147779_3_, p_147779_4_);
		uvRotateSouth = 0;
		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		return var6;
	}

	public boolean renderBlockRedstoneComparator(
			BlockRedstoneComparator p_147781_1_, int p_147781_2_,
			int p_147781_3_, int p_147781_4_) {
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147781_1_.getBlockBrightness(blockAccess,
				p_147781_2_, p_147781_3_, p_147781_4_));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final int var6 = blockAccess.getBlockMetadata(p_147781_2_, p_147781_3_,
				p_147781_4_);
		final int var7 = var6 & 3;
		double var8 = 0.0D;
		double var10 = -0.1875D;
		double var12 = 0.0D;
		double var14 = 0.0D;
		double var16 = 0.0D;
		IIcon var18;

		if (p_147781_1_.func_149969_d(var6)) {
			var18 = Blocks.redstone_torch.getBlockTextureFromSide(0);
		} else {
			var10 -= 0.1875D;
			var18 = Blocks.unlit_redstone_torch.getBlockTextureFromSide(0);
		}

		switch (var7) {
		case 0:
			var12 = -0.3125D;
			var16 = 1.0D;
			break;

		case 1:
			var8 = 0.3125D;
			var14 = -1.0D;
			break;

		case 2:
			var12 = 0.3125D;
			var16 = -1.0D;
			break;

		case 3:
			var8 = -0.3125D;
			var14 = 1.0D;
		}

		renderTorchAtAngle(p_147781_1_, p_147781_2_ + 0.25D * var14 + 0.1875D
				* var16, p_147781_3_ - 0.1875F, p_147781_4_ + 0.25D * var16
				+ 0.1875D * var14, 0.0D, 0.0D, var6);
		renderTorchAtAngle(p_147781_1_, p_147781_2_ + 0.25D * var14 + -0.1875D
				* var16, p_147781_3_ - 0.1875F, p_147781_4_ + 0.25D * var16
				+ -0.1875D * var14, 0.0D, 0.0D, var6);
		setOverrideBlockTexture(var18);
		renderTorchAtAngle(p_147781_1_, p_147781_2_ + var8,
				p_147781_3_ + var10, p_147781_4_ + var12, 0.0D, 0.0D, var6);
		clearOverrideBlockTexture();
		renderBlockRedstoneDiodeMetadata(p_147781_1_, p_147781_2_, p_147781_3_,
				p_147781_4_, var7);
		return true;
	}

	public boolean renderBlockRedstoneDiode(BlockRedstoneDiode p_147748_1_,
			int p_147748_2_, int p_147748_3_, int p_147748_4_) {
		renderBlockRedstoneDiodeMetadata(p_147748_1_, p_147748_2_, p_147748_3_,
				p_147748_4_, blockAccess.getBlockMetadata(p_147748_2_,
						p_147748_3_, p_147748_4_) & 3);
		return true;
	}

	public void renderBlockRedstoneDiodeMetadata(
			BlockRedstoneDiode p_147732_1_, int p_147732_2_, int p_147732_3_,
			int p_147732_4_, int p_147732_5_) {
		renderStandardBlock(p_147732_1_, p_147732_2_, p_147732_3_, p_147732_4_);
		final Tessellator var6 = Tessellator.instance;
		var6.setBrightness(p_147732_1_.getBlockBrightness(blockAccess,
				p_147732_2_, p_147732_3_, p_147732_4_));
		var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final int var7 = blockAccess.getBlockMetadata(p_147732_2_, p_147732_3_,
				p_147732_4_);
		final IIcon var8 = getBlockIconFromSideAndMetadata(p_147732_1_, 1, var7);
		final double var9 = var8.getMinU();
		final double var11 = var8.getMaxU();
		final double var13 = var8.getMinV();
		final double var15 = var8.getMaxV();
		final double var17 = 0.125D;
		double var19 = p_147732_2_ + 1;
		double var21 = p_147732_2_ + 1;
		double var23 = p_147732_2_ + 0;
		double var25 = p_147732_2_ + 0;
		double var27 = p_147732_4_ + 0;
		double var29 = p_147732_4_ + 1;
		double var31 = p_147732_4_ + 1;
		double var33 = p_147732_4_ + 0;
		final double var35 = p_147732_3_ + var17;

		if (p_147732_5_ == 2) {
			var19 = var21 = p_147732_2_ + 0;
			var23 = var25 = p_147732_2_ + 1;
			var27 = var33 = p_147732_4_ + 1;
			var29 = var31 = p_147732_4_ + 0;
		} else if (p_147732_5_ == 3) {
			var19 = var25 = p_147732_2_ + 0;
			var21 = var23 = p_147732_2_ + 1;
			var27 = var29 = p_147732_4_ + 0;
			var31 = var33 = p_147732_4_ + 1;
		} else if (p_147732_5_ == 1) {
			var19 = var25 = p_147732_2_ + 1;
			var21 = var23 = p_147732_2_ + 0;
			var27 = var29 = p_147732_4_ + 1;
			var31 = var33 = p_147732_4_ + 0;
		}

		var6.addVertexWithUV(var25, var35, var33, var9, var13);
		var6.addVertexWithUV(var23, var35, var31, var9, var15);
		var6.addVertexWithUV(var21, var35, var29, var11, var15);
		var6.addVertexWithUV(var19, var35, var27, var11, var13);
	}

	public boolean renderBlockRedstoneWire(Block p_147788_1_, int p_147788_2_,
			int p_147788_3_, int p_147788_4_) {
		final Tessellator var5 = Tessellator.instance;
		final int var6 = blockAccess.getBlockMetadata(p_147788_2_, p_147788_3_,
				p_147788_4_);
		final IIcon var7 = BlockRedstoneWire.func_150173_e("cross");
		final IIcon var8 = BlockRedstoneWire.func_150173_e("line");
		final IIcon var9 = BlockRedstoneWire.func_150173_e("cross_overlay");
		final IIcon var10 = BlockRedstoneWire.func_150173_e("line_overlay");
		var5.setBrightness(p_147788_1_.getBlockBrightness(blockAccess,
				p_147788_2_, p_147788_3_, p_147788_4_));
		final float var11 = var6 / 15.0F;
		float var12 = var11 * 0.6F + 0.4F;

		if (var6 == 0) {
			var12 = 0.3F;
		}

		float var13 = var11 * var11 * 0.7F - 0.5F;
		float var14 = var11 * var11 * 0.6F - 0.7F;

		if (var13 < 0.0F) {
			var13 = 0.0F;
		}

		if (var14 < 0.0F) {
			var14 = 0.0F;
		}

		final int rsColor = CustomColorizer.getRedstoneColor(var6);

		if (rsColor != -1) {
			final int var15 = rsColor >> 16 & 255;
			final int green = rsColor >> 8 & 255;
			final int var17 = rsColor & 255;
			var12 = var15 / 255.0F;
			var13 = green / 255.0F;
			var14 = var17 / 255.0F;
		}

		var5.setColorOpaque_F(var12, var13, var14);
		boolean var19 = BlockRedstoneWire.func_150174_f(blockAccess,
				p_147788_2_ - 1, p_147788_3_, p_147788_4_, 1)
				|| !blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_,
						p_147788_4_).isBlockNormalCube()
				&& BlockRedstoneWire.func_150174_f(blockAccess,
						p_147788_2_ - 1, p_147788_3_ - 1, p_147788_4_, -1);
		boolean var20 = BlockRedstoneWire.func_150174_f(blockAccess,
				p_147788_2_ + 1, p_147788_3_, p_147788_4_, 3)
				|| !blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_,
						p_147788_4_).isBlockNormalCube()
				&& BlockRedstoneWire.func_150174_f(blockAccess,
						p_147788_2_ + 1, p_147788_3_ - 1, p_147788_4_, -1);
		boolean var21 = BlockRedstoneWire.func_150174_f(blockAccess,
				p_147788_2_, p_147788_3_, p_147788_4_ - 1, 2)
				|| !blockAccess.getBlock(p_147788_2_, p_147788_3_,
						p_147788_4_ - 1).isBlockNormalCube()
				&& BlockRedstoneWire.func_150174_f(blockAccess, p_147788_2_,
						p_147788_3_ - 1, p_147788_4_ - 1, -1);
		boolean var22 = BlockRedstoneWire.func_150174_f(blockAccess,
				p_147788_2_, p_147788_3_, p_147788_4_ + 1, 0)
				|| !blockAccess.getBlock(p_147788_2_, p_147788_3_,
						p_147788_4_ + 1).isBlockNormalCube()
				&& BlockRedstoneWire.func_150174_f(blockAccess, p_147788_2_,
						p_147788_3_ - 1, p_147788_4_ + 1, -1);

		if (!blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1, p_147788_4_)
				.isBlockNormalCube()) {
			if (blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_, p_147788_4_)
					.isBlockNormalCube()
					&& BlockRedstoneWire.func_150174_f(blockAccess,
							p_147788_2_ - 1, p_147788_3_ + 1, p_147788_4_, -1)) {
				var19 = true;
			}

			if (blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_, p_147788_4_)
					.isBlockNormalCube()
					&& BlockRedstoneWire.func_150174_f(blockAccess,
							p_147788_2_ + 1, p_147788_3_ + 1, p_147788_4_, -1)) {
				var20 = true;
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ - 1)
					.isBlockNormalCube()
					&& BlockRedstoneWire.func_150174_f(blockAccess,
							p_147788_2_, p_147788_3_ + 1, p_147788_4_ - 1, -1)) {
				var21 = true;
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ + 1)
					.isBlockNormalCube()
					&& BlockRedstoneWire.func_150174_f(blockAccess,
							p_147788_2_, p_147788_3_ + 1, p_147788_4_ + 1, -1)) {
				var22 = true;
			}
		}

		float var23 = p_147788_2_ + 0;
		float var24 = p_147788_2_ + 1;
		float var25 = p_147788_4_ + 0;
		float var26 = p_147788_4_ + 1;
		boolean var27 = false;

		if ((var19 || var20) && !var21 && !var22) {
			var27 = true;
		}

		if ((var21 || var22) && !var20 && !var19) {
			var27 = true;
		}

		if (!var27) {
			int var33 = 0;
			int var29 = 0;
			int var30 = 16;
			int var31 = 16;
			if (!var19) {
				var23 += 0.3125F;
			}

			if (!var19) {
				var33 += 5;
			}

			if (!var20) {
				var24 -= 0.3125F;
			}

			if (!var20) {
				var30 -= 5;
			}

			if (!var21) {
				var25 += 0.3125F;
			}

			if (!var21) {
				var29 += 5;
			}

			if (!var22) {
				var26 -= 0.3125F;
			}

			if (!var22) {
				var31 -= 5;
			}

			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var26,
					var7.getInterpolatedU(var30), var7.getInterpolatedV(var31));
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var25,
					var7.getInterpolatedU(var30), var7.getInterpolatedV(var29));
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var25,
					var7.getInterpolatedU(var33), var7.getInterpolatedV(var29));
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var26,
					var7.getInterpolatedU(var33), var7.getInterpolatedV(var31));
			var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var26,
					var9.getInterpolatedU(var30), var9.getInterpolatedV(var31));
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var25,
					var9.getInterpolatedU(var30), var9.getInterpolatedV(var29));
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var25,
					var9.getInterpolatedU(var33), var9.getInterpolatedV(var29));
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var26,
					var9.getInterpolatedU(var33), var9.getInterpolatedV(var31));
		} else if (var27) {
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var26,
					var8.getMaxU(), var8.getMaxV());
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var25,
					var8.getMaxU(), var8.getMinV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var25,
					var8.getMinU(), var8.getMinV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var26,
					var8.getMinU(), var8.getMaxV());
			var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var26,
					var10.getMaxU(), var10.getMaxV());
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var25,
					var10.getMaxU(), var10.getMinV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var25,
					var10.getMinU(), var10.getMinV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var26,
					var10.getMinU(), var10.getMaxV());
		} else {
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var26,
					var8.getMaxU(), var8.getMaxV());
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var25,
					var8.getMinU(), var8.getMaxV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var25,
					var8.getMinU(), var8.getMinV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var26,
					var8.getMaxU(), var8.getMinV());
			var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var26,
					var10.getMaxU(), var10.getMaxV());
			var5.addVertexWithUV(var24, p_147788_3_ + 0.015625D, var25,
					var10.getMinU(), var10.getMaxV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var25,
					var10.getMinU(), var10.getMinV());
			var5.addVertexWithUV(var23, p_147788_3_ + 0.015625D, var26,
					var10.getMaxU(), var10.getMinV());
		}

		if (!blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1, p_147788_4_)
				.isBlockNormalCube()) {
			if (blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_, p_147788_4_)
					.isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_ + 1,
							p_147788_4_) == Blocks.redstone_wire) {
				var5.setColorOpaque_F(var12, var13, var14);
				var5.addVertexWithUV(p_147788_2_ + 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						var8.getMaxU(), var8.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0,
						p_147788_4_ + 1, var8.getMinU(), var8.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0,
						p_147788_4_ + 0, var8.getMinU(), var8.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						var8.getMaxU(), var8.getMaxV());
				var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				var5.addVertexWithUV(p_147788_2_ + 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						var10.getMaxU(), var10.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0,
						p_147788_4_ + 1, var10.getMinU(), var10.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0,
						p_147788_4_ + 0, var10.getMinU(), var10.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						var10.getMaxU(), var10.getMaxV());
			}

			if (blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_, p_147788_4_)
					.isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_ + 1,
							p_147788_4_) == Blocks.redstone_wire) {
				var5.setColorOpaque_F(var12, var13, var14);
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 0, p_147788_4_ + 1, var8.getMinU(),
						var8.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						var8.getMaxU(), var8.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						var8.getMaxU(), var8.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 0, p_147788_4_ + 0, var8.getMinU(),
						var8.getMinV());
				var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 0, p_147788_4_ + 1, var10.getMinU(),
						var10.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						var10.getMaxU(), var10.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						var10.getMaxU(), var10.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 1 - 0.015625D,
						p_147788_3_ + 0, p_147788_4_ + 0, var10.getMinU(),
						var10.getMinV());
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ - 1)
					.isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1,
							p_147788_4_ - 1) == Blocks.redstone_wire) {
				var5.setColorOpaque_F(var12, var13, var14);
				var5.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0,
						p_147788_4_ + 0.015625D, var8.getMinU(), var8.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 1,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						var8.getMaxU(), var8.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 0,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						var8.getMaxU(), var8.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0,
						p_147788_4_ + 0.015625D, var8.getMinU(), var8.getMinV());
				var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				var5.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0,
						p_147788_4_ + 0.015625D, var10.getMinU(),
						var10.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 1,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						var10.getMaxU(), var10.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 0,
						p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						var10.getMaxU(), var10.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0,
						p_147788_4_ + 0.015625D, var10.getMinU(),
						var10.getMinV());
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ + 1)
					.isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1,
							p_147788_4_ + 1) == Blocks.redstone_wire) {
				var5.setColorOpaque_F(var12, var13, var14);
				var5.addVertexWithUV(p_147788_2_ + 1,
						p_147788_3_ + 1 + 0.021875F,
						p_147788_4_ + 1 - 0.015625D, var8.getMaxU(),
						var8.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0,
						p_147788_4_ + 1 - 0.015625D, var8.getMinU(),
						var8.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0,
						p_147788_4_ + 1 - 0.015625D, var8.getMinU(),
						var8.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 0,
						p_147788_3_ + 1 + 0.021875F,
						p_147788_4_ + 1 - 0.015625D, var8.getMaxU(),
						var8.getMaxV());
				var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				var5.addVertexWithUV(p_147788_2_ + 1,
						p_147788_3_ + 1 + 0.021875F,
						p_147788_4_ + 1 - 0.015625D, var10.getMaxU(),
						var10.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0,
						p_147788_4_ + 1 - 0.015625D, var10.getMinU(),
						var10.getMinV());
				var5.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0,
						p_147788_4_ + 1 - 0.015625D, var10.getMinU(),
						var10.getMaxV());
				var5.addVertexWithUV(p_147788_2_ + 0,
						p_147788_3_ + 1 + 0.021875F,
						p_147788_4_ + 1 - 0.015625D, var10.getMaxU(),
						var10.getMaxV());
			}
		}

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147788_2_, p_147788_3_, p_147788_4_)) {
			renderSnow(p_147788_2_, p_147788_3_, p_147788_4_, 0.01D);
		}

		return true;
	}

	/**
	 * render a redstone repeater at the given coordinates
	 */
	public boolean renderBlockRepeater(BlockRedstoneRepeater p_147759_1_,
			int p_147759_2_, int p_147759_3_, int p_147759_4_) {
		final int var5 = blockAccess.getBlockMetadata(p_147759_2_, p_147759_3_,
				p_147759_4_);
		final int var6 = var5 & 3;
		final int var7 = (var5 & 12) >> 2;
		final Tessellator var8 = Tessellator.instance;
		var8.setBrightness(p_147759_1_.getBlockBrightness(blockAccess,
				p_147759_2_, p_147759_3_, p_147759_4_));
		var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final double var9 = -0.1875D;
		final boolean var11 = p_147759_1_.func_149910_g(blockAccess,
				p_147759_2_, p_147759_3_, p_147759_4_, var5);
		double var12 = 0.0D;
		double var14 = 0.0D;
		double var16 = 0.0D;
		double var18 = 0.0D;

		switch (var6) {
		case 0:
			var18 = -0.3125D;
			var14 = BlockRedstoneRepeater.field_149973_b[var7];
			break;

		case 1:
			var16 = 0.3125D;
			var12 = -BlockRedstoneRepeater.field_149973_b[var7];
			break;

		case 2:
			var18 = 0.3125D;
			var14 = -BlockRedstoneRepeater.field_149973_b[var7];
			break;

		case 3:
			var16 = -0.3125D;
			var12 = BlockRedstoneRepeater.field_149973_b[var7];
		}

		if (!var11) {
			renderTorchAtAngle(p_147759_1_, p_147759_2_ + var12, p_147759_3_
					+ var9, p_147759_4_ + var14, 0.0D, 0.0D, 0);
		} else {
			final IIcon var20 = this.getBlockIcon(Blocks.bedrock);
			setOverrideBlockTexture(var20);
			float var21 = 2.0F;
			float var22 = 14.0F;
			float var23 = 7.0F;
			float var24 = 9.0F;

			switch (var6) {
			case 1:
			case 3:
				var21 = 7.0F;
				var22 = 9.0F;
				var23 = 2.0F;
				var24 = 14.0F;

			case 0:
			case 2:
			default:
				setRenderBounds(var21 / 16.0F + (float) var12, 0.125D, var23
						/ 16.0F + (float) var14, var22 / 16.0F + (float) var12,
						0.25D, var24 / 16.0F + (float) var14);
				final double var25 = var20.getInterpolatedU(var21);
				final double var27 = var20.getInterpolatedV(var23);
				final double var29 = var20.getInterpolatedU(var22);
				final double var31 = var20.getInterpolatedV(var24);
				var8.addVertexWithUV(p_147759_2_ + var21 / 16.0F + var12,
						p_147759_3_ + 0.25F, p_147759_4_ + var23 / 16.0F
								+ var14, var25, var27);
				var8.addVertexWithUV(p_147759_2_ + var21 / 16.0F + var12,
						p_147759_3_ + 0.25F, p_147759_4_ + var24 / 16.0F
								+ var14, var25, var31);
				var8.addVertexWithUV(p_147759_2_ + var22 / 16.0F + var12,
						p_147759_3_ + 0.25F, p_147759_4_ + var24 / 16.0F
								+ var14, var29, var31);
				var8.addVertexWithUV(p_147759_2_ + var22 / 16.0F + var12,
						p_147759_3_ + 0.25F, p_147759_4_ + var23 / 16.0F
								+ var14, var29, var27);
				renderStandardBlock(p_147759_1_, p_147759_2_, p_147759_3_,
						p_147759_4_);
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
				clearOverrideBlockTexture();
			}
		}

		var8.setBrightness(p_147759_1_.getBlockBrightness(blockAccess,
				p_147759_2_, p_147759_3_, p_147759_4_));
		var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		renderTorchAtAngle(p_147759_1_, p_147759_2_ + var16,
				p_147759_3_ + var9, p_147759_4_ + var18, 0.0D, 0.0D, 0);
		renderBlockRedstoneDiode(p_147759_1_, p_147759_2_, p_147759_3_,
				p_147759_4_);
		return true;
	}

	public void renderBlockSandFalling(Block p_147749_1_, World p_147749_2_,
			int p_147749_3_, int p_147749_4_, int p_147749_5_, int p_147749_6_) {
		final float var7 = 0.5F;
		final float var8 = 1.0F;
		final float var9 = 0.8F;
		final float var10 = 0.6F;
		final Tessellator var11 = Tessellator.instance;
		var11.startDrawingQuads();
		var11.setBrightness(p_147749_1_.getBlockBrightness(p_147749_2_,
				p_147749_3_, p_147749_4_, p_147749_5_));
		var11.setColorOpaque_F(var7, var7, var7);
		renderFaceYNeg(p_147749_1_, -0.5D, -0.5D, -0.5D,
				getBlockIconFromSideAndMetadata(p_147749_1_, 0, p_147749_6_));
		var11.setColorOpaque_F(var8, var8, var8);
		renderFaceYPos(p_147749_1_, -0.5D, -0.5D, -0.5D,
				getBlockIconFromSideAndMetadata(p_147749_1_, 1, p_147749_6_));
		var11.setColorOpaque_F(var9, var9, var9);
		renderFaceZNeg(p_147749_1_, -0.5D, -0.5D, -0.5D,
				getBlockIconFromSideAndMetadata(p_147749_1_, 2, p_147749_6_));
		var11.setColorOpaque_F(var9, var9, var9);
		renderFaceZPos(p_147749_1_, -0.5D, -0.5D, -0.5D,
				getBlockIconFromSideAndMetadata(p_147749_1_, 3, p_147749_6_));
		var11.setColorOpaque_F(var10, var10, var10);
		renderFaceXNeg(p_147749_1_, -0.5D, -0.5D, -0.5D,
				getBlockIconFromSideAndMetadata(p_147749_1_, 4, p_147749_6_));
		var11.setColorOpaque_F(var10, var10, var10);
		renderFaceXPos(p_147749_1_, -0.5D, -0.5D, -0.5D,
				getBlockIconFromSideAndMetadata(p_147749_1_, 5, p_147749_6_));
		var11.draw();
	}

	public boolean renderBlockStainedGlassPane(Block block, int x, int y, int z) {
		blockAccess.getHeight();
		final Tessellator var6 = Tessellator.instance;
		var6.setBrightness(block.getBlockBrightness(blockAccess, x, y, z));
		final int var7 = block.colorMultiplier(blockAccess, x, y, z);
		float var8 = (var7 >> 16 & 255) / 255.0F;
		float var9 = (var7 >> 8 & 255) / 255.0F;
		float var10 = (var7 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float isStainedGlass = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			final float iconGlass = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			final float iconGlassPaneTop = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = isStainedGlass;
			var9 = iconGlass;
			var10 = iconGlassPaneTop;
		}

		var6.setColorOpaque_F(var8, var9, var10);
		final boolean isStainedGlass1 = block instanceof BlockStainedGlassPane;
		int metadata = 0;
		IIcon iconGlass1;
		IIcon iconGlassPaneTop1;

		if (hasOverrideBlockTexture()) {
			iconGlass1 = overrideBlockTexture;
			iconGlassPaneTop1 = overrideBlockTexture;
		} else {
			metadata = blockAccess.getBlockMetadata(x, y, z);
			iconGlass1 = getBlockIconFromSideAndMetadata(block, 0, metadata);
			iconGlassPaneTop1 = isStainedGlass1 ? ((BlockStainedGlassPane) block)
					.func_150104_b(metadata) : ((BlockPane) block)
					.func_150097_e();
		}

		IIcon iconZ = iconGlass1;
		boolean drawTop = true;
		boolean drawBottom = true;

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			final IIcon gMinU = ConnectedTextures.getConnectedTexture(
					blockAccess, block, x, y, z, 4, iconGlass1);
			final IIcon iz = ConnectedTextures.getConnectedTexture(blockAccess,
					block, x, y, z, 3, iconGlass1);

			if (gMinU != iconGlass1 || iz != iconGlass1) {
				drawTop = blockAccess.getBlock(x, y + 1, z) != block
						|| blockAccess.getBlockMetadata(x, y + 1, z) != metadata;
				drawBottom = blockAccess.getBlock(x, y - 1, z) != block
						|| blockAccess.getBlockMetadata(x, y - 1, z) != metadata;
			}

			iconGlass1 = gMinU;
			iconZ = iz;
		}

		final double gMinU1 = iconGlass1.getMinU();
		final double gHalf7U1 = iconGlass1.getInterpolatedU(7.0D);
		final double gHalf9U = iconGlass1.getInterpolatedU(9.0D);
		final double gMaxU = iconGlass1.getMaxU();
		final double gMinV = iconGlass1.getMinV();
		final double gMaxV = iconGlass1.getMaxV();
		final double gMinUz = iconZ.getMinU();
		final double gHalf7Uz = iconZ.getInterpolatedU(7.0D);
		final double gHalf9Uz = iconZ.getInterpolatedU(9.0D);
		final double gMaxUz = iconZ.getMaxU();
		final double gMinVz = iconZ.getMinV();
		final double gMaxVz = iconZ.getMaxV();
		final double var26 = iconGlassPaneTop1.getInterpolatedU(7.0D);
		final double var28 = iconGlassPaneTop1.getInterpolatedU(9.0D);
		final double var30 = iconGlassPaneTop1.getMinV();
		final double var32 = iconGlassPaneTop1.getMaxV();
		final double var34 = iconGlassPaneTop1.getInterpolatedV(7.0D);
		final double var36 = iconGlassPaneTop1.getInterpolatedV(9.0D);
		final double x0 = x;
		final double x1 = x + 1;
		final double z0 = z;
		final double z1 = z + 1;
		final double xHalfMin = x + 0.5D - 0.0625D;
		final double xHalfMax = x + 0.5D + 0.0625D;
		final double zHalfMin = z + 0.5D - 0.0625D;
		final double zHalfMax = z + 0.5D + 0.0625D;
		final boolean connZNeg = isStainedGlass1 ? ((BlockStainedGlassPane) block)
				.func_150098_a(blockAccess.getBlock(x, y, z - 1))
				: ((BlockPane) block).func_150098_a(blockAccess.getBlock(x, y,
						z - 1));
		final boolean connZPos = isStainedGlass1 ? ((BlockStainedGlassPane) block)
				.func_150098_a(blockAccess.getBlock(x, y, z + 1))
				: ((BlockPane) block).func_150098_a(blockAccess.getBlock(x, y,
						z + 1));
		final boolean connXNeg = isStainedGlass1 ? ((BlockStainedGlassPane) block)
				.func_150098_a(blockAccess.getBlock(x - 1, y, z))
				: ((BlockPane) block).func_150098_a(blockAccess.getBlock(x - 1,
						y, z));
		final boolean connXPos = isStainedGlass1 ? ((BlockStainedGlassPane) block)
				.func_150098_a(blockAccess.getBlock(x + 1, y, z))
				: ((BlockPane) block).func_150098_a(blockAccess.getBlock(x + 1,
						y, z));
		final boolean disconnected = !connZNeg && !connZPos && !connXNeg
				&& !connXPos;
		double yTop = y + 0.999D;
		double yBottom = y + 0.001D;

		if (!drawTop) {
			yTop = y + 1;
		}

		if (!drawBottom) {
			yBottom = y;
		}

		if (!connXNeg && !disconnected) {
			if (!connZNeg && !connZPos) {
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7U1, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7U1,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf9U,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf9U, gMinV);
			}
		} else if (connXNeg && connXPos) {
			if (!connZNeg) {
				var6.addVertexWithUV(x1, yTop, zHalfMin, gMaxUz, gMinVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMin, gMaxUz, gMaxVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMin, gMinUz, gMaxVz);
				var6.addVertexWithUV(x0, yTop, zHalfMin, gMinUz, gMinVz);
			} else {
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7Uz, gMinVz);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7Uz,
						gMaxVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMin, gMinUz, gMaxVz);
				var6.addVertexWithUV(x0, yTop, zHalfMin, gMinUz, gMinVz);
				var6.addVertexWithUV(x1, yTop, zHalfMin, gMaxUz, gMinVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMin, gMaxUz, gMaxVz);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf9Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf9Uz, gMinVz);
			}

			if (!connZPos) {
				var6.addVertexWithUV(x0, yTop, zHalfMax, gMinUz, gMinVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMax, gMinUz, gMaxVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMax, gMaxUz, gMaxVz);
				var6.addVertexWithUV(x1, yTop, zHalfMax, gMaxUz, gMinVz);
			} else {
				var6.addVertexWithUV(x0, yTop, zHalfMax, gMinUz, gMinVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMax, gMinUz, gMaxVz);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf7Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf7Uz, gMinVz);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf9Uz, gMinVz);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf9Uz,
						gMaxVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMax, gMaxUz, gMaxVz);
				var6.addVertexWithUV(x1, yTop, zHalfMax, gMaxUz, gMinVz);
			}

			if (drawTop) {
				var6.addVertexWithUV(x0, yTop, zHalfMax, var28, var30);
				var6.addVertexWithUV(x1, yTop, zHalfMax, var28, var32);
				var6.addVertexWithUV(x1, yTop, zHalfMin, var26, var32);
				var6.addVertexWithUV(x0, yTop, zHalfMin, var26, var30);
			}

			if (drawBottom) {
				var6.addVertexWithUV(x1, yBottom, zHalfMax, var26, var32);
				var6.addVertexWithUV(x0, yBottom, zHalfMax, var26, var30);
				var6.addVertexWithUV(x0, yBottom, zHalfMin, var28, var30);
				var6.addVertexWithUV(x1, yBottom, zHalfMin, var28, var32);
			}
		} else {
			if (!connZNeg && !disconnected) {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf9Uz, gMinVz);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf9Uz,
						gMaxVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMin, gMinUz, gMaxVz);
				var6.addVertexWithUV(x0, yTop, zHalfMin, gMinUz, gMinVz);
			} else {
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7Uz, gMinVz);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7Uz,
						gMaxVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMin, gMinUz, gMaxVz);
				var6.addVertexWithUV(x0, yTop, zHalfMin, gMinUz, gMinVz);
			}

			if (!connZPos && !disconnected) {
				var6.addVertexWithUV(x0, yTop, zHalfMax, gMinUz, gMinVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMax, gMinUz, gMaxVz);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf9Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf9Uz, gMinVz);
			} else {
				var6.addVertexWithUV(x0, yTop, zHalfMax, gMinUz, gMinVz);
				var6.addVertexWithUV(x0, yBottom, zHalfMax, gMinUz, gMaxVz);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf7Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf7Uz, gMinVz);
			}

			if (drawTop) {
				var6.addVertexWithUV(x0, yTop, zHalfMax, var28, var30);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, var28, var34);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, var26, var34);
				var6.addVertexWithUV(x0, yTop, zHalfMin, var26, var30);
			}

			if (drawBottom) {
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, var26, var34);
				var6.addVertexWithUV(x0, yBottom, zHalfMax, var26, var30);
				var6.addVertexWithUV(x0, yBottom, zHalfMin, var28, var30);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, var28, var34);
			}
		}

		if ((connXPos || disconnected) && !connXNeg) {
			if (!connZPos && !disconnected) {
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf7Uz, gMinVz);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf7Uz,
						gMaxVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMax, gMaxUz, gMaxVz);
				var6.addVertexWithUV(x1, yTop, zHalfMax, gMaxUz, gMinVz);
			} else {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf9Uz, gMinVz);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf9Uz,
						gMaxVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMax, gMaxUz, gMaxVz);
				var6.addVertexWithUV(x1, yTop, zHalfMax, gMaxUz, gMinVz);
			}

			if (!connZNeg && !disconnected) {
				var6.addVertexWithUV(x1, yTop, zHalfMin, gMaxUz, gMinVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMin, gMaxUz, gMaxVz);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7Uz, gMinVz);
			} else {
				var6.addVertexWithUV(x1, yTop, zHalfMin, gMaxUz, gMinVz);
				var6.addVertexWithUV(x1, yBottom, zHalfMin, gMaxUz, gMaxVz);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf9Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf9Uz, gMinVz);
			}

			if (drawTop) {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, var28, var36);
				var6.addVertexWithUV(x1, yTop, zHalfMax, var28, var30);
				var6.addVertexWithUV(x1, yTop, zHalfMin, var26, var30);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, var26, var36);
			}

			if (drawBottom) {
				var6.addVertexWithUV(x1, yBottom, zHalfMax, var26, var32);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, var26, var36);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, var28, var36);
				var6.addVertexWithUV(x1, yBottom, zHalfMin, var28, var32);
			}
		} else if (!connXPos && !connZNeg && !connZPos) {
			var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf7U1, gMinV);
			var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf7U1, gMaxV);
			var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf9U, gMaxV);
			var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf9U, gMinV);
		}

		if (!connZNeg && !disconnected) {
			if (!connXPos && !connXNeg) {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf9Uz, gMinVz);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf9Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7Uz,
						gMaxVz);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7Uz, gMinVz);
			}
		} else if (connZNeg && connZPos) {
			if (!connXNeg) {
				var6.addVertexWithUV(xHalfMin, yTop, z0, gMinU1, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, z1, gMaxU, gMinV);
			} else {
				var6.addVertexWithUV(xHalfMin, yTop, z0, gMinU1, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7U1,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7U1, gMinV);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf9U, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf9U,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, z1, gMaxU, gMinV);
			}

			if (!connXPos) {
				var6.addVertexWithUV(xHalfMax, yTop, z1, gMaxU, gMinV);
				var6.addVertexWithUV(xHalfMax, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMax, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMax, yTop, z0, gMinU1, gMinV);
			} else {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf7U1, gMinV);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf7U1,
						gMaxV);
				var6.addVertexWithUV(xHalfMax, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMax, yTop, z0, gMinU1, gMinV);
				var6.addVertexWithUV(xHalfMax, yTop, z1, gMaxU, gMinV);
				var6.addVertexWithUV(xHalfMax, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf9U,
						gMaxV);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf9U, gMinV);
			}

			if (drawTop) {
				var6.addVertexWithUV(xHalfMax, yTop, z0, var28, var30);
				var6.addVertexWithUV(xHalfMin, yTop, z0, var26, var30);
				var6.addVertexWithUV(xHalfMin, yTop, z1, var26, var32);
				var6.addVertexWithUV(xHalfMax, yTop, z1, var28, var32);
			}

			if (drawBottom) {
				var6.addVertexWithUV(xHalfMin, yBottom, z0, var26, var30);
				var6.addVertexWithUV(xHalfMax, yBottom, z0, var28, var30);
				var6.addVertexWithUV(xHalfMax, yBottom, z1, var28, var32);
				var6.addVertexWithUV(xHalfMin, yBottom, z1, var26, var32);
			}
		} else {
			if (!connXNeg && !disconnected) {
				var6.addVertexWithUV(xHalfMin, yTop, z0, gMinU1, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf9U,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf9U, gMinV);
			} else {
				var6.addVertexWithUV(xHalfMin, yTop, z0, gMinU1, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7U1,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7U1, gMinV);
			}

			if (!connXPos && !disconnected) {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf9U, gMinV);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf9U,
						gMaxV);
				var6.addVertexWithUV(xHalfMax, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMax, yTop, z0, gMinU1, gMinV);
			} else {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf7U1, gMinV);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf7U1,
						gMaxV);
				var6.addVertexWithUV(xHalfMax, yBottom, z0, gMinU1, gMaxV);
				var6.addVertexWithUV(xHalfMax, yTop, z0, gMinU1, gMinV);
			}

			if (drawTop) {
				var6.addVertexWithUV(xHalfMax, yTop, z0, var28, var30);
				var6.addVertexWithUV(xHalfMin, yTop, z0, var26, var30);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, var26, var34);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, var28, var34);
			}

			if (drawBottom) {
				var6.addVertexWithUV(xHalfMin, yBottom, z0, var26, var30);
				var6.addVertexWithUV(xHalfMax, yBottom, z0, var28, var30);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, var28, var34);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, var26, var34);
			}
		}

		if ((connZPos || disconnected) && !connZNeg) {
			if (!connXNeg && !disconnected) {
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, gHalf7U1, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, gHalf7U1,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, z1, gMaxU, gMinV);
			} else {
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf9U, gMinV);
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf9U,
						gMaxV);
				var6.addVertexWithUV(xHalfMin, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMin, yTop, z1, gMaxU, gMinV);
			}

			if (!connXPos && !disconnected) {
				var6.addVertexWithUV(xHalfMax, yTop, z1, gMaxU, gMinV);
				var6.addVertexWithUV(xHalfMax, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, gHalf7U1,
						gMaxV);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, gHalf7U1, gMinV);
			} else {
				var6.addVertexWithUV(xHalfMax, yTop, z1, gMaxU, gMinV);
				var6.addVertexWithUV(xHalfMax, yBottom, z1, gMaxU, gMaxV);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf9U,
						gMaxV);
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf9U, gMinV);
			}

			if (drawTop) {
				var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, var28, var36);
				var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, var26, var36);
				var6.addVertexWithUV(xHalfMin, yTop, z1, var26, var32);
				var6.addVertexWithUV(xHalfMax, yTop, z1, var28, var32);
			}

			if (drawBottom) {
				var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, var26, var36);
				var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, var28, var36);
				var6.addVertexWithUV(xHalfMax, yBottom, z1, var28, var32);
				var6.addVertexWithUV(xHalfMin, yBottom, z1, var26, var32);
			}
		} else if (!connZPos && !connXPos && !connXNeg) {
			var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, gHalf7Uz, gMinVz);
			var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, gHalf7Uz, gMaxVz);
			var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, gHalf9Uz, gMaxVz);
			var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, gHalf9Uz, gMinVz);
		}

		if (drawTop) {
			var6.addVertexWithUV(xHalfMax, yTop, zHalfMin, var28, var34);
			var6.addVertexWithUV(xHalfMin, yTop, zHalfMin, var26, var34);
			var6.addVertexWithUV(xHalfMin, yTop, zHalfMax, var26, var36);
			var6.addVertexWithUV(xHalfMax, yTop, zHalfMax, var28, var36);
		}

		if (drawBottom) {
			var6.addVertexWithUV(xHalfMin, yBottom, zHalfMin, var26, var34);
			var6.addVertexWithUV(xHalfMax, yBottom, zHalfMin, var28, var34);
			var6.addVertexWithUV(xHalfMax, yBottom, zHalfMax, var28, var36);
			var6.addVertexWithUV(xHalfMin, yBottom, zHalfMax, var26, var36);
		}

		if (disconnected) {
			var6.addVertexWithUV(x0, yTop, zHalfMin, gHalf7U1, gMinV);
			var6.addVertexWithUV(x0, yBottom, zHalfMin, gHalf7U1, gMaxV);
			var6.addVertexWithUV(x0, yBottom, zHalfMax, gHalf9U, gMaxV);
			var6.addVertexWithUV(x0, yTop, zHalfMax, gHalf9U, gMinV);
			var6.addVertexWithUV(x1, yTop, zHalfMax, gHalf7U1, gMinV);
			var6.addVertexWithUV(x1, yBottom, zHalfMax, gHalf7U1, gMaxV);
			var6.addVertexWithUV(x1, yBottom, zHalfMin, gHalf9U, gMaxV);
			var6.addVertexWithUV(x1, yTop, zHalfMin, gHalf9U, gMinV);
			var6.addVertexWithUV(xHalfMax, yTop, z0, gHalf9Uz, gMinVz);
			var6.addVertexWithUV(xHalfMax, yBottom, z0, gHalf9Uz, gMaxVz);
			var6.addVertexWithUV(xHalfMin, yBottom, z0, gHalf7Uz, gMaxVz);
			var6.addVertexWithUV(xHalfMin, yTop, z0, gHalf7Uz, gMinVz);
			var6.addVertexWithUV(xHalfMin, yTop, z1, gHalf7Uz, gMinVz);
			var6.addVertexWithUV(xHalfMin, yBottom, z1, gHalf7Uz, gMaxVz);
			var6.addVertexWithUV(xHalfMax, yBottom, z1, gHalf9Uz, gMaxVz);
			var6.addVertexWithUV(xHalfMax, yTop, z1, gHalf9Uz, gMinVz);
		}

		return true;
	}

	public boolean renderBlockStairs(BlockStairs p_147722_1_, int p_147722_2_,
			int p_147722_3_, int p_147722_4_) {
		p_147722_1_.func_150147_e(blockAccess, p_147722_2_, p_147722_3_,
				p_147722_4_);
		setRenderBoundsFromBlock(p_147722_1_);
		renderStandardBlock(p_147722_1_, p_147722_2_, p_147722_3_, p_147722_4_);
		field_152631_f = true;
		final boolean var5 = p_147722_1_.func_150145_f(blockAccess,
				p_147722_2_, p_147722_3_, p_147722_4_);
		setRenderBoundsFromBlock(p_147722_1_);
		renderStandardBlock(p_147722_1_, p_147722_2_, p_147722_3_, p_147722_4_);

		if (var5
				&& p_147722_1_.func_150144_g(blockAccess, p_147722_2_,
						p_147722_3_, p_147722_4_)) {
			setRenderBoundsFromBlock(p_147722_1_);
			renderStandardBlock(p_147722_1_, p_147722_2_, p_147722_3_,
					p_147722_4_);
		}

		field_152631_f = false;
		return true;
	}

	public boolean renderBlockStem(Block p_147724_1_, int p_147724_2_,
			int p_147724_3_, int p_147724_4_) {
		final BlockStem var5 = (BlockStem) p_147724_1_;
		final Tessellator var6 = Tessellator.instance;
		var6.setBrightness(var5.getBlockBrightness(blockAccess, p_147724_2_,
				p_147724_3_, p_147724_4_));
		final int var7 = CustomColorizer.getStemColorMultiplier(var5,
				blockAccess, p_147724_2_, p_147724_3_, p_147724_4_);
		float var8 = (var7 >> 16 & 255) / 255.0F;
		float var9 = (var7 >> 8 & 255) / 255.0F;
		float var10 = (var7 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var14 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
			final float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
			final float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
			var8 = var14;
			var9 = var12;
			var10 = var13;
		}

		var6.setColorOpaque_F(var8, var9, var10);
		var5.setBlockBoundsBasedOnState(blockAccess, p_147724_2_, p_147724_3_,
				p_147724_4_);
		final int var141 = var5.func_149873_e(blockAccess, p_147724_2_,
				p_147724_3_, p_147724_4_);

		if (var141 < 0) {
			renderBlockStemSmall(var5, blockAccess.getBlockMetadata(
					p_147724_2_, p_147724_3_, p_147724_4_), renderMaxY,
					p_147724_2_, p_147724_3_ - 0.0625F, p_147724_4_);
		} else {
			renderBlockStemSmall(var5, blockAccess.getBlockMetadata(
					p_147724_2_, p_147724_3_, p_147724_4_), 0.5D, p_147724_2_,
					p_147724_3_ - 0.0625F, p_147724_4_);
			renderBlockStemBig(var5, blockAccess.getBlockMetadata(p_147724_2_,
					p_147724_3_, p_147724_4_), var141, renderMaxY, p_147724_2_,
					p_147724_3_ - 0.0625F, p_147724_4_);
		}

		return true;
	}

	public void renderBlockStemBig(BlockStem p_147740_1_, int p_147740_2_,
			int p_147740_3_, double p_147740_4_, double p_147740_6_,
			double p_147740_8_, double p_147740_10_) {
		final Tessellator var12 = Tessellator.instance;
		IIcon var13 = p_147740_1_.func_149872_i();

		if (hasOverrideBlockTexture()) {
			var13 = overrideBlockTexture;
		}

		double var14 = var13.getMinU();
		final double var16 = var13.getMinV();
		double var18 = var13.getMaxU();
		final double var20 = var13.getMaxV();
		final double var22 = p_147740_6_ + 0.5D - 0.5D;
		final double var24 = p_147740_6_ + 0.5D + 0.5D;
		final double var26 = p_147740_10_ + 0.5D - 0.5D;
		final double var28 = p_147740_10_ + 0.5D + 0.5D;
		final double var30 = p_147740_6_ + 0.5D;
		final double var32 = p_147740_10_ + 0.5D;

		if ((p_147740_3_ + 1) / 2 % 2 == 1) {
			final double var34 = var18;
			var18 = var14;
			var14 = var34;
		}

		if (p_147740_3_ < 2) {
			var12.addVertexWithUV(var22, p_147740_8_ + p_147740_4_, var32,
					var14, var16);
			var12.addVertexWithUV(var22, p_147740_8_ + 0.0D, var32, var14,
					var20);
			var12.addVertexWithUV(var24, p_147740_8_ + 0.0D, var32, var18,
					var20);
			var12.addVertexWithUV(var24, p_147740_8_ + p_147740_4_, var32,
					var18, var16);
			var12.addVertexWithUV(var24, p_147740_8_ + p_147740_4_, var32,
					var18, var16);
			var12.addVertexWithUV(var24, p_147740_8_ + 0.0D, var32, var18,
					var20);
			var12.addVertexWithUV(var22, p_147740_8_ + 0.0D, var32, var14,
					var20);
			var12.addVertexWithUV(var22, p_147740_8_ + p_147740_4_, var32,
					var14, var16);
		} else {
			var12.addVertexWithUV(var30, p_147740_8_ + p_147740_4_, var28,
					var14, var16);
			var12.addVertexWithUV(var30, p_147740_8_ + 0.0D, var28, var14,
					var20);
			var12.addVertexWithUV(var30, p_147740_8_ + 0.0D, var26, var18,
					var20);
			var12.addVertexWithUV(var30, p_147740_8_ + p_147740_4_, var26,
					var18, var16);
			var12.addVertexWithUV(var30, p_147740_8_ + p_147740_4_, var26,
					var18, var16);
			var12.addVertexWithUV(var30, p_147740_8_ + 0.0D, var26, var18,
					var20);
			var12.addVertexWithUV(var30, p_147740_8_ + 0.0D, var28, var14,
					var20);
			var12.addVertexWithUV(var30, p_147740_8_ + p_147740_4_, var28,
					var14, var16);
		}
	}

	public void renderBlockStemSmall(Block p_147730_1_, int p_147730_2_,
			double p_147730_3_, double p_147730_5_, double p_147730_7_,
			double p_147730_9_) {
		final Tessellator var11 = Tessellator.instance;
		IIcon var12 = getBlockIconFromSideAndMetadata(p_147730_1_, 0,
				p_147730_2_);

		if (hasOverrideBlockTexture()) {
			var12 = overrideBlockTexture;
		}

		final double var13 = var12.getMinU();
		final double var15 = var12.getMinV();
		final double var17 = var12.getMaxU();
		final double var19 = var12.getInterpolatedV(p_147730_3_ * 16.0D);
		final double var21 = p_147730_5_ + 0.5D - 0.44999998807907104D;
		final double var23 = p_147730_5_ + 0.5D + 0.44999998807907104D;
		final double var25 = p_147730_9_ + 0.5D - 0.44999998807907104D;
		final double var27 = p_147730_9_ + 0.5D + 0.44999998807907104D;
		var11.addVertexWithUV(var21, p_147730_7_ + p_147730_3_, var25, var13,
				var15);
		var11.addVertexWithUV(var21, p_147730_7_ + 0.0D, var25, var13, var19);
		var11.addVertexWithUV(var23, p_147730_7_ + 0.0D, var27, var17, var19);
		var11.addVertexWithUV(var23, p_147730_7_ + p_147730_3_, var27, var17,
				var15);
		var11.addVertexWithUV(var23, p_147730_7_ + p_147730_3_, var27, var17,
				var15);
		var11.addVertexWithUV(var23, p_147730_7_ + 0.0D, var27, var17, var19);
		var11.addVertexWithUV(var21, p_147730_7_ + 0.0D, var25, var13, var19);
		var11.addVertexWithUV(var21, p_147730_7_ + p_147730_3_, var25, var13,
				var15);
		var11.addVertexWithUV(var21, p_147730_7_ + p_147730_3_, var27, var13,
				var15);
		var11.addVertexWithUV(var21, p_147730_7_ + 0.0D, var27, var13, var19);
		var11.addVertexWithUV(var23, p_147730_7_ + 0.0D, var25, var17, var19);
		var11.addVertexWithUV(var23, p_147730_7_ + p_147730_3_, var25, var17,
				var15);
		var11.addVertexWithUV(var23, p_147730_7_ + p_147730_3_, var25, var17,
				var15);
		var11.addVertexWithUV(var23, p_147730_7_ + 0.0D, var25, var17, var19);
		var11.addVertexWithUV(var21, p_147730_7_ + 0.0D, var27, var13, var19);
		var11.addVertexWithUV(var21, p_147730_7_ + p_147730_3_, var27, var13,
				var15);
	}

	/**
	 * Renders a torch block at the given coordinates
	 */
	public boolean renderBlockTorch(Block p_147791_1_, int p_147791_2_,
			int p_147791_3_, int p_147791_4_) {
		final int var5 = blockAccess.getBlockMetadata(p_147791_2_, p_147791_3_,
				p_147791_4_);
		final Tessellator var6 = Tessellator.instance;
		var6.setBrightness(p_147791_1_.getBlockBrightness(blockAccess,
				p_147791_2_, p_147791_3_, p_147791_4_));
		var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final double var7 = 0.4000000059604645D;
		final double var9 = 0.5D - var7;
		final double var11 = 0.20000000298023224D;

		if (var5 == 1) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_ - var9, p_147791_3_
					+ var11, p_147791_4_, -var7, 0.0D, 0);
		} else if (var5 == 2) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_ + var9, p_147791_3_
					+ var11, p_147791_4_, var7, 0.0D, 0);
		} else if (var5 == 3) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_, p_147791_3_ + var11,
					p_147791_4_ - var9, 0.0D, -var7, 0);
		} else if (var5 == 4) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_, p_147791_3_ + var11,
					p_147791_4_ + var9, 0.0D, var7, 0);
		} else {
			renderTorchAtAngle(p_147791_1_, p_147791_2_, p_147791_3_,
					p_147791_4_, 0.0D, 0.0D, 0);

			if (p_147791_1_ != Blocks.torch && Config.isBetterSnow()
					&& hasSnowNeighbours(p_147791_2_, p_147791_3_, p_147791_4_)) {
				renderSnow(p_147791_2_, p_147791_3_, p_147791_4_,
						Blocks.snow_layer.getBlockBoundsMaxY());
			}
		}

		return true;
	}

	public boolean renderBlockTripWire(Block p_147756_1_, int p_147756_2_,
			int p_147756_3_, int p_147756_4_) {
		final Tessellator var5 = Tessellator.instance;
		IIcon var6 = getBlockIconFromSide(p_147756_1_, 0);
		final int var7 = blockAccess.getBlockMetadata(p_147756_2_, p_147756_3_,
				p_147756_4_);
		final boolean var8 = (var7 & 4) == 4;
		final boolean var9 = (var7 & 2) == 2;

		if (hasOverrideBlockTexture()) {
			var6 = overrideBlockTexture;
		}

		var5.setBrightness(p_147756_1_.getBlockBrightness(blockAccess,
				p_147756_2_, p_147756_3_, p_147756_4_));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final double var10 = var6.getMinU();
		final double var12 = var6.getInterpolatedV(var8 ? 2.0D : 0.0D);
		final double var14 = var6.getMaxU();
		final double var16 = var6.getInterpolatedV(var8 ? 4.0D : 2.0D);
		final double var18 = (var9 ? 3.5F : 1.5F) / 16.0D;
		final boolean var20 = BlockTripWire.func_150139_a(blockAccess,
				p_147756_2_, p_147756_3_, p_147756_4_, var7, 1);
		final boolean var21 = BlockTripWire.func_150139_a(blockAccess,
				p_147756_2_, p_147756_3_, p_147756_4_, var7, 3);
		boolean var22 = BlockTripWire.func_150139_a(blockAccess, p_147756_2_,
				p_147756_3_, p_147756_4_, var7, 2);
		boolean var23 = BlockTripWire.func_150139_a(blockAccess, p_147756_2_,
				p_147756_3_, p_147756_4_, var7, 0);
		final float var24 = 0.03125F;
		final float var25 = 0.5F - var24 / 2.0F;
		final float var26 = var25 + var24;

		if (!var22 && !var21 && !var23 && !var20) {
			var22 = true;
			var23 = true;
		}

		if (var22) {
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var10, var12);
		}

		if (var22 || var23 && !var21 && !var20) {
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.25D, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var10, var12);
		}

		if (var23 || var22 && !var21 && !var20) {
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.5D, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var10, var12);
		}

		if (var23) {
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 1, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 1, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 0.75D, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + var26, p_147756_3_ + var18,
					p_147756_4_ + 1, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + var25, p_147756_3_ + var18,
					p_147756_4_ + 1, var10, var12);
		}

		if (var20) {
			var5.addVertexWithUV(p_147756_2_, p_147756_3_ + var18, p_147756_4_
					+ var26, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_, p_147756_3_ + var18, p_147756_4_
					+ var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_, p_147756_3_ + var18, p_147756_4_
					+ var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_, p_147756_3_ + var18, p_147756_4_
					+ var26, var10, var16);
		}

		if (var20 || var21 && !var22 && !var23) {
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var26, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + var18,
					p_147756_4_ + var26, var10, var16);
		}

		if (var21 || var20 && !var22 && !var23) {
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var26, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + var18,
					p_147756_4_ + var26, var10, var16);
		}

		if (var21) {
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var26, var10, var16);
			var5.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var25, var10, var12);
			var5.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + var18,
					p_147756_4_ + var25, var14, var12);
			var5.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + var18,
					p_147756_4_ + var26, var14, var16);
			var5.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + var18,
					p_147756_4_ + var26, var10, var16);
		}

		return true;
	}

	public boolean renderBlockTripWireSource(Block p_147723_1_,
			int p_147723_2_, int p_147723_3_, int p_147723_4_) {
		final Tessellator var5 = Tessellator.instance;
		final int var6 = blockAccess.getBlockMetadata(p_147723_2_, p_147723_3_,
				p_147723_4_);
		final int var7 = var6 & 3;
		final boolean var8 = (var6 & 4) == 4;
		final boolean var9 = (var6 & 8) == 8;
		final boolean var10 = !World.doesBlockHaveSolidTopSurface(blockAccess,
				p_147723_2_, p_147723_3_ - 1, p_147723_4_);
		final boolean var11 = hasOverrideBlockTexture();

		if (!var11) {
			setOverrideBlockTexture(this.getBlockIcon(Blocks.planks));
		}

		final float var12 = 0.25F;
		final float var13 = 0.125F;
		final float var14 = 0.125F;
		final float var15 = 0.3F - var12;
		final float var16 = 0.3F + var12;

		if (var7 == 2) {
			setRenderBounds(0.5F - var13, var15, 1.0F - var14, 0.5F + var13,
					var16, 1.0D);
		} else if (var7 == 0) {
			setRenderBounds(0.5F - var13, var15, 0.0D, 0.5F + var13, var16,
					var14);
		} else if (var7 == 1) {
			setRenderBounds(1.0F - var14, var15, 0.5F - var13, 1.0D, var16,
					0.5F + var13);
		} else if (var7 == 3) {
			setRenderBounds(0.0D, var15, 0.5F - var13, var14, var16,
					0.5F + var13);
		}

		renderStandardBlock(p_147723_1_, p_147723_2_, p_147723_3_, p_147723_4_);

		if (!var11) {
			clearOverrideBlockTexture();
		}

		var5.setBrightness(p_147723_1_.getBlockBrightness(blockAccess,
				p_147723_2_, p_147723_3_, p_147723_4_));
		var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		IIcon var17 = getBlockIconFromSide(p_147723_1_, 0);

		if (hasOverrideBlockTexture()) {
			var17 = overrideBlockTexture;
		}

		double var18 = var17.getMinU();
		double var20 = var17.getMinV();
		double var22 = var17.getMaxU();
		double var24 = var17.getMaxV();
		final Vec3[] var26 = new Vec3[8];
		final float var27 = 0.046875F;
		final float var28 = 0.046875F;
		final float var29 = 0.3125F;
		var26[0] = Vec3.createVectorHelper(-var27, 0.0D, -var28);
		var26[1] = Vec3.createVectorHelper(var27, 0.0D, -var28);
		var26[2] = Vec3.createVectorHelper(var27, 0.0D, var28);
		var26[3] = Vec3.createVectorHelper(-var27, 0.0D, var28);
		var26[4] = Vec3.createVectorHelper(-var27, var29, -var28);
		var26[5] = Vec3.createVectorHelper(var27, var29, -var28);
		var26[6] = Vec3.createVectorHelper(var27, var29, var28);
		var26[7] = Vec3.createVectorHelper(-var27, var29, var28);

		for (int var60 = 0; var60 < 8; ++var60) {
			var26[var60].zCoord += 0.0625D;

			if (var9) {
				var26[var60].rotateAroundX(0.5235988F);
				var26[var60].yCoord -= 0.4375D;
			} else if (var8) {
				var26[var60].rotateAroundX(0.08726647F);
				var26[var60].yCoord -= 0.4375D;
			} else {
				var26[var60].rotateAroundX(-((float) Math.PI * 2F / 9F));
				var26[var60].yCoord -= 0.375D;
			}

			var26[var60].rotateAroundX((float) Math.PI / 2F);

			if (var7 == 2) {
				var26[var60].rotateAroundY(0.0F);
			}

			if (var7 == 0) {
				var26[var60].rotateAroundY((float) Math.PI);
			}

			if (var7 == 1) {
				var26[var60].rotateAroundY((float) Math.PI / 2F);
			}

			if (var7 == 3) {
				var26[var60].rotateAroundY(-((float) Math.PI / 2F));
			}

			var26[var60].xCoord += p_147723_2_ + 0.5D;
			var26[var60].yCoord += p_147723_3_ + 0.3125F;
			var26[var60].zCoord += p_147723_4_ + 0.5D;
		}

		Vec3 var601 = null;
		Vec3 var31 = null;
		Vec3 var32 = null;
		Vec3 var33 = null;
		final byte var34 = 7;
		final byte var35 = 9;
		final byte var36 = 9;
		final byte var37 = 16;

		for (int var61 = 0; var61 < 6; ++var61) {
			if (var61 == 0) {
				var601 = var26[0];
				var31 = var26[1];
				var32 = var26[2];
				var33 = var26[3];
				var18 = var17.getInterpolatedU(var34);
				var20 = var17.getInterpolatedV(var36);
				var22 = var17.getInterpolatedU(var35);
				var24 = var17.getInterpolatedV(var36 + 2);
			} else if (var61 == 1) {
				var601 = var26[7];
				var31 = var26[6];
				var32 = var26[5];
				var33 = var26[4];
			} else if (var61 == 2) {
				var601 = var26[1];
				var31 = var26[0];
				var32 = var26[4];
				var33 = var26[5];
				var18 = var17.getInterpolatedU(var34);
				var20 = var17.getInterpolatedV(var36);
				var22 = var17.getInterpolatedU(var35);
				var24 = var17.getInterpolatedV(var37);
			} else if (var61 == 3) {
				var601 = var26[2];
				var31 = var26[1];
				var32 = var26[5];
				var33 = var26[6];
			} else if (var61 == 4) {
				var601 = var26[3];
				var31 = var26[2];
				var32 = var26[6];
				var33 = var26[7];
			} else if (var61 == 5) {
				var601 = var26[0];
				var31 = var26[3];
				var32 = var26[7];
				var33 = var26[4];
			}

			var5.addVertexWithUV(var601.xCoord, var601.yCoord, var601.zCoord,
					var18, var24);
			var5.addVertexWithUV(var31.xCoord, var31.yCoord, var31.zCoord,
					var22, var24);
			var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord,
					var22, var20);
			var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord,
					var18, var20);
		}

		final float var611 = 0.09375F;
		final float var39 = 0.09375F;
		final float var40 = 0.03125F;
		var26[0] = Vec3.createVectorHelper(-var611, 0.0D, -var39);
		var26[1] = Vec3.createVectorHelper(var611, 0.0D, -var39);
		var26[2] = Vec3.createVectorHelper(var611, 0.0D, var39);
		var26[3] = Vec3.createVectorHelper(-var611, 0.0D, var39);
		var26[4] = Vec3.createVectorHelper(-var611, var40, -var39);
		var26[5] = Vec3.createVectorHelper(var611, var40, -var39);
		var26[6] = Vec3.createVectorHelper(var611, var40, var39);
		var26[7] = Vec3.createVectorHelper(-var611, var40, var39);

		for (int var62 = 0; var62 < 8; ++var62) {
			var26[var62].zCoord += 0.21875D;

			if (var9) {
				var26[var62].yCoord -= 0.09375D;
				var26[var62].zCoord -= 0.1625D;
				var26[var62].rotateAroundX(0.0F);
			} else if (var8) {
				var26[var62].yCoord += 0.015625D;
				var26[var62].zCoord -= 0.171875D;
				var26[var62].rotateAroundX(0.17453294F);
			} else {
				var26[var62].rotateAroundX(0.87266463F);
			}

			if (var7 == 2) {
				var26[var62].rotateAroundY(0.0F);
			}

			if (var7 == 0) {
				var26[var62].rotateAroundY((float) Math.PI);
			}

			if (var7 == 1) {
				var26[var62].rotateAroundY((float) Math.PI / 2F);
			}

			if (var7 == 3) {
				var26[var62].rotateAroundY(-((float) Math.PI / 2F));
			}

			var26[var62].xCoord += p_147723_2_ + 0.5D;
			var26[var62].yCoord += p_147723_3_ + 0.3125F;
			var26[var62].zCoord += p_147723_4_ + 0.5D;
		}

		final byte var621 = 5;
		final byte var42 = 11;
		final byte var43 = 3;
		final byte var44 = 9;

		for (int var63 = 0; var63 < 6; ++var63) {
			if (var63 == 0) {
				var601 = var26[0];
				var31 = var26[1];
				var32 = var26[2];
				var33 = var26[3];
				var18 = var17.getInterpolatedU(var621);
				var20 = var17.getInterpolatedV(var43);
				var22 = var17.getInterpolatedU(var42);
				var24 = var17.getInterpolatedV(var44);
			} else if (var63 == 1) {
				var601 = var26[7];
				var31 = var26[6];
				var32 = var26[5];
				var33 = var26[4];
			} else if (var63 == 2) {
				var601 = var26[1];
				var31 = var26[0];
				var32 = var26[4];
				var33 = var26[5];
				var18 = var17.getInterpolatedU(var621);
				var20 = var17.getInterpolatedV(var43);
				var22 = var17.getInterpolatedU(var42);
				var24 = var17.getInterpolatedV(var43 + 2);
			} else if (var63 == 3) {
				var601 = var26[2];
				var31 = var26[1];
				var32 = var26[5];
				var33 = var26[6];
			} else if (var63 == 4) {
				var601 = var26[3];
				var31 = var26[2];
				var32 = var26[6];
				var33 = var26[7];
			} else if (var63 == 5) {
				var601 = var26[0];
				var31 = var26[3];
				var32 = var26[7];
				var33 = var26[4];
			}

			var5.addVertexWithUV(var601.xCoord, var601.yCoord, var601.zCoord,
					var18, var24);
			var5.addVertexWithUV(var31.xCoord, var31.yCoord, var31.zCoord,
					var22, var24);
			var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord,
					var22, var20);
			var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord,
					var18, var20);
		}

		if (var8) {
			final double var631 = var26[0].yCoord;
			final float var47 = 0.03125F;
			final float var48 = 0.5F - var47 / 2.0F;
			final float var49 = var48 + var47;
			final double var50 = var17.getMinU();
			final double var52 = var17.getInterpolatedV(var8 ? 2.0D : 0.0D);
			final double var54 = var17.getMaxU();
			final double var56 = var17.getInterpolatedV(var8 ? 4.0D : 2.0D);
			final double var58 = (var10 ? 3.5F : 1.5F) / 16.0D;
			var5.setColorOpaque_F(0.75F, 0.75F, 0.75F);

			if (var7 == 2) {
				var5.addVertexWithUV(p_147723_2_ + var48, p_147723_3_ + var58,
						p_147723_4_ + 0.25D, var50, var52);
				var5.addVertexWithUV(p_147723_2_ + var49, p_147723_3_ + var58,
						p_147723_4_ + 0.25D, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + var49, p_147723_3_ + var58,
						p_147723_4_, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + var48, p_147723_3_ + var58,
						p_147723_4_, var54, var52);
				var5.addVertexWithUV(p_147723_2_ + var48, var631,
						p_147723_4_ + 0.5D, var50, var52);
				var5.addVertexWithUV(p_147723_2_ + var49, var631,
						p_147723_4_ + 0.5D, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + var49, p_147723_3_ + var58,
						p_147723_4_ + 0.25D, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + var48, p_147723_3_ + var58,
						p_147723_4_ + 0.25D, var54, var52);
			} else if (var7 == 0) {
				var5.addVertexWithUV(p_147723_2_ + var48, p_147723_3_ + var58,
						p_147723_4_ + 0.75D, var50, var52);
				var5.addVertexWithUV(p_147723_2_ + var49, p_147723_3_ + var58,
						p_147723_4_ + 0.75D, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + var49, var631,
						p_147723_4_ + 0.5D, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + var48, var631,
						p_147723_4_ + 0.5D, var54, var52);
				var5.addVertexWithUV(p_147723_2_ + var48, p_147723_3_ + var58,
						p_147723_4_ + 1, var50, var52);
				var5.addVertexWithUV(p_147723_2_ + var49, p_147723_3_ + var58,
						p_147723_4_ + 1, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + var49, p_147723_3_ + var58,
						p_147723_4_ + 0.75D, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + var48, p_147723_3_ + var58,
						p_147723_4_ + 0.75D, var54, var52);
			} else if (var7 == 1) {
				var5.addVertexWithUV(p_147723_2_, p_147723_3_ + var58,
						p_147723_4_ + var49, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + var58,
						p_147723_4_ + var49, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + var58,
						p_147723_4_ + var48, var54, var52);
				var5.addVertexWithUV(p_147723_2_, p_147723_3_ + var58,
						p_147723_4_ + var48, var50, var52);
				var5.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + var58,
						p_147723_4_ + var49, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + 0.5D, var631, p_147723_4_
						+ var49, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + 0.5D, var631, p_147723_4_
						+ var48, var54, var52);
				var5.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + var58,
						p_147723_4_ + var48, var50, var52);
			} else {
				var5.addVertexWithUV(p_147723_2_ + 0.5D, var631, p_147723_4_
						+ var49, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + var58,
						p_147723_4_ + var49, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + var58,
						p_147723_4_ + var48, var54, var52);
				var5.addVertexWithUV(p_147723_2_ + 0.5D, var631, p_147723_4_
						+ var48, var50, var52);
				var5.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + var58,
						p_147723_4_ + var49, var50, var56);
				var5.addVertexWithUV(p_147723_2_ + 1, p_147723_3_ + var58,
						p_147723_4_ + var49, var54, var56);
				var5.addVertexWithUV(p_147723_2_ + 1, p_147723_3_ + var58,
						p_147723_4_ + var48, var54, var52);
				var5.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + var58,
						p_147723_4_ + var48, var50, var52);
			}
		}

		return true;
	}

	/**
	 * Renders a block using the given texture instead of the block's own
	 * default texture
	 */
	public void renderBlockUsingTexture(Block p_147792_1_, int p_147792_2_,
			int p_147792_3_, int p_147792_4_, IIcon p_147792_5_) {
		setOverrideBlockTexture(p_147792_5_);
		renderBlockByRenderType(p_147792_1_, p_147792_2_, p_147792_3_,
				p_147792_4_);
		clearOverrideBlockTexture();
	}

	public boolean renderBlockVine(Block p_147726_1_, int p_147726_2_,
			int p_147726_3_, int p_147726_4_) {
		final Tessellator var5 = Tessellator.instance;
		IIcon var6 = getBlockIconFromSide(p_147726_1_, 0);

		if (hasOverrideBlockTexture()) {
			var6 = overrideBlockTexture;
		}

		final int var17 = blockAccess.getBlockMetadata(p_147726_2_,
				p_147726_3_, p_147726_4_);

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			byte var7 = 0;

			if ((var17 & 1) != 0) {
				var7 = 2;
			} else if ((var17 & 2) != 0) {
				var7 = 5;
			} else if ((var17 & 4) != 0) {
				var7 = 3;
			} else if ((var17 & 8) != 0) {
				var7 = 4;
			}

			var6 = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147726_1_, p_147726_2_, p_147726_3_, p_147726_4_, var7,
					var6);
		}

		var5.setBrightness(p_147726_1_.getBlockBrightness(blockAccess,
				p_147726_2_, p_147726_3_, p_147726_4_));
		final int var71 = CustomColorizer.getColorMultiplier(p_147726_1_,
				blockAccess, p_147726_2_, p_147726_3_, p_147726_4_);
		final float var8 = (var71 >> 16 & 255) / 255.0F;
		final float var9 = (var71 >> 8 & 255) / 255.0F;
		final float var10 = (var71 & 255) / 255.0F;
		var5.setColorOpaque_F(var8, var9, var10);
		final double var18 = var6.getMinU();
		final double var19 = var6.getMinV();
		final double var11 = var6.getMaxU();
		final double var13 = var6.getMaxV();
		final double var15 = 0.05000000074505806D;

		if ((var17 & 2) != 0) {
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 1,
					p_147726_4_ + 1, var18, var19);
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 0,
					p_147726_4_ + 1, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 0,
					p_147726_4_ + 0, var11, var13);
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 1,
					p_147726_4_ + 0, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 1,
					p_147726_4_ + 0, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 0,
					p_147726_4_ + 0, var11, var13);
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 0,
					p_147726_4_ + 1, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + var15, p_147726_3_ + 1,
					p_147726_4_ + 1, var18, var19);
		}

		if ((var17 & 8) != 0) {
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 0,
					p_147726_4_ + 1, var11, var13);
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 1,
					p_147726_4_ + 1, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 1,
					p_147726_4_ + 0, var18, var19);
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 0,
					p_147726_4_ + 0, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 0,
					p_147726_4_ + 0, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 1,
					p_147726_4_ + 0, var18, var19);
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 1,
					p_147726_4_ + 1, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + 1 - var15, p_147726_3_ + 0,
					p_147726_4_ + 1, var11, var13);
		}

		if ((var17 & 4) != 0) {
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_
					+ var15, var11, var13);
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_
					+ var15, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_
					+ var15, var18, var19);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_
					+ var15, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_
					+ var15, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_
					+ var15, var18, var19);
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_
					+ var15, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_
					+ var15, var11, var13);
		}

		if ((var17 & 1) != 0) {
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_
					+ 1 - var15, var18, var19);
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_
					+ 1 - var15, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_
					+ 1 - var15, var11, var13);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_
					+ 1 - var15, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_
					+ 1 - var15, var11, var19);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_
					+ 1 - var15, var11, var13);
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_
					+ 1 - var15, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_
					+ 1 - var15, var18, var19);
		}

		if (blockAccess.getBlock(p_147726_2_, p_147726_3_ + 1, p_147726_4_)
				.isBlockNormalCube()) {
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1 - var15,
					p_147726_4_ + 0, var18, var19);
			var5.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1 - var15,
					p_147726_4_ + 1, var18, var13);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1 - var15,
					p_147726_4_ + 1, var11, var13);
			var5.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1 - var15,
					p_147726_4_ + 0, var11, var19);
		}

		return true;
	}

	public boolean renderBlockWall(BlockWall p_147807_1_, int p_147807_2_,
			int p_147807_3_, int p_147807_4_) {
		final boolean var5 = p_147807_1_.func_150091_e(blockAccess,
				p_147807_2_ - 1, p_147807_3_, p_147807_4_);
		final boolean var6 = p_147807_1_.func_150091_e(blockAccess,
				p_147807_2_ + 1, p_147807_3_, p_147807_4_);
		final boolean var7 = p_147807_1_.func_150091_e(blockAccess,
				p_147807_2_, p_147807_3_, p_147807_4_ - 1);
		final boolean var8 = p_147807_1_.func_150091_e(blockAccess,
				p_147807_2_, p_147807_3_, p_147807_4_ + 1);
		final boolean var9 = var7 && var8 && !var5 && !var6;
		final boolean var10 = !var7 && !var8 && var5 && var6;
		final boolean var11 = blockAccess.isAirBlock(p_147807_2_,
				p_147807_3_ + 1, p_147807_4_);

		if ((var9 || var10) && var11) {
			if (var9) {
				setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 1.0D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_,
						p_147807_4_);
			} else {
				setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_,
						p_147807_4_);
			}
		} else {
			setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
			renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_,
					p_147807_4_);

			if (var5) {
				setRenderBounds(0.0D, 0.0D, 0.3125D, 0.25D, 0.8125D, 0.6875D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_,
						p_147807_4_);
			}

			if (var6) {
				setRenderBounds(0.75D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_,
						p_147807_4_);
			}

			if (var7) {
				setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 0.25D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_,
						p_147807_4_);
			}

			if (var8) {
				setRenderBounds(0.3125D, 0.0D, 0.75D, 0.6875D, 0.8125D, 1.0D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_,
						p_147807_4_);
			}
		}

		p_147807_1_.setBlockBoundsBasedOnState(blockAccess, p_147807_2_,
				p_147807_3_, p_147807_4_);

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147807_2_, p_147807_3_, p_147807_4_)) {
			renderSnow(p_147807_2_, p_147807_3_, p_147807_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		return true;
	}

	public boolean renderCrossedSquares(Block p_147746_1_, int p_147746_2_,
			int p_147746_3_, int p_147746_4_) {
		final Tessellator var5 = Tessellator.instance;
		var5.setBrightness(p_147746_1_.getBlockBrightness(blockAccess,
				p_147746_2_, p_147746_3_, p_147746_4_));
		final int var6 = CustomColorizer.getColorMultiplier(p_147746_1_,
				blockAccess, p_147746_2_, p_147746_3_, p_147746_4_);
		float var7 = (var6 >> 16 & 255) / 255.0F;
		float var8 = (var6 >> 8 & 255) / 255.0F;
		float var9 = (var6 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var18 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
			final float var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
			final float var19 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
			var7 = var18;
			var8 = var11;
			var9 = var19;
		}

		var5.setColorOpaque_F(var7, var8, var9);
		double var181 = p_147746_2_;
		double var191 = p_147746_3_;
		double var14 = p_147746_4_;
		long var16;

		if (p_147746_1_ == Blocks.tallgrass) {
			var16 = p_147746_2_ * 3129871 ^ p_147746_4_ * 116129781L
					^ p_147746_3_;
			var16 = var16 * var16 * 42317861L + var16 * 11L;
			var181 += ((var16 >> 16 & 15L) / 15.0F - 0.5D) * 0.5D;
			var191 += ((var16 >> 20 & 15L) / 15.0F - 1.0D) * 0.2D;
			var14 += ((var16 >> 24 & 15L) / 15.0F - 0.5D) * 0.5D;
		} else if (p_147746_1_ == Blocks.red_flower
				|| p_147746_1_ == Blocks.yellow_flower) {
			var16 = p_147746_2_ * 3129871 ^ p_147746_4_ * 116129781L
					^ p_147746_3_;
			var16 = var16 * var16 * 42317861L + var16 * 11L;
			var181 += ((var16 >> 16 & 15L) / 15.0F - 0.5D) * 0.3D;
			var14 += ((var16 >> 24 & 15L) / 15.0F - 0.5D) * 0.3D;
		}

		IIcon var20 = getBlockIconFromSideAndMetadata(p_147746_1_, 0,
				blockAccess.getBlockMetadata(p_147746_2_, p_147746_3_,
						p_147746_4_));

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			var20 = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147746_1_, p_147746_2_, p_147746_3_, p_147746_4_, 2,
					var20);
		}

		drawCrossedSquares(var20, var181, var191, var14, 1.0F);

		if (Config.isBetterSnow()
				&& hasSnowNeighbours(p_147746_2_, p_147746_3_, p_147746_4_)) {
			renderSnow(p_147746_2_, p_147746_3_, p_147746_4_,
					Blocks.snow_layer.getBlockBoundsMaxY());
		}

		return true;
	}

	public void renderFaceXNeg(Block p_147798_1_, double p_147798_2_,
			double p_147798_4_, double p_147798_6_, IIcon p_147798_8_) {
		final Tessellator var9 = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147798_8_ = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			p_147798_8_ = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147798_1_, (int) p_147798_2_, (int) p_147798_4_,
					(int) p_147798_6_, 4, p_147798_8_);
		}

		boolean uvRotateSet = false;

		if (Config.isNaturalTextures() && overrideBlockTexture == null
				&& uvRotateNorth == 0) {
			final NaturalProperties var10 = NaturalTextures
					.getNaturalProperties(p_147798_8_);

			if (var10 != null) {
				final int rand = Config.getRandom((int) p_147798_2_,
						(int) p_147798_4_, (int) p_147798_6_, 4);

				if (var10.rotation > 1) {
					uvRotateNorth = rand & 3;
				}

				if (var10.rotation == 2) {
					uvRotateNorth = uvRotateNorth / 2 * 3;
				}

				if (var10.flip) {
					flipTexture = (rand & 4) != 0;
				}

				uvRotateSet = true;
			}
		}

		double var101 = p_147798_8_.getInterpolatedU(renderMinZ * 16.0D);
		double var12 = p_147798_8_.getInterpolatedU(renderMaxZ * 16.0D);
		double var14 = p_147798_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double var16 = p_147798_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double var18;

		if (flipTexture) {
			var18 = var101;
			var101 = var12;
			var12 = var18;
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			var101 = p_147798_8_.getMinU();
			var12 = p_147798_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			var14 = p_147798_8_.getMinV();
			var16 = p_147798_8_.getMaxV();
		}

		var18 = var12;
		double var20 = var101;
		double var22 = var14;
		double var24 = var16;
		double var26;

		if (uvRotateNorth == 1) {
			var101 = p_147798_8_.getInterpolatedU(renderMinY * 16.0D);
			var14 = p_147798_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
			var12 = p_147798_8_.getInterpolatedU(renderMaxY * 16.0D);
			var16 = p_147798_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var22 = var14;
			var24 = var16;
			var18 = var101;
			var20 = var12;
			var14 = var16;
			var16 = var22;
		} else if (uvRotateNorth == 2) {
			var101 = p_147798_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			var14 = p_147798_8_.getInterpolatedV(renderMinZ * 16.0D);
			var12 = p_147798_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			var16 = p_147798_8_.getInterpolatedV(renderMaxZ * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var101 = var12;
			var12 = var20;
			var22 = var16;
			var24 = var14;
		} else if (uvRotateNorth == 3) {
			var101 = p_147798_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			var12 = p_147798_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			var14 = p_147798_8_.getInterpolatedV(renderMaxY * 16.0D);
			var16 = p_147798_8_.getInterpolatedV(renderMinY * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var22 = var14;
			var24 = var16;
		}

		if (uvRotateSet) {
			uvRotateNorth = 0;
			flipTexture = false;
		}

		var26 = p_147798_2_ + renderMinX;
		final double var28 = p_147798_4_ + renderMinY;
		final double var30 = p_147798_4_ + renderMaxY;
		double var32 = p_147798_6_ + renderMinZ;
		double var34 = p_147798_6_ + renderMaxZ;

		if (renderFromInside) {
			var32 = p_147798_6_ + renderMaxZ;
			var34 = p_147798_6_ + renderMinZ;
		}

		if (enableAO) {
			var9.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft,
					colorBlueTopLeft);
			var9.setBrightness(brightnessTopLeft);
			var9.addVertexWithUV(var26, var30, var34, var18, var22);
			var9.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft,
					colorBlueBottomLeft);
			var9.setBrightness(brightnessBottomLeft);
			var9.addVertexWithUV(var26, var30, var32, var101, var14);
			var9.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight,
					colorBlueBottomRight);
			var9.setBrightness(brightnessBottomRight);
			var9.addVertexWithUV(var26, var28, var32, var20, var24);
			var9.setColorOpaque_F(colorRedTopRight, colorGreenTopRight,
					colorBlueTopRight);
			var9.setBrightness(brightnessTopRight);
			var9.addVertexWithUV(var26, var28, var34, var12, var16);
		} else {
			var9.addVertexWithUV(var26, var30, var34, var18, var22);
			var9.addVertexWithUV(var26, var30, var32, var101, var14);
			var9.addVertexWithUV(var26, var28, var32, var20, var24);
			var9.addVertexWithUV(var26, var28, var34, var12, var16);
		}
	}

	public void renderFaceXPos(Block p_147764_1_, double p_147764_2_,
			double p_147764_4_, double p_147764_6_, IIcon p_147764_8_) {
		final Tessellator var9 = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147764_8_ = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			p_147764_8_ = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147764_1_, (int) p_147764_2_, (int) p_147764_4_,
					(int) p_147764_6_, 5, p_147764_8_);
		}

		boolean uvRotateSet = false;

		if (Config.isNaturalTextures() && overrideBlockTexture == null
				&& uvRotateSouth == 0) {
			final NaturalProperties var10 = NaturalTextures
					.getNaturalProperties(p_147764_8_);

			if (var10 != null) {
				final int rand = Config.getRandom((int) p_147764_2_,
						(int) p_147764_4_, (int) p_147764_6_, 5);

				if (var10.rotation > 1) {
					uvRotateSouth = rand & 3;
				}

				if (var10.rotation == 2) {
					uvRotateSouth = uvRotateSouth / 2 * 3;
				}

				if (var10.flip) {
					flipTexture = (rand & 4) != 0;
				}

				uvRotateSet = true;
			}
		}

		double var101 = p_147764_8_.getInterpolatedU(renderMinZ * 16.0D);
		double var12 = p_147764_8_.getInterpolatedU(renderMaxZ * 16.0D);

		if (field_152631_f) {
			var12 = p_147764_8_.getInterpolatedU((1.0D - renderMinZ) * 16.0D);
			var101 = p_147764_8_.getInterpolatedU((1.0D - renderMaxZ) * 16.0D);
		}

		double var14 = p_147764_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double var16 = p_147764_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double var18;

		if (flipTexture) {
			var18 = var101;
			var101 = var12;
			var12 = var18;
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			var101 = p_147764_8_.getMinU();
			var12 = p_147764_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			var14 = p_147764_8_.getMinV();
			var16 = p_147764_8_.getMaxV();
		}

		var18 = var12;
		double var20 = var101;
		double var22 = var14;
		double var24 = var16;
		double var26;

		if (uvRotateSouth == 2) {
			var101 = p_147764_8_.getInterpolatedU(renderMinY * 16.0D);
			var14 = p_147764_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);
			var12 = p_147764_8_.getInterpolatedU(renderMaxY * 16.0D);
			var16 = p_147764_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var22 = var14;
			var24 = var16;
			var18 = var101;
			var20 = var12;
			var14 = var16;
			var16 = var22;
		} else if (uvRotateSouth == 1) {
			var101 = p_147764_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			var14 = p_147764_8_.getInterpolatedV(renderMaxZ * 16.0D);
			var12 = p_147764_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			var16 = p_147764_8_.getInterpolatedV(renderMinZ * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var101 = var12;
			var12 = var20;
			var22 = var16;
			var24 = var14;
		} else if (uvRotateSouth == 3) {
			var101 = p_147764_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			var12 = p_147764_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			var14 = p_147764_8_.getInterpolatedV(renderMaxY * 16.0D);
			var16 = p_147764_8_.getInterpolatedV(renderMinY * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var22 = var14;
			var24 = var16;
		}

		if (uvRotateSet) {
			uvRotateSouth = 0;
			flipTexture = false;
		}

		var26 = p_147764_2_ + renderMaxX;
		final double var28 = p_147764_4_ + renderMinY;
		final double var30 = p_147764_4_ + renderMaxY;
		double var32 = p_147764_6_ + renderMinZ;
		double var34 = p_147764_6_ + renderMaxZ;

		if (renderFromInside) {
			var32 = p_147764_6_ + renderMaxZ;
			var34 = p_147764_6_ + renderMinZ;
		}

		if (enableAO) {
			var9.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft,
					colorBlueTopLeft);
			var9.setBrightness(brightnessTopLeft);
			var9.addVertexWithUV(var26, var28, var34, var20, var24);
			var9.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft,
					colorBlueBottomLeft);
			var9.setBrightness(brightnessBottomLeft);
			var9.addVertexWithUV(var26, var28, var32, var12, var16);
			var9.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight,
					colorBlueBottomRight);
			var9.setBrightness(brightnessBottomRight);
			var9.addVertexWithUV(var26, var30, var32, var18, var22);
			var9.setColorOpaque_F(colorRedTopRight, colorGreenTopRight,
					colorBlueTopRight);
			var9.setBrightness(brightnessTopRight);
			var9.addVertexWithUV(var26, var30, var34, var101, var14);
		} else {
			var9.addVertexWithUV(var26, var28, var34, var20, var24);
			var9.addVertexWithUV(var26, var28, var32, var12, var16);
			var9.addVertexWithUV(var26, var30, var32, var18, var22);
			var9.addVertexWithUV(var26, var30, var34, var101, var14);
		}
	}

	public void renderFaceYNeg(Block p_147768_1_, double p_147768_2_,
			double p_147768_4_, double p_147768_6_, IIcon p_147768_8_) {
		final Tessellator var9 = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147768_8_ = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			p_147768_8_ = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147768_1_, (int) p_147768_2_, (int) p_147768_4_,
					(int) p_147768_6_, 0, p_147768_8_);
		}

		boolean uvRotateSet = false;

		if (Config.isNaturalTextures() && overrideBlockTexture == null
				&& uvRotateBottom == 0) {
			final NaturalProperties var10 = NaturalTextures
					.getNaturalProperties(p_147768_8_);

			if (var10 != null) {
				final int rand = Config.getRandom((int) p_147768_2_,
						(int) p_147768_4_, (int) p_147768_6_, 0);

				if (var10.rotation > 1) {
					uvRotateBottom = rand & 3;
				}

				if (var10.rotation == 2) {
					uvRotateBottom = uvRotateBottom / 2 * 3;
				}

				if (var10.flip) {
					flipTexture = (rand & 4) != 0;
				}

				uvRotateSet = true;
			}
		}

		double var101 = p_147768_8_.getInterpolatedU(renderMinX * 16.0D);
		double var12 = p_147768_8_.getInterpolatedU(renderMaxX * 16.0D);
		double var14 = p_147768_8_.getInterpolatedV(renderMinZ * 16.0D);
		double var16 = p_147768_8_.getInterpolatedV(renderMaxZ * 16.0D);

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			var101 = p_147768_8_.getMinU();
			var12 = p_147768_8_.getMaxU();
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			var14 = p_147768_8_.getMinV();
			var16 = p_147768_8_.getMaxV();
		}

		double var18;

		if (flipTexture) {
			var18 = var101;
			var101 = var12;
			var12 = var18;
		}

		var18 = var12;
		double var20 = var101;
		double var22 = var14;
		double var24 = var16;
		double var26;

		if (uvRotateBottom == 2) {
			var101 = p_147768_8_.getInterpolatedU(renderMinZ * 16.0D);
			var14 = p_147768_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);
			var12 = p_147768_8_.getInterpolatedU(renderMaxZ * 16.0D);
			var16 = p_147768_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var22 = var14;
			var24 = var16;
			var18 = var101;
			var20 = var12;
			var14 = var16;
			var16 = var22;
		} else if (uvRotateBottom == 1) {
			var101 = p_147768_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			var14 = p_147768_8_.getInterpolatedV(renderMinX * 16.0D);
			var12 = p_147768_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			var16 = p_147768_8_.getInterpolatedV(renderMaxX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var101 = var12;
			var12 = var20;
			var22 = var16;
			var24 = var14;
		} else if (uvRotateBottom == 3) {
			var101 = p_147768_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			var12 = p_147768_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			var14 = p_147768_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);
			var16 = p_147768_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var22 = var14;
			var24 = var16;
		}

		if (uvRotateSet) {
			uvRotateBottom = 0;
			flipTexture = false;
		}

		var26 = p_147768_2_ + renderMinX;
		double var28 = p_147768_2_ + renderMaxX;
		final double var30 = p_147768_4_ + renderMinY;
		final double var32 = p_147768_6_ + renderMinZ;
		final double var34 = p_147768_6_ + renderMaxZ;

		if (renderFromInside) {
			var26 = p_147768_2_ + renderMaxX;
			var28 = p_147768_2_ + renderMinX;
		}

		if (enableAO) {
			var9.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft,
					colorBlueTopLeft);
			var9.setBrightness(brightnessTopLeft);
			var9.addVertexWithUV(var26, var30, var34, var20, var24);
			var9.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft,
					colorBlueBottomLeft);
			var9.setBrightness(brightnessBottomLeft);
			var9.addVertexWithUV(var26, var30, var32, var101, var14);
			var9.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight,
					colorBlueBottomRight);
			var9.setBrightness(brightnessBottomRight);
			var9.addVertexWithUV(var28, var30, var32, var18, var22);
			var9.setColorOpaque_F(colorRedTopRight, colorGreenTopRight,
					colorBlueTopRight);
			var9.setBrightness(brightnessTopRight);
			var9.addVertexWithUV(var28, var30, var34, var12, var16);
		} else {
			var9.addVertexWithUV(var26, var30, var34, var20, var24);
			var9.addVertexWithUV(var26, var30, var32, var101, var14);
			var9.addVertexWithUV(var28, var30, var32, var18, var22);
			var9.addVertexWithUV(var28, var30, var34, var12, var16);
		}
	}

	public void renderFaceYPos(Block p_147806_1_, double p_147806_2_,
			double p_147806_4_, double p_147806_6_, IIcon p_147806_8_) {
		final Tessellator var9 = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147806_8_ = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			p_147806_8_ = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147806_1_, (int) p_147806_2_, (int) p_147806_4_,
					(int) p_147806_6_, 1, p_147806_8_);
		}

		boolean uvRotateSet = false;

		if (Config.isNaturalTextures() && overrideBlockTexture == null
				&& uvRotateTop == 0) {
			final NaturalProperties var10 = NaturalTextures
					.getNaturalProperties(p_147806_8_);

			if (var10 != null) {
				final int rand = Config.getRandom((int) p_147806_2_,
						(int) p_147806_4_, (int) p_147806_6_, 1);

				if (var10.rotation > 1) {
					uvRotateTop = rand & 3;
				}

				if (var10.rotation == 2) {
					uvRotateTop = uvRotateTop / 2 * 3;
				}

				if (var10.flip) {
					flipTexture = (rand & 4) != 0;
				}

				uvRotateSet = true;
			}
		}

		double var101 = p_147806_8_.getInterpolatedU(renderMinX * 16.0D);
		double var12 = p_147806_8_.getInterpolatedU(renderMaxX * 16.0D);
		double var14 = p_147806_8_.getInterpolatedV(renderMinZ * 16.0D);
		double var16 = p_147806_8_.getInterpolatedV(renderMaxZ * 16.0D);
		double var18;

		if (flipTexture) {
			var18 = var101;
			var101 = var12;
			var12 = var18;
		}

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			var101 = p_147806_8_.getMinU();
			var12 = p_147806_8_.getMaxU();
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			var14 = p_147806_8_.getMinV();
			var16 = p_147806_8_.getMaxV();
		}

		var18 = var12;
		double var20 = var101;
		double var22 = var14;
		double var24 = var16;
		double var26;

		if (uvRotateTop == 1) {
			var101 = p_147806_8_.getInterpolatedU(renderMinZ * 16.0D);
			var14 = p_147806_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);
			var12 = p_147806_8_.getInterpolatedU(renderMaxZ * 16.0D);
			var16 = p_147806_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var22 = var14;
			var24 = var16;
			var18 = var101;
			var20 = var12;
			var14 = var16;
			var16 = var22;
		} else if (uvRotateTop == 2) {
			var101 = p_147806_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			var14 = p_147806_8_.getInterpolatedV(renderMinX * 16.0D);
			var12 = p_147806_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			var16 = p_147806_8_.getInterpolatedV(renderMaxX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var101 = var12;
			var12 = var20;
			var22 = var16;
			var24 = var14;
		} else if (uvRotateTop == 3) {
			var101 = p_147806_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			var12 = p_147806_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			var14 = p_147806_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);
			var16 = p_147806_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var22 = var14;
			var24 = var16;
		}

		if (uvRotateSet) {
			uvRotateTop = 0;
			flipTexture = false;
		}

		var26 = p_147806_2_ + renderMinX;
		double var28 = p_147806_2_ + renderMaxX;
		final double var30 = p_147806_4_ + renderMaxY;
		final double var32 = p_147806_6_ + renderMinZ;
		final double var34 = p_147806_6_ + renderMaxZ;

		if (renderFromInside) {
			var26 = p_147806_2_ + renderMaxX;
			var28 = p_147806_2_ + renderMinX;
		}

		if (enableAO) {
			var9.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft,
					colorBlueTopLeft);
			var9.setBrightness(brightnessTopLeft);
			var9.addVertexWithUV(var28, var30, var34, var12, var16);
			var9.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft,
					colorBlueBottomLeft);
			var9.setBrightness(brightnessBottomLeft);
			var9.addVertexWithUV(var28, var30, var32, var18, var22);
			var9.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight,
					colorBlueBottomRight);
			var9.setBrightness(brightnessBottomRight);
			var9.addVertexWithUV(var26, var30, var32, var101, var14);
			var9.setColorOpaque_F(colorRedTopRight, colorGreenTopRight,
					colorBlueTopRight);
			var9.setBrightness(brightnessTopRight);
			var9.addVertexWithUV(var26, var30, var34, var20, var24);
		} else {
			var9.addVertexWithUV(var28, var30, var34, var12, var16);
			var9.addVertexWithUV(var28, var30, var32, var18, var22);
			var9.addVertexWithUV(var26, var30, var32, var101, var14);
			var9.addVertexWithUV(var26, var30, var34, var20, var24);
		}
	}

	public void renderFaceZNeg(Block p_147761_1_, double p_147761_2_,
			double p_147761_4_, double p_147761_6_, IIcon p_147761_8_) {
		final Tessellator var9 = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147761_8_ = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			p_147761_8_ = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147761_1_, (int) p_147761_2_, (int) p_147761_4_,
					(int) p_147761_6_, 2, p_147761_8_);
		}

		boolean uvRotateSet = false;

		if (Config.isNaturalTextures() && overrideBlockTexture == null
				&& uvRotateEast == 0) {
			final NaturalProperties var10 = NaturalTextures
					.getNaturalProperties(p_147761_8_);

			if (var10 != null) {
				final int rand = Config.getRandom((int) p_147761_2_,
						(int) p_147761_4_, (int) p_147761_6_, 2);

				if (var10.rotation > 1) {
					uvRotateEast = rand & 3;
				}

				if (var10.rotation == 2) {
					uvRotateEast = uvRotateEast / 2 * 3;
				}

				if (var10.flip) {
					flipTexture = (rand & 4) != 0;
				}

				uvRotateSet = true;
			}
		}

		double var101 = p_147761_8_.getInterpolatedU(renderMinX * 16.0D);
		double var12 = p_147761_8_.getInterpolatedU(renderMaxX * 16.0D);

		if (field_152631_f) {
			var12 = p_147761_8_.getInterpolatedU((1.0D - renderMinX) * 16.0D);
			var101 = p_147761_8_.getInterpolatedU((1.0D - renderMaxX) * 16.0D);
		}

		double var14 = p_147761_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double var16 = p_147761_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double var18;

		if (flipTexture) {
			var18 = var101;
			var101 = var12;
			var12 = var18;
		}

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			var101 = p_147761_8_.getMinU();
			var12 = p_147761_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			var14 = p_147761_8_.getMinV();
			var16 = p_147761_8_.getMaxV();
		}

		var18 = var12;
		double var20 = var101;
		double var22 = var14;
		double var24 = var16;
		double var26;

		if (uvRotateEast == 2) {
			var101 = p_147761_8_.getInterpolatedU(renderMinY * 16.0D);
			var12 = p_147761_8_.getInterpolatedU(renderMaxY * 16.0D);
			var14 = p_147761_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);
			var16 = p_147761_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var22 = var14;
			var24 = var16;
			var18 = var101;
			var20 = var12;
			var14 = var16;
			var16 = var22;
		} else if (uvRotateEast == 1) {
			var101 = p_147761_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			var12 = p_147761_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			var14 = p_147761_8_.getInterpolatedV(renderMaxX * 16.0D);
			var16 = p_147761_8_.getInterpolatedV(renderMinX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var101 = var12;
			var12 = var20;
			var22 = var16;
			var24 = var14;
		} else if (uvRotateEast == 3) {
			var101 = p_147761_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			var12 = p_147761_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			var14 = p_147761_8_.getInterpolatedV(renderMaxY * 16.0D);
			var16 = p_147761_8_.getInterpolatedV(renderMinY * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var22 = var14;
			var24 = var16;
		}

		if (uvRotateSet) {
			uvRotateEast = 0;
			flipTexture = false;
		}

		var26 = p_147761_2_ + renderMinX;
		double var28 = p_147761_2_ + renderMaxX;
		final double var30 = p_147761_4_ + renderMinY;
		final double var32 = p_147761_4_ + renderMaxY;
		final double var34 = p_147761_6_ + renderMinZ;

		if (renderFromInside) {
			var26 = p_147761_2_ + renderMaxX;
			var28 = p_147761_2_ + renderMinX;
		}

		if (enableAO) {
			var9.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft,
					colorBlueTopLeft);
			var9.setBrightness(brightnessTopLeft);
			var9.addVertexWithUV(var26, var32, var34, var18, var22);
			var9.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft,
					colorBlueBottomLeft);
			var9.setBrightness(brightnessBottomLeft);
			var9.addVertexWithUV(var28, var32, var34, var101, var14);
			var9.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight,
					colorBlueBottomRight);
			var9.setBrightness(brightnessBottomRight);
			var9.addVertexWithUV(var28, var30, var34, var20, var24);
			var9.setColorOpaque_F(colorRedTopRight, colorGreenTopRight,
					colorBlueTopRight);
			var9.setBrightness(brightnessTopRight);
			var9.addVertexWithUV(var26, var30, var34, var12, var16);
		} else {
			var9.addVertexWithUV(var26, var32, var34, var18, var22);
			var9.addVertexWithUV(var28, var32, var34, var101, var14);
			var9.addVertexWithUV(var28, var30, var34, var20, var24);
			var9.addVertexWithUV(var26, var30, var34, var12, var16);
		}
	}

	public void renderFaceZPos(Block p_147734_1_, double p_147734_2_,
			double p_147734_4_, double p_147734_6_, IIcon p_147734_8_) {
		final Tessellator var9 = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147734_8_ = overrideBlockTexture;
		}

		if (Config.isConnectedTextures() && overrideBlockTexture == null) {
			p_147734_8_ = ConnectedTextures.getConnectedTexture(blockAccess,
					p_147734_1_, (int) p_147734_2_, (int) p_147734_4_,
					(int) p_147734_6_, 3, p_147734_8_);
		}

		boolean uvRotateSet = false;

		if (Config.isNaturalTextures() && overrideBlockTexture == null
				&& uvRotateWest == 0) {
			final NaturalProperties var10 = NaturalTextures
					.getNaturalProperties(p_147734_8_);

			if (var10 != null) {
				final int rand = Config.getRandom((int) p_147734_2_,
						(int) p_147734_4_, (int) p_147734_6_, 3);

				if (var10.rotation > 1) {
					uvRotateWest = rand & 3;
				}

				if (var10.rotation == 2) {
					uvRotateWest = uvRotateWest / 2 * 3;
				}

				if (var10.flip) {
					flipTexture = (rand & 4) != 0;
				}

				uvRotateSet = true;
			}
		}

		double var101 = p_147734_8_.getInterpolatedU(renderMinX * 16.0D);
		double var12 = p_147734_8_.getInterpolatedU(renderMaxX * 16.0D);
		double var14 = p_147734_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double var16 = p_147734_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double var18;

		if (flipTexture) {
			var18 = var101;
			var101 = var12;
			var12 = var18;
		}

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			var101 = p_147734_8_.getMinU();
			var12 = p_147734_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			var14 = p_147734_8_.getMinV();
			var16 = p_147734_8_.getMaxV();
		}

		var18 = var12;
		double var20 = var101;
		double var22 = var14;
		double var24 = var16;
		double var26;

		if (uvRotateWest == 1) {
			var101 = p_147734_8_.getInterpolatedU(renderMinY * 16.0D);
			var16 = p_147734_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);
			var12 = p_147734_8_.getInterpolatedU(renderMaxY * 16.0D);
			var14 = p_147734_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var22 = var14;
			var24 = var16;
			var18 = var101;
			var20 = var12;
			var14 = var16;
			var16 = var22;
		} else if (uvRotateWest == 2) {
			var101 = p_147734_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			var14 = p_147734_8_.getInterpolatedV(renderMinX * 16.0D);
			var12 = p_147734_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			var16 = p_147734_8_.getInterpolatedV(renderMaxX * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var101 = var12;
			var12 = var20;
			var22 = var16;
			var24 = var14;
		} else if (uvRotateWest == 3) {
			var101 = p_147734_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			var12 = p_147734_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			var14 = p_147734_8_.getInterpolatedV(renderMaxY * 16.0D);
			var16 = p_147734_8_.getInterpolatedV(renderMinY * 16.0D);

			if (flipTexture) {
				var26 = var101;
				var101 = var12;
				var12 = var26;
			}

			var18 = var12;
			var20 = var101;
			var22 = var14;
			var24 = var16;
		}

		if (uvRotateSet) {
			uvRotateWest = 0;
			flipTexture = false;
		}

		var26 = p_147734_2_ + renderMinX;
		double var28 = p_147734_2_ + renderMaxX;
		final double var30 = p_147734_4_ + renderMinY;
		final double var32 = p_147734_4_ + renderMaxY;
		final double var34 = p_147734_6_ + renderMaxZ;

		if (renderFromInside) {
			var26 = p_147734_2_ + renderMaxX;
			var28 = p_147734_2_ + renderMinX;
		}

		if (enableAO) {
			var9.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft,
					colorBlueTopLeft);
			var9.setBrightness(brightnessTopLeft);
			var9.addVertexWithUV(var26, var32, var34, var101, var14);
			var9.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft,
					colorBlueBottomLeft);
			var9.setBrightness(brightnessBottomLeft);
			var9.addVertexWithUV(var26, var30, var34, var20, var24);
			var9.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight,
					colorBlueBottomRight);
			var9.setBrightness(brightnessBottomRight);
			var9.addVertexWithUV(var28, var30, var34, var12, var16);
			var9.setColorOpaque_F(colorRedTopRight, colorGreenTopRight,
					colorBlueTopRight);
			var9.setBrightness(brightnessTopRight);
			var9.addVertexWithUV(var28, var32, var34, var18, var22);
		} else {
			var9.addVertexWithUV(var26, var32, var34, var101, var14);
			var9.addVertexWithUV(var26, var30, var34, var20, var24);
			var9.addVertexWithUV(var28, var30, var34, var12, var16);
			var9.addVertexWithUV(var28, var32, var34, var18, var22);
		}
	}

	public boolean renderPistonBase(Block p_147731_1_, int p_147731_2_,
			int p_147731_3_, int p_147731_4_, boolean p_147731_5_) {
		final int var6 = blockAccess.getBlockMetadata(p_147731_2_, p_147731_3_,
				p_147731_4_);
		final boolean var7 = p_147731_5_ || (var6 & 8) != 0;
		final int var8 = BlockPistonBase.func_150076_b(var6);
		if (var7) {
			switch (var8) {
			case 0:
				uvRotateEast = 3;
				uvRotateWest = 3;
				uvRotateSouth = 3;
				uvRotateNorth = 3;
				setRenderBounds(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);
				break;

			case 1:
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
				break;

			case 2:
				uvRotateSouth = 1;
				uvRotateNorth = 2;
				setRenderBounds(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
				break;

			case 3:
				uvRotateSouth = 2;
				uvRotateNorth = 1;
				uvRotateTop = 3;
				uvRotateBottom = 3;
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
				break;

			case 4:
				uvRotateEast = 1;
				uvRotateWest = 2;
				uvRotateTop = 2;
				uvRotateBottom = 1;
				setRenderBounds(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
				break;

			case 5:
				uvRotateEast = 2;
				uvRotateWest = 1;
				uvRotateTop = 1;
				uvRotateBottom = 2;
				setRenderBounds(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
			}

			((BlockPistonBase) p_147731_1_).func_150070_b((float) renderMinX,
					(float) renderMinY, (float) renderMinZ, (float) renderMaxX,
					(float) renderMaxY, (float) renderMaxZ);
			renderStandardBlock(p_147731_1_, p_147731_2_, p_147731_3_,
					p_147731_4_);
			uvRotateEast = 0;
			uvRotateWest = 0;
			uvRotateSouth = 0;
			uvRotateNorth = 0;
			uvRotateTop = 0;
			uvRotateBottom = 0;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			((BlockPistonBase) p_147731_1_).func_150070_b((float) renderMinX,
					(float) renderMinY, (float) renderMinZ, (float) renderMaxX,
					(float) renderMaxY, (float) renderMaxZ);
		} else {
			switch (var8) {
			case 0:
				uvRotateEast = 3;
				uvRotateWest = 3;
				uvRotateSouth = 3;
				uvRotateNorth = 3;

			case 1:
			default:
				break;

			case 2:
				uvRotateSouth = 1;
				uvRotateNorth = 2;
				break;

			case 3:
				uvRotateSouth = 2;
				uvRotateNorth = 1;
				uvRotateTop = 3;
				uvRotateBottom = 3;
				break;

			case 4:
				uvRotateEast = 1;
				uvRotateWest = 2;
				uvRotateTop = 2;
				uvRotateBottom = 1;
				break;

			case 5:
				uvRotateEast = 2;
				uvRotateWest = 1;
				uvRotateTop = 1;
				uvRotateBottom = 2;
			}

			renderStandardBlock(p_147731_1_, p_147731_2_, p_147731_3_,
					p_147731_4_);
			uvRotateEast = 0;
			uvRotateWest = 0;
			uvRotateSouth = 0;
			uvRotateNorth = 0;
			uvRotateTop = 0;
			uvRotateBottom = 0;
		}

		return true;
	}

	public void renderPistonBaseAllFaces(Block p_147804_1_, int p_147804_2_,
			int p_147804_3_, int p_147804_4_) {
		renderAllFaces = true;
		renderPistonBase(p_147804_1_, p_147804_2_, p_147804_3_, p_147804_4_,
				true);
		renderAllFaces = false;
	}

	public boolean renderPistonExtension(Block p_147809_1_, int p_147809_2_,
			int p_147809_3_, int p_147809_4_, boolean p_147809_5_) {
		final int var6 = blockAccess.getBlockMetadata(p_147809_2_, p_147809_3_,
				p_147809_4_);
		final int var7 = BlockPistonExtension.func_150085_b(var6);
		final float var11 = p_147809_5_ ? 1.0F : 0.5F;
		final double var12 = p_147809_5_ ? 16.0D : 8.0D;

		switch (var7) {
		case 0:
			uvRotateEast = 3;
			uvRotateWest = 3;
			uvRotateSouth = 3;
			uvRotateNorth = 3;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_,
					p_147809_4_);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F,
					p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + var11,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.625F, 0.8F, var12);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F,
					p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + var11,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.375F, 0.8F, var12);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F,
					p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + var11,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.625F, 0.6F, var12);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F,
					p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + var11,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.375F, 0.6F, var12);
			break;

		case 1:
			setRenderBounds(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_,
					p_147809_4_);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F,
					p_147809_3_ - 0.25F + 1.0F - var11,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.625F,
					p_147809_4_ + 0.625F, 0.8F, var12);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F,
					p_147809_3_ - 0.25F + 1.0F - var11,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.375F,
					p_147809_4_ + 0.375F, 0.8F, var12);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F,
					p_147809_3_ - 0.25F + 1.0F - var11,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.375F,
					p_147809_4_ + 0.625F, 0.6F, var12);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F,
					p_147809_3_ - 0.25F + 1.0F - var11,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.625F,
					p_147809_4_ + 0.375F, 0.6F, var12);
			break;

		case 2:
			uvRotateSouth = 1;
			uvRotateNorth = 2;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_,
					p_147809_4_);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F,
					p_147809_3_ + 0.625F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + var11, 0.6F,
					var12);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F,
					p_147809_3_ + 0.375F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + var11, 0.6F,
					var12);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F,
					p_147809_3_ + 0.375F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + var11, 0.5F,
					var12);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F,
					p_147809_3_ + 0.625F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + var11, 1.0F,
					var12);
			break;

		case 3:
			uvRotateSouth = 2;
			uvRotateNorth = 1;
			uvRotateTop = 3;
			uvRotateBottom = 3;
			setRenderBounds(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_,
					p_147809_4_);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F,
					p_147809_3_ + 0.625F, p_147809_3_ + 0.375F, p_147809_4_
							- 0.25F + 1.0F - var11, p_147809_4_ - 0.25F + 1.0F,
					0.6F, var12);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F,
					p_147809_3_ + 0.375F, p_147809_3_ + 0.625F, p_147809_4_
							- 0.25F + 1.0F - var11, p_147809_4_ - 0.25F + 1.0F,
					0.6F, var12);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F,
					p_147809_3_ + 0.375F, p_147809_3_ + 0.375F, p_147809_4_
							- 0.25F + 1.0F - var11, p_147809_4_ - 0.25F + 1.0F,
					0.5F, var12);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F,
					p_147809_3_ + 0.625F, p_147809_3_ + 0.625F, p_147809_4_
							- 0.25F + 1.0F - var11, p_147809_4_ - 0.25F + 1.0F,
					1.0F, var12);
			break;

		case 4:
			uvRotateEast = 1;
			uvRotateWest = 2;
			uvRotateTop = 2;
			uvRotateBottom = 1;
			setRenderBounds(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_,
					p_147809_4_);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + var11,
					p_147809_3_ + 0.375F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.375F, 0.5F, var12);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + var11,
					p_147809_3_ + 0.625F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.625F, 1.0F, var12);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + var11,
					p_147809_3_ + 0.375F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.375F, 0.6F, var12);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + var11,
					p_147809_3_ + 0.625F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.625F, 0.6F, var12);
			break;

		case 5:
			uvRotateEast = 2;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 2;
			setRenderBounds(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_,
					p_147809_4_);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - var11,
					p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.375F,
					p_147809_3_ + 0.375F, p_147809_4_ + 0.625F,
					p_147809_4_ + 0.375F, 0.5F, var12);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - var11,
					p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.625F,
					p_147809_3_ + 0.625F, p_147809_4_ + 0.375F,
					p_147809_4_ + 0.625F, 1.0F, var12);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - var11,
					p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.375F,
					p_147809_3_ + 0.625F, p_147809_4_ + 0.375F,
					p_147809_4_ + 0.375F, 0.6F, var12);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - var11,
					p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.625F,
					p_147809_3_ + 0.375F, p_147809_4_ + 0.625F,
					p_147809_4_ + 0.625F, 0.6F, var12);
		}

		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateSouth = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return true;
	}

	public void renderPistonExtensionAllFaces(Block p_147750_1_,
			int p_147750_2_, int p_147750_3_, int p_147750_4_,
			boolean p_147750_5_) {
		renderAllFaces = true;
		renderPistonExtension(p_147750_1_, p_147750_2_, p_147750_3_,
				p_147750_4_, p_147750_5_);
		renderAllFaces = false;
	}

	public void renderPistonRodEW(double p_147738_1_, double p_147738_3_,
			double p_147738_5_, double p_147738_7_, double p_147738_9_,
			double p_147738_11_, float p_147738_13_, double p_147738_14_) {
		IIcon var16 = BlockPistonBase.func_150074_e("piston_side");

		if (hasOverrideBlockTexture()) {
			var16 = overrideBlockTexture;
		}

		final Tessellator var17 = Tessellator.instance;
		final double var18 = var16.getMinU();
		final double var20 = var16.getMinV();
		final double var22 = var16.getInterpolatedU(p_147738_14_);
		final double var24 = var16.getInterpolatedV(4.0D);
		var17.setColorOpaque_F(p_147738_13_, p_147738_13_, p_147738_13_);
		var17.addVertexWithUV(p_147738_3_, p_147738_5_, p_147738_9_, var22,
				var20);
		var17.addVertexWithUV(p_147738_1_, p_147738_5_, p_147738_9_, var18,
				var20);
		var17.addVertexWithUV(p_147738_1_, p_147738_7_, p_147738_11_, var18,
				var24);
		var17.addVertexWithUV(p_147738_3_, p_147738_7_, p_147738_11_, var22,
				var24);
	}

	public void renderPistonRodSN(double p_147789_1_, double p_147789_3_,
			double p_147789_5_, double p_147789_7_, double p_147789_9_,
			double p_147789_11_, float p_147789_13_, double p_147789_14_) {
		IIcon var16 = BlockPistonBase.func_150074_e("piston_side");

		if (hasOverrideBlockTexture()) {
			var16 = overrideBlockTexture;
		}

		final Tessellator var17 = Tessellator.instance;
		final double var18 = var16.getMinU();
		final double var20 = var16.getMinV();
		final double var22 = var16.getInterpolatedU(p_147789_14_);
		final double var24 = var16.getInterpolatedV(4.0D);
		var17.setColorOpaque_F(p_147789_13_, p_147789_13_, p_147789_13_);
		var17.addVertexWithUV(p_147789_1_, p_147789_5_, p_147789_11_, var22,
				var20);
		var17.addVertexWithUV(p_147789_1_, p_147789_5_, p_147789_9_, var18,
				var20);
		var17.addVertexWithUV(p_147789_3_, p_147789_7_, p_147789_9_, var18,
				var24);
		var17.addVertexWithUV(p_147789_3_, p_147789_7_, p_147789_11_, var22,
				var24);
	}

	public void renderPistonRodUD(double p_147763_1_, double p_147763_3_,
			double p_147763_5_, double p_147763_7_, double p_147763_9_,
			double p_147763_11_, float p_147763_13_, double p_147763_14_) {
		IIcon var16 = BlockPistonBase.func_150074_e("piston_side");

		if (hasOverrideBlockTexture()) {
			var16 = overrideBlockTexture;
		}

		final Tessellator var17 = Tessellator.instance;
		final double var18 = var16.getMinU();
		final double var20 = var16.getMinV();
		final double var22 = var16.getInterpolatedU(p_147763_14_);
		final double var24 = var16.getInterpolatedV(4.0D);
		var17.setColorOpaque_F(p_147763_13_, p_147763_13_, p_147763_13_);
		var17.addVertexWithUV(p_147763_1_, p_147763_7_, p_147763_9_, var22,
				var20);
		var17.addVertexWithUV(p_147763_1_, p_147763_5_, p_147763_9_, var18,
				var20);
		var17.addVertexWithUV(p_147763_3_, p_147763_5_, p_147763_11_, var18,
				var24);
		var17.addVertexWithUV(p_147763_3_, p_147763_7_, p_147763_11_, var22,
				var24);
	}

	private void renderSnow(int x, int y, int z, double maxY) {
		if (betterSnowEnabled) {
			setRenderBoundsFromBlock(Blocks.snow_layer);
			renderMaxY = maxY;
			renderStandardBlock(Blocks.snow_layer, x, y, z);
		}
	}

	/**
	 * Renders a standard cube block at the given coordinates
	 */
	public boolean renderStandardBlock(Block p_147784_1_, int p_147784_2_,
			int p_147784_3_, int p_147784_4_) {
		final int var5 = CustomColorizer.getColorMultiplier(p_147784_1_,
				blockAccess, p_147784_2_, p_147784_3_, p_147784_4_);
		float var6 = (var5 >> 16 & 255) / 255.0F;
		float var7 = (var5 >> 8 & 255) / 255.0F;
		float var8 = (var5 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float var9 = (var6 * 30.0F + var7 * 59.0F + var8 * 11.0F) / 100.0F;
			final float var10 = (var6 * 30.0F + var7 * 70.0F) / 100.0F;
			final float var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
			var6 = var9;
			var7 = var10;
			var8 = var11;
		}

		return Minecraft.isAmbientOcclusionEnabled()
				&& p_147784_1_.getLightValue() == 0 ? partialRenderBounds ? renderStandardBlockWithAmbientOcclusionPartial(
				p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, var6, var7,
				var8) : renderStandardBlockWithAmbientOcclusion(p_147784_1_,
				p_147784_2_, p_147784_3_, p_147784_4_, var6, var7, var8)
				: renderStandardBlockWithColorMultiplier(p_147784_1_,
						p_147784_2_, p_147784_3_, p_147784_4_, var6, var7, var8);
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block p_147751_1_,
			int p_147751_2_, int p_147751_3_, int p_147751_4_,
			float p_147751_5_, float p_147751_6_, float p_147751_7_) {
		enableAO = true;
		final boolean defaultTexture = Tessellator.instance.defaultTexture;
		final boolean betterGrass = Config.isBetterGrass() && defaultTexture;
		final boolean simpleAO = p_147751_1_ == Blocks.glass;
		boolean var8 = false;
		float var9 = 0.0F;
		float var10 = 0.0F;
		float var11 = 0.0F;
		float var12 = 0.0F;
		boolean var13 = true;
		int var14 = -1;
		final Tessellator var15 = Tessellator.instance;
		var15.setBrightness(983055);

		if (p_147751_1_ == Blocks.grass) {
			var13 = false;
		} else if (hasOverrideBlockTexture()) {
			var13 = false;
		}

		boolean var16;
		boolean var17;
		boolean var18;
		boolean var19;
		int var20;
		float var21;

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_,
						p_147751_3_ - 1, p_147751_4_, 0)) {
			if (renderMinY <= 0.0D) {
				--p_147751_3_;
			}

			aoBrightnessXYNN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoBrightnessYZNN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoBrightnessYZNP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			aoBrightnessXYPN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			aoLightValueScratchXYNN = getAmbientOcclusionLightValue(
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoLightValueScratchYZNN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoLightValueScratchYZNP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			aoLightValueScratchXYPN = getAmbientOcclusionLightValue(
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			var16 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1,
					p_147751_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1,
					p_147751_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1,
					p_147751_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1,
					p_147751_4_ - 1).getCanBlockGrass();

			if (!var19 && !var17) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
				aoBrightnessXYZNNN = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1);
				aoBrightnessXYZNNN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!var18 && !var17) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
				aoBrightnessXYZNNP = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1);
				aoBrightnessXYZNNP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (!var19 && !var16) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
				aoBrightnessXYZPNN = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1);
				aoBrightnessXYZPNN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!var18 && !var16) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
				aoBrightnessXYZPNP = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1);
				aoBrightnessXYZPNP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (renderMinY <= 0.0D) {
				++p_147751_3_;
			}

			if (var14 < 0) {
				var14 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_);
			}

			var20 = var14;

			if (renderMinY <= 0.0D
					|| !blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1,
							p_147751_4_).isOpaqueCube()) {
				var20 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147751_2_, p_147751_3_ - 1,
					p_147751_4_);
			var9 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN
					+ aoLightValueScratchYZNP + var21) / 4.0F;
			var12 = (aoLightValueScratchYZNP + var21
					+ aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4.0F;
			var11 = (var21 + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4.0F;
			var10 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN
					+ var21 + aoLightValueScratchYZNN) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP,
					aoBrightnessXYNN, aoBrightnessYZNP, var20);
			brightnessTopRight = getAoBrightness(aoBrightnessYZNP,
					aoBrightnessXYZPNP, aoBrightnessXYPN, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNN,
					aoBrightnessXYPN, aoBrightnessXYZPNN, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN,
					aoBrightnessXYZNNN, aoBrightnessYZNN, var20);

			if (simpleAO) {
				var10 = var21;
				var11 = var21;
				var12 = var21;
				var9 = var21;
				brightnessTopLeft = brightnessTopRight = brightnessBottomRight = brightnessBottomLeft = var20;
			}

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_ * 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.5F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.5F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			renderFaceYNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_,
							p_147751_3_, p_147751_4_, 0));
			var8 = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_,
						p_147751_3_ + 1, p_147751_4_, 1)) {
			if (renderMaxY >= 1.0D) {
				++p_147751_3_;
			}

			aoBrightnessXYNP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoBrightnessXYPP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			aoBrightnessYZPN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoBrightnessYZPP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			aoLightValueScratchXYNP = getAmbientOcclusionLightValue(
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoLightValueScratchXYPP = getAmbientOcclusionLightValue(
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			aoLightValueScratchYZPN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoLightValueScratchYZPP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			var16 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1,
					p_147751_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1,
					p_147751_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1,
					p_147751_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1,
					p_147751_4_ - 1).getCanBlockGrass();

			if (!var19 && !var17) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
				aoBrightnessXYZNPN = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1);
				aoBrightnessXYZNPN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!var19 && !var16) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
				aoBrightnessXYZPPN = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1);
				aoBrightnessXYZPPN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!var18 && !var17) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
				aoBrightnessXYZNPP = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1);
				aoBrightnessXYZNPP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (!var18 && !var16) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
				aoBrightnessXYZPPP = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1);
				aoBrightnessXYZPPP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (renderMaxY >= 1.0D) {
				--p_147751_3_;
			}

			if (var14 < 0) {
				var14 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_);
			}

			var20 = var14;

			if (renderMaxY >= 1.0D
					|| !blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1,
							p_147751_4_).isOpaqueCube()) {
				var20 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147751_2_, p_147751_3_ + 1,
					p_147751_4_);
			var12 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP
					+ aoLightValueScratchYZPP + var21) / 4.0F;
			var9 = (aoLightValueScratchYZPP + var21 + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4.0F;
			var10 = (var21 + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4.0F;
			var11 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN
					+ var21 + aoLightValueScratchYZPN) / 4.0F;
			brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP,
					aoBrightnessXYNP, aoBrightnessYZPP, var20);
			brightnessTopLeft = getAoBrightness(aoBrightnessYZPP,
					aoBrightnessXYZPPP, aoBrightnessXYPP, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN,
					aoBrightnessXYPP, aoBrightnessXYZPPN, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessXYNP,
					aoBrightnessXYZNPN, aoBrightnessYZPN, var20);

			if (simpleAO) {
				var10 = var21;
				var11 = var21;
				var12 = var21;
				var9 = var21;
				brightnessTopLeft = brightnessTopRight = brightnessBottomRight = brightnessBottomLeft = var20;
			}

			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_;
			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			renderFaceYPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_,
							p_147751_3_, p_147751_4_, 1));
			var8 = true;
		}

		IIcon var22;

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_,
						p_147751_3_, p_147751_4_ - 1, 2)) {
			if (renderMinZ <= 0.0D) {
				--p_147751_4_;
			}

			aoLightValueScratchXZNN = getAmbientOcclusionLightValue(
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoLightValueScratchYZNN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoLightValueScratchYZPN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			aoLightValueScratchXZPN = getAmbientOcclusionLightValue(
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			aoBrightnessXZNN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoBrightnessYZNN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoBrightnessYZPN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			aoBrightnessXZPN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			var16 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_,
					p_147751_4_ - 1).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_,
					p_147751_4_ - 1).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1,
					p_147751_4_ - 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1,
					p_147751_4_ - 1).getCanBlockGrass();

			if (!var17 && !var19) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_);
				aoBrightnessXYZNNN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_ - 1,
						p_147751_4_);
			}

			if (!var17 && !var18) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_);
				aoBrightnessXYZNPN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_ + 1,
						p_147751_4_);
			}

			if (!var16 && !var19) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_);
				aoBrightnessXYZPNN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_ - 1,
						p_147751_4_);
			}

			if (!var16 && !var18) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_);
				aoBrightnessXYZPPN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_ + 1,
						p_147751_4_);
			}

			if (renderMinZ <= 0.0D) {
				++p_147751_4_;
			}

			if (var14 < 0) {
				var14 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_);
			}

			var20 = var14;

			if (renderMinZ <= 0.0D
					|| !blockAccess.getBlock(p_147751_2_, p_147751_3_,
							p_147751_4_ - 1).isOpaqueCube()) {
				var20 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			}

			var21 = getAmbientOcclusionLightValue(p_147751_2_, p_147751_3_,
					p_147751_4_ - 1);
			var9 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + var21 + aoLightValueScratchYZPN) / 4.0F;
			var10 = (var21 + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4.0F;
			var11 = (aoLightValueScratchYZNN + var21
					+ aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4.0F;
			var12 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN
					+ aoLightValueScratchYZNN + var21) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXZNN,
					aoBrightnessXYZNPN, aoBrightnessYZPN, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN,
					aoBrightnessXZPN, aoBrightnessXYZPPN, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNN,
					aoBrightnessXYZPNN, aoBrightnessXZPN, var20);
			brightnessTopRight = getAoBrightness(aoBrightnessXYZNNN,
					aoBrightnessXZNN, aoBrightnessYZNN, var20);

			if (simpleAO) {
				var10 = var21;
				var11 = var21;
				var12 = var21;
				var9 = var21;
				brightnessTopLeft = brightnessTopRight = brightnessBottomRight = brightnessBottomLeft = var20;
			}

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_ * 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var22 = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_,
					p_147751_3_, p_147751_4_, 2);

			if (betterGrass) {
				var22 = fixAoSideGrassTexture(var22, p_147751_2_, p_147751_3_,
						p_147751_4_, 2, p_147751_5_, p_147751_6_, p_147751_7_);
			}

			renderFaceZNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					var22);

			if (defaultTexture && fancyGrass
					&& var22 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceZNeg(p_147751_1_, p_147751_2_, p_147751_3_,
						p_147751_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_,
						p_147751_3_, p_147751_4_ + 1, 3)) {
			if (renderMaxZ >= 1.0D) {
				++p_147751_4_;
			}

			aoLightValueScratchXZNP = getAmbientOcclusionLightValue(
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoLightValueScratchXZPP = getAmbientOcclusionLightValue(
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			aoLightValueScratchYZNP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoLightValueScratchYZPP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			aoBrightnessXZNP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			aoBrightnessXZPP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			aoBrightnessYZNP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoBrightnessYZPP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			var16 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_,
					p_147751_4_ + 1).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_,
					p_147751_4_ + 1).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1,
					p_147751_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1,
					p_147751_4_ + 1).getCanBlockGrass();

			if (!var17 && !var19) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_);
				aoBrightnessXYZNNP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_ - 1,
						p_147751_4_);
			}

			if (!var17 && !var18) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(
						p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_);
				aoBrightnessXYZNPP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ - 1, p_147751_3_ + 1,
						p_147751_4_);
			}

			if (!var16 && !var19) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_);
				aoBrightnessXYZPNP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_ - 1,
						p_147751_4_);
			}

			if (!var16 && !var18) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(
						p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_);
				aoBrightnessXYZPPP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_ + 1, p_147751_3_ + 1,
						p_147751_4_);
			}

			if (renderMaxZ >= 1.0D) {
				--p_147751_4_;
			}

			if (var14 < 0) {
				var14 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_);
			}

			var20 = var14;

			if (renderMaxZ >= 1.0D
					|| !blockAccess.getBlock(p_147751_2_, p_147751_3_,
							p_147751_4_ + 1).isOpaqueCube()) {
				var20 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			}

			var21 = getAmbientOcclusionLightValue(p_147751_2_, p_147751_3_,
					p_147751_4_ + 1);
			var9 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + var21 + aoLightValueScratchYZPP) / 4.0F;
			var12 = (var21 + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4.0F;
			var11 = (aoLightValueScratchYZNP + var21
					+ aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4.0F;
			var10 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP
					+ aoLightValueScratchYZNP + var21) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXZNP,
					aoBrightnessXYZNPP, aoBrightnessYZPP, var20);
			brightnessTopRight = getAoBrightness(aoBrightnessYZPP,
					aoBrightnessXZPP, aoBrightnessXYZPPP, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNP,
					aoBrightnessXYZPNP, aoBrightnessXZPP, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYZNNP,
					aoBrightnessXZNP, aoBrightnessYZNP, var20);

			if (simpleAO) {
				var10 = var21;
				var11 = var21;
				var12 = var21;
				var9 = var21;
				brightnessTopLeft = brightnessTopRight = brightnessBottomRight = brightnessBottomLeft = var20;
			}

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_ * 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var22 = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_,
					p_147751_3_, p_147751_4_, 3);

			if (betterGrass) {
				var22 = fixAoSideGrassTexture(var22, p_147751_2_, p_147751_3_,
						p_147751_4_, 3, p_147751_5_, p_147751_6_, p_147751_7_);
			}

			renderFaceZPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					var22);

			if (defaultTexture && fancyGrass
					&& var22 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceZPos(p_147751_1_, p_147751_2_, p_147751_3_,
						p_147751_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess,
						p_147751_2_ - 1, p_147751_3_, p_147751_4_, 4)) {
			if (renderMinX <= 0.0D) {
				--p_147751_2_;
			}

			aoLightValueScratchXYNN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoLightValueScratchXZNN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoLightValueScratchXZNP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			aoLightValueScratchXYNP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			aoBrightnessXYNN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoBrightnessXZNN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoBrightnessXZNP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			aoBrightnessXYNP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			var16 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1,
					p_147751_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1,
					p_147751_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_,
					p_147751_4_ - 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_,
					p_147751_4_ + 1).getCanBlockGrass();

			if (!var18 && !var17) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1);
				aoBrightnessXYZNNN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ - 1);
			}

			if (!var19 && !var17) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1);
				aoBrightnessXYZNNP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ + 1);
			}

			if (!var18 && !var16) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1);
				aoBrightnessXYZNPN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ - 1);
			}

			if (!var19 && !var16) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1);
				aoBrightnessXYZNPP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ + 1);
			}

			if (renderMinX <= 0.0D) {
				++p_147751_2_;
			}

			if (var14 < 0) {
				var14 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_);
			}

			var20 = var14;

			if (renderMinX <= 0.0D
					|| !blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_,
							p_147751_4_).isOpaqueCube()) {
				var20 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147751_2_ - 1, p_147751_3_,
					p_147751_4_);
			var12 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP
					+ var21 + aoLightValueScratchXZNP) / 4.0F;
			var9 = (var21 + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4.0F;
			var10 = (aoLightValueScratchXZNN + var21
					+ aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4.0F;
			var11 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN
					+ aoLightValueScratchXZNN + var21) / 4.0F;
			brightnessTopRight = getAoBrightness(aoBrightnessXYNN,
					aoBrightnessXYZNNP, aoBrightnessXZNP, var20);
			brightnessTopLeft = getAoBrightness(aoBrightnessXZNP,
					aoBrightnessXYNP, aoBrightnessXYZNPP, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXZNN,
					aoBrightnessXYZNPN, aoBrightnessXYNP, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessXYZNNN,
					aoBrightnessXYNN, aoBrightnessXZNN, var20);

			if (simpleAO) {
				var10 = var21;
				var11 = var21;
				var12 = var21;
				var9 = var21;
				brightnessTopLeft = brightnessTopRight = brightnessBottomRight = brightnessBottomLeft = var20;
			}

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_ * 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var22 = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_,
					p_147751_3_, p_147751_4_, 4);

			if (betterGrass) {
				var22 = fixAoSideGrassTexture(var22, p_147751_2_, p_147751_3_,
						p_147751_4_, 4, p_147751_5_, p_147751_6_, p_147751_7_);
			}

			renderFaceXNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					var22);

			if (defaultTexture && fancyGrass
					&& var22 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceXNeg(p_147751_1_, p_147751_2_, p_147751_3_,
						p_147751_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess,
						p_147751_2_ + 1, p_147751_3_, p_147751_4_, 5)) {
			if (renderMaxX >= 1.0D) {
				++p_147751_2_;
			}

			aoLightValueScratchXYPN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoLightValueScratchXZPN = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoLightValueScratchXZPP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			aoLightValueScratchXYPP = getAmbientOcclusionLightValue(
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			aoBrightnessXYPN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			aoBrightnessXZPN = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			aoBrightnessXZPP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			aoBrightnessXYPP = p_147751_1_.getBlockBrightness(blockAccess,
					p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			var16 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1,
					p_147751_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1,
					p_147751_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_,
					p_147751_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_,
					p_147751_4_ - 1).getCanBlockGrass();

			if (!var17 && !var19) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1);
				aoBrightnessXYZPNN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ - 1);
			}

			if (!var17 && !var18) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1);
				aoBrightnessXYZPNP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ + 1);
			}

			if (!var16 && !var19) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1);
				aoBrightnessXYZPPN = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ - 1);
			}

			if (!var16 && !var18) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(
						p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1);
				aoBrightnessXYZPPP = p_147751_1_.getBlockBrightness(
						blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ + 1);
			}

			if (renderMaxX >= 1.0D) {
				--p_147751_2_;
			}

			if (var14 < 0) {
				var14 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_, p_147751_3_, p_147751_4_);
			}

			var20 = var14;

			if (renderMaxX >= 1.0D
					|| !blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_,
							p_147751_4_).isOpaqueCube()) {
				var20 = p_147751_1_.getBlockBrightness(blockAccess,
						p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147751_2_ + 1, p_147751_3_,
					p_147751_4_);
			var9 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + var21 + aoLightValueScratchXZPP) / 4.0F;
			var10 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN
					+ aoLightValueScratchXZPN + var21) / 4.0F;
			var11 = (aoLightValueScratchXZPN + var21
					+ aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4.0F;
			var12 = (var21 + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXYPN,
					aoBrightnessXYZPNP, aoBrightnessXZPP, var20);
			brightnessTopRight = getAoBrightness(aoBrightnessXZPP,
					aoBrightnessXYPP, aoBrightnessXYZPPP, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessXZPN,
					aoBrightnessXYZPPN, aoBrightnessXYPP, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYZPNN,
					aoBrightnessXYPN, aoBrightnessXZPN, var20);

			if (simpleAO) {
				var10 = var21;
				var11 = var21;
				var12 = var21;
				var9 = var21;
				brightnessTopLeft = brightnessTopRight = brightnessBottomRight = brightnessBottomLeft = var20;
			}

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_ * 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var22 = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_,
					p_147751_3_, p_147751_4_, 5);

			if (betterGrass) {
				var22 = fixAoSideGrassTexture(var22, p_147751_2_, p_147751_3_,
						p_147751_4_, 5, p_147751_5_, p_147751_6_, p_147751_7_);
			}

			renderFaceXPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					var22);

			if (defaultTexture && fancyGrass
					&& var22 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceXPos(p_147751_1_, p_147751_2_, p_147751_3_,
						p_147751_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		enableAO = false;
		return var8;
	}

	public boolean renderStandardBlockWithAmbientOcclusionPartial(
			Block p_147808_1_, int p_147808_2_, int p_147808_3_,
			int p_147808_4_, float p_147808_5_, float p_147808_6_,
			float p_147808_7_) {
		enableAO = true;
		boolean var8 = false;
		float var9 = 0.0F;
		float var10 = 0.0F;
		float var11 = 0.0F;
		float var12 = 0.0F;
		boolean var13 = true;
		final int var14 = p_147808_1_.getBlockBrightness(blockAccess,
				p_147808_2_, p_147808_3_, p_147808_4_);
		final Tessellator var15 = Tessellator.instance;
		var15.setBrightness(983055);

		if (p_147808_1_ == Blocks.grass) {
			var13 = false;
		} else if (hasOverrideBlockTexture()) {
			var13 = false;
		}

		boolean var16;
		boolean var17;
		boolean var18;
		boolean var19;
		int var20;
		float var21;

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_,
						p_147808_3_ - 1, p_147808_4_, 0)) {
			if (renderMinY <= 0.0D) {
				--p_147808_3_;
			}

			aoBrightnessXYNN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoBrightnessYZNN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoBrightnessYZNP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			aoBrightnessXYPN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			aoLightValueScratchXYNN = getAmbientOcclusionLightValue(
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoLightValueScratchYZNN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoLightValueScratchYZNP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			aoLightValueScratchXYPN = getAmbientOcclusionLightValue(
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			var16 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1,
					p_147808_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1,
					p_147808_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1,
					p_147808_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1,
					p_147808_4_ - 1).getCanBlockGrass();

			if (!var19 && !var17) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
				aoBrightnessXYZNNN = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1);
				aoBrightnessXYZNNN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!var18 && !var17) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
				aoBrightnessXYZNNP = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1);
				aoBrightnessXYZNNP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (!var19 && !var16) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
				aoBrightnessXYZPNN = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1);
				aoBrightnessXYZPNN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!var18 && !var16) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
				aoBrightnessXYZPNP = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1);
				aoBrightnessXYZPNP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (renderMinY <= 0.0D) {
				++p_147808_3_;
			}

			var20 = var14;

			if (renderMinY <= 0.0D
					|| !blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1,
							p_147808_4_).isOpaqueCube()) {
				var20 = p_147808_1_.getBlockBrightness(blockAccess,
						p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147808_2_, p_147808_3_ - 1,
					p_147808_4_);
			var9 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN
					+ aoLightValueScratchYZNP + var21) / 4.0F;
			var12 = (aoLightValueScratchYZNP + var21
					+ aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4.0F;
			var11 = (var21 + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4.0F;
			var10 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN
					+ var21 + aoLightValueScratchYZNN) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP,
					aoBrightnessXYNN, aoBrightnessYZNP, var20);
			brightnessTopRight = getAoBrightness(aoBrightnessYZNP,
					aoBrightnessXYZPNP, aoBrightnessXYPN, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNN,
					aoBrightnessXYPN, aoBrightnessXYZPNN, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN,
					aoBrightnessXYZNNN, aoBrightnessYZNN, var20);

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_ * 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.5F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.5F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			renderFaceYNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_,
							p_147808_3_, p_147808_4_, 0));
			var8 = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_,
						p_147808_3_ + 1, p_147808_4_, 1)) {
			if (renderMaxY >= 1.0D) {
				++p_147808_3_;
			}

			aoBrightnessXYNP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoBrightnessXYPP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			aoBrightnessYZPN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoBrightnessYZPP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			aoLightValueScratchXYNP = getAmbientOcclusionLightValue(
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoLightValueScratchXYPP = getAmbientOcclusionLightValue(
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			aoLightValueScratchYZPN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoLightValueScratchYZPP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			var16 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1,
					p_147808_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1,
					p_147808_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1,
					p_147808_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1,
					p_147808_4_ - 1).getCanBlockGrass();

			if (!var19 && !var17) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
				aoBrightnessXYZNPN = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1);
				aoBrightnessXYZNPN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!var19 && !var16) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
				aoBrightnessXYZPPN = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1);
				aoBrightnessXYZPPN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!var18 && !var17) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
				aoBrightnessXYZNPP = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1);
				aoBrightnessXYZNPP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (!var18 && !var16) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
				aoBrightnessXYZPPP = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1);
				aoBrightnessXYZPPP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (renderMaxY >= 1.0D) {
				--p_147808_3_;
			}

			var20 = var14;

			if (renderMaxY >= 1.0D
					|| !blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1,
							p_147808_4_).isOpaqueCube()) {
				var20 = p_147808_1_.getBlockBrightness(blockAccess,
						p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147808_2_, p_147808_3_ + 1,
					p_147808_4_);
			var12 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP
					+ aoLightValueScratchYZPP + var21) / 4.0F;
			var9 = (aoLightValueScratchYZPP + var21 + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4.0F;
			var10 = (var21 + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4.0F;
			var11 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN
					+ var21 + aoLightValueScratchYZPN) / 4.0F;
			brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP,
					aoBrightnessXYNP, aoBrightnessYZPP, var20);
			brightnessTopLeft = getAoBrightness(aoBrightnessYZPP,
					aoBrightnessXYZPPP, aoBrightnessXYPP, var20);
			brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN,
					aoBrightnessXYPP, aoBrightnessXYZPPN, var20);
			brightnessBottomRight = getAoBrightness(aoBrightnessXYNP,
					aoBrightnessXYZNPN, aoBrightnessYZPN, var20);
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_;
			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			renderFaceYPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_,
							p_147808_3_, p_147808_4_, 1));
			var8 = true;
		}

		float var22;
		float var23;
		float var24;
		float var25;
		int var26;
		int var27;
		int var28;
		int var29;
		IIcon var30;

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_,
						p_147808_3_, p_147808_4_ - 1, 2)) {
			if (renderMinZ <= 0.0D) {
				--p_147808_4_;
			}

			aoLightValueScratchXZNN = getAmbientOcclusionLightValue(
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoLightValueScratchYZNN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoLightValueScratchYZPN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			aoLightValueScratchXZPN = getAmbientOcclusionLightValue(
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			aoBrightnessXZNN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoBrightnessYZNN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoBrightnessYZPN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			aoBrightnessXZPN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			var16 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_,
					p_147808_4_ - 1).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_,
					p_147808_4_ - 1).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1,
					p_147808_4_ - 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1,
					p_147808_4_ - 1).getCanBlockGrass();

			if (!var17 && !var19) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_);
				aoBrightnessXYZNNN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_ - 1,
						p_147808_4_);
			}

			if (!var17 && !var18) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_);
				aoBrightnessXYZNPN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_ + 1,
						p_147808_4_);
			}

			if (!var16 && !var19) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_);
				aoBrightnessXYZPNN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_ - 1,
						p_147808_4_);
			}

			if (!var16 && !var18) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_);
				aoBrightnessXYZPPN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_ + 1,
						p_147808_4_);
			}

			if (renderMinZ <= 0.0D) {
				++p_147808_4_;
			}

			var20 = var14;

			if (renderMinZ <= 0.0D
					|| !blockAccess.getBlock(p_147808_2_, p_147808_3_,
							p_147808_4_ - 1).isOpaqueCube()) {
				var20 = p_147808_1_.getBlockBrightness(blockAccess,
						p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			}

			var21 = getAmbientOcclusionLightValue(p_147808_2_, p_147808_3_,
					p_147808_4_ - 1);
			var22 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN
					+ var21 + aoLightValueScratchYZPN) / 4.0F;
			var23 = (var21 + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4.0F;
			var24 = (aoLightValueScratchYZNN + var21
					+ aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4.0F;
			var25 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN
					+ aoLightValueScratchYZNN + var21) / 4.0F;
			var9 = (float) (var22 * renderMaxY * (1.0D - renderMinX) + var23
					* renderMaxY * renderMinX + var24 * (1.0D - renderMaxY)
					* renderMinX + var25 * (1.0D - renderMaxY)
					* (1.0D - renderMinX));
			var10 = (float) (var22 * renderMaxY * (1.0D - renderMaxX) + var23
					* renderMaxY * renderMaxX + var24 * (1.0D - renderMaxY)
					* renderMaxX + var25 * (1.0D - renderMaxY)
					* (1.0D - renderMaxX));
			var11 = (float) (var22 * renderMinY * (1.0D - renderMaxX) + var23
					* renderMinY * renderMaxX + var24 * (1.0D - renderMinY)
					* renderMaxX + var25 * (1.0D - renderMinY)
					* (1.0D - renderMaxX));
			var12 = (float) (var22 * renderMinY * (1.0D - renderMinX) + var23
					* renderMinY * renderMinX + var24 * (1.0D - renderMinY)
					* renderMinX + var25 * (1.0D - renderMinY)
					* (1.0D - renderMinX));
			var26 = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN,
					aoBrightnessYZPN, var20);
			var27 = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN,
					aoBrightnessXYZPPN, var20);
			var28 = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN,
					aoBrightnessXZPN, var20);
			var29 = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN,
					aoBrightnessYZNN, var20);
			brightnessTopLeft = mixAoBrightness(var26, var27, var28, var29,
					renderMaxY * (1.0D - renderMinX), renderMaxY * renderMinX,
					(1.0D - renderMaxY) * renderMinX, (1.0D - renderMaxY)
							* (1.0D - renderMinX));
			brightnessBottomLeft = mixAoBrightness(var26, var27, var28, var29,
					renderMaxY * (1.0D - renderMaxX), renderMaxY * renderMaxX,
					(1.0D - renderMaxY) * renderMaxX, (1.0D - renderMaxY)
							* (1.0D - renderMaxX));
			brightnessBottomRight = mixAoBrightness(var26, var27, var28, var29,
					renderMinY * (1.0D - renderMaxX), renderMinY * renderMaxX,
					(1.0D - renderMinY) * renderMaxX, (1.0D - renderMinY)
							* (1.0D - renderMaxX));
			brightnessTopRight = mixAoBrightness(var26, var27, var28, var29,
					renderMinY * (1.0D - renderMinX), renderMinY * renderMinX,
					(1.0D - renderMinY) * renderMinX, (1.0D - renderMinY)
							* (1.0D - renderMinX));

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_ * 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var30 = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_,
					p_147808_3_, p_147808_4_, 2);
			renderFaceZNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					var30);

			if (fancyGrass && var30.getIconName().equals("grass_side")
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceZNeg(p_147808_1_, p_147808_2_, p_147808_3_,
						p_147808_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_,
						p_147808_3_, p_147808_4_ + 1, 3)) {
			if (renderMaxZ >= 1.0D) {
				++p_147808_4_;
			}

			aoLightValueScratchXZNP = getAmbientOcclusionLightValue(
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoLightValueScratchXZPP = getAmbientOcclusionLightValue(
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			aoLightValueScratchYZNP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoLightValueScratchYZPP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			aoBrightnessXZNP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			aoBrightnessXZPP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			aoBrightnessYZNP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoBrightnessYZPP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			var16 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_,
					p_147808_4_ + 1).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_,
					p_147808_4_ + 1).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1,
					p_147808_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1,
					p_147808_4_ + 1).getCanBlockGrass();

			if (!var17 && !var19) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_);
				aoBrightnessXYZNNP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_ - 1,
						p_147808_4_);
			}

			if (!var17 && !var18) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(
						p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_);
				aoBrightnessXYZNPP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ - 1, p_147808_3_ + 1,
						p_147808_4_);
			}

			if (!var16 && !var19) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_);
				aoBrightnessXYZPNP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_ - 1,
						p_147808_4_);
			}

			if (!var16 && !var18) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(
						p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_);
				aoBrightnessXYZPPP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_ + 1, p_147808_3_ + 1,
						p_147808_4_);
			}

			if (renderMaxZ >= 1.0D) {
				--p_147808_4_;
			}

			var20 = var14;

			if (renderMaxZ >= 1.0D
					|| !blockAccess.getBlock(p_147808_2_, p_147808_3_,
							p_147808_4_ + 1).isOpaqueCube()) {
				var20 = p_147808_1_.getBlockBrightness(blockAccess,
						p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			}

			var21 = getAmbientOcclusionLightValue(p_147808_2_, p_147808_3_,
					p_147808_4_ + 1);
			var22 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP
					+ var21 + aoLightValueScratchYZPP) / 4.0F;
			var23 = (var21 + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4.0F;
			var24 = (aoLightValueScratchYZNP + var21
					+ aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4.0F;
			var25 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP
					+ aoLightValueScratchYZNP + var21) / 4.0F;
			var9 = (float) (var22 * renderMaxY * (1.0D - renderMinX) + var23
					* renderMaxY * renderMinX + var24 * (1.0D - renderMaxY)
					* renderMinX + var25 * (1.0D - renderMaxY)
					* (1.0D - renderMinX));
			var10 = (float) (var22 * renderMinY * (1.0D - renderMinX) + var23
					* renderMinY * renderMinX + var24 * (1.0D - renderMinY)
					* renderMinX + var25 * (1.0D - renderMinY)
					* (1.0D - renderMinX));
			var11 = (float) (var22 * renderMinY * (1.0D - renderMaxX) + var23
					* renderMinY * renderMaxX + var24 * (1.0D - renderMinY)
					* renderMaxX + var25 * (1.0D - renderMinY)
					* (1.0D - renderMaxX));
			var12 = (float) (var22 * renderMaxY * (1.0D - renderMaxX) + var23
					* renderMaxY * renderMaxX + var24 * (1.0D - renderMaxY)
					* renderMaxX + var25 * (1.0D - renderMaxY)
					* (1.0D - renderMaxX));
			var26 = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP,
					aoBrightnessYZPP, var20);
			var27 = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP,
					aoBrightnessXYZPPP, var20);
			var28 = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP,
					aoBrightnessXZPP, var20);
			var29 = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP,
					aoBrightnessYZNP, var20);
			brightnessTopLeft = mixAoBrightness(var26, var29, var28, var27,
					renderMaxY * (1.0D - renderMinX), (1.0D - renderMaxY)
							* (1.0D - renderMinX), (1.0D - renderMaxY)
							* renderMinX, renderMaxY * renderMinX);
			brightnessBottomLeft = mixAoBrightness(var26, var29, var28, var27,
					renderMinY * (1.0D - renderMinX), (1.0D - renderMinY)
							* (1.0D - renderMinX), (1.0D - renderMinY)
							* renderMinX, renderMinY * renderMinX);
			brightnessBottomRight = mixAoBrightness(var26, var29, var28, var27,
					renderMinY * (1.0D - renderMaxX), (1.0D - renderMinY)
							* (1.0D - renderMaxX), (1.0D - renderMinY)
							* renderMaxX, renderMinY * renderMaxX);
			brightnessTopRight = mixAoBrightness(var26, var29, var28, var27,
					renderMaxY * (1.0D - renderMaxX), (1.0D - renderMaxY)
							* (1.0D - renderMaxX), (1.0D - renderMaxY)
							* renderMaxX, renderMaxY * renderMaxX);

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_ * 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var30 = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_,
					p_147808_3_, p_147808_4_, 3);
			renderFaceZPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					var30);

			if (fancyGrass && var30.getIconName().equals("grass_side")
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceZPos(p_147808_1_, p_147808_2_, p_147808_3_,
						p_147808_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess,
						p_147808_2_ - 1, p_147808_3_, p_147808_4_, 4)) {
			if (renderMinX <= 0.0D) {
				--p_147808_2_;
			}

			aoLightValueScratchXYNN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoLightValueScratchXZNN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoLightValueScratchXZNP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			aoLightValueScratchXYNP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			aoBrightnessXYNN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoBrightnessXZNN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoBrightnessXZNP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			aoBrightnessXYNP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			var16 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1,
					p_147808_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1,
					p_147808_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_,
					p_147808_4_ - 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_,
					p_147808_4_ + 1).getCanBlockGrass();

			if (!var18 && !var17) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1);
				aoBrightnessXYZNNN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ - 1);
			}

			if (!var19 && !var17) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1);
				aoBrightnessXYZNNP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ + 1);
			}

			if (!var18 && !var16) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1);
				aoBrightnessXYZNPN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ - 1);
			}

			if (!var19 && !var16) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1);
				aoBrightnessXYZNPP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ + 1);
			}

			if (renderMinX <= 0.0D) {
				++p_147808_2_;
			}

			var20 = var14;

			if (renderMinX <= 0.0D
					|| !blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_,
							p_147808_4_).isOpaqueCube()) {
				var20 = p_147808_1_.getBlockBrightness(blockAccess,
						p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147808_2_ - 1, p_147808_3_,
					p_147808_4_);
			var22 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP
					+ var21 + aoLightValueScratchXZNP) / 4.0F;
			var23 = (var21 + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4.0F;
			var24 = (aoLightValueScratchXZNN + var21
					+ aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4.0F;
			var25 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN
					+ aoLightValueScratchXZNN + var21) / 4.0F;
			var9 = (float) (var23 * renderMaxY * renderMaxZ + var24
					* renderMaxY * (1.0D - renderMaxZ) + var25
					* (1.0D - renderMaxY) * (1.0D - renderMaxZ) + var22
					* (1.0D - renderMaxY) * renderMaxZ);
			var10 = (float) (var23 * renderMaxY * renderMinZ + var24
					* renderMaxY * (1.0D - renderMinZ) + var25
					* (1.0D - renderMaxY) * (1.0D - renderMinZ) + var22
					* (1.0D - renderMaxY) * renderMinZ);
			var11 = (float) (var23 * renderMinY * renderMinZ + var24
					* renderMinY * (1.0D - renderMinZ) + var25
					* (1.0D - renderMinY) * (1.0D - renderMinZ) + var22
					* (1.0D - renderMinY) * renderMinZ);
			var12 = (float) (var23 * renderMinY * renderMaxZ + var24
					* renderMinY * (1.0D - renderMaxZ) + var25
					* (1.0D - renderMinY) * (1.0D - renderMaxZ) + var22
					* (1.0D - renderMinY) * renderMaxZ);
			var26 = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP,
					aoBrightnessXZNP, var20);
			var27 = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP,
					aoBrightnessXYZNPP, var20);
			var28 = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN,
					aoBrightnessXYNP, var20);
			var29 = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN,
					aoBrightnessXZNN, var20);
			brightnessTopLeft = mixAoBrightness(var27, var28, var29, var26,
					renderMaxY * renderMaxZ, renderMaxY * (1.0D - renderMaxZ),
					(1.0D - renderMaxY) * (1.0D - renderMaxZ),
					(1.0D - renderMaxY) * renderMaxZ);
			brightnessBottomLeft = mixAoBrightness(var27, var28, var29, var26,
					renderMaxY * renderMinZ, renderMaxY * (1.0D - renderMinZ),
					(1.0D - renderMaxY) * (1.0D - renderMinZ),
					(1.0D - renderMaxY) * renderMinZ);
			brightnessBottomRight = mixAoBrightness(var27, var28, var29, var26,
					renderMinY * renderMinZ, renderMinY * (1.0D - renderMinZ),
					(1.0D - renderMinY) * (1.0D - renderMinZ),
					(1.0D - renderMinY) * renderMinZ);
			brightnessTopRight = mixAoBrightness(var27, var28, var29, var26,
					renderMinY * renderMaxZ, renderMinY * (1.0D - renderMaxZ),
					(1.0D - renderMinY) * (1.0D - renderMaxZ),
					(1.0D - renderMinY) * renderMaxZ);

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_ * 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var30 = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_,
					p_147808_3_, p_147808_4_, 4);
			renderFaceXNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					var30);

			if (fancyGrass && var30.getIconName().equals("grass_side")
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceXNeg(p_147808_1_, p_147808_2_, p_147808_3_,
						p_147808_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess,
						p_147808_2_ + 1, p_147808_3_, p_147808_4_, 5)) {
			if (renderMaxX >= 1.0D) {
				++p_147808_2_;
			}

			aoLightValueScratchXYPN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoLightValueScratchXZPN = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoLightValueScratchXZPP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			aoLightValueScratchXYPP = getAmbientOcclusionLightValue(
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			aoBrightnessXYPN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			aoBrightnessXZPN = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			aoBrightnessXZPP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			aoBrightnessXYPP = p_147808_1_.getBlockBrightness(blockAccess,
					p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			var16 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1,
					p_147808_4_).getCanBlockGrass();
			var17 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1,
					p_147808_4_).getCanBlockGrass();
			var18 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_,
					p_147808_4_ + 1).getCanBlockGrass();
			var19 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_,
					p_147808_4_ - 1).getCanBlockGrass();

			if (!var17 && !var19) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1);
				aoBrightnessXYZPNN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ - 1);
			}

			if (!var17 && !var18) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1);
				aoBrightnessXYZPNP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ + 1);
			}

			if (!var16 && !var19) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1);
				aoBrightnessXYZPPN = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ - 1);
			}

			if (!var16 && !var18) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(
						p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1);
				aoBrightnessXYZPPP = p_147808_1_.getBlockBrightness(
						blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ + 1);
			}

			if (renderMaxX >= 1.0D) {
				--p_147808_2_;
			}

			var20 = var14;

			if (renderMaxX >= 1.0D
					|| !blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_,
							p_147808_4_).isOpaqueCube()) {
				var20 = p_147808_1_.getBlockBrightness(blockAccess,
						p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			}

			var21 = getAmbientOcclusionLightValue(p_147808_2_ + 1, p_147808_3_,
					p_147808_4_);
			var22 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP
					+ var21 + aoLightValueScratchXZPP) / 4.0F;
			var23 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN
					+ aoLightValueScratchXZPN + var21) / 4.0F;
			var24 = (aoLightValueScratchXZPN + var21
					+ aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4.0F;
			var25 = (var21 + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4.0F;
			var9 = (float) (var22 * (1.0D - renderMinY) * renderMaxZ + var23
					* (1.0D - renderMinY) * (1.0D - renderMaxZ) + var24
					* renderMinY * (1.0D - renderMaxZ) + var25 * renderMinY
					* renderMaxZ);
			var10 = (float) (var22 * (1.0D - renderMinY) * renderMinZ + var23
					* (1.0D - renderMinY) * (1.0D - renderMinZ) + var24
					* renderMinY * (1.0D - renderMinZ) + var25 * renderMinY
					* renderMinZ);
			var11 = (float) (var22 * (1.0D - renderMaxY) * renderMinZ + var23
					* (1.0D - renderMaxY) * (1.0D - renderMinZ) + var24
					* renderMaxY * (1.0D - renderMinZ) + var25 * renderMaxY
					* renderMinZ);
			var12 = (float) (var22 * (1.0D - renderMaxY) * renderMaxZ + var23
					* (1.0D - renderMaxY) * (1.0D - renderMaxZ) + var24
					* renderMaxY * (1.0D - renderMaxZ) + var25 * renderMaxY
					* renderMaxZ);
			var26 = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP,
					aoBrightnessXZPP, var20);
			var27 = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP,
					aoBrightnessXYZPPP, var20);
			var28 = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN,
					aoBrightnessXYPP, var20);
			var29 = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN,
					aoBrightnessXZPN, var20);
			brightnessTopLeft = mixAoBrightness(var26, var29, var28, var27,
					(1.0D - renderMinY) * renderMaxZ, (1.0D - renderMinY)
							* (1.0D - renderMaxZ), renderMinY
							* (1.0D - renderMaxZ), renderMinY * renderMaxZ);
			brightnessBottomLeft = mixAoBrightness(var26, var29, var28, var27,
					(1.0D - renderMinY) * renderMinZ, (1.0D - renderMinY)
							* (1.0D - renderMinZ), renderMinY
							* (1.0D - renderMinZ), renderMinY * renderMinZ);
			brightnessBottomRight = mixAoBrightness(var26, var29, var28, var27,
					(1.0D - renderMaxY) * renderMinZ, (1.0D - renderMaxY)
							* (1.0D - renderMinZ), renderMaxY
							* (1.0D - renderMinZ), renderMaxY * renderMinZ);
			brightnessTopRight = mixAoBrightness(var26, var29, var28, var27,
					(1.0D - renderMaxY) * renderMaxZ, (1.0D - renderMaxY)
							* (1.0D - renderMaxZ), renderMaxY
							* (1.0D - renderMaxZ), renderMaxY * renderMaxZ);

			if (var13) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_ * 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= var9;
			colorGreenTopLeft *= var9;
			colorBlueTopLeft *= var9;
			colorRedBottomLeft *= var10;
			colorGreenBottomLeft *= var10;
			colorBlueBottomLeft *= var10;
			colorRedBottomRight *= var11;
			colorGreenBottomRight *= var11;
			colorBlueBottomRight *= var11;
			colorRedTopRight *= var12;
			colorGreenTopRight *= var12;
			colorBlueTopRight *= var12;
			var30 = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_,
					p_147808_3_, p_147808_4_, 5);
			renderFaceXPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					var30);

			if (fancyGrass && var30.getIconName().equals("grass_side")
					&& !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceXPos(p_147808_1_, p_147808_2_, p_147808_3_,
						p_147808_4_, BlockGrass.func_149990_e());
			}

			var8 = true;
		}

		enableAO = false;
		return var8;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block p_147736_1_,
			int p_147736_2_, int p_147736_3_, int p_147736_4_,
			float p_147736_5_, float p_147736_6_, float p_147736_7_) {
		enableAO = false;
		final boolean defaultTexture = Tessellator.instance.defaultTexture;
		final boolean betterGrass = Config.isBetterGrass() && defaultTexture;
		final Tessellator var8 = Tessellator.instance;
		boolean var9 = false;
		int var26 = -1;
		float var27;
		float var13;
		float var19;
		float var22;

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_,
						p_147736_3_ - 1, p_147736_4_, 0)) {
			if (var26 < 0) {
				var26 = p_147736_1_.getBlockBrightness(blockAccess,
						p_147736_2_, p_147736_3_, p_147736_4_);
			}

			var27 = 0.5F;
			var13 = var27;
			var19 = var27;
			var22 = var27;

			if (p_147736_1_ != Blocks.grass) {
				var13 = var27 * p_147736_5_;
				var19 = var27 * p_147736_6_;
				var22 = var27 * p_147736_7_;
			}

			var8.setBrightness(renderMinY > 0.0D ? var26 : p_147736_1_
					.getBlockBrightness(blockAccess, p_147736_2_,
							p_147736_3_ - 1, p_147736_4_));
			var8.setColorOpaque_F(var13, var19, var22);
			renderFaceYNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_,
							p_147736_3_, p_147736_4_, 0));
			var9 = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_,
						p_147736_3_ + 1, p_147736_4_, 1)) {
			if (var26 < 0) {
				var26 = p_147736_1_.getBlockBrightness(blockAccess,
						p_147736_2_, p_147736_3_, p_147736_4_);
			}

			var27 = 1.0F;
			var13 = var27 * p_147736_5_;
			var19 = var27 * p_147736_6_;
			var22 = var27 * p_147736_7_;
			var8.setBrightness(renderMaxY < 1.0D ? var26 : p_147736_1_
					.getBlockBrightness(blockAccess, p_147736_2_,
							p_147736_3_ + 1, p_147736_4_));
			var8.setColorOpaque_F(var13, var19, var22);
			renderFaceYPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_,
							p_147736_3_, p_147736_4_, 1));
			var9 = true;
		}

		float var25;
		IIcon var271;

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_,
						p_147736_3_, p_147736_4_ - 1, 2)) {
			if (var26 < 0) {
				var26 = p_147736_1_.getBlockBrightness(blockAccess,
						p_147736_2_, p_147736_3_, p_147736_4_);
			}

			var13 = 0.8F;
			var19 = var13;
			var22 = var13;
			var25 = var13;

			if (p_147736_1_ != Blocks.grass) {
				var19 = var13 * p_147736_5_;
				var22 = var13 * p_147736_6_;
				var25 = var13 * p_147736_7_;
			}

			var8.setBrightness(renderMinZ > 0.0D ? var26 : p_147736_1_
					.getBlockBrightness(blockAccess, p_147736_2_, p_147736_3_,
							p_147736_4_ - 1));
			var8.setColorOpaque_F(var19, var22, var25);
			var271 = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_,
					p_147736_3_, p_147736_4_, 2);

			if (betterGrass) {
				if (var271 == TextureUtils.iconGrassSide
						|| var271 == TextureUtils.iconMyceliumSide) {
					var271 = Config.getSideGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 2, var271);

					if (var271 == TextureUtils.iconGrassTop) {
						var8.setColorOpaque_F(var19 * p_147736_5_, var22
								* p_147736_6_, var25 * p_147736_7_);
					}
				}

				if (var271 == TextureUtils.iconGrassSideSnowed) {
					var271 = Config.getSideSnowGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 2);
				}
			}

			renderFaceZNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					var271);

			if (defaultTexture && fancyGrass
					&& var271 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var19 * p_147736_5_, var22 * p_147736_6_,
						var25 * p_147736_7_);
				renderFaceZNeg(p_147736_1_, p_147736_2_, p_147736_3_,
						p_147736_4_, BlockGrass.func_149990_e());
			}

			var9 = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_,
						p_147736_3_, p_147736_4_ + 1, 3)) {
			if (var26 < 0) {
				var26 = p_147736_1_.getBlockBrightness(blockAccess,
						p_147736_2_, p_147736_3_, p_147736_4_);
			}

			var13 = 0.8F;
			var19 = var13;
			var22 = var13;
			var25 = var13;

			if (p_147736_1_ != Blocks.grass) {
				var19 = var13 * p_147736_5_;
				var22 = var13 * p_147736_6_;
				var25 = var13 * p_147736_7_;
			}

			var8.setBrightness(renderMaxZ < 1.0D ? var26 : p_147736_1_
					.getBlockBrightness(blockAccess, p_147736_2_, p_147736_3_,
							p_147736_4_ + 1));
			var8.setColorOpaque_F(var19, var22, var25);
			var271 = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_,
					p_147736_3_, p_147736_4_, 3);

			if (betterGrass) {
				if (var271 == TextureUtils.iconGrassSide
						|| var271 == TextureUtils.iconMyceliumSide) {
					var271 = Config.getSideGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 3, var271);

					if (var271 == TextureUtils.iconGrassTop) {
						var8.setColorOpaque_F(var19 * p_147736_5_, var22
								* p_147736_6_, var25 * p_147736_7_);
					}
				}

				if (var271 == TextureUtils.iconGrassSideSnowed) {
					var271 = Config.getSideSnowGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 3);
				}
			}

			renderFaceZPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					var271);

			if (defaultTexture && fancyGrass
					&& var271 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var19 * p_147736_5_, var22 * p_147736_6_,
						var25 * p_147736_7_);
				renderFaceZPos(p_147736_1_, p_147736_2_, p_147736_3_,
						p_147736_4_, BlockGrass.func_149990_e());
			}

			var9 = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess,
						p_147736_2_ - 1, p_147736_3_, p_147736_4_, 4)) {
			if (var26 < 0) {
				var26 = p_147736_1_.getBlockBrightness(blockAccess,
						p_147736_2_, p_147736_3_, p_147736_4_);
			}

			var13 = 0.6F;
			var19 = var13;
			var22 = var13;
			var25 = var13;

			if (p_147736_1_ != Blocks.grass) {
				var19 = var13 * p_147736_5_;
				var22 = var13 * p_147736_6_;
				var25 = var13 * p_147736_7_;
			}

			var8.setBrightness(renderMinX > 0.0D ? var26 : p_147736_1_
					.getBlockBrightness(blockAccess, p_147736_2_ - 1,
							p_147736_3_, p_147736_4_));
			var8.setColorOpaque_F(var19, var22, var25);
			var271 = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_,
					p_147736_3_, p_147736_4_, 4);

			if (betterGrass) {
				if (var271 == TextureUtils.iconGrassSide
						|| var271 == TextureUtils.iconMyceliumSide) {
					var271 = Config.getSideGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 4, var271);

					if (var271 == TextureUtils.iconGrassTop) {
						var8.setColorOpaque_F(var19 * p_147736_5_, var22
								* p_147736_6_, var25 * p_147736_7_);
					}
				}

				if (var271 == TextureUtils.iconGrassSideSnowed) {
					var271 = Config.getSideSnowGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 4);
				}
			}

			renderFaceXNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					var271);

			if (defaultTexture && fancyGrass
					&& var271 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var19 * p_147736_5_, var22 * p_147736_6_,
						var25 * p_147736_7_);
				renderFaceXNeg(p_147736_1_, p_147736_2_, p_147736_3_,
						p_147736_4_, BlockGrass.func_149990_e());
			}

			var9 = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess,
						p_147736_2_ + 1, p_147736_3_, p_147736_4_, 5)) {
			if (var26 < 0) {
				var26 = p_147736_1_.getBlockBrightness(blockAccess,
						p_147736_2_, p_147736_3_, p_147736_4_);
			}

			var13 = 0.6F;
			var19 = var13;
			var22 = var13;
			var25 = var13;

			if (p_147736_1_ != Blocks.grass) {
				var19 = var13 * p_147736_5_;
				var22 = var13 * p_147736_6_;
				var25 = var13 * p_147736_7_;
			}

			var8.setBrightness(renderMaxX < 1.0D ? var26 : p_147736_1_
					.getBlockBrightness(blockAccess, p_147736_2_ + 1,
							p_147736_3_, p_147736_4_));
			var8.setColorOpaque_F(var19, var22, var25);
			var271 = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_,
					p_147736_3_, p_147736_4_, 5);

			if (betterGrass) {
				if (var271 == TextureUtils.iconGrassSide
						|| var271 == TextureUtils.iconMyceliumSide) {
					var271 = Config.getSideGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 5, var271);

					if (var271 == TextureUtils.iconGrassTop) {
						var8.setColorOpaque_F(var19 * p_147736_5_, var22
								* p_147736_6_, var25 * p_147736_7_);
					}
				}

				if (var271 == TextureUtils.iconGrassSideSnowed) {
					var271 = Config.getSideSnowGrassTexture(blockAccess,
							p_147736_2_, p_147736_3_, p_147736_4_, 5);
				}
			}

			renderFaceXPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					var271);

			if (defaultTexture && fancyGrass
					&& var271 == TextureUtils.iconGrassSide
					&& !hasOverrideBlockTexture()) {
				var8.setColorOpaque_F(var19 * p_147736_5_, var22 * p_147736_6_,
						var25 * p_147736_7_);
				renderFaceXPos(p_147736_1_, p_147736_2_, p_147736_3_,
						p_147736_4_, BlockGrass.func_149990_e());
			}

			var9 = true;
		}

		return var9;
	}

	public void renderTorchAtAngle(Block p_147747_1_, double p_147747_2_,
			double p_147747_4_, double p_147747_6_, double p_147747_8_,
			double p_147747_10_, int p_147747_12_) {
		final Tessellator var13 = Tessellator.instance;
		IIcon var14 = getBlockIconFromSideAndMetadata(p_147747_1_, 0,
				p_147747_12_);

		if (hasOverrideBlockTexture()) {
			var14 = overrideBlockTexture;
		}

		final double var15 = var14.getMinU();
		final double var17 = var14.getMinV();
		final double var19 = var14.getMaxU();
		final double var21 = var14.getMaxV();
		final double var23 = var14.getInterpolatedU(7.0D);
		final double var25 = var14.getInterpolatedV(6.0D);
		final double var27 = var14.getInterpolatedU(9.0D);
		final double var29 = var14.getInterpolatedV(8.0D);
		final double var31 = var14.getInterpolatedU(7.0D);
		final double var33 = var14.getInterpolatedV(13.0D);
		final double var35 = var14.getInterpolatedU(9.0D);
		final double var37 = var14.getInterpolatedV(15.0D);
		p_147747_2_ += 0.5D;
		p_147747_6_ += 0.5D;
		final double var39 = p_147747_2_ - 0.5D;
		final double var41 = p_147747_2_ + 0.5D;
		final double var43 = p_147747_6_ - 0.5D;
		final double var45 = p_147747_6_ + 0.5D;
		final double var47 = 0.0625D;
		final double var49 = 0.625D;
		var13.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - var49)
				- var47, p_147747_4_ + var49, p_147747_6_ + p_147747_10_
				* (1.0D - var49) - var47, var23, var25);
		var13.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - var49)
				- var47, p_147747_4_ + var49, p_147747_6_ + p_147747_10_
				* (1.0D - var49) + var47, var23, var29);
		var13.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - var49)
				+ var47, p_147747_4_ + var49, p_147747_6_ + p_147747_10_
				* (1.0D - var49) + var47, var27, var29);
		var13.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - var49)
				+ var47, p_147747_4_ + var49, p_147747_6_ + p_147747_10_
				* (1.0D - var49) - var47, var27, var25);
		var13.addVertexWithUV(p_147747_2_ + var47 + p_147747_8_, p_147747_4_,
				p_147747_6_ - var47 + p_147747_10_, var35, var33);
		var13.addVertexWithUV(p_147747_2_ + var47 + p_147747_8_, p_147747_4_,
				p_147747_6_ + var47 + p_147747_10_, var35, var37);
		var13.addVertexWithUV(p_147747_2_ - var47 + p_147747_8_, p_147747_4_,
				p_147747_6_ + var47 + p_147747_10_, var31, var37);
		var13.addVertexWithUV(p_147747_2_ - var47 + p_147747_8_, p_147747_4_,
				p_147747_6_ - var47 + p_147747_10_, var31, var33);
		var13.addVertexWithUV(p_147747_2_ - var47, p_147747_4_ + 1.0D, var43,
				var15, var17);
		var13.addVertexWithUV(p_147747_2_ - var47 + p_147747_8_,
				p_147747_4_ + 0.0D, var43 + p_147747_10_, var15, var21);
		var13.addVertexWithUV(p_147747_2_ - var47 + p_147747_8_,
				p_147747_4_ + 0.0D, var45 + p_147747_10_, var19, var21);
		var13.addVertexWithUV(p_147747_2_ - var47, p_147747_4_ + 1.0D, var45,
				var19, var17);
		var13.addVertexWithUV(p_147747_2_ + var47, p_147747_4_ + 1.0D, var45,
				var15, var17);
		var13.addVertexWithUV(p_147747_2_ + p_147747_8_ + var47,
				p_147747_4_ + 0.0D, var45 + p_147747_10_, var15, var21);
		var13.addVertexWithUV(p_147747_2_ + p_147747_8_ + var47,
				p_147747_4_ + 0.0D, var43 + p_147747_10_, var19, var21);
		var13.addVertexWithUV(p_147747_2_ + var47, p_147747_4_ + 1.0D, var43,
				var19, var17);
		var13.addVertexWithUV(var39, p_147747_4_ + 1.0D, p_147747_6_ + var47,
				var15, var17);
		var13.addVertexWithUV(var39 + p_147747_8_, p_147747_4_ + 0.0D,
				p_147747_6_ + var47 + p_147747_10_, var15, var21);
		var13.addVertexWithUV(var41 + p_147747_8_, p_147747_4_ + 0.0D,
				p_147747_6_ + var47 + p_147747_10_, var19, var21);
		var13.addVertexWithUV(var41, p_147747_4_ + 1.0D, p_147747_6_ + var47,
				var19, var17);
		var13.addVertexWithUV(var41, p_147747_4_ + 1.0D, p_147747_6_ - var47,
				var15, var17);
		var13.addVertexWithUV(var41 + p_147747_8_, p_147747_4_ + 0.0D,
				p_147747_6_ - var47 + p_147747_10_, var15, var21);
		var13.addVertexWithUV(var39 + p_147747_8_, p_147747_4_ + 0.0D,
				p_147747_6_ - var47 + p_147747_10_, var19, var21);
		var13.addVertexWithUV(var39, p_147747_4_ + 1.0D, p_147747_6_ - var47,
				var19, var17);
	}

	/**
	 * Sets overrideBlockTexture
	 */
	public void setOverrideBlockTexture(IIcon p_147757_1_) {
		overrideBlockTexture = p_147757_1_;
	}

	public void setRenderAllFaces(boolean p_147753_1_) {
		renderAllFaces = p_147753_1_;
	}

	public void setRenderBounds(double p_147782_1_, double p_147782_3_,
			double p_147782_5_, double p_147782_7_, double p_147782_9_,
			double p_147782_11_) {
		if (!lockBlockBounds) {
			renderMinX = p_147782_1_;
			renderMaxX = p_147782_7_;
			renderMinY = p_147782_3_;
			renderMaxY = p_147782_9_;
			renderMinZ = p_147782_5_;
			renderMaxZ = p_147782_11_;
			partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2
					&& (renderMinX > 0.0D || renderMaxX < 1.0D
							|| renderMinY > 0.0D || renderMaxY < 1.0D
							|| renderMinZ > 0.0D || renderMaxZ < 1.0D);
		}
	}

	public void setRenderBoundsFromBlock(Block p_147775_1_) {
		if (!lockBlockBounds) {
			renderMinX = p_147775_1_.getBlockBoundsMinX();
			renderMaxX = p_147775_1_.getBlockBoundsMaxX();
			renderMinY = p_147775_1_.getBlockBoundsMinY();
			renderMaxY = p_147775_1_.getBlockBoundsMaxY();
			renderMinZ = p_147775_1_.getBlockBoundsMinZ();
			renderMaxZ = p_147775_1_.getBlockBoundsMaxZ();
			partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2
					&& (renderMinX > 0.0D || renderMaxX < 1.0D
							|| renderMinY > 0.0D || renderMaxY < 1.0D
							|| renderMinZ > 0.0D || renderMaxZ < 1.0D);
		}
	}

	public void setRenderFromInside(boolean p_147786_1_) {
		renderFromInside = p_147786_1_;
	}

	/**
	 * Unlocks the visual bounding box so that RenderBlocks can change it again.
	 */
	public void unlockBlockBounds() {
		lockBlockBounds = false;
	}
}
