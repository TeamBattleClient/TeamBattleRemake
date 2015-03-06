package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class RandomMobs {
	private static Field fieldEntityUuid = getField(Entity.class, UUID.class);

	private static boolean initialized = false;

	private static Random random = new Random();

	private static RenderGlobal renderGlobal = null;

	private static Map textureVariantsMap = new HashMap();

	private static boolean working = false;

	public static void entityLoaded(Entity entity) {
		if (entity instanceof EntityLiving) {
			final EntityLiving el = (EntityLiving) entity;
			final WorldServer ws = Config.getWorldServer();

			if (ws != null) {
				final Entity es = ws.getEntityByID(entity.getEntityId());

				if (es instanceof EntityLiving) {
					final EntityLiving els = (EntityLiving) es;

					if (fieldEntityUuid != null) {
						try {
							final Object e = fieldEntityUuid.get(els);
							fieldEntityUuid.set(el, e);
						} catch (final Exception var6) {
							var6.printStackTrace();
							fieldEntityUuid = null;
						}
					}
				}
			}
		}
	}

	private static int getCountTextureVariants(String prefix, String suffix) {
		final short maxNum = 1000;

		for (int num = 2; num < maxNum; ++num) {
			final String variant = prefix + num + suffix;
			final ResourceLocation loc = new ResourceLocation(variant);

			if (!Config.hasResource(loc))
				return num - 1;
		}

		return maxNum;
	}

	private static Field getField(Class cls, Class fieldType) {
		try {
			final Field[] e = cls.getDeclaredFields();

			for (final Field field : e) {
				final Class type = field.getType();

				if (type == fieldType) {
					field.setAccessible(true);
					return field;
				}
			}

			return null;
		} catch (final Exception var6) {
			var6.printStackTrace();
			return null;
		}
	}

	public static ResourceLocation getTextureLocation(ResourceLocation loc) {
		if (working)
			return loc;
		else {
			ResourceLocation name;

			try {
				working = true;

				if (!initialized) {
					initialize();
				}

				if (renderGlobal == null) {
					final ResourceLocation entity1 = loc;
					return entity1;
				}

				final Entity entity = renderGlobal.renderedEntity;

				if (entity == null) {
					name = loc;
					return name;
				}

				if (entity instanceof EntityLiving) {
					final String name1 = loc.getResourcePath();

					if (!name1.startsWith("textures/entity/")) {
						final ResourceLocation uuidLow1 = loc;
						return uuidLow1;
					}

					final long uuidLow = entity.getUniqueID()
							.getLeastSignificantBits();
					final int id = (int) (uuidLow & 2147483647L);
					final ResourceLocation var6 = getTextureLocation(loc, id);
					return var6;
				}

				name = loc;
			} finally {
				working = false;
			}

			return name;
		}
	}

	private static ResourceLocation getTextureLocation(ResourceLocation loc,
			int randomId) {
		if (randomId <= 0)
			return loc;
		else {
			final String name = loc.getResourcePath();
			ResourceLocation[] texLocs = (ResourceLocation[]) textureVariantsMap
					.get(name);

			if (texLocs == null) {
				texLocs = getTextureVariants(loc);
				textureVariantsMap.put(name, texLocs);
			}

			if (texLocs != null && texLocs.length > 0) {
				final int index = randomId % texLocs.length;
				final ResourceLocation texLoc = texLocs[index];
				return texLoc;
			} else
				return loc;
		}
	}

	private static ResourceLocation[] getTextureVariants(ResourceLocation loc) {
		TextureUtils.getTexture(loc);
		ResourceLocation[] texLocs = new ResourceLocation[0];
		final String name = loc.getResourcePath();
		final int pointPos = name.lastIndexOf(46);

		if (pointPos < 0)
			return texLocs;
		else {
			String prefix = name.substring(0, pointPos);
			final String suffix = name.substring(pointPos);
			final String texEntStr = "textures/entity/";

			if (!prefix.startsWith(texEntStr))
				return texLocs;
			else {
				prefix = prefix.substring(texEntStr.length());
				prefix = "mcpatcher/mob/" + prefix;
				final int countVariants = getCountTextureVariants(prefix,
						suffix);

				if (countVariants <= 1)
					return texLocs;
				else {
					texLocs = new ResourceLocation[countVariants];
					texLocs[0] = loc;

					for (int i = 1; i < texLocs.length; ++i) {
						final int texNum = i + 1;
						final String texName = prefix + texNum + suffix;
						texLocs[i] = new ResourceLocation(
								loc.getResourceDomain(), texName);
						TextureUtils.getTexture(texLocs[i]);
					}

					Config.dbg("RandomMobs: " + loc + ", variants: "
							+ texLocs.length);
					return texLocs;
				}
			}
		}
	}

	private static void initialize() {
		renderGlobal = Config.getRenderGlobal();

		if (renderGlobal != null) {
			initialized = true;
			final ArrayList list = new ArrayList();
			list.add("bat");
			list.add("blaze");
			list.add("cat/black");
			list.add("cat/ocelot");
			list.add("cat/red");
			list.add("cat/siamese");
			list.add("chicken");
			list.add("cow/cow");
			list.add("cow/mooshroom");
			list.add("creeper/creeper");
			list.add("enderman/enderman");
			list.add("enderman/enderman_eyes");
			list.add("ghast/ghast");
			list.add("ghast/ghast_shooting");
			list.add("iron_golem");
			list.add("pig/pig");
			list.add("sheep/sheep");
			list.add("sheep/sheep_fur");
			list.add("silverfish");
			list.add("skeleton/skeleton");
			list.add("skeleton/wither_skeleton");
			list.add("slime/slime");
			list.add("slime/magmacube");
			list.add("snowman");
			list.add("spider/cave_spider");
			list.add("spider/spider");
			list.add("spider_eyes");
			list.add("squid");
			list.add("villager/villager");
			list.add("villager/butcher");
			list.add("villager/farmer");
			list.add("villager/librarian");
			list.add("villager/priest");
			list.add("villager/smith");
			list.add("wither/wither");
			list.add("wither/wither_armor");
			list.add("wither/wither_invulnerable");
			list.add("wolf/wolf");
			list.add("wolf/wolf_angry");
			list.add("wolf/wolf_collar");
			list.add("wolf/wolf_tame");
			list.add("zombie_pigman");
			list.add("zombie/zombie");
			list.add("zombie/zombie_villager");

			for (int i = 0; i < list.size(); ++i) {
				final String name = (String) list.get(i);
				final String tex = "textures/entity/" + name + ".png";
				final ResourceLocation texLoc = new ResourceLocation(tex);

				if (!Config.hasResource(texLoc)) {
					Config.warn("Not found: " + texLoc);
				}

				getTextureLocation(texLoc, 100);
			}
		}
	}

	public static void resetTextures() {
		textureVariantsMap.clear();

		if (Config.isRandomMobs()) {
			initialize();
		}
	}

	public static void worldChanged(World oldWorld, World newWorld) {
		if (newWorld != null) {
			final List entityList = newWorld.getLoadedEntityList();

			for (int e = 0; e < entityList.size(); ++e) {
				final Entity entity = (Entity) entityList.get(e);
				entityLoaded(entity);
			}
		}
	}
}
