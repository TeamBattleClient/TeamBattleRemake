package net.minecraft.client.renderer.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class RenderManager {
	public static boolean field_85095_o;

	/** The static instance of RenderManager. */
	public static RenderManager instance = new RenderManager();

	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	/** A map of entity classes and the associated renderer. */
	private final Map entityRenderMap = new HashMap();
	public Entity field_147941_i;
	/** Renders fonts */
	private FontRenderer fontRenderer;

	public ItemRenderer itemRenderer;

	/** Rendermanager's variable for the player */
	public EntityLivingBase livingPlayer;
	/** Reference to the GameSettings object. */
	public GameSettings options;
	public float playerViewX;
	public float playerViewY;

	public TextureManager renderEngine;
	public double viewerPosX;
	public double viewerPosY;
	public double viewerPosZ;
	/** Reference to the World object. */
	public World worldObj;

	private RenderManager() {
		entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider());
		entityRenderMap.put(EntitySpider.class, new RenderSpider());
		entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(),
				new ModelPig(0.5F), 0.7F));
		entityRenderMap.put(EntitySheep.class, new RenderSheep(
				new ModelSheep2(), new ModelSheep1(), 0.7F));
		entityRenderMap.put(EntityCow.class,
				new RenderCow(new ModelCow(), 0.7F));
		entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(
				new ModelCow(), 0.7F));
		entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(),
				new ModelWolf(), 0.5F));
		entityRenderMap.put(EntityChicken.class, new RenderChicken(
				new ModelChicken(), 0.3F));
		entityRenderMap.put(EntityOcelot.class, new RenderOcelot(
				new ModelOcelot(), 0.4F));
		entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish());
		entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		entityRenderMap.put(EntityEnderman.class, new RenderEnderman());
		entityRenderMap.put(EntitySnowman.class, new RenderSnowMan());
		entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton());
		entityRenderMap.put(EntityWitch.class, new RenderWitch());
		entityRenderMap.put(EntityBlaze.class, new RenderBlaze());
		entityRenderMap.put(EntityZombie.class, new RenderZombie());
		entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(
				16), new ModelSlime(0), 0.25F));
		entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube());
		entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(
				new ModelZombie(), 0.5F, 6.0F));
		entityRenderMap.put(EntityGhast.class, new RenderGhast());
		entityRenderMap.put(EntitySquid.class, new RenderSquid(
				new ModelSquid(), 0.7F));
		entityRenderMap.put(EntityVillager.class, new RenderVillager());
		entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem());
		entityRenderMap.put(EntityBat.class, new RenderBat());
		entityRenderMap.put(EntityDragon.class, new RenderDragon());
		entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal());
		entityRenderMap.put(EntityWither.class, new RenderWither());
		entityRenderMap.put(Entity.class, new RenderEntity());
		entityRenderMap.put(EntityPainting.class, new RenderPainting());
		entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame());
		entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot());
		entityRenderMap.put(EntityArrow.class, new RenderArrow());
		entityRenderMap.put(EntitySnowball.class, new RenderSnowball(
				Items.snowball));
		entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(
				Items.ender_pearl));
		entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(
				Items.ender_eye));
		entityRenderMap.put(EntityEgg.class, new RenderSnowball(Items.egg));
		entityRenderMap.put(EntityPotion.class, new RenderSnowball(
				Items.potionitem, 16384));
		entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(
				Items.experience_bottle));
		entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(
				Items.fireworks));
		entityRenderMap
				.put(EntityLargeFireball.class, new RenderFireball(2.0F));
		entityRenderMap
				.put(EntitySmallFireball.class, new RenderFireball(0.5F));
		entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull());
		entityRenderMap.put(EntityItem.class, new RenderItem());
		entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb());
		entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock());
		entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart());
		entityRenderMap.put(EntityMinecartMobSpawner.class,
				new RenderMinecartMobSpawner());
		entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		entityRenderMap.put(EntityBoat.class, new RenderBoat());
		entityRenderMap.put(EntityFishHook.class, new RenderFish());
		entityRenderMap.put(EntityHorse.class, new RenderHorse(
				new ModelHorse(), 0.75F));
		entityRenderMap.put(EntityLightningBolt.class,
				new RenderLightningBolt());
		final Iterator var1 = entityRenderMap.values().iterator();

		while (var1.hasNext()) {
			final Render var2 = (Render) var1.next();
			var2.setRenderManager(this);
		}
	}

	public boolean func_147936_a(Entity p_147936_1_, float p_147936_2_,
			boolean p_147936_3_) {
		if (p_147936_1_.ticksExisted == 0) {
			p_147936_1_.lastTickPosX = p_147936_1_.posX;
			p_147936_1_.lastTickPosY = p_147936_1_.posY;
			p_147936_1_.lastTickPosZ = p_147936_1_.posZ;
		}

		final double var4 = p_147936_1_.lastTickPosX
				+ (p_147936_1_.posX - p_147936_1_.lastTickPosX) * p_147936_2_;
		final double var6 = p_147936_1_.lastTickPosY
				+ (p_147936_1_.posY - p_147936_1_.lastTickPosY) * p_147936_2_;
		final double var8 = p_147936_1_.lastTickPosZ
				+ (p_147936_1_.posZ - p_147936_1_.lastTickPosZ) * p_147936_2_;
		final float var10 = p_147936_1_.prevRotationYaw
				+ (p_147936_1_.rotationYaw - p_147936_1_.prevRotationYaw)
				* p_147936_2_;
		int var11 = p_147936_1_.getBrightnessForRender(p_147936_2_);

		if (p_147936_1_.isBurning()) {
			var11 = 15728880;
		}

		final int var12 = var11 % 65536;
		final int var13 = var11 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				var12 / 1.0F, var13 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		return func_147939_a(p_147936_1_, var4 - renderPosX, var6 - renderPosY,
				var8 - renderPosZ, var10, p_147936_2_, p_147936_3_);
	}

	public boolean func_147937_a(Entity p_147937_1_, float p_147937_2_) {
		return func_147936_a(p_147937_1_, p_147937_2_, false);
	}

	public void func_147938_a(World p_147938_1_, TextureManager p_147938_2_,
			FontRenderer p_147938_3_, EntityLivingBase p_147938_4_,
			Entity p_147938_5_, GameSettings p_147938_6_, float p_147938_7_) {
		worldObj = p_147938_1_;
		renderEngine = p_147938_2_;
		options = p_147938_6_;
		livingPlayer = p_147938_4_;
		field_147941_i = p_147938_5_;
		fontRenderer = p_147938_3_;

		if (p_147938_4_.isPlayerSleeping()) {
			final Block var8 = p_147938_1_.getBlock(
					MathHelper.floor_double(p_147938_4_.posX),
					MathHelper.floor_double(p_147938_4_.posY),
					MathHelper.floor_double(p_147938_4_.posZ));

			if (var8 == Blocks.bed) {
				final int var9 = p_147938_1_.getBlockMetadata(
						MathHelper.floor_double(p_147938_4_.posX),
						MathHelper.floor_double(p_147938_4_.posY),
						MathHelper.floor_double(p_147938_4_.posZ));
				final int var10 = var9 & 3;
				playerViewY = var10 * 90 + 180;
				playerViewX = 0.0F;
			}
		} else {
			playerViewY = p_147938_4_.prevRotationYaw
					+ (p_147938_4_.rotationYaw - p_147938_4_.prevRotationYaw)
					* p_147938_7_;
			playerViewX = p_147938_4_.prevRotationPitch
					+ (p_147938_4_.rotationPitch - p_147938_4_.prevRotationPitch)
					* p_147938_7_;
		}

		if (p_147938_6_.thirdPersonView == 2) {
			playerViewY += 180.0F;
		}

		viewerPosX = p_147938_4_.lastTickPosX
				+ (p_147938_4_.posX - p_147938_4_.lastTickPosX) * p_147938_7_;
		viewerPosY = p_147938_4_.lastTickPosY
				+ (p_147938_4_.posY - p_147938_4_.lastTickPosY) * p_147938_7_;
		viewerPosZ = p_147938_4_.lastTickPosZ
				+ (p_147938_4_.posZ - p_147938_4_.lastTickPosZ) * p_147938_7_;
	}

	public boolean func_147939_a(Entity p_147939_1_, double p_147939_2_,
			double p_147939_4_, double p_147939_6_, float p_147939_8_,
			float p_147939_9_, boolean p_147939_10_) {
		Render var11 = null;

		try {
			var11 = getEntityRenderObject(p_147939_1_);

			if (var11 != null && renderEngine != null) {
				if (!var11.func_147905_a() || p_147939_10_) {
					try {
						var11.doRender(p_147939_1_, p_147939_2_, p_147939_4_,
								p_147939_6_, p_147939_8_, p_147939_9_);
					} catch (final Throwable var18) {
						throw new ReportedException(
								CrashReport.makeCrashReport(var18,
										"Rendering entity in world"));
					}

					try {
						var11.doRenderShadowAndFire(p_147939_1_, p_147939_2_,
								p_147939_4_, p_147939_6_, p_147939_8_,
								p_147939_9_);
					} catch (final Throwable var17) {
						throw new ReportedException(
								CrashReport.makeCrashReport(var17,
										"Post-rendering entity in world"));
					}

					if (field_85095_o && !p_147939_1_.isInvisible()
							&& !p_147939_10_) {
						try {
							func_85094_b(p_147939_1_, p_147939_2_, p_147939_4_,
									p_147939_6_, p_147939_8_, p_147939_9_);
						} catch (final Throwable var16) {
							throw new ReportedException(
									CrashReport.makeCrashReport(var16,
											"Rendering entity hitbox in world"));
						}
					}
				}
			} else if (renderEngine != null)
				return false;

			return true;
		} catch (final Throwable var19) {
			final CrashReport var13 = CrashReport.makeCrashReport(var19,
					"Rendering entity in world");
			final CrashReportCategory var14 = var13
					.makeCategory("Entity being rendered");
			p_147939_1_.addEntityCrashInfo(var14);
			final CrashReportCategory var15 = var13
					.makeCategory("Renderer details");
			var15.addCrashSection("Assigned renderer", var11);
			var15.addCrashSection("Location", CrashReportCategory.func_85074_a(
					p_147939_2_, p_147939_4_, p_147939_6_));
			var15.addCrashSection("Rotation", Float.valueOf(p_147939_8_));
			var15.addCrashSection("Delta", Float.valueOf(p_147939_9_));
			throw new ReportedException(var13);
		}
	}

	public boolean func_147940_a(Entity p_147940_1_, double p_147940_2_,
			double p_147940_4_, double p_147940_6_, float p_147940_8_,
			float p_147940_9_) {
		return func_147939_a(p_147940_1_, p_147940_2_, p_147940_4_,
				p_147940_6_, p_147940_8_, p_147940_9_, false);
	}

	private void func_85094_b(Entity p_85094_1_, double p_85094_2_,
			double p_85094_4_, double p_85094_6_, float p_85094_8_,
			float p_85094_9_) {
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		final float var10 = p_85094_1_.width / 2.0F;
		final AxisAlignedBB var11 = AxisAlignedBB.getBoundingBox(p_85094_2_
				- var10, p_85094_4_, p_85094_6_ - var10, p_85094_2_ + var10,
				p_85094_4_ + p_85094_1_.height, p_85094_6_ + var10);
		RenderGlobal.drawOutlinedBoundingBox(var11, 16777215);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	public double getDistanceToCamera(double p_78714_1_, double p_78714_3_,
			double p_78714_5_) {
		final double var7 = p_78714_1_ - viewerPosX;
		final double var9 = p_78714_3_ - viewerPosY;
		final double var11 = p_78714_5_ - viewerPosZ;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public Render getEntityClassRenderObject(Class p_78715_1_) {
		Render var2 = (Render) entityRenderMap.get(p_78715_1_);

		if (var2 == null && p_78715_1_ != Entity.class) {
			var2 = getEntityClassRenderObject(p_78715_1_.getSuperclass());
			entityRenderMap.put(p_78715_1_, var2);
		}

		return var2;
	}

	public Render getEntityRenderObject(Entity p_78713_1_) {
		return getEntityClassRenderObject(p_78713_1_.getClass());
	}

	/**
	 * Returns the font renderer
	 */
	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}

	/**
	 * World sets this RenderManager's worldObj to the world provided
	 */
	public void set(World p_78717_1_) {
		worldObj = p_78717_1_;
	}

	public void updateIcons(IIconRegister p_94178_1_) {
		final Iterator var2 = entityRenderMap.values().iterator();

		while (var2.hasNext()) {
			final Render var3 = (Render) var2.next();
			var3.updateIcons(p_94178_1_);
		}
	}
}
