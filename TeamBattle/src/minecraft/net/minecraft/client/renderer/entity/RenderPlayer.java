package net.minecraft.client.renderer.entity;

import java.util.UUID;

import me.client.Client;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

public class RenderPlayer extends RendererLivingEntity {
	private static final ResourceLocation steveTextures = new ResourceLocation(
			"textures/entity/steve.png");
	private final ModelBiped modelArmor;
	private final ModelBiped modelArmorChestplate;
	private final ModelBiped modelBipedMain;

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
		modelBipedMain = (ModelBiped) mainModel;
		modelArmorChestplate = new ModelBiped(1.0F);
		modelArmor = new ModelBiped(0.5F);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(AbstractClientPlayer p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		final ItemStack var10 = p_76986_1_.inventory.getCurrentItem();
		modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = var10 != null ? 1
				: 0;

		if (var10 != null && p_76986_1_.getItemInUseCount() > 0) {
			final EnumAction var11 = var10.getItemUseAction();

			if (var11 == EnumAction.block) {
				modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 3;
			} else if (var11 == EnumAction.bow) {
				modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = true;
			}
		}

		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = p_76986_1_
				.isSneaking();
		double var13 = p_76986_4_ - p_76986_1_.yOffset;

		if (p_76986_1_.isSneaking() && !(p_76986_1_ instanceof EntityPlayerSP)) {
			var13 -= 0.125D;
		}

		super.doRender(p_76986_1_, p_76986_2_, var13, p_76986_6_, p_76986_8_,
				p_76986_9_);
		modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = false;
		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = false;
		modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 0;
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((AbstractClientPlayer) p_76986_1_, p_76986_2_,
				p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((AbstractClientPlayer) p_76986_1_, p_76986_2_,
				p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	protected void func_82408_c(AbstractClientPlayer p_82408_1_,
			int p_82408_2_, float p_82408_3_) {
		final ItemStack var4 = p_82408_1_.inventory
				.armorItemInSlot(3 - p_82408_2_);

		if (var4 != null) {
			final Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				bindTexture(RenderBiped.func_110858_a((ItemArmor) var5,
						p_82408_2_, "overlay"));
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
			}
		}
	}

	@Override
	protected void func_82408_c(EntityLivingBase p_82408_1_, int p_82408_2_,
			float p_82408_3_) {
		this.func_82408_c((AbstractClientPlayer) p_82408_1_, p_82408_2_,
				p_82408_3_);
	}

	protected void func_96449_a(AbstractClientPlayer p_96449_1_,
			double p_96449_2_, double p_96449_4_, double p_96449_6_,
			String p_96449_8_, float p_96449_9_, double p_96449_10_) {
		if (p_96449_10_ < 100.0D) {
			final Scoreboard var12 = p_96449_1_.getWorldScoreboard();
			final ScoreObjective var13 = var12.func_96539_a(2);

			if (var13 != null) {
				final Score var14 = var12.func_96529_a(
						p_96449_1_.getCommandSenderName(), var13);

				if (p_96449_1_.isPlayerSleeping()) {
					func_147906_a(p_96449_1_, var14.getScorePoints() + " "
							+ var13.getDisplayName(), p_96449_2_,
							p_96449_4_ - 1.5D, p_96449_6_, 64);
				} else {
					func_147906_a(p_96449_1_, var14.getScorePoints() + " "
							+ var13.getDisplayName(), p_96449_2_, p_96449_4_,
							p_96449_6_, 64);
				}

				p_96449_4_ += getFontRendererFromRenderManager().FONT_HEIGHT
						* 1.15F * p_96449_9_;
			}
		}

		super.func_96449_a(p_96449_1_, p_96449_2_, p_96449_4_, p_96449_6_,
				p_96449_8_, p_96449_9_, p_96449_10_);
	}

	@Override
	protected void func_96449_a(EntityLivingBase p_96449_1_, double p_96449_2_,
			double p_96449_4_, double p_96449_6_, String p_96449_8_,
			float p_96449_9_, double p_96449_10_) {
		this.func_96449_a((AbstractClientPlayer) p_96449_1_, p_96449_2_,
				p_96449_4_, p_96449_6_, p_96449_8_, p_96449_9_, p_96449_10_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(AbstractClientPlayer p_110775_1_) {
		return p_110775_1_.getLocationSkin();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((AbstractClientPlayer) p_110775_1_);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(AbstractClientPlayer p_77041_1_,
			float p_77041_2_) {
		final float var3 = 0.9375F;
		GL11.glScalef(var3, var3, var3);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_,
			float p_77041_2_) {
		this.preRenderCallback((AbstractClientPlayer) p_77041_1_, p_77041_2_);
	}

	protected void renderEquippedItems(AbstractClientPlayer p_77029_1_,
			float p_77029_2_) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		super.renderArrowsStuckInEntity(p_77029_1_, p_77029_2_);
		final ItemStack var3 = p_77029_1_.inventory.armorItemInSlot(3);

		if (var3 != null) {
			GL11.glPushMatrix();
			modelBipedMain.bipedHead.postRender(0.0625F);
			float var4;

			if (var3.getItem() instanceof ItemBlock) {
				if (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(
						var3.getItem()).getRenderType())) {
					var4 = 0.625F;
					GL11.glTranslatef(0.0F, -0.25F, 0.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(var4, -var4, -var4);
				}

				renderManager.itemRenderer.renderItem(p_77029_1_, var3, 0);
			} else if (var3.getItem() == Items.skull) {
				var4 = 1.0625F;
				GL11.glScalef(var4, -var4, -var4);
				GameProfile var5 = null;

				if (var3.hasTagCompound()) {
					final NBTTagCompound var6 = var3.getTagCompound();

					if (var6.func_150297_b("SkullOwner", 10)) {
						var5 = NBTUtil.func_152459_a(var6
								.getCompoundTag("SkullOwner"));
					} else if (var6.func_150297_b("SkullOwner", 8)
							&& !StringUtils.isNullOrEmpty(var6
									.getString("SkullOwner"))) {
						var5 = new GameProfile((UUID) null,
								var6.getString("SkullOwner"));
					}
				}

				TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F,
						0.0F, -0.5F, 1, 180.0F, var3.getItemDamage(), var5);
			}

			GL11.glPopMatrix();
		}

		float var7;

		if (p_77029_1_.getCommandSenderName().equals("deadmau5")
				&& p_77029_1_.func_152123_o()) {
			bindTexture(p_77029_1_.getLocationSkin());

			for (int var20 = 0; var20 < 2; ++var20) {
				final float var22 = p_77029_1_.prevRotationYaw
						+ (p_77029_1_.rotationYaw - p_77029_1_.prevRotationYaw)
						* p_77029_2_
						- (p_77029_1_.prevRenderYawOffset + (p_77029_1_.renderYawOffset - p_77029_1_.prevRenderYawOffset)
								* p_77029_2_);
				final float var25 = p_77029_1_.prevRotationPitch
						+ (p_77029_1_.rotationPitch - p_77029_1_.prevRotationPitch)
						* p_77029_2_;
				GL11.glPushMatrix();
				GL11.glRotatef(var22, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var25, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.375F * (var20 * 2 - 1), 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.375F, 0.0F);
				GL11.glRotatef(-var25, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-var22, 0.0F, 1.0F, 0.0F);
				var7 = 1.3333334F;
				GL11.glScalef(var7, var7, var7);
				modelBipedMain.renderEars(0.0625F);
				GL11.glPopMatrix();
			}
		}

		final boolean var21 = p_77029_1_.func_152122_n();
		final boolean friend = Client.getFriendManager()
				.isFriend(
						StringUtils.stripControlCodes(p_77029_1_
								.getCommandSenderName()));
		final boolean localPlayer = Minecraft.getMinecraft().session
				.getUsername().equals(
						StringUtils.stripControlCodes(p_77029_1_
								.getCommandSenderName()));
		float var11;

		if ((friend || localPlayer || var21) && !p_77029_1_.isInvisible()
				&& !p_77029_1_.getHideCape()) {
			bindTexture(friend || localPlayer ? new ResourceLocation(
					"textures/latemod/cape.png") : p_77029_1_.getLocationCape());
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, 0.125F);
			final double var23 = p_77029_1_.field_71091_bM
					+ (p_77029_1_.field_71094_bP - p_77029_1_.field_71091_bM)
					* p_77029_2_
					- (p_77029_1_.prevPosX + (p_77029_1_.posX - p_77029_1_.prevPosX)
							* p_77029_2_);
			final double var27 = p_77029_1_.field_71096_bN
					+ (p_77029_1_.field_71095_bQ - p_77029_1_.field_71096_bN)
					* p_77029_2_
					- (p_77029_1_.prevPosY + (p_77029_1_.posY - p_77029_1_.prevPosY)
							* p_77029_2_);
			final double var9 = p_77029_1_.field_71097_bO
					+ (p_77029_1_.field_71085_bR - p_77029_1_.field_71097_bO)
					* p_77029_2_
					- (p_77029_1_.prevPosZ + (p_77029_1_.posZ - p_77029_1_.prevPosZ)
							* p_77029_2_);
			var11 = p_77029_1_.prevRenderYawOffset
					+ (p_77029_1_.renderYawOffset - p_77029_1_.prevRenderYawOffset)
					* p_77029_2_;
			final double var12 = MathHelper.sin(var11 * (float) Math.PI
					/ 180.0F);
			final double var14 = -MathHelper.cos(var11 * (float) Math.PI
					/ 180.0F);
			float var16 = (float) var27 * 10.0F;

			if (var16 < -6.0F) {
				var16 = -6.0F;
			}

			if (var16 > 32.0F) {
				var16 = 32.0F;
			}

			float var17 = (float) (var23 * var12 + var9 * var14) * 100.0F;
			final float var18 = (float) (var23 * var14 - var9 * var12) * 100.0F;

			if (var17 < 0.0F) {
				var17 = 0.0F;
			}

			final float var19 = p_77029_1_.prevCameraYaw
					+ (p_77029_1_.cameraYaw - p_77029_1_.prevCameraYaw)
					* p_77029_2_;
			var16 += MathHelper
					.sin((p_77029_1_.prevDistanceWalkedModified + (p_77029_1_.distanceWalkedModified - p_77029_1_.prevDistanceWalkedModified)
							* p_77029_2_) * 6.0F)
					* 32.0F * var19;

			if (p_77029_1_.isSneaking()) {
				var16 += 25.0F;
			}

			GL11.glRotatef(6.0F + var17 / 2.0F + var16, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var18 / 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var18 / 2.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			modelBipedMain.renderCloak(0.0625F);
			GL11.glPopMatrix();
		}

		ItemStack var24 = p_77029_1_.inventory.getCurrentItem();

		if (var24 != null) {
			GL11.glPushMatrix();
			modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if (p_77029_1_.fishEntity != null) {
				var24 = new ItemStack(Items.stick);
			}

			EnumAction var26 = null;

			if (p_77029_1_.getItemInUseCount() > 0) {
				var26 = var24.getItemUseAction();
			}

			if (var24.getItem() instanceof ItemBlock
					&& RenderBlocks.renderItemIn3d(Block.getBlockFromItem(
							var24.getItem()).getRenderType())) {
				var7 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var7 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-var7, -var7, var7);
			} else if (var24.getItem() == Items.bow) {
				var7 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var7, -var7, var7);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (var24.getItem().isFull3D()) {
				var7 = 0.625F;

				if (var24.getItem().shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (p_77029_1_.getItemInUseCount() > 0
						&& var26 == EnumAction.block) {
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(var7, -var7, var7);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var7 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var7, var7, var7);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float var10;
			int var28;
			float var30;

			if (var24.getItem().requiresMultipleRenderPasses()) {
				for (var28 = 0; var28 <= 1; ++var28) {
					final int var8 = var24.getItem().getColorFromItemStack(
							var24, var28);
					var30 = (var8 >> 16 & 255) / 255.0F;
					var10 = (var8 >> 8 & 255) / 255.0F;
					var11 = (var8 & 255) / 255.0F;
					GL11.glColor4f(var30, var10, var11, 1.0F);
					renderManager.itemRenderer.renderItem(p_77029_1_, var24,
							var28);
				}
			} else {
				var28 = var24.getItem().getColorFromItemStack(var24, 0);
				final float var29 = (var28 >> 16 & 255) / 255.0F;
				var30 = (var28 >> 8 & 255) / 255.0F;
				var10 = (var28 & 255) / 255.0F;
				GL11.glColor4f(var29, var30, var10, 1.0F);
				renderManager.itemRenderer.renderItem(p_77029_1_, var24, 0);
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_,
			float p_77029_2_) {
		this.renderEquippedItems((AbstractClientPlayer) p_77029_1_, p_77029_2_);
	}

	public void renderFirstPersonArm(EntityPlayer p_82441_1_) {
		final float var2 = 1.0F;
		GL11.glColor3f(var2, var2, var2);
		modelBipedMain.onGround = 0.0F;
		modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F,
				p_82441_1_);
		modelBipedMain.bipedRightArm.render(0.0625F);
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(AbstractClientPlayer p_77039_1_,
			double p_77039_2_, double p_77039_4_, double p_77039_6_) {
		if (p_77039_1_.isEntityAlive() && p_77039_1_.isPlayerSleeping()) {
			super.renderLivingAt(p_77039_1_, p_77039_2_
					+ p_77039_1_.field_71079_bU, p_77039_4_
					+ p_77039_1_.field_71082_cx, p_77039_6_
					+ p_77039_1_.field_71089_bV);
		} else {
			super.renderLivingAt(p_77039_1_, p_77039_2_, p_77039_4_, p_77039_6_);
		}
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	@Override
	protected void renderLivingAt(EntityLivingBase p_77039_1_,
			double p_77039_2_, double p_77039_4_, double p_77039_6_) {
		this.renderLivingAt((AbstractClientPlayer) p_77039_1_, p_77039_2_,
				p_77039_4_, p_77039_6_);
	}

	protected void rotateCorpse(AbstractClientPlayer p_77043_1_,
			float p_77043_2_, float p_77043_3_, float p_77043_4_) {
		if (p_77043_1_.isEntityAlive() && p_77043_1_.isPlayerSleeping()) {
			GL11.glRotatef(p_77043_1_.getBedOrientationInDegrees(), 0.0F, 1.0F,
					0.0F);
			GL11.glRotatef(getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
		}
	}

	@Override
	protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_,
			float p_77043_3_, float p_77043_4_) {
		this.rotateCorpse((AbstractClientPlayer) p_77043_1_, p_77043_2_,
				p_77043_3_, p_77043_4_);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(AbstractClientPlayer p_77032_1_,
			int p_77032_2_, float p_77032_3_) {
		final ItemStack var4 = p_77032_1_.inventory
				.armorItemInSlot(3 - p_77032_2_);

		if (var4 != null) {
			final Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				final ItemArmor var6 = (ItemArmor) var5;
				bindTexture(RenderBiped.func_110857_a(var6, p_77032_2_));
				final ModelBiped var7 = p_77032_2_ == 2 ? modelArmor
						: modelArmorChestplate;
				var7.bipedHead.showModel = p_77032_2_ == 0;
				var7.bipedHeadwear.showModel = p_77032_2_ == 0;
				var7.bipedBody.showModel = p_77032_2_ == 1 || p_77032_2_ == 2;
				var7.bipedRightArm.showModel = p_77032_2_ == 1;
				var7.bipedLeftArm.showModel = p_77032_2_ == 1;
				var7.bipedRightLeg.showModel = p_77032_2_ == 2
						|| p_77032_2_ == 3;
				var7.bipedLeftLeg.showModel = p_77032_2_ == 2
						|| p_77032_2_ == 3;
				setRenderPassModel(var7);
				var7.onGround = mainModel.onGround;
				var7.isRiding = mainModel.isRiding;
				var7.isChild = mainModel.isChild;

				if (var6.getArmorMaterial() == ItemArmor.ArmorMaterial.CLOTH) {
					final int var8 = var6.getColor(var4);
					final float var9 = (var8 >> 16 & 255) / 255.0F;
					final float var10 = (var8 >> 8 & 255) / 255.0F;
					final float var11 = (var8 & 255) / 255.0F;
					GL11.glColor3f(var9, var10, var11);

					if (var4.isItemEnchanted())
						return 31;

					return 16;
				}

				GL11.glColor3f(1.0F, 1.0F, 1.0F);

				if (var4.isItemEnchanted())
					return 15;

				return 1;
			}
		}

		return -1;
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_,
			float p_77032_3_) {
		return this.shouldRenderPass((AbstractClientPlayer) p_77032_1_,
				p_77032_2_, p_77032_3_);
	}
}
