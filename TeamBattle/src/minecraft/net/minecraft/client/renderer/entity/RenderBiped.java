package net.minecraft.client.renderer.entity;

import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

public class RenderBiped extends RenderLiving {
	/** List of armor texture filenames. */
	private static final String[] bipedArmorFilenamePrefix = new String[] {
			"leather", "chainmail", "iron", "diamond", "gold" };

	private static final Map field_110859_k = Maps.newHashMap();

	public static ResourceLocation func_110857_a(ItemArmor p_110857_0_,
			int p_110857_1_) {
		return func_110858_a(p_110857_0_, p_110857_1_, (String) null);
	}

	public static ResourceLocation func_110858_a(ItemArmor p_110858_0_,
			int p_110858_1_, String p_110858_2_) {
		final String var3 = String.format(
				"textures/models/armor/%s_layer_%d%s.png",
				new Object[] {
						bipedArmorFilenamePrefix[p_110858_0_.renderIndex],
						Integer.valueOf(p_110858_1_ == 2 ? 2 : 1),
						p_110858_2_ == null ? "" : String.format("_%s",
								new Object[] { p_110858_2_ }) });
		ResourceLocation var4 = (ResourceLocation) field_110859_k.get(var3);

		if (var4 == null) {
			var4 = new ResourceLocation(var3);
			field_110859_k.put(var3, var4);
		}

		return var4;
	}

	protected float field_77070_b;

	protected ModelBiped field_82423_g;

	protected ModelBiped field_82425_h;

	protected ModelBiped modelBipedMain;

	public RenderBiped(ModelBiped p_i1257_1_, float p_i1257_2_) {
		this(p_i1257_1_, p_i1257_2_, 1.0F);
	}

	public RenderBiped(ModelBiped p_i1258_1_, float p_i1258_2_, float p_i1258_3_) {
		super(p_i1258_1_, p_i1258_2_);
		modelBipedMain = p_i1258_1_;
		field_77070_b = p_i1258_3_;
		func_82421_b();
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
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_,
				p_76986_6_, p_76986_8_, p_76986_9_);
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
	public void doRender(EntityLiving p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		final ItemStack var10 = p_76986_1_.getHeldItem();
		func_82420_a(p_76986_1_, var10);
		double var11 = p_76986_4_ - p_76986_1_.yOffset;

		if (p_76986_1_.isSneaking()) {
			var11 -= 0.125D;
		}

		super.doRender(p_76986_1_, p_76986_2_, var11, p_76986_6_, p_76986_8_,
				p_76986_9_);
		field_82423_g.aimedBow = field_82425_h.aimedBow = modelBipedMain.aimedBow = false;
		field_82423_g.isSneak = field_82425_h.isSneak = modelBipedMain.isSneak = false;
		field_82423_g.heldItemRight = field_82425_h.heldItemRight = modelBipedMain.heldItemRight = 0;
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
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_,
				p_76986_6_, p_76986_8_, p_76986_9_);
	}

	protected void func_82408_c(EntityLiving p_82408_1_, int p_82408_2_,
			float p_82408_3_) {
		final ItemStack var4 = p_82408_1_.func_130225_q(3 - p_82408_2_);

		if (var4 != null) {
			final Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				bindTexture(func_110858_a((ItemArmor) var5, p_82408_2_,
						"overlay"));
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
			}
		}
	}

	@Override
	protected void func_82408_c(EntityLivingBase p_82408_1_, int p_82408_2_,
			float p_82408_3_) {
		this.func_82408_c((EntityLiving) p_82408_1_, p_82408_2_, p_82408_3_);
	}

	protected void func_82420_a(EntityLiving p_82420_1_, ItemStack p_82420_2_) {
		field_82423_g.heldItemRight = field_82425_h.heldItemRight = modelBipedMain.heldItemRight = p_82420_2_ != null ? 1
				: 0;
		field_82423_g.isSneak = field_82425_h.isSneak = modelBipedMain.isSneak = p_82420_1_
				.isSneaking();
	}

	protected void func_82421_b() {
		field_82423_g = new ModelBiped(1.0F);
		field_82425_h = new ModelBiped(0.5F);
	}

	protected void func_82422_c() {
		GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityLiving) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_) {
		return null;
	}

	protected void renderEquippedItems(EntityLiving p_77029_1_, float p_77029_2_) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		final ItemStack var3 = p_77029_1_.getHeldItem();
		final ItemStack var4 = p_77029_1_.func_130225_q(3);
		Item var5;
		float var6;

		if (var4 != null) {
			GL11.glPushMatrix();
			modelBipedMain.bipedHead.postRender(0.0625F);
			var5 = var4.getItem();

			if (var5 instanceof ItemBlock) {
				if (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(var5)
						.getRenderType())) {
					var6 = 0.625F;
					GL11.glTranslatef(0.0F, -0.25F, 0.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(var6, -var6, -var6);
				}

				renderManager.itemRenderer.renderItem(p_77029_1_, var4, 0);
			} else if (var5 == Items.skull) {
				var6 = 1.0625F;
				GL11.glScalef(var6, -var6, -var6);
				GameProfile var7 = null;

				if (var4.hasTagCompound()) {
					final NBTTagCompound var8 = var4.getTagCompound();

					if (var8.func_150297_b("SkullOwner", 10)) {
						var7 = NBTUtil.func_152459_a(var8
								.getCompoundTag("SkullOwner"));
					} else if (var8.func_150297_b("SkullOwner", 8)
							&& !StringUtils.isNullOrEmpty(var8
									.getString("SkullOwner"))) {
						var7 = new GameProfile((UUID) null,
								var8.getString("SkullOwner"));
					}
				}

				TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F,
						0.0F, -0.5F, 1, 180.0F, var4.getItemDamage(), var7);
			}

			GL11.glPopMatrix();
		}

		if (var3 != null && var3.getItem() != null) {
			var5 = var3.getItem();
			GL11.glPushMatrix();

			if (mainModel.isChild) {
				var6 = 0.5F;
				GL11.glTranslatef(0.0F, 0.625F, 0.0F);
				GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
				GL11.glScalef(var6, var6, var6);
			}

			modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if (var5 instanceof ItemBlock
					&& RenderBlocks.renderItemIn3d(Block.getBlockFromItem(var5)
							.getRenderType())) {
				var6 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var6 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-var6, -var6, var6);
			} else if (var5 == Items.bow) {
				var6 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var6, -var6, var6);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (var5.isFull3D()) {
				var6 = 0.625F;

				if (var5.shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				func_82422_c();
				GL11.glScalef(var6, -var6, var6);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var6 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var6, var6, var6);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float var9;
			int var11;
			float var14;

			if (var3.getItem().requiresMultipleRenderPasses()) {
				for (var11 = 0; var11 <= 1; ++var11) {
					final int var12 = var3.getItem().getColorFromItemStack(
							var3, var11);
					var14 = (var12 >> 16 & 255) / 255.0F;
					var9 = (var12 >> 8 & 255) / 255.0F;
					final float var10 = (var12 & 255) / 255.0F;
					GL11.glColor4f(var14, var9, var10, 1.0F);
					renderManager.itemRenderer.renderItem(p_77029_1_, var3,
							var11);
				}
			} else {
				var11 = var3.getItem().getColorFromItemStack(var3, 0);
				final float var13 = (var11 >> 16 & 255) / 255.0F;
				var14 = (var11 >> 8 & 255) / 255.0F;
				var9 = (var11 & 255) / 255.0F;
				GL11.glColor4f(var13, var14, var9, 1.0F);
				renderManager.itemRenderer.renderItem(p_77029_1_, var3, 0);
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_,
			float p_77029_2_) {
		this.renderEquippedItems((EntityLiving) p_77029_1_, p_77029_2_);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving p_77032_1_, int p_77032_2_,
			float p_77032_3_) {
		final ItemStack var4 = p_77032_1_.func_130225_q(3 - p_77032_2_);

		if (var4 != null) {
			final Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				final ItemArmor var6 = (ItemArmor) var5;
				bindTexture(func_110857_a(var6, p_77032_2_));
				final ModelBiped var7 = p_77032_2_ == 2 ? field_82425_h
						: field_82423_g;
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
		return this.shouldRenderPass((EntityLiving) p_77032_1_, p_77032_2_,
				p_77032_3_);
	}
}
