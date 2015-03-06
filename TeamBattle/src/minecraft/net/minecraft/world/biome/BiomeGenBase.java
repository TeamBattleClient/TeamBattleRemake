package net.minecraft.world.biome;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;

public abstract class BiomeGenBase {
	public static class Height {
		public float field_150777_a;
		public float field_150776_b;

		public Height(float p_i45371_1_, float p_i45371_2_) {
			field_150777_a = p_i45371_1_;
			field_150776_b = p_i45371_2_;
		}

		public BiomeGenBase.Height func_150775_a() {
			return new BiomeGenBase.Height(field_150777_a * 0.8F,
					field_150776_b * 0.6F);
		}
	}

	public static class SpawnListEntry extends WeightedRandom.Item {
		public Class entityClass;
		public int minGroupCount;
		public int maxGroupCount;

		public SpawnListEntry(Class p_i1970_1_, int p_i1970_2_, int p_i1970_3_,
				int p_i1970_4_) {
			super(p_i1970_2_);
			entityClass = p_i1970_1_;
			minGroupCount = p_i1970_3_;
			maxGroupCount = p_i1970_4_;
		}

		@Override
		public String toString() {
			return entityClass.getSimpleName() + "*(" + minGroupCount + "-"
					+ maxGroupCount + "):" + itemWeight;
		}
	}

	public static enum TempCategory {
		OCEAN("OCEAN", 0), COLD("COLD", 1), MEDIUM("MEDIUM", 2), WARM("WARM", 3);

		private TempCategory(String p_i45372_1_, int p_i45372_2_) {
		}
	}

	public static BiomeGenBase func_150568_d(int p_150568_0_) {
		if (p_150568_0_ >= 0 && p_150568_0_ <= biomeList.length)
			return biomeList[p_150568_0_];
		else {
			logger.warn("Biome ID is out of bounds: " + p_150568_0_
					+ ", defaulting to 0 (Ocean)");
			return ocean;
		}
	}

	public static BiomeGenBase[] getBiomeGenArray() {
		return biomeList;
	}

	private static final Logger logger = LogManager.getLogger();
	protected static final BiomeGenBase.Height field_150596_a = new BiomeGenBase.Height(
			0.1F, 0.2F);
	protected static final BiomeGenBase.Height field_150594_b = new BiomeGenBase.Height(
			-0.5F, 0.0F);
	protected static final BiomeGenBase.Height field_150595_c = new BiomeGenBase.Height(
			-1.0F, 0.1F);
	protected static final BiomeGenBase.Height field_150592_d = new BiomeGenBase.Height(
			-1.8F, 0.1F);
	protected static final BiomeGenBase.Height field_150593_e = new BiomeGenBase.Height(
			0.125F, 0.05F);
	protected static final BiomeGenBase.Height field_150590_f = new BiomeGenBase.Height(
			0.2F, 0.2F);
	protected static final BiomeGenBase.Height field_150591_g = new BiomeGenBase.Height(
			0.45F, 0.3F);
	protected static final BiomeGenBase.Height field_150602_h = new BiomeGenBase.Height(
			1.5F, 0.025F);

	protected static final BiomeGenBase.Height field_150603_i = new BiomeGenBase.Height(
			1.0F, 0.5F);
	protected static final BiomeGenBase.Height field_150600_j = new BiomeGenBase.Height(
			0.0F, 0.025F);
	protected static final BiomeGenBase.Height field_150601_k = new BiomeGenBase.Height(
			0.1F, 0.8F);
	protected static final BiomeGenBase.Height field_150598_l = new BiomeGenBase.Height(
			0.2F, 0.3F);
	protected static final BiomeGenBase.Height field_150599_m = new BiomeGenBase.Height(
			-0.2F, 0.1F);
	/** An array of all the biomes, indexed by biome id. */
	private static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
	public static final Set field_150597_n = Sets.newHashSet();
	public static final BiomeGenBase ocean = new BiomeGenOcean(0).setColor(112)
			.setBiomeName("Ocean").func_150570_a(field_150595_c);
	public static final BiomeGenBase plains = new BiomeGenPlains(1).setColor(
			9286496).setBiomeName("Plains");
	public static final BiomeGenBase desert = new BiomeGenDesert(2)
			.setColor(16421912).setBiomeName("Desert").setDisableRain()
			.setTemperatureRainfall(2.0F, 0.0F).func_150570_a(field_150593_e);
	public static final BiomeGenBase extremeHills = new BiomeGenHills(3, false)
			.setColor(6316128).setBiomeName("Extreme Hills")
			.func_150570_a(field_150603_i).setTemperatureRainfall(0.2F, 0.3F);

	public static final BiomeGenBase forest = new BiomeGenForest(4, 0)
			.setColor(353825).setBiomeName("Forest");
	public static final BiomeGenBase taiga = new BiomeGenTaiga(5, 0)
			.setColor(747097).setBiomeName("Taiga").func_76733_a(5159473)
			.setTemperatureRainfall(0.25F, 0.8F).func_150570_a(field_150590_f);
	public static final BiomeGenBase swampland = new BiomeGenSwamp(6)
			.setColor(522674).setBiomeName("Swampland").func_76733_a(9154376)
			.func_150570_a(field_150599_m).setTemperatureRainfall(0.8F, 0.9F);
	public static final BiomeGenBase river = new BiomeGenRiver(7).setColor(255)
			.setBiomeName("River").func_150570_a(field_150594_b);
	public static final BiomeGenBase hell = new BiomeGenHell(8)
			.setColor(16711680).setBiomeName("Hell").setDisableRain()
			.setTemperatureRainfall(2.0F, 0.0F);
	/** Is the biome used for sky world. */
	public static final BiomeGenBase sky = new BiomeGenEnd(9).setColor(8421631)
			.setBiomeName("Sky").setDisableRain();
	public static final BiomeGenBase frozenOcean = new BiomeGenOcean(10)
			.setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow()
			.func_150570_a(field_150595_c).setTemperatureRainfall(0.0F, 0.5F);

	public static final BiomeGenBase frozenRiver = new BiomeGenRiver(11)
			.setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow()
			.func_150570_a(field_150594_b).setTemperatureRainfall(0.0F, 0.5F);

	public static final BiomeGenBase icePlains = new BiomeGenSnow(12, false)
			.setColor(16777215).setBiomeName("Ice Plains").setEnableSnow()
			.setTemperatureRainfall(0.0F, 0.5F).func_150570_a(field_150593_e);

	public static final BiomeGenBase iceMountains = new BiomeGenSnow(13, false)
			.setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow()
			.func_150570_a(field_150591_g).setTemperatureRainfall(0.0F, 0.5F);

	public static final BiomeGenBase mushroomIsland = new BiomeGenMushroomIsland(
			14).setColor(16711935).setBiomeName("MushroomIsland")
			.setTemperatureRainfall(0.9F, 1.0F).func_150570_a(field_150598_l);

	public static final BiomeGenBase mushroomIslandShore = new BiomeGenMushroomIsland(
			15).setColor(10486015).setBiomeName("MushroomIslandShore")
			.setTemperatureRainfall(0.9F, 1.0F).func_150570_a(field_150600_j);

	/** Beach biome. */
	public static final BiomeGenBase beach = new BiomeGenBeach(16)
			.setColor(16440917).setBiomeName("Beach")
			.setTemperatureRainfall(0.8F, 0.4F).func_150570_a(field_150600_j);
	/** Desert Hills biome. */
	public static final BiomeGenBase desertHills = new BiomeGenDesert(17)
			.setColor(13786898).setBiomeName("DesertHills").setDisableRain()
			.setTemperatureRainfall(2.0F, 0.0F).func_150570_a(field_150591_g);
	/** Forest Hills biome. */
	public static final BiomeGenBase forestHills = new BiomeGenForest(18, 0)
			.setColor(2250012).setBiomeName("ForestHills")
			.func_150570_a(field_150591_g);
	/** Taiga Hills biome. */
	public static final BiomeGenBase taigaHills = new BiomeGenTaiga(19, 0)
			.setColor(1456435).setBiomeName("TaigaHills").func_76733_a(5159473)
			.setTemperatureRainfall(0.25F, 0.8F).func_150570_a(field_150591_g);
	/** Extreme Hills Edge biome. */
	public static final BiomeGenBase extremeHillsEdge = new BiomeGenHills(20,
			true).setColor(7501978).setBiomeName("Extreme Hills Edge")
			.func_150570_a(field_150603_i.func_150775_a())
			.setTemperatureRainfall(0.2F, 0.3F);
	/** Jungle biome identifier */
	public static final BiomeGenBase jungle = new BiomeGenJungle(21, false)
			.setColor(5470985).setBiomeName("Jungle").func_76733_a(5470985)
			.setTemperatureRainfall(0.95F, 0.9F);
	public static final BiomeGenBase jungleHills = new BiomeGenJungle(22, false)
			.setColor(2900485).setBiomeName("JungleHills")
			.func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.9F)
			.func_150570_a(field_150591_g);
	public static final BiomeGenBase field_150574_L = new BiomeGenJungle(23,
			true).setColor(6458135).setBiomeName("JungleEdge")
			.func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.8F);
	public static final BiomeGenBase field_150575_M = new BiomeGenOcean(24)
			.setColor(48).setBiomeName("Deep Ocean")
			.func_150570_a(field_150592_d);
	public static final BiomeGenBase field_150576_N = new BiomeGenStoneBeach(25)
			.setColor(10658436).setBiomeName("Stone Beach")
			.setTemperatureRainfall(0.2F, 0.3F).func_150570_a(field_150601_k);
	public static final BiomeGenBase field_150577_O = new BiomeGenBeach(26)
			.setColor(16445632).setBiomeName("Cold Beach")
			.setTemperatureRainfall(0.05F, 0.3F).func_150570_a(field_150600_j)
			.setEnableSnow();
	public static final BiomeGenBase field_150583_P = new BiomeGenForest(27, 2)
			.setBiomeName("Birch Forest").setColor(3175492);
	public static final BiomeGenBase field_150582_Q = new BiomeGenForest(28, 2)
			.setBiomeName("Birch Forest Hills").setColor(2055986)
			.func_150570_a(field_150591_g);
	public static final BiomeGenBase field_150585_R = new BiomeGenForest(29, 3)
			.setColor(4215066).setBiomeName("Roofed Forest");
	public static final BiomeGenBase field_150584_S = new BiomeGenTaiga(30, 0)
			.setColor(3233098).setBiomeName("Cold Taiga").func_76733_a(5159473)
			.setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F)
			.func_150570_a(field_150590_f).func_150563_c(16777215);
	public static final BiomeGenBase field_150579_T = new BiomeGenTaiga(31, 0)
			.setColor(2375478).setBiomeName("Cold Taiga Hills")
			.func_76733_a(5159473).setEnableSnow()
			.setTemperatureRainfall(-0.5F, 0.4F).func_150570_a(field_150591_g)
			.func_150563_c(16777215);
	public static final BiomeGenBase field_150578_U = new BiomeGenTaiga(32, 1)
			.setColor(5858897).setBiomeName("Mega Taiga").func_76733_a(5159473)
			.setTemperatureRainfall(0.3F, 0.8F).func_150570_a(field_150590_f);
	public static final BiomeGenBase field_150581_V = new BiomeGenTaiga(33, 1)
			.setColor(4542270).setBiomeName("Mega Taiga Hills")
			.func_76733_a(5159473).setTemperatureRainfall(0.3F, 0.8F)
			.func_150570_a(field_150591_g);
	public static final BiomeGenBase field_150580_W = new BiomeGenHills(34,
			true).setColor(5271632).setBiomeName("Extreme Hills+")
			.func_150570_a(field_150603_i).setTemperatureRainfall(0.2F, 0.3F);
	public static final BiomeGenBase field_150588_X = new BiomeGenSavanna(35)
			.setColor(12431967).setBiomeName("Savanna")
			.setTemperatureRainfall(1.2F, 0.0F).setDisableRain()
			.func_150570_a(field_150593_e);
	public static final BiomeGenBase field_150587_Y = new BiomeGenSavanna(36)
			.setColor(10984804).setBiomeName("Savanna Plateau")
			.setTemperatureRainfall(1.0F, 0.0F).setDisableRain()
			.func_150570_a(field_150602_h);
	public static final BiomeGenBase field_150589_Z = new BiomeGenMesa(37,
			false, false).setColor(14238997).setBiomeName("Mesa");
	public static final BiomeGenBase field_150607_aa = new BiomeGenMesa(38,
			false, true).setColor(11573093).setBiomeName("Mesa Plateau F")
			.func_150570_a(field_150602_h);
	public static final BiomeGenBase field_150608_ab = new BiomeGenMesa(39,
			false, false).setColor(13274213).setBiomeName("Mesa Plateau")
			.func_150570_a(field_150602_h);
	protected static final NoiseGeneratorPerlin field_150605_ac;

	protected static final NoiseGeneratorPerlin field_150606_ad;
	protected static final WorldGenDoublePlant field_150610_ae;

	public String biomeName;
	public int color;

	public int field_150609_ah;

	/** The block expected to be on the top of this biome */
	public Block topBlock;

	public int field_150604_aj;

	/** The block to fill spots in when not on the top */
	public Block fillerBlock;

	public int field_76754_C;

	/** The minimum height of this biome. Default 0.1. */
	public float minHeight;

	/** The maximum height of this biome. Default 0.3. */
	public float maxHeight;

	/** The temperature of this biome. */
	public float temperature;

	/** The rainfall in this biome. */
	public float rainfall;
	/** Color tint applied to water depending on biome */
	public int waterColorMultiplier;

	/** The biome decorator. */
	public BiomeDecorator theBiomeDecorator;

	/**
	 * Holds the classes of IMobs (hostile mobs) that can be spawned in the
	 * biome.
	 */
	protected List spawnableMonsterList;

	/**
	 * Holds the classes of any creature that can be spawned in the biome as
	 * friendly creature.
	 */
	protected List spawnableCreatureList;

	/**
	 * Holds the classes of any aquatic creature that can be spawned in the
	 * water of the biome.
	 */
	protected List spawnableWaterCreatureList;

	protected List spawnableCaveCreatureList;

	/** Set to true if snow is enabled for this biome. */
	protected boolean enableSnow;

	/**
	 * Is true (default) if the biome support rain (desert and nether can't have
	 * rain)
	 */
	protected boolean enableRain;

	/** The id number to this biome, and its index in the biomeList array. */
	public final int biomeID;

	/** The tree generator. */
	protected WorldGenTrees worldGeneratorTrees;

	/** The big tree generator. */
	protected WorldGenBigTree worldGeneratorBigTree;

	/** The swamp tree generator. */
	protected WorldGenSwamp worldGeneratorSwamp;

	static {
		plains.func_150566_k();
		desert.func_150566_k();
		forest.func_150566_k();
		taiga.func_150566_k();
		swampland.func_150566_k();
		icePlains.func_150566_k();
		jungle.func_150566_k();
		field_150574_L.func_150566_k();
		field_150584_S.func_150566_k();
		field_150588_X.func_150566_k();
		field_150587_Y.func_150566_k();
		field_150589_Z.func_150566_k();
		field_150607_aa.func_150566_k();
		field_150608_ab.func_150566_k();
		field_150583_P.func_150566_k();
		field_150582_Q.func_150566_k();
		field_150585_R.func_150566_k();
		field_150578_U.func_150566_k();
		extremeHills.func_150566_k();
		field_150580_W.func_150566_k();
		biomeList[field_150581_V.biomeID + 128] = biomeList[field_150578_U.biomeID + 128];
		final BiomeGenBase[] var0 = biomeList;
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			final BiomeGenBase var3 = var0[var2];

			if (var3 != null && var3.biomeID < 128) {
				field_150597_n.add(var3);
			}
		}

		field_150597_n.remove(hell);
		field_150597_n.remove(sky);
		field_150597_n.remove(frozenOcean);
		field_150597_n.remove(extremeHillsEdge);
		field_150605_ac = new NoiseGeneratorPerlin(new Random(1234L), 1);
		field_150606_ad = new NoiseGeneratorPerlin(new Random(2345L), 1);
		field_150610_ae = new WorldGenDoublePlant();
	}

	protected BiomeGenBase(int p_i1971_1_) {
		topBlock = Blocks.grass;
		field_150604_aj = 0;
		fillerBlock = Blocks.dirt;
		field_76754_C = 5169201;
		minHeight = field_150596_a.field_150777_a;
		maxHeight = field_150596_a.field_150776_b;
		temperature = 0.5F;
		rainfall = 0.5F;
		waterColorMultiplier = 16777215;
		spawnableMonsterList = new ArrayList();
		spawnableCreatureList = new ArrayList();
		spawnableWaterCreatureList = new ArrayList();
		spawnableCaveCreatureList = new ArrayList();
		enableRain = true;
		worldGeneratorTrees = new WorldGenTrees(false);
		worldGeneratorBigTree = new WorldGenBigTree(false);
		worldGeneratorSwamp = new WorldGenSwamp();
		biomeID = p_i1971_1_;
		biomeList[p_i1971_1_] = this;
		theBiomeDecorator = createBiomeDecorator();
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(
				EntitySheep.class, 12, 4, 4));
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(
				EntityPig.class, 10, 4, 4));
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(
				EntityChicken.class, 10, 4, 4));
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(
				EntityCow.class, 8, 4, 4));
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntitySpider.class, 100, 4, 4));
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntityZombie.class, 100, 4, 4));
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntitySkeleton.class, 100, 4, 4));
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntityCreeper.class, 100, 4, 4));
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntitySlime.class, 100, 4, 4));
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntityEnderman.class, 10, 1, 4));
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntityWitch.class, 5, 1, 1));
		spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(
				EntitySquid.class, 10, 4, 4));
		spawnableCaveCreatureList.add(new BiomeGenBase.SpawnListEntry(
				EntityBat.class, 10, 8, 8));
	}

	/**
	 * Return true if the biome supports lightning bolt spawn, either by have
	 * the bolts enabled and have rain enabled.
	 */
	public boolean canSpawnLightningBolt() {
		return func_150559_j() ? false : enableRain;
	}

	/**
	 * Allocate a new BiomeDecorator for this BiomeGenBase
	 */
	protected BiomeDecorator createBiomeDecorator() {
		return new BiomeDecorator();
	}

	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_,
			int p_76728_4_) {
		theBiomeDecorator.func_150512_a(p_76728_1_, p_76728_2_, this,
				p_76728_3_, p_76728_4_);
	}

	protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_) {
		color = p_150557_1_;

		if (p_150557_2_) {
			field_150609_ah = (p_150557_1_ & 16711422) >> 1;
		} else {
			field_150609_ah = p_150557_1_;
		}

		return this;
	}

	public boolean func_150559_j() {
		return enableSnow;
	}

	public final void func_150560_b(World p_150560_1_, Random p_150560_2_,
			Block[] p_150560_3_, byte[] p_150560_4_, int p_150560_5_,
			int p_150560_6_, double p_150560_7_) {
		Block var10 = topBlock;
		byte var11 = (byte) (field_150604_aj & 255);
		Block var12 = fillerBlock;
		int var13 = -1;
		final int var14 = (int) (p_150560_7_ / 3.0D + 3.0D + p_150560_2_
				.nextDouble() * 0.25D);
		final int var15 = p_150560_5_ & 15;
		final int var16 = p_150560_6_ & 15;
		final int var17 = p_150560_3_.length / 256;

		for (int var18 = 255; var18 >= 0; --var18) {
			final int var19 = (var16 * 16 + var15) * var17 + var18;

			if (var18 <= 0 + p_150560_2_.nextInt(5)) {
				p_150560_3_[var19] = Blocks.bedrock;
			} else {
				final Block var20 = p_150560_3_[var19];

				if (var20 != null && var20.getMaterial() != Material.air) {
					if (var20 == Blocks.stone) {
						if (var13 == -1) {
							if (var14 <= 0) {
								var10 = null;
								var11 = 0;
								var12 = Blocks.stone;
							} else if (var18 >= 59 && var18 <= 64) {
								var10 = topBlock;
								var11 = (byte) (field_150604_aj & 255);
								var12 = fillerBlock;
							}

							if (var18 < 63
									&& (var10 == null || var10.getMaterial() == Material.air)) {
								if (getFloatTemperature(p_150560_5_, var18,
										p_150560_6_) < 0.15F) {
									var10 = Blocks.ice;
									var11 = 0;
								} else {
									var10 = Blocks.water;
									var11 = 0;
								}
							}

							var13 = var14;

							if (var18 >= 62) {
								p_150560_3_[var19] = var10;
								p_150560_4_[var19] = var11;
							} else if (var18 < 56 - var14) {
								var10 = null;
								var12 = Blocks.stone;
								p_150560_3_[var19] = Blocks.gravel;
							} else {
								p_150560_3_[var19] = var12;
							}
						} else if (var13 > 0) {
							--var13;
							p_150560_3_[var19] = var12;

							if (var13 == 0 && var12 == Blocks.sand) {
								var13 = p_150560_2_.nextInt(4)
										+ Math.max(0, var18 - 63);
								var12 = Blocks.sandstone;
							}
						}
					}
				} else {
					var13 = -1;
				}
			}
		}
	}

	public BiomeGenBase.TempCategory func_150561_m() {
		return temperature < 0.2D ? BiomeGenBase.TempCategory.COLD
				: temperature < 1.0D ? BiomeGenBase.TempCategory.MEDIUM
						: BiomeGenBase.TempCategory.WARM;
	}

	public Class func_150562_l() {
		return this.getClass();
	}

	protected BiomeGenBase func_150563_c(int p_150563_1_) {
		field_150609_ah = p_150563_1_;
		return this;
	}

	protected BiomeGenBase func_150566_k() {
		return new BiomeGenMutated(biomeID + 128, this);
	}

	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return p_150567_1_.nextInt(10) == 0 ? worldGeneratorBigTree
				: worldGeneratorTrees;
	}

	public boolean func_150569_a(BiomeGenBase p_150569_1_) {
		return p_150569_1_ == this ? true : p_150569_1_ == null ? false
				: func_150562_l() == p_150569_1_.func_150562_l();
	}

	protected final BiomeGenBase func_150570_a(BiomeGenBase.Height p_150570_1_) {
		minHeight = p_150570_1_.field_150777_a;
		maxHeight = p_150570_1_.field_150776_b;
		return this;
	}

	public String func_150572_a(Random p_150572_1_, int p_150572_2_,
			int p_150572_3_, int p_150572_4_) {
		return p_150572_1_.nextInt(3) > 0 ? BlockFlower.field_149858_b[0]
				: BlockFlower.field_149859_a[0];
	}

	public void func_150573_a(World p_150573_1_, Random p_150573_2_,
			Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_,
			int p_150573_6_, double p_150573_7_) {
		func_150560_b(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_,
				p_150573_5_, p_150573_6_, p_150573_7_);
	}

	protected BiomeGenBase func_76733_a(int p_76733_1_) {
		field_76754_C = p_76733_1_;
		return this;
	}

	/**
	 * Provides the basic foliage color based on the biome temperature and
	 * rainfall
	 */
	public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_,
			int p_150571_3_) {
		final double var4 = MathHelper.clamp_float(
				getFloatTemperature(p_150571_1_, p_150571_2_, p_150571_3_),
				0.0F, 1.0F);
		final double var6 = MathHelper.clamp_float(getFloatRainfall(), 0.0F,
				1.0F);
		return ColorizerFoliage.getFoliageColor(var4, var6);
	}

	/**
	 * Provides the basic grass color based on the biome temperature and
	 * rainfall
	 */
	public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_,
			int p_150558_3_) {
		final double var4 = MathHelper.clamp_float(
				getFloatTemperature(p_150558_1_, p_150558_2_, p_150558_3_),
				0.0F, 1.0F);
		final double var6 = MathHelper.clamp_float(getFloatRainfall(), 0.0F,
				1.0F);
		return ColorizerGrass.getGrassColor(var4, var6);
	}

	/**
	 * Returns true if the biome have snowfall instead a normal rain.
	 */
	public boolean getEnableSnow() {
		return func_150559_j();
	}

	/**
	 * Gets a floating point representation of this biome's rainfall
	 */
	public final float getFloatRainfall() {
		return rainfall;
	}

	/**
	 * Gets a floating point representation of this biome's temperature
	 */
	public final float getFloatTemperature(int p_150564_1_, int p_150564_2_,
			int p_150564_3_) {
		if (p_150564_2_ > 64) {
			final float var4 = (float) field_150605_ac.func_151601_a(
					p_150564_1_ * 1.0D / 8.0D, p_150564_3_ * 1.0D / 8.0D) * 4.0F;
			return temperature - (var4 + p_150564_2_ - 64.0F) * 0.05F / 30.0F;
		} else
			return temperature;
	}

	/**
	 * Gets an integer representation of this biome's rainfall
	 */
	public final int getIntRainfall() {
		return (int) (rainfall * 65536.0F);
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	public WorldGenerator getRandomWorldGenForGrass(Random p_76730_1_) {
		return new WorldGenTallGrass(Blocks.tallgrass, 1);
	}

	/**
	 * takes temperature, returns color
	 */
	public int getSkyColorByTemp(float p_76731_1_) {
		p_76731_1_ /= 3.0F;

		if (p_76731_1_ < -1.0F) {
			p_76731_1_ = -1.0F;
		}

		if (p_76731_1_ > 1.0F) {
			p_76731_1_ = 1.0F;
		}

		return Color.getHSBColor(0.62222224F - p_76731_1_ * 0.05F,
				0.5F + p_76731_1_ * 0.1F, 1.0F).getRGB();
	}

	/**
	 * Returns the correspondent list of the EnumCreatureType informed.
	 */
	public List getSpawnableList(EnumCreatureType p_76747_1_) {
		return p_76747_1_ == EnumCreatureType.monster ? spawnableMonsterList
				: p_76747_1_ == EnumCreatureType.creature ? spawnableCreatureList
						: p_76747_1_ == EnumCreatureType.waterCreature ? spawnableWaterCreatureList
								: p_76747_1_ == EnumCreatureType.ambient ? spawnableCaveCreatureList
										: null;
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	public float getSpawningChance() {
		return 0.1F;
	}

	/**
	 * Checks to see if the rainfall level of the biome is extremely high
	 */
	public boolean isHighHumidity() {
		return rainfall > 0.85F;
	}

	protected BiomeGenBase setBiomeName(String p_76735_1_) {
		biomeName = p_76735_1_;
		return this;
	}

	protected BiomeGenBase setColor(int p_76739_1_) {
		func_150557_a(p_76739_1_, false);
		return this;
	}

	/**
	 * Disable the rain for the biome.
	 */
	protected BiomeGenBase setDisableRain() {
		enableRain = false;
		return this;
	}

	/**
	 * sets enableSnow to true during biome initialization. returns
	 * BiomeGenBase.
	 */
	protected BiomeGenBase setEnableSnow() {
		enableSnow = true;
		return this;
	}

	/**
	 * Sets the temperature and rainfall of this biome.
	 */
	protected BiomeGenBase setTemperatureRainfall(float p_76732_1_,
			float p_76732_2_) {
		if (p_76732_1_ > 0.1F && p_76732_1_ < 0.2F)
			throw new IllegalArgumentException(
					"Please avoid temperatures in the range 0.1 - 0.2 because of snow");
		else {
			temperature = p_76732_1_;
			rainfall = p_76732_2_;
			return this;
		}
	}
}
