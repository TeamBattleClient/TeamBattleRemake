package net.minecraft.entity.passive;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityVillager extends EntityAgeable implements IMerchant, INpc {
	/** Selling list of Blacksmith items. */
	private static final Map blacksmithSellingList = new HashMap();

	/** Selling list of Villagers items. */
	private static final Map villagersSellingList = new HashMap();

	static {
		villagersSellingList.put(Items.coal, new Tuple(Integer.valueOf(16),
				Integer.valueOf(24)));
		villagersSellingList.put(Items.iron_ingot, new Tuple(
				Integer.valueOf(8), Integer.valueOf(10)));
		villagersSellingList.put(Items.gold_ingot, new Tuple(
				Integer.valueOf(8), Integer.valueOf(10)));
		villagersSellingList.put(Items.diamond, new Tuple(Integer.valueOf(4),
				Integer.valueOf(6)));
		villagersSellingList.put(Items.paper, new Tuple(Integer.valueOf(24),
				Integer.valueOf(36)));
		villagersSellingList.put(Items.book, new Tuple(Integer.valueOf(11),
				Integer.valueOf(13)));
		villagersSellingList.put(Items.written_book,
				new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
		villagersSellingList.put(Items.ender_pearl,
				new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		villagersSellingList.put(Items.ender_eye, new Tuple(Integer.valueOf(2),
				Integer.valueOf(3)));
		villagersSellingList.put(Items.porkchop, new Tuple(Integer.valueOf(14),
				Integer.valueOf(18)));
		villagersSellingList.put(Items.beef, new Tuple(Integer.valueOf(14),
				Integer.valueOf(18)));
		villagersSellingList.put(Items.chicken, new Tuple(Integer.valueOf(14),
				Integer.valueOf(18)));
		villagersSellingList.put(Items.cooked_fished,
				new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
		villagersSellingList.put(Items.wheat_seeds,
				new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
		villagersSellingList.put(Items.melon_seeds,
				new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		villagersSellingList.put(Items.pumpkin_seeds,
				new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		villagersSellingList.put(Items.wheat, new Tuple(Integer.valueOf(18),
				Integer.valueOf(22)));
		villagersSellingList.put(Item.getItemFromBlock(Blocks.wool), new Tuple(
				Integer.valueOf(14), Integer.valueOf(22)));
		villagersSellingList.put(Items.rotten_flesh,
				new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
		blacksmithSellingList.put(Items.flint_and_steel,
				new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.shears, new Tuple(Integer.valueOf(3),
				Integer.valueOf(4)));
		blacksmithSellingList.put(Items.iron_sword,
				new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
		blacksmithSellingList.put(Items.diamond_sword,
				new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
		blacksmithSellingList.put(Items.iron_axe, new Tuple(Integer.valueOf(6),
				Integer.valueOf(8)));
		blacksmithSellingList.put(Items.diamond_axe,
				new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
		blacksmithSellingList.put(Items.iron_pickaxe,
				new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
		blacksmithSellingList.put(Items.diamond_pickaxe,
				new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		blacksmithSellingList.put(Items.iron_shovel,
				new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_shovel,
				new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_hoe, new Tuple(Integer.valueOf(4),
				Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_hoe,
				new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_boots,
				new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_boots,
				new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_helmet,
				new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_helmet,
				new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_chestplate,
				new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
		blacksmithSellingList.put(Items.diamond_chestplate,
				new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
		blacksmithSellingList.put(Items.iron_leggings,
				new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		blacksmithSellingList.put(Items.diamond_leggings,
				new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
		blacksmithSellingList.put(Items.chainmail_boots,
				new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		blacksmithSellingList.put(Items.chainmail_helmet,
				new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		blacksmithSellingList.put(Items.chainmail_chestplate,
				new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
		blacksmithSellingList.put(Items.chainmail_leggings,
				new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
		blacksmithSellingList.put(Items.bread, new Tuple(Integer.valueOf(-4),
				Integer.valueOf(-2)));
		blacksmithSellingList.put(Items.melon, new Tuple(Integer.valueOf(-8),
				Integer.valueOf(-4)));
		blacksmithSellingList.put(Items.apple, new Tuple(Integer.valueOf(-8),
				Integer.valueOf(-4)));
		blacksmithSellingList.put(Items.cookie, new Tuple(Integer.valueOf(-10),
				Integer.valueOf(-7)));
		blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glass),
				new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
		blacksmithSellingList.put(Item.getItemFromBlock(Blocks.bookshelf),
				new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.leather_chestplate,
				new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
		blacksmithSellingList.put(Items.leather_boots,
				new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.leather_helmet,
				new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.leather_leggings,
				new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.saddle, new Tuple(Integer.valueOf(6),
				Integer.valueOf(8)));
		blacksmithSellingList.put(Items.experience_bottle,
				new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
		blacksmithSellingList.put(Items.redstone, new Tuple(
				Integer.valueOf(-4), Integer.valueOf(-1)));
		blacksmithSellingList.put(Items.compass, new Tuple(Integer.valueOf(10),
				Integer.valueOf(12)));
		blacksmithSellingList.put(Items.clock, new Tuple(Integer.valueOf(10),
				Integer.valueOf(12)));
		blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glowstone),
				new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
		blacksmithSellingList.put(Items.cooked_porkchop,
				new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		blacksmithSellingList.put(Items.cooked_beef,
				new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		blacksmithSellingList.put(Items.cooked_chicken,
				new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
		blacksmithSellingList.put(Items.ender_eye, new Tuple(
				Integer.valueOf(7), Integer.valueOf(11)));
		blacksmithSellingList.put(Items.arrow, new Tuple(Integer.valueOf(-12),
				Integer.valueOf(-8)));
	}

	private static ItemStack func_146088_a(Item p_146088_0_, Random p_146088_1_) {
		return new ItemStack(p_146088_0_, func_146092_b(p_146088_0_,
				p_146088_1_), 0);
	}

	private static void func_146089_b(MerchantRecipeList p_146089_0_,
			Item p_146089_1_, Random p_146089_2_, float p_146089_3_) {
		if (p_146089_2_.nextFloat() < p_146089_3_) {
			final int var4 = func_146090_c(p_146089_1_, p_146089_2_);
			ItemStack var5;
			ItemStack var6;

			if (var4 < 0) {
				var5 = new ItemStack(Items.emerald, 1, 0);
				var6 = new ItemStack(p_146089_1_, -var4, 0);
			} else {
				var5 = new ItemStack(Items.emerald, var4, 0);
				var6 = new ItemStack(p_146089_1_, 1, 0);
			}

			p_146089_0_.add(new MerchantRecipe(var5, var6));
		}
	}

	private static int func_146090_c(Item p_146090_0_, Random p_146090_1_) {
		final Tuple var2 = (Tuple) blacksmithSellingList.get(p_146090_0_);
		return var2 == null ? 1
				: ((Integer) var2.getFirst()).intValue() >= ((Integer) var2
						.getSecond()).intValue() ? ((Integer) var2.getFirst())
						.intValue() : ((Integer) var2.getFirst()).intValue()
						+ p_146090_1_.nextInt(((Integer) var2.getSecond())
								.intValue()
								- ((Integer) var2.getFirst()).intValue());
	}

	private static void func_146091_a(MerchantRecipeList p_146091_0_,
			Item p_146091_1_, Random p_146091_2_, float p_146091_3_) {
		if (p_146091_2_.nextFloat() < p_146091_3_) {
			p_146091_0_.add(new MerchantRecipe(func_146088_a(p_146091_1_,
					p_146091_2_), Items.emerald));
		}
	}

	private static int func_146092_b(Item p_146092_0_, Random p_146092_1_) {
		final Tuple var2 = (Tuple) villagersSellingList.get(p_146092_0_);
		return var2 == null ? 1
				: ((Integer) var2.getFirst()).intValue() >= ((Integer) var2
						.getSecond()).intValue() ? ((Integer) var2.getFirst())
						.intValue() : ((Integer) var2.getFirst()).intValue()
						+ p_146092_1_.nextInt(((Integer) var2.getSecond())
								.intValue()
								- ((Integer) var2.getFirst()).intValue());
	}

	/** Initialises the MerchantRecipeList.java */
	private MerchantRecipeList buyingList;

	/** This villager's current customer. */
	private EntityPlayer buyingPlayer;
	private float field_82191_bN;
	private boolean isLookingForHome;

	private boolean isMating;

	private boolean isPlaying;

	/** Last player to trade with this villager, used for aggressivity. */
	private String lastBuyingPlayer;

	/** addDefaultEquipmentAndRecipies is called if this is true */
	private boolean needsInitilization;

	private int randomTickDivider;

	private int timeUntilReset;

	Village villageObj;

	private int wealth;

	public EntityVillager(World p_i1747_1_) {
		this(p_i1747_1_, 0);
	}

	public EntityVillager(World p_i1748_1_, int p_i1748_2_) {
		super(p_i1748_1_);
		setProfession(p_i1748_2_);
		setSize(0.6F, 1.8F);
		getNavigator().setBreakDoors(true);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class,
				8.0F, 0.6D, 0.6D));
		tasks.addTask(1, new EntityAITradePlayer(this));
		tasks.addTask(1, new EntityAILookAtTradePlayer(this));
		tasks.addTask(2, new EntityAIMoveIndoors(this));
		tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		tasks.addTask(6, new EntityAIVillagerMate(this));
		tasks.addTask(7, new EntityAIFollowGolem(this));
		tasks.addTask(8, new EntityAIPlay(this, 0.32D));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class,
				3.0F, 1.0F));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class,
				5.0F, 0.02F));
		tasks.addTask(9, new EntityAIWander(this, 0.6D));
		tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class,
				8.0F));
	}

	/**
	 * based on the villagers profession add items, equipment, and recipies adds
	 * par1 random items to the list of things that the villager wants to buy.
	 * (at most 1 of each wanted type is added)
	 */
	private void addDefaultEquipmentAndRecipies(int p_70950_1_) {
		if (buyingList != null) {
			field_82191_bN = MathHelper.sqrt_float(buyingList.size()) * 0.2F;
		} else {
			field_82191_bN = 0.0F;
		}

		MerchantRecipeList var2;
		var2 = new MerchantRecipeList();
		int var6;
		label50:

		switch (getProfession()) {
		case 0:
			func_146091_a(var2, Items.wheat, rand, adjustProbability(0.9F));
			func_146091_a(var2, Item.getItemFromBlock(Blocks.wool), rand,
					adjustProbability(0.5F));
			func_146091_a(var2, Items.chicken, rand, adjustProbability(0.5F));
			func_146091_a(var2, Items.cooked_fished, rand,
					adjustProbability(0.4F));
			func_146089_b(var2, Items.bread, rand, adjustProbability(0.9F));
			func_146089_b(var2, Items.melon, rand, adjustProbability(0.3F));
			func_146089_b(var2, Items.apple, rand, adjustProbability(0.3F));
			func_146089_b(var2, Items.cookie, rand, adjustProbability(0.3F));
			func_146089_b(var2, Items.shears, rand, adjustProbability(0.3F));
			func_146089_b(var2, Items.flint_and_steel, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.cooked_chicken, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.arrow, rand, adjustProbability(0.5F));

			if (rand.nextFloat() < adjustProbability(0.5F)) {
				var2.add(new MerchantRecipe(new ItemStack(Blocks.gravel, 10),
						new ItemStack(Items.emerald), new ItemStack(
								Items.flint, 4 + rand.nextInt(2), 0)));
			}

			break;

		case 1:
			func_146091_a(var2, Items.paper, rand, adjustProbability(0.8F));
			func_146091_a(var2, Items.book, rand, adjustProbability(0.8F));
			func_146091_a(var2, Items.written_book, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Item.getItemFromBlock(Blocks.bookshelf), rand,
					adjustProbability(0.8F));
			func_146089_b(var2, Item.getItemFromBlock(Blocks.glass), rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.compass, rand, adjustProbability(0.2F));
			func_146089_b(var2, Items.clock, rand, adjustProbability(0.2F));

			if (rand.nextFloat() < adjustProbability(0.07F)) {
				final Enchantment var8 = Enchantment.enchantmentsBookList[rand
						.nextInt(Enchantment.enchantmentsBookList.length)];
				final int var10 = MathHelper.getRandomIntegerInRange(rand,
						var8.getMinLevel(), var8.getMaxLevel());
				final ItemStack var11 = Items.enchanted_book
						.getEnchantedItemStack(new EnchantmentData(var8, var10));
				var6 = 2 + rand.nextInt(5 + var10 * 10) + 3 * var10;
				var2.add(new MerchantRecipe(new ItemStack(Items.book),
						new ItemStack(Items.emerald, var6), var11));
			}

			break;

		case 2:
			func_146089_b(var2, Items.ender_eye, rand, adjustProbability(0.3F));
			func_146089_b(var2, Items.experience_bottle, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.redstone, rand, adjustProbability(0.4F));
			func_146089_b(var2, Item.getItemFromBlock(Blocks.glowstone), rand,
					adjustProbability(0.3F));
			final Item[] var3 = new Item[] { Items.iron_sword,
					Items.diamond_sword, Items.iron_chestplate,
					Items.diamond_chestplate, Items.iron_axe,
					Items.diamond_axe, Items.iron_pickaxe,
					Items.diamond_pickaxe };
			final Item[] var4 = var3;
			final int var5 = var3.length;
			var6 = 0;

			while (true) {
				if (var6 >= var5) {
					break label50;
				}

				final Item var7 = var4[var6];

				if (rand.nextFloat() < adjustProbability(0.05F)) {
					var2.add(new MerchantRecipe(
							new ItemStack(var7, 1, 0),
							new ItemStack(Items.emerald, 2 + rand.nextInt(3), 0),
							EnchantmentHelper.addRandomEnchantment(rand,
									new ItemStack(var7, 1, 0),
									5 + rand.nextInt(15))));
				}

				++var6;
			}

		case 3:
			func_146091_a(var2, Items.coal, rand, adjustProbability(0.7F));
			func_146091_a(var2, Items.iron_ingot, rand, adjustProbability(0.5F));
			func_146091_a(var2, Items.gold_ingot, rand, adjustProbability(0.5F));
			func_146091_a(var2, Items.diamond, rand, adjustProbability(0.5F));
			func_146089_b(var2, Items.iron_sword, rand, adjustProbability(0.5F));
			func_146089_b(var2, Items.diamond_sword, rand,
					adjustProbability(0.5F));
			func_146089_b(var2, Items.iron_axe, rand, adjustProbability(0.3F));
			func_146089_b(var2, Items.diamond_axe, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.iron_pickaxe, rand,
					adjustProbability(0.5F));
			func_146089_b(var2, Items.diamond_pickaxe, rand,
					adjustProbability(0.5F));
			func_146089_b(var2, Items.iron_shovel, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.diamond_shovel, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.iron_hoe, rand, adjustProbability(0.2F));
			func_146089_b(var2, Items.diamond_hoe, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.iron_boots, rand, adjustProbability(0.2F));
			func_146089_b(var2, Items.diamond_boots, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.iron_helmet, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.diamond_helmet, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.iron_chestplate, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.diamond_chestplate, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.iron_leggings, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.diamond_leggings, rand,
					adjustProbability(0.2F));
			func_146089_b(var2, Items.chainmail_boots, rand,
					adjustProbability(0.1F));
			func_146089_b(var2, Items.chainmail_helmet, rand,
					adjustProbability(0.1F));
			func_146089_b(var2, Items.chainmail_chestplate, rand,
					adjustProbability(0.1F));
			func_146089_b(var2, Items.chainmail_leggings, rand,
					adjustProbability(0.1F));
			break;

		case 4:
			func_146091_a(var2, Items.coal, rand, adjustProbability(0.7F));
			func_146091_a(var2, Items.porkchop, rand, adjustProbability(0.5F));
			func_146091_a(var2, Items.beef, rand, adjustProbability(0.5F));
			func_146089_b(var2, Items.saddle, rand, adjustProbability(0.1F));
			func_146089_b(var2, Items.leather_chestplate, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.leather_boots, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.leather_helmet, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.leather_leggings, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.cooked_porkchop, rand,
					adjustProbability(0.3F));
			func_146089_b(var2, Items.cooked_beef, rand,
					adjustProbability(0.3F));
		}

		if (var2.isEmpty()) {
			func_146091_a(var2, Items.gold_ingot, rand, 1.0F);
		}

		Collections.shuffle(var2);

		if (buyingList == null) {
			buyingList = new MerchantRecipeList();
		}

		for (int var9 = 0; var9 < p_70950_1_ && var9 < var2.size(); ++var9) {
			buyingList.addToListWithCheck((MerchantRecipe) var2.get(var9));
		}
	}

	/**
	 * Adjusts the probability of obtaining a given recipe being offered by a
	 * villager
	 */
	private float adjustProbability(float p_82188_1_) {
		final float var2 = p_82188_1_ + field_82191_bN;
		return var2 > 0.9F ? 0.9F - (var2 - 0.9F) : var2;
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.5D);
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public EntityVillager createChild(EntityAgeable p_90011_1_) {
		final EntityVillager var2 = new EntityVillager(worldObj);
		var2.onSpawnWithEgg((IEntityLivingData) null);
		return var2;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}

	@Override
	public void func_110297_a_(ItemStack p_110297_1_) {
		if (!worldObj.isClient && livingSoundTime > -getTalkInterval() + 20) {
			livingSoundTime = -getTalkInterval();

			if (p_110297_1_ != null) {
				playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
			} else {
				playSound("mob.villager.no", getSoundVolume(), getSoundPitch());
			}
		}
	}

	/**
	 * par1 is the particleName
	 */
	private void generateRandomParticles(String p_70942_1_) {
		for (int var2 = 0; var2 < 5; ++var2) {
			final double var3 = rand.nextGaussian() * 0.02D;
			final double var5 = rand.nextGaussian() * 0.02D;
			final double var7 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(p_70942_1_, posX + rand.nextFloat() * width
					* 2.0F - width, posY + 1.0D + rand.nextFloat() * height,
					posZ + rand.nextFloat() * width * 2.0F - width, var3, var5,
					var7);
		}
	}

	@Override
	public EntityPlayer getCustomer() {
		return buyingPlayer;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.villager.death";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.villager.hit";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
	}

	public int getProfession() {
		return dataWatcher.getWatchableObjectInt(16);
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
		if (buyingList == null) {
			addDefaultEquipmentAndRecipies(1);
		}

		return buyingList;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 12) {
			generateRandomParticles("heart");
		} else if (p_70103_1_ == 13) {
			generateRandomParticles("angryVillager");
		} else if (p_70103_1_ == 14) {
			generateRandomParticles("happyVillager");
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
		final boolean var3 = var2 != null && var2.getItem() == Items.spawn_egg;

		if (!var3 && isEntityAlive() && !isTrading() && !isChild()) {
			if (!worldObj.isClient) {
				setCustomer(p_70085_1_);
				p_70085_1_.displayGUIMerchant(this, getCustomNameTag());
			}

			return true;
		} else
			return super.interact(p_70085_1_);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	public boolean isMating() {
		return isMating;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public boolean isTrading() {
		return buyingPlayer != null;
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		if (villageObj != null) {
			final Entity var2 = p_70645_1_.getEntity();

			if (var2 != null) {
				if (var2 instanceof EntityPlayer) {
					villageObj.setReputationForPlayer(
							var2.getCommandSenderName(), -2);
				} else if (var2 instanceof IMob) {
					villageObj.endMatingSeason();
				}
			} else if (var2 == null) {
				final EntityPlayer var3 = worldObj.getClosestPlayerToEntity(
						this, 16.0D);

				if (var3 != null) {
					villageObj.endMatingSeason();
				}
			}
		}

		super.onDeath(p_70645_1_);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
		setProfession(worldObj.rand.nextInt(5));
		return p_110161_1_;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setProfession(p_70037_1_.getInteger("Profession"));
		wealth = p_70037_1_.getInteger("Riches");

		if (p_70037_1_.func_150297_b("Offers", 10)) {
			final NBTTagCompound var2 = p_70037_1_.getCompoundTag("Offers");
			buyingList = new MerchantRecipeList(var2);
		}
	}

	@Override
	public void setCustomer(EntityPlayer p_70932_1_) {
		buyingPlayer = p_70932_1_;
	}

	public void setLookingForHome() {
		isLookingForHome = true;
	}

	public void setMating(boolean p_70947_1_) {
		isMating = p_70947_1_;
	}

	public void setPlaying(boolean p_70939_1_) {
		isPlaying = p_70939_1_;
	}

	public void setProfession(int p_70938_1_) {
		dataWatcher.updateObject(16, Integer.valueOf(p_70938_1_));
	}

	@Override
	public void setRecipes(MerchantRecipeList p_70930_1_) {
	}

	@Override
	public void setRevengeTarget(EntityLivingBase p_70604_1_) {
		super.setRevengeTarget(p_70604_1_);

		if (villageObj != null && p_70604_1_ != null) {
			villageObj.addOrRenewAgressor(p_70604_1_);

			if (p_70604_1_ instanceof EntityPlayer) {
				byte var2 = -1;

				if (isChild()) {
					var2 = -3;
				}

				villageObj.setReputationForPlayer(
						p_70604_1_.getCommandSenderName(), var2);

				if (isEntityAlive()) {
					worldObj.setEntityState(this, (byte) 13);
				}
			}
		}
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick() {
		if (--randomTickDivider <= 0) {
			worldObj.villageCollectionObj.addVillagerPosition(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ));
			randomTickDivider = 70 + rand.nextInt(50);
			villageObj = worldObj.villageCollectionObj.findNearestVillage(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ), 32);

			if (villageObj == null) {
				detachHome();
			} else {
				final ChunkCoordinates var1 = villageObj.getCenter();
				setHomeArea(var1.posX, var1.posY, var1.posZ,
						(int) (villageObj.getVillageRadius() * 0.6F));

				if (isLookingForHome) {
					isLookingForHome = false;
					villageObj.setDefaultPlayerReputation(5);
				}
			}
		}

		if (!isTrading() && timeUntilReset > 0) {
			--timeUntilReset;

			if (timeUntilReset <= 0) {
				if (needsInitilization) {
					if (buyingList.size() > 1) {
						final Iterator var3 = buyingList.iterator();

						while (var3.hasNext()) {
							final MerchantRecipe var2 = (MerchantRecipe) var3
									.next();

							if (var2.isRecipeDisabled()) {
								var2.func_82783_a(rand.nextInt(6)
										+ rand.nextInt(6) + 2);
							}
						}
					}

					addDefaultEquipmentAndRecipies(1);
					needsInitilization = false;

					if (villageObj != null && lastBuyingPlayer != null) {
						worldObj.setEntityState(this, (byte) 14);
						villageObj.setReputationForPlayer(lastBuyingPlayer, 1);
					}
				}

				addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
			}
		}

		super.updateAITick();
	}

	@Override
	public void useRecipe(MerchantRecipe p_70933_1_) {
		p_70933_1_.incrementToolUses();
		livingSoundTime = -getTalkInterval();
		playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());

		if (p_70933_1_.hasSameIDsAs((MerchantRecipe) buyingList.get(buyingList
				.size() - 1))) {
			timeUntilReset = 40;
			needsInitilization = true;

			if (buyingPlayer != null) {
				lastBuyingPlayer = buyingPlayer.getCommandSenderName();
			} else {
				lastBuyingPlayer = null;
			}
		}

		if (p_70933_1_.getItemToBuy().getItem() == Items.emerald) {
			wealth += p_70933_1_.getItemToBuy().stackSize;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Profession", getProfession());
		p_70014_1_.setInteger("Riches", wealth);

		if (buyingList != null) {
			p_70014_1_.setTag("Offers", buyingList.getRecipiesAsTags());
		}
	}
}
